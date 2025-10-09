package content.region.misthalin.lumbridge.quest.tog.plugin

import content.region.misthalin.lumbridge.quest.tog.npc.LightCreatureBehavior
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery

class TearsOfGuthixListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles quest dialogue with Juna NPC.
         */

        on(Scenery.JUNA_31302, SCENERY, "talk-to") { player, node ->
            openDialogue(player, NPCs.JUNA_2023, node.location)
            return@on true
        }

        /*
         * Handles talk with Juna NPC after quest.
         */

        on(Scenery.JUNA_31303, SCENERY, "talk-to", "tell-story") { player, node ->
            openDialogue(player, NPCs.JUNA_2023, node.location)
            return@on true
        }

        on(Scenery.ROCKS_6673, SCENERY, "climb") { player, _ ->
            val dx = if (player.location.x > 3240) -2 else 2
            val destination = Location.create(player.location).transform(dx, 0, 0)

            ForceMovement.run(
                player,
                player.location,
                destination,
                Animation(1148),
                Animation(1148),
                Direction.WEST,
                13
            ).endAnimation = Animation.RESET

            return@on true
        }

        on(Scenery.ROCKS_6672, SCENERY, "climb") { player, _ ->
            if (player.location.x > 3239) {
                sendMessage(player, "You could climb down here, but it is too uneven to climb up.")
            } else {
                val destination = Location.create(player.location).transform(2, 0, 0)
                ForceMovement.run(
                    player,
                    player.location,
                    destination,
                    Animation(1148),
                    Animation(1148),
                    Direction.WEST,
                    13
                ).endAnimation = Animation.RESET
                sendMessage(player, "You leap across with a mighty leap!")
            }
            return@on true
        }

        // Please note: part of this is already done in craftBullseyeLantern() except for the swapping out which is here.
        onUseWith(ITEM, Items.BULLSEYE_LANTERN_4548, Items.SAPPHIRE_1607) { player, used, with ->
            sendMessage(player, "You swap the lantern's lens for a sapphire.")
            if (removeItem(player, with) && removeItem(player, used)) {
                addItemOrDrop(player, Items.SAPPHIRE_LANTERN_4701)
                addItemOrDrop(player, Items.LANTERN_LENS_4542)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.SAPPHIRE_LANTERN_4701, Items.LANTERN_LENS_4542) { player, used, with ->
            if (removeItem(player, with) && removeItem(player, used)) {
                addItemOrDrop(player, Items.BULLSEYE_LANTERN_4548)
                addItemOrDrop(player, Items.SAPPHIRE_1607)
                sendMessage(player, "You swap the lantern's sapphire for a lens.")
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.SAPPHIRE_1607, Items.BULLSEYE_LANTERN_4549, Items.BULLSEYE_LANTERN_4550, Items.LANTERN_LENS_4542) { player, _, _ ->
            sendMessage(player, "The lantern is too hot to do that while it is lit.")
            return@onUseWith true
        }

        onUseWith(ITEM, Items.CHISEL_1755, Items.MAGIC_STONE_4703) { player, _, with ->
            if (removeItem(player, with.asItem())) {
                sendMessage(player, "You make a stone bowl.")
                addItemOrDrop(player, Items.STONE_BOWL_4704)
            }
            return@onUseWith true
        }

        onUseWith(NPC, Items.SAPPHIRE_LANTERN_4702, NPCs.LIGHT_CREATURE_2021) { player, _, npc ->
            val target = npc as NPC

            if (!hasRequirement(player, Quests.WHILE_GUTHIX_SLEEPS)) {
                crossTheChasm(player, target)
            } else {
                sendOptions(player, "Select an Option", "Across the Chasm.", "Into the Chasm.")
                addDialogueAction(player) { _, button ->
                    if (button == 2) crossTheChasm(player, target)
                    else player.teleport(Location.create(2538, 5881, 0))
                }
            }

            return@onUseWith true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(NPCs.LIGHT_CREATURE_2021), "use") { player, _ ->
            return@setDest player.location
        }
    }

    companion object {
        /**
         * Handles the player cross the chasm with the help of a light creature.
         */
        fun crossTheChasm(player: Player, npc: NPC) {
            sendMessage(player, "The light-creature is attracted to your beam and comes towards you...")
            LightCreatureBehavior.moveLightCreature(npc, player.location)
            val destination = if (player.location.y > 9516) {
                Location.create(3229, 9504, 2)
            } else {
                Location.create(3228, 9527, 2)
            }
            forceMove(player, player.location, destination, 0, 400, null, 2048)
        }
    }
}

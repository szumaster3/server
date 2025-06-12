package content.region.misthalin.lumbridge.quest.tog.handlers

import core.api.*
import core.api.quest.hasRequirement
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class TearsOfGuthixListener : InteractionListener {
    companion object {
        fun crossTheChasm(
            player: Player,
            with: NPC,
        ) {
            // You have to do the following:
            // 1 - Get the light creature to your location.
            // 2 - Animate both you and the light creature to float up.
            // 3 - Walk both YOU AND THE LIGHT CREATURE to the other side.
            // 4 - Float both you and the light creature to the ground.
            //
            // 2046 - Magically float into the air
            // 2047 - Magically float back to the ground
            // 2048 - Keep floating in the air

            val lightCreature = with as NPC
            sendMessage(player, "The light-creature is attracted to your beam and comes towards you...")
            LightCreatureBehavior.moveLightCreature(lightCreature, player.location)
            // Could also do player.appearance.setAnimations(Animation(913)) which is the group animation for floating.
            if (player.location.y > 9516) {
                forceMove(player, player.location, Location.create(3229, 9504, 2), 0, 400, null, 2048)
            } else {
                forceMove(player, player.location, Location.create(3228, 9527, 2), 0, 400, null, 2048)
            }
        }
    }

    override fun defineListeners() {
        on(Scenery.JUNA_31302, SCENERY, "talk-to") { player, node ->
            openDialogue(player, NPCs.JUNA_2023, node.location)
            return@on true
        }

        // Quest complete
        on(Scenery.JUNA_31303, SCENERY, "talk-to", "tell-story") { player, node ->
            openDialogue(player, NPCs.JUNA_2023, node.location)
            return@on true
        }

        on(Scenery.ROCKS_6673, SCENERY, "climb") { player, _ ->
            if (player.location.x > 3240) {
                ForceMovement
                    .run(
                        player,
                        player.location,
                        Location.create(player.location).transform(-2, 0, 0),
                        Animation(1148),
                        Animation(1148),
                        Direction.WEST,
                        13,
                    ).endAnimation =
                    Animation.RESET
            } else {
                ForceMovement
                    .run(
                        player,
                        player.location,
                        Location.create(player.location).transform(2, 0, 0),
                        Animation(1148),
                        Animation(1148),
                        Direction.WEST,
                        13,
                    ).endAnimation =
                    Animation.RESET
            }
            return@on true
        }

        on(Scenery.ROCKS_6672, SCENERY, "climb") { player, _ ->
            if (player.location.x > 3239) {
                sendMessage(player, "You could climb down here, but it is too uneven to climb up.")
            } else {
                ForceMovement
                    .run(
                        player,
                        player.location,
                        Location.create(player.location).transform(2, 0, 0),
                        Animation(1148),
                        Animation(1148),
                        Direction.WEST,
                        13,
                    ).endAnimation =
                    Animation.RESET
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

        onUseWith(ITEM, Items.SAPPHIRE_1607, Items.BULLSEYE_LANTERN_4549) { player, _, _ ->
            sendMessage(player, "The lantern is too hot to do that while it is lit.")
            return@onUseWith true
        }

        onUseWith(ITEM, Items.SAPPHIRE_1607, Items.BULLSEYE_LANTERN_4550) { player, _, _ ->
            sendMessage(player, "The lantern is too hot to do that while it is lit.")
            return@onUseWith true
        }

        onUseWith(ITEM, Items.SAPPHIRE_LANTERN_4702, Items.LANTERN_LENS_4542) { player, _, _ ->
            sendMessage(player, "The lantern is too hot to do that while it is lit.")
            return@onUseWith true
        }

        onUseWith(ITEM, Items.MAGIC_STONE_4703, Items.CHISEL_1755) { player, used, _ ->
            sendMessage(player, "You make a stone bowl.")
            if (removeItem(player, used)) {
                addItemOrDrop(player, Items.STONE_BOWL_4704)
            }
            return@onUseWith true
        }

        onUseWith(NPC, Items.SAPPHIRE_LANTERN_4702, NPCs.LIGHT_CREATURE_2021) { player, _, with ->
            if (!hasRequirement(player, Quests.WHILE_GUTHIX_SLEEPS)) {
                crossTheChasm(player, with as NPC)
            } else {
                // Options when you have WGS - B6KHH7AQc2Q
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(
                            componentID: Int,
                            buttonID: Int,
                        ) {
                            when (stage) {
                                0 ->
                                    interpreter!!
                                        .sendOptions(
                                            "Select an Option",
                                            "Across the Chasm.",
                                            "Into the Chasm.",
                                        ).also { stage++ }
                                1 ->
                                    when (buttonID) {
                                        1 -> {
                                            crossTheChasm(player, with as NPC)
                                            end()
                                        }
                                        2 -> {
                                            player.lock(2)
                                            player.teleport(Location.create(2538, 5881, 0))
                                            end()
                                        }
                                    }
                            }
                        }
                    },
                )
            }
            return@onUseWith true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(NPCs.LIGHT_CREATURE_2021), "use") { player, _ ->
            return@setDest player.location
        }
    }
}

package content.region.island.entrana.plugin

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.bots.AIPlayer
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.repository.Repository.findNPC
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.*

class EntranaPlugin : InteractionListener, MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(
        getRegionBorders(11060), getRegionBorders(11316)
    )

    override fun areaEnter(entity: Entity) {
        if (entity is Player && entity !is AIPlayer && entity.details.rights != Rights.ADMINISTRATOR) {
            if (!ItemDefinition.canEnterEntrana(entity)) {
                kickThemOut(entity)
            }
        }
    }

    override fun defineListeners() {

        /*
         * Handles searching the bookcase in the house near Fritz on Entrana.
         */

        on(Scenery.BOOKCASE_33964, IntType.SCENERY, "Search") { player, _ ->
            sendMessage(player, "You search the bookcase...")

            if (inInventory(player, Items.GLASSBLOWING_BOOK_11656)) {
                sendMessage(player, "You don't find anything that you'd ever want to read.")
                return@on true
            }

            if (freeSlots(player) == 0) {
                sendMessage(player, "You find a 'Glassblowing Book', but you don't have enough room to take it.")
            } else {
                sendMessage(player, "You find a 'Glassblowing Book'.")
                addItem(player, Items.GLASSBLOWING_BOOK_11656, 1)
            }

            return@on true
        }

        /*
         * Handles entering the Entrana dungeon (Lost City quest).
         */

        on(Scenery.LADDER_2408, IntType.SCENERY, "climb-down") { player, _ ->
            openDialogue(player, NPCs.CAVE_MONK_656, findNPC(NPCs.CAVE_MONK_656)!!.asNpc())
            return@on true
        }

        /*
         * Handles exit from the Entrana dungeon.
         */

        on(Scenery.MAGIC_DOOR_2407, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "You feel the world around you dissolve...")
            teleport(player, Location(3093, 3224, 0), TeleportManager.TeleportType.ENTRANA_MAGIC_DOOR)
            return@on true
        }

        /*
         * Handles talking to NPC.
         */

        on(NPCs.MONK_222, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Greetings traveller.")
            return@on true
        }

        val dialogues = listOf(
            "Nice weather we're having today!",
            "Please leave me alone, a parrot stole my banana."
        )

        on(NPCs.MAZION_3114, IntType.NPC, "talk-to") { player, node ->
            val randomDialogue = (1..3).random()
            when (randomDialogue) {
                1 -> sendNPCDialogue(player, node.id, dialogues[0])
                2 -> sendNPCDialogue(player, node.id, "Hello ${player.name}, fine day today!")
                3 -> sendNPCDialogue(player, node.id, dialogues[1])
            }
            return@on true
        }

        on(NPCs.FRINCOS_578, IntType.NPC, "talk-to") { player, node ->
            dialogue(player) {
                npc(node.id, FaceAnim.HALF_GUILTY, "Hello, how can I help you?")
                options(null, "What are you selling?", "You can't; I'm beyond help.", "I'm okay, thank you.") { selected ->
                    when (selected) {
                        1 -> player(FaceAnim.HALF_GUILTY, "What are you selling?").also {
                            openNpcShop(player, node.id)
                        }
                        2 -> player(FaceAnim.HALF_GUILTY, "You can't; I'm beyond help.")
                        3 -> player(FaceAnim.HALF_GUILTY, "I'm okay, thank you.")
                    }
                }
            }
            return@on true
        }

        on(NPCs.CRONE_217, IntType.NPC, "talk-to") { player, node ->
            dialogue(player) {
                npc(node.id, FaceAnim.HALF_GUILTY, "Hello deary.")
                player(FaceAnim.HALF_GUILTY, "Um... hello.")
            }
            return@on true
        }
    }

    private fun kickThemOut(entity: Player) {
        val monk = core.game.node.entity.npc.NPC(NPCs.MONK_4608).apply {
            isNeverWalks = true
            isWalks = false
            location = entity.location
            init()
        }

        entity.lock(10)
        sendGraphics(Graphics.create(86, 96), monk.location)

        sendChat(monk, "Hey, ${entity.asPlayer().username}, get that stuff off our island!")
        sendMessage(entity.asPlayer(), "A passing monk objects to the items you are carrying!")

        runTask(monk, 1) {
            monk.moveStep()
            monk.face(entity)
            monk.animate(Animation.create(442))
            entity.asPlayer().animator.animate(Animation(Animations.DEATH_836))
            openInterface(entity.asPlayer(), Components.FADE_TO_BLACK_115)
            GameWorld.Pulser.submit(
                object : Pulse(3) {
                    override fun pulse(): Boolean {
                        if (getAttribute(entity, "teleporting-away", false)) return true
                        teleport(entity.asPlayer(), Location.create(3047, 3231, 1))
                        openInterface(entity.asPlayer(), Components.FADE_FROM_BLACK_170)
                        entity.asPlayer().animator.animate(Animation(Animations.DEATH_4200))
                        sendGraphics(
                            Graphics(org.rs.consts.Graphics.STUN_BIRDIES_ABOVE_HEAD_80, 96), entity.asPlayer().location
                        )
                        sendChat(entity.asPlayer(), "Urrrrrgh!", 6)
                        if (!entity.isActive) poofClear(monk)
                        return !monk.isActive || !entity.isActive
                    }
                },
            )
        }
    }
}
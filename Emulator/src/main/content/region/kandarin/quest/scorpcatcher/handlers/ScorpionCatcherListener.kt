package content.region.kandarin.quest.scorpcatcher.handlers

import content.region.kandarin.quest.scorpcatcher.ScorpionCatcher
import content.region.kandarin.quest.scorpcatcher.dialogue.SeersDialogueFile
import content.region.kandarin.quest.scorpcatcher.dialogue.ThormacDialogueFile
import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.config.NPCConfigParser
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.tools.Log
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class ScorpionCatcherListener : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles jail doors.
         */

        on(Scenery.DOOR_31838, IntType.SCENERY, "open") { player, node ->
            if(!inInventory(player, Items.JAIL_KEY_1591, 1)) {
                player.packetDispatch.sendMessage("The doors won't open.")
                return@on true
            }

            player.packetDispatch.sendMessage("You unlock the door.")
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            if(player.location.y < 9690 || player.location.y > 9694) {
                player.packetDispatch.sendMessage("The door locks shut behind you.")
            }
            return@on true
        }

        /*
         * Handles interaction with old wall.
         */

        on(Scenery.OLD_WALL_2117, IntType.SCENERY, "search") { player, node ->
            if (inInventory(player, Items.DUSTY_KEY_1590, 1) && getAttribute(player, ScorpionCatcher.ATTRIBUTE_SECRET, false)) {
                if(player.location.y > 9798) {
                    sendMessage(player, "You've found a secret door.")
                }
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                return@on true
            }
            return@on false
        }

        /*
         * Handles catching interaction.
         */

        val scorpionAttributes = mapOf(
            NPCs.KHARID_SCORPION_385 to ScorpionCatcher.ATTRIBUTE_BARBARIAN,
            NPCs.KHARID_SCORPION_386 to ScorpionCatcher.ATTRIBUTE_TAVERLEY,
            NPCs.KHARID_SCORPION_387 to ScorpionCatcher.ATTRIBUTE_MONK
        )
        val cageState = mapOf(
            Items.SCORPION_CAGE_456 to setOf(),
            Items.SCORPION_CAGE_457 to setOf(NPCs.KHARID_SCORPION_386),
            Items.SCORPION_CAGE_458 to setOf(NPCs.KHARID_SCORPION_386, NPCs.KHARID_SCORPION_385),
            Items.SCORPION_CAGE_459 to setOf(NPCs.KHARID_SCORPION_385),
            Items.SCORPION_CAGE_460 to setOf(NPCs.KHARID_SCORPION_385, NPCs.KHARID_SCORPION_387),
            Items.SCORPION_CAGE_461 to setOf(NPCs.KHARID_SCORPION_387),
            Items.SCORPION_CAGE_462 to setOf(NPCs.KHARID_SCORPION_386, NPCs.KHARID_SCORPION_387),
            Items.SCORPION_CAGE_463 to setOf(NPCs.KHARID_SCORPION_386, NPCs.KHARID_SCORPION_385, NPCs.KHARID_SCORPION_387)
        )

        /**
         * Handles the logic for catching a scorpion during the [ScorpionCatcher] quest using a cage item.
         *
         * @param player The player attempting to catch the scorpion.
         * @param item The cage item currently used by the player.
         * @param scorpion The scorpion NPC node the player is trying to catch.
         * @return True if the scorpion was successfully caught and handled; false otherwise.
         */
        fun catchScorpion(player: Player, item: Node, scorpion: Node): Boolean {
            val haveInCage = cageState[item.id] ?: return false
            if (scorpion.id in haveInCage) {
                sendMessage(player, "You already have this scorpion in this cage.")
                return true
            }
            val newScorpionSet = haveInCage + setOf(scorpion.id)
            var newItem: Int? = null
            for ((cage, scorps) in cageState) {
                if (scorps == newScorpionSet) {
                    newItem = cage
                }
            }
            if (newItem == null) {
                log(this::class.java, Log.ERR, "Error looking up new scorpion cage item - this isn't possible")
                return false
            }
            val attribute = scorpionAttributes[scorpion.id]
            if (removeItem(player, Item(item.id, 1)) && addItem(player, newItem)) {
                sendMessage(player, "You catch a scorpion!")
                setAttribute(player, attribute.toString(), true)
                runTask(player, 1) {
                    scorpion.asNpc().respawnTick = GameWorld.ticks + scorpion.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
                }
                return true
            }
            return false
        }

        for (scorpion in scorpionAttributes.keys) {
            for (cage in cageState.keys) {
                onUseWith(IntType.NPC, cage, scorpion) { player, usedCage, usedScorp ->
                    return@onUseWith catchScorpion(player, usedCage, usedScorp)
                }
            }
        }

    }


    companion object {
        /**
         * Starts a sequence where the Seer uses a mirror to reveal the scorpion's location.
         *
         * @param player The player for whom the sequence is being executed.
         */
        fun getScorpionLocation(player: Player) {
            submitIndividualPulse(player, object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        1 -> sendMessage(player, "The seer produces a small mirror.")
                        3 -> sendMessage(player, "The seer gazes into the mirror.")
                        6 -> sendMessage(player, "The seer smoothes his hair with his hand.")
                        9 -> {
                            setAttribute(player, ScorpionCatcher.ATTRIBUTE_MIRROR, true)
                            setAttribute(player, ScorpionCatcher.ATTRIBUTE_SECRET, true)
                            openDialogue(player, SeersDialogueFile())
                            return true
                        }
                    }
                    return false
                }
            })
        }

        /**
         * Gives the player a scorpion cage and starts a short dialogue sequence with Thormac.
         *
         * @param player The player who receives the cage and dialogue interaction.
         */
        fun getCage(player: Player) {
            submitIndividualPulse(player, object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            sendMessage(player, "Thormac gives you a cage.")
                            addItemOrDrop(player, Items.SCORPION_CAGE_456)
                        }
                        2 -> {
                            setAttribute(player, ScorpionCatcher.ATTRIBUTE_CAGE, true)
                            openDialogue(player, ThormacDialogueFile())
                            return true
                        }
                    }
                    return false
                }
            })
        }
    }
}

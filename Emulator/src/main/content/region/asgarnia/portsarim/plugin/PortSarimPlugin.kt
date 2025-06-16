package content.region.asgarnia.portsarim.plugin

import core.api.*
import core.api.quest.getQuestStage
import core.api.ui.closeDialogue
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.*

class PortSarimPlugin : InteractionListener {

    companion object {
        private const val CAVE_ENTRANCE = Scenery.ICY_CAVERN_33174
        private const val CAVE_EXIT = Scenery.CAVE_33173
        private const val SLEEPING_GUARD = NPCs.GUARD_2704
        private const val WORMBRAIN = NPCs.WORMBRAIN_745
        private const val WYDIN_BANANA_CRATE = Scenery.CRATE_2071
        private const val WYDIN_STORE_DOOR = Scenery.DOOR_2069
        private val DOORS = intArrayOf(Scenery.CELL_DOOR_9563, Scenery.DOOR_9565)
        private val MONKS_OF_ENTRANA = intArrayOf(NPCs.MONK_OF_ENTRANA_2728, NPCs.MONK_OF_ENTRANA_657, NPCs.MONK_OF_ENTRANA_2729, 2730, NPCs.MONK_OF_ENTRANA_2731, NPCs.MONK_OF_ENTRANA_658)
        private val SEAMAN = intArrayOf(NPCs.CAPTAIN_TOBIAS_376, NPCs.SEAMAN_LORRIS_377, NPCs.SEAMAN_THRESNOR_378)
    }

    override fun defineListeners() {

        /*
         * Handles taking Ahab's beer from the ground.
         */

        on(Items.AHABS_BEER_6561, IntType.GROUND_ITEM, "take") { player, node ->
            face(player, node)
            animate(player, Animations.HUMAN_MULTI_USE_832)
            player.dialogueInterpreter.open(NPCs.AHAB_2692, findNPC(NPCs.AHAB_2692), false)
            return@on true
        }

        /*
         * Handles paying the fare to the Seaman NPCs.
         */

        on(SEAMAN, IntType.NPC, "pay-fare") { player, node ->
            val npc = node as NPC
            player.dialogueInterpreter.open(npc.id, npc, true)
            return@on true
        }

        /*
         * Handles opening the Wydin store door.
         */

        on(WYDIN_STORE_DOOR, IntType.SCENERY, "open") { player, node ->
            val needsApron = !inEquipment(player, Items.WHITE_APRON_1005) && player.location.x == 3012

            if (needsApron) {
                player.dialogueInterpreter.open(NPCs.WYDIN_557, true, true)
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }

            return@on true
        }

        /*
         * Handles searching the Wydin banana crate.
         */

        on(WYDIN_BANANA_CRATE, IntType.SCENERY, "search") { player, _ ->
            if (freeSlots(player) == 0) {
                sendMessage(player, "Not enough inventory space.")
                return@on true
            }

            lock(player, 2)
            sendMessage(player, "There are lots of bananas in the crate.")

            if (!player.getAttribute("wydin-rum", false)) {
                setTitle(player, 2)
                sendDialogueOptions(player, "Do you want to take a banana?", "Yes.", "No.")
                addDialogueAction(player) { _, option ->
                    closeDialogue(player)
                    if (option == 2) {
                        animate(player, Animations.HUMAN_MULTI_USE_832)
                        addItem(player, Items.BANANA_1963)
                        sendMessage(player, "You take a banana.")
                    }
                }
            } else {
                animate(player, Animations.HUMAN_MULTI_USE_832)
                addItem(player, Items.KARAMJAN_RUM_431)
                removeAttributes(player, "wydin-rum", "stashed-rum")
                sendMessage(player, "You find your bottle of rum in amongst the bananas.")
            }

            return@on true
        }

        /*
         * Handles interacting with doors (open or pick-lock).
         */

        on(DOORS, IntType.SCENERY, "open", "pick-lock") { player, _ ->
            when (getUsedOption(player)) {
                "open" -> sendMessage(player, "The door is securely locked.")
                "pick-lock" -> {
                    if (player.location.y <= 3187) {
                        sendMessage(player, "You simply cannot find a way to pick the lock from this side.")
                    } else {
                        sendMessage(player, "The door is securely locked.")
                    }
                }
            }
            return@on true
        }

        /*
         * Handles taking a boat from the Monks of Entrana.
         */

        on(MONKS_OF_ENTRANA, IntType.NPC, "take-boat") { player, node ->
            openDialogue(player, (node as NPC).id, node)
            return@on true
        }

        /*
         * Handles talking to the sleeping guard NPC.
         */

        on(SLEEPING_GUARD, IntType.NPC, "talk-to") { player, node ->
            val forceChat =
                arrayOf(
                    "Hmph... heh heh heh...",
                    "Mmmm... big pint of beer... kebab...",
                    "Mmmmmm... donuts...",
                    "Guh.. mwww... zzzzzz...",
                )
            lock(player, 2)
            sendChat((node as NPC), forceChat[RandomFunction.random(forceChat.size)])
            queueScript(player, 1, QueueStrength.SOFT) {
                sendDialogue(player, "Maybe I should let him sleep.")
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        /*
         * Handles attacking Wormbrain NPC.
         */

        on(WORMBRAIN, IntType.NPC, "attack") { player, node ->
            if (getQuestStage(player, Quests.DRAGON_SLAYER) != 20) {
                sendDialogue(player, "The goblin is already in prison. You have no reason to attack him.")
            } else {
                player.properties.combatPulse.attack(node)
            }
            return@on true
        }

        /*
         * Handles entering the icy cavern.
         */

        on(CAVE_ENTRANCE, IntType.SCENERY, "enter") { player, _ ->
            queueScript(player, 1, QueueStrength.SOFT) {
                player.properties.teleportLocation = Location(3056, 9562, 0)
                sendMessage(player, "You leave the icy cavern.")
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        /*
         * Handles exiting the icy cavern.
         */

        on(CAVE_EXIT, IntType.SCENERY, "exit") { player, _ ->
            dialogue(player) {
                message("STOP! The creatures in this cave are VERY Dangerous. Are you", "sure you want to enter?")
                options(null, "Yes, I'm not afraid of death!", "No thanks, I don't want to die!") { buttonId ->
                    if (buttonId == 1) {
                        val LOCATION = Location.create(3056, 9555, 0)
                        player.properties.teleportLocation = LOCATION
                        sendMessage(player, "You venture into the icy cavern.")
                    }
                }
            }
            return@on true
        }
    }
}

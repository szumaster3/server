package content.region.asgarnia.handlers

import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.*

class PortSarimListener : InteractionListener {
    override fun defineListeners() {
        on(Items.AHABS_BEER_6561, IntType.GROUNDITEM, "take") { player, node ->
            face(player, node)
            animate(player, Animations.HUMAN_MULTI_USE_832)
            player.dialogueInterpreter.open(NPCs.AHAB_2692, findNPC(NPCs.AHAB_2692), false)
            return@on true
        }

        on(SEAMAN, IntType.NPC, "pay-fare") { player, node ->
            val npc = node as NPC
            player.dialogueInterpreter.open(npc.id, npc, true)
            return@on true
        }

        on(WYDIN_STORE_DOOR, IntType.SCENERY, "open") { player, node ->
            if (!inEquipment(player, Items.WHITE_APRON_1005) && player.location.x == 3012) {
                player.dialogueInterpreter.open(NPCs.WYDIN_557, true, true)
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }

        on(WYDIN_BANANA_CRATE, IntType.SCENERY, "search") { player, _ ->
            if (freeSlots(player) == 0) {
                sendMessage(player, "Not enough inventory space.")
                return@on true
            }
            lock(player, 2)
            sendMessage(player, "There are lots of bananas in the crate.")
            queueScript(player, 1, QueueStrength.SOFT) {
                if (player.getAttribute("wydin-rum", false)) {
                    animate(player, Animations.HUMAN_MULTI_USE_832)
                    addItem(player, Items.KARAMJAN_RUM_431)
                    removeAttributes(player, "wydin-rum", "stashed-rum")
                    sendMessage(player, "You find your bottle of rum in amongst the bananas.")
                } else {
                    openDialogue(
                        player,
                        object : DialogueFile() {
                            override fun handle(
                                componentID: Int,
                                buttonID: Int,
                            ) {
                                when (stage) {
                                    0 -> {
                                        setTitle(player, 2)
                                        sendDialogueOptions(player, "Do you want to take a banana?", "Yes.", "No.")
                                        stage++
                                    }

                                    1 ->
                                        when (buttonID) {
                                            1 -> {
                                                end()
                                                animate(player, Animations.HUMAN_MULTI_USE_832)
                                                addItem(player, Items.BANANA_1963)
                                                sendMessage(player, "You take a banana.")
                                            }

                                            2 -> end()
                                        }
                                }
                            }
                        },
                    )
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(DOORS, IntType.SCENERY, "open", "pick-lock") { player, _ ->
            if (getUsedOption(player) == "open") {
                sendMessage(player, "The door is securely locked.")
                return@on true
            }

            if (getUsedOption(player) == "pick-lock") {
                if (player.location.y <=
                    3187
                ) {
                    sendMessage(player, "You simply cannot find a way to pick the lock from this side.")
                } else {
                    sendMessage(player, "The door is securely locked.")
                }
            }
            return@on true
        }

        on(MONKS_OF_ENTRANA, IntType.NPC, "take-boat") { player, node ->
            openDialogue(player, (node as NPC).id, node)
            return@on true
        }

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

        on(WORMBRAIN, IntType.NPC, "attack") { player, node ->
            if (getQuestStage(player, Quests.DRAGON_SLAYER) != 20) {
                sendDialogue(player, "The goblin is already in prison. You have no reason to attack him.")
            } else {
                player.properties.combatPulse.attack(node)
            }
            return@on true
        }

        on(CAVE_ENTRANCE, IntType.SCENERY, "enter") { player, _ ->
            queueScript(player, 1, QueueStrength.SOFT) {
                player.properties.teleportLocation = Location(3056, 9562, 0)
                sendMessage(player, "You leave the icy cavern.")
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(CAVE_EXIT, IntType.SCENERY, "exit") { player, _ ->
            player.dialogueInterpreter.open(238284)
            return@on true
        }
    }

    companion object {
        private const val CAVE_ENTRANCE = Scenery.ICY_CAVERN_33174
        private const val CAVE_EXIT = Scenery.CAVE_33173
        private const val SLEEPING_GUARD = NPCs.GUARD_2704
        private const val WORMBRAIN = NPCs.WORMBRAIN_745
        private const val WYDIN_BANANA_CRATE = Scenery.CRATE_2071
        private const val WYDIN_STORE_DOOR = Scenery.DOOR_2069
        private val DOORS = intArrayOf(Scenery.CELL_DOOR_9563, Scenery.DOOR_9565)
        private val MONKS_OF_ENTRANA =
            intArrayOf(
                NPCs.MONK_OF_ENTRANA_2728,
                NPCs.MONK_OF_ENTRANA_657,
                NPCs.MONK_OF_ENTRANA_2729,
                2730,
                NPCs.MONK_OF_ENTRANA_2731,
                NPCs.MONK_OF_ENTRANA_658,
            )
        private val SEAMAN = intArrayOf(NPCs.CAPTAIN_TOBIAS_376, NPCs.SEAMAN_LORRIS_377, NPCs.SEAMAN_THRESNOR_378)
    }
}

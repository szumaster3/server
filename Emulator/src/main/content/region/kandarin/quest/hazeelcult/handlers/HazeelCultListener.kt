package content.region.kandarin.quest.hazeelcult.handlers

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.*

class HazeelCultListener : InteractionListener {
    init {
        ALOMONE.init()
        ALOMONE.isWalks = true
        ALOMONE.isRespawn = true
    }

    override fun defineListeners() {
        on(CLIVET_STAIRS, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, location(2586, 3237, 0))
            sendMessage(player, "You climb up the stairs.")
            return@on true
        }

        on(CAVE_ENTRANCE, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, location(2570, 9682, 0))
            sendMessage(player, "You enter the cave.")
            return@on true
        }

        on(CLIVET_RAFT, IntType.SCENERY, "board") { player, _ ->
            if (getAttribute(player, RAFT_UNLOCK, 0) == 1) {
                setAttribute(player, RAFT_UNLOCK, 0)
                teleport(player, location(2606, 9692, 0))
                sendDialogue(
                    player,
                    "The raft washes up the sewer, past the islands until it reaches the end of the sewer passage.",
                )
            } else if (inBorders(player, 2604, 9688, 2611, 9694)) {
                setAttribute(player, RAFT_UNLOCK, 1)
                teleport(player, location(2567, 9680, 0))
                sendDialogue(player, "The raft flows back to the cave entrance.")
            } else {
                sendNPCDialogue(
                    player,
                    CLIVET_NPC,
                    "Hey! I don't remember saying you could use that raft!",
                    FaceAnim.ANNOYED,
                )
            }
            return@on true
        }

        on(SEWERS, IntType.SCENERY, "turn-left") { player, node ->
            if (!player.location.withinDistance(node.asScenery().location, 2)) return@on true
            if (getQuestStage(player, Quests.HAZEEL_CULT) >= 1) {
                lock(player, 4)
                animate(player, TURN_VALVE_ANIMATION)
                player.dialogueInterpreter.sendDialogue(
                    "Turn the large metal valve to the left. Beneath your feet you",
                    "can heat the sudden sound of rushing water from the sewer.",
                )
            }
            return@on true
        }

        on(SEWER_1, IntType.SCENERY, "turn-left") { player, node ->
            if (getQuestStage(player, Quests.HAZEEL_CULT) >= 1 && getAttribute(player, SEWER_LEFT, 0) == 0) {
                setAttribute(player, SEWER_LEFT, 1)
            }
            if (getQuestStage(player, Quests.HAZEEL_CULT) == 10 && getAttribute(player, SEWER_RIGHT_2, 0) == 1) {
                setAttribute(player, SEWER_RIGHT_3, 1)
            }
            if (!player.location.withinDistance(node.asScenery().location, 2)) return@on true
            lock(player, 4)
            animate(player, TURN_VALVE_ANIMATION)
            player.dialogueInterpreter.sendDialogue(
                "Turn the large metal valve to the left. Beneath your feet you",
                "can heat the sudden sound of rushing water from the sewer.",
            )
            return@on true
        }

        on(SEWER_1, IntType.SCENERY, "turn-right") { player, _ ->
            lock(player, 4)
            animate(player, TURN_VALVE_ANIMATION)
            player.dialogueInterpreter.sendDialogue(
                "Turn the large metal valve to the right. Beneath your feet you",
                "can heat the sudden sound of rushing water from the sewer.",
            )
            return@on true
        }

        on(SEWERS, IntType.SCENERY, "turn-right") { player, node ->
            if (!player.location.withinDistance(node.asScenery().location, 2)) return@on true
            if (getQuestStage(player, Quests.HAZEEL_CULT) >= 1) {
                for (i in SEWERS) {
                    when (i) {
                        SEWER_2 ->
                            if (getAttribute(player, SEWER_LEFT, 0) == 1) {
                                setAttribute(player, SEWER_RIGHT_1, 1)
                            }

                        SEWER_3 ->
                            if (getAttribute(player, SEWER_RIGHT_1, 0) == 1) {
                                setAttribute(player, SEWER_RIGHT_2, 1)
                            }

                        SEWER_4 ->
                            if (getAttribute(player, SEWER_RIGHT_2, 0) == 1) {
                                setAttribute(player, SEWER_RIGHT_3, 1)
                            }

                        SEWER_5 ->
                            if (getAttribute(player, SEWER_RIGHT_3, 0) == 1) {
                                setAttribute(player, RAFT_UNLOCK, 1)
                                removeAttributes(player, SEWER_RIGHT_1, SEWER_RIGHT_2, SEWER_RIGHT_3, SEWER_LEFT)
                            }
                    }
                }
            } else if (getQuestStage(player, Quests.HAZEEL_CULT) == 10) {
                if (!player.location.withinDistance(node.asScenery().location, 2)) return@on true
                for (i in SEWERS) {
                    when (i) {
                        SEWER_4 -> setAttribute(player, SEWER_RIGHT_1, 1)
                        SEWER_5 ->
                            if (getAttribute(player, SEWER_RIGHT_1, 0) == 1) {
                                setAttribute(player, SEWER_RIGHT_2, 1)
                            }

                        SEWER_2 ->
                            if (getAttribute(player, SEWER_RIGHT_3, 0) == 1) {
                                setAttribute(player, SEWER_LEFT, 1)
                            }

                        SEWER_3 ->
                            if (getAttribute(player, SEWER_LEFT, 0) == 1) {
                                setAttribute(player, RAFT_UNLOCK, 1)
                                removeAttributes(player, SEWER_RIGHT_1, SEWER_RIGHT_2, SEWER_RIGHT_3, SEWER_LEFT)
                            }
                    }
                }
            }
            lock(player, 4)
            animate(player, TURN_VALVE_ANIMATION)
            player.dialogueInterpreter.sendDialogue(
                "Turn the large metal valve to the right. Beneth your feet you",
                "can heat the sudden sound of rushing water from the sewer.",
            )
            return@on true
        }

        on(WADROBE, IntType.SCENERY, "search") { player, _ ->
            sendDialogue(player, "You search the wardrobe thoroughly but find nothing of interest.")
            return@on true
        }

        onUseWith(IntType.SCENERY, POISON, COOKING_RANGE) { player, _, _ ->
            if (getAttribute(player, MAHJARRAT, true) && removeItem(player, POISON)) {
                player.dialogueInterpreter.sendDialogue(
                    "You pour the poison into the hot pot.",
                    "The poison dissolves into the soup.",
                )
                setQuestStage(player, Quests.HAZEEL_CULT, 3)
            } else {
                sendNPCDialogue(
                    player,
                    NPCs.CLAUS_THE_CHEF_886,
                    "Oi - I don't want people messing around with my range!",
                )
            }
            return@onUseWith true
        }

        on(CRATE, IntType.SCENERY, "search") { player, _ ->
            if (getQuestStage(player, Quests.HAZEEL_CULT) >= 1 &&
                getAttribute(player, MAHJARRAT, true) &&
                !getAttribute(player, CARNILLEAN, true) &&
                freeSlots(player) > 1
            ) {
                sendMessage(player, "You search the crate and find an old key hidden at the bottom.")
                addItem(player, CHEST_KEY, 1)
            } else if (freeSlots(player) < 1) {
                sendMessage(
                    player,
                    "You search the crate and find an old key hidden at the bottom, but you don't have enough room to take it.",
                )
            } else {
                sendMessage(player, "You search the crate but find nothing of interest.")
            }
            return@on true
        }

        on(SECRET_PASSAGE, IntType.SCENERY, "Knock-at") { player, node ->
            if (inInventory(player, CHEST_KEY) && player.location.y == 3274) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                sendMessage(player, "You find the secret passageway")
            } else if (player.location.y == 3275) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, CHEST_KEY, Scenery.CHEST_2856) { player, _, _ ->
            animate(player, 536)
            addScenery(Scenery.CHEST_2857, Location(2565, 3272, 2), 2, 10)
            removeScenery(
                core.game.node.scenery
                    .Scenery(Scenery.CHEST_2856, Location(2565, 3272, 2)),
            )
            return@onUseWith true
        }

        on(Scenery.CHEST_2857, IntType.SCENERY, "Close") { _, _ ->
            addScenery(Scenery.CHEST_2856, Location(2565, 3272, 2), 2, 10)
            removeScenery(
                core.game.node.scenery.Scenery(
                    Scenery.CHEST_2857,
                    Location(2565, 3272, 2),
                ),
            )
            return@on true
        }

        on(Scenery.CHEST_2857, IntType.SCENERY, "Search") { player, _ ->
            val hasScroll = hasAnItem(player, HAZEEL_SCROLL).container != null
            if (!hasScroll && freeSlots(player) > 1) {
                addItem(player, HAZEEL_SCROLL, 1)
                sendItemDialogue(player, HAZEEL_SCROLL, "You unlock the chest and find a scroll inside.")
            } else if (!hasScroll && freeSlots(player) < 1) {
                player.dialogueInterpreter.sendItemMessage(
                    HAZEEL_SCROLL,
                    "You unlock the chest and find a scroll inside",
                    "but you don't have enough room to take it.",
                )
            } else if (hasScroll && freeSlots(player) > 1) {
                sendDialogue(player, "You already have the scroll from this chest.")
            }
            return@on true
        }

        fun hazeelScroll(player: Player) {
            val scroll =
                arrayOf(
                    "Sentente sillaberi junque dithmento! Ia! Ia!",
                    "",
                    "dextrimon encanto! termando... imcando...",
                    "",
                    "solly enty rando... sentente! Ia! Ia!",
                    "",
                    "Indenti zaggarati g'thxa! Dintenta! Sententa!",
                    "",
                    "Retenta! Q'exjta! Ia! Sottottott!",
                    "",
                    "Ia! Dysmenta junque fammatio svelken!",
                    "",
                    "Sottey! Sentey! SOLOMENT!",
                )
            sendString(player, scroll.joinToString("<br>"), Components.BLANK_SCROLL_222, 2)
        }

        onUseWith(IntType.ITEM, CHEST_KEY, HAZEEL_SCROLL) { player, _, _ ->
            openInterface(player, SCROLL_INTERFACE).also { hazeelScroll(player) }
            return@onUseWith true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(SEWER_1), "turn-left") { _, _ ->
            return@setDest Location.create(2586, 3244, 0)
        }
        setDest(IntType.SCENERY, intArrayOf(SEWER_2), "turn-right") { _, _ ->
            return@setDest Location.create(2597, 3262, 0)
        }
        setDest(IntType.SCENERY, intArrayOf(SEWER_3), "turn-right") { _, _ ->
            return@setDest Location.create(2610, 3242, 0)
        }
        setDest(IntType.SCENERY, intArrayOf(SEWER_4), "turn-right") { _, _ ->
            return@setDest Location.create(2563, 3246, 0)
        }
        setDest(IntType.SCENERY, intArrayOf(SEWER_5), "turn-right") { _, _ ->
            return@setDest Location.create(2571, 3263, 0)
        }
    }

    companion object {
        const val CARNILLEAN = "/save:hazeelcult:carnillean"
        const val MAHJARRAT = "/save:hazeelcult:mahjarrat"
        const val SEWER_LEFT = "/save:hazeelcult:sewer-left"
        const val SEWER_RIGHT_1 = "/save:hazeelcult:sewer-right:1"
        const val SEWER_RIGHT_2 = "/save:hazeelcult:sewer-right:2"
        const val SEWER_RIGHT_3 = "/save:hazeelcult:sewer-right:3"
        const val RAFT_UNLOCK = "/save:hazeelcult:raft-unlock"

        const val SCROLL_INTERFACE = Components.BLANK_SCROLL_222

        const val CLIVET_NPC = NPCs.CLIVET_893
        const val CLIVET_RAFT = Scenery.RAFT_2849

        const val CAVE_ENTRANCE = Scenery.CAVE_ENTRANCE_2852
        const val CLIVET_STAIRS = Scenery.STAIRS_2853

        const val HAZEEL_SCROLL = Items.HAZEEL_SCROLL_2403

        const val SEWER_1 = Scenery.SEWER_VALVE_2846
        const val SEWER_2 = Scenery.SEWER_VALVE_2847
        const val SEWER_3 = Scenery.SEWER_VALVE_2848
        const val SEWER_4 = Scenery.SEWER_VALVE_2844
        const val SEWER_5 = Scenery.SEWER_VALVE_2845

        val SEWERS = intArrayOf(SEWER_2, SEWER_3, SEWER_4, SEWER_5)

        const val WADROBE = Scenery.WARDROBE_2851

        const val TURN_VALVE_ANIMATION = 4861

        const val CRATE = Scenery.CRATE_34585
        const val CHEST_KEY = Items.CHEST_KEY_709
        const val POISON = Items.POISON_273
        const val COOKING_RANGE = Scenery.COOKING_RANGE_2859

        val ALOMONE = AlomoneNPC(NPCs.ALOMONE_891, Location.create(2607, 9671, 0))

        const val SECRET_PASSAGE = Scenery.WALL_26940
    }
}

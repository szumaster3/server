package content.region.misthalin.quest.surok.handlers

import content.data.items.SkillingTool.Companion.getPickaxe
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class WhatLiesBelowListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.PORTAL_23095, IntType.SCENERY, "use") { player, _ ->
            player.teleport(Location(2270, 4836, 1))
            finishDiaryTask(player, DiaryType.VARROCK, 1, 2)
            return@on true
        }

        on(Scenery.OPEN_STATUE_23058, IntType.SCENERY, "enter") { player, _ ->
            player.animate(Animation.create(6103))
            player.teleport(Location(3179, 5191, 0), 2)
            setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_3_3660, 1, true)
            return@on true
        }

        on(Items.ZAFFS_INSTRUCTIONS_11011, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.ZAFFS_INSTRUCTIONS_251)
            return@on true
        }

        on(Items.BEACON_RING_11014, IntType.ITEM, "summon", "operate") { player, _ ->
            if (getQuestStage(player, Quests.WHAT_LIES_BELOW) > 70) {
                sendMessage(player, "Zaff has removed the summon charges from the ring. It will not summon him again.")
            } else {
                if (getQuestStage(
                        player,
                        Quests.WHAT_LIES_BELOW,
                    ) == 70 &&
                    player.zoneMonitor.isInZone(Quests.WHAT_LIES_BELOW)
                ) {
                    val cutscene = player.getAttribute<WhatLiesBelowCutscene>("cutscene", null)
                    cutscene.summonZaff()
                    return@on true
                }
                sendMessage(player, "You need to use this at the proper time.")
            }
            return@on true
        }

        on(QUEST_ITEMS, IntType.ITEM, "read") { player, node ->
            when (node.id) {
                11003 -> sendMessage(player, "The folder is empty at the moment so there is nothing inside to read!")
                11008, 11007, 11006 ->
                    sendDialogueLines(
                        player,
                        "The piece of papers appears to contain lots of facts and figures.",
                        "They look like accounts and lists of items. You're",
                        "not sure what they all mean.",
                    )

                11009, 11010 -> {
                    sendMessage(player, "You read the letter.")
                    openInterface(
                        player,
                        if (node.id ==
                            Items.RATS_LETTER_11009
                        ) {
                            Components.SUROK_LETTER1_249
                        } else {
                            Components.SUROK_LETTER2_250
                        },
                    )
                }
            }
            return@on true
        }

        on(Scenery.BOOKCASE_24281, IntType.SCENERY, "search") { player, node ->
            if (node.id == Scenery.BOOKCASE_24281 && !inInventory(player, Items.DAGONHAI_HISTORY_11001, 1)) {
                sendMessage(player, "You search the " + node.name.lowercase() + "...")
                sendMessage(player, "and find a book named 'Dagon'hai history'")
                addItemOrDrop(player, Items.DAGONHAI_HISTORY_11001, 1)
            } else {
                sendMessage(
                    player,
                    "You search the " + node.name.lowercase() + " and you find nothing of interest to you.",
                )
            }
            return@on true
        }

        on(Scenery.STATUE_23057, IntType.SCENERY, "excavate") { player, _ ->
            if (getQuestStage(player, Quests.WHAT_LIES_BELOW) < 30) {
                val tool = getPickaxe(player)
                if (tool == null) {
                    sendMessage(player, "You need a pickaxe in order to do that.")
                    return@on true
                }
                lock(player, 16)
                animate(player, tool.animation)
                Pulser.submit(
                    object : Pulse(1, player) {
                        var count: Int = 0

                        override fun pulse(): Boolean {
                            count++
                            val duration = 7
                            when (count) {
                                duration -> {
                                    sendChat(player, "Hmmm...")
                                }

                                duration + 2 -> {
                                    animate(player, tool.animation)
                                }

                                duration * 2 -> {
                                    setQuestStage(player, Quests.WHAT_LIES_BELOW, 40)
                                    resetAnimator(player)
                                    setVarp(player, 992, 1 shl 8, true)
                                    openDialogue(player, 5837, true, true, true)
                                    unlock(player)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
            return@on true
        }
    }

    companion object {
        const val CHAOS_RUNES = Items.CHAOS_RUNE_562
        const val CHAOS_TALISMAN = Items.CHAOS_TALISMAN_1452
        const val CHAOS_TIARA = Items.CHAOS_TIARA_5543
        const val BOWL = Items.BOWL_1923
        const val SIN_KETH_DIARY = Items.SINKETHS_DIARY_11002
        const val EMPTY_FOLDER = Items.AN_EMPTY_FOLDER_11003
        const val USED_FOLDER = Items.USED_FOLDER_11006
        const val FULL_FOLDER = Items.FULL_FOLDER_11007
        const val RATS_PAPER = Items.RATS_PAPER_11008
        const val RATS_LETTER = Items.RATS_LETTER_11009
        const val SUROKS_LETTER = Items.SUROKS_LETTER_11010
        const val WAND = Items.WAND_11012
        const val INFUSED_WAND = Items.INFUSED_WAND_11013
        const val BEACON_RING = Items.BEACON_RING_11014
        val QUEST_ITEMS = intArrayOf(RATS_PAPER, EMPTY_FOLDER, USED_FOLDER, FULL_FOLDER, RATS_LETTER, SUROKS_LETTER)
    }
}

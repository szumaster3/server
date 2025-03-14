package content.region.tirannwn.quest.roving_elves.dialogue

import content.region.tirannwn.quest.roving_elves.RovingElves
import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class IslwynDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.ROVING_ELVES)
        val waterfall = player.getQuestRepository().getQuest(Quests.WATERFALL_QUEST)
        if (quest.getStage(player) == 0 && waterfall.isCompleted(player)) {
            player(FaceAnim.HALF_GUILTY, "Hello there.")
            stage = 0
        }
        if (!waterfall.isCompleted(player)) {
            player(FaceAnim.HALF_GUILTY, "Hello there.")
            stage = 1000
        }
        if (quest.isStarted(player) && quest.getStage(player) >= 10) {
            player(FaceAnim.HALF_GUILTY, "Hello Islwyn.")
            stage = 0
        }
        if (quest.isCompleted(player) || quest.getStage(player) >= 100) {
            player(FaceAnim.HALF_GUILTY, "Hello Islwyn, I'm back.")
            stage = 31
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            500 -> end()
            1000 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Hello there, it's a lovely day for a walk in the woods.",
                    "So what can I help you with?",
                )
                stage = 1001
            }

            1001 -> {
                player(FaceAnim.HALF_GUILTY, "I'm just looking around.")
                stage = 500
            }

            2000 -> {
                if (getQuestStage(player, Quests.ROVING_ELVES) == 15) {
                    player(
                        FaceAnim.HALF_GUILTY,
                        "Yes I have! She explained that I have to visit",
                        "Glarial's old tomb and obtain a consecration seed",
                        "from the temple guardian in there.",
                    )
                    stage = 2001
                }
                if (getQuestStage(player, Quests.ROVING_ELVES) != 15) {
                    player(FaceAnim.HALF_GUILTY, "Not yet, I'll be back when I have.")
                    stage = 500
                }
            }

            2001 -> {
                interpreter.sendDialogues(
                    1680,
                    FaceAnim.HALF_GUILTY,
                    "Good luck against the guardian, adventurer.",
                    "Do it in the name of my grandmother Glarial.",
                )
                stage = 500
            }

            0 -> {
                if (getQuestStage(player, Quests.ROVING_ELVES) == 10 || getQuestStage(player, Quests.ROVING_ELVES) == 15
                ) {
                    interpreter.sendDialogues(1680, FaceAnim.HALF_GUILTY, "Have you spoken to Eluned yet?")
                    stage = 2000
                }
                if (getQuestStage(player, Quests.ROVING_ELVES) == 20) {
                    interpreter.sendDialogues(
                        1680,
                        FaceAnim.HALF_GUILTY,
                        "You have returned! Thank you for all you have done.",
                        "Now both me and my grandmother can rest in peace.",
                    )
                    stage = 19
                } else if (getQuestStage(player, Quests.ROVING_ELVES) != 10 &&
                    getQuestStage(
                        player,
                        Quests.ROVING_ELVES,
                    ) != 15
                ) {
                    interpreter.sendDialogues(
                        1680,
                        FaceAnim.HALF_GUILTY,
                        "Leave me be, I have no time for easterners. Between",
                        "your lot and them gnomes, all you do is take and",
                        "destroy. No thought for others.",
                    )
                    stage = 1
                }
            }

            1 -> {
                player(FaceAnim.HALF_GUILTY, "...but...")
                stage = 2
            }

            2 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Save your excuses young one! It was one of your",
                    "species that disturbed my grandmother's remains. Will",
                    "she never get the peace she deserves?",
                )
                stage = 3
            }

            3 -> {
                player(FaceAnim.HALF_GUILTY, "Grandmother?")
                stage = 4
            }

            4 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Yes! Someone took her ashes from her tomb. If it",
                    "wasn't for them gnomes she'd have been left in peace.",
                    "But now I can sense her restlessness.",
                )
                stage = 5
            }

            5 -> {
                player(FaceAnim.HALF_GUILTY, "Gnomes?")
                stage = 6
            }

            6 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Yes gnomes! One of those little pests took the key to",
                    "my grandmother's tomb. He must've given it to the",
                    "human that desecrated the tomb.",
                )
                stage = 7
            }

            7 -> {
                player(FaceAnim.HALF_GUILTY, "Was your grandmother's name Glarial?")
                stage = 8
            }

            8 -> {
                npc(FaceAnim.HALF_GUILTY, "Yes... How did you know that?")
                stage = 9
            }

            9 -> {
                sendDialogueOptions(player, "Do you want to;", "Tell the truth?", "Lie?", "Leave the old elf be?")
                stage = 10
            }

            10 ->
                when (buttonId) {
                    1 -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "It's a bit of a long tale, but to cut the story short, her",
                            "remains reside in Baxtorian's home. I thought it's where",
                            "she'd want to be. It was I that removed your",
                            "grandmother's ashes.",
                        )
                        stage = 11
                    }

                    2 -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "I just guessed.",
                            "Well, now that that's over, I really need to be",
                            "going.",
                        )
                        stage = 500
                    }

                    3 -> {
                        player(FaceAnim.HALF_GUILTY, "On second thought, I really should be going.")
                        stage = 500
                    }
                }

            11 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You've been in grandfather's home? That's where we",
                    "originally wanted to leave Glarial's ashes to rest, but we",
                    "could not understand how to enter.",
                )
                stage = 12
            }

            12 -> {
                npc(FaceAnim.HALF_GUILTY, "This is gravely concerning... Her resting place must be", "consecrated.")
                stage = 13
            }

            13 -> {
                options("Maybe I could help.", "Sounds like you've got a lot to do.")
                stage = 14
            }

            14 ->
                when (buttonId) {
                    1 -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "Maybe I could help. What needs doing to consecrate",
                            "her new tomb?",
                        )
                        stage = 15
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "Sounds like you've got a lot to do.")
                        stage = 500
                    }
                }

            15 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Are you offering to help?!? Maybe not all humans are",
                    "as bad as I thought.",
                )
                stage = 16
            }

            16 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I don't know the consecration process. You should speak",
                    "with Eluned... she is wise in the ways of the ritual.",
                )
                stage = 17
            }

            17 -> {
                player(FaceAnim.HALF_GUILTY, "I'll see what I can do.")
                stage = 18
            }

            18 -> {
                end()
                setQuestStage(player, Quests.ROVING_ELVES, 10)
            }

            19 -> {
                player(FaceAnim.HALF_GUILTY, "How did you know that I have consecrated the tomb?")
                stage = 20
            }

            20 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Her restlessness has finally left me. Here - I should",
                    "give you something for your effort.",
                )
                stage = 21
            }

            21 -> {
                sendDoubleItemDialogue(
                    player,
                    RovingElves.CRYSTAL_BOW_FULL,
                    RovingElves.CRYSTAL_SHIELD_FULL,
                    "Islwyn shows you a crystal bow and a crystal shield.",
                )
                stage = 22
            }

            22 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Crystal equipment is at its best when new and",
                    "previously unused. The bow does not require",
                    "ammunition and reduces in strength the more it's fired.",
                    "The shield decreases in defensive capabilities the more",
                )
                stage = 23
            }

            23 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "it's hit. Both the shield and the bow I am carrying only",
                    "have 500 uses before they revert to seed.",
                )
                stage = 24
            }

            24 -> {
                player(FaceAnim.HALF_GUILTY, "Revert to seed? What do you mean?")
                stage = 25
            }

            25 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Ahhh, young one. It was thousands of years before we",
                    "fully understood that ourselves. All will be explained if",
                    "we feel you are ready. Now which one of these crystal",
                    "creations would you like?",
                )
                stage = 26
            }

            26 -> {
                options(
                    "Shields are for wimps! Give me the bow!",
                    "I don't like running and hiding behind mushrooms. Shield please!",
                )
                stage = 27
            }

            27 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "Shields are for wimps! Give me the bow!")
                        stage = 30
                    }

                    2 -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "I don't like running and hiding behind mushrooms.",
                            "Shield please!",
                        )
                        stage = 301
                    }
                }

            30 -> {
                if (!isQuestComplete(player, Quests.ROVING_ELVES)) {
                    if (!addItem(player, Items.CRYSTAL_BOW_2_10_4222, 1)) {
                        GroundItemManager.create(Item(Items.CRYSTAL_BOW_2_10_4222, 1), player)
                    }
                    finishQuest(player, Quests.ROVING_ELVES)
                }
                end()
            }

            301 -> {
                if (!isQuestComplete(player, Quests.ROVING_ELVES)) {
                    if (!addItem(player, Items.CRYSTAL_SHIELD_2_10_4233, 1)) {
                        GroundItemManager.create(Item(Items.CRYSTAL_SHIELD_2_10_4233, 1), player)
                    }
                    finishQuest(player, Quests.ROVING_ELVES)
                }
                end()
            }

            31 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Welcome back to the land of the elves, friend!",
                    "Do you need your seeds charged into equipment?",
                )
                stage = 32
            }

            32 -> {
                options("I need to buy a new piece of equipment.", "I need to recharge my seeds into equipment.")
                stage = 33
            }

            33 ->
                when (buttonId) {
                    1 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Ah, very well.",
                            "I will sell you a new bow or shield for 900,000 coins.",
                        )
                        stage = 37
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "I need to recharge my current seeds into equipment.")
                        stage = 34
                    }
                }

            34 -> {
                options("Recharge seed into bow", "Recharge seed into shield")
                stage = 35
            }

            35 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "Recharge my seed into a bow, please.")
                        stage = 36
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "Recharge my seed into a shield, please.")
                        stage = 3601
                    }
                }

            36 -> {
                if (!inInventory(player, RovingElves.CRYSTAL_SEED, 1)) {
                    sendDialogue(player, "You don't have any seeds to recharge.")
                    stage = 500
                }
                val timesRecharged = player.getAttribute("rovingelves:crystal-equip-recharges", 0)
                val price = crystalWeaponPrice(timesRecharged)
                if (!inInventory(player, 995, price)) {
                    interpreter.sendDialogue(String.format("You don't have enough coins, you need %d.", price))
                    stage = 500
                }
                if (inInventory(player, 995, price) && inInventory(player, RovingElves.CRYSTAL_SEED, 1)) {
                    if (removeItem(player, RovingElves.CRYSTAL_SEED) && removeItem(player, Item(995, price))) {
                        addItem(player, Items.CRYSTAL_BOW_FULL_4214, 1)
                        player.incrementAttribute("/save:rovingelves:crystal-equip-recharges", 1)
                        end()
                    }
                }
            }

            3601 -> {
                if (!inInventory(player, RovingElves.CRYSTAL_SEED, 1)) {
                    sendDialogue(player, "You don't have any seeds to recharge.")
                    stage = 500
                }
                val timesRecharged = player.getAttribute<Int>("rovingelves:crystal-equip-recharges", 0)
                var price = crystalWeaponPrice(timesRecharged)
                if (!inInventory(player, 995, price)) {
                    sendDialogue(player, "You don't have enough coins.")
                    stage = 500
                }
                if (inInventory(player, 995, price) && inInventory(player, RovingElves.CRYSTAL_SEED, 1)) {
                    if (removeItem(player, RovingElves.CRYSTAL_SEED) && removeItem(player, Item(995, price))) {
                        addItem(player, Items.CRYSTAL_SHIELD_FULL_4225, 1)
                        player.incrementAttribute("/save:rovingelves:crystal-equip-recharges", 1)
                        end()
                    }
                }
            }

            37 -> {
                options("Purchase bow", "Purchase shield")
                stage = 38
            }

            38 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "I'd like to buy a new bow.")
                        stage = 39
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "I'd like to buy a new shield.")
                        stage = 40
                    }
                }

            39 -> {
                val price = crystalWeaponPrice(0)
                if (!inInventory(player, 995, price)) {
                    sendDialogue(player, "You don't have enough coins.")
                    stage = 500
                }
                if (inInventory(player, 995, price)) {
                    if (removeItem(player, Item(995, price))) {
                        if (!addItem(player, Items.NEW_CRYSTAL_BOW_4212, 1)) {
                            GroundItemManager.create(Item(Items.NEW_CRYSTAL_BOW_4212, 1), player)
                        }
                        end()
                    }
                }
            }

            40 -> {
                val price = crystalWeaponPrice(0)
                if (!inInventory(player, 995, price)) {
                    sendDialogue(player, "You don't have enough coins.")
                    stage = 500
                }
                if (inInventory(player, 995, price)) {
                    if (removeItem(player, Item(995, price))) {
                        if (!addItem(player, Items.NEW_CRYSTAL_SHIELD_4224, 1)) {
                            GroundItemManager.create(Item(Items.NEW_CRYSTAL_SHIELD_4224, 1), player)
                        }
                        end()
                    }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(DialogueInterpreter.getDialogueKey("islwyn_dialogue"), NPCs.ISLWYN_1680)
    }

    fun crystalWeaponPrice(timesRecharged: Int): Int {
        return Math.max(900000 - 180000 * timesRecharged, 180000)
    }
}

package content.region.asgarnia.quest.death.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests

class HaroldDialogueFile : DialogueFile() {
    companion object {
        const val ATTRIBUTE_JUMPSTAGE = "deathplateau:jumpStage"
        const val ATTRIBUTE_HAROLD_MONEY = "/save:deathplateau:haroldMoney"

        fun resetNpc(player: Player) {
            setAttribute(player, ATTRIBUTE_JUMPSTAGE, 0)
            setAttribute(player, ATTRIBUTE_HAROLD_MONEY, 200)
        }
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        if (getAttribute(player!!, ATTRIBUTE_JUMPSTAGE, 0) != 0) {
            stage = getAttribute(player!!, ATTRIBUTE_JUMPSTAGE, 0)
            setAttribute(player!!, ATTRIBUTE_JUMPSTAGE, 0)
        }
        println(getAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, -1))
        when (getQuestStage(player!!, Quests.DEATH_PLATEAU)) {
            10 -> {
                when (stage) {
                    START_DIALOGUE -> player(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Hi.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "You're the guard that was on duty last night?").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Yeah.").also { stage++ }
                    4 ->
                        playerl(
                            FaceAnim.HAPPY,
                            "Denulth said that you lost the combination to the equipment room?",
                        ).also { stage++ }

                    5 ->
                        npcl(FaceAnim.FRIENDLY, "I don't want to talk about it!").also {
                            setQuestStage(player!!, Quests.DEATH_PLATEAU, 11)
                            stage = END_DIALOGUE
                        }
                }
            }

            11 -> {
                when (stage) {
                    START_DIALOGUE -> player(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npcl(FaceAnim.ANNOYED, "What?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "You're the guard that was on duty last night?").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "I said I didn't want to talk about it!").also { stage = END_DIALOGUE }
                }
            }

            12 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Hi.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Can I buy you a drink?").also { stage++ }
                    3 -> npcl(FaceAnim.HAPPY, "Now you're talking! An Asgarnian Ale, please!").also { stage++ }
                    4 -> {
                        if (!removeItem(player!!, Items.ASGARNIAN_ALE_1905)) {
                            playerl(FaceAnim.FRIENDLY, "I'll go and get you one.").also { stage = END_DIALOGUE }
                        } else {
                            end()
                            animate(npc!!, Animations.EAT_OLD_829)
                            setQuestStage(player!!, Quests.DEATH_PLATEAU, 13)
                            sendMessage(player!!, "You give Harold an Asgarnian Ale.")
                            setAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, 200)
                            sendItemDialogue(
                                player!!,
                                Items.ASGARNIAN_ALE_1905,
                                "You give Harold an Asgarnian Ale.",
                            ).also { stage++ }
                            runTask(npc!!, 5) {
                                npcl(FaceAnim.FRIENDLY, "Arrh. That hit the spot!").also {
                                    stage = END_DIALOGUE
                                }
                            }
                        }
                    }
                }
            }

            13 -> {
                when (stage) {
                    START_DIALOGUE -> player(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npc(FaceAnim.FRIENDLY, "Hi.").also { stage++ }
                    2 ->
                        showTopics(
                            Topic(FaceAnim.ASKING, "Where were you when you last had the combination?", 10),
                            Topic(FaceAnim.FRIENDLY, "Would you like to gamble?", 30),
                            Topic(FaceAnim.FRIENDLY, "Can I buy you a drink?", 20),
                        )

                    10 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I honestly don't know! I've looked everywhere. I've searched the castle and my room!",
                        ).also { stage++ }

                    11 ->
                        playerl(
                            FaceAnim.ASKING,
                            "Have you tried looking between here and the castle?",
                        ).also { stage++ }

                    12 -> npcl(FaceAnim.FRIENDLY, "Yeah, I tried that.").also { stage++ }
                    13 -> npcl(FaceAnim.FRIENDLY, "I need another beer.").also { stage = END_DIALOGUE }
                    20 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Sounds good! I normally drink Asgarnian Ale but you know what?",
                        ).also { stage++ }

                    21 -> playerl(FaceAnim.ASKING, "What?").also { stage++ }
                    22 ->
                        npc(
                            FaceAnim.FRIENDLY,
                            "I really fancy one of those Blurberry Specials. I never",
                            "get over to the Gnome Stronghold so I haven't had one",
                            "for ages!",
                        ).also { stage++ }

                    23 -> {
                        if (removeItem(player!!, Item(Items.BLURBERRY_SPECIAL_2064, 1)) ||
                            removeItem(
                                player!!,
                                Item(Items.BLURBERRY_SPECIAL_9520, 1),
                            ) ||
                            removeItem(player!!, Item(Items.PREMADE_BLURB_SP_2028, 1))
                        ) {
                            sendMessage(player!!, "You give Harold a Blurberry Special.")
                            sendItemDialogue(
                                player!!,
                                Items.BLURBERRY_SPECIAL_2064,
                                "You give Harold a Blurberry Special.",
                            ).also { stage++ }
                        } else {
                            player(FaceAnim.FRIENDLY, "I'll go and get you one.").also { stage = END_DIALOGUE }
                        }
                    }

                    24 -> {
                        end()
                        setQuestStage(player!!, Quests.DEATH_PLATEAU, 14)
                        npc!!.isWalks = false
                        animate(npc!!, Animations.EAT_OLD_829)
                        runTask(npc!!, 4) {
                            npc!!.sendChat("Wow!")
                            runTask(npc!!, 4) {
                                visualize(
                                    npc!!,
                                    2770,
                                    Graphics(80, 96),
                                )
                                runTask(npc!!, 6) {
                                    npc(FaceAnim.DRUNK, "Now THAT hit the spot!").also {
                                        runTask(npc!!, 6) {
                                            npc!!.isWalks = true
                                            animate(npc!!, -1)
                                            end()
                                            stage = END_DIALOGUE
                                        }
                                    }
                                }
                            }
                        }
                    }

                    30 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Good. Good. I have some dice. How much do you want to offer?",
                        ).also { stage++ }

                    31 -> {
                        sendInputDialogue(player!!, true, "Enter amount:") { value ->
                            val wagerAmount = value as Int

                            if (wagerAmount <= 0) {
                                sendMessage(player!!, "You have to offer some money.").also { stage = END_DIALOGUE }
                            } else if (wagerAmount > 1000) {
                                npcl(
                                    FaceAnim.FRIENDLY,
                                    "Woah! Do you think I'm made of money? Max bet is 1000 gold.",
                                ).also { stage = END_DIALOGUE }
                            } else if (!inInventory(player!!, Items.COINS_995) ||
                                amountInInventory(
                                    player!!,
                                    Items.COINS_995,
                                ) < value
                            ) {
                                sendMessage(player!!, "You do not have that much money!").also { stage = END_DIALOGUE }
                            } else if (removeItem(player!!, Item(Items.COINS_995, value))) {
                                player!!.setAttribute("deathplateau:wager", wagerAmount)
                                npc(FaceAnim.FRIENDLY, "OK. I'll roll first!").also { stage++ }
                            }
                            return@sendInputDialogue
                        }
                    }

                    32 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Don't forget that once I start my roll you can't back out of the bet! If you do you lose your stake!",
                        ).also { stage++ }

                    33 -> {
                        end()
                        npc!!.isWalks = false

                        val advanceStage: (() -> Unit) = {
                            npc!!.isWalks = true
                            setAttribute(player!!, ATTRIBUTE_JUMPSTAGE, stage + 1)
                            openDialogue(player!!, HaroldDialogueFile(), npc!!)
                        }
                        setAttribute(player!!, "deathplateau:dicegameclose", advanceStage)
                        openInterface(player!!, 99)
                        sendString(player!!, player!!.name, 99, 6)
                    }

                    34 -> {
                        val wager = getAttribute(player!!, "deathplateau:wager", 0)
                        val haroldAmount = getAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, 0)
                        if (getAttribute(player!!, "deathplateau:winstate", false)) {
                            if (wager > haroldAmount) {
                                npcl(FaceAnim.FRIENDLY, "Oh dear, I seem to have run out of money!").also { stage++ }
                            } else {
                                end()
                                addItemOrDrop(player!!, Items.COINS_995, wager * 2)
                                sendMessage(player!!, "Harold has given you your winnings!")
                                setAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, haroldAmount - wager)
                                sendItemDialogue(
                                    player!!,
                                    Items.COINS_6964,
                                    "Harold has given you your winnings!",
                                ).also { stage = END_DIALOGUE }
                            }
                        } else {
                            end()
                            setAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, haroldAmount + wager)
                            sendItemDialogue(player!!, Items.COINS_6964, "You give Harold his winnings.").also {
                                stage = END_DIALOGUE
                            }
                        }
                    }

                    35 -> npcl(FaceAnim.FRIENDLY, "Here's what I have.").also { stage++ }
                    36 ->
                        sendItemDialogue(
                            player!!,
                            Items.COINS_6964,
                            "Harold has given you some of your winnings!",
                        ).also {
                            sendMessage(player!!, "Harold has given you some of your winnings!")
                            val haroldAmount = getAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, 200)
                            setAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, 0)
                            addItemOrDrop(player!!, Items.COINS_995, haroldAmount)
                            stage++
                        }

                    37 -> npcl(FaceAnim.FRIENDLY, "I'll write you out an IOU for the rest.").also { stage++ }
                    38 -> {
                        end()
                        addItemOrDrop(player!!, Items.IOU_3103)
                        setQuestStage(player!!, Quests.DEATH_PLATEAU, 15)
                        sendMessage(player!!, "Harold has given you an IOU scribbled on some paper.")
                        sendItemDialogue(
                            player!!,
                            Items.IOU_3103,
                            "Harold has given you an IOU scribbled on some paper.",
                        )
                    }
                }
            }

            14 -> {
                when (stage) {
                    START_DIALOGUE -> player(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npc(FaceAnim.DRUNK, "'Ello matey!'").also { stage++ }
                    2 ->
                        showTopics(
                            Topic(FaceAnim.THINKING, "Where were you when you last had the combination?", 10),
                            Topic(FaceAnim.FRIENDLY, "Would you like to gamble?", 20),
                            Topic(FaceAnim.FRIENDLY, "Can I buy you a drink?", 15),
                        )

                    10 -> npc(FaceAnim.DRUNK, "Hmm...!").also { stage++ }
                    11 -> npc(FaceAnim.DRUNK, "Er...!").also { stage++ }
                    12 -> npc(FaceAnim.DRUNK, "What wash the queshtion?").also { stage = END_DIALOGUE }

                    15 -> npcl(FaceAnim.DRUNK, "I fink I've had enough!").also { stage = END_DIALOGUE }

                    20 -> npc(FaceAnim.DRUNK, "Shure!").also { stage++ }
                    21 -> npc(FaceAnim.DRUNK, "Place your betsh pleashe!").also { stage++ }
                    22 -> npc(FaceAnim.DRUNK, "*giggle*").also { stage++ }

                    23 -> {
                        sendInputDialogue(player!!, true, "Enter amount:") { value ->
                            val wagerAmount = value as Int

                            if (wagerAmount <= 0) {
                                sendMessage(player!!, "You have to offer some money.").also { stage = END_DIALOGUE }
                            } else if (wagerAmount > 1000) {
                                npcl(FaceAnim.DRUNK, "Eashy tiger! Max bet ish 1000 coinsh.").also {
                                    stage = END_DIALOGUE
                                }
                            } else if (!inInventory(player!!, Items.COINS_995) ||
                                amountInInventory(
                                    player!!,
                                    Items.COINS_995,
                                ) < value
                            ) {
                                sendMessage(player!!, "You do not have that much money!").also { stage = END_DIALOGUE }
                            } else if (removeItem(player!!, Item(Items.COINS_995, value))) {
                                player!!.setAttribute("deathplateau:wager", wagerAmount)
                                npc(FaceAnim.DRUNK, "Right...er...here goes...").also { stage++ }
                            }
                            return@sendInputDialogue
                        }
                    }

                    24 -> {
                        end()
                        npc!!.isWalks = false

                        val advanceStage: (() -> Unit) = {
                            setAttribute(player!!, ATTRIBUTE_JUMPSTAGE, stage + (1..4).random())
                            openDialogue(player!!, HaroldDialogueFile(), npc!!)
                        }
                        setAttribute(player!!, "deathplateau:dicegameclose", advanceStage)
                        openInterface(player!!, 99)
                        sendString(player!!, player!!.name, 99, 6)
                    }

                    25 -> npc(FaceAnim.DRUNK, "Shixteen! How am I shupposhed to beat that!").also { stage = 30 }
                    26 -> npc(FaceAnim.DRUNK, "I didn't know you could ushe four dishe. Oh well.").also { stage = 30 }

                    27 -> npc(FaceAnim.DRUNK, "*hic*").also { stage = 30 }
                    28 -> npc(FaceAnim.DRUNK, "I sheemed to have rolled a one.").also { stage = 30 }
                    30 -> {
                        sendDialogue(
                            player!!,
                            "Harold is so drunk he can hardly see, let alone count!",
                        ).also { stage++ }
                        npc!!.isWalks = true
                        sendMessage(player!!, "Harold is so drunk he can hardly see, let alone count!")
                    }

                    31 -> {
                        val wager = getAttribute(player!!, "deathplateau:wager", 0)
                        val haroldAmount = getAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, 0)

                        if (wager > haroldAmount) {
                            npcl(FaceAnim.DRUNK, " Um...not enough money.").also { stage++ }
                        } else {
                            end()
                            addItemOrDrop(player!!, Items.COINS_995, wager * 2)
                            sendMessage(player!!, "Harold has given you your winnings!")
                            setAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, haroldAmount - wager)
                            sendItemDialogue(player!!, Items.COINS_6964, "Harold has given you your winnings!")
                        }
                    }

                    32 -> npcl(FaceAnim.DRUNK, "Heresh shome of it.").also { stage++ }
                    33 -> {
                        sendItemDialogue(player!!, Items.COINS_6964, "Harold has given you some of your winnings!")
                        sendMessage(player!!, "Harold has given you some of your winnings!")
                        val haroldAmount = getAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, 200)
                        setAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, 0)
                        addItemOrDrop(player!!, Items.COINS_995, haroldAmount)
                        stage++
                    }

                    34 -> npcl(FaceAnim.DRUNK, "I owe you the resht!").also { stage++ }
                    35 -> {
                        end()
                        addItemOrDrop(player!!, Items.IOU_3103)
                        setQuestStage(player!!, Quests.DEATH_PLATEAU, 15)
                        sendMessage(player!!, "Harold has given you an IOU scribbled on some paper.")
                        sendItemDialogue(
                            player!!,
                            Items.IOU_3103,
                            "Harold has given you an IOU scribbled on some paper.",
                        ).also { stage = END_DIALOGUE }
                    }
                }
            }

            15 -> {
                when (stage) {
                    START_DIALOGUE ->
                        npcl(FaceAnim.FRIENDLY, "Hi.").also {
                            if (inInventory(player!!, Items.IOU_3103)) {
                                stage = 5
                            } else {
                                stage++
                            }
                        }

                    1 -> playerl(FaceAnim.FRIENDLY, "I've lost the IOU you gave me.").also { stage++ }
                    2 -> npcl(FaceAnim.FRIENDLY, "I'll write you another.").also { stage++ }
                    3 -> {
                        end()
                        addItemOrDrop(player!!, Items.IOU_3103)
                        sendItemDialogue(
                            player!!,
                            Items.IOU_3103,
                            "Harold has given you an IOU scribbled on some paper.",
                        ).also { stage = END_DIALOGUE }
                    }

                    5 -> npc(FaceAnim.FRIENDLY, "Hi.").also { stage++ }
                    6 ->
                        showTopics(
                            Topic(FaceAnim.ASKING, "Where were you when you last had the combination?", 10),
                            Topic(FaceAnim.FRIENDLY, "Would you like to gamble?", 30),
                            Topic(FaceAnim.FRIENDLY, "Can I buy you a drink?", 20),
                        )

                    10 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I honestly don't know! I've looked everywhere. I've searched the castle and my room!",
                        ).also { stage++ }

                    11 ->
                        playerl(
                            FaceAnim.ASKING,
                            "Have you tried looking between here and the castle?",
                        ).also { stage++ }

                    12 -> npcl(FaceAnim.HALF_GUILTY, "Yeah, I tried that.").also { stage++ }
                    13 -> npcl(FaceAnim.HALF_GUILTY, "I need another beer.").also { stage = END_DIALOGUE }
                    20 -> npcl(FaceAnim.FRIENDLY, "I've run out of money!").also { stage++ }
                    21 -> npcl(FaceAnim.FRIENDLY, "Oh dear. I need beer.").also { stage = END_DIALOGUE }
                    30 -> npcl(FaceAnim.HAPPY, "Now you're talking! An Asgarnian Ale, please!").also { stage++ }
                    31 -> {
                        if (!removeItem(player!!, Items.ASGARNIAN_ALE_1905)) {
                            playerl(FaceAnim.FRIENDLY, "I'll go and get you one.").also { stage = END_DIALOGUE }
                        } else {
                            sendMessage(player!!, "You give Harold an Asgarnian Ale.")
                            end()
                            sendItemDialogue(
                                player!!,
                                Items.ASGARNIAN_ALE_1905,
                                "You give Harold an Asgarnian Ale.",
                            ).also { stage++ }
                            animate(npc!!, Animations.EAT_OLD_829)
                            runTask(npc!!, 5) {
                                npcl(FaceAnim.FRIENDLY, "Arrh. That hit the spot!").also {
                                    stage = END_DIALOGUE
                                }
                            }
                        }
                    }
                }
            }

            in 16..29 -> {
                when (stage) {
                    START_DIALOGUE ->
                        npcl(FaceAnim.FRIENDLY, "Hi.").also {
                            if (inInventory(player!!, Items.COMBINATION_3102) ||
                                inInventory(player!!, Items.IOU_3103)
                            ) {
                                stage = 5
                            } else {
                                stage++
                            }
                        }

                    1 -> playerl(FaceAnim.FRIENDLY, "I've lost the IOU you gave me.").also { stage++ }
                    2 -> npcl(FaceAnim.FRIENDLY, "I'll write you another.").also { stage++ }
                    3 -> {
                        end()
                        addItemOrDrop(player!!, Items.COMBINATION_3102)
                        sendItemDialogue(
                            player!!,
                            Items.COMBINATION_3102,
                            "Harold has given you the IOU, which you know is the combination.",
                        ).also { stage = END_DIALOGUE }
                    }

                    5 -> npc(FaceAnim.FRIENDLY, "Hi.").also { stage++ }
                    6 ->
                        showTopics(
                            Topic(FaceAnim.ASKING, "Where were you when you last had the combination?", 10),
                            Topic(FaceAnim.FRIENDLY, "Would you like to gamble?", 30),
                            Topic(FaceAnim.FRIENDLY, "Can I buy you a drink?", 20),
                        )

                    10 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I honestly don't know! I've looked everywhere. I've searched the castle and my room!",
                        ).also { stage++ }

                    11 ->
                        playerl(
                            FaceAnim.ASKING,
                            "Have you tried looking between here and the castle?",
                        ).also { stage++ }

                    12 -> npcl(FaceAnim.FRIENDLY, "Yeah, I tried that.").also { stage++ }
                    13 -> npcl(FaceAnim.FRIENDLY, "I need another beer.").also { stage = END_DIALOGUE }
                    20 -> npcl(FaceAnim.FRIENDLY, "I've run out of money!").also { stage++ }
                    21 -> npcl(FaceAnim.FRIENDLY, "Oh dear. I need beer.").also { stage = END_DIALOGUE }
                    30 -> npcl(FaceAnim.HAPPY, "Now you're talking! An Asgarnian Ale, please!").also { stage++ }
                    31 -> {
                        if (!removeItem(player!!, Items.ASGARNIAN_ALE_1905)) {
                            playerl(FaceAnim.FRIENDLY, "I'll go and get you one.").also { stage = END_DIALOGUE }
                        } else {
                            end()
                            animate(npc!!, Animations.EAT_OLD_829)
                            sendMessage(player!!, "You give Harold an Asgarnian Ale.")
                            sendItemDialogue(
                                player!!,
                                Items.ASGARNIAN_ALE_1905,
                                "You give Harold an Asgarnian Ale.",
                            ).also { stage++ }
                            runTask(npc!!, 5) {
                                npcl(FaceAnim.FRIENDLY, "Arrh. That hit the spot!").also { stage = END_DIALOGUE }
                            }
                        }
                    }
                }
            }
        }
    }
}

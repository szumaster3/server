package content.region.karamja.quest.mm.dialogue

import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.quest.startQuest
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests

class KingNarnodeMMDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (getQuestStage(player!!, Quests.MONKEY_MADNESS)) {
            0 ->
                when (stage) {
                    0 -> npcl("Adventurer! It is good to see you again!").also { stage++ }
                    1 -> playerl("And you too. How fares the tree?").also { stage++ }
                    2 ->
                        npcl(
                            "The Tree? It is fine, as it has always been since we foiled Glough's plans",
                        ).also { stage++ }
                    3 -> playerl("Good. What ever did happen to Glough?").also { stage++ }
                    4 ->
                        npcl(
                            "Oh, I forced him to resign. I have now appointed a new Head Tree Guardian, Daero. He is learning quickly and serves me well.",
                        ).also {
                            stage++
                        }
                    5 -> playerl("King, you look worried. Is anything the matter?").also { stage++ }
                    6 -> npcl("Nothing in particular ... Well actually yes - there is.").also { stage++ }
                    7 -> playerl("what is it?").also { stage++ }
                    8 -> npcl("Well, do you remember Glough's ship building facilities in Karamja?").also { stage++ }
                    9 -> playerl("Yes, there were in the eastern coast. What of them?").also { stage++ }
                    10 ->
                        npcl(
                            "After you defeated Glough's demon, I sent an envoy of my Royal Guard, the 10th squad, to oversee the decommission of the shipyard. They were ordered to use force if necessary.",
                        ).also {
                            stage++
                        }
                    11 -> playerl("I see. Were they successful?").also { stage++ }
                    12 ->
                        npcl(
                            "I ... I don't know. I have heard nothing from them. I do not even know if they reached the shipyard!",
                        ).also {
                            stage++
                        }
                    13 -> playerl("It is a long way...").also { stage++ }
                    14 ->
                        npcl(
                            "But I need to know what happened. These are elite soldiers - their disappearance cannot simply be ignored. I cannot wait any longer.",
                        ).also {
                            stage++
                        }
                    15 ->
                        npcl(
                            "And so i ask you: would you visit Glough's old shipyard in Karamja and find out if the 10th squad ever managed to reach?",
                        ).also {
                            stage++
                        }
                    16 -> sendDialogueOptions(player!!, "Start Monkey Madness quest?", "Yes", "No").also { stage++ }
                    17 ->
                        when (buttonID) {
                            1 -> playerl("Ok, I'll do it.").also { stage++ }
                            2 -> end()
                        }

                    18 -> {
                        addItemOrDrop(player!!, Items.GNOME_ROYAL_SEAL_4004)
                        sendItemDialogue(
                            player!!,
                            Items.GNOME_ROYAL_SEAL_4004,
                            "Narnode hands you a copy of the Royal Seal.",
                        ).also { stage++ }
                    }

                    19 ->
                        npcl(
                            "Thank you very much. You may need this Royal Seal to identify yourself as my envoy.",
                        ).also { stage++ }
                    20 -> npcl("Please report to me as soon as you have any information.").also { stage++ }
                    21 ->
                        end().also {
                            startQuest(player!!, Quests.MONKEY_MADNESS)
                            setAttribute(player!!, "/save:MonkeyMadnessPuzzleSolved", false)
                            val interfaceId = Components.QUEST_COMPLETE_SCROLL_277
                            player!!.interfaceManager.open(Component(interfaceId))
                            player!!.packetDispatch.sendItemZoomOnInterface(
                                Items.MSPEAK_AMULET_4022,
                                230,
                                interfaceId,
                                5,
                            )
                            for (i in 0..17) {
                                when (i) {
                                    3 -> sendString(player!!, "Monkey Madness: Chapter 1", interfaceId, i)
                                    9 ->
                                        sendString(
                                            player!!,
                                            "In which our ${if (player!!.isMale) "hero finds himself" else "heroine finds herself"} drawn back into Glough's",
                                            interfaceId,
                                            i,
                                        )

                                    10 -> sendString(player!!, "web of deception and deceit.", interfaceId, i)
                                    else -> sendString(player!!, "", interfaceId, i)
                                }
                            }
                        }
                }

            10 ->
                when (stage) {
                    0 -> npcl("Welcome back, adventurer.").also { stage = END_DIALOGUE }
                }

            11 ->
                when (stage) {
                    0 -> npcl("Welcome back, adventurer.").also { stage++ }
                    1 -> playerl("Hello. I investigated the shipyard.").also { stage++ }
                    2 -> npcl("Thank you for doing this. What did you find?").also { stage++ }
                    3 ->
                        playerl(
                            "I met a gnome who goes by the name of Canarock. Do you recognise it?",
                        ).also { stage++ }
                    4 -> npcl("The name sounds a little familiar, but it is nobody i know personally.").also { stage++ }
                    5 ->
                        playerl(
                            "He calls himself a Gnome Liaison Officer. He seemed a little ... odd",
                        ).also { stage++ }
                    6 -> npcl("Never mind that - Did you find anything out about my 10th squad?").also { stage++ }
                    7 ->
                        playerl(
                            "Caranock suggested they were blown off course by extremely southerly winds.",
                        ).also { stage++ }
                    8 -> npcl("Do you believe him?").also { stage++ }
                    9 -> playerl("I don't have any other information right now.").also { stage++ }
                    10 ->
                        npcl(
                            "Very well. I will now prepare some orders. You must convey them to my new High Tree Guardian, Daero.",
                        ).also {
                            stage++
                        }
                    11 ->
                        sendItemDialogue(
                            player!!,
                            Items.NARNODES_ORDERS_4005,
                            "Narnode hands you some handwritten orders.",
                        ).also {
                            addItemOrDrop(player!!, Items.NARNODES_ORDERS_4005)
                            stage++
                        }

                    12 -> playerl("Where will I find Daero?").also { stage++ }
                    13 ->
                        npcl("You will find him attending to business somewhere on the Grand Tree.").also {
                            setQuestStage(player!!, Quests.MONKEY_MADNESS, 20)
                            stage = END_DIALOGUE
                        }
                }

            96 ->
                when (stage) {
                    0 -> playerl("King Narnode!").also { stage++ }
                    1 ->
                        npcl(
                            "Yes? How is the mission going ... it has been quite some time since I sent you on your way.",
                        ).also {
                            stage++
                        }
                    2 -> playerl("It's over - it's finally over.").also { stage++ }
                    3 -> npcl("What do you men 'over'?").also { stage++ }
                    4 -> playerl("I mean 'finished'.").also { stage++ }
                    5 -> npcl("Yes, alright. Report on what happened.").also { stage++ }
                    6 ->
                        playerl(
                            "With all due respect, sir, if I told you, you would not believe me. I expect Sergeant Garkor will be sending you a full report soon enough.",
                        ).also {
                            stage++
                        }
                    7 -> npcl("And what of my 10th squad?").also { stage++ }
                    8 -> playerl("They all alive - we suffered no casualries.").also { stage++ }
                    9 -> npcl("'We', ${player!!.name}?").also { stage++ }
                    10 -> playerl("I, uh, I'm part of the 10th squad now. I even have the sigil.").also { stage++ }
                    11 ->
                        sendItemDialogue(
                            player!!,
                            Items.TENTH_SQUAD_SIGIL_4035,
                            "You show King Narnode your sigil.",
                        ).also { stage++ }

                    12 ->
                        npcl(
                            "Well, now. It appears I cannot argue with that. Garkor obviously thinks highly of you, as do I.",
                        ).also {
                            stage++
                        }
                    13 ->
                        npcl(
                            "No service such as what you have done for me goes unrewarded in my kingdom. I personally made a visit to the Royal Treasury to withdraw your reward.",
                        ).also {
                            stage++
                        }
                    14 ->
                        sendItemDialogue(
                            player!!,
                            arrayOf(Items.DIAMOND_1601, Items.COINS_617),
                            "King Narnode hands you a huge stack of old coins and several diamonds!",
                        ).also {
                            addItemOrDrop(player!!, Items.DIAMOND_1601, 3)
                            addItemOrDrop(player!!, Items.COINS_617, 10000)
                            finishQuest(player!!, Quests.MONKEY_MADNESS)
                            stage = 99
                        }

                    99 -> end()
                }

            100 ->
                when (stage) {
                    0 -> npcl("So you're officially a member of the 10th squad then?").also { stage++ }
                    1 -> playerl("I suppose so...").also { stage++ }
                    2 -> npcl("Well then you had better sign up for training.").also { stage++ }
                    3 -> playerl("Training?").also { stage++ }
                    4 ->
                        npcl(
                            "Yes. All members of the Royal Guard must complete a mandatory training programme.",
                        ).also { stage++ }
                    5 -> playerl("Where do I sign up for this?").also { stage++ }
                    6 ->
                        npcl(
                            "The High Tree Guardian Daero is in charge of the training programme. You should know where to find him by now.",
                        ).also {
                            stage = 99
                        }

                    99 -> end()
                }
        }
    }
}

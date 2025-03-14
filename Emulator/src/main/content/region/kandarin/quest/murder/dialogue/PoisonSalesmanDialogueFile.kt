package content.region.kandarin.quest.murder.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class PoisonSalesmanDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questName = Quests.MURDER_MYSTERY
        val questStage = getQuestStage(player!!, questName)
        npc = NPC(NPCs.POISON_SALESMAN_820)

        when {
            (questStage >= 2) -> {
                when (stage) {
                    0 ->
                        playerl(
                            FaceAnim.SUSPICIOUS,
                            "I'm investigating the murder at the Sinclair house.",
                        ).also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "There was a murder at the Sinclair House??? That's terrible! And I was only there the other day too! They bought the last of my Patented Multi Purpose Poison!",
                        ).also {
                            stage++
                        }
                    2 -> {
                        if (inInventory(player!!, Items.PUNGENT_POT_1812)) {
                            options(
                                "Patented Multi Purpose Poison?",
                                "Who did you sell Poison to at the house?",
                                "Can I buy some Poison?",
                                "I have this pot I found at the murder scene...",
                            ).also {
                                stage++
                            }
                        } else {
                            options(
                                "Patented Multi Purpose Poison?",
                                "Who did you sell Poison to at the house?",
                                "Can I buy some Poison?",
                            ).also {
                                stage++
                            }
                        }
                    }

                    3 -> {
                        if (inInventory(player!!, Items.PUNGENT_POT_1812)) {
                            when (buttonID) {
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Patented Multi Purpose Poison?").also { stage++ }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Who did you sell Poison to at the house?").also {
                                        stage =
                                            14
                                    }
                                3 -> playerl(FaceAnim.SUSPICIOUS, "Can I buy some Poison?").also { stage = 18 }
                                4 ->
                                    playerl(
                                        FaceAnim.SUSPICIOUS,
                                        "I have this pot I found at the murder scene...",
                                    ).also {
                                        stage =
                                            22
                                    }
                            }
                        } else {
                            when (buttonID) {
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Patented Multi Purpose Poison?").also { stage++ }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Who did you sell Poison to at the house?").also {
                                        stage =
                                            14
                                    }
                                3 -> playerl(FaceAnim.SUSPICIOUS, "Can I buy some Poison?").also { stage = 18 }
                            }
                        }
                    }

                    4 -> npcl(FaceAnim.NEUTRAL, "Aaaaah... a miracle of modern apothecaries!").also { stage++ }
                    5 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "This exclusive concoction has been tested on all known forms of life and been proven to kill them all in varying dilutions from cockroaches to king dragons!",
                        ).also {
                            stage++
                        }

                    6 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "So incredibly versatile, it can be used as pest control, a cleansing agent, drain cleaner, metal polish and washes whiter than white,",
                        ).also {
                            stage++
                        }

                    7 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "all with our uniquely fragrant concoction that is immediately recognisable across the land as Peter Potter's Patented Poison potion!!!",
                        ).also {
                            stage++
                        }

                    8 -> sendDialogue(player!!, "The salesman stops for breath.").also { stage++ }
                    9 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "I'd love to sell you some but I've sold out recently. That's just how good it is! Three hundred and twenty eight people in this area alone cannot be wrong!",
                        ).also {
                            stage++
                        }

                    10 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Nine out of Ten poisoners prefer it in controlled tests!",
                        ).also { stage++ }

                    11 -> npcl(FaceAnim.NEUTRAL, "Can I help you with anything else?").also { stage++ }
                    12 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Perhaps I can take your name and add it to our mailing list of poison users? We will only send you information related to the use of poison and other Peter Potter Products!",
                        ).also {
                            stage++
                        }

                    13 -> playerl(FaceAnim.SUSPICIOUS, "Uh... no, it's ok. Really.").also { stage = END_DIALOGUE }
                    14 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Well, Peter Potter's Patented Multi Purpose Poison is a product of such obvious quality that I am glad to say I managed to sell a bottle to each of the Sinclairs!",
                        ).also {
                            stage++
                        }
                    15 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Anna, Bob, Carol, David, Elizabeth and Frank all bought a bottle! In fact they bought the last of my supplies!",
                        ).also {
                            stage++
                        }
                    16 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Maybe I can take your name and address and I will personally come and visit you when stocks return?",
                        ).also {
                            stage++
                        }
                    17 -> playerl(FaceAnim.SUSPICIOUS, "Uh... no, it's ok.").also { stage = END_DIALOGUE }
                    18 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "I'm afraid I am totally out of stock at the moment after my successful trip to the Sinclairs' House the other day.",
                        ).also {
                            stage++
                        }
                    19 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "But don't worry! Our factories are working overtime to produce Peter Potter's Patented Multi Purpose Poison!",
                        ).also {
                            stage++
                        }
                    20 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Possibly the finest multi purpose poison and cleaner yet available to the general market.",
                        ).also {
                            stage++
                        }
                    21 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "And its unique fragrance makes it the number one choice for cleaners and exterminators the whole country over!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    22 ->
                        sendItemDialogue(
                            player!!,
                            Items.PUNGENT_POT_1812,
                            "You show the poison salesman the pot you found at the murder scene with the unusual smell.",
                        ).also {
                            stage++
                        }
                    23 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Hmmm... yes, that smells exactly like my Patented Multi Purpose Poison, but I don't see how it could be. It quite clearly says on the label of all bottles",
                        ).also {
                            stage++
                        }
                    24 -> npcl(FaceAnim.NEUTRAL, "'Not to be taken internally - EXTREMELY POISONOUS'.").also { stage++ }
                    25 -> playerl(FaceAnim.SUSPICIOUS, "Perhaps someone else put it in his wine?").also { stage++ }
                    26 -> npcl(FaceAnim.NEUTRAL, "Yes... I suppose that could have happened...").also { stage++ }
                    27 -> {
                        end()
                        setQuestStage(player!!, Quests.MURDER_MYSTERY, 3)
                    }
                }
            }

            (isQuestComplete(player!!, Quests.MURDER_MYSTERY)) ->
                when (stage) {
                    0 ->
                        npcl(FaceAnim.FRIENDLY, "I hear you're pretty smart to have solved the Sinclair Murder!").also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
    }
}

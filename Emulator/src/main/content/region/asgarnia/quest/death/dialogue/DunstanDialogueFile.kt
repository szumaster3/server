package content.region.asgarnia.quest.death.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.Quests

class DunstanDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (getQuestStage(player!!, Quests.DEATH_PLATEAU)) {
            21 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Hi! How can I help?").also { stage++ }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Tenzing has asked me to bring you his climbing boots, he needs to have spikes put on them.",
                        ).also {
                            stage++
                        }

                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "He does, does he? Well I won't do it till he pays for the last set I made for him!",
                        ).also {
                            stage++
                        }

                    4 -> playerl(FaceAnim.FRIENDLY, "This is really important!").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "How so?").also { stage++ }
                    6 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Well, I need the Sherpa to show me a secret way up Death Plateau so that the Imperial Guard can destroy the troll camp! He won't help me till I've got the spikes!",
                        ).also {
                            stage++
                        }

                    7 -> npcl(FaceAnim.FRIENDLY, "Hmm. That's different!").also { stage++ }
                    8 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Tell you what, I'll make them for you on one condition.",
                        ).also { stage++ }

                    9 -> playerl(FaceAnim.FRIENDLY, "*sigh* What's the condition?").also { stage++ }
                    10 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "My son has just turned 16 and I'd very much like him to join the Imperial Guard. The Prince's elite forces are invite only so it's very unlikely he'll get in. If you can arrange that you have a deal!",
                        ).also {
                            stage++
                        }

                    11 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "That won't be a problem as I'm helping out the Imperial Guard!",
                        ).also { stage++ }

                    12 ->
                        npcl(FaceAnim.FRIENDLY, "Excellent! You'll need to bring an Iron bar for the spikes!").also {
                            setQuestStage(player!!, Quests.DEATH_PLATEAU, 22)
                            stage = END_DIALOGUE
                        }
                }
            }

            22 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Have you managed to get my son signed up for the Imperial Guard?",
                        ).also { stage++ }

                    2 ->
                        playerl(FaceAnim.FRIENDLY, "Not yet! I just need to speak to Denulth!").also {
                            stage = END_DIALOGUE
                        }
                }
            }

            23 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Have you managed to get my son signed up for the Imperial Guard?",
                        ).also { stage++ }

                    2 -> {
                        if (!inInventory(player!!, Items.CERTIFICATE_3114)) {
                            playerl(
                                FaceAnim.FRIENDLY,
                                "I have but I don't have the entrance certificate on me.",
                            ).also { stage++ }
                        } else {
                            sendItemDialogue(
                                player!!,
                                Items.CERTIFICATE_3114,
                                "You give Dunstan the certificate.",
                            ).also { stage = 5 }
                        }
                    }

                    3 ->
                        npcl(FaceAnim.FRIENDLY, "Good but I need to have the certificate.").also {
                            stage = END_DIALOGUE
                        }

                    5 ->
                        npcl(FaceAnim.FRIENDLY, "Thank you!").also {
                            setQuestStage(player!!, Quests.DEATH_PLATEAU, 24)
                            sendMessage(player!!, "You give Dunstan the certificate.")
                            stage = 2
                        }
                }
            }

            24 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Now to keep my end of the bargain. Give me the boots and an iron bar and I'll put on the spikes.",
                        ).also {
                            if (inInventory(player!!, Items.CLIMBING_BOOTS_3105) &&
                                inInventory(
                                    player!!,
                                    Items.IRON_BAR_2351,
                                )
                            ) {
                                stage = 7
                            } else if (inInventory(player!!, Items.CLIMBING_BOOTS_3105)) {
                                stage = 3
                            } else if (inInventory(player!!, Items.IRON_BAR_2351)) {
                                stage = 4
                            } else {
                                stage = 5
                            }
                        }

                    3 -> playerl(FaceAnim.FRIENDLY, "I don't have the iron bar.").also { stage = END_DIALOGUE }
                    4 -> playerl(FaceAnim.FRIENDLY, "I don't have the climbing boots.").also { stage = END_DIALOGUE }
                    5 ->
                        playerl(FaceAnim.FRIENDLY, "I don't have the iron bar or the climbing boots.").also {
                            stage = END_DIALOGUE
                        }

                    7 ->
                        sendDoubleItemDialogue(
                            player!!,
                            Items.IRON_BAR_2351,
                            Items.CLIMBING_BOOTS_3105,
                            "You give Dunstan an Iron bar and the climbing boots.",
                        ).also {
                            if (removeItem(player!!, Item(Items.CLIMBING_BOOTS_3105)) &&
                                removeItem(
                                    player!!,
                                    Item(Items.IRON_BAR_2351),
                                )
                            ) {
                                sendMessage(player!!, "You give Dunstan an Iron bar and the climbing boots.")
                                stage++
                            } else {
                                stage = END_DIALOGUE
                            }
                        }

                    8 ->
                        sendItemDialogue(
                            player!!,
                            Items.SPIKED_BOOTS_3107,
                            "Dunstan has given you the Spiked boots.",
                        ).also {
                            stage++
                            addItemOrDrop(player!!, Items.SPIKED_BOOTS_3107)
                        }

                    9 -> playerl(FaceAnim.FRIENDLY, "Thank you!").also { stage++ }
                    10 ->
                        npcl(FaceAnim.FRIENDLY, "No problem.").also {
                            sendMessage(player!!, "Dunstan has given you the Spiked boots.")
                            stage = END_DIALOGUE
                        }
                }
            }
        }
    }
}

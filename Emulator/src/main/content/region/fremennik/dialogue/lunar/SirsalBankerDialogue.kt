package content.region.fremennik.dialogue.lunar

import core.api.*
import core.api.interaction.hasAwaitingGrandExchangeCollections
import core.api.interaction.openBankAccount
import core.api.interaction.openBankPinSettings
import core.api.interaction.openGrandExchangeCollectionBox
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

class SirsalBankerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                if (hasSealOfPassage(player)) {
                    if (hasIronmanRestriction(player, IronmanMode.ULTIMATE)) {
                        npcl(
                            FaceAnim.NEUTRAL,
                            "My apologies, dear ${if (player.isMale) "sir" else "madam"}, " +
                                "our services are not available for Ultimate ${if (player.isMale) "Ironman" else "Ironwoman"}",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    } else {
                        npcl(FaceAnim.NEUTRAL, "Good day, how may I help you?").also {
                            if (hasAwaitingGrandExchangeCollections(player)) {
                                stage++
                            } else {
                                stage += 2
                            }
                        }
                    }
                } else {
                    playerl(FaceAnim.HALF_WORRIED, "Hi, I...")
                    stage = 30
                }

            1 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Before we go any further, I should inform you that you " +
                        "have items ready for collection from the Grand Exchange.",
                ).also { stage++ }
            2 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "I'd like to access my bank account, please.", 10),
                    Topic(FaceAnim.NEUTRAL, "I'd like to check my PIN settings.", 11),
                    Topic(FaceAnim.NEUTRAL, "I'd like to collect items.", 12),
                    Topic(FaceAnim.ASKING, "What is this place?", 3),
                )
            3 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "This is a branch of the Bank of Gielinor. We have branches in many towns.",
                ).also {
                    stage++
                }
            4 -> playerl(FaceAnim.ASKING, "And what do you do?").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "We will look after your items and money for you. " +
                        "Leave your valuables with us if you want to keep them safe.",
                ).also { stage = END_DIALOGUE }
            10 -> {
                openBankAccount(player)
                end()
            }

            11 -> {
                openBankPinSettings(player)
                end()
            }

            12 -> {
                openGrandExchangeCollectionBox(player)
                end()
            }
            30 -> npcl(FaceAnim.ANNOYED, "What are you doing here, Fremennik?!").also { stage++ }
            31 -> playerl(FaceAnim.WORRIED, "I have a Seal of Pass...").also { stage++ }
            32 -> npcl(FaceAnim.ANGRY, "No you don't! Begone!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SirsalBankerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIRSAL_BANKER_4519)
    }
}

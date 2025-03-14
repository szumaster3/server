package content.region.asgarnia.dialogue.burthope

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

/**
 * Represents the Jade dialogue.
 */
class JadeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                if (hasIronmanRestriction(player, IronmanMode.ULTIMATE)) {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Greetings, warrior. I wish I could help you, but " +
                            "our services are not available for Ultimate ${if (player.isMale) "Ironman" else "Ironwoman"}.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    npcl(FaceAnim.NEUTRAL, "Greetings warrior, how may I help you?").also {
                        if (hasAwaitingGrandExchangeCollections(player)) {
                            stage++
                        } else {
                            stage += 2
                        }
                    }
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
                    Topic(FaceAnim.NEUTRAL, "I'd like to see my collection box.", 12),
                    Topic(FaceAnim.ASKING, "How long have you worked here?", 3),
                )

            3 -> npcl(FaceAnim.FRIENDLY, "Oh, ever since the Guild opened. I like it here.").also { stage++ }
            4 -> playerl(FaceAnim.ASKING, "Why's that?").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well... What with all these warriors around, there's not much chance of my bank being robbed, is there?",
                ).also {
                    stage =
                        2
                }
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
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.JADE_4296)
}

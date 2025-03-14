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
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Emerald benedict dialogue.
 */
@Initializable
class EmeraldBenedictDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                if (hasIronmanRestriction(player, IronmanMode.ULTIMATE)) {
                    npcl(FaceAnim.ANNOYED, "Get lost, tin can.").also { stage = END_DIALOGUE }
                } else {
                    npcl(FaceAnim.SUSPICIOUS, "Got anything you don't want to lose?").also {
                        if (hasAwaitingGrandExchangeCollections(player)) {
                            stage++
                        } else {
                            stage += 2
                        }
                    }
                }
            1 ->
                npcl(
                    FaceAnim.SUSPICIOUS,
                    "By the way, a little bird told me you got some stuff waiting for you " + "on the Grand Exchange.",
                ).also { stage++ }
            2 ->
                showTopics(
                    Topic(FaceAnim.ASKING, "Yes, actually. Can you help?", 3),
                    Topic(FaceAnim.ASKING, "Yes, but can you show me my PIN settings?", 5),
                    Topic(FaceAnim.ASKING, "Yes, but can you show me my collection box?", 6),
                    Topic(FaceAnim.ANNOYED, "Yes, thanks. And I'll keep hold of it too.", END_DIALOGUE),
                )
            3 -> {
                openBankAccount(player)
                end()
            }
            5 -> {
                openBankPinSettings(player)
                end()
            }
            6 -> {
                openGrandExchangeCollectionBox(player)
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.EMERALD_BENEDICT_2271)
}

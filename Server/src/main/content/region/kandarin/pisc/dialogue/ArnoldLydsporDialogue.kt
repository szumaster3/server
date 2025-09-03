package content.region.kandarin.pisc.dialogue

import core.api.addItemOrDrop
import core.api.hasIronmanRestriction
import core.api.openBankAccount
import core.api.openBankPinSettings
import core.api.openGrandExchangeCollectionBox
import core.api.openNpcShop
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Arnold Lydspor dialogue.
 */
class ArnoldLydsporDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.FRIENDLY, "Ah, you come back! What you want from Arnold, heh?").also { stage++ }
            1 -> showTopics(
                IfTopic(FaceAnim.ASKING, "Can you open my bank account, please?", 2, !hasIronmanRestriction(player, IronmanMode.ULTIMATE)),
                IfTopic(FaceAnim.NEUTRAL, "I'd like to check my bank PIN settings.", 3, !hasIronmanRestriction(player, IronmanMode.ULTIMATE)),
                IfTopic(FaceAnim.NEUTRAL, "I'd like to collect items.", 4, !hasIronmanRestriction(player, IronmanMode.ULTIMATE)),
                Topic(FaceAnim.ASKING, "Would you like to trade?", 5),
                Topic(FaceAnim.FRIENDLY, "Nothing, I just came to chat.", 7),
            )
            2 -> {
                openBankAccount(player)
                end()
            }

            3 -> {
                openBankPinSettings(player)
                end()
            }

            4 -> {
                openGrandExchangeCollectionBox(player)
                end()
            }
            5 -> npcl(FaceAnim.FRIENDLY, "Ja, I have wide range of stock...").also { stage++ }
            6 -> {
                openNpcShop(player, NPCs.ARNOLD_LYDSPOR_3824)
                end()
            }
            7 -> npcl(FaceAnim.FRIENDLY, "Ah, that is nice - always I like to chat, but " + "Herr Caranos tell me to get back to work! " + "Here, you been nice, so have a present.").also { stage++ }
            8 -> sendItemDialogue(player, Items.CABBAGE_1965, "Arnold gives you a cabbage.").also {
                addItemOrDrop(player, Items.CABBAGE_1965)
                stage++
            }
            9 -> playerl(FaceAnim.HALF_THINKING, "A cabbage?").also { stage++ }
            10 -> npcl(FaceAnim.HAPPY, "Ja, cabbage is good for you!").also { stage++ }
            11 -> playerl(FaceAnim.NEUTRAL, "Um... Thanks!").also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ARNOLD_LYDSPOR_3824)
}

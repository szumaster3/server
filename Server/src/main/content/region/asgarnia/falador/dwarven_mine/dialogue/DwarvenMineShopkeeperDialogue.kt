package content.region.asgarnia.falador.dwarven_mine.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Dwarven Shopkeeper dialogue.
 */
class DwarvenMineShopkeeperDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.OLD_HAPPY, "Can I help you at all?").also { stage++ }
            1 -> showTopics(
                Topic("Yes please, what are you selling?", 3),
                Topic("How should I use your shop?", 2),
                Topic("No, thanks.", END_DIALOGUE)
            )
            2 -> npc(FaceAnim.OLD_HAPPY, "I'm glad you ask! You can buy as many of the items", "stocked as you wish. You can also sell most items to", "the shop.").also { stage = 1 }
            3 -> {
                end()
                openNpcShop(player!!, NPCs.DWARF_582)
            }
        }
    }
}

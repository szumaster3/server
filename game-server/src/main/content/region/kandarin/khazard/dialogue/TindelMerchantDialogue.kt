package content.region.kandarin.khazard.dialogue

import content.region.kandarin.khazard.plugin.TindelMerchantPlugin
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs

/**
 * Represents the Tindel Merchant dialogue.
 */
class TindelMerchantDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.TINDEL_MARCHANT_1799)
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Hello there! Welcome to my special antiques boutique.").also { stage++ }
            1 -> showTopics(
                Topic("What do you do here?", 2),
                Topic("What's involved?", 5),
                Topic("What do I get from this?", 8),
                Topic(FaceAnim.HAPPY, "Ok, I'll give it a go!", 11),
                Topic(FaceAnim.FRIENDLY, "Ok, thanks.", 10)
            )
            2 -> npcl(FaceAnim.FRIENDLY, "I'm a specialist at identifying exotic and antique weapons, specifically swords, but I plan to branch out.").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "If you have any old and rusty weapons that you want me to check out, just show them to me, pay me 100 Gold and I'll see if you have an antique on your hands.").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "I can also repair some antique weapons and armours, just show me the item and I'll let you know if I can repair it and for how much.").also { stage = 1 }
            5 -> npcl(FaceAnim.THINKING, "Well, pay me 100 Gold and I'll see if any rusty swords that you've found are actually worth anything.").also { stage++ }
            6 -> npcl(FaceAnim.FRIENDLY, "Some of them might be worth some money! If it's an antique item though, I reserve the right to purchase it immediately for adding to my own personal collection.").also { stage++ }
            7 -> npcl(FaceAnim.HAPPY, "I'll give you fair price for it.").also { stage = 1 }
            8 -> npcl(FaceAnim.FRIENDLY, "If I can reclaim the sword with my own specialist skills, I'll return it to you in peak condition. If it's an antique, I'll just give you what I think it's worth and I generally pay quite well.").also { stage++ }
            9 -> npcl(FaceAnim.FRIENDLY, "However, if it's just a piece of junk, I'll simply give you the bad news and get rid of the item for you.").also { stage = 1 }
            10 -> end()
            11 -> {
                end()
                TindelMerchantPlugin.exchangeRustyWeapon(player!!)
            }
        }
    }
}

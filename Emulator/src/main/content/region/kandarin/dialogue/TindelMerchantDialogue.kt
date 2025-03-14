package content.region.kandarin.dialogue

import content.region.kandarin.handlers.TindelMerchantListener
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class TindelMerchantDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.TINDEL_MARCHANT_1799)
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Hello there! Welcome to my special antiques boutique.").also { stage++ }
            1 ->
                options(
                    "What do you do here?",
                    "What's involved?",
                    "What do I get from this?",
                    "Ok, I'll give it a go!",
                    "Ok, thanks.",
                ).also {
                    stage++
                }
            2 ->
                when (buttonID) {
                    1 -> player("What do you do here?").also { stage++ }
                    2 -> player("What's involved?").also { stage = 5 }
                    3 -> player("What do I get from this?").also { stage = 7 }
                    4 -> player("Ok, I'll give it a go!").also { stage = 9 }
                    5 -> player("Ok, thanks.").also { stage = END_DIALOGUE }
                }
            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I'm a specialist at identifying exotic and antique weapons, specifically swords, but I plan to branch out. If you have any old and rusty weapons that you want me to check out, just show them to",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "me, pay me 100 Gold and I'll see if you have an antique on your hands. I can also repair some antique weapons and armours, just show me the item and I'll let you know if I can repair it and for how much.",
                ).also {
                    stage =
                        1
                }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, pay me 100 Gold and I'll see if any rusty swords that you've found are actually worth anything. Some of them might be worth some money! If it's an antique item though, I reserve the",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "right to purchase it immediately for adding to my own personal collection. I'll give you fair price for it.",
                ).also {
                    stage =
                        1
                }
            7 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "If I can reclaim the sword with my own specialist skills, I'll return it to you in peak condition. If it's an antique, I'll just give you what I think it's worth and I generally pay quite well. However, if",
                ).also {
                    stage++
                }
            8 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "it's just a piece of junk, I'll simply give you the bad news and get rid of the item for you.",
                ).also {
                    stage =
                        1
                }
            9 -> end().also { TindelMerchantListener.exchangeRustyWeapon(player!!) }
        }
    }
}

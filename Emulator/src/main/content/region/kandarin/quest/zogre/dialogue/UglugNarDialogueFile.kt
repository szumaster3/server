package content.region.kandarin.quest.zogre.dialogue

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Vars

class UglugNarDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.UGLUG_NAR_2039)
        when (stage) {
            0 -> sendItemDialogue(player!!, Items.RELICYMS_BALM3_4844, "You show the potion to Uglug Nar.")
            1 ->
                player(
                    "Hey, here you go! I brought you some of the potion",
                    "which should cure the disease. You said that you would",
                    "buy some from me.",
                ).also {
                    stage++
                }
            2 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Yous creatures done da good finf... yous get many",
                    "bright pretties for dis...!",
                ).also {
                    stage++
                }
            3 ->
                if (removeItem(player!!, Items.RELICYMS_BALM3_4844)) {
                    addItemOrDrop(player!!, Items.COINS_995, 1000)
                    sendDoubleItemDialogue(
                        player!!,
                        Items.COINS_8896,
                        Items.RELICYMS_BALM3_4844,
                        "You sell the potion and get 1000 coins in return.",
                    )
                    setVarbit(player!!, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 7, true)
                }
        }
    }
}

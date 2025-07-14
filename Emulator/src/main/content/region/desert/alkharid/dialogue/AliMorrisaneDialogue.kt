package content.region.desert.alkharid.dialogue

import content.region.desert.alkharid.quest.feud.dialogue.AliMorrisaneFeudDialogue
import core.api.getVarbit
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.shops.Shops
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Vars

/**
 * Represents the Ali Morrisane dialogue.
 */
@Initializable
class AliMorrisaneDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Good day and welcome back to Al Kharid.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "Hello to you too.").also { stage++ }
            1 -> npc(FaceAnim.FRIENDLY, "My name is Ali Morrisane - the greatest salesman in", "the world.").also { stage++ }
            2 -> options("If you are, then why are you still selling goods from a stall?", "So what are you selling then?").also { stage++ }
            3 -> when (buttonId) {
                1 -> player(FaceAnim.ASKING, "If you are then why are you still selling goods from a", "stall?").also { stage = 10 }
                2 -> {
                    end()
                    if (getVarbit(player, Vars.VARBIT_QUEST_THE_FEUD_PROGRESS_334) < 1) {
                        return true
                    }
                    Shops.openId(player, 107)
                }
            }
            10 -> {
                end()
                openDialogue(player, AliMorrisaneFeudDialogue())
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = AliMorrisaneDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ALI_MORRISANE_1862)
}

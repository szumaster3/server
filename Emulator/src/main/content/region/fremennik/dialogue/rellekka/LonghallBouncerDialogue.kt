package content.region.fremennik.dialogue.rellekka

import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LonghallBouncerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npcl(FaceAnim.ANNOYED, "Hey, outerlander. You can't go through there. Talent only, backstage.").also {
                stage =
                    END_DIALOGUE
            }
        } else {
            npcl(FaceAnim.ANNOYED, "You can't go through there. Talent only, backstage.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.PANICKED, "But I'm a Bard!").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "No you're not. I saw your performance. I was paid well to keep you from ever setting foot on stage here again.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return LonghallBouncerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LONGHALL_BOUNCER_1278)
    }
}

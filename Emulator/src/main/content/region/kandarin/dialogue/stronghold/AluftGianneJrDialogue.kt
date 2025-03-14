package content.region.kandarin.dialogue.stronghold

import content.minigame.gnomecook.handlers.GC_BASE_ATTRIBUTE
import content.minigame.gnomecook.handlers.GC_TUT_PROG
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AluftGianneJrDialogue(
    player: Player? = null,
) : Dialogue(player) {
    var tutorialStage = -1

    override fun open(vararg args: Any?): Boolean {
        tutorialStage = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_TUT_PROG", -1)
        if (tutorialStage == -1) {
            player("Hey can I get a job here?")
        } else {
            npc(FaceAnim.OLD_NORMAL, "Having fun?").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_NORMAL, "Sure, go talk to my dad. I'll put", "a good word in!").also { stage++ }
            1 -> player(FaceAnim.THINKING, "Th-thanks...?").also { stage++ }
            2 -> {
                end()
                setAttribute(player, "/save:$GC_BASE_ATTRIBUTE:$GC_TUT_PROG", 0)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AluftGianneJrDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GIANNE_JNR_4572)
    }
}

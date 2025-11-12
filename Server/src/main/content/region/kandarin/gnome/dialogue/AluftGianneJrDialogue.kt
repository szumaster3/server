package content.region.kandarin.gnome.dialogue

import content.minigame.gnomecooking.plugin.GC_BASE_ATTRIBUTE
import content.minigame.gnomecooking.plugin.GC_TUT_PROG
import core.api.setAttribute
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Aluft Gianne Junior dialogue.
 */
@Initializable
class AluftGianneJrDialogue(player: Player? = null) : Dialogue(player) {
    
    var tutorialStage = -1

    override fun open(vararg args: Any?): Boolean {
        tutorialStage = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_TUT_PROG", -1)
        if (tutorialStage == -1) {
            player("Hey can I get a job here?")
        } else {
            npc("Having fun?").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc("Sure, go talk to my dad. I'll put", "a good word in!").also { stage++ }
            1 -> player(FaceAnim.THINKING, "Th-thanks...?").also { stage++ }
            2 -> {
                end()
                setAttribute(player, "/save:$GC_BASE_ATTRIBUTE:$GC_TUT_PROG", 0)
            }
        }
        return true
    }

    override fun npc(vararg messages: String?): Component {
        return super.npc(FaceAnim.OLD_NORMAL,*messages)
    }

    override fun newInstance(player: Player?): Dialogue = AluftGianneJrDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GIANNE_JNR_4572)
}
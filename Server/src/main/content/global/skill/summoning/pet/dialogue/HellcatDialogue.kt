package content.global.skill.summoning.pet.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Hellcat dialogue.
 */
@Initializable
class HellcatDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = HellcatDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npcl(FaceAnim.CHILD_NORMAL, "Miaow! I'm ready for action!")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "How are you doing?")
                stage++
            }
            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Feeling stronger and more mischievous than ever!")
                stage++
            }
            2 -> {
                playerl(FaceAnim.FRIENDLY, "How old are you now?")
                stage++
            }
            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I've grown bigger, but still quick on my paws.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HELLCAT_3504)
}
package content.global.skill.summoning.objectpets.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Hell kitten dialogue.
 */
@Initializable
class HellKittenDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = HellKittenDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npcl(FaceAnim.CHILD_NORMAL, "Meow! I'm ready to take on some pesky rats!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "How are you doing?")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Great, I'm ready for action!")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "How old are you now?")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Still young and fresh, not like you at all.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(
        NPCs.HELL_KITTEN_3505
    )
}
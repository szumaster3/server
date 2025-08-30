package content.global.skill.summoning.pet.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Wily hellcat dialogue.
 */
@Initializable
class WilyHellcatDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = WilyHellcatDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.CHILD_NORMAL, "Meeow! I'm ready to take on the world and its dogs!")
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
                npcl(FaceAnim.CHILD_NORMAL, "I'm as happy as a demon in a lava pit!")
                stage++
            }
            2 -> {
                playerl(FaceAnim.FRIENDLY, "How old are you now?")
                stage++
            }
            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm feeling a bit like a ghost in a cake shop.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.WILY_HELLCAT_3507)
}
package content.global.skill.summoning.objectpets.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Lazy hellcat dialogue.
 */
@Initializable
class LazyHellcatDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = LazyHellcatDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.CHILD_NORMAL, "Curling up on a nice rug... or maybe in the fire. Either way is good.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "How are you doing?")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Old, but still ready to scratch your cheeky face!")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "How old are you now?")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Old enough to take life slow, young whippersnapper.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.LAZY_HELLCAT_3506)
}
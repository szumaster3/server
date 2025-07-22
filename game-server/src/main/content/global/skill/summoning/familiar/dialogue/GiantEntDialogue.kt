package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Giant ent dialogue.
 */
@Initializable
class GiantEntDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = GiantEntDialogue(player)

    /**
     * Instantiates a new Giant ent dialogue.
     */
    constructor()

    /**
     * Instantiates a new Giant ent dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 8).toInt()) {
            0 -> {
                npc(FaceAnim.CHILD_NORMAL, "Creeeeeeeeeeeak.....", "(I.....)")
                stage = 0
            }

            1 -> {
                npc(FaceAnim.CHILD_NORMAL, "Creak..... Creaaaaaaaaak.....", "(Am.....)")
                stage = 0
            }

            2 -> {
                npc(FaceAnim.CHILD_NORMAL, "Grooooooooan.....", "(Feeling.....)")
                stage = 3
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Groooooooooan.....", "(Sleepy.....)")
                stage = 4
            }

            4 -> {
                npc(FaceAnim.CHILD_NORMAL, "Grooooooan.....creeeeeeeak", "(Restful.....)")
                stage = 4
            }

            5 -> {
                npc(FaceAnim.CHILD_NORMAL, "Grrrrooooooooooooooan.....", "(Achey.....)")
                stage = 4
            }

            6 -> {
                npc(FaceAnim.CHILD_NORMAL, "Creeeeeeeegroooooooan.....", "(Goood.....)")
                stage = 4
            }

            7 -> {
                npc(FaceAnim.CHILD_NORMAL, "Creeeeeeeeeeeeeaaaaaak.....", "(Tired.....)")
                stage = 4
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.ASKING, "Yes?")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, ".....")
                stage++
            }

            2 -> {
                sendDialogue("After a while you realise that the ent has finished speaking for the moment.")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(FaceAnim.ASKING, "Yes? We almost have a full sentence now - the suspense is killing me!")
                stage = 1
            }

            4 -> {
                playerl(FaceAnim.ASKING, "I'm not sure if that was worth all the waiting.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GIANT_ENT_6800, NPCs.GIANT_ENT_6801)
}

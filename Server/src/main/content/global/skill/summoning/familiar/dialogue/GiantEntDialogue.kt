package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Giant Ent familiar dialogues.
 */
@Initializable
class GiantEntDialogue : Dialogue {
    private var branch: Int = 0

    override fun newInstance(player: Player?) = GiantEntDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (0..7).random()
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> {
                    npc(FaceAnim.CHILD_NORMAL, "Creeeeeeeeeeeak.....", "(I.....)")
                    stage++
                }
                1 -> {
                    playerl(FaceAnim.ASKING, "Yes?")
                    stage++
                }
                2 -> {
                    npcl(FaceAnim.CHILD_NORMAL, ".....")
                    stage++
                }
                3 -> {
                    sendDialogue("After a while you realise that the ent has finished speaking for the moment.")
                    stage = END_DIALOGUE
                }
            }

            1 -> when (stage) {
                0 -> {
                    npc(FaceAnim.CHILD_NORMAL, "Creak..... Creaaaaaaaaak.....", "(Am.....)")
                    stage++
                }
                1 -> {
                    playerl(FaceAnim.ASKING, "Yes?")
                    stage++
                }
                2 -> {
                    npcl(FaceAnim.CHILD_NORMAL, ".....")
                    stage++
                }
                3 -> {
                    sendDialogue("After a while you realise that the ent has finished speaking for the moment.")
                    stage = END_DIALOGUE
                }
            }

            2 -> when (stage) {
                0 -> {
                    npc(FaceAnim.CHILD_NORMAL, "Grooooooooan.....", "(Feeling.....)")
                    stage++
                }
                1 -> {
                    playerl(FaceAnim.ASKING, "Yes? We almost have a full sentence now - the suspense is killing me!")
                    stage = 2
                }
                2 -> {
                    npcl(FaceAnim.CHILD_NORMAL, ".....")
                    stage++
                }
                3 -> {
                    sendDialogue("After a while you realise that the ent has finished speaking for the moment.")
                    stage = END_DIALOGUE
                }
            }

            3,4,5,6,7 -> when (stage) {
                0 -> {
                    val text = when(branch) {
                        3 -> "Groooooooooan....." to "(Sleepy.....)"
                        4 -> "Grooooooan.....creeeeeeeak" to "(Restful.....)"
                        5 -> "Grrrrooooooooooooooan....." to "(Achey.....)"
                        6 -> "Creeeeeeeegroooooooan....." to "(Goood.....)"
                        7 -> "Creeeeeeeeeeeeeaaaaaak....." to "(Tired.....)"
                        else -> "" to ""
                    }
                    npc(FaceAnim.CHILD_NORMAL, text.first, text.second)
                    stage++
                }
                1 -> {
                    playerl(FaceAnim.ASKING, "I'm not sure if that was worth all the waiting.")
                    stage = END_DIALOGUE
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GIANT_ENT_6800, NPCs.GIANT_ENT_6801)
}
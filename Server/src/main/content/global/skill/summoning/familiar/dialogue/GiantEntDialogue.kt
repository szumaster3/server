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
    private var branch: Int = -1

    override fun newInstance(player: Player?) = GiantEntDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (0..7).random()
        stage = 0

        when (branch) {
            0 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Creeeeeeeeeeeak.....", "(I.....)")
            1 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Creak..... Creaaaaaaaaak.....", "(Am.....)")
            2 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Grooooooooan.....", "(Feeling.....)")
            3 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Groooooooooan.....", "(Sleepy.....)")
            4 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Grooooooan.....creeeeeeeak", "(Restful.....)")
            5 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Grrrrooooooooooooooan.....", "(Achey.....)")
            6 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Creeeeeeeegroooooooan.....", "(Goood.....)")
            7 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Creeeeeeeeeeeeeaaaaaak.....", "(Tired.....)")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.ASKING, "Yes?"); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "....."); stage++ }
                2 -> { sendDialogue("After a while you realise that the ent has finished speaking for the moment."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { playerl(FaceAnim.ASKING, "Yes?"); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "....."); stage++ }
                2 -> { sendDialogue("After a while you realise that the ent has finished speaking for the moment."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { playerl(FaceAnim.ASKING, "Yes? We almost have a full sentence now - the suspense is killing me!"); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "....."); stage++ }
                2 -> { sendDialogue("After a while you realise that the ent has finished speaking for the moment."); stage = END_DIALOGUE }
            }

            3,4,5,6,7 -> when (stage) {
                0 -> { playerl(FaceAnim.ASKING, "I'm not sure if that was worth all the waiting."); stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GIANT_ENT_6800, NPCs.GIANT_ENT_6801)
}
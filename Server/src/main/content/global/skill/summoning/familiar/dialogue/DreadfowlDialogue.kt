package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Dreadfowl familiar dialogues.
 */
@Initializable
class DreadfowlDialogue : Dialogue {
    private var branch: Int = 0

    override fun newInstance(player: Player?) = DreadfowlDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (0..2).random()
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "Attack! Fight! Annihilate!")
            1 -> npcl(FaceAnim.OLD_NORMAL, "Can it be fightin' time, please?")
            2 -> npcl(FaceAnim.OLD_NORMAL, "I want to fight something.")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> { playerl(FaceAnim.HALF_ASKING, "It always worries me when you're so happy saying that."); stage = END_DIALOGUE }
            1 -> { playerl(FaceAnim.FRIENDLY, "Look I'll find something for you to fight, just give me a second."); stage = END_DIALOGUE }
            2 -> { playerl(FaceAnim.FRIENDLY, "I'll find something for you in a minute - just be patient."); stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.DREADFOWL_6825, NPCs.DREADFOWL_6826)
}
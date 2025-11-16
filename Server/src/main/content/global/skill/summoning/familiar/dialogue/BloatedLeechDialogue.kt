package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import kotlin.random.Random

/**
 * Represents the Bloated Leech familiar dialogue.
 */
@Initializable
class BloatedLeechDialogue : Dialogue {

    private var branch: Int = -1

    override fun newInstance(player: Player?) = BloatedLeechDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as? NPC ?: return false
        branch = Random.nextInt(4)
        stage = 0

        when (branch) {
            0 -> playerl(FaceAnim.HALF_ASKING, "What is?")
            1 -> playerl(FaceAnim.HALF_ASKING, "Is it terminal?")
            2 -> playerl(FaceAnim.FRIENDLY, "What? My brains stay inside my head, thanks.")
            3 -> playerl(FaceAnim.FRIENDLY, "I think we can skip that for now.")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Never mind. Trust me, I'm almost a doctor."); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "I think I'll get a second opinion."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Not yet. Let me get a better look and I'll see what I can do about it."); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "There are two ways to take that...and I think I'll err on the side of caution."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "That's ok, I can just drill a hole."); stage++ }
                1 -> { playerl(FaceAnim.HALF_ASKING, "How about you don't and pretend you did?"); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Who's the doctor here?"); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "Not you."); stage++ }
                2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I may not be a doctor, but I'm keen. Does that not count?"); stage++ }
                3 -> { playerl(FaceAnim.FRIENDLY, "In most other fields, yes; in medicine, no."); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BLOATED_LEECH_6843, NPCs.BLOATED_LEECH_6844)
}

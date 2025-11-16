package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Magpie familiar dialogue.
 */
@Initializable
class MagpieDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = MagpieDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    private var branch = 0

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (Math.random() * 4).toInt()
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.CHILD_NORMAL, "There's nowt gannin on here...")
            1 -> npcl(FaceAnim.CHILD_NORMAL, "Howway, let's gaan see what's happenin' in toon.")
            2 -> npcl(FaceAnim.CHILD_NORMAL, "Are we gaan oot soon? I'm up fer a good walk me.")
            3 -> npcl(FaceAnim.CHILD_NORMAL, "Ye' been plowdin' i' the claarts aall day.")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> playerl(FaceAnim.HALF_ASKING, "Err...sure? Maybe?").also { stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> playerl(FaceAnim.HALF_ASKING, "It seems upset, but what is it saying?").also { stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> playerl(FaceAnim.HALF_ASKING, "What? I can't understand what you're saying.").also { stage = END_DIALOGUE }

            }
            3 -> when (stage) {
                0 -> playerl(FaceAnim.HALF_ASKING, "That...that was just noise. What does that mean?").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MAGPIE_6824)
}

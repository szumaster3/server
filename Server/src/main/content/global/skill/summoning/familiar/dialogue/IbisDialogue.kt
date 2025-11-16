package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Ibis familiar dialogues.
 */
@Initializable
class IbisDialogue : Dialogue {
    private var branch: Int = 0

    override fun newInstance(player: Player?) = IbisDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (0..4).random()
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.HALF_ASKING, "Where is your skillcape to prove it, then?"); stage++ }
                1 -> { npcl(FaceAnim.OLD_DEFAULT, "At home..."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "I'll bet it is."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { playerl(FaceAnim.HAPPY, "I know!"); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "We can't be fishing all the time you know."); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { playerl(FaceAnim.HALF_ASKING, "What do you mean?"); stage++ }
                1 -> { npcl(FaceAnim.OLD_DEFAULT, "I just noticed we weren't fishing."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Well, we can't fish all the time."); stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                0 -> { playerl(FaceAnim.HALF_ASKING, "I don't know. Would you eat them?"); stage++ }
                1 -> { npcl(FaceAnim.OLD_DEFAULT, "Yes! Ooops..."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "I think I'll hang onto them myself for now."); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.IBIS_6991)
}
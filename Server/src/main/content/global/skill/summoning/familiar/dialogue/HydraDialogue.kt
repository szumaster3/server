package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Hydra familiar dialogue.
 */
@Initializable
class HydraDialogue : Dialogue {
    private var branch: Int = -1

    override fun newInstance(player: Player?) = HydraDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (0..3).random()
        stage = 0

        when (branch) {
            0 -> playerl(FaceAnim.FRIENDLY, "Not really!")
            1 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Raaasp ssssss raaaasp.", "(That's easy for you to say.)")
            2 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Raaaasp rassssp sssssp....", "(Unless you're the one doing all the heavy thinking....)")
            3 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Raasp raasp raaaaasp?", "(What's up this time?)")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Raaasp raaaaap raaaasp?", "(Well I suppose you work with what you got, right?)"); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Raaaaaasp raaaasp raaaasp.", "(At least he doesn't have someone whittering in their ear all the time.)"); stage++ }
                2 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Raaaaaaasp!", "(Quiet, you!)"); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { playerl(FaceAnim.HALF_ASKING, "What's up?"); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Raaa....", "(well...)"); stage++ }
                2 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Raaaaasp sss rassssp.", "(Don't pay any attention, they are just feeling whiny.)"); stage++ }
                3 -> { playerl(FaceAnim.HALF_ASKING, "But they're you, aren't they?"); stage++ }
                4 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Raaaasp raasp rasssp!", "(Don't remind me!)"); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "I think I'll stick to one for now, thanks."); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { playerl(FaceAnim.HALF_ASKING, "Can I help?"); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Rasssp ssssssp? raaaaasp raaaasp.", "(Do you mind? This is a private conversation.)"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Well, excu-u-use me."); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HYDRA_6811, NPCs.HYDRA_6812)
}

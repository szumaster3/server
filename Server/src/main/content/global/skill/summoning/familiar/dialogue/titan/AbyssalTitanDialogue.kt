package content.global.skill.summoning.familiar.dialogue.titan

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Abyssal Titan familiar dialogue.
 */
@Initializable
class AbyssalTitanDialogue : Dialogue {

    constructor()
    constructor(player: Player?) : super(player)

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        if (stage == START_DIALOGUE) {
            npcl(FaceAnim.FAMILIAR_NEUTRAL, "Scruunt, scraaan.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = AbyssalTitanDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ABYSSAL_TITAN_7349, NPCs.ABYSSAL_TITAN_7350)
}

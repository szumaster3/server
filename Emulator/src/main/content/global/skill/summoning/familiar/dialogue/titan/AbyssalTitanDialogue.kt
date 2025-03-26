package content.global.skill.summoning.familiar.dialogue.titan

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Abyssal titan dialogue.
 */
@Initializable
class AbyssalTitanDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return AbyssalTitanDialogue(player)
    }

    /**
     * Instantiates a new Abyssal titan dialogue.
     */
    constructor()

    /**
     * Instantiates a new Abyssal titan dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (stage == START_DIALOGUE) {
            npcl(FaceAnim.CHILD_NORMAL, "Scruunt, scraaan.")
            stage = END_DIALOGUE
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ABYSSAL_TITAN_7349, NPCs.ABYSSAL_TITAN_7350)
    }
}

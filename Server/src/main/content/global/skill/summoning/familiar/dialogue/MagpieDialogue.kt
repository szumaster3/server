package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Dialogue handler for the Magpie familiar.
 */
@Initializable
class MagpieDialogue : Dialogue {

    /**
     * Creates a new instance of this dialogue for the given [player].
     */
    override fun newInstance(player: Player?): Dialogue = MagpieDialogue(player)

    constructor()

    /**
     * Constructs the dialogue with the active [player].
     */
    constructor(player: Player?) : super(player)

    private var branch = -1

    /**
     * Opens the dialogue with the familiar.
     */
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (Math.random() * 4).toInt()
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "There's nowt gannin on here...")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Howway, let's gaan see what's happenin' in toon.")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Are we gaan oot soon? I'm up fer a good walk me.")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Ye' been plowdin' i' the claarts aall day.")
        }

        return true
    }

    /**
     * Handles progression of the dialogue when the player clicks.
     */
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> if (stage == 0) {
                playerl(FaceAnim.HALF_ASKING, "Err...sure? Maybe?")
                stage = END_DIALOGUE
            }
            1 -> if (stage == 0) {
                playerl(FaceAnim.HALF_ASKING, "It seems upset, but what is it saying?")
                stage = END_DIALOGUE
            }
            2 -> if (stage == 0) {
                playerl(FaceAnim.HALF_ASKING, "What? I can't understand what you're saying.")
                stage = END_DIALOGUE
            }
            3 -> if (stage == 0) {
                playerl(FaceAnim.HALF_ASKING, "That...that was just noise. What does that mean?")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    /**
     * Returns the familiar NPC IDs that this dialogue should be attached to.
     */
    override fun getIds(): IntArray = intArrayOf(NPCs.MAGPIE_6824)
}

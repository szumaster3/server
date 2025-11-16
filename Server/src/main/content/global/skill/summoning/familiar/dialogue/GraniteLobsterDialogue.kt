package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Granite Lobster familiar dialogues.
 */
@Initializable
class GraniteLobsterDialogue : Dialogue {
    private var branch: Int = 0

    override fun newInstance(player: Player?) = GraniteLobsterDialogue(player)

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
                0 -> playerl(FaceAnim.FRIENDLY, "The outlanders have insulted our heritage for the last time!").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "The longhall will resound with our celebration!").also { stage = END_DIALOGUE }
            }
            1 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Yes! We shall pile gold before the longhall of our tribesmen!").also { stage = END_DIALOGUE }
            }
            2 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Crush your enemies, see them driven before you, and hear the lamentation of their women!").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "I would have settled for raw sharks, but that's good too!").also { stage = END_DIALOGUE }
            }
            3 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Well, I suppose we could when I'm done with this.").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "Yes! To the looting and the plunder!").also { stage = END_DIALOGUE }
            }
            4 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Fair enough.").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "Clonkclonkclonk grind clonk grind? (It's nothing personal, you're just an Outlander, you know?)").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GRANITE_LOBSTER_6849, NPCs.GRANITE_LOBSTER_6850)
}

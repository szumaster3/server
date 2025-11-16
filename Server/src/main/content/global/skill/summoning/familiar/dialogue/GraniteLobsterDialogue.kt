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

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = GraniteLobsterDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (branch == -1) branch = (Math.random() * 5).toInt()
        stage = 0

        when (branch) {
            0 -> playerl(FaceAnim.FRIENDLY, "The outlanders have insulted our heritage for the last time!")
            1 -> playerl(FaceAnim.FRIENDLY, "Yes! We shall pile gold before the longhall of our tribesmen!").also { stage = END_DIALOGUE }
            2 -> playerl(FaceAnim.FRIENDLY, "Crush your enemies, see them driven before you, and hear the lamentation of their women!")
            3 -> playerl(FaceAnim.FRIENDLY, "Well, I suppose we could when I'm done with this.")
            4 -> playerl(FaceAnim.FRIENDLY, "Fair enough.")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {

        when (branch) {

            0 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "The longhall will resound with our celebration!"); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I would have settled for raw sharks, but that's good too!"); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Yes! To the looting and the plunder!"); stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Clonkclonkclonk grind clonk grind? (It's nothing personal, you're just an Outlander, you know?)"); stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(NPCs.GRANITE_LOBSTER_6849, NPCs.GRANITE_LOBSTER_6850)
}

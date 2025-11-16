package content.global.skill.summoning.familiar.dialogue.spirit

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Spirit Mosquito familiar dialogue.
 */
@Initializable
class SpiritMosquitoDialogue : Dialogue {

    private var branch: Int = -1

    override fun newInstance(player: Player?): Dialogue = SpiritMosquitoDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as? NPC ?: return false
        if (branch == -1) {
            branch = (Math.random() * 4).toInt()
        }

        stage = when (branch) {
            0 -> 0
            1 -> 4
            2 -> 9
            3 -> 13
            else -> 0
        }

        when (branch) {
            0 -> npcl(FaceAnim.CHILD_NORMAL, "You have lovely ankles.")
            1 -> npcl(FaceAnim.CHILD_NORMAL, "How about that local sports team?")
            2 -> npcl(FaceAnim.CHILD_NORMAL, "Have you ever tasted pirate blood?")
            3 -> npcl(FaceAnim.CHILD_NORMAL, "I'm soooo hungry!")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Am I meant to be pleased by that?"); stage++ }
                1 -> { npcl(FaceAnim.CHILD_NORMAL, "Thin skin. Your delicious blood is easier to get too."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "I knew I couldn't trust you."); stage++ }
                3 -> { npcl(FaceAnim.CHILD_NORMAL, "Oh come on, you won't feel a thing..."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                4 -> { playerl(FaceAnim.FRIENDLY, "Which one? The gnomeball team?"); stage++ }
                5 -> { npcl(FaceAnim.CHILD_NORMAL, "I must confess: I have no idea."); stage++ }
                6 -> { playerl(FaceAnim.FRIENDLY, "Why did you ask, then?"); stage++ }
                7 -> { npcl(FaceAnim.CHILD_NORMAL, "I was just trying to be friendly."); stage++ }
                8 -> { playerl(FaceAnim.FRIENDLY, "Just trying to get to my veins, more like!"); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                9 -> { playerl(FaceAnim.FRIENDLY, "Why would I drink pirate blood?"); stage++ }
                10 -> { npcl(FaceAnim.CHILD_NORMAL, "How about dwarf blood?"); stage++ }
                11 -> { playerl(FaceAnim.FRIENDLY, "I don't think you quite understand..."); stage++ }
                12 -> { npcl(FaceAnim.CHILD_NORMAL, "Gnome blood, then?"); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                13 -> { playerl(FaceAnim.FRIENDLY, "What would you like to eat?"); stage++ }
                14 -> { npcl(FaceAnim.CHILD_NORMAL, "Well, if you're not too attached to your elbow..."); stage++ }
                15 -> { playerl(FaceAnim.FRIENDLY, "You can't eat my elbow! You don't have teeth!"); stage++ }
                16 -> { npcl(FaceAnim.CHILD_NORMAL, "Tell me about it. Cousin Nigel always makes fun of me. Calls me 'No-teeth'."); stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_MOSQUITO_7331, NPCs.SPIRIT_MOSQUITO_7332)
}
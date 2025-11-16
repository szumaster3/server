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
class SpiritMosquitoDialogue(player: Player? = null) : Dialogue(player) {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = SpiritMosquitoDialogue(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as? NPC ?: return false

        if (branch == -1) branch = (0..3).random()

        stage = when (branch) {
            0 -> 0
            1 -> 4
            2 -> 9
            3 -> 13
            else -> 0
        }

        when (branch) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "You have lovely ankles.")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "How about that local sports team?")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Have you ever tasted pirate blood?")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I'm soooo hungry!")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Am I meant to be pleased by that?"); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Thin skin. Your delicious blood is easier to get too."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "I knew I couldn't trust you."); stage++ }
                3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Oh come on, you won't feel a thing..."); stage = END_DIALOGUE }
            }
            1 -> when (stage) {
                4 -> { playerl(FaceAnim.FRIENDLY, "Which one? The gnomeball team?"); stage++ }
                5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I must confess: I have no idea."); stage++ }
                6 -> { playerl(FaceAnim.FRIENDLY, "Why did you ask, then?"); stage++ }
                7 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I was just trying to be friendly."); stage++ }
                8 -> { playerl(FaceAnim.FRIENDLY, "Just trying to get to my veins, more like!"); stage = END_DIALOGUE }
            }
            2 -> when (stage) {
                9 -> { playerl(FaceAnim.FRIENDLY, "Why would I drink pirate blood?"); stage++ }
                10 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "How about dwarf blood?"); stage++ }
                11 -> { playerl(FaceAnim.FRIENDLY, "I don't think you quite understand..."); stage++ }
                12 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Gnome blood, then?"); stage = END_DIALOGUE }
            }
            3 -> when (stage) {
                13 -> { playerl(FaceAnim.FRIENDLY, "What would you like to eat?"); stage++ }
                14 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Well, if you're not too attached to your elbow..."); stage++ }
                15 -> { playerl(FaceAnim.FRIENDLY, "You can't eat my elbow! You don't have teeth!"); stage++ }
                16 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Tell me about it. Cousin Nigel always makes fun of me. Calls me 'No-teeth'."); stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_MOSQUITO_7331, NPCs.SPIRIT_MOSQUITO_7332)
}

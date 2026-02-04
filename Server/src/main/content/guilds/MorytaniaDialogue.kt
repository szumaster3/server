package content.guilds

import content.guilds.Guilds.Companion.inGuild
import core.api.sendOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE

/**
 * Represents the Morytania dialogue.
 */
@Initializable
class MorytaniaDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.NEUTRAL, "Welcome, adventurer.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val guild = Guilds.Morytania
        when (stage) {
            0 -> npcl(FaceAnim.NEUTRAL, "The land of Morytania is not kind to those who tread without care.").also { stage++ }
            1 -> player(FaceAnim.HALF_ASKING, "Why do you say that?").also { stage++ }
            2 -> npcl(FaceAnim.NEUTRAL, "Mist and darkness conceal much here - the forests whisper, and the swamps remember. Few survive unscathed without allies who understand the shadows.").also { stage++ }
            3 -> player(FaceAnim.HALF_ASKING, "Allies? Who are they?").also { stage++ }
            4 -> npcl(FaceAnim.NEUTRAL, "A secret brotherhood, known as secret brotherhood.").also { stage++ }
            5 -> npcl(FaceAnim.NEUTRAL, "We watch, guide, and intervene when Morytania teeters on chaos. We do not reveal ourselves lightly.").also { stage++ }
            6 -> player(FaceAnim.HALF_ASKING, "Could I join?").also { stage++ }
            7 -> if(inGuild(player))
                npcl(FaceAnim.NEUTRAL, "You walk the path of another order already. Shadow Fang does not welcome divided loyalties.").also { stage = END_DIALOGUE }
            else if(!guild.checkRequirements(player))
                npcl(FaceAnim.NEUTRAL, "You are not yet ready to endure the darkness of Morytania. Return when your resolve is stronger.").also { stage = END_DIALOGUE }
             else
                npcl(FaceAnim.NEUTRAL, "Only those who endure the darkness, act with subtlety, and value secrecy over recognition may walk our path.").also { stage++ }

            8 -> sendOptions(player, "Do you wish to join us?", "Yes.", "No.").also { stage++ }
            9 -> when(buttonId) {
                1 -> player("Yes, I will join.").also { stage++ }
                2 -> player("Not yet.").also { stage = END_DIALOGUE }
            }
            10 -> npcl(FaceAnim.NEUTRAL, "Very well.").also { stage++ }
            11 -> end()

        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = AsgarniaDialogue(player)

    override fun getIds(): IntArray = intArrayOf(-1)
}

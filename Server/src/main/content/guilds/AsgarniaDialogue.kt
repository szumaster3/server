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
 * Represents the Asgarnia dialogue.
 */
@Initializable
class AsgarniaDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.NEUTRAL, "Greetings, adventurer.").also { stage++ }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val guild = Guilds.Asgarnia
        when (stage) {
            0 -> npcl(FaceAnim.HALF_ASKING,"Do you see Falador as a city of order and knights, or as a place full of hidden truths?").also { stage++ }
            1 -> playerl(FaceAnim.HALF_THINKING, "What do you mean?").also { stage++ }
            2 -> npcl(FaceAnim.NEUTRAL, "The White Knights defend the land, yes. But there are events and forces they do not record, scholars who write in code, and shadows that walk unseen.").also { stage++ }
            3 -> playerl(FaceAnim.HALF_ASKING, "Are these shadows dangerous?").also { stage++ }
            4 -> npcl(FaceAnim.NEUTRAL, "Not all shadows are malevolent. Some watch to maintain balance, to guard Asgarnia from threats unknown to most citizens.").also { stage++ }
            5 -> playerl(FaceAnim.HALF_ASKING, "Do you belong to them?").also { stage++ }
            6 -> npcl(FaceAnim.NEUTRAL, "I am part of a quiet circle, known in whispers as the brotherhood. We observe, chronicle, and guide events beyond the sight of the common folk.").also { stage++ }
            7 -> player(FaceAnim.HALF_ASKING, "Can I join?").also { stage++ }
            8 -> if(inGuild(player))
                npcl(FaceAnim.NEUTRAL, "I see you have already pledged yourself to another order. We cannot accept your request.").also { stage = END_DIALOGUE }
             else if(!guild.checkRequirements(player))
                npcl(FaceAnim.NEUTRAL, "You are not yet ready to walk the path of the brotherhood. Return when your mind and skills are sharper.").also { stage = END_DIALOGUE }
             else
                npcl(FaceAnim.NEUTRAL, "Only those who value knowledge, patience, and subtlety over glory.").also { stage++ }

            9 -> sendOptions(player, "Do you wish to join us?", "Yes.", "No, thanks.").also { stage++ }
            10 -> when(buttonId) {
                1 -> player("Yes, I will join.").also { stage++ }
                2 -> player("Not yet.").also { stage = END_DIALOGUE }
            }
            11 -> npc("Very well.").also { stage++ }
            12 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = AsgarniaDialogue(player)

    override fun getIds(): IntArray = intArrayOf(-1)
}

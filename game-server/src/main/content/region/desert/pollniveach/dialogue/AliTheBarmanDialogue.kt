package content.region.desert.pollniveach.dialogue

import core.api.getQuestStage
import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Ali The Barman dialogue.
 */
@Initializable
class AliTheBarmanDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player("Hello there.").also { stage++ }
            1 -> npc("Good day. Can I help you with anything?").also { stage++ }
            2 -> showTopics(
                Topic("Yes, I'd like a drink please.", 3),
                Topic("What's going on in town?", 4),
                IfTopic("I'm looking for Ali from Pollnivneach.", 10, getQuestStage(player!!, Quests.THE_FEUD) >= 10),
                Topic("The 'Asp and Snake'? What a strange name for a bar.", 18)
            )
            3 -> end().also { openNpcShop(player!!, NPCs.ALI_THE_BARMAN_1864) }
            4 -> npcl(FaceAnim.FRIENDLY, "Nothing much really, the same as you would expect of any border town on the edge of the desert.").also { stage++ }
            5 -> playerl(FaceAnim.FRIENDLY, "So can you tell me anything about those two gangs in the town?").also { stage++ }
            6 -> npcl(FaceAnim.FRIENDLY, "Nothing much to say really about them. They're all paying customers so I won't rock the boat if you don't mind.").also { stage++ }
            7 -> npcl(FaceAnim.FRIENDLY, "Is there anything else I can help you with?").also { stage++ }
            8 -> options("No thanks I'm ok.", "Yes, I'd like to talk about something else.").also { stage++ }
            9 -> when (buttonId) {
                1 -> playerl(FaceAnim.FRIENDLY, "No thanks I'm ok. Thanks for your time.").also { stage = END_DIALOGUE }
                2 -> playerl(FaceAnim.FRIENDLY, "Yes, I'd like to talk about something else.").also { stage = 2 }
            }
            10 -> npcl(FaceAnim.FRIENDLY, "Could you be a little more specific? I'm sure there's more than one Ali from Pollnivneach.").also { stage++ }
            11 -> playerl(FaceAnim.FRIENDLY, "The Ali I'm looking for has an uncle called Ali.").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "What can you tell me about his uncle?").also { stage++ }
            13 -> playerl(FaceAnim.FRIENDLY, "Well he has a stall in the Bazaar in Al Kharid, does that help?").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "Um... no.").also { stage++ }
            15 -> playerl(FaceAnim.FRIENDLY, "Well do you know if anyone here might know who I'm looking for?").also { stage++ }
            16 -> npcl(FaceAnim.FRIENDLY, "That's a difficult question to answer. It doesn't seem like you really know who you're looking for, but you should ask around town.").also { stage++ }
            17 -> npcl(FaceAnim.FRIENDLY, "Is there anything else I can help you with?").also { stage = 2 }
            18 -> npcl(FaceAnim.HALF_ASKING, "I know what you're thinking, asps are a type of snake, right?").also { stage++ }
            19 -> npcl(FaceAnim.HALF_ASKING, "I admit it is a little confusing, but neither 'The Asp' nor 'The Snake' have quite the same ring to them.").also { stage++ }
            20 -> player("I get your point. I quite like the name.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = AliTheBarmanDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ALI_THE_BARMAN_1864)
}

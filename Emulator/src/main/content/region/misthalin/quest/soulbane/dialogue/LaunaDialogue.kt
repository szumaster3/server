package content.region.misthalin.quest.soulbane.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LaunaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        player("Hi there.").also { stage++ }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.SAD, "Hello.").also { stage++ }
            1 -> player("What's this big hole in the ground all about?").also { stage++ }
            2 ->
                npc(
                    FaceAnim.SAD,
                    "Oh I wish I knew for sure... All I do know is that it",
                    "opened seemingly by itself a few months ago. It was",
                    "first discovered by the students over at the digsite, a",
                    "few of which have gone down there but never returned.",
                ).also {
                    stage++
                }
            3 -> npc(FaceAnim.SAD, "They told me about it straight away.").also { stage++ }
            4 -> player("So why is it important to you?").also { stage++ }
            5 -> npc(FaceAnim.SAD, "A few reasons, but I think the situation is beyond help", "now.").also { stage++ }
            6 -> player("What situation? Maybe I can help.").also { stage++ }
            7 ->
                npc(
                    FaceAnim.SAD,
                    "I suppose it wouldn't hurt to tell you. I'm waiting to",
                    "see my son Tolna. Don't suppose you've seen him?",
                ).also {
                    stage++
                }
            8 -> player("Your son, Tolna? I don't know, what does he look like?").also { stage++ }
            9 -> npc(FaceAnim.SAD, "I don't really know.").also { stage++ }
            10 -> player("You don't know what your own son looks like?").also { stage++ }
            11 ->
                npc(
                    FaceAnim.SAD,
                    "No. You see, I haven't seen him for... for... 25 years",
                    "now. He just ran away one day and never returned.",
                    "I'm hoping he's down this rift as I know he headed in",
                    "this direction. My husband went to look, but he hasn't",
                ).also {
                    stage++
                }
            12 -> npc(FaceAnim.SAD, "returned for days.").also { stage++ }
            13 ->
                options(
                    "Would you like me to go down to look for your husband and son?",
                    "Surely by now they'd both be dead?",
                ).also {
                    stage++
                }
            14 ->
                when (buttonId) {
                    1 -> player("Would you like me to go down to look for your", "husband and son?").also { stage = 20 }
                    2 -> player("Surely by now they'd both be dead?").also { stage++ }
                }
            15 -> npc(FaceAnim.ANGRY, "What?!?!").also { stage++ }
            16 -> player("Oops.").also { stage++ }
            17 -> npc(FaceAnim.ANGRY, "How could you say such a thing? Get away from me!").also { stage = END_DIALOGUE }
            20 -> npc("You would do that? Oh thank you so much!").also { stage++ }
            21 -> player("Don't worry. I'll investigate.").also { stage++ }
            22 ->
                npc(
                    "Thank you. You can use a rope to get into the rift,",
                    "just attach it at the edge.",
                ).also { stage++ }
            23 -> player("No problem.").also { stage++ }
            24 -> npc("Be careful.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LAUNA_3640, NPCs.LAUNA_3639, 3638)
    }
}

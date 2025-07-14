package content.region.desert.alkharid.quest.feud.dialogue

import core.api.setVarbit
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Vars

/**
 * Represents the Drunken Ali dialogue (The Feud quest).
 */
class DrunkenAliFeudDialogue(private val beer: Int) : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.DRUNKEN_ALI_1863)

        when (beer) {
            1 -> when (stage) {
                0 -> npc(FaceAnim.DRUNK, "I happen to know that you are looking for somebody.").also { stage++ }
                1 -> player(FaceAnim.THINKING, "How do you know that?").also { stage++ }
                2 -> npc(FaceAnim.DRUNK, "Well you're not here sightseeing, that's for certain.", "Anyway it's not really important how I know what I", "know. What is important is that I know all about the", "person you're looking for.").also { stage++ }
                3 -> player(FaceAnim.ASKING, "You do? How do you know I'm looking for Ali", "Morrisane's nephew, Ali?").also { stage++ }
                4 -> npc(FaceAnim.DRUNK, "I'd tell you more, but my throat is drying up and my", "lips are dry. This desert climate is an inhospitable one.", "Get me another beer so that I can continue.").also { stage = END_DIALOGUE }
            }
            2 -> when (stage) {
                0 -> npc(FaceAnim.DRUNK, "You are too kind, now what were we talking about?").also { stage++ }
                1 -> player(FaceAnim.THINKING, "Ali Morrisane's nephew.").also { stage++ }
                2 -> npc(FaceAnim.DRUNK, "I've known him since he was a wee lad. A right little", "mischievous beggar....").also { stage++ }
                3 -> player(FaceAnim.HALF_ASKING, "That's all fine and well, but do you know any more", "useful information about him, such as his whereabouts?").also { stage++ }
                4 -> npc(FaceAnim.DRUNK, "Well now, if you had just asked me about that in the", "first place I would have told you directly.").also { stage++ }
                5 -> player(FaceAnim.HALF_ASKING, "I'm sorry if I was a little sharp with you, but do you", "know where he is?").also { stage++ }
                6 -> npc(FaceAnim.DRUNK, "I'd tell you more, but my throat is drying up and my", "lips are dry. This desert climate is an inhospitable one.", "Get me another beer so that I can continue.").also { stage = END_DIALOGUE }
            }
            3 -> when (stage) {
                0  -> npc(FaceAnim.DRUNK, "It's yourself again, isn't it?").also { stage++ }
                1  -> player(FaceAnim.THINKING, "Well it would have to be, who else could I possibly be", "other than myself?").also { stage++ }
                2  -> npc(FaceAnim.DRUNK, "That's kind of getting a little deep for me, existential", "reasoning or justification was never my strong suit. So", "what can I do for you?").also { stage++ }
                3  -> player(FaceAnim.HALF_ASKING, "Um what? Look, all I want to know is the whereabouts", "of Ali Morrisane's nephew.").also { stage++ }
                4  -> npc(FaceAnim.DRUNK, "Now is that his nephew from his side or his wife's?", "They have a huge family you see. I remember.......").also { stage++ }
                5  -> player(FaceAnim.ANNOYED, "No no no stop. You are not going to fool me into", "buying you another beer without first giving me some", "useful information.").also { stage++ }
                6  -> npc(FaceAnim.DRUNK, "But my lips are dry, how can I possibly continue?").also { stage++ }
                7  -> player(FaceAnim.THINKING, "Well if you can't continue, I will just have to wring", "you like a wet cloth and squeeze out enough booze to", "fill another tankard. I'd reckon it would hurt though.").also { stage++ }
                8  -> npc(FaceAnim.DRUNK, "I see. Don't worry about me, I'll just have to soldier", "on. Now what do you want to know?").also { stage++ }
                9  -> player(FaceAnim.ANNOYED, "For the last time, where is Ali Morrisane's nephew?").also { stage++ }
                10 -> npc(FaceAnim.DRUNK, "I don't know exactly, he disappeared last week, either", "those Bandits or Menaphites have something to do with", "it. Perhaps their feuding has something to do with it,", "perhaps if you were to solve that problem...").also { stage++ }
                11 -> npc(FaceAnim.ASKING, "Anyway is there any chance of another beer?").also { stage++ }
                12 -> player(FaceAnim.ANNOYED, "What do you think?").also {
                    end()
                    setVarbit(player!!, Vars.VARBIT_THE_FEUD_PROGRESS_334, 2, true)
                }
            }
            4 -> npc(FaceAnim.DRUNK, "Cheers for the beers!").also { stage = END_DIALOGUE }
        }
    }
}
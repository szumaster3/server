package content.global.skill.summoning.familiar.dialogue.titan

import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class LavaTitanDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.LAVA_TITAN_7341)
        when ((0..4).random()) {
            0 ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Isn't it a lovely day, Titan?").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.CHILD_NORMAL,
                            "It is quite beautiful. The perfect sort of day for a limerick. Perhaps, I could tell you one?",
                        ).also {
                            stage++
                        }
                    2 -> playerl(FaceAnim.FRIENDLY, "That sounds splendid.").also { stage++ }
                    3 -> npcl(FaceAnim.CHILD_NORMAL, "There once was a bard of Edgeville, ").also { stage++ }
                    4 -> npcl(FaceAnim.CHILD_NORMAL, "Whose limericks were quite a thrill, ").also { stage++ }
                    5 -> npcl(FaceAnim.CHILD_NORMAL, "He wrote this one here, ").also { stage++ }
                    6 -> npcl(FaceAnim.CHILD_NORMAL, "His best? Nowhere near, ").also { stage++ }
                    7 ->
                        npcl(FaceAnim.CHILD_NORMAL, "But at least half a page it did fill.").also {
                            stage =
                                END_DIALOGUE
                        }
                }

            1 ->
                when (stage) {
                    0 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I was just thinking about the River Lum, Titan. Isn't it beautiful?",
                        ).also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.CHILD_NORMAL,
                            "I had a bad experience with the River Lum once. Would you like me to tell you about it?",
                        ).also {
                            stage++
                        }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Well, okay, but only if it's in the form of a limerick.",
                        ).also { stage++ }
                    3 -> npcl(FaceAnim.CHILD_NORMAL, "I once saw a river called Lum, ").also { stage++ }
                    4 -> npcl(FaceAnim.CHILD_NORMAL, "So lovely I had to succumb, ").also { stage++ }
                    5 -> npcl(FaceAnim.CHILD_NORMAL, "The results, they were grave, ").also { stage++ }
                    6 -> npcl(FaceAnim.CHILD_NORMAL, "For I boiled all the waves, ").also { stage++ }
                    7 -> npcl(FaceAnim.CHILD_NORMAL, "And ended up just feeling glum.").also { stage = END_DIALOGUE }
                }

            2 ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Titan, I wish I had more money.").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.CHILD_NORMAL,
                            "Money doesn't always bring happiness. I know a limerick about money as it happens. Would you like to hear it?",
                        ).also {
                            stage++
                        }
                    2 -> playerl(FaceAnim.FRIENDLY, "Sure.").also { stage++ }
                    3 -> npcl(FaceAnim.CHILD_NORMAL, "There was a king of Ardougne, ").also { stage++ }
                    4 -> npcl(FaceAnim.CHILD_NORMAL, "Who cared too much about coin, ").also { stage++ }
                    5 -> npcl(FaceAnim.CHILD_NORMAL, "He filled up ten pools, ").also { stage++ }
                    6 -> npcl(FaceAnim.CHILD_NORMAL, "Then swam amongst jewels, ").also { stage++ }
                    7 -> npcl(FaceAnim.CHILD_NORMAL, "And wouldn't let anyone join.").also { stage = END_DIALOGUE }
                }

            3 ->
                when (stage) {
                    0 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I'm quite a warrior, you know, Titan! I was just reminiscing about how I defeated Glough.",
                        ).also {
                            stage++
                        }
                    1 ->
                        npcl(
                            FaceAnim.CHILD_NORMAL,
                            "Glough, eh? I've heard of that fellow. In fact, I happen to know a limerick about him.",
                        ).also {
                            stage++
                        }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Oh, really? Well, I'm quite busy; perhaps you could tell me some oth",
                        ).also { stage++ }
                    3 ->
                        sendDialogue(
                            player!!,
                            "The titan seems rather excited and launches into the limerick regardless!",
                        ).also {
                            stage++
                        }
                    4 -> npcl(FaceAnim.CHILD_NORMAL, "There once was a scoundrel called Glough, ").also { stage++ }
                    5 -> npcl(FaceAnim.CHILD_NORMAL, "For whom nothing was ever enough, ").also { stage++ }
                    6 -> npcl(FaceAnim.CHILD_NORMAL, "His goal was the world, ").also { stage++ }
                    7 -> npcl(FaceAnim.CHILD_NORMAL, "Battle standard unfurled, ").also { stage++ }
                    8 -> npcl(FaceAnim.CHILD_NORMAL, "But he luckily wasn't that tough!").also { stage = END_DIALOGUE }
                }

            4 ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Are you hungry?").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.CHILD_NORMAL,
                            "Hmm...no, I don't think so. I don't need to eat, you see.",
                        ).also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Oh, how awful. Food is delicious, you'd love it!").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.CHILD_NORMAL,
                            "It's alright. I met a dwarf once who described it all to me. In fact, I wrote a limerick in his honour.",
                        ).also {
                            stage++
                        }
                    4 -> playerl(FaceAnim.FRIENDLY, "Oh, I'd love to hear it.").also { stage++ }
                    5 -> npcl(FaceAnim.CHILD_NORMAL, "I once met a dwarf who loved pie, ").also { stage++ }
                    6 -> npcl(FaceAnim.CHILD_NORMAL, "For a redberry one he would cry, ").also { stage++ }
                    7 -> npcl(FaceAnim.CHILD_NORMAL, "'I do love you so.'").also { stage++ }
                    8 -> npcl(FaceAnim.CHILD_NORMAL, "'From berry to dough.'").also { stage++ }
                    9 ->
                        npcl(
                            FaceAnim.CHILD_NORMAL,
                            "Then he'd eat it and let out a sigh.",
                        ).also { stage = END_DIALOGUE }
                }
        }
    }
}

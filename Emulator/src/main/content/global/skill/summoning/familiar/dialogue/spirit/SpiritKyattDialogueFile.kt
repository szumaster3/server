package content.global.skill.summoning.familiar.dialogue.spirit

import core.api.inInventory
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class SpiritKyattDialogueFile : DialogueFile() {
    private val randomConversation = (0..3).random()

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SPIRIT_KYATT_7365)
        if (randomConversation == 0) {
            when (stage) {
                START_DIALOGUE -> npcl(FaceAnim.OLD_DEFAULT, "Guess who wants a belly rub, human.").also { stage++ }
                1 -> playerl(FaceAnim.FRIENDLY, "Umm...is it me?").also { stage++ }
                2 -> npcl(FaceAnim.OLD_DEFAULT, "No, human, it is not you. Guess again.").also { stage++ }
                3 -> playerl(FaceAnim.FRIENDLY, "Is it the Duke of Lumbridge?").also { stage++ }
                4 -> npcl(FaceAnim.OLD_DEFAULT, "You try my patience, human!").also { stage++ }
                5 ->
                    playerl(
                        FaceAnim.FRIENDLY,
                        "Is it Zamorak? That would explain why he's so cranky.",
                    ).also { stage++ }
                6 ->
                    npcl(FaceAnim.OLD_DEFAULT, "Please do not make me destroy you before I get my belly rub!").also {
                        stage =
                            END_DIALOGUE
                    }
            }
        }

        if (randomConversation == 1) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Here, kitty!").also { stage++ }
                1 -> npcl(FaceAnim.OLD_DEFAULT, "What do you want, human?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "I just thought I would see how you were.").also { stage++ }
                3 ->
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "I do not have time for your distractions. Leave me be!",
                    ).also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Well, sorry! Would a ball of wool cheer you up?").also { stage++ }
                5 -> npcl(FaceAnim.OLD_DEFAULT, "How dare you insult my intelli- what colour wool?").also { stage++ }
                6 -> playerl(FaceAnim.FRIENDLY, "Umm...white?").also { stage++ }
                7 -> npcl(FaceAnim.OLD_DEFAULT, "I will end you!").also { stage = END_DIALOGUE }
            }
        }

        if (randomConversation == 2) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello, kitty cat!").also { stage++ }
                1 ->
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "Human, leave me be. I'm far too busy to deal with your nonsense.",
                    ).also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "What are you up to?").also { stage++ }
                3 -> npcl(FaceAnim.OLD_DEFAULT, "I am engaged in an intricate dirt-purging operation!").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Aww, kitty's cleaning his paws! How cute!").also { stage++ }
                5 -> npcl(FaceAnim.OLD_DEFAULT, "Know this, human. Once I finish cleaning my paws...").also { stage++ }
                6 -> npcl(FaceAnim.OLD_DEFAULT, "I will destroy you!").also { stage = END_DIALOGUE }
            }
        }

        if (randomConversation == 3) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Here, kitty!").also { stage++ }
                1 -> npcl(FaceAnim.OLD_DEFAULT, "Do not toy with me, human!").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "What about under your chin?").also { stage++ }
                3 ->
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "I am not one of your playful kittens, human. I eat playful kittens for breakfast!",
                    ).also {
                        stage++
                    }
                4 -> playerl(FaceAnim.FRIENDLY, "Not even behind your ears?").also { stage++ }
                5 -> sendDialogue(player!!, "You lean down and tickle the kyatt behind the ears.").also { stage++ }
                6 ->
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "I will...purrrrr...ooh that's quite nice...destroy...purrrrrrr...you.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
            }
        }

        if (inInventory(player!!, Items.BALL_OF_WOOL_1759, 1)) {
            when (stage) {
                START_DIALOGUE -> npcl(FaceAnim.OLD_DEFAULT, "Human, hand me that ball of wool.").also { stage++ }
                1 -> playerl(FaceAnim.FRIENDLY, "Aww...do you want to play with it?").also { stage++ }
                2 -> npcl(FaceAnim.OLD_DEFAULT, "I do not 'play', human.").also { stage++ }
                3 -> playerl(FaceAnim.FRIENDLY, "If you say so, kitty! Alright, you can have it.").also { stage++ }
                4 ->
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "Aha! Ball of wool: you are mine now. I will destroy you!",
                    ).also { stage++ }
                5 ->
                    playerl(
                        FaceAnim.FRIENDLY,
                        "Well I'm not giving it to you, now! I'll never get it back.",
                    ).also { stage++ }
                6 ->
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "Then you leave me no choice but to destroy YOU, human!",
                    ).also { stage++ }
                7 -> playerl(FaceAnim.FRIENDLY, "Bad kitty!").also { stage = END_DIALOGUE }
            }
        }
    }
}

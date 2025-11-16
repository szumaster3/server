package content.global.skill.summoning.familiar.dialogue.spirit

import core.api.inInventory
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Spirit Kyatt familiar dialogue.
 */
class SpiritKyattDialogue : DialogueFile() {

    private var randomDialogue = (0..3).random()

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.SPIRIT_KYATT_7365)
        if (randomDialogue == 0) {
            when (stage) {
                START_DIALOGUE -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Guess who wants a belly rub, human.").also { stage++ }
                1 -> playerl(FaceAnim.FRIENDLY, "Umm...is it me?").also { stage++ }
                2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "No, human, it is not you. Guess again.").also { stage++ }
                3 -> playerl(FaceAnim.FRIENDLY, "Is it the Duke of Lumbridge?").also { stage++ }
                4 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "You try my patience, human!").also { stage++ }
                5 -> playerl(FaceAnim.FRIENDLY, "Is it Zamorak? That would explain why he's so cranky.").also { stage++ }
                6 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Please do not make me destroy you before I get my belly rub!").also { stage = END_DIALOGUE }
            }
        }

        if (randomDialogue == 1) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Here, kitty!").also { stage++ }
                1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "What do you want, human?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "I just thought I would see how you were.").also { stage++ }
                3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I do not have time for your distractions. Leave me be!").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Well, sorry! Would a ball of wool cheer you up?").also { stage++ }
                5 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "How dare you insult my intelli- what colour wool?").also { stage++ }
                6 -> playerl(FaceAnim.FRIENDLY, "Umm...white?").also { stage++ }
                7 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I will end you!").also { stage = END_DIALOGUE }
            }
        }

        if (randomDialogue == 2) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello, kitty cat!").also { stage++ }
                1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Human, leave me be. I'm far too busy to deal with your nonsense.").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "What are you up to?").also { stage++ }
                3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I am engaged in an intricate dirt-purging operation!").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Aww, kitty's cleaning his paws! How cute!").also { stage++ }
                5 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Know this, human. Once I finish cleaning my paws...").also { stage++ }
                6 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I will destroy you!").also { stage = END_DIALOGUE }
            }
        }

        if (randomDialogue == 3) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Here, kitty!").also { stage++ }
                1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Do not toy with me, human!").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "What about under your chin?").also { stage++ }
                3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I am not one of your playful kittens, human. I eat playful kittens for breakfast!").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Not even behind your ears?").also { stage++ }
                5 -> sendDialogue(player!!, "You lean down and tickle the kyatt behind the ears.").also { stage++ }
                6 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I will...purrrrr...ooh that's quite nice...destroy...purrrrrrr...you.").also { stage = END_DIALOGUE }
            }
        }

        if (inInventory(player!!, Items.BALL_OF_WOOL_1759, 1)) {
            when (stage) {
                START_DIALOGUE -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Human, hand me that ball of wool.").also { stage++ }
                1 -> playerl(FaceAnim.FRIENDLY, "Aww...do you want to play with it?").also { stage++ }
                2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I do not 'play', human.").also { stage++ }
                3 -> playerl(FaceAnim.FRIENDLY, "If you say so, kitty! Alright, you can have it.").also { stage++ }
                4 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Aha! Ball of wool: you are mine now. I will destroy you!").also { stage++ }
                5 -> playerl(FaceAnim.FRIENDLY, "Well I'm not giving it to you, now! I'll never get it back.").also { stage++ }
                6 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Then you leave me no choice but to destroy YOU, human!").also { stage++ }
                7 -> playerl(FaceAnim.FRIENDLY, "Bad kitty!").also { stage = END_DIALOGUE }
            }
        }
    }
}

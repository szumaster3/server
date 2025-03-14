package content.global.skill.construction.decoration.skillhall.head

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class KalphiteHeadDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.KALPHITE_QUEEN_4234)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.CHILD_THINKING,
                    "Soft-thing! How dare you approach the queen of the kalphite?",
                ).also { stage++ }

            1 -> {
                if (!player!!.houseManager.isInHouse(player!!)) {
                    player("Player killed you! I can do what I like.").also { stage++ }
                } else {
                    player("I killed you, remember?").also { stage = 3 }
                }
            }

            2 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Player killed me but you could not. My successor will be as strong as me. Come down to meet her, she is unafraid.",
                ).also { stage = END_DIALOGUE }

            3 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Yes you killed the queen, but by now another will have risen up! One kalphite may die but the hive goes on!",
                ).also { stage++ }

            4 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "The kalphite race grows stronger everyday, and our young feed on the blood of the soft-things that invade from above!",
                ).also { stage++ }

            5 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Someday we will overrun the world again and all soft creatures will die.",
                ).also { stage++ }

            6 ->
                npcl(
                    FaceAnim.CHILD_FRIENDLY,
                    "But we will reserve the worst fate for those who have killed a queen and hung her head in their house.",
                ).also { stage++ }

            7 ->
                npcl(
                    FaceAnim.CHILD_THINKING,
                    "We will lay eggs in your brain and you will not die until it explodes and a million kalphites emerge to make a grand hive of all the world!",
                ).also { stage++ }

            8 -> options("You don't scare me!", "Please don't kill me!").also { stage++ }
            9 ->
                when (buttonID) {
                    1 -> player(FaceAnim.CALM, "You don't scare me!").also { stage++ }
                    2 -> player(FaceAnim.WORRIED, "Please don't kill me!").also { stage = 11 }
                }

            10 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Your pitiful misplaced confidence is irrelevant. You will all die!",
                ).also { stage = END_DIALOGUE }

            11 ->
                npcl(
                    FaceAnim.CHILD_EVIL_LAUGH,
                    "Ha ha ha! It is too late for pleading now, pathetic mammal! Your fate is sealed!",
                ).also { stage = END_DIALOGUE }
        }
    }
}

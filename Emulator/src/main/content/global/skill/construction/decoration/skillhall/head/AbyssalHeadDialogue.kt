package content.global.skill.construction.decoration.skillhall.head

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.DARK_RED
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class AbyssalHeadDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.ABYSSAL_DEMON_4230)
        when (stage) {
            0 -> {
                if (!player!!.houseManager.isInHouse(player!!)) {
                    player("Player killed an abyssal demon! Cool!").also { stage++ }
                } else {
                    npc(
                        FaceAnim.CHILD_FRIENDLY,
                        "Have you considered visiting$DARK_RED THE ABYSS</col>?",
                    ).also { stage = 2 }
                }
            }

            1 ->
                npcl(
                    FaceAnim.CHILD_THINKING,
                    "Cool for ${if (player!!.isMale) "him" else "her"} maybe. How would you like to be stuck on a wall?",
                ).also { stage = END_DIALOGUE }

            2 ->
                options(
                    "I visit the abyss all the time.",
                    "It's too scary for me.",
                    "Could I get an abyssal whip?",
                ).also { stage++ }

            3 ->
                when (buttonID) {
                    1 -> player(FaceAnim.HAPPY, "I visit the abyss all the time.").also { stage = 8 }
                    2 -> player("It's too scary for me.").also { stage = 13 }
                    3 -> player(FaceAnim.HALF_ASKING, "Could I get an abyssal whip?").also { stage++ }
                }

            4 ->
                npc(
                    FaceAnim.CHILD_FRIENDLY,
                    "You must take all your gold and all your most valued",
                    "items, and take them into$DARK_RED THE ABYSS</col> without",
                    "weapons or armour.",
                ).also { stage++ }

            5 -> playerl(FaceAnim.HALF_ASKING, "And then will I get an abyssal whip?").also { stage++ }
            6 -> npcl(FaceAnim.CHILD_EVIL_LAUGH, "You'll get an$DARK_RED ABYSSAL WHIPPING</col>!").also { stage++ }
            7 -> playerl(FaceAnim.SAD, "That pun was abyssmal.").also { stage = END_DIALOGUE }
            8 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "I bet you just rush through it though. Everyone there",
                    "is in such a rush. No one stops to appreciate the",
                    "beauty of$DARK_RED THE ABYSS</col>.",
                ).also { stage++ }

            9 ->
                options(
                    "I have to run through quickly or I'll die.",
                    "The abyss looks pretty ugly to me.",
                ).also { stage++ }

            10 ->
                when (buttonID) {
                    1 -> player(FaceAnim.SCARED, "I have to run through quickly or I'll die.").also { stage++ }
                    2 -> player("The abyss looks pretty ugly to me.").also { stage = 12 }
                }

            11 ->
                npc(
                    FaceAnim.CHILD_FRIENDLY,
                    "Death is a small thing compared to the beauty of$DARK_RED THE",
                    "$DARK_RED ABYSS</col>.",
                ).also { stage = END_DIALOGUE }

            12 ->
                npc(FaceAnim.CHILD_FRIENDLY, "Poor deluded fool. There is no hope for you at all.").also {
                    stage = END_DIALOGUE
                }

            13 ->
                npc(
                    FaceAnim.CHILD_FRIENDLY,
                    "But does not the fear contribute to your appreciation",
                    "of$DARK_RED THE ABYSS</col>?",
                ).also { stage = END_DIALOGUE }

            14 -> options("No, it's just scary.", "I suppose fear does heighten the senses.").also { stage++ }
            15 ->
                when (buttonID) {
                    1 -> player(FaceAnim.HALF_GUILTY, "No, it's just scary.").also { stage++ }
                    2 ->
                        player(FaceAnim.HALF_GUILTY, "I suppose fear does heighten the senses.").also {
                            stage = 17
                        }
                }

            16 ->
                npcl(
                    FaceAnim.CHILD_EVIL_LAUGH,
                    "Poor human. You must not judge$DARK_RED THE ABYSS</col> by the standards of this world. You must learn to embrace your fear as part of the experience of$DARK_RED THE ABYSS</col>.",
                ).also { stage = END_DIALOGUE }

            17 ->
                npcl(
                    FaceAnim.CHILD_FRIENDLY,
                    "Then you should enhance them further by raising the stakes. Next time you go to$DARK_RED THE ABYSS</col> you should take all your most valuable items with you.",
                ).also { stage = END_DIALOGUE }
        }
    }
}

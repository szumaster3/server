package content.global.skill.summoning.pet.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SquirrelDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val babySquirrel =
        intArrayOf(
            NPCs.BABY_SQUIRREL_6919,
            NPCs.BABY_SQUIRREL_7301,
            NPCs.BABY_SQUIRREL_7303,
            NPCs.BABY_SQUIRREL_7305,
            NPCs.BABY_SQUIRREL_7307,
        )
    private val adultSquirrel =
        intArrayOf(NPCs.SQUIRREL_6920, NPCs.SQUIRREL_7302, NPCs.SQUIRREL_7304, NPCs.SQUIRREL_7306, NPCs.SQUIRREL_7308)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (npc.id in babySquirrel) {
            npcl(FaceAnim.CHILD_NORMAL, "Throw a ball for me!").also { stage = 0 }
            return true
        } else {
            when ((0..3).random()) {
                0 -> npcl(FaceAnim.CHILD_NORMAL, "Gimme a nut!").also { stage = 5 }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "Stop doing that and play with me!").also { stage = 7 }
                2 -> npcl(FaceAnim.CHILD_NORMAL, "This is boring, take me someplace fun.").also { stage = 9 }
                3 -> npcl(FaceAnim.CHILD_NORMAL, "Is it nearly nut time?").also { stage = 11 }
            }
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "Are you part-dog or something?").also { stage++ }
            1 -> npcl(FaceAnim.CHILD_NORMAL, "What's a dog?").also { stage++ }
            2 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "It's another kind of animal. Squirrels are more famous for eating nuts than chasing balls.",
                ).also { stage++ }

            3 -> npcl(FaceAnim.CHILD_NORMAL, "Give me a nut, then!").also { stage++ }
            4 -> playerl(FaceAnim.FRIENDLY, "I walked into that one, didn't I?").also { stage = END_DIALOGUE }

            5 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Not just now, I wouldn't want to spoil your dinner with one.",
                ).also { stage++ }

            6 -> npcl(FaceAnim.CHILD_NORMAL, "Awww...").also { stage = END_DIALOGUE }

            7 -> playerl(FaceAnim.FRIENDLY, "Okay, but just for a minute, I'm busy.").also { stage++ }
            8 -> npcl(FaceAnim.CHILD_NORMAL, "Yay!").also { stage = END_DIALOGUE }

            9 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "I'll be done in a moment, and you can play here if you want.",
                ).also { stage++ }

            10 -> npcl(FaceAnim.CHILD_NORMAL, "Huzzah!").also { stage = END_DIALOGUE }

            11 -> playerl(FaceAnim.FRIENDLY, "Didn't you just ask me that a little while ago?").also { stage++ }
            12 -> npcl(FaceAnim.CHILD_NORMAL, "Maybe.").also { stage++ }
            13 ->
                playerl(FaceAnim.FRIENDLY, "Then whatever answer I gave still applies now.").also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(*babySquirrel, *adultSquirrel)
    }
}

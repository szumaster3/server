package content.minigame.mta.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TelekineticGuardianDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        player("Hi.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "What do I have to do in this room?",
                    "What are the rewards?",
                    "Got any tips that may help me?",
                    "Thanks, bye!",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 -> player("What do I have to do in this room?").also { stage++ }
                    2 -> player("What are the rewards?").also { stage = 6 }
                    3 -> player("Got any tips that may help me?").also { stage = 8 }
                    4 -> player("Thanks, bye!").also { stage = END_DIALOGUE }
                }
            2 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "In this room you will see a maze within which one of",
                    "my fellow Guardians has been turned to stone for the",
                    "purpose of this exercise. You must move the statue",
                    "using your telekinetic grab spell to the exit square at",
                ).also {
                    stage++
                }
            3 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "the edge of the maze to bring the Guardian back to life.",
                    "Simply stand on the side that you wish for the statue to",
                    "travel towards and cast the spell on the statue. Once",
                    "you have solved the maze, the statue will change back",
                ).also {
                    stage++
                }
            4 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "into the Guardian and he will award you with",
                    "Telekinetic Pizazz Points and teleport you to the next",
                    "maze. You can switch to a better view of the maze by",
                    "selecting the 'Observe' option on the statue and return",
                ).also {
                    stage++
                }
            5 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "your view to normal by selecting the same option again.",
                    "There is also a 'Reset' option on the statue just in case",
                    "things aren't going too well.",
                ).also {
                    stage =
                        0
                }
            6 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "As well as the experience in casting magic, you will get",
                    "Telekinetic Pizazz Points for each maze successfully",
                    "solved and bonus points for completing five mazes in a",
                    "row without returning to the entrance.",
                ).also {
                    stage++
                }
            7 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "should also note that you will occasionally be rewarded",
                    "with items when you put one of the enchanted orbs in",
                    "the floor.",
                ).also {
                    stage =
                        0
                }
            8 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Have a good look at the maze before you try to solve it",
                    "because this can save you time and runes required to",
                    "navigate the maze. Although you will still be getting",
                    "magic experience for moving the statue incorrectly, you",
                ).also {
                    stage++
                }
            9 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "won't be progressing towards collecting Telekinetic",
                    "Pizazz Points. Lastly, all the mazes can be solved in ten",
                    "moves or less.",
                ).also {
                    stage++
                }
            10 -> player("I see.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TELEKINETIC_GUARDIAN_3098)
    }
}

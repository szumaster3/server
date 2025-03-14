package content.global.skill.summoning.pet.dialogue

import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SheepdogDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (npc.id in intArrayOf(6966, 7253, 7255)) {
            npc(FaceAnim.CHILD_NORMAL, "Yapyap yip yap!", "(Hello!)").also { stage = 0 }
            return true
        } else if (inInventory(player, Items.BALL_OF_WOOL_1759)) {
            npc(
                FaceAnim.CHILD_NORMAL,
                "Whurf. Whine woof whine whurf.",
                "(I say. It's not on, stealing someone else's coat.)",
            ).also { stage = 4 }
            return true
        } else {
            when ((0..3).random()) {
                0 ->
                    npc(
                        FaceAnim.CHILD_NORMAL,
                        "Whurf. Woof whurf woofwoof?",
                        "(I say. Where are we off to?)",
                    ).also { stage = 9 }

                1 ->
                    npc(
                        FaceAnim.CHILD_NORMAL,
                        "Whurf woof woof woof, whurfwoof?",
                        "(Are we lost, old sport?)",
                    ).also { stage = 11 }

                2 -> npc(FaceAnim.CHILD_NORMAL, "Whurf, woofwoof.", "(I say, old fruit.)").also { stage = 13 }
                3 ->
                    npc(
                        FaceAnim.CHILD_NORMAL,
                        "Whurfwoofwhurf, woof woof?",
                        "(What's the plan, old bean?)",
                    ).also { stage = 17 }
            }
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "Hello to you, too!").also { stage++ }
            1 -> npc(FaceAnim.CHILD_NORMAL, "Yipyipyip!", "(I'm a good dog!)").also { stage++ }
            2 -> player(FaceAnim.FRIENDLY, "Yes you are. You're a very good puppy.").also { stage++ }
            3 -> npc(FaceAnim.CHILD_NORMAL, "Yappypyappyyap!", "(Huzzah!)").also { stage = END_DIALOGUE }

            4 -> playerl(FaceAnim.HALF_ASKING, "What are you talking about?").also { stage++ }
            5 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Whurf whurf whurf whiiiine.",
                    "(All that fur you are holding there.)",
                ).also { stage++ }

            6 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "You mean this wool? It's from a sheep, not a sheepdog.",
                ).also { stage++ }

            7 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Woof, woof? Whoof woof whurf.",
                    "(Oh, really? How terribly embarrassing.)",
                ).also { stage++ }

            8 -> playerl(FaceAnim.CHILD_THINKING, "It was an honest mistake.").also { stage = END_DIALOGUE }

            9 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well, I've got a list of jobs to get done, and they do take me all over the place.",
                ).also { stage++ }

            10 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Whurfwhurf, woof? Whurf!",
                    "(Bit of a trek, then? Masterful!)",
                ).also { stage = END_DIALOGUE }

            11 -> playerl(FaceAnim.FRIENDLY, "No more than we were the last time you asked.").also { stage++ }
            12 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Woofwoofwhurf woof.",
                    "(Just keeping tabs on the situation.)",
                ).also { stage = END_DIALOGUE }

            13 -> playerl(FaceAnim.HALF_ASKING, "Yes? What do you want?").also { stage++ }
            14 -> npc(FaceAnim.CHILD_NORMAL, "Whurfwhurf.", "(Nothing, really.)").also { stage++ }
            15 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Oh, okay. Well, if you want something just bark, okay?",
                ).also { stage++ }

            16 ->
                npc(FaceAnim.CHILD_NORMAL, "Whurf woof woof.", "(Reading you loud and clear.)").also {
                    stage = END_DIALOGUE
                }

            17 ->
                playerl(
                    FaceAnim.ANNOYED,
                    "I haven't decided yet. When I do, you'll be the first to know.",
                ).also { stage++ }

            18 -> npc(FaceAnim.CHILD_NORMAL, "Whurf whurf!", "(Smashing!)").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.SHEEPDOG_PUPPY_6966,
            NPCs.SHEEPDOG_6967,
            NPCs.SHEEPDOG_PUPPY_7253,
            NPCs.SHEEPDOG_7254,
            NPCs.SHEEPDOG_PUPPY_7255,
            NPCs.SHEEPDOG_7256,
        )
    }
}

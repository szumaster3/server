package content.global.skill.summoning.pet.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RaccoonDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val babyRaccoon = intArrayOf(NPCs.BABY_RACCOON_6913, NPCs.BABY_RACCOON_7271, NPCs.BABY_RACCOON_7273)
    private val adultRaccoon = intArrayOf(NPCs.RACCOON_6914, NPCs.RACCOON_7272, NPCs.RACCOON_7274)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (npc.id in babyRaccoon) {
            npc(
                FaceAnim.CHILD_NORMAL,
                "Chitterchatterchittter chatter chatter!",
                "(I wanna go play, now!)",
            ).also { stage = 0 }
            return true
        } else {
            when ((0..3).random()) {
                0 ->
                    npc(
                        FaceAnim.CHILD_NORMAL,
                        "Chitter chatterchittter chitter?",
                        "(When we gonna do somethin' fun?)",
                    ).also { stage = 2 }

                1 ->
                    npc(
                        FaceAnim.CHILD_NORMAL,
                        "Chitterchatter chatterchitter chitter?",
                        "(When are we gonna be done here?)",
                    ).also { stage = 8 }

                2 ->
                    npc(
                        FaceAnim.CHILD_NORMAL,
                        "Chatter chatter chatter chatter?",
                        "(When is we gonna go back to one of those evergreen forests?)",
                    ).also { stage = 10 }

                3 ->
                    npc(
                        FaceAnim.CHILD_NORMAL,
                        "Chitter chatter chittterchatter chatter?",
                        "(So where are ya takin' me today?)",
                    ).also { stage = 16 }
            }
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (stage) {
            0 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well, why not just look at all this walking around after me as a game.",
                ).also { stage++ }

            1 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Chitter! Chatterchitter chitter!",
                    "(Yay! I win at this here game!)",
                ).also { stage = END_DIALOGUE }

            2 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Chitter chatterchittter chitter?",
                    "(When we gonna do somethin' fun?)",
                ).also { stage++ }

            4 -> playerl(FaceAnim.FRIENDLY, "What do you mean 'fun'?").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Chatter chatter chitter. (More fun than this. Like chasin' cats - I don't like kitties.)",
                ).also { stage++ }

            6 -> playerl(FaceAnim.FRIENDLY, "Oh, give me a minute and we'll see...").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Chatter chatter chitter? (Don't you keep me waiting too long, now - ya hear?)",
                ).also { stage = END_DIALOGUE }

            8 -> playerl(FaceAnim.FRIENDLY, "Well, when I'm finished what I'm doing, I'll tell you.").also { stage++ }
            9 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Chitterchatter chatterchitter chitter. (You don't tell me nothin'. You'd best start openin' up ter me soon, " +
                        if (player.isMale) "boy" else "girl" + ", or we are gonna fall out.)",
                ).also { stage = END_DIALOGUE }

            10 -> playerl(FaceAnim.FRIENDLY, "We'll go soon, I promise.").also { stage++ }
            11 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Chatter chitter. (I like it when them other critters run with us.)",
                ).also { stage++ }

            12 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Yeah, me to. The squirrels and the rabbits are really cute.",
                ).also { stage++ }

            13 -> npc(FaceAnim.CHILD_NORMAL, "Chatter.", "(Yeah.)").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Chatter chitter. (Don't ever refer to me as cute, though, " +
                        if (player.isMale) "boy" else "girl" + ".)",
                ).also { stage++ }

            15 -> playerl(FaceAnim.FRIENDLY, "No chance. I know better than that by now.").also { stage = END_DIALOGUE }

            16 -> playerl(FaceAnim.FRIENDLY, "Oh, I think we'll be covering a lot of ground today.").also { stage++ }
            17 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Chitter chatter chatter. (There'd better not be much walkin'. I don't like to walk much. You'd best carry me.)",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(*babyRaccoon, *adultRaccoon)
    }
}

package content.global.skill.summoning.familiar.dialogue.spirit

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Spirit tz kih dialogue.
 */
@Initializable
class SpiritTzKihDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return SpiritTzKihDialogue(player)
    }

    /**
     * Instantiates a new Spirit tz kih dialogue.
     */
    constructor()

    /**
     * Instantiates a new Spirit tz kih dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 5).toInt()) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "How's it going, Tz-kih?")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Does JalYt think Tz-kih as strong as Jad Jad?")
                stage = 3
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Have you heard of blood bat, JalYt?")
                stage = 5
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Pray pray pray pray pray pray pray pray!")
                stage = 10
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You drink pray, me drink pray.")
                stage = 13
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Pray pray?")
                stage++
            }

            1 -> {
                playerl(FaceAnim.FRIENDLY, "Don't start with all that again.")
                stage++
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Hmph, silly JalYt.")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "Are you as strong as TzTok-Jad? Yeah, sure, why not.")
                stage++
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Really? Thanks, JalYt. Tz-Kih strong and happy.")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "Blood bats? You mean vampire bats?")
                stage++
            }

            6 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yes. Blood bat.")
                stage++
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "Yes, I've heard of them. What about them?")
                stage++
            }

            8 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Tz-Kih like blood bat, but drink pray pray not blood blood. Blood blood is yuck."
                )
                stage++
            }

            9 -> {
                playerl(FaceAnim.FRIENDLY, "Thanks, Tz-Kih, that's nice to know.")
                stage = END_DIALOGUE
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "FRIENDLY down, Tz-Kih, we'll find you something to drink soon.")
                stage++
            }

            11 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Pray praaaaaaaaaaaaaay!")
                stage++
            }

            12 -> {
                playerl(FaceAnim.FRIENDLY, "Okay, okay. FRIENDLY down!")
                stage = END_DIALOGUE
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "What's that, Tz-Kih?")
                stage++
            }

            14 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "You got pray pray pot. Tz-Kih drink pray pray you, you drink pray pray pot."
                )
                stage++
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "You want to drink my Prayer points?")
                stage++
            }

            16 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yes. Pray pray.")
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "Err, not right now, Tz-Kih. I, er, need them myself.")
                stage++
            }

            18 -> {
                playerl(FaceAnim.FRIENDLY, "Sorry.")
                stage++
            }

            19 -> {
                npcl(FaceAnim.CHILD_NORMAL, "But, pray praaaay...?")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_TZ_KIH_7361, NPCs.SPIRIT_TZ_KIH_7362)
    }
}

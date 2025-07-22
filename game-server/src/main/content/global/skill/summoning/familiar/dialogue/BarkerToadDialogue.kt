package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import java.util.*

/**
 * The type Barker toad dialogue.
 */
@Initializable
class BarkerToadDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = BarkerToadDialogue(player)

    /**
     * Instantiates a new Barker toad dialogue.
     */
    constructor()

    /**
     * Instantiates a new Barker toad dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val rand = Random()
        when (rand.nextInt(6)) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Ladies and gentlemen, for my next trick, I shall swallow this fly!")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Roll up, roll up, roll up! See the greatest show on Gielinor!")
                stage = 5
            }

            2 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "We need to set up the big top somewhere near here. The locals look friendly enough.",
                )
                stage = 11
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Braaaaaaaaaaaaaaaaaaaaaaap!", "(*Burp!*)")
                stage = 13
            }

            4 -> {
                npc(FaceAnim.CHILD_NORMAL, "Mumblemumblegrumblemumble...", "(*Inaudible mumbles*)")
                stage = 18
            }

            5 -> {
                npc(FaceAnim.CHILD_NORMAL, "Bwaaarp graaaawk?", "(What's that croaking in your inventory?)")
                stage = 19
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Seen it.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Ah, but last time was the frog...on fire?")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "No! That would be a good trick.")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Well, it won't be this time either.")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Awwwww...")
                stage = END_DIALOGUE
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Roll up, roll up, roll up! See the greatest show on Gielinor!")
                stage++
            }

            6 -> {
                playerl(FaceAnim.HALF_ASKING, "Where?")
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Well, it's kind of...you.")
                stage++
            }

            8 -> {
                playerl(FaceAnim.HALF_ASKING, "Me?")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Roll up, roll up, roll up! See the greatest freakshow on Gielinor!")
                stage++
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "Don't make me smack you, slimy.")
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(FaceAnim.HALF_ASKING, "Are you kidding?")
                stage++
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Your problem is that you never see opportunities.")
                stage = END_DIALOGUE
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "That's disgusting behaviour!")
                stage++
            }

            14 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Braap craaaaawk craaaawk.",
                    "(That, my dear boy, was my world-renowned belching.)",
                )
                stage++
            }

            15 -> {
                playerl(FaceAnim.HALF_ASKING, "I got that part. Why are you so happy about it?")
                stage++
            }

            16 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Braaaaaaap craaaaaawk craaaaaaaawk.",
                    "(My displays have bedazzled the crowned heads of Gielinor.)",
                )
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "I'd give you a standing ovation, but I have my hands full.")
                stage = END_DIALOGUE
            }

            18 -> {
                playerl(FaceAnim.LAUGH, "Well, that cannonball seems to have shut him up!")
                stage = END_DIALOGUE
            }

            19 -> {
                playerl(FaceAnim.HALF_ASKING, "Ah, you mean that toad?")
                stage++
            }

            20 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, I'm guessing you're not going to like me carrying a toad about.")
                stage++
            }

            21 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Craaawk, croak. (I might not be all that happy, no.)")
                stage++
            }

            22 -> {
                playerl(FaceAnim.FRIENDLY, "I'm not going to eat it.")
                stage++
            }

            23 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Craaaaawk braaap croak.",
                    "(Weeeeell, I'd hope not! Reminds me of my mama toad.",
                    "She was inflated and fed to a jubbly, you know.",
                    "A sad, demeaning way to die.)",
                )
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BARKER_TOAD_6889, NPCs.BARKER_TOAD_6890)
}

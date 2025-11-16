package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import kotlin.random.Random

/**
 * Represents the Barker Toad familiar dialogue.
 */
@Initializable
class BarkerToadDialogue : Dialogue {

    private var branch: Int = -1

    override fun newInstance(player: Player?) = BarkerToadDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = Random.nextInt(6)
        stage = 0


        when (branch) {
            0 -> playerl(FaceAnim.FRIENDLY, "Seen it.")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Roll up, roll up, roll up! See the greatest show on Gielinor!")
            2 -> playerl(FaceAnim.HALF_ASKING, "Are you kidding?")
            3 -> playerl(FaceAnim.FRIENDLY, "That's disgusting behaviour!")
            4 -> playerl(FaceAnim.LAUGH, "Well, that cannonball seems to have shut him up!")
            5 -> playerl(FaceAnim.HALF_ASKING, "Ah, you mean that toad?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Ah, but last time was the frog...on fire?"); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "No! That would be a good trick."); stage++ }
                    2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Well, it won't be this time either."); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "Awwwww..."); stage = END_DIALOGUE }
                }
            }

            1 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.HALF_ASKING, "Where?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Well, it's kind of...you."); stage++ }
                    2 -> { playerl(FaceAnim.HALF_ASKING, "Me?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Roll up, roll up, roll up! See the greatest freakshow on Gielinor!"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Don't make me smack you, slimy."); stage = END_DIALOGUE }
                }
            }

            2 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Your problem is that you never see opportunities."); stage = END_DIALOGUE }
                }
            }

            3 -> {
                when (stage) {
                    0 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Braap craaaaawk craaaawk.", "(That, my dear boy, was my world-renowned belching.)"); stage++ }
                    1 -> { playerl(FaceAnim.HALF_ASKING, "I got that part. Why are you so happy about it?"); stage++ }
                    2 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Braaaaaaap craaaaaawk craaaaaaaawk.", "(My displays have bedazzled the crowned heads of Gielinor.)"); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "I'd give you a standing ovation, but I have my hands full."); stage = END_DIALOGUE }
                }
            }

            4 -> {
                when (stage) {
                    0 -> { stage = END_DIALOGUE }
                }
            }

            5 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Oh, I'm guessing you're not going to like me carrying a toad about."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Craaawk, croak. (I might not be all that happy, no.)"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "I'm not going to eat it."); stage++ }
                    3 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Craaaaawk braaap croak.", "(Weeeeell, I'd hope not! Reminds me of my mama toad.", "She was inflated and fed to a jubbly, you know.", "A sad, demeaning way to die.)"); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BARKER_TOAD_6889, NPCs.BARKER_TOAD_6890)
}

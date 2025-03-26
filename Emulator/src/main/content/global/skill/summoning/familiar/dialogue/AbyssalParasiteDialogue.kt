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
 * The type Abyssal parasite dialogue.
 */
@Initializable
class AbyssalParasiteDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return AbyssalParasiteDialogue(player)
    }

    /**
     * Instantiates a new Abyssal parasite dialogue.
     */
    constructor()

    /**
     * Instantiates a new Abyssal parasite dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val random = Random()
        val randomIndex = random.nextInt(5)

        when (randomIndex) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Ongk n'hd?")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Noslr'rh...")
                stage = 5
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Ace'e e ur'y!")
                stage = 9
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Tdsa tukk!")
                stage = 10
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Tdsa tukk!")
                stage = 12
            }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.HALF_WORRIED, "Oh, I'm not feeling so well.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Uge f't es?")
                stage++
            }

            2 -> {
                playerl(FaceAnim.SAD, "Please have mercy!")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "F'tp ohl't?")
                stage++
            }

            4 -> {
                playerl(FaceAnim.AFRAID, "I shouldn't have eaten that kebab. Please stop talking!")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.HALF_ASKING, "What's the matter?")
                stage++
            }

            6 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Kdso Seo...")
                stage++
            }

            7 -> {
                playerl(FaceAnim.HALF_ASKING, "Could you...could you mime what the problem is?")
                stage++
            }

            8 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yiao itl!")
                stage++
            }

            9 -> {
                playerl(
                    FaceAnim.HALF_ASKING,
                    "I want to help it but, aside from the language gap its noises make me retch!"
                )
                stage = END_DIALOGUE
            }

            10 -> {
                playerl(FaceAnim.HALF_WORRIED, "I think I'm going to be sick... The noises! Oh, the terrifying noises.")
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(FaceAnim.AFRAID, "Oh, the noises again.")
                stage = END_DIALOGUE
            }

            12 -> {
                playerl(FaceAnim.AFRAID, "Oh, the noises again.")
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Hem s'htee?")
                stage = END_DIALOGUE
            }
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ABYSSAL_PARASITE_6818, NPCs.ABYSSAL_PARASITE_6819)
    }
}

package content.global.skill.summoning.familiar.dialogue.spirit

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Spirit larupia dialogue.
 */
@Initializable
class SpiritLarupiaDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return SpiritLarupiaDialogue(player)
    }

    /**
     * Instantiates a new Spirit larupia dialogue.
     */
    constructor()

    /**
     * Instantiates a new Spirit larupia dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 4).toInt()) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Kitty cat!")
                stage = 0
            }

            1 -> {
                playerl(FaceAnim.FRIENDLY, "Hello friend!")
                stage = 5
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What are we doing today, master?")
                stage = 11
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Master, do you ever worry that I might eat you?")
                stage = 14
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What is your wish master?")
                stage++
            }

            1 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Have you ever thought about doing something other than hunting and serving me?"
                )
                stage++
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You mean, like stand-up comedy, master?")
                stage++
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "Umm...yes, like that.")
                stage++
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "No, master.")
                stage = END_DIALOGUE
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "'Friend', master? I do not understand this word.")
                stage++
            }

            6 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Friends are people, or animals, who like one another. I think we are friends."
                )
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Ah, I think I understand friends, master.")
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "Great!")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "A friend is someone who looks tasty, but you don't eat.")
                stage++
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "!")
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "I don't know, what do you want to do?")
                stage++
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I desire only to hunt and to serve my master.")
                stage++
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "Err...great! I guess I'll decide then.")
                stage = END_DIALOGUE
            }

            14 -> {
                playerl(FaceAnim.FRIENDLY, "No, of course not! We're pals.")
                stage++
            }

            15 -> {
                npcl(FaceAnim.CHILD_NORMAL, "That is good, master.")
                stage++
            }

            16 -> {
                playerl(FaceAnim.FRIENDLY, "Should I?")
                stage++
            }

            17 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Of course not, master.")
                stage++
            }

            18 -> {
                playerl(FaceAnim.FRIENDLY, "Oh. Good.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_LARUPIA_7337, NPCs.SPIRIT_LARUPIA_7338)
    }
}

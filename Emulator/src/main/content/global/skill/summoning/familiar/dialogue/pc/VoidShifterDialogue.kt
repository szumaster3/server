package content.global.skill.summoning.familiar.dialogue.pc

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Void shifter dialogue.
 */
@Initializable
class VoidShifterDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return VoidShifterDialogue(player)
    }

    /**
     * Instantiates a new Void shifter dialogue.
     */
    constructor()

    /**
     * Instantiates a new Void shifter dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val randomIndex = (Math.random() * 4).toInt()
        when (randomIndex) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What a splendid day, " + (if (player.isMale) "sir" else "madam") + "!")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm sorry to bother you, but could you assist me briefly?")
                stage = 3
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "How do you do?")
                stage = 9
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Lets go and see to those cads and bounders!")
                stage = 11
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Yes, it is!")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "It could only be marginally improved, perhaps, by tea and biscuits.")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "What a marvellous idea!")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "I suppose so.")
                stage++
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I was wondering, briefly, if perchance you might care to dance?")
                stage++
            }

            5 -> {
                playerl(FaceAnim.HALF_ASKING, "Dance? With a pest?")
                stage++
            }

            6 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Well, you see, I'm dreadfully out of practice and now I can barely leap, let alone teleport."
                )
                stage++
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "I'm not going to help you remember how to destroy the world!")
                stage++
            }

            8 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "What a beastly world we live in where one " + (if (player.isMale) "gentleman" else "lady") + " will not aid a pest in need..."
                )
                stage = END_DIALOGUE
            }

            9 -> {
                playerl(FaceAnim.FRIENDLY, "Okay, I suppose.")
                stage++
            }

            10 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Marvellous, simply marvellous!")
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(FaceAnim.HALF_ASKING, "Which 'cads and bounders' did you mean, exactly?")
                stage++
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Why, the ones with no honour, of course.")
                stage++
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "I don't think he knows what pests do...")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.VOID_SHIFTER_7367, NPCs.VOID_SHIFTER_7368, NPCs.VOID_SHIFTER_7369)
    }
}

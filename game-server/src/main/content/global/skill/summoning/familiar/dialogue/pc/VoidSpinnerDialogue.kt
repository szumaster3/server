package content.global.skill.summoning.familiar.dialogue.pc

import core.api.anyInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Void spinner dialogue.
 */
@Initializable
class VoidSpinnerDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = VoidSpinnerDialogue(player)

    /**
     * Instantiates a new Void spinner dialogue.
     */
    constructor()

    /**
     * Instantiates a new Void spinner dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (anyInInventory(player, Items.PURPLE_SWEETS_4561, Items.PURPLE_SWEETS_10476)) {
            npcl(FaceAnim.CHILD_NORMAL, "You have sweeties for spinner?")
            stage = 0
            return true
        }
        val randomIndex = (Math.random() * 4).toInt()
        when (randomIndex) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Let's go play hide an' seek!")
                stage = 6
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "My mummy told me I was clever.")
                stage = 9
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm coming to tickle you!")
                stage = 13
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Where's the sweeties?")
                stage = 16
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
                playerl(FaceAnim.FRIENDLY, "Sweeties? No sweeties here.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You do! You do! Gimmie sweeties!")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "I don't have any sweeties!")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What you hiding in your backpack, then?")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "That? Oh, that's...erm...worms! Yes, worms. Purple worms.")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yucky!")
                stage = END_DIALOGUE
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "Okay, you hide and I'll come find you.")
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You'll never find me!")
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "What a disaster that would be...")
                stage = END_DIALOGUE
            }

            9 -> {
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Aren't you meant to be the essence of a spinner? How do you have a mother?",
                )
                stage++
            }

            10 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What you mean, 'essence'?")
                stage++
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "Never mind, I don't think it matters.")
                stage++
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "My logimical powers has proved me smarterer than you!")
                stage = END_DIALOGUE
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "No! You've got so many tentacles!")
                stage++
            }

            14 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm coming to tickle you!")
                stage++
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "Aieee!")
                stage = END_DIALOGUE
            }

            16 -> {
                playerl(FaceAnim.FRIENDLY, "They are wherever good spinners go.")
                stage++
            }

            17 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yay for me!")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.VOID_SPINNER_7333, NPCs.VOID_SPINNER_7334)
}

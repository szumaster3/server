package content.global.skill.summoning.familiar.dialogue.titan

import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Iron titan dialogue.
 */
@Initializable
class IronTitanDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return IronTitanDialogue(player)
    }

    /**
     * Instantiates a new Iron titan dialogue.
     */
    constructor()

    /**
     * Instantiates a new Iron titan dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (inInventory(player, Items.IRON_BAR_2351, 1)) {
            npcl(FaceAnim.CHILD_SAD, "Are you using that iron bar, boss?")
            stage++
            return true
        }
        when ((Math.random() * 4).toInt()) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "Titan?")
                stage = 7
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Boss!")
                stage = 14
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Boss?")
                stage = 21
            }

            3 -> {
                playerl(FaceAnim.HALF_ASKING, "How are you today, titan?")
                stage = 27
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "Well, not right now, why?")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Can I have it, then?")
                stage++
            }

            2 -> {
                playerl(FaceAnim.HALF_ASKING, "What for?")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I've got a cunning plan.")
                stage++
            }

            4 -> {
                playerl(FaceAnim.HALF_ASKING, "Involving my iron bar?")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "No, but if I sell your iron bar I'll have enough money to buy a new hat.")
                stage++
            }

            6 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "You can't go through with a cunning plan without the right headgear, boss!"
                )
                stage = END_DIALOGUE
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yes, boss?")
                stage++
            }

            8 -> {
                playerl(FaceAnim.HALF_ASKING, "What's that in your hand?")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm glad you asked that, boss.")
                stage++
            }

            10 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "This is the prototype for the Iron Titan (tm) action figure. You just pull this string here and he fights crime with real action sounds."
                )
                stage++
            }

            11 -> {
                playerl(FaceAnim.ASKING, "Titan?")
                stage++
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yes, boss?")
                stage++
            }

            13 -> {
                playerl(FaceAnim.STRUGGLE, "Never mind.")
                stage = END_DIALOGUE
            }

            14 -> {
                playerl(FaceAnim.HALF_ASKING, "What?")
                stage++
            }

            15 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I've just had a vision of the future.")
                stage++
            }

            16 -> {
                playerl(FaceAnim.FRIENDLY, "I didn't know you were a fortune teller. Let's hear it then.")
                stage++
            }

            17 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Just imagine, boss, an Iron Titan (tm) on every desk.")
                stage++
            }

            18 -> {
                playerl(FaceAnim.FRIENDLY, "That doesn't even make sense.")
                stage++
            }

            19 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Hmm. It was a bit blurry, perhaps the future is having technical issues at the moment."
                )
                stage++
            }

            20 -> {
                playerl(FaceAnim.FRIENDLY, "Riiight.")
                stage = END_DIALOGUE
            }

            21 -> {
                playerl(FaceAnim.HALF_ASKING, "Yes, titan?")
                stage++
            }

            22 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You know how you're the boss and I'm the titan?")
                stage++
            }

            23 -> {
                playerl(FaceAnim.HALF_ASKING, "Yes?")
                stage++
            }

            24 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Do you think we could swap for a bit?")
                stage++
            }

            25 -> {
                playerl(FaceAnim.FRIENDLY, "No, titan!")
                stage++
            }

            26 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Aww...")
                stage = END_DIALOGUE
            }

            27 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm very happy.")
                stage++
            }

            28 -> {
                playerl(FaceAnim.HALF_ASKING, "That's marvellous, why are you so happy?")
                stage++
            }

            29 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Because I love the great taste of Iron Titan (tm) cereal!")
                stage++
            }

            30 -> {
                playerl(FaceAnim.ASKING, "?")
                stage++
            }

            31 -> {
                playerl(FaceAnim.ASKING, "You're supposed to be working for me, not promoting yourself.")
                stage++
            }

            32 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Sorry, boss.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.IRON_TITAN_7375, NPCs.IRON_TITAN_7376)
    }
}

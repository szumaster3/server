package content.global.skill.summoning.familiar.dialogue.pc

import core.api.anyInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Void Spinner familiar dialogue.
 */
@Initializable
class VoidSpinnerDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = VoidSpinnerDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (anyInInventory(player, Items.PURPLE_SWEETS_4561, Items.PURPLE_SWEETS_10476)) {
            branch = 0
            npcl(FaceAnim.CHILD_NORMAL, "You have sweeties for spinner?")
            stage = 0
            return true
        }

        if (branch == -1) branch = (Math.random() * 4).toInt() + 1
        stage = 0

        when (branch) {
            1 -> npcl(FaceAnim.CHILD_NORMAL, "Let's go play hide an' seek!")
            2 -> npcl(FaceAnim.CHILD_NORMAL, "My mummy told me I was clever.")
            3 -> npcl(FaceAnim.CHILD_NORMAL, "I'm coming to tickle you!")
            4 -> npcl(FaceAnim.CHILD_NORMAL, "Where's the sweeties?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> { // sweeties
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Sweeties? No sweeties here."); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "You do! You do! Gimmie sweeties!"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "I don't have any sweeties!"); stage++ }
                    3 -> { npcl(FaceAnim.CHILD_NORMAL, "What you hiding in your backpack, then?"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "That? Oh, that's...erm...worms! Yes, worms. Purple worms."); stage++ }
                    5 -> { npcl(FaceAnim.CHILD_NORMAL, "Yucky!"); stage = END_DIALOGUE }
                }
            }
            1 -> { // hide and seek
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Okay, you hide and I'll come find you."); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "You'll never find me!"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "What a disaster that would be..."); stage = END_DIALOGUE }
                }
            }
            2 -> { // clever mummy
                when (stage) {
                    0 -> { playerl(FaceAnim.HALF_ASKING, "Aren't you meant to be the essence of a spinner? How do you have a mother?"); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "What you mean, 'essence'?"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Never mind, I don't think it matters."); stage++ }
                    3 -> { npcl(FaceAnim.CHILD_NORMAL, "My logimical powers has proved me smarterer than you!"); stage = END_DIALOGUE }
                }
            }
            3 -> { // tickle
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "No! You've got so many tentacles!"); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "I'm coming to tickle you!"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Aieee!"); stage = END_DIALOGUE }
                }
            }
            4 -> { // sweeties question
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "They are wherever good spinners go."); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "Yay for me!"); stage = END_DIALOGUE }
                }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.VOID_SPINNER_7333, NPCs.VOID_SPINNER_7334)
}

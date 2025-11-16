package content.global.skill.summoning.familiar.dialogue.titan

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import kotlin.random.Random

/**
 * Represents the Steel Titan familiar dialogue.
 */
@Initializable
class SteelTitanDialogue : Dialogue {

    private var branch: Int = 0

    override fun newInstance(player: Player?) = SteelTitanDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = Random.nextInt(5)
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.CHILD_NORMAL, "Forward, master, to a battle that will waken the gods!"); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "I'd rather not, if it's all the same to you."); stage++ }
                    2 -> { npcl(FaceAnim.CHILD_NORMAL, "I shall never meet my end at this rate..."); stage = END_DIALOGUE }
                }
            }

            1 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.CHILD_NORMAL, "How do you wish to meet your end, master?"); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "Hopefully not for a very long time."); stage++ }
                    2 -> { npcl(FaceAnim.CHILD_NORMAL, "You do not wish to be torn asunder by the thousand limbs of a horde of demons?"); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "No! I'm quite happy picking flax and turning unstrung bows into gold..."); stage = END_DIALOGUE }
                }
            }

            2 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.CHILD_NORMAL, "Why must we dawdle when glory awaits?"); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "I'm beginning to think you just want me to die horribly..."); stage++ }
                    2 -> { npcl(FaceAnim.CHILD_NORMAL, "We could have deaths that bards sing of for a thousand years."); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "That's not much compensation."); stage = END_DIALOGUE }
                }
            }

            3 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.CHILD_NORMAL, "Master, we should be marching into glorious battle!"); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "You know, I think you're onto something."); stage++ }
                    2 -> { npcl(FaceAnim.CHILD_NORMAL, "We could find a death befitting such heroes of ${GameWorld.settings!!.name}!"); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "Ah. You know, I'd prefer not to die..."); stage++ }
                    4 -> { npcl(FaceAnim.CHILD_NORMAL, "Beneath the claws of a mighty foe shall I be sent into the embrace of death!"); stage = END_DIALOGUE }
                }
            }

            4 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.CHILD_NORMAL, "Let us go forth to battle, my ${if (player.isMale) "lord" else "lady"}!"); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "Why do you like fighting so much? It's not very nice to kill things."); stage++ }
                    2 -> { npcl(FaceAnim.CHILD_NORMAL, "It is the most honourable thing in life."); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "But I summoned you, I'm not sure I can even say that you're alive..."); stage++ }
                    4 -> { npcl(FaceAnim.CHILD_NORMAL, "Alas, you have discovered the woe of all summoned creatures' existence."); stage++ }
                    5 -> { playerl(FaceAnim.ASKING, "Really? I was right?"); stage++ }
                    6 -> { npcl(FaceAnim.CHILD_NORMAL, "Oh, woe...woe!"); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.STEEL_TITAN_7343, NPCs.STEEL_TITAN_7344)
}
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

    private var branch: Int = -1

    override fun newInstance(player: Player?) = SteelTitanDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as? NPC ?: return false
        branch = Random.nextInt(5)
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Forward, master, to a battle that will waken the gods!")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "How do you wish to meet your end, master?")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Why must we dawdle when glory awaits?")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Master, we should be marching into glorious battle!")
            4 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Let us go forth to battle, my ${if (player?.isMale == true) "lord" else "lady"}!")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "I'd rather not, if it's all the same to you."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I shall never meet my end at this rate..."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Hopefully not for a very long time."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "You do not wish to be torn asunder by the thousand limbs of a horde of demons?"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "No! I'm quite happy picking flax and turning unstrung bows into gold..."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "I'm beginning to think you just want me to die horribly..."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "We could have deaths that bards sing of for a thousand years."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "That's not much compensation."); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "You know, I think you're onto something."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "We could find a death befitting such heroes of ${GameWorld.settings!!.name}!"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Ah. You know, I'd prefer not to die..."); stage++ }
                3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Beneath the claws of a mighty foe shall I be sent into the embrace of death!"); stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Why do you like fighting so much? It's not very nice to kill things."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "It is the most honourable thing in life."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "But I summoned you, I'm not sure I can even say that you're alive..."); stage++ }
                3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Alas, you have discovered the woe of all summoned creatures' existence."); stage++ }
                4 -> { playerl(FaceAnim.ASKING, "Really? I was right?"); stage++ }
                5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Oh, woe...woe!"); stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.STEEL_TITAN_7343, NPCs.STEEL_TITAN_7344)
}
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
 * Represents the Bull Ant familiar dialogue.
 */
@Initializable
class BullAntDialogue : Dialogue {

    private var branch: Int = 0

    override fun newInstance(player: Player?) = BullAntDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        branch = if (player.settings.runEnergy < 50) 0 else Random.nextInt(1, 5)
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Sir...wheeze...yes Sir!"); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "Not enjoying the run? You need more training biped?"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Sir, no Sir! Sir, I'm enjoying the run a great deal, Sir!"); stage++ }
                    3 -> { npcl(FaceAnim.CHILD_NORMAL, "Then hop to, Private!"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!"); stage = END_DIALOGUE }
                }
            }

            1 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!"); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "We're going to work you so hard your boots fall off, understood?"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!"); stage++ }
                    3 -> { npcl(FaceAnim.CHILD_NORMAL, "Carry on Private!"); stage = END_DIALOGUE }
                }
            }

            2 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.CHILD_NORMAL, "Aten...hut!"); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "I can't believe they stuck me with you..."); stage++ }
                    2 -> { npcl(FaceAnim.CHILD_NORMAL, "What in the name of all the layers of the abyss do you think you're doing, biped?"); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "Sir, Private Player reporting for immediate active duty, Sir!"); stage++ }
                    4 -> { npcl(FaceAnim.CHILD_NORMAL, "As you were, Private!"); stage = END_DIALOGUE }
                }
            }

            3 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Buck up, Sir, it's not that bad."); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "Stow that, Private, and get back to work!"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!"); stage = END_DIALOGUE }
                }
            }

            4 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Sir, nothing Sir!"); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "Well double-time it, Private, whatever it is!"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!"); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BULL_ANT_6867, NPCs.BULL_ANT_6868)
}
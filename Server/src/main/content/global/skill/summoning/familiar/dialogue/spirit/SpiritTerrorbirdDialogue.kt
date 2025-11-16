package content.global.skill.summoning.familiar.dialogue.spirit

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Spirit Terrorbird familiar dialogue.
 */
@Initializable
class SpiritTerrorbirdDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = SpiritTerrorbirdDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (branch == -1) branch = (Math.random() * 5).toInt()
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "This is a fun little walk.")
            1 -> npcl(FaceAnim.OLD_NORMAL, "I can keep this up for hours.")
            2 -> npcl(FaceAnim.OLD_NORMAL, "Are we going to visit a bank soon?")
            3 -> npcl(FaceAnim.OLD_NORMAL, "Can we go to a bank now?")
            4 -> npcl(FaceAnim.OLD_NORMAL, "So...heavy...")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Why do I get the feeling you'll change your tune when I start loading you up with items?"); stage = END_DIALOGUE }
                }
            }
            1 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "I'm glad, as we still have plenty of time to go."); stage = END_DIALOGUE }
                }
            }
            2 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "I'm not sure, you still have plenty of room for more stuff."); stage++ }
                    1 -> { npcl(FaceAnim.OLD_NORMAL, "Just don't leave it too long, okay?"); stage = END_DIALOGUE }
                }
            }
            3 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Just give me a little longer, okay?"); stage++ }
                    1 -> { npcl(FaceAnim.OLD_NORMAL, "That's what you said last time!"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Did I?"); stage++ }
                    3 -> { npcl(FaceAnim.OLD_NORMAL, "Yes!"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Well, I mean it this time, promise."); stage = END_DIALOGUE }
                }
            }
            4 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "I knew you'd change your tune once you started carrying things."); stage++ }
                    1 -> { npcl(FaceAnim.OLD_NORMAL, "Can we go bank this stuff now?"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Sure. You do look like you're about to collapse."); stage = END_DIALOGUE }
                }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_TERRORBIRD_6794, NPCs.SPIRIT_TERRORBIRD_6795)
}
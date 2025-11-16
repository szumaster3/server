package content.global.skill.summoning.familiar.dialogue.spirit

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Spirit Larupia familiar dialogue.
 */
@Initializable
class SpiritLarupiaDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = SpiritLarupiaDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (branch == -1) {
            branch = (Math.random() * 4).toInt()
            when (branch) {
                0 -> playerl(FaceAnim.FRIENDLY, "Kitty cat!")
                1 -> playerl(FaceAnim.FRIENDLY, "Hello friend!")
                2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "What are we doing today, master?")
                3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Master, do you ever worry that I might eat you?")
            }
        }

        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "What is your wish master?"); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "Have you ever thought about doing something other than hunting and serving me?"); stage++ }
                    2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "You mean, like stand-up comedy, master?"); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "Umm...yes, like that."); stage++ }
                    4 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "No, master."); stage = END_DIALOGUE }
                }
            }
            1 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "'Friend', master? I do not understand this word."); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "Friends are people, or animals, who like one another. I think we are friends."); stage++ }
                    2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Ah, I think I understand friends, master."); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "Great!"); stage++ }
                    4 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "A friend is someone who looks tasty, but you don't eat."); stage++ }
                    5 -> { playerl(FaceAnim.FRIENDLY, "!"); stage = END_DIALOGUE }
                }
            }
            2 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "I don't know, what do you want to do?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I desire only to hunt and to serve my master."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Err...great! I guess I'll decide then."); stage = END_DIALOGUE }
                }
            }
            3 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "No, of course not! We're pals."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "That is good, master."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Should I?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Of course not, master."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Oh. Good."); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_LARUPIA_7337, NPCs.SPIRIT_LARUPIA_7338)
}
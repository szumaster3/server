package content.global.skill.summoning.familiar.dialogue.spirit

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Spirit Spider familiar dialogue.
 */
@Initializable
class SpiritSpiderDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = SpiritSpiderDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (branch == -1) branch = (Math.random() * 5).toInt()
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Where are we going?")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Who is that?")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "What are you doing?")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Sigh...")
            4 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "So, do I get any of those flies?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "I've not decided yet."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Fine, don't tell me..."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Oh, okay, well, we are going..."); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Don't want to know now."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Siiiigh...spiders."); stage = END_DIALOGUE }
                }
            }
            1 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Who?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "The two-legs over there."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "I can't see who you mean..."); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Never mind..."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Can you describe them a little better..."); stage++ }
                    5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "It doesn't matter now."); stage++ }
                    6 -> { playerl(FaceAnim.FRIENDLY, "Siiiigh...spiders."); stage = END_DIALOGUE }
                }
            }
            2 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Nothing that you should concern yourself with."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I see, you don't think I'm smart enough to understand..."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "That's not it at all! Look, I was..."); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Don't wanna know now."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Siiiigh...spiders."); stage = END_DIALOGUE }
                }
            }
            3 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "What is it now?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Nothing really."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Oh, well that's a relief."); stage = END_DIALOGUE }
                }
            }
            4 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "I don't know, I was saving these for a pet."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I see..."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Look, you can have some if you want."); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Oh, don't do me any favours."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Look, here, have some!"); stage++ }
                    5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Don't want them now."); stage++ }
                    6 -> { playerl(FaceAnim.FRIENDLY, "Siiiigh...spiders."); stage = END_DIALOGUE }
                }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_SPIDER_6841, NPCs.SPIRIT_SPIDER_6842)
}
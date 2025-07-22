package content.global.skill.summoning.familiar.dialogue.spirit

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Spirit spider dialogue.
 */
@Initializable
class SpiritSpiderDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = SpiritSpiderDialogue(player)

    /**
     * Instantiates a new Spirit spider dialogue.
     */
    constructor()

    /**
     * Instantiates a new Spirit spider dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 5).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Where are we going?")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Who is that?")
                stage = 5
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What are you doing?")
                stage = 12
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Sigh...")
                stage = 17
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "So, do I get any of those flies?")
                stage = 20
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
                playerl(FaceAnim.FRIENDLY, "I've not decided yet.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Fine, don't tell me...")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, okay, well, we are going...")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Don't want to know now.")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Siiiigh...spiders.")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "Who?")
                stage++
            }

            6 -> {
                npcl(FaceAnim.CHILD_NORMAL, "The two-legs over there.")
                stage++
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "I can't see who you mean...")
                stage++
            }

            8 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Never mind...")
                stage++
            }

            9 -> {
                playerl(FaceAnim.FRIENDLY, "Can you describe them a little better...")
                stage++
            }

            10 -> {
                npcl(FaceAnim.CHILD_NORMAL, "It doesn't matter now.")
                stage = 7
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "Siiiigh...spiders.")
                stage = END_DIALOGUE
            }

            12 -> {
                playerl(FaceAnim.FRIENDLY, "Nothing that you should concern yourself with.")
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I see, you don't think I'm smart enough to understand...")
                stage++
            }

            14 -> {
                playerl(FaceAnim.FRIENDLY, "That's not it at all! Look, I was...")
                stage++
            }

            15 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Don't wanna know now.")
                stage++
            }

            16 -> {
                playerl(FaceAnim.FRIENDLY, "Siiiigh...spiders.")
                stage = END_DIALOGUE
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "What is it now?")
                stage++
            }

            18 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Nothing really.")
                stage++
            }

            19 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, well that's a relief.")
                stage = END_DIALOGUE
            }

            20 -> {
                playerl(FaceAnim.FRIENDLY, "I don't know, I was saving these for a pet.")
                stage++
            }

            21 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I see...")
                stage++
            }

            22 -> {
                playerl(FaceAnim.FRIENDLY, "Look, you can have some if you want.")
                stage++
            }

            23 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Oh, don't do me any favours.")
                stage++
            }

            24 -> {
                playerl(FaceAnim.FRIENDLY, "Look, here, have some!")
                stage++
            }

            25 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Don't want them now.")
                stage = 7
            }

            26 -> {
                playerl(FaceAnim.FRIENDLY, "Siiiigh...spiders.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_SPIDER_6841, NPCs.SPIRIT_SPIDER_6842)
}

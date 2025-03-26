package content.global.skill.summoning.familiar.dialogue.pc

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Void ravager dialogue.
 */
@Initializable
class VoidRavagerDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return VoidRavagerDialogue(player)
    }

    /**
     * Instantiates a new Void ravager dialogue.
     */
    constructor()

    /**
     * Instantiates a new Void ravager dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val randomIndex = (Math.random() * 4).toInt()
        when (randomIndex) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You look delicious!")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Take me to the rift!")
                stage = 1
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Pardon me. Could I trouble you for a moment?")
                stage = 4
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "How do you bear life without ravaging?")
                stage = 12
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Don't make me dismiss you!")
                stage = END_DIALOGUE
            }

            1 -> {
                playerl(FaceAnim.FRIENDLY, "I'm not taking you there! Goodness knows what you'd get up to.")
                stage++
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I promise not to destroy your world...")
                stage++
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "If only I could believe you...")
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Yeah, sure.")
                stage++
            }

            5 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Oh, it's just a trifling thing. Mmm, trifle...you look like trifle...So, will you help?"
                )
                stage++
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "Fire away!")
                stage++
            }

            7 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Oh, just be honest. I just want a second opinion...Is this me? Mmm trifle..."
                )
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "Huh?")
                stage++
            }

            9 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Oh! The claws! The whiskers! The single, yellow eye! Oh! Is it me? Is it truly me?"
                )
                stage++
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "Erm...why yes...of course. It definitely reflects the inner you.")
                stage++
            }

            11 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Oh, I knew it! You've been an absolute delight. An angel delight! And everyone said it was just a phase!"
                )
                stage = END_DIALOGUE
            }

            12 -> {
                playerl(FaceAnim.FRIENDLY, "It's not always easy.")
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I could show you how to ravage, if you like...")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.VOID_RAVAGER_7370, NPCs.VOID_RAVAGER_7371)
    }
}

package content.global.skill.summoning.familiar.dialogue.titan

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Moss titan dialogue.
 */
@Initializable
class MossTitanDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return MossTitanDialogue(player)
    }

    /**
     * Instantiates a new Moss titan dialogue.
     */
    constructor()

    /**
     * Instantiates a new Moss titan dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Oh, look! A bug.")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "When you stamp on 'em, humies go squish.")
                stage = 9
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Stampy stampy stampy stampy stampy stampy, I've got big feet.")
                stage = 23
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What are we doing today?")
                stage = 31
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "It's quite a large bug.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "He's so cute! I wanna keep him.")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Well, be careful.")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm gonna call him Buggie and I'm gonna keep him in a box.")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Don't get overexcited.")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm gonna feed him and we're gonna be so happy together!")
                stage++
            }

            6 -> {
                sendDialogue("The Moss titan begins to bounce up and down.")
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Aww...Buggie went squish.")
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "Sigh.")
                stage = END_DIALOGUE
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "When you punch 'em, humies go squish.")
                stage++
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "...")
                stage++
            }

            11 -> {
                npcl(FaceAnim.CHILD_NORMAL, "When you push 'em, humies go squish.")
                stage++
            }

            12 -> {
                playerl(FaceAnim.FRIENDLY, "...")
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Squish squish squish.")
                stage++
            }

            14 -> {
                playerl(FaceAnim.FRIENDLY, "...")
                stage++
            }

            15 -> {
                npcl(FaceAnim.CHILD_NORMAL, "When you touch 'em, humies go squish.")
                stage++
            }

            16 -> {
                playerl(FaceAnim.FRIENDLY, "...")
                stage++
            }

            17 -> {
                npcl(FaceAnim.CHILD_NORMAL, "When you talk to 'em, humies go squish.")
                stage++
            }

            18 -> {
                playerl(FaceAnim.FRIENDLY, "...")
                stage++
            }

            19 -> {
                npcl(FaceAnim.CHILD_NORMAL, "When you poke 'em, humies go squish.")
                stage++
            }

            20 -> {
                playerl(FaceAnim.FRIENDLY, "...")
                stage++
            }

            21 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Squish squish squish.")
                stage++
            }

            22 -> {
                playerl(FaceAnim.FRIENDLY, "You have problems, you know that. Come on, we have got stuff to do.")
                stage = END_DIALOGUE
            }

            23 -> {
                playerl(FaceAnim.FRIENDLY, "Are you quite finished?")
                stage++
            }

            24 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Stampy stampy stampy stampy stampy stampy, I've got big hands.")
                stage++
            }

            25 -> {
                playerl(FaceAnim.FRIENDLY, "Done yet?")
                stage++
            }

            26 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Stampy stampy stampy stampy stampy stampy, I've got big chest.")
                stage++
            }

            27 -> {
                playerl(FaceAnim.FRIENDLY, "Done yet?")
                stage++
            }

            28 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Stampy stampy stampy stampy stampy stampy, I've got big hair.")
                stage++
            }

            29 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, be quiet and come on.")
                stage++
            }

            30 -> {
                npcl(FaceAnim.CHILD_NORMAL, "...")
                stage = END_DIALOGUE
            }

            31 -> {
                playerl(FaceAnim.FRIENDLY, "Let's just wait and see.")
                stage++
            }

            32 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I want to do some squishing of tiny things!")
                stage++
            }

            33 -> {
                playerl(FaceAnim.FRIENDLY, "Preferably not me.")
                stage++
            }

            34 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Even if only a little bit, like your foot or something?")
                stage++
            }

            35 -> {
                playerl(FaceAnim.FRIENDLY, "Um, no. I really don't fancy being squished today, thanks.")
                stage++
            }

            36 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Awww...")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MOSS_TITAN_7357, NPCs.MOSS_TITAN_7358)
    }
}

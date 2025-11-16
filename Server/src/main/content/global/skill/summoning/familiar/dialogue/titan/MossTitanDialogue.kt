package content.global.skill.summoning.familiar.dialogue.titan

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import kotlin.random.Random

/**
 * Represents the Moss Titan familiar dialogue.
 */
@Initializable
class MossTitanDialogue : Dialogue {

    private var branch: Int = 0

    override fun newInstance(player: Player?) = MossTitanDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = Random.nextInt(4)
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "It's quite a large bug."); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "He's so cute! I wanna keep him."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Well, be careful."); stage++ }
                    3 -> { npcl(FaceAnim.CHILD_NORMAL, "I'm gonna call him Buggie and I'm gonna keep him in a box."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Don't get overexcited."); stage++ }
                    5 -> { npcl(FaceAnim.CHILD_NORMAL, "I'm gonna feed him and we're gonna be so happy together!"); stage++ }
                    6 -> { sendDialogue("The Moss titan begins to bounce up and down."); stage++ }
                    7 -> { npcl(FaceAnim.CHILD_NORMAL, "Aww...Buggie went squish."); stage++ }
                    8 -> { playerl(FaceAnim.FRIENDLY, "Sigh."); stage = END_DIALOGUE }
                }
            }

            1 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.CHILD_NORMAL, "When you punch 'em, humies go squish."); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "..."); stage++ }
                    2 -> { npcl(FaceAnim.CHILD_NORMAL, "When you push 'em, humies go squish."); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "..."); stage++ }
                    4 -> { npcl(FaceAnim.CHILD_NORMAL, "Squish squish squish."); stage++ }
                    5 -> { playerl(FaceAnim.FRIENDLY, "..."); stage++ }
                    6 -> { npcl(FaceAnim.CHILD_NORMAL, "When you touch 'em, humies go squish."); stage++ }
                    7 -> { playerl(FaceAnim.FRIENDLY, "..."); stage++ }
                    8 -> { npcl(FaceAnim.CHILD_NORMAL, "When you talk to 'em, humies go squish."); stage++ }
                    9 -> { playerl(FaceAnim.FRIENDLY, "..."); stage++ }
                    10 -> { npcl(FaceAnim.CHILD_NORMAL, "When you poke 'em, humies go squish."); stage++ }
                    11 -> { playerl(FaceAnim.FRIENDLY, "..."); stage++ }
                    12 -> { npcl(FaceAnim.CHILD_NORMAL, "Squish squish squish."); stage++ }
                    13 -> { playerl(FaceAnim.FRIENDLY, "You have problems, you know that. Come on, we have got stuff to do."); stage = END_DIALOGUE }
                }
            }

            2 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Are you quite finished?"); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "Stampy stampy stampy stampy stampy stampy, I've got big hands."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Done yet?"); stage++ }
                    3 -> { npcl(FaceAnim.CHILD_NORMAL, "Stampy stampy stampy stampy stampy stampy, I've got big chest."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Done yet?"); stage++ }
                    5 -> { npcl(FaceAnim.CHILD_NORMAL, "Stampy stampy stampy stampy stampy stampy, I've got big hair."); stage++ }
                    6 -> { playerl(FaceAnim.FRIENDLY, "Oh, be quiet and come on."); stage++ }
                    7 -> { npcl(FaceAnim.CHILD_NORMAL, "..."); stage = END_DIALOGUE }
                }
            }

            3 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Let's just wait and see."); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "I want to do some squishing of tiny things!"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Preferably not me."); stage++ }
                    3 -> { npcl(FaceAnim.CHILD_NORMAL, "Even if only a little bit, like your foot or something?"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Um, no. I really don't fancy being squished today, thanks."); stage++ }
                    5 -> { npcl(FaceAnim.CHILD_NORMAL, "Awww..."); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MOSS_TITAN_7357, NPCs.MOSS_TITAN_7358)
}
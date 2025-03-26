package content.global.skill.summoning.familiar.dialogue

import content.global.skill.firemaking.Log
import core.api.anyInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import java.util.*

/**
 * The type Beaver dialogue.
 */
@Initializable
class BeaverDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return BeaverDialogue(player)
    }

    /**
     * Instantiates a new Beaver dialogue.
     */
    constructor()

    /**
     * Instantiates a new Beaver dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (anyInInventory(player, *logs)) {
            npcl(FaceAnim.CHILD_NORMAL, "'Ere, you 'ave ze logs, now form zem into a mighty dam!")
            stage = 0
            return true
        }

        val rand = Random()
        when (rand.nextInt(4)) {
            0 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Vot are you doing 'ere when we could be logging and building mighty dams, alors?"
                )
                stage = 2
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Pardonnez-moi - you call yourself a lumberjack?")
                stage = 5
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Paul Bunyan 'as nothing on moi!")
                stage = 7
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Zis is a fine day make some lumber.")
                stage = 10
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Well, I was thinking of burning, selling, or fletching them.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Sacre bleu! Such a waste.")
                stage = END_DIALOGUE
            }

            2 -> {
                playerl(FaceAnim.HALF_ASKING, "Why would I want to build a dam again?")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Why wouldn't you want to build a dam again?")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "I can't argue with that logic.")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "No")
                stage++
            }

            6 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Carry on zen.")
                stage = END_DIALOGUE
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "Except several feet in height, a better beard, and opposable thumbs.")
                stage++
            }

            8 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What was zat?")
                stage++
            }

            9 -> {
                playerl(FaceAnim.FRIENDLY, "Nothing.")
                stage = END_DIALOGUE
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "That it is!")
                stage++
            }

            11 -> {
                npcl(FaceAnim.CHILD_NORMAL, "So why are you talking to moi? Get chopping!")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BEAVER_6808)
    }

    companion object {
        private val logs = IntArray(Log.values().size)

        init {
            for (i in Log.values().indices) {
                logs[i] = Log.values()[i].logId
            }
        }
    }
}

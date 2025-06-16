package content.global.skill.summoning.familiar.dialogue

import core.api.anyInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import java.util.*

/**
 * The type Beaver dialogue.
 */
@Initializable
class BeaverDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = BeaverDialogue(player)

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

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (anyInInventory(player, Items.LOGS_1511, Items.OAK_LOGS_1521, Items.WILLOW_LOGS_1519, Items.MAPLE_LOGS_1517, Items.YEW_LOGS_1515, Items.MAGIC_LOGS_1513, Items.ACHEY_TREE_LOGS_2862, Items.PYRE_LOGS_3438, Items.OAK_PYRE_LOGS_3440, Items.WILLOW_PYRE_LOGS_3442, Items.MAPLE_PYRE_LOGS_3444, Items.YEW_PYRE_LOGS_3446, Items.MAGIC_PYRE_LOGS_3448, Items.TEAK_PYRE_LOGS_6211, Items.MAHOGANY_PYRE_LOG_6213, Items.MAHOGANY_LOGS_6332, Items.TEAK_LOGS_6333, Items.RED_LOGS_7404, Items.GREEN_LOGS_7405, Items.BLUE_LOGS_7406, Items.PURPLE_LOGS_10329, Items.WHITE_LOGS_10328, Items.SCRAPEY_TREE_LOGS_8934, Items.DREAM_LOG_9067, Items.ARCTIC_PYRE_LOGS_10808, Items.ARCTIC_PINE_LOGS_10810, Items.SPLIT_LOG_10812, Items.WINDSWEPT_LOGS_11035, Items.EUCALYPTUS_LOGS_12581, Items.EUCALYPTUS_PYRE_LOGS_12583, Items.JOGRE_BONES_3125)) {
            npcl(FaceAnim.CHILD_NORMAL, "'Ere, you 'ave ze logs, now form zem into a mighty dam!")
            stage = 0
            return true
        }

        val rand = Random()
        when (rand.nextInt(4)) {
            0 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Vot are you doing 'ere when we could be logging and building mighty dams, alors?",
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

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
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

    override fun getIds(): IntArray = intArrayOf(NPCs.BEAVER_6808)
}

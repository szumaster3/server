package content.global.skill.summoning.familiar.dialogue

import core.api.anyInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import kotlin.random.Random

/**
 * Represents the Beaver familiar dialogue.
 */
@Initializable
class BeaverDialogue : Dialogue {

    private var branch: Int = -1

    override fun newInstance(player: Player?) = BeaverDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        branch = if (anyInInventory(
                player,
                Items.LOGS_1511, Items.OAK_LOGS_1521, Items.WILLOW_LOGS_1519, Items.MAPLE_LOGS_1517,
                Items.YEW_LOGS_1515, Items.MAGIC_LOGS_1513, Items.ACHEY_TREE_LOGS_2862, Items.PYRE_LOGS_3438,
                Items.OAK_PYRE_LOGS_3440, Items.WILLOW_PYRE_LOGS_3442, Items.MAPLE_PYRE_LOGS_3444,
                Items.YEW_PYRE_LOGS_3446, Items.MAGIC_PYRE_LOGS_3448, Items.TEAK_PYRE_LOGS_6211,
                Items.MAHOGANY_PYRE_LOG_6213, Items.MAHOGANY_LOGS_6332, Items.TEAK_LOGS_6333,
                Items.RED_LOGS_7404, Items.GREEN_LOGS_7405, Items.BLUE_LOGS_7406, Items.PURPLE_LOGS_10329,
                Items.WHITE_LOGS_10328, Items.SCRAPEY_TREE_LOGS_8934, Items.DREAM_LOG_9067,
                Items.ARCTIC_PYRE_LOGS_10808, Items.ARCTIC_PINE_LOGS_10810, Items.SPLIT_LOG_10812,
                Items.WINDSWEPT_LOGS_11035, Items.EUCALYPTUS_LOGS_12581, Items.EUCALYPTUS_PYRE_LOGS_12583,
                Items.JOGRE_BONES_3125
            )
        ) 0 else Random.nextInt(4) + 1

        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "'Ere, you 'ave ze logs, now form zem into a mighty dam!"); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "Well, I was thinking of burning, selling, or fletching them."); stage++ }
                    2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Sacre bleu! Such a waste."); stage = END_DIALOGUE }
                }
            }

            1 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Vot are you doing 'ere when we could be logging and building mighty dams, alors?"); stage++ }
                    1 -> { playerl(FaceAnim.HALF_ASKING, "Why would I want to build a dam again?"); stage++ }
                    2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Why wouldn't you want to build a dam again?"); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "I can't argue with that logic."); stage = END_DIALOGUE }
                }
            }

            2 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Pardonnez-moi - you call yourself a lumberjack?"); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "No"); stage++ }
                    2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Carry on zen."); stage = END_DIALOGUE }
                }
            }

            3 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Paul Bunyan 'as nothing on moi!"); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "Except several feet in height, a better beard, and opposable thumbs."); stage++ }
                    2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "What was zat?"); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "Nothing."); stage = END_DIALOGUE }
                }
            }

            4 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Zis is a fine day make some lumber."); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "That it is!"); stage++ }
                    2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "So why are you talking to moi? Get chopping!"); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BEAVER_6808)
}
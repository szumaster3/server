package content.region.misthalin.dialogue.lumbridge.tutor

import core.api.sendDoubleItemDialogue
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class FishingTutorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        options(
            "Can you teach me the basics of fishing please?",
            "Tell me about different fish.",
            "Where and what should I fish?",
            "Goodbye.",
        ).also {
            stage =
                1
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "Can you teach me the basics of fishing please?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_ASKING, "Tell me about different fish.").also { stage = 20 }
                    3 -> player(FaceAnim.ASKING, "Where and what should I fish?").also { stage = 30 }
                    4 -> player(FaceAnim.FRIENDLY, "Goodbye.").also { stage = END_DIALOGUE }
                }
            10 ->
                npc(
                    FaceAnim.HAPPY,
                    "Ahoy, to fish, you click on your net in your inventory,",
                    "then on the fishin' spot to put it in. Then you pull the",
                    "net out and see if you got any shrimp.",
                ).also {
                    stage++
                }
            11 -> player(FaceAnim.THINKING, "I see.. is that it?").also { stage++ }
            12 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "There's far more as you progress, not just shrimps,",
                    "you get more equipment, bigger fish and other things",
                    "too...",
                ).also {
                    stage++
                }
            13 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "When you have a full inventory, you can cook it or",
                    "take it to the bank. You can find a bank on the roof of",
                    "the castle in Lumbridge and a cookin' range in the",
                    "castles kitchen.",
                ).also {
                    stage++
                }
            14 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Arrr.. if yer lookin' fer quests you should head to",
                    "the Mountain Dwarf who lies north-west of Taverley",
                    "and have a chat with him, I'm sure he can point you in",
                    "the right direction.",
                ).also {
                    stage++
                }
            15 ->
                options(
                    "Can you teach me the basics of fishing please?",
                    "Tell me about different fish.",
                    "Where and what should I fish?",
                    "Goodbye.",
                ).also {
                    stage =
                        1
                }
            20 -> options("Small net Fish", "Big Net Fish", "Rod and Fly Fishing", "Tell me about...").also { stage++ }
            21 ->
                when (buttonId) {
                    1 ->
                        sendItemDialogue(
                            player,
                            Items.SMALL_FISHING_NET_303,
                            "Ahoy, small net fishin' you can do just south of Draynor Village and in these very spots here. Aye.",
                        ).also {
                            stage =
                                210
                        }
                    2 ->
                        sendItemDialogue(
                            player,
                            Items.BIG_FISHING_NET_305,
                            "Aye, you can net yourself some big fish in Catherby, which is a good place to fish for most things, Gar!",
                        ).also {
                            stage =
                                220
                        }
                    3 ->
                        sendItemDialogue(
                            player,
                            Items.FISHING_ROD_307,
                            "Aye, rod fishin' can be practiced here at these spots, as well as south of Draynor Village and in the Lumbridge river, depending upon your experience. You can get bait at any fishin' shop, there be one in Port Sarim.",
                        ).also {
                            stage =
                                230
                        }
                    4 ->
                        options(
                            "Can you teach me the basics of fishing please?",
                            "Tell me about different fish.",
                            "Where and what should I fish?",
                            "Goodbye.",
                        ).also {
                            stage =
                                1
                        }
                }
            30 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Herrin' can be fished from Catherby and some other",
                    "places when you reach level 10.",
                ).also {
                    stage++
                }
            31 ->
                options(
                    "Can you teach me the basics of fishing please?",
                    "Tell me about different fish.",
                    "Where and what should I fish?",
                    "Goodbye.",
                ).also {
                    stage =
                        1
                }
            210 ->
                sendDoubleItemDialogue(
                    player,
                    Items.SHRIMPS_315,
                    Items.ANCHOVIES_319,
                    "Shrimps and anchovies can be caught with your small fishin' net",
                ).also {
                    stage =
                        20
                }
            220 ->
                sendDoubleItemDialogue(
                    player,
                    Items.MACKEREL_355,
                    Items.COD_339,
                    "Mackerel and Cod will form the backbone of your catch when big net fishin'.. except for the added extras...",
                ).also {
                    stage++
                }
            221 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Some rich rewards for big net fishin', make sure you be",
                    "using a big net fishing spot though...",
                ).also {
                    stage =
                        20
                }
            230 ->
                sendItemDialogue(
                    player,
                    Items.PIKE_351,
                    "With a rod you can catch pike, sardines and herring. Good eating on them.",
                ).also {
                    stage++
                }
            231 ->
                sendItemDialogue(
                    player,
                    Items.FLY_FISHING_ROD_309,
                    "The art of fly fishin' can be done in rivers, so the Lumbridge river here would suffice.",
                ).also {
                    stage++
                }
            232 ->
                sendItemDialogue(
                    player,
                    Items.SALMON_329,
                    "Aye, you can catch yourself a delicious trout or salmon.",
                ).also {
                    stage =
                        20
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return FishingTutorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FISHING_TUTOR_4901)
    }
}

package content.minigame.duelarena.dialogue

import core.api.sendDialogueOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MubarizDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hi!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Welcome to the Duel Arena!").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "Thanks! I need some information.").also { stage++ }
            2 -> npc(FaceAnim.HALF_GUILTY, "What would you like to know?").also { stage++ }
            3 ->
                sendDialogueOptions(
                    player,
                    "Information",
                    "What is this place?",
                    "How do I challenge someone to a duel?",
                    "What kind of options are there?",
                    "This place looks really old, where did it come from?",
                ).also { stage++ }

            4 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "What is this place?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "How do I challenge someone to a duel?").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_GUILTY, "What kind of options are there?").also { stage = 30 }
                    4 ->
                        player(FaceAnim.HALF_GUILTY, "This place looks really old, where did it come from?").also {
                            stage = 40
                        }
                }

            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "The Duel Arena has six duel arenas where you can",
                    "fight other players in a controlled environment. We",
                    "have our own dedicated hospital where we guarantee to",
                    "put you back together, even if you lose.",
                ).also { stage++ }

            11 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "In between the arenas are walkways where you can",
                    "watch the fights and challenge other players",
                ).also { stage++ }

            12 -> player(FaceAnim.HALF_GUILTY, "Sounds great. Thanks!").also { stage++ }
            13 -> npc(FaceAnim.HALF_GUILTY, "See you in the arenas!").also { stage = END_DIALOGUE }
            20 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "When you go to the arena you'll go up an access ramp",
                    "to the walkways that overlook the duel arenas. From the",
                    "walkways you can watch the duels and challenge other",
                    "players.",
                ).also { stage++ }

            21 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You'll know you're in the right place as you'll have a",
                    "Duel-with option when you right-click a player.",
                ).also { stage++ }

            22 -> player(FaceAnim.HALF_GUILTY, "I'm there!").also { stage = END_DIALOGUE }
            30 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You and your opponent can offer items as a stake. If",
                    "you win, you receive what your opponent staked, but if",
                    "you lose, your opponent will get whatever items you",
                    "staked.",
                ).also { stage++ }

            31 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You can choose to use rules to spice things up a bit.",
                    "For instance if you both agree to use the 'No Magic'",
                    "rule then neither player can use magic to attack the",
                    "other player. The fight will be restricted to ranging and",
                ).also { stage++ }

            32 -> npc(FaceAnim.HALF_GUILTY, "melee only.").also { stage++ }
            33 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "The rules are fairly self-evident with lots of different",
                    "combinations for you to try out!",
                ).also { stage++ }

            34 -> player(FaceAnim.HALF_GUILTY, "Cool! Thanks!").also { stage = END_DIALOGUE }
            40 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Now that the archaeologists have moved out, a group of",
                    "warriors, headed by myself, have bought the land and",
                    "converted it to a set of duel arenas. The best fighters",
                    "from around the world come here to fight!",
                ).also { stage++ }

            41 -> player(FaceAnim.HALF_GUILTY, "I challenge you!").also { stage++ }
            42 -> npc(FaceAnim.HALF_GUILTY, "Ho! Ho! Ho!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MUBARIZ_957)
    }
}

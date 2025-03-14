package content.region.misthalin.dialogue.varrock

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.ge.GuidePrices
import core.game.ge.GuidePrices.GuideType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ReloboBlinyoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hey there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.HALF_GUILTY, "Why hello to you too, my friend.").also { stage++ }
            1 ->
                options(
                    "You look like you've travelled a fair distance.",
                    "I'm trying to find the prices for logs.",
                    "Sorry, I need to be macking tracks.",
                ).also {
                    stage++
                }
            2 ->
                when (buttonId) {
                    1 ->
                        playerl(FaceAnim.HALF_GUILTY, "You look like you've travelled a fair distance.").also {
                            stage =
                                3
                        }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "I'm trying to find the prices for logs.").also { stage = 11 }
                    3 -> playerl(FaceAnim.HALF_GUILTY, "Sorry, I need to be making tracks.").also { stage = 10 }
                }
            3 -> npcl(FaceAnim.HALF_GUILTY, "What gave me away?").also { stage++ }
            4 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "I don't mean to be rude, but the face paint and",
                    "hair, for startes.",
                ).also {
                    stage++
                }
            5 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Ah, yes I'm from Shilo Village on Karamja. It's a style",
                    "I've had since I was little.",
                ).also {
                    stage++
                }
            6 -> playerl(FaceAnim.HALF_GUILTY, "Then tell me, why are you so far from home?").also { stage++ }
            7 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "This Grand Exchange! Isn't it marvellous I've never seen",
                    "anything like it in my life. My people were not pleased to",
                    "see me break traditions to visit such a place. But i hope",
                    "to make some serious profit. then they'll see I was right!",
                ).also {
                    stage++
                }
            8 -> player(FaceAnim.HALF_GUILTY, "So, what are you selling?").also { stage++ }
            9 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Logs! of all kinds! That's my plan, at least. Nature",
                    "is one thing my people understand very well.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            10 -> npc(FaceAnim.HALF_GUILTY, "Okay. Nice talking to you!").also { stage = END_DIALOGUE }
            11 -> npcl(FaceAnim.HALF_GUILTY, "Then you've come to the right person.").also { stage++ }
            12 -> {
                end()
                GuidePrices.open(player, GuideType.LOGS)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return ReloboBlinyoDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RELOBO_BLINYO_LOGS_6526)
    }
}

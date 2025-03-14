package content.region.misthalin.dialogue.varrock

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SurokMagisDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Excuse me?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "What do you want? ...Oh, wait. I know! You're",
                    "probably just like all the others, aren't you? After some",
                    "fancy spell or potion from me, I bet!",
                ).also {
                    stage++
                }
            1 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "No! at least, I don't think so. What sort of spells",
                    "do you have?",
                ).also {
                    stage++
                }
            2 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Hah! I knew it! I expect you want my Aphro-Dizzy-",
                    "Yak spell! Want someone to fall madly in love with you,",
                    "eh?",
                ).also {
                    stage++
                }
            3 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "That spell sounds very interesting, but I didn't mean to",
                    "disturb you!",
                ).also {
                    stage++
                }
            4 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, I see that you do have some manners. I'm glad",
                    "to see that you use them.",
                ).also {
                    stage++
                }
            5 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Now, if it's all the same, I am very bust at the",
                    "moment. Come back another time",
                    "please and thank you.",
                ).also {
                    stage++
                }
            6 -> player(FaceAnim.HALF_GUILTY, "Yes, of course!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SurokMagisDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SUROK_MAGIS_7002)
    }
}

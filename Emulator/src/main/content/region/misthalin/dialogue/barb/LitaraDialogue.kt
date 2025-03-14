package content.region.misthalin.dialogue.barb

import core.api.sendDialogueLines
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LitaraDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Hello there. You look lost - are you okay?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "I'm looking for stronghold, or something.",
                    "I'm fine, just passing through.",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "I'm looking for a stronghold, or something.").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "I'm fine, just passing through.").also { stage = END_DIALOGUE }
                }
            2 -> npc(FaceAnim.HALF_GUILTY, "Ahh, the Stronghold of Security. It's down there.").also { stage++ }
            3 ->
                sendDialogueLines(
                    player,
                    "Litara points to the hole in the ground that looks",
                    "like you could squeeze, through.",
                ).also {
                    stage++
                }
            4 -> player(FaceAnim.HALF_GUILTY, "Looks kind of ...deep and dark.").also { stage++ }
            5 -> npc(FaceAnim.HALF_GUILTY, "Yeah, tell that to my brother. He still hasn't come back.").also { stage++ }
            6 -> player(FaceAnim.HALF_GUILTY, "Your brother?").also { stage++ }
            7 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "He's an explorer too. When the miner fell down that hole",
                    "he'd made and came back babbling about treasure, my",
                    "brother went to explore. No one has seen him since.",
                ).also {
                    stage++
                }
            8 -> player(FaceAnim.HALF_GUILTY, "Oh, that's not good.").also { stage++ }
            9 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Lots of people have been down there, but none of them",
                    "have seen him. Let me know if you do, will you?",
                ).also {
                    stage++
                }
            10 -> player(FaceAnim.HALF_GUILTY, "I'll certainly keep my eyes open.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return LitaraDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LITARA_4376)
    }
}

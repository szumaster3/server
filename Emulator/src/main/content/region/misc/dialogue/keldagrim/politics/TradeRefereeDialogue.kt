package content.region.misc.dialogue.keldagrim.politics

import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TradeRefereeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when ((0..4).random()) {
            0 -> npc("Talk to the secretaries, not to me!").also { stage = END_DIALOGUE }
            1 -> npc("Clear out!").also { stage = END_DIALOGUE }
            2 -> npc("Stay out of the trade center!").also { stage = END_DIALOGUE }
            3 -> npc("I can't help you!").also { stage = END_DIALOGUE }
            4 -> npc("Can't talk to you, too busy!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return TradeRefereeDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TRADE_REFEREE_2127)
    }
}

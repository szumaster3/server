package content.global.dialogue

import content.global.skill.thieving.ThievingDefinition
import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.isQuestComplete
import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Fur trader dialogue.
 */
@Initializable
class FurTradeDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when(npc.id) {
            NPCs.FUR_TRADER_1316 -> if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                npc(FaceAnim.ANNOYED, "I don't sell to outlanders.").also { stage = END_DIALOGUE }
            } else {
                val canTrade = ThievingDefinition.Stall.handleStallCooldown(
                    player = player,
                    stallName = "FUR_STALL",
                    shopNpc = npc,
                    guardNpcIds = listOf(NPCs.MARKET_GUARD_1317, NPCs.WARRIOR_1318)
                )
                if (!canTrade) return false
                npcl(FaceAnim.FRIENDLY, "Welcome back, ${FremennikTrials.getFremennikName(player)}. Have you seen the furs I have today?").also { stage = 2 }
            }
            else -> npc(FaceAnim.HALF_GUILTY, "Would you like to trade in fur?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes.", "No.").also { stage++ }
            1 -> when (buttonId) {
                1 -> {
                    end()
                    openNpcShop(player, NPCs.FUR_TRADER_573)
                }
                2 -> player(FaceAnim.HALF_GUILTY, "No, thanks.").also { stage = END_DIALOGUE }
            }
            2 -> {
                end()
                openNpcShop(player, NPCs.FUR_TRADER_1316)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = FurTradeDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.FUR_TRADER_573, NPCs.FUR_TRADER_1316)
}

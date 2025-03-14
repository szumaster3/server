package content.region.misthalin.handlers

import content.global.handlers.iface.ge.ExchangeItemSets
import content.global.handlers.iface.ge.StockMarket
import core.api.getUsedOption
import core.api.openDialogue
import core.game.ge.ExchangeHistory
import core.game.ge.GuidePrices
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class GrandExchangeListener : InteractionListener {
    companion object {
        private val CLERK =
            intArrayOf(
                NPCs.GRAND_EXCHANGE_CLERK_6531,
                NPCs.GRAND_EXCHANGE_CLERK_6529,
                NPCs.GRAND_EXCHANGE_CLERK_6530,
                NPCs.GRAND_EXCHANGE_CLERK_6528,
            )
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, CLERK, "talk-to", "exchange", "history", "sets") { _, node ->
            val npc = node.asNpc()
            return@setDest npc.location.transform(node.direction, 1)
        }
    }

    override fun defineListeners() {
        on(CLERK, IntType.NPC, "talk-to", "exchange", "history", "sets") { player, node ->
            val records = ExchangeHistory.getInstance(player)
            if (getUsedOption(player) == "talk-to") {
                val npc = node as NPC
                player.dialogueInterpreter.open(npc.id, npc)
            }
            if (getUsedOption(player) == "exchange") StockMarket.openFor(player)
            if (getUsedOption(player) == "history") records.openHistoryLog(player)
            if (getUsedOption(player) == "sets") ExchangeItemSets.openFor(player)
            return@on true
        }

        on(Scenery.DESK_28089, IntType.SCENERY, "use", "exchange", "collect", "history", "sets") { player, _ ->
            val records = ExchangeHistory.getInstance(player)
            when (getUsedOption(player)) {
                "use" -> openDialogue(player, NPCs.GRAND_EXCHANGE_CLERK_6528)
                "exchange" -> StockMarket.openFor(player)
                "collect" -> records.openCollectionBox()
                "history" -> records.openHistoryLog(player)
                "sets" -> ExchangeItemSets.openFor(player)
            }
            return@on true
        }

        on(NPCs.FARID_MORRISANE_ORES_6523, IntType.NPC, "info-ores") { player, _ ->
            GuidePrices.open(player, GuidePrices.GuideType.ORES)
            return@on true
        }

        on(NPCs.BOB_BARTER_HERBS_6524, IntType.NPC, "info-herbs") { player, _ ->
            GuidePrices.open(player, GuidePrices.GuideType.HERBS)
            return@on true
        }

        on(NPCs.MURKY_MATT_RUNES_6525, IntType.NPC, "info-runes") { player, _ ->
            GuidePrices.open(player, GuidePrices.GuideType.RUNES)
            return@on true
        }

        on(NPCs.RELOBO_BLINYO_LOGS_6526, IntType.NPC, "info-logs") { player, _ ->
            GuidePrices.open(player, GuidePrices.GuideType.LOGS)
            return@on true
        }

        on(NPCs.HOFUTHAND_ARMOUR_AND_WEAPONS_6527, IntType.NPC, "info-combat") { player, _ ->
            GuidePrices.open(player, GuidePrices.GuideType.WEAPONS_AND_ARMOUR)
            return@on true
        }
    }
}

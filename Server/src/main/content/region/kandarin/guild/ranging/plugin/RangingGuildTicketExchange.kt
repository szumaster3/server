package content.region.kandarin.guild.ranging.plugin

import core.api.freeSlots
import core.api.itemDefinition
import core.api.removeItem
import core.api.sendMessage
import core.game.container.access.InterfaceContainer.generateItems
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items

class RangingGuildTicketExchange : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.RANGING_GUILD_TICKET_EXCHANGE_278) { player, _ ->
            val items = RangingGuildStock.values().map { it.item }
            player.generateItems(items, Components.RANGING_GUILD_TICKET_EXCHANGE_278, 16, listOf("Buy", "Value"), 3, 6)
            return@onOpen true
        }

        on(Components.RANGING_GUILD_TICKET_EXCHANGE_278) { player, _, opcode, _, slot, _ ->
            val exchange = RangingGuildStock.itemsMap[slot]

            if (exchange == null) {
                sendMessage(player, "Invalid exchange slot.")
                return@on true
            }

            when (opcode) {
                9 -> {
                    // Examine item.
                    sendMessage(player, itemDefinition(exchange.item.id).examine)
                }
                155 -> {
                    // Show value.
                    sendMessage(player, exchange.value)
                }
                196 -> {
                    // Exchange tickets for item.
                    val requiredTickets = exchange.tickets
                    if (freeSlots(player) < 1) {
                        sendMessage(player, "You don't have enough inventory space.")
                    } else if (!removeItem(player, Item(Items.ARCHERY_TICKET_1464, requiredTickets))) {
                        sendMessage(player, "You don't have enough Archery Tickets.")
                    } else {
                        player.inventory.add(exchange.item)
                        sendMessage(player, "You have received ${exchange.item.amount} x ${itemDefinition(exchange.item.id).name}.")
                    }
                }
            }
            return@on true
        }
    }
}


/**
 * Ticket exchange.
 *
 * @param item      The item to be exchanged.
 * @param tickets   The number of tickets required for the exchange.
 * @param slot      The slot number of the exchange.
 * @param value     The value of the exchange.
 */
private enum class RangingGuildStock(
    val item: Item,
    val tickets: Int,
    val slot: Int,
    val value: String,
) {
    BARB_BOLT_TIPS(Item(Items.BARB_BOLTTIPS_47, 30), 140, 0, "The 30 Barb Boltips cost 140 Archery Tickets."),
    STUDDED_BODY(Item(Items.STUDDED_BODY_1133, 1), 150, 1, "The Studded Body costs 150 Archery Tickets."),
    RUNE_ARROW(Item(Items.RUNE_ARROW_892, 50), 2000, 2, "The 50 Rune Arrows cost 2,000 Archery Tickets."),
    COIF(Item(Items.COIF_1169, 1), 100, 3, "The Coif costs 100 Archery Tickets."),
    GREEN_D_HIDE(Item(Items.GREEN_DHIDE_BODY_1135, 1), 2400, 4, "The Dragonhide body costs 2,400 Archery Tickets."),
    ADAMANT_JAVELIN(Item(Items.ADAMANT_JAVELIN_829, 20), 2000, 5, "The 20 Adamant Javelins cost 2,000 Archery Tickets."),
    ;

    companion object {
        val itemsMap = HashMap<Int, RangingGuildStock>()

        init {
            for (exchange in values()) {
                itemsMap[exchange.slot] = exchange
            }
        }
    }
}

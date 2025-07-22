package content.global.plugin.iface.ge

import core.api.*
import core.cache.def.impl.CS2Mapping
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.ge.ExchangeHistory
import core.game.ge.GuidePrices
import core.game.ge.ItemSet
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.ContainerPacket
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.Sounds

/**
 * Handles the Grand Exchange interface options.
 *
 * @author Emperor
 */
@Initializable
class GrandExchangeInterface : ComponentPlugin() {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        // ComponentDefinition.put(105, this) // Main interface
        // ComponentDefinition.put(107, this) // Selling tab
        ComponentDefinition.put(Components.STOCKCOLLECT_109, this)
        ComponentDefinition.put(Components.EXCHANGE_SEARCH_389, this)
        ComponentDefinition.put(Components.EXCHANGE_SETS_SIDE_644, this)
        ComponentDefinition.put(Components.EXCHANGE_ITEMSETS_645, this)
        ComponentDefinition.put(Components.EXCHANGE_GUIDE_PRICE_642, this)
        return this
    }

    override fun handle(
        player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int
    ): Boolean {
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                when (component.id) {
                    Components.EXCHANGE_SETS_SIDE_644, Components.EXCHANGE_ITEMSETS_645 -> {
                        handleItemSet(player, component, opcode, button, slot, itemId)
                        return true
                    }

                    Components.EXCHANGE_SEARCH_389 -> {
                        handleSearchInterface(player, opcode, button, slot, itemId)
                        return true
                    }

                    Components.STOCKCOLLECT_109 -> {
                        handleCollectionBox(player, opcode, button, slot, itemId)
                        return true
                    }

                    Components.EXCHANGE_GUIDE_PRICE_642 -> {
                        handleGuidePrice(player, opcode, button, slot, itemId)
                        return true
                    }
                }
                return true
            }
        })
        return true
    }

    /**
     * Handles the search interface options.
     */
    fun handleSearchInterface(player: Player, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        return when (button) {
            10 -> {
                closeChatBox(player)
                true
            }

            else -> false
        }
    }

    /**
     * Handles the selling tab interface options.
     */
    fun handleCollectionBox(player: Player, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        var index = -1
        when (button) {
            18, 23, 28 -> index = (button - 18) shr 2
            36, 44, 52 -> index = 3 + ((button - 36) shr 3)
        }
        val records = ExchangeHistory.getInstance(player)
        val offer = if (index > -1) records.getOffer(records.offerRecords[index]) else null
        if (offer != null) {
            StockMarket.withdraw(player, offer, slot shr 1, opcode)
        }
        return true
    }

    /**
     * Handles the item set.
     */
    private fun handleItemSet(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int) {
        if (button != 16 && button != 0) return

        val inventory = component.id == Components.EXCHANGE_SETS_SIDE_644

        if (slot < 0 || slot >= if (inventory) 28 else ItemSet.values().size) return

        val item: Item?
        val set: ItemSet?

        if (inventory) {
            item = player.inventory[slot]
            if (item == null) return
            set = ItemSet.forId(item.id) ?: return
        } else {
            set = ItemSet.values()[slot]
            item = Item(set.itemId)
        }

        if (opcode != 127 && inventory && set == null) {
            sendMessage(player, "This isn't a set item.")
            return
        }

        when (opcode) {
            9 -> sendMessage(player, item.definition.examine)
            196 -> {
                if (inventory) {
                    if (freeSlots(player) < set.components.size - 1) {
                        sendMessage(player, "You don't have enough inventory space for the component parts.")
                        return
                    }
                    if (!player.inventory.remove(item, false)) return
                    for (id in set.components) {
                        player.inventory.add(Item(id, 1))
                    }
                    refreshInventory(player)
                    sendMessage(player, "You successfully traded your set for its component items!")
                } else {
                    if (!player.inventory.containItems(*set.components)) {
                        sendMessage(player, "You don't have the parts that make up this set.")
                        return
                    }
                    for (id in set.components) {
                        player.inventory.remove(Item(id, 1), false)
                    }
                    player.inventory.add(item)
                    refreshInventory(player)
                    sendMessage(player, "You successfully traded your item components for a set!")
                }
                playAudio(player, Sounds.GE_TRADE_OK_4044)
                PacketRepository.send(
                    ContainerPacket::class.java, OutgoingContext.Container(
                        player, -1, -2, player.getAttribute("container-key", 93), player.inventory, false
                    )
                )
            }

            155 -> {
                val mapping = CS2Mapping.forId(1089)
                if (mapping != null && set != null) {
                    val message = mapping.map?.get(set.itemId) as? String ?: ""
                    sendMessage(player, message)
                }
            }
        }
    }

    /**
     * Handles the guide price opcode.
     */
    private fun handleGuidePrice(player: Player, opcode: Int, buttonId: Int, slot: Int, itemId: Int) {
        if (opcode != 155) return

        val type = player.getAttribute<GuidePrices.GuideType>("guide-price", null) ?: return

        val subtract = when (buttonId) {
            in 15..23 -> 15
            in 43..57 -> 43
            in 89..103 -> 89
            in 135..144 -> 135
            in 167..182 -> 167
            else -> 0
        }

        sendMessage(player, ItemDefinition.forId(type.items[buttonId - subtract].item).examine)
    }
}

package content.minigame.impetuous.plugin

import content.global.skill.hunter.bnet.BNetTypes
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

class ElnockExchangeInterface : InterfaceListener {

    override fun defineInterfaceListeners() {
        onOpen(Components.ELNOCK_EXCHANGE_540) { player, _ ->
            val values = intArrayOf(22, 25, 28, 31)
            ElnockExchange.values().forEachIndexed { i, e ->
                sendItemZoomOnInterface(player, Components.ELNOCK_EXCHANGE_540, values[i], e.sendItem)
            }
            return@onOpen true
        }

        on(Components.ELNOCK_EXCHANGE_540) { player, _, _, buttonID, _, _ ->
            var exchange = player.getAttribute<ElnockExchange>("exchange", null)

            if (buttonID == 34) { // Confirm button.
                setVarp(player, 1018, 0)

                if (exchange == null) {
                    sendMessage(player, "Make a selection before confirming.")
                    return@on true
                }

                if (!exchange.hasItems(player)) {
                    sendMessage(player, "You don't have the required implings in a jar to trade for this.")
                    return@on true
                }

                if (exchange == ElnockExchange.JAR_GENERATOR && player.hasItem(ElnockExchange.JAR_GENERATOR.reward)) {
                    sendMessage(player, "You can't have more than one jar generator at a time.")
                    return@on true
                }

                val requiredSpace = exchange.reward.amount
                val free = freeSlots(player)
                if (free < requiredSpace) {
                    val needed = requiredSpace - free
                    if (exchange.reward.id == Items.IMPLING_JAR_11260) {
                        sendDialogue(player, "You'll need $needed empty inventory ${if (needed > 1) "spaces" else "space"} to hold the impling ${if (needed > 1) "jars" else "jar"}.")
                    } else {
                        sendMessage(player, "You don't have enough inventory space.")
                    }
                    closeInterface(player)
                    removeAttribute(player, "exchange")
                    return@on true
                }

                // Remove required items.
                val removed = if (exchange == ElnockExchange.IMPLING_JAR) {
                    player.inventory.remove(ElnockExchange.getItem(player))
                } else {
                    player.inventory.remove(*exchange.required)
                }

                if (removed) {
                    closeInterface(player)
                    removeAttribute(player, "exchange")
                    player.inventory.add(exchange.reward, player)

                    val rewardName = getItemName(exchange.reward.id).lowercase()
                    val message = if (exchange.reward.id == Items.IMPLING_JAR_11260) {
                        "Elnock gives you three $rewardName."
                    } else {
                        "Elnock gives you $rewardName."
                    }

                    sendItemDialogue(player, exchange.reward, message)

                    addDialogueAction(player) { _, _ ->
                        sendNPCDialogue(player, NPCs.ELNOCK_INQUISITOR_6070, "Pleasure doing business with you!")
                    }
                }
                return@on true
            }

            // Selection buttons.
            exchange = ElnockExchange.forButton(buttonID)
            if (exchange != null) {
                setAttribute(player, "exchange", exchange)
                setVarp(player, 1018, exchange.configValue)
            }

            return@on true
        }
    }
}

private enum class ElnockExchange(val button: Int, val configValue: Int, val sendItem: Int, val reward: Item, vararg val required: Item) {
    IMP_REPELLENT(23, 444928, Items.PICTURE_11271, Item(Items.IMP_REPELLENT_11262), Item(Items.BABY_IMPLING_JAR_11238, 3), Item(Items.YOUNG_IMPLING_JAR_11240, 2), Item(Items.GOURM_IMPLING_JAR_11242)),
    MAGIC_BUTTERFLY(26, 707072, Items.PICTURE_11268, Item(Items.MAGIC_BUTTERFLY_NET_11259), Item(Items.GOURM_IMPLING_JAR_11242, 3), Item(Items.EARTH_IMPLING_JAR_11244, 2), Item(Items.ESS_IMPLING_JAR_11246)),
    JAR_GENERATOR(29, 969216, Items.PICTURE_11267, Item(Items.JAR_GENERATOR_11258), Item(Items.ESS_IMPLING_JAR_11246, 3), Item(Items.ECLECTIC_IMPLING_JAR_11248, 2), Item(Items.NATURE_IMPLING_JAR_11250)),
    IMPLING_JAR(32, 1231360, Items.PICTURE_11269, Item(Items.IMPLING_JAR_11260, 3))
    {
        override fun hasItems(player: Player): Boolean = BNetTypes.getImplings().any { player.inventory.containsItem(it.reward) }
    };

    open fun hasItems(player: Player): Boolean = player.inventory.containsItems(*required)

    companion object {
        @JvmStatic
        fun getItem(player: Player): Item? = BNetTypes.getImplings().firstOrNull { player.inventory.containsItem(it.reward) }?.reward

        @JvmStatic
        fun forButton(button: Int): ElnockExchange? = values().firstOrNull { it.button == button }
    }
}
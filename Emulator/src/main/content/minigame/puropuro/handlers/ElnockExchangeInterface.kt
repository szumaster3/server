package content.minigame.puropuro.handlers

import core.api.*
import core.game.interaction.InterfaceListener
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

class ElnockExchangeInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.ELNOCK_EXCHANGE_540) { player, _ ->
            val values = intArrayOf(22, 25, 28, 31)
            for (i in ElnockExchange.values().indices) {
                val e = ElnockExchange.values()[i]
                sendItemZoomOnInterface(player, Components.ELNOCK_EXCHANGE_540, values[i], e.sendItem)
            }
            return@onOpen true
        }

        on(Components.ELNOCK_EXCHANGE_540) { player, _, _, buttonID, _, _ ->
            var exchange = player.getAttribute<ElnockExchange>("exchange", null)
            if (buttonID == 34) {
                setVarp(player, 1018, 0)
                if (exchange == null) {
                    sendMessage(player, "Making a selection before confirming.")
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
                if (!hasSpaceFor(player, exchange.reward)) {
                    var amount = exchange.reward.amount - freeSlots(player)
                    when {
                        (exchange.reward.id == Items.IMPLING_JAR_11260) ->
                            sendDialogue(
                                player,
                                "You'll need $amount empty inventory ${if (freeSlots(
                                        player,
                                    ) <= 1
                                ) {
                                    "spaces"
                                } else {
                                    "space"
                                }} to hold the impling ${
                                    if (freeSlots(player) <= 1) "jars" else "jar"
                                }.",
                            )

                        else -> sendMessage(player, "You don't have enough inventory space.")
                    }
                    closeInterface(player)
                    removeAttribute(player, "exchange")
                    return@on true
                }
                if (if (exchange ==
                        ElnockExchange.IMPLING_JAR
                    ) {
                        player.inventory.remove(ElnockExchange.getItem(player))
                    } else {
                        player.inventory.remove(
                            *exchange.required,
                        )
                    }
                ) {
                    closeInterface(player)
                    removeAttribute(player, "exchange")
                    player.inventory.add(exchange.reward, player)

                    when {
                        (exchange.reward.id == Items.IMPLING_JAR_11260) ->
                            sendItemDialogue(
                                player,
                                exchange.reward,
                                "Elnock gives you three ${getItemName(exchange.reward.id).lowercase()}s.",
                            )

                        else ->
                            sendItemDialogue(
                                player,
                                exchange.reward,
                                "Elnock gives you ${getItemName(exchange.reward.id).lowercase()}.",
                            )
                    }

                    addDialogueAction(player) { _, button ->
                        if (button > 1) {
                            sendNPCDialogue(player, NPCs.ELNOCK_INQUISITOR_6070, "Pleasure doing business with you!")
                        }
                    }
                }
                return@on true
            }

            exchange = ElnockExchange.forButton(buttonID)
            if (exchange != null) {
                setAttribute(player, "exchange", exchange)
                setVarp(player, 1018, exchange.configValue)
            }
            return@on true
        }
    }
}

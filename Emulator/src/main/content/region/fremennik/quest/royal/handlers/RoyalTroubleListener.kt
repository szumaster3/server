package content.region.fremennik.quest.royal.handlers

import core.api.addItem
import core.api.inInventory
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class RoyalTroubleListener : InteractionListener {
    private val FIRE_REMAINS = intArrayOf(15206, 15207, 15208, 15209, 15210)
    private val ROCKS = 15213

    override fun defineListeners() {
        on(FIRE_REMAINS, IntType.SCENERY, "search") { player, node ->
            when (node.id) {
                15206 -> {
                    sendMessage(player, "You find a burnt diary with one page in the embers.")
                    addItem(player, Items.BURNT_DIARY_7961)
                }

                15207 -> {
                    if (removeItem(player, Items.BURNT_DIARY_7961)) {
                        addItem(player, Items.BURNT_DIARY_7962)
                        sendMessage(player, "You find some diary pages in the embers.")
                        sendMessage(player, "You add them to the diary you have.")
                    } else {
                        sendMessage(player, "You search the embers but find nothing.")
                    }
                }

                15208 -> {
                    if (removeItem(player, Items.BURNT_DIARY_7962)) {
                        addItem(player, Items.BURNT_DIARY_7963)
                        sendMessage(player, "You find some diary pages in the embers.")
                        sendMessage(player, "You add them to the diary you have.")
                    } else {
                        sendMessage(player, "You search the embers but find nothing.")
                    }
                }

                15209 -> {
                    if (removeItem(player, Items.BURNT_DIARY_7963)) {
                        addItem(player, Items.BURNT_DIARY_7964)
                        sendMessage(player, "You find some diary pages in the embers.")
                        sendMessage(player, "You add them to the diary you have.")
                    } else {
                        sendMessage(player, "You search the embers but find nothing.")
                    }
                }

                else -> {
                    if (removeItem(player, Items.BURNT_DIARY_7964)) {
                        addItem(player, Items.BURNT_DIARY_7965)
                        sendMessage(player, "You find some diary pages in the embers.")
                        sendMessage(player, "You add them to the diary you have.")
                    } else {
                        sendMessage(player, "You search the embers but find nothing.")
                    }
                }
            }
            return@on true
        }

        on(ROCKS, IntType.SCENERY, "Look-At") { player, _ ->
            if (!inInventory(player, Items.BURNT_DIARY_7961)) {
                sendMessage(player, "I should check the fires I saw ealier in the tunnel.")
            } else {
                sendMessage(player, "You can't reach!")
            }
            return@on true
        }
    }
}

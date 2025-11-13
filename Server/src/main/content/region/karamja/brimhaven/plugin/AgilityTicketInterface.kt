package content.region.karamja.brimhaven.plugin

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.StringUtils
import shared.consts.Components
import shared.consts.Items

/**
 * Handles agility tickets exchange.
 */
class AgilityTicketInterface : InterfaceListener {

    private data class RewardData(val tickets: Int, val experience: Double = 0.0, val reward: Item? = null) {
        val displayName: String?
            get() = reward?.let {
                StringUtils.formatDisplayName(it.name.replace("Clean", "").trim())
            }
    }

    private val rewardsMap = mapOf(
        21 to RewardData(tickets = 1, experience = 240.0),
        22 to RewardData(tickets = 10, experience = 2480.0),
        23 to RewardData(tickets = 25, experience = 6500.0),
        24 to RewardData(tickets = 100, experience = 28000.0),
        25 to RewardData(tickets = 1000, experience = 320000.0),
        8 to RewardData(tickets = 3, reward = Item(CLEAN_TOADFLAX)),
        9 to RewardData(tickets = 10, reward = Item(SNAPDRAGON)),
        10 to RewardData(tickets = 800, reward = Item(PIRATE_HOOK))
    )

    override fun defineInterfaceListeners() {
        on(Components.AGILITYARENA_TRADE_6) { player, _, _, buttonID, _, _ ->
            val data = rewardsMap[buttonID] ?: return@on true

            if (!inInventory(player, ARENA_TICKET, data.tickets)) {
                val costName = data.displayName ?: "Agility experience"
                sendMessage(player, "$costName costs ${data.tickets} tickets.")
                return@on true
            }

            val removed = removeItem(player, Item(ARENA_TICKET, data.tickets))
            if (!removed) {
                sendMessage(player, "Failed to remove tickets from your inventory.")
                return@on true
            }

            if (data.experience > 0.0) {
                var exp = data.experience
                if (player.achievementDiaryManager.karamjaGlove >= 1) {
                    exp *= 1.1
                }
                rewardXP(player, Skills.AGILITY, exp)
                sendMessage(player, "You have been granted some Agility experience!")
            } else if (data.reward != null) {
                addItem(player, data.reward.id)
                sendMessage(player, "You have been granted a ${data.displayName}.")
            }

            true
        }
    }

    companion object {
        private const val ARENA_TICKET = Items.AGILITY_ARENA_TICKET_2996
        private const val PIRATE_HOOK = Items.PIRATES_HOOK_2997
        private const val CLEAN_TOADFLAX = Items.CLEAN_TOADFLAX_2998
        private const val SNAPDRAGON = Items.CLEAN_SNAPDRAGON_3000
    }
}
package content.region.karamja.handlers.brimhaven

import core.api.inInventory
import core.api.sendMessage
import core.game.interaction.InterfaceListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.StringUtils
import org.rs.consts.Components

class AgilityTicketInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.AGILITYARENA_TRADE_6) { player, _, _, buttonID, _, _ ->
            var reward: Item? = null
            var experience = 0.0
            var tickets = 0

            when (buttonID) {
                21 -> {
                    experience = 240.0
                    tickets = 1
                }

                22 -> {
                    experience = 2480.0
                    tickets = 10
                }

                23 -> {
                    experience = 6500.0
                    tickets = 25
                }

                24 -> {
                    experience = 28000.0
                    tickets = 100
                }

                25 -> {
                    experience = 320000.0
                    tickets = 1000
                }

                8 -> {
                    reward = TOADFLAX
                    tickets = 3
                }

                9 -> {
                    reward = SNAPDRAGON
                    tickets = 10
                }

                10 -> {
                    reward = PIRATE_HOOK
                    tickets = 800
                }
            }
            if (experience > 0.0 && !inInventory(player, ARENA_TICKET, tickets)) {
                sendMessage(player, "This Agility experience costs $tickets tickets.")
            }
            if (reward != null && !inInventory(player, ARENA_TICKET, tickets)) {
                sendMessage(
                    player,
                    "${StringUtils.formatDisplayName(reward.name.replace("Clean", "").trim())} costs $tickets tickets.",
                )
            }
            if (!inInventory(player, ARENA_TICKET, tickets)) return@on true

            if (experience > 0.0) {
                if (!inInventory(player, ARENA_TICKET, tickets)) return@on true

                if (player.inventory.remove(Item(ARENA_TICKET, tickets))) {
                    if (player.achievementDiaryManager.karamjaGlove >= 1) {
                        experience *= 1.1
                    }
                    player.skills.addExperience(Skills.AGILITY, experience)
                    sendMessage(player, "You have been granted some Agility experience!")
                }
            }

            if (reward != null) {
                if (player.inventory.remove(Item(ARENA_TICKET, tickets))) {
                    player.inventory.add(reward)
                    sendMessage(
                        player,
                        "You have been granted a ${
                            StringUtils.formatDisplayName(
                                reward.name.replace("Clean", "").trim(),
                            )
                        }.",
                    )
                }
            }
            return@on true
        }
    }

    companion object {
        private val PIRATE_HOOK = Item(2997)
        private val TOADFLAX = Item(2998)
        private val SNAPDRAGON = Item(3000)
        private const val ARENA_TICKET = 2996
    }
}

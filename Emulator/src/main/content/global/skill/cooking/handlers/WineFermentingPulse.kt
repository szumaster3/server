package content.global.skill.cooking.handlers

import core.api.inInventory
import core.api.replaceSlot
import core.api.rewardXP
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.tools.RandomFunction
import org.rs.consts.Items

class WineFermentingPulse(
    delay: Int,
    val player: Player,
) : Pulse(delay) {
    private var count = 0

    override fun pulse(): Boolean {
        if (count++ >= 16) {
            val rand = RandomFunction.random(1, 3)
            when (rand) {
                1 -> {
                    if (inInventory(player, Items.UNFERMENTED_WINE_1995, 1)) {
                        replaceSlot(
                            player,
                            Item(Items.JUG_OF_BAD_WINE_1991).index,
                            Item(Items.UNFERMENTED_WINE_1995, 1),
                        )
                    } else if (player.bank.contains(Items.UNFERMENTED_WINE_1995, 1)) {
                        player.bank.replace(Item(Items.JUG_OF_BAD_WINE_1991, 1), player.bank.getSlot(Item(1995, 1)))
                    }
                    return true
                }

                2 -> {
                    if (inInventory(player, Items.UNFERMENTED_WINE_1995, 1)) {
                        replaceSlot(player, Item(Items.JUG_OF_WINE_1993).index, Item(Items.UNFERMENTED_WINE_1995, 1))
                        rewardXP(player, Skills.COOKING, 200.0)
                    } else if (player.bank.contains(Items.UNFERMENTED_WINE_1995, 1)) {
                        player.bank.replace(Item(Items.JUG_OF_WINE_1993, 1), player.bank.getSlot(Item(1995, 1)))
                        rewardXP(player, Skills.COOKING, 200.0)
                    }
                    return true
                }

                3 -> {
                    if (inInventory(player, Items.UNFERMENTED_WINE_1995, 1)) {
                        replaceSlot(player, Item(Items.JUG_OF_WINE_1993).index, Item(Items.UNFERMENTED_WINE_1995, 1))
                        rewardXP(player, Skills.COOKING, 200.0)
                    } else if (player.bank.contains(Items.UNFERMENTED_WINE_1995, 1)) {
                        player.bank.replace(Item(Items.JUG_OF_WINE_1993, 1), player.bank.getSlot(Item(1995, 1)))
                        rewardXP(player, Skills.COOKING, 200.0)
                    }
                    return true
                }
            }
            return true
        }
        count++
        return false
    }
}

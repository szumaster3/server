package content.global.skill.cooking

import core.api.replaceSlot
import core.api.rewardXP
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.tools.RandomUtils
import shared.consts.Items
import core.api.*
import kotlin.math.min

/**
 * Handles the wine fermenting.
 */
class WineFermentingPulse(delay: Int, private val player: Player) : Pulse(delay) {

    private var count = 0
    private val batchSize = 15

    override fun pulse(): Boolean {
        if (count++ < 16) return false

        val cookingLevel = player.getSkills().getStaticLevel(Skills.COOKING)
        // 35 -> 60%, 68 -> 100%
        val successRate = ((cookingLevel - 35).coerceAtLeast(0) / 33.0).coerceIn(0.0, 1.0)

        val inventoryAmount = amountInInventory(player, Items.UNFERMENTED_WINE_1995)
        val bankAmount = player.bank.getAmount(Items.UNFERMENTED_WINE_1995)
        val totalAmount = inventoryAmount + bankAmount

        if (totalAmount <= 0) {
            sendMessage(player, "You have no unfermented wine to process.")
            return true
        }

        val batchCount = min(batchSize, totalAmount)
        var processed = 0

        repeat(batchCount) {
            val isSuccess = RandomUtils.randomDouble() <= successRate
            val resultId = if (isSuccess) Items.JUG_OF_WINE_1993 else Items.JUG_OF_BAD_WINE_1991

            val slot = player.inventory.getSlot(Item(Items.UNFERMENTED_WINE_1995))
            if (slot != -1) {
                replaceSlot(player, slot, Item(resultId))
            } else {
                val bankSlot = player.bank.getSlot(Item(Items.UNFERMENTED_WINE_1995))
                if (bankSlot != -1) {
                    player.bank.replace(Item(resultId), bankSlot)
                }
            }

            if (isSuccess) rewardXP(player, Skills.COOKING, 200.0)
            processed++
        }

        return processed >= totalAmount
    }
}

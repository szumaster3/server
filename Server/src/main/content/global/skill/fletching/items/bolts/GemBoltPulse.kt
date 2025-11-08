package content.global.skill.fletching.items.bolts

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import kotlin.math.min

/**
 * Represents the attaching of a gem bolt to a premade bolt.
 *
 * @author Ceikry
 */
class GemBoltPulse(player: Player?, node: Item?, private val bolt: GemBolt, sets: Int) : SkillPulse<Item?>(player, node) {
    /**
     * Represents the sets to make.
     */
    private var sets = 0

    init {
        this.sets = sets
    }

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.FLETCHING) < bolt.level) {
            sendDialogue(player, "You need a Fletching level of " + bolt.level + " or above to do that.")
            return false
        }
        if (!inInventory(player, bolt.base) || !inInventory(player, bolt.tip)) {
            return false
        }
        if (!hasSpaceFor(player, Item(bolt.product))) {
            sendDialogue(player, "You do not have enough inventory space.")
            return false
        }
        return true
    }

    override fun animate() {
    }

    override fun reward(): Boolean {
        if (!clockReady(player, Clocks.SKILLING)) return false
        delayClock(player, Clocks.SKILLING, 2)

        val baseAmount = player.inventory.getAmount(bolt.base)
        val tipAmount = player.inventory.getAmount(bolt.tip)
        if (baseAmount <= 0 || tipAmount <= 0) {
            val materials = if (baseAmount <= 0) bolt.base else bolt.tip
            player.packetDispatch.sendMessage("You do not have any more $materials to fletch.")
            return true
        }
        val base = Item(bolt.base)
        val tip = Item(bolt.tip)
        val product = Item(bolt.product)
        if (baseAmount >= 10 && tipAmount >= 10) {
            base.amount = 10
            tip.amount = 10
            product.amount = 10
        } else {
            val amount = min(baseAmount.toDouble(), tipAmount.toDouble()).toInt()
            base.amount = amount
            tip.amount = amount
            product.amount = amount
        }
        if (player.inventory.remove(base, tip)) {
            player.inventory.add(product)
            player.getSkills().addExperience(Skills.FLETCHING, bolt.experience * product.amount, true)
            player.packetDispatch.sendMessage(if (product.amount == 1) "You attach the tip to the bolt." else "You fletch " + product.amount + " bolts.")
        }
        sets--
        return sets <= 0
    }
}

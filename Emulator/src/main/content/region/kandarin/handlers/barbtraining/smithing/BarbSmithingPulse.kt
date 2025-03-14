package content.region.kandarin.handlers.barbtraining.smithing

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import kotlin.math.min

class BarbSmithingPulse(
    player: Player?,
    val weapon: BarbarianWeapon,
    var amount: Int,
    var button: Int,
) : SkillPulse<Item>(player, null) {
    val hasta = weapon.hastaId
    val spear = weapon.spearId

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.SMITHING) < weapon.requiredLevel) {
            sendMessage(player, "You need a Smithing level of ${weapon.requiredLevel} to make this.")
            return false
        }

        if (!inInventory(player, Items.HAMMER_2347)) {
            sendDialogue(player, "You need a hammer to work the metal with.")
            return false
        }

        if (!inInventory(player, weapon.requiredBar)) {
            sendDialogue(player, "You don't have the necessary material for the weapon.")
            return false
        }

        if (!inInventory(player, weapon.requiredWood)) {
            sendDialogue(player, "You don't have the necessary logs for the weapon.")
            return false
        }

        return true
    }

    override fun start() {
        super.start()
        val bar = Item(weapon.requiredBar)
        val wood = Item(weapon.requiredWood)
        val barsAmount = player.inventory.getAmount(bar)
        val woodAmount: Int = player.inventory.getAmount(wood)
        amount = min(min(barsAmount, woodAmount), amount)
    }

    override fun animate() {
        animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
    }

    override fun reward(): Boolean {
        if (delay == 1) {
            delay = 4
            return false
        }

        var index = button
        if (player.inventory.remove(Item(weapon.requiredBar, 1)) &&
            player.inventory.remove(
                Item(
                    weapon.requiredWood,
                    1,
                ),
            )
        ) {
            sendMessage(player, "You make a ${getItemName(if (index == 0) spear else hasta)}.")
            player.inventory.add(Item(if (index == 0) spear else hasta, 1))
            rewardXP(player, Skills.SMITHING, weapon.experience)
            amount--
            return amount == 0
        }
        return true
    }

    override fun message(type: Int) {}
}

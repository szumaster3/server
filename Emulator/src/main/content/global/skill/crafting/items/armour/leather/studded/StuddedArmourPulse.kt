package content.global.skill.crafting.items.armour.leather.studded

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items

class StuddedArmourPulse(
    player: Player?,
    node: Item?,
    val armour: StuddedArmour,
    private var amount: Int,
) : SkillPulse<Item?>(player, node) {
    private var ticks = 0

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < armour.level) {
            sendMessage(player, "You need a crafting level of at least " + armour.level + " to do this.")
            return false
        }
        if (!inInventory(player, Items.STEEL_STUDS_2370)) {
            sendMessage(player, "You need studs in order to make studded armour.")
            return false
        }
        return inInventory(player, armour.leather)
    }

    override fun animate() {
        if (ticks % 5 == 0) {
            animate(player, Animations.CRAFT_LEATHER_1249)
        }
    }

    override fun reward(): Boolean {
        if (++ticks % 5 != 0) {
            return false
        }
        sendMessage(player, "You use the studs with the " + getItemName(node!!.id).lowercase() + ".")
        if (removeItem(player, armour.leather) && removeItem(player, Item(Items.STEEL_STUDS_2370, 1))) {
            addItem(player, armour.product, 1)
            rewardXP(player, Skills.CRAFTING, armour.experience)
            sendMessage(player, "You make a " + getItemName(armour.product).lowercase() + ".")
        }
        amount--
        return amount < 1
    }
}

package content.global.skill.crafting.items.armour.snelms

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item

class SnelmCraftingPulse(
    player: Player?,
    node: Item?,
    val itemId: Snelm,
) : SkillPulse<Item?>(player, node) {
    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < 15) {
            sendDialogue(player, "You need a crafting level of at least 15 in order to do this.")
            return false
        }
        return true
    }

    override fun animate() {}

    override fun reward(): Boolean {
        sendMessage(player, "You craft the shell into a helmet.")
        replaceSlot(player, node!!.slot, Item(itemId.product))
        rewardXP(player, Skills.CRAFTING, 32.5)
        return true
    }
}

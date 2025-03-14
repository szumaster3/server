package content.global.skill.crafting.casting

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Sounds

class GemCuttingPulse(
    player: Player?,
    item: Item?,
    var amount: Int,
    val gem: Gem,
) : SkillPulse<Item?>(player, item) {
    val ticks = 0

    init {
        resetAnimation = false
    }

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < gem.level) {
            sendMessage(player, "You need a crafting level of " + gem.level + " to craft this gem.")
            return false
        }
        if (!inInventory(player, CHISEL)) {
            return false
        }
        if (!inInventory(player, gem.uncut.id)) {
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0 || ticks < 1) {
            playAudio(player, Sounds.CHISEL_2586)
            animate(player, gem.animation)
        }
    }

    override fun reward(): Boolean {
        if (player.inventory.remove(gem.uncut)) {
            val item = gem.gem
            player.inventory.add(item)
            rewardXP(player, Skills.CRAFTING, gem.exp)
        }
        amount--
        return amount < 1
    }

    companion object {
        private const val CHISEL = Items.CHISEL_1755
    }
}

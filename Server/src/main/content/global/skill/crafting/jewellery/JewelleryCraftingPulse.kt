package content.global.skill.crafting.jewellery

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Sounds

class JewelleryCraftingPulse(player: Player?, node: Item?, val type: Jewellery.JewelleryItem, var amount: Int) : SkillPulse<Item?>(player, node) {
    companion object {
        private const val ANIMATION = Animations.HUMAN_FURNACE_SMELT_3243
    }

    var ticks = 0

    override fun checkRequirements(): Boolean = getStatLevel(player, Skills.CRAFTING) >= type.level

    override fun animate() {
        if (ticks % 3 == 0) {
            animate(player, ANIMATION)
            playAudio(player, Sounds.FURNACE_2725)
        }
    }

    override fun reward(): Boolean {
        if (!clockReady(player, Clocks.SKILLING)) return false
        if (player.inventory.remove(*items)) {
            val item = Item(type.sendItem)
            player.inventory.add(item)
            rewardXP(player, Skills.CRAFTING, type.experience)
        }
        amount--
        return amount < 1
    }

    private val items: Array<Item?>
        get() {
            val items = arrayOfNulls<Item>(type.items.size)
            for ((index, i) in type.items.indices.withIndex()) {
                items[index] = Item(type.items[i], 1)
            }
            return items
        }
}

package content.global.skill.herblore

import content.global.skill.herblore.potions.GenericPotion
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Sounds

class HerblorePulse(player: Player?, node: Item?, private var amount: Int, private val potion: GenericPotion) : SkillPulse<Item?>(player, node) {

    private val initialAmount = amount
    private var cycles = 0

    override fun checkRequirements(): Boolean {
        if (!isQuestComplete(player, Quests.DRUIDIC_RITUAL)) {
            sendMessage(player, "You must complete the ${Quests.DRUIDIC_RITUAL} quest before you can use Herblore.")
            return false
        }

        if (getDynLevel(player, Skills.HERBLORE) < potion.level) {
            sendMessage(player, "You need a Herblore level of at least ${potion.level} in order to do this.")
            return false
        }

        val watchtowerPotions = intArrayOf(Items.VIAL_2389, Items.VIAL_2390, Items.POTION_2394)
        potion.product?.let {
            if (watchtowerPotions.contains(it) && !isQuestComplete(player, Quests.WATCHTOWER)) {
                sendMessage(player, "Hmmm...perhaps I shouldn't try to mix these items together.")
                sendMessage(player, "It might have unpredictable results...")
                return false
            }
        }

        return inInventory(player, potion.base ?: return false) && inInventory(player, potion.ingredient ?: return false)
    }

    override fun animate() {}

    override fun reward(): Boolean {
        val isUnfinished = potion.base == VIAL_OF_WATER

        if (initialAmount == 1 && delay == 1) {
            animate(player, ANIMATION)
            delay = 3
            return false
        }

        if (!isUnfinished && delay == 1) {
            animate(player, ANIMATION)
            delay = 3
            return false
        }

        if (isUnfinished) handleUnfinished() else handleFinished()
        return --amount == 0
    }

    private fun handleUnfinished() {
        val base = potion.base ?: return
        val ingredient = potion.ingredient ?: return
        val product = potion.product ?: return

        if (cycles == 0) animate(player, ANIMATION)

        if (inInventory(player, base) && inInventory(player, ingredient) && player.inventory.remove(base.asItem(), ingredient.asItem())) {
            addItem(player, product)
            val herb = getItemName(ingredient).lowercase().replace("clean", "").trim()
            sendMessage(player, "You put the $herb into the vial of water.")
            playAudio(player, Sounds.GRIND_2608)

            if (++cycles == 4) {
                animate(player, ANIMATION)
                cycles = 0
            }
        }
    }

    private fun handleFinished() {
        val base = potion.base ?: return
        val ingredient = potion.ingredient ?: return
        val product = potion.product ?: return

        if (inInventory(player, base) && inInventory(player, ingredient) && player.inventory.remove(base.asItem(), ingredient.asItem())) {
            addItem(player, product)
            rewardXP(player, Skills.HERBLORE, potion.experience)
                val item =  getItemName(ingredient).lowercase()
            sendMessage(player, "You mix the $item into your potion.")

            playAudio(player, Sounds.GRIND_2608)
            animate(player, ANIMATION)
        }
    }

    companion object {
        const val VIAL_OF_WATER = Items.VIAL_OF_WATER_227
        const val COCONUT_MILK = Items.COCONUT_MILK_5935
        private const val ANIMATION = Animations.MIX_POTION_363
    }
}
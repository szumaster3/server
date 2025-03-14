package content.global.skill.herblore

import content.global.skill.herblore.potions.GenericPotion
import core.api.*
import core.api.quest.isQuestComplete
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Sounds

class HerblorePulse(
    player: Player?,
    node: Item?,
    var amount: Int,
    private val potion: GenericPotion,
) : SkillPulse<Item?>(player, node) {
    private val initialAmount: Int = amount
    private var cycles = 0

    override fun checkRequirements(): Boolean {
        if (!isQuestComplete(player, Quests.DRUIDIC_RITUAL)) {
            sendMessage(player, "You must complete the ${Quests.DRUIDIC_RITUAL} quest before you can use Herblore.")
            return false
        }
        if (getDynLevel(player, Skills.HERBLORE) < potion.level) {
            sendMessage(player, "You need a Herblore level of at least " + potion.level + " in order to do this.")
            return false
        }
        return inInventory(player, potion.base!!.id) && inInventory(player, potion.ingredient!!.id)
    }

    override fun animate() {}

    override fun reward(): Boolean {
        if (potion.base!!.id == VIAL_OF_WATER.id) {
            if (initialAmount == 1 && delay == 1) {
                animate(player, ANIMATION)
                delay = 3
                return false
            }
            handleUnfinished()
        } else {
            if (initialAmount == 1 && delay == 1) {
                animate(player, ANIMATION)
                delay = 3
                return false
            }
            if (delay == 1) {
                delay = 3
                animate(player, ANIMATION)
                return false
            }
            handleFinished()
        }
        amount--
        return amount == 0
    }

    fun handleUnfinished() {
        if (cycles == 0) {
            animate(player, ANIMATION)
        }
        if (inInventory(player, potion.base!!.id) &&
            inInventory(
                player,
                potion.ingredient!!.id,
            ) &&
            player.inventory.remove(potion.base, potion.ingredient)
        ) {
            val item = potion.product
            addItem(player, item!!.id)
            sendMessage(
                player,
                "You put the" +
                    getItemName(potion.ingredient.id)
                        .lowercase()
                        .replace("clean", "") + " leaf into the vial of water.",
            )
            playAudio(player, Sounds.GRIND_2608)
            if (cycles++ == 3) {
                animate(player, ANIMATION)
                cycles = 0
            }
        }
    }

    fun handleFinished() {
        if (inInventory(player, potion.base!!.id) &&
            inInventory(
                player,
                potion.ingredient!!.id,
            ) &&
            player.inventory.remove(potion.base, potion.ingredient)
        ) {
            var item = potion.product
            addItem(player, item!!.id)
            rewardXP(player, Skills.HERBLORE, potion.experience)
            sendMessage(player, "You mix the " + potion.ingredient.name.lowercase() + " into your potion.")
            playAudio(player, Sounds.GRIND_2608)
            animate(player, ANIMATION)
        }
    }

    companion object {
        @JvmField val VIAL_OF_WATER = Item(Items.VIAL_OF_WATER_227)

        @JvmField val COCONUT_MILK = Item(Items.COCONUT_MILK_5935)
        private const val ANIMATION = Animations.MIX_POTION_363
    }
}

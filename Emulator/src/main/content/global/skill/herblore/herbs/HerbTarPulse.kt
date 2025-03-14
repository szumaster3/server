package content.global.skill.herblore.herbs

import core.api.*
import core.api.quest.isQuestComplete
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests

class HerbTarPulse(
    player: Player?,
    node: Item?,
    val tar: Tars,
    private var amount: Int,
) : SkillPulse<Item?>(player, node) {
    override fun checkRequirements(): Boolean {
        if (!isQuestComplete(player, Quests.DRUIDIC_RITUAL)) {
            sendMessage(player, "You must complete the ${Quests.DRUIDIC_RITUAL} quest before you can use Herblore.")
            return false
        }
        if (getDynLevel(player, Skills.HERBLORE) < tar.level) {
            sendMessage(player, "You need a Herblore level of at least " + tar.level + " in order to do this.")
            return false
        }
        if (!inInventory(player, PESTLE_AND_MORTAR)) {
            sendMessage(player, "You need Pestle and Mortar in order to crush the herb.")
            return false
        }
        if (!inInventory(player, Items.SWAMP_TAR_1939, 15)) {
            sendMessage(player, "You need at least 15 swamp tar in order to do this.")
            return false
        }
        return true
    }

    override fun animate() {
        animate(player, ANIMATION)
    }

    override fun reward(): Boolean {
        if (delay == 1) {
            delay = 4
            return false
        }
        if (inInventory(player, Items.SWAMP_TAR_1939, 15) &&
            inInventory(player, tar.ingredient) &&
            removeItem(
                player,
                Item(Items.SWAMP_TAR_1939, 15),
            ) &&
            removeItem(player, tar.ingredient)
        ) {
            addItem(player, tar.product, 15)
            rewardXP(player, Skills.HERBLORE, tar.experience)
            sendMessage(
                player,
                "You add the " +
                    getItemName(tar.ingredient)
                        .lowercase()
                        .replace("clean", "")
                        .trim { it <= ' ' } + " to the swamp tar.",
            )
        } else {
            return true
        }
        amount--
        return amount == 0
    }

    override fun message(type: Int) {}

    companion object {
        private const val ANIMATION = Animations.PESTLE_MORTAR_364
        private const val PESTLE_AND_MORTAR = Items.PESTLE_AND_MORTAR_233
    }
}

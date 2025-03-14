package content.global.skill.crafting.items.armour.leather.soft

import content.global.skill.crafting.items.armour.leather.Thread
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.StringUtils
import org.rs.consts.Animations
import org.rs.consts.Items

class SoftLeatherCraftingPulse(
    player: Player?,
    node: Item?,
    val soft: SoftLeather,
    var amount: Int,
) : SkillPulse<Item?>(player, node) {
    private var ticks = 0

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < soft.level) {
            sendDialogue(
                player,
                "You need a crafting level of " + soft.level + " to make " +
                    (if (StringUtils.isPlusN(soft.product.name)) "an" else "a" + " " + soft.product.name).lowercase() +
                    ".",
            )
            return false
        }
        if (!inInventory(player, Items.NEEDLE_1733, 1)) {
            return false
        }
        if (!inInventory(player, Items.LEATHER_1741, 1)) {
            return false
        }
        if (!inInventory(player, Items.THREAD_1734, 1)) {
            sendDialogue(player, "You need thread to make this.")
            amount = 0
            return false
        }
        closeInterface(player)
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0) {
            animate(player, ANIMATION)
        }
    }

    override fun reward(): Boolean {
        if (++ticks % 5 != 0) {
            return false
        }
        if (removeItem(player, Item(Items.LEATHER_1741))) {
            if (soft == SoftLeather.GLOVES || soft == SoftLeather.BOOTS || soft == SoftLeather.VAMBRACES) {
                sendMessage(player, "You make a pair of " + soft.product.name.lowercase() + ".")
            } else {
                sendMessage(
                    player,
                    "You make " + (if (StringUtils.isPlusN(soft.product.name)) "an " else "a ") +
                        soft.product.name.lowercase() +
                        ".",
                )
            }
            val item = soft.product.id
            addItem(player, item)
            rewardXP(player, Skills.CRAFTING, soft.experience)
            Thread.decayThread(player)
            if (Thread.isLastThread(player)) {
                Thread.removeThread(player)
            }
            if (soft == SoftLeather.GLOVES) {
                finishDiaryTask(player, DiaryType.LUMBRIDGE, 1, 3)
            }
        }
        amount--
        return amount < 1
    }

    companion object {
        private const val ANIMATION = Animations.CRAFT_LEATHER_1249
    }
}

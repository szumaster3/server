package content.region.fremennik.handlers.neitiznot

import content.data.items.SkillingTool
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items

class LogCuttingPulse(
    player: Player?,
    node: Item?,
    var amount: Int,
) : SkillPulse<Item?>(player, null) {
    private val arcticPineLog = Items.ARCTIC_PINE_LOGS_10810
    private val splitLog: Item = Item(Items.SPLIT_LOG_10812)
    private val splittingAnimation = Animation(Animations.HUMAN_SPLIT_LOGS_5755)

    var ticks = 0

    override fun checkRequirements(): Boolean {
        val tool = SkillingTool.getToolForSkill(player, Skills.WOODCUTTING)
        if (tool == null) {
            sendMessage(player, "You do not have a pickaxe to use.")
            return false
        }
        if (amountInInventory(player, Items.ARCTIC_PINE_LOGS_10810) < 1) {
            sendMessage(player, "You have run out of an Arctic pine log.")
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0) {
            animate(player, splittingAnimation)
        }
    }

    override fun reward(): Boolean {
        if (++ticks % 5 != 0) {
            return false
        }
        amount = arcticPineLog
        if (removeItem(player, arcticPineLog)) {
            addItem(player, splitLog.id, 1)
            rewardXP(player, Skills.WOODCUTTING, 42.5)
            sendMessage(player, "You make a split log of Arctic pine.")
        }
        amount--
        return amount < 1
    }
}

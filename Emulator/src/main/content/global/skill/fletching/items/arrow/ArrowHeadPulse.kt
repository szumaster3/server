package content.global.skill.fletching.items.arrow

import content.global.skill.slayer.SlayerManager.Companion.getInstance
import core.api.hasSpaceFor
import core.api.sendDialogue
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item

class ArrowHeadPulse(
    player: Player?,
    node: Item?,
    private val arrow: ArrowHead,
    private var sets: Int,
) : SkillPulse<Item?>(player, node) {
    override fun checkRequirements(): Boolean {
        if (arrow.unfinished == 4160) {
            if (!getInstance(player).flags.isBroadsUnlocked()) {
                player.dialogueInterpreter.sendDialogue("You need to unlock the ability to create broad arrows.")
                return false
            }
        }
        if (player.getSkills().getLevel(Skills.FLETCHING) < arrow.level) {
            player.dialogueInterpreter.sendDialogue("You need a fletching level of " + arrow.level + " to do this.")
            return false
        }
        if (!hasSpaceFor(player, Item(arrow.finished))) {
            sendDialogue(player, "You do not have enough inventory space.")
            return false
        }
        return true
    }

    override fun animate() {
    }

    override fun reward(): Boolean {
        if (delay == 1) {
            super.setDelay(3)
        }
        val tip = Item(arrow.unfinished)
        val tipAmount = player.inventory.getAmount(arrow.unfinished)
        val shaftAmount = player.inventory.getAmount(HEADLESS_ARROW)
        if (tipAmount >= 15 && shaftAmount >= 15) {
            HEADLESS_ARROW.amount = 15
            tip.amount = 15
            player.packetDispatch.sendMessage("You attach arrow heads to 15 arrow shafts.")
        } else {
            val amount = if (tipAmount > shaftAmount) shaftAmount else tipAmount
            HEADLESS_ARROW.amount = amount
            tip.amount = amount
            player.packetDispatch.sendMessage(
                if (amount ==
                    1
                ) {
                    "You attach an arrow head to an arrow shaft."
                } else {
                    "You attach arrow heads to $amount arrow shafts."
                },
            )
        }
        if (player.inventory.remove(HEADLESS_ARROW, tip)) {
            player.getSkills().addExperience(Skills.FLETCHING, arrow.experience * tip.amount, true)
            val product = Item(arrow.finished)
            product.amount = tip.amount
            player.inventory.add(product)
        }
        HEADLESS_ARROW.amount = 1
        tip.amount = 1
        if (!player.inventory.containsItem(HEADLESS_ARROW)) {
            return true
        }
        if (!player.inventory.containsItem(tip)) {
            return true
        }
        sets--
        return sets == 0
    }

    override fun message(type: Int) {
    }

    companion object {
        private val HEADLESS_ARROW = Item(53)
    }
}

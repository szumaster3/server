package content.global.skill.fletching.items.arrow

import core.api.hasSpaceFor
import core.api.sendDialogue
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

class HeadlessArrowPulse(
    player: Player?,
    node: Item?,
    private val feather: Item?,
    private var sets: Int,
) : SkillPulse<Item?>(player, node) {
    private val HEADLESS_ARROW = Item(Items.HEADLESS_ARROW_53)
    private val ARROW_SHAFT = Item(Items.ARROW_SHAFT_52)
    private var useSets = false

    override fun checkRequirements(): Boolean {
        if (!player.inventory.containsItem(ARROW_SHAFT)) {
            player.dialogueInterpreter.sendDialogue("You don't have any arrow shafts.")
            return false
        }
        if (feather == null || !player.inventory.containsItem(feather)) {
            player.dialogueInterpreter.sendDialogue("You don't have any feathers.")
            return false
        }
        useSets =
            if (player.inventory.contains(ARROW_SHAFT.id, 15) && player.inventory.contains(feather.id, 15)) {
                true
            } else {
                false
            }
        if (!hasSpaceFor(player, HEADLESS_ARROW.asItem())) {
            sendDialogue(player, "You do not have enough inventory space.")
            return false
        }
        return true
    }

    override fun animate() {
    }

    override fun reward(): Boolean {
        val featherAmount = player.inventory.getAmount(feather)
        val shaftAmount = player.inventory.getAmount(ARROW_SHAFT)
        if (delay == 1) {
            super.setDelay(3)
        }
        if (featherAmount >= 15 && shaftAmount >= 15) {
            feather!!.amount = 15
            ARROW_SHAFT.amount = 15
            player.packetDispatch.sendMessage("You attach feathers to 15 arrow shafts.")
        } else {
            val amount = min(featherAmount.toDouble(), shaftAmount.toDouble()).toInt()
            feather!!.amount = amount
            ARROW_SHAFT.amount = amount
            player.packetDispatch.sendMessage(
                if (amount == 1) {
                    "You attach a feathers to a shaft."
                } else {
                    "You attach feathers to $amount arrow shafts."
                },
            )
        }
        if (player.inventory.remove(feather, ARROW_SHAFT)) {
            HEADLESS_ARROW.amount = feather.amount
            player.getSkills().addExperience(Skills.FLETCHING, HEADLESS_ARROW.amount.toDouble(), true)
            player.inventory.add(HEADLESS_ARROW)
        }
        HEADLESS_ARROW.amount = 1
        feather.amount = 1
        ARROW_SHAFT.amount = 1
        if (!player.inventory.containsItem(ARROW_SHAFT)) {
            return true
        }
        if (!player.inventory.containsItem(feather)) {
            return true
        }
        sets--
        return sets <= 0
    }

    override fun message(type: Int) {
    }

    private fun getFeather(): Item? {
        val length = FEATHER.size
        for (i in 0 until length) {
            val f = FEATHER[i]
            if (player.inventory.containsItem(f)) {
                return f
            }
        }
        return null
    }

    companion object {
        private val FEATHER =
            arrayOf<Item>(
                Item(Items.FEATHER_314),
                Item(Items.STRIPY_FEATHER_10087),
                Item(Items.RED_FEATHER_10088),
                Item(Items.BLUE_FEATHER_10089),
                Item(Items.YELLOW_FEATHER_10090),
                Item(Items.ORANGE_FEATHER_10091),
            )
    }
}

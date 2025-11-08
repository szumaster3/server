package content.global.skill.fletching.items.darts

import content.global.skill.fletching.Fletching
import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Quests

/**
 * Handles fletching darts by attaching feathers
 * to dart tips.
 */
class DartPulse(player: Player?, node: Item?, private val dart: Dart, private var sets: Int): SkillPulse<Item?>(player, node) {

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.FLETCHING) < dart.level) {
            sendDialogue(player, "You need a fletching level of ${dart.level} to do this.")
            return false
        }
        if (!isQuestComplete(player, Quests.THE_TOURIST_TRAP)) {
            sendDialogue(player, "You need to have completed Tourist Trap to fletch darts.")
            return false
        }
        if (!hasSpaceFor(player, Item(dart.finished))) {
            sendDialogue(player, "You do not have enough inventory space.")
            return false
        }
        if (getFeatherAmount(player) <= 0) {
            sendDialogue(player, "You need feathers to fletch darts.")
            return false
        }
        return true
    }

    override fun animate() {}

    override fun reward(): Boolean {
        if (!clockReady(player, Clocks.SKILLING)) return false
        delayClock(player, Clocks.SKILLING, 1)

        val unfinished = Item(dart.unfinished)
        val dartAmount = player.inventory.getAmount(unfinished)
        val featherAmount = getFeatherAmount(player)

        val amount = minOf(10, dartAmount, featherAmount)
        if (amount <= 0) return true
        unfinished.amount = amount

        player.packetDispatch.sendMessage(
            when (amount) {
                1 -> "You attach a feather to a dart."
                10 -> "You attach feathers to 10 darts."
                else -> "You attach feathers to $amount darts."
            }
        )

        var toRemove = amount
        for (id in getFeatherPriorityOrder()) {
            if (toRemove <= 0) break
            val have = player.inventory.getAmount(Item(id))
            if (have > 0) {
                val removeCount = minOf(have, toRemove)
                player.inventory.remove(Item(id, removeCount))
                toRemove -= removeCount
            }
        }

        player.inventory.remove(unfinished)

        val product = Item(dart.finished, amount)
        player.skills.addExperience(Skills.FLETCHING, dart.experience * amount, true)
        player.inventory.add(product)

        if (getFeatherAmount(player) <= 0) return true
        if (!player.inventory.containsItem(Item(dart.unfinished))) return true

        sets--
        return sets == 0
    }

    override fun message(type: Int) {}

    private fun getFeatherAmount(player: Player): Int =
        FEATHER_IDS.sumOf { id -> player.inventory.getAmount(Item(id)) }

    private fun getFeatherPriorityOrder(): List<Int> {
        val normal = Items.FEATHER_314
        val others = FEATHER_IDS.filter { it != normal }
        return listOf(normal) + others
    }

    companion object {
        private val FEATHER_IDS: IntArray = Fletching.featherIds
    }
}
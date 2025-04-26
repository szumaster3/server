package content.global.skill.fletching.items.darts

import core.api.getStatLevel
import core.api.hasSpaceFor
import core.api.quest.isQuestComplete
import core.api.sendDialogue
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Quests

/**
 * The type Dart pulse.
 */
class DartPulse
/**
 * Instantiates a new Dart pulse.
 *
 * @param player the player
 * @param node   the node
 * @param dart   the dart
 * @param sets   the sets
 */(player: Player?, node: Item?, private val dart: Dart, private var sets: Int) : SkillPulse<Item?>(player, node) {
    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.FLETCHING) < dart.level) {
            sendDialogue(player, "You need a fletching level of " + dart.level + " to do this.")
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
        return true
    }

    override fun animate() {
    }

    override fun reward(): Boolean {
        if (delay == 1) {
            super.setDelay(3)
        }
        val unfinished = Item(dart.unfinished)
        val dartAmount = player.inventory.getAmount(unfinished)
        val featherAmount = player.inventory.getAmount(FEATHER)
        if (dartAmount >= 10 && featherAmount >= 10) {
            FEATHER.amount = 10
            unfinished.amount = 10
            player.packetDispatch.sendMessage("You attach feathers to 10 darts.")
        } else {
            val amount = if (featherAmount > dartAmount) dartAmount else featherAmount
            FEATHER.amount = amount
            unfinished.amount = amount
            player.packetDispatch.sendMessage(if (amount == 1) "You attach a feather to a dart." else "You attach feathers to $amount darts.")
        }
        if (player.inventory.remove(FEATHER, unfinished)) {
            val product = Item(dart.finished)
            product.amount = FEATHER.amount
            player.getSkills().addExperience(Skills.FLETCHING, dart.experience * product.amount, true)
            player.inventory.add(product)
        }
        FEATHER.amount = 1
        if (!player.inventory.containsItem(FEATHER)) {
            return true
        }
        if (!player.inventory.containsItem(Item(dart.unfinished))) {
            return true
        }
        sets--
        return sets == 0
    }

    override fun message(type: Int) {
    }

    companion object {
        private val FEATHER = Item(314)
    }
}

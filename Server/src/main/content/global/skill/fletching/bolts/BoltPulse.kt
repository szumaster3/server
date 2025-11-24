package content.global.skill.fletching.bolts

import content.global.skill.slayer.SlayerManager.Companion.getInstance
import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items

/**
 * Handles fletching bolts.
 */
class BoltPulse(player: Player?, node: Item?, private val bolt: Bolt, private val feather: Item, private var sets: Int) : SkillPulse<Item?>(player, node) {

    override fun checkRequirements(): Boolean {
        if (bolt.unfinished == Items.BROAD_BOLTS_UNF_13279) {
            if (!getInstance(player).flags.isBroadsUnlocked()) {
                sendDialogue(player, "You need to unlock the ability to create broad bolts.")
                return false
            }
        }
        if (getStatLevel(player, Skills.FLETCHING) < bolt.level) {
            sendDialogue(player, "You need a fletching level of " + bolt.level + " in order to do this.")
            return false
        }
        if (!inInventory(player, feather.id)) {
            return false
        }
        if (!inInventory(player, bolt.unfinished)) {
            return false
        }
        if (!hasSpaceFor(player, Item(bolt.finished))) {
            sendDialogue(player, "You do not have enough inventory space.")
            return false
        }

        return true
    }

    override fun animate() {
    }

    override fun reward(): Boolean {
        if (!clockReady(player, Clocks.SKILLING)) return false
        delayClock(player, Clocks.SKILLING, 3)

        val featherAmount = player.inventory.getAmount(feather)
        val boltAmount = player.inventory.getAmount(bolt.unfinished)
        val unfinished = Item(bolt.unfinished)
        if (featherAmount >= 10 && boltAmount >= 10) {
            feather.amount = 10
            unfinished.amount = 10
            player.packetDispatch.sendMessage("You fletch 10 bolts.")
        } else {
            val amount = if (featherAmount > boltAmount) boltAmount else featherAmount
            feather.amount = amount
            unfinished.amount = amount
            player.packetDispatch.sendMessage(if (amount == 1) "You attach a feather to a bolt." else "You fletch $amount bolts")
        }
        if (player.inventory.remove(feather, unfinished)) {
            val product = Item(bolt.finished)
            product.amount = feather.amount
            player.getSkills().addExperience(Skills.FLETCHING, product.amount * bolt.experience, true)
            player.inventory.add(product)
        }
        feather.amount = 1
        if (!player.inventory.containsItem(feather)) {
            return true
        }
        if (!player.inventory.containsItem(Item(bolt.unfinished))) {
            return true
        }
        sets--
        return sets <= 0
    }

    override fun message(type: Int) {
    }

}

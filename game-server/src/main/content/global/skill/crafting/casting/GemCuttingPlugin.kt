package content.global.skill.crafting.casting

import core.api.*
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Sounds

class GemCuttingPlugin : InteractionListener {

    private val itemIDs = intArrayOf(Items.UNCUT_DIAMOND_1617, Items.UNCUT_RUBY_1619, Items.UNCUT_EMERALD_1621, Items.UNCUT_SAPPHIRE_1623, Items.UNCUT_OPAL_1625, Items.UNCUT_JADE_1627, Items.UNCUT_RED_TOPAZ_1629, Items.UNCUT_DRAGONSTONE_1631, Items.UNCUT_ONYX_6571)

    override fun defineListeners() {

        /*
         * Handles cutting gems.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, *itemIDs) { player, used, with ->
            val gem = Gem.forId(if (used.id == Items.CHISEL_1755) with.asItem() else used.asItem())
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, gem!!.gem) {
                    override fun create(
                        amount: Int,
                        index: Int,
                    ) {
                        player.pulseManager.run(GemCuttingPulse(player, gem!!.uncut, amount, gem))
                    }

                    override fun getAll(index: Int): Int = amountInInventory(player, gem!!.uncut.id)
                }
            if (amountInInventory(player, gem.uncut.id) == 1) {
                handler.create(0, 1)
            } else {
                handler.open()
            }
            return@onUseWith true
        }
    }

}

/**
 * Handles cutting gem pulse.
 */
private class GemCuttingPulse(player: Player?, item: Item?, var amount: Int, val gem: Gem, ) : SkillPulse<Item?>(player, item) {
    val ticks = 0

    init {
        resetAnimation = false
    }

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < gem.level) {
            sendMessage(player, "You need a crafting level of " + gem.level + " to craft this gem.")
            return false
        }
        if (!inInventory(player, CHISEL)) {
            return false
        }
        if (!inInventory(player, gem.uncut.id)) {
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0 || ticks < 1) {
            playAudio(player, Sounds.CHISEL_2586)
            animate(player, gem.animation)
        }
    }

    override fun reward(): Boolean {
        if (player.inventory.remove(gem.uncut)) {
            val item = gem.gem
            player.inventory.add(item)
            rewardXP(player, Skills.CRAFTING, gem.exp)
        }
        amount--
        return amount < 1
    }

    companion object {
        private const val CHISEL = Items.CHISEL_1755
    }
}
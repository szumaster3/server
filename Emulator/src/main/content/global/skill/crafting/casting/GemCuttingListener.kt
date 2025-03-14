package content.global.skill.crafting.casting

import core.api.amountInInventory
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class GemCuttingListener : InteractionListener {
    override fun defineListeners() {
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

                    override fun getAll(index: Int): Int {
                        return amountInInventory(player, gem!!.uncut.id)
                    }
                }
            if (amountInInventory(player, gem.uncut.id) == 1) {
                handler.create(0, 1)
            } else {
                handler.open()
            }
            return@onUseWith true
        }
    }

    companion object {
        private val itemIDs =
            intArrayOf(
                Items.UNCUT_DIAMOND_1617,
                Items.UNCUT_RUBY_1619,
                Items.UNCUT_EMERALD_1621,
                Items.UNCUT_SAPPHIRE_1623,
                Items.UNCUT_OPAL_1625,
                Items.UNCUT_JADE_1627,
                Items.UNCUT_RED_TOPAZ_1629,
                Items.UNCUT_DRAGONSTONE_1631,
                Items.UNCUT_ONYX_6571,
            )
    }
}

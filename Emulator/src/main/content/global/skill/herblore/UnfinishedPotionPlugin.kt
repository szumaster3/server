package content.global.skill.herblore

import core.api.amountInInventory
import core.api.asItem
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class UnfinishedPotionPlugin : InteractionListener {

    override fun defineListeners() {
        val liquids = intArrayOf(Items.VIAL_OF_WATER_227, Items.COCONUT_MILK_5935)
        val ingredients = UnfinishedPotion.values().map { it.ingredient }.distinct().toIntArray()

        onUseWith(IntType.ITEM, ingredients, *liquids) { player, used, with ->
            val unf = UnfinishedPotion.forID(used.id, with.id) ?: return@onUseWith false
            val potion = GenericPotion.transform(unf)
            val base = potion.base
            val product = potion.product ?: return@onUseWith false

            val amount = amountInInventory(player, base)

            val handler = object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(product)) {
                override fun create(amount: Int, index: Int) {
                    player.pulseManager.run(HerblorePulse(player, base.asItem(), amount, potion))
                }

                override fun getAll(index: Int): Int = amountInInventory(player, base)
            }

            if (amount == 1) {
                handler.create(0, 1)
            } else {
                handler.open()
            }

            return@onUseWith true
        }
    }
}
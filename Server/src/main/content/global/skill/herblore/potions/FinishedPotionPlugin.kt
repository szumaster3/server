package content.global.skill.herblore.potions

import content.global.skill.herblore.HerblorePulse
import core.api.amountInInventory
import core.api.asItem
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item

class FinishedPotionPlugin : InteractionListener {

    override fun defineListeners() {
        val unfinishedItems = UnfinishedPotion.values().map { it.potion }.distinct().toIntArray()
        val ingredients = FinishedPotion.values().map { it.ingredient }.distinct().toIntArray()

        /*
         * Handles creating potions.
         */

        onUseWith(IntType.ITEM, ingredients, *unfinishedItems) { player, used, with ->
            val unfId: Int
            val ingId: Int

            if (used.name.contains("(unf)")) {
                unfId = used.id
                ingId = with.id
            } else {
                unfId = with.id
                ingId = used.id
            }

            val finished = FinishedPotion.getPotion(unfId, ingId) ?: return@onUseWith false
            val potion = GenericPotion.transform(finished)
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
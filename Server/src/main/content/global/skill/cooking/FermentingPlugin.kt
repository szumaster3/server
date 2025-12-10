package content.global.skill.cooking

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.PulseType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery
import shared.consts.Vars

class FermentingPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles using a jug of water with grapes to create unfermented wine.
         */

        onUseWith(IntType.ITEM, Items.JUG_OF_WATER_1937, Items.GRAPES_1987) { player, used, with ->
            val itemSlot = used.asItem().slot
            if (getStatLevel(player, Skills.COOKING) < 35) {
                sendDialogue(player, "You need a Cooking level of 35 to do this.")
                return@onUseWith false
            }
            if (removeItem(player, with.asItem())) {
                replaceSlot(player, itemSlot, Item(Items.UNFERMENTED_WINE_1995, 1))
                submitIndividualPulse(player, WineFermentingPulse(1, player), type = PulseType.STANDARD)
            }
            return@onUseWith true
        }

        /*
         * Not complete.
         */

        onUseWith(IntType.SCENERY, Items.BUCKET_OF_WATER_1929, Scenery.FERMENTING_VAT_7473) { player, used, with ->
            setAttribute(player, addWaterAttribute, baseValue)
            if (removeItem(player, used.asItem())) {
                face(player, with.asScenery())
                animate(player, Animations.POUR_BUCKET_OVER_GROUND_2283)
                replaceSlot(player, used.asItem().slot, Item(Items.BUCKET_1925, 1))
                player.incrementAttribute(addWaterAttribute)
            }
            if (getAttribute(player, addWaterAttribute, baseValue) == 2) {
                setVarbit(player, Vars.VARBIT_SCENERY_BREWING_VAT_736, 1)
            }
            return@onUseWith true
        }
    }

    companion object {
        val baseValue = 0
        val addWaterAttribute = "brewing:add-water"
        val addBarleyMaltAttribute = "brewing:add-barley-malt"
    }
}

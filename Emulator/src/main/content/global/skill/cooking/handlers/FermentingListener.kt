package content.global.skill.cooking.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery
import org.rs.consts.Vars

class FermentingListener : InteractionListener {
    val fermentingVat = 7437
    val valve = 7442
    val location = Location.create(2916, 10193, 1)

    val VARBIT_736 = 736
    val VARBIT_738 = 738

    override fun defineListeners() {
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

        onUseWith(IntType.SCENERY, Items.BARLEY_MALT_6008, Scenery.FERMENTING_VAT_7438) { player, used, with ->
            return@onUseWith true
        }
    }

    companion object {
        val baseValue = 0
        val addWaterAttribute = "brewing:add-water"
        val addBarleyMaltAttribute = "brewing:add-barley-malt"
    }
}

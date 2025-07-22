package content.region.kandarin.camelot.plugin

import core.api.Container
import core.api.addItem
import core.api.removeItem
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Scenery

class AquariumPlugin : InteractionListener {

    /*
     * TODO: Players will get this message if any item except fish food is used on the pet fish:
     *  "Your fish looks at you strangely. You get the feeling this will not work."
     */
    private val petFish = intArrayOf(Items.FISHBOWL_6670, Items.FISHBOWL_6671, Items.FISHBOWL_6672)

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, petFish, Scenery.AQUARIUM_10091) { player, used, _ ->
            if(removeItem(player, used.asItem(), Container.INVENTORY)) {
                addItem(player, Items.FISHBOWL_6667, 1)
            }
            return@onUseWith true
        }
    }
}
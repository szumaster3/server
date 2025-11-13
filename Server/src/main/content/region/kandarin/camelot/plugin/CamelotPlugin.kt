package content.region.kandarin.camelot.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class CamelotPlugin : InteractionListener {
    private val petFish = intArrayOf(Items.FISHBOWL_6670, Items.FISHBOWL_6671, Items.FISHBOWL_6672)
    private val feedAnim = Animation(Animations.FEED_BOWL_2781)

    private val fishFoodInteraction: Map<Int, (Player, Item) -> Unit> = mapOf(
        Items.FISH_FOOD_272 to { player, used ->
            lock(player, feedAnim.duration)
            removeItem(player, used)
            addItem(player, Items.AN_EMPTY_BOX_6675, 1)
            animate(player, feedAnim)
            sendMessage(player, "You feed your fish.")
        },
        Items.POISONED_FISH_FOOD_274 to { player, _ ->
            sendMessage(player, "You can't poison your own pet!")
        }
    )

    override fun defineListeners() {

        /*
         * Handles using a fishbowl on the aquarium to place pet fish inside.
         */

        onUseWith(IntType.SCENERY, petFish, Scenery.AQUARIUM_10091) { player, used, _ ->
            if(removeItem(player, used.asItem(), Container.INVENTORY)) {
                addItem(player, Items.FISHBOWL_6667, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles using any items on pet fish.
         */

        onUseAnyWith(IntType.ITEM, *petFish) { player, used, _ ->
            fishFoodInteraction[used.id]?.invoke(player, used.asItem())
                ?: sendMessage(player, "Your fish looks at you strangely. You get the feeling this will not work.")
            return@onUseAnyWith true
        }
    }
}
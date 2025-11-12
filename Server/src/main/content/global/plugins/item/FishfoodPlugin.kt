package content.global.plugins.item

import core.api.addItem
import core.api.animate
import core.api.lock
import core.api.sendMessage
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Items

/**
 * Handles the logic for combining fish food.
 */
@Initializable
class FishfoodPlugin : UseWithHandler(*FishFood.usables) {
    companion object {
        private const val FISH_FOOD = Items.FISH_FOOD_272
        private const val POISON = Items.POISON_273
        private const val POISONED_FISH_FOOD = Items.POISONED_FISH_FOOD_274
    }

    /**
     * Represents fish food item combinations.
     */
    enum class FishFood(private val used: Int, val with: Int, private val product: Int, private val msg: String) {
        POISONED(POISON, FISH_FOOD, POISONED_FISH_FOOD, "You poison the fish food."),
        GUAMBOX(Items.GROUND_GUAM_6681, Items.AN_EMPTY_BOX_6675, Items.GUAM_IN_A_BOX_6677, "You put the ground Guam into the box."),
        SEAWEEDBOX(Items.GROUND_SEAWEED_6683, Items.AN_EMPTY_BOX_6675, Items.SEAWEED_IN_A_BOX_6679, "You put the ground Seaweed into the box."),
        FOOD1(Items.GROUND_SEAWEED_6683, Items.GUAM_IN_A_BOX_6677, FISH_FOOD, "You put the ground Seaweed into the box and make Fish Food."),
        FOOD2(Items.GROUND_GUAM_6681, Items.SEAWEED_IN_A_BOX_6679, FISH_FOOD, "You put the ground Guam into the box and make Fish Food."),
        FISHBOWL(Items.FISHBOWL_6668, Items.SEAWEED_401, Items.FISHBOWL_6669, "You place the seaweed in the bowl."), ;

        companion object {
            /**
             * List of all item ids that can be used with another item.
             */
            val usables = values().map { it.used }.toIntArray()

            /**
             * Returns the product item for a given combination.
             */
            fun product(used: Int, with: Int): Item? {
                for (value in values()) {
                    if (value.used == used && value.with == with) {
                        return Item(value.product)
                    }
                }
                return null
            }

            /**
             * Returns the message to display for a given combination.
             */
            fun message(used: Int, with: Int): String? {
                for (value in values()) {
                    if (value.used == used && value.with == with) {
                        return value.msg
                    }
                }
                return null
            }
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        for (value in FishFood.values()) {
            addHandler(value.with, ITEM_TYPE, this)
        }
        return this
    }

    /**
     * Handles the item-on-item combination when triggered by the player.
     */
    override fun handle(event: NodeUsageEvent): Boolean {
        val player: Player = event.player
        val used: Int = event.usedItem.id
        val with: Int = event.baseItem.id
        val product: Item? = FishFood.product(used, with)

        if (product != null) {
            player.pulseManager.run(
                object : Pulse(1, player) {
                    override fun pulse(): Boolean {
                        if (player.inventory.remove(Item(with), Item(used))) {
                            animate(player, Animation(Animations.CRAFT_ITEM_1309))
                            sendMessage(player, FishFood.message(used, with)!!)
                            addItem(player, product.id)
                            lock(player, 2)
                        }
                        return true
                    }

                    override fun stop() {
                        super.stop()
                    }
                },
            )
        }
        return true
    }
}

package content.global.skill.firemaking

import content.global.skill.firemaking.items.GnomishFirelighter
import content.region.kandarin.baxtorian.plugin.barbarian_training.BarbarianFiremakingPlugin
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItem
import core.game.node.item.Item
import shared.consts.Items

class FireMakingListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles lighting logs in inventory using a tinderbox.
         */

        onUseWith(IntType.ITEM, Items.TINDERBOX_590, *BarbarianFiremakingPlugin.logs) { player, _, with ->
            player.pulseManager.run(FireMakingPlugin(player, with.asItem(), null))
            return@onUseWith true
        }

        /*
         * Handles lighting logs on the ground using a tinderbox.
         */

        onUseWith(IntType.GROUND_ITEM, Items.TINDERBOX_590, *BarbarianFiremakingPlugin.logs) { player, _, with ->
            player.pulseManager.run(FireMakingPlugin(player, with.asItem(), with as GroundItem))
            return@onUseWith true
        }

        /*
         * Handles combining logs with a gnomish firelighter to coat logs.
         */

        onUseWith(IntType.ITEM, Items.LOGS_1511, *intArrayOf(Items.RED_FIRELIGHTER_7329, Items.GREEN_FIRELIGHTER_7330, Items.BLUE_FIRELIGHTER_7331, Items.PURPLE_FIRELIGHTER_10326, Items.WHITE_FIRELIGHTER_10327)) { player, used, with ->
            val firelighter = GnomishFirelighter.forProduct(with.id) ?: return@onUseWith false

            if (with.id == firelighter.product || used.id == firelighter.base) {
                sendMessage(player, "You can't do that.")
                return@onUseWith true
            }

            if (!removeItem(player, Item(with.id, 1))) {
                sendMessage(player, "You don't have the required items in your inventory.")
                return@onUseWith true
            }

            addItem(player, firelighter.product, 1)
            val chemicalName = getItemName(firelighter.base).replaceFirst("firelighter", "chemicals").lowercase()
            sendMessage(player, "You coat the log with the $chemicalName.")
            return@onUseWith true
        }
    }
}

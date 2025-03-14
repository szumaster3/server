package content.global.skill.cooking

import core.api.addItem
import core.api.playAudio
import core.api.removeItem
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import org.rs.consts.Sounds

class BurnPulse
    internal constructor(
        override var player: Player,
        override var scenery: Scenery,
        override var initial: Int,
        override var product: Int,
        override var amount: Int,
    ) : CookingPulse(player, scenery, initial, product, amount) {
        override fun checkRequirements(): Boolean {
            return scenery.isActive
        }

        override fun reward(): Boolean {
            if (delay == 1) {
                delay = if (scenery.name.equals("range", ignoreCase = true)) 5 else 4
                return false
            }
            if (cook(player, null, false, initial, product)) {
                amount--
            } else {
                return true
            }

            return amount <= 1
        }

        override fun cook(
            player: Player,
            sceneryId: Scenery?,
            burned: Boolean,
            initial: Int,
            product: Int,
        ): Boolean {
            super.animate()
            val initialItem = Item(initial)
            val productItem = Item(product)

            if (removeItem(player, initialItem)) {
                addItem(player, productItem.id)
                getMessage(initialItem, productItem, burned)?.let { sendMessage(player, it) }
                playAudio(player, Sounds.FRY_2577)
                return true
            }
            return false
        }
    }

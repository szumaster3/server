package content.global.plugin.item

import content.data.GameAttributes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Items

class SkullSceptreListener : InteractionListener {
    override fun defineListeners() {
        on(Items.SKULL_SCEPTRE_9013, IntType.ITEM, "invoke", "divine", "operate") { player, node ->
            val option = getUsedOption(player)
            val charge = getCharge(node)

            if (option == "invoke" || option == "operate") {
                if (hasTimerActive(player, GameAttributes.TELEBLOCK_TIMER)) {
                    sendMessage(player, "A magical force has stopped you from teleporting.")
                    return@on true
                }

                if (!inEquipmentOrInventory(player, Items.SKULL_SCEPTRE_9013)) {
                    sendMessage(player, "You need to have the sceptre in your equipment or inventory to use it.")
                    return@on true
                }

                if (charge < 1) {
                    sendMessage(player, "You don't have enough charges left.")
                    return@on true
                }

                lock(player, 7)
                submitIndividualPulse(player, object : Pulse(1, player) {
                    var count = 0
                    override fun pulse(): Boolean {
                        when(count++) {
                            0 -> {
                                visualize(player, Animations.HUMAN_USE_SCEPTRE_9601, Graphics(org.rs.consts.Graphics.USE_SCEPTRE_1683, 100))
                            }
                            6 -> {
                                teleport(player, Location.create(3081, 3421, 0), TeleportManager.TeleportType.INSTANT)
                                return true
                            }
                        }
                        return false
                    }
                })

                setCharge(node, charge - 200)
                if (getCharge(node) < 1) {
                    removeItem(player, Items.SKULL_SCEPTRE_9013, Container.INVENTORY)
                    sendMessage(player, "Your staff crumbles to dust as you use its last charge.")
                }
            } else {
                if (charge < 1) {
                    sendMessage(player, "You don't have enough charges left.")
                } else {
                    val remaining = charge / 200
                    sendMessage(player, "Concentrating deeply, you divine that the sceptre has $remaining charges left.")
                }
            }

            return@on true
        }

        onUseWith(IntType.ITEM, Items.LEFT_SKULL_HALF_9008, Items.RIGHT_SKULL_HALF_9007) { player, used, with ->
            if (removeItem(player, used.asItem())) {
                sendItemDialogue(player, Items.STRANGE_SKULL_9009, "The two halves of the skull fit perfectly.")
                replaceSlot(player, with.asItem().index, Item(Items.STRANGE_SKULL_9009, 1))
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.BOTTOM_OF_SCEPTRE_9011, Items.TOP_OF_SCEPTRE_9010) { player, used, with ->
            // The two halves of the Sceptre fit perfectly. The Sceptre appears to be designed to have something on top.
            if (removeItem(player, used.asItem())) {
                sendItemDialogue(player, Items.RUNED_SCEPTRE_9012, "The two halves of the sceptre fit perfectly.")
                replaceSlot(player, with.asItem().index, Item(Items.RUNED_SCEPTRE_9012, 1))
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.STRANGE_SKULL_9009, Items.RUNED_SCEPTRE_9012) { player, used, with ->
            // The skull fits perfectly atop the Sceptre, you feel there is great magical power at work here.
            if (removeItem(player, used.asItem())) {
                sendDoubleItemDialogue(player, -1, Items.SKULL_SCEPTRE_9013, "The skull fits perfectly atop the Sceptre.")
                replaceSlot(player, with.asItem().index, Item(Items.SKULL_SCEPTRE_9013, 1))
            }
            return@onUseWith true
        }
    }
}

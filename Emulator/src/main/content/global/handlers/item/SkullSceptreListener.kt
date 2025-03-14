package content.global.handlers.item

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
            if (getUsedOption(player) == "invoke" || getUsedOption(player) == "operate") {
                if (hasTimerActive(player, "teleblock")) {
                    sendMessage(player, "A magical force has stopped you from teleporting.")
                    return@on true
                }

                if (node.id != null && inEquipmentOrInventory(player, Items.SKULL_SCEPTRE_9013)) {
                    lock(player, 3)
                    animate(player, Animations.HUMAN_USE_SCEPTRE_9601)
                    Graphics.send(
                        Graphics(
                            org.rs.consts.Graphics.USE_SCEPTRE_1683,
                            100,
                        ),
                        player.location,
                    )
                    submitIndividualPulse(
                        player,
                        object : Pulse(2, player) {
                            override fun pulse(): Boolean {
                                teleport(player, Location.create(3081, 3421, 0), TeleportManager.TeleportType.INSTANT)
                                return true
                            }
                        },
                    )
                }
                setCharge(node, getCharge(node) - 200)
                if (getCharge(node) < 1) {
                    removeItem(player, Items.SKULL_SCEPTRE_9013, Container.INVENTORY)
                    sendMessage(player, "Your staff crumbles to dust as you use its last charge.")
                }
            } else {
                if (getCharge(node) < 1) {
                    sendMessage(player, "You don't have enough charges left.")
                }
                sendMessage(
                    player,
                    "Concentrating deeply, you divine that the sceptre has " + (getCharge(node) / 200) +
                        " charges left.",
                )
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
                sendDoubleItemDialogue(
                    player,
                    -1,
                    Items.SKULL_SCEPTRE_9013,
                    "The skull fits perfectly atop the Sceptre.",
                )
                replaceSlot(player, with.asItem().index, Item(Items.SKULL_SCEPTRE_9013, 1))
            }
            return@onUseWith true
        }
    }
}

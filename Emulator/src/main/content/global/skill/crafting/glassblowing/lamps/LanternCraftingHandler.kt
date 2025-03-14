package content.global.skill.crafting.glassblowing.lamps

import core.api.addItem
import core.api.getStatLevel
import core.api.sendMessage
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

private val itemIDs =
    intArrayOf(
        Items.CANDLE_36,
        Items.BLACK_CANDLE_38,
        Items.OIL_LAMP_4525,
        Items.LANTERN_LENS_4542,
        Items.SAPPHIRE_1607,
    )

@Initializable
class LanternCraftingHandler : UseWithHandler(*itemIDs) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(Items.CANDLE_LANTERN_4527, ITEM_TYPE, this)
        addHandler(Items.OIL_LANTERN_FRAME_4540, ITEM_TYPE, this)
        addHandler(Items.BULLSEYE_LANTERN_4544, ITEM_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val used = event.used
        return when (used.id) {
            4527 -> craftCandleLantern(event.player, event)
            4540 -> craftOilLantern(event.player, event)
            4544 -> craftBullseyeLantern(event.player, event)
            else -> false
        }
    }

    private fun craftCandleLantern(
        player: Player,
        event: NodeUsageEvent,
    ): Boolean {
        return when (event.usedWith.id) {
            36, 38 -> {
                removeEventItems(player, event)
                addItem(player, if (event.usedWith.id == 36) Items.CANDLE_LANTERN_4529 else Items.CANDLE_LANTERN_4532)
                sendMessage(player, "You place the unlit candle inside the lantern.")
                return true
            }

            else -> false
        }
    }

    private fun craftOilLantern(
        player: Player,
        event: NodeUsageEvent,
    ): Boolean {
        return when (event.usedWith.id) {
            4525 -> {
                removeEventItems(player, event)
                addItem(player, Items.OIL_LANTERN_4535)
                sendMessage(player, "You place the oil lamp inside its metal frame.")
                return true
            }

            else -> false
        }
    }

    private fun craftBullseyeLantern(
        player: Player,
        event: NodeUsageEvent,
    ): Boolean {
        return when (event.usedWith.id) {
            4542 -> {
                removeEventItems(player, event)
                addItem(player, Items.BULLSEYE_LANTERN_4546)
                sendMessage(player, "You fashion the lens onto the lantern.")
                return true
            }

            1607 -> {
                if (getStatLevel(player, Skills.CRAFTING) >= 20) {
                    removeEventItems(player, event)
                    addItem(player, Items.SAPPHIRE_LANTERN_4700)
                    sendMessage(player, "You fashion the gem into a lens and fit it onto the lantern.")
                } else {
                    sendMessage(player, "You require a crafting level of 20 to use a gem as a lens.")
                }
                return true
            }

            else -> false
        }
    }

    private fun removeEventItems(
        player: Player,
        event: NodeUsageEvent,
    ) {
        player.inventory.remove(event.used.asItem())
        player.inventory.remove(event.usedWith.asItem())
    }
}

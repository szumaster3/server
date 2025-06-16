package content.global.plugin.item

import content.data.EnchantedJewellery
import core.api.addDialogueAction
import core.api.sendDialogueOptions
import core.api.sendMessage
import core.api.setTitle
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import org.rs.consts.Items

class EnchantedJewelleryPlugin : InteractionListener {
    private val ids = EnchantedJewellery.idMap.keys.toIntArray()

    override fun defineListeners() {
        /*
         * Handles the rub interaction for jewellery items.
         */

        on(ids, IntType.ITEM, "rub") { player, node ->
            handle(player, node, false)
            return@on true
        }

        /*
         * Handles the "operate" interaction for jewellery items.
         */

        on(ids, IntType.ITEM, "operate") { player, node ->
            handle(player, node, true)
            return@on true
        }
    }

    private fun handle(player: Player, node: Node, isEquipped: Boolean) {
        player.pulseManager.clear(PulseType.STANDARD)
        val item = node.asItem()

        if (item.id == Items.RING_OF_LIFE_2570) {
            sendMessage(player, "You can't operate that.")
            return
        }

        val jewellery = EnchantedJewellery.idMap[item.id] ?: return

        if (!jewellery.crumbled && jewellery.isLastItemIndex(jewellery.getItemIndex(item))) {
            sendMessage(player, "It will need to be recharged before you can use it again.")
            return
        }

        val typeName = jewellery.getJewelleryType(item).replace("combat bracelet", "bracelet", ignoreCase = true)
        sendMessage(player, "You rub the $typeName...")

        if (jewellery.options.isEmpty()) {
            jewellery.use(player, item, 0, isEquipped)
        } else {
            setTitle(player, jewellery.options.size)
            sendDialogueOptions(player, "Where would you like to teleport to?", *jewellery.options)
            addDialogueAction(player) { p, buttonID ->
                jewellery.use(p, item, buttonID - 2, isEquipped)
            }
        }
    }

}

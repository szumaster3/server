package content.global.handlers.item

import content.data.EnchantedJewellery
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import org.rs.consts.Items

class EnchantedJewelleryListener : InteractionListener {
    private val ids = EnchantedJewellery.idMap.keys.toIntArray()

    override fun defineListeners() {
        on(ids, IntType.ITEM, "rub") { player, node ->
            handle(player, node, false)
            return@on true
        }
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
        if (jewellery.isLastItemIndex(jewellery.getItemIndex(item)) && !jewellery.crumbled) {
            sendMessage(player, "It will need to be recharged before you can use it again.")
            return
        }

        sendMessage(player, "You rub the ${jewellery.getJewelleryType(item).replace("combat bracelet", "bracelet", true)}...")
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

package content.global.plugins.item

import content.data.EnchantedJewellery
import core.api.addDialogueAction
import core.api.sendMessage
import core.api.sendOptions
import core.api.setTitle
import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items

/**
 * Handles the options for enchanted jewellery items.
 */
@Initializable
class JewelleryTeleportOptionHandler : OptionHandler() {

    private val itemIds = EnchantedJewellery.idMap.keys

    override fun newInstance(arg: Any?): Plugin<Any>? {
        itemIds.forEach { id ->
            ItemDefinition.forId(id).handlers["option:rub"] = this
            ItemDefinition.forId(id).handlers["option:operate"] = this
        }
        return null
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val item = node.asItem()
        val isEquipped = option.equals("operate", ignoreCase = true)

        player.pulseManager.clear(PulseType.STANDARD)

        if (item.id == Items.RING_OF_LIFE_2570) {
            sendMessage(player, "You can't operate that.")
            return true
        }

        val jewellery = EnchantedJewellery.idMap[item.id] ?: return true

        if (!jewellery.crumbled && jewellery.isLastItemIndex(jewellery.getItemIndex(item))) {
            sendMessage(player, "You will need to recharge your ${jewellery.getJewelleryType(item)} before you can use it again.")
            return true
        }

        val typeName = jewellery.getJewelleryType(item)
            .replace("combat bracelet", "bracelet", ignoreCase = true)

        sendMessage(player, "You rub the $typeName...")

        if (jewellery.options.isEmpty()) {
            jewellery.use(player, item, 0, isEquipped)
        } else {
            setTitle(player, jewellery.options.size)
            sendOptions(player, "Where would you like to teleport to?", *jewellery.options)
            addDialogueAction(player) { p, buttonID ->
                jewellery.use(p, item, buttonID - 2, isEquipped)
            }
        }
        return true
    }
}
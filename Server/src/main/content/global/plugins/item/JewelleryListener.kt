package content.global.plugins.item

import content.data.EnchantedJewellery
import core.api.sendMessage
import core.api.setTitle
import core.api.sendOptions
import core.game.dialogue.DialogueFile
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Items

class JewelleryListener : InteractionListener {

    private val itemIds = EnchantedJewellery.idMap.keys

    override fun defineListeners() {
        itemIds.forEach { id ->
            on(id, IntType.ITEM, "rub") { player, node ->
                handleJewellery(player, node.asItem(), false)
                return@on true
            }
            on(id, IntType.ITEM, "operate") { player, node ->
                handleJewellery(player, node.asItem(), true)
                return@on true
            }
        }
    }

    private fun handleJewellery(player: Player, item: Item, isEquipped: Boolean) {
        player.pulseManager.clear(PulseType.STANDARD)

        if (item.id == Items.RING_OF_LIFE_2570) {
            sendMessage(player, "You can't operate that.")
            return
        }

        val jewellery = EnchantedJewellery.idMap[item.id] ?: return
        val itemIndex = jewellery.getItemIndex(item)

        if (!jewellery.crumbled && jewellery.isLastItemIndex(itemIndex)) {
            sendMessage(player, "You will need to recharge your ${jewellery.getJewelleryType(item)} before you can use it again.")
            return
        }

        val typeName = jewellery.getJewelleryType(item).replace("combat bracelet", "bracelet", ignoreCase = true)
        sendMessage(player, "You rub the $typeName...")

        if (jewellery.options.isEmpty()) {
            jewellery.use(player, item, 0, isEquipped)
        } else {
            player.dialogueInterpreter.open(JewelleryTeleportDialogue(item, jewellery, isEquipped))
        }
    }

    class JewelleryTeleportDialogue(
        private val item: Item,
        private val jewellery: EnchantedJewellery,
        private val isEquipped: Boolean
    ) : DialogueFile() {

        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> {
                    setTitle(player!!, jewellery.options.size)
                    sendOptions(player!!, "Where would you like to teleport to?", *jewellery.options)
                    stage = 1
                }
                1 -> {
                    val optionIndex = (buttonID - 1).coerceIn(0, jewellery.options.lastIndex)
                    jewellery.use(player!!, item, optionIndex, isEquipped)
                    end()
                }
            }
        }
    }
}

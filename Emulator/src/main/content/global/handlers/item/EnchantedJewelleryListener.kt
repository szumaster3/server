package content.global.handlers.item

import content.data.EnchantedJewellery
import core.api.openDialogue
import core.api.sendDialogueOptions
import core.api.sendMessage
import core.api.setTitle
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.START_DIALOGUE
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

    private fun handle(
        player: Player,
        node: Node,
        isEquipped: Boolean,
    ) {
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
        sendMessage(
            player,
            "You rub the ${jewellery.getJewelleryType(item).replace("combat bracelet", "bracelet", true)}...",
        )
        if (jewellery.options.isEmpty()) {
            jewellery.use(player, item, 0, isEquipped)
        } else {
            openDialogue(player, EnchantedJewelleryDialogueFile(jewellery, item, isEquipped))
        }
    }

    class EnchantedJewelleryDialogueFile(
        private val jewellery: EnchantedJewellery,
        private val item: Item,
        private val isEquipped: Boolean,
    ) : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            when (stage) {
                START_DIALOGUE -> {
                    setTitle(player!!, jewellery.options.size)
                    sendDialogueOptions(player!!, "Where would you like to teleport to?", *jewellery.options)
                    stage++
                }

                1 -> {
                    end()
                    jewellery.use(player!!, item, buttonID - 1, isEquipped)
                }
            }
        }
    }
}

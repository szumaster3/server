package content.global.skill.construction.storage

import content.global.skill.construction.storage.box.FancyDressBoxItem
import core.api.*
import core.api.ui.sendInterfaceConfig
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import org.rs.consts.Animations
import org.rs.consts.Scenery

class FancyDressBoxListener : InteractionListener {
    private val FANCY_DRESS_BOX_CLOSE =
        intArrayOf(
            Scenery.FANCY_DRESS_BOX_18772,
            Scenery.FANCY_DRESS_BOX_18774,
            Scenery.FANCY_DRESS_BOX_18776
        )
    private val FANCY_DRESS_BOX_OPEN =
        intArrayOf(
            Scenery.FANCY_DRESS_BOX_18773,
            Scenery.FANCY_DRESS_BOX_18775,
            Scenery.FANCY_DRESS_BOX_18777
        )

    private val INTERFACE = 467

    override fun defineListeners() {

        /*
         * Handles opening of fancy dress box.
         */

        on(FANCY_DRESS_BOX_CLOSE, IntType.SCENERY, "open") { player, node ->
            animate(player, Animations.OPEN_CHEST_536)
            replaceScenery(node.asScenery(), node.id + 1, -1)
            return@on true
        }

        /*
         * Handles interaction with fancy dress box.
         */

        on(FANCY_DRESS_BOX_OPEN, IntType.SCENERY, "search", "close") { player, node ->
            when (getUsedOption(player)) {
                "close" -> {
                    animate(player, Animations.HUMAN_CLOSE_CHEST_538)
                    replaceScenery(node.asScenery(), node.id - 1, -1)
                }

                else -> {
                    setAttribute(player, "con:fancy-dress-box", true)
                    val contentId =
                        FancyDressBoxItem.values().map { Item(it.displayId) }.toTypedArray()
                    openInterface(player, INTERFACE).also {
                        PacketRepository.send(
                            ContainerPacket::class.java,
                            ContainerContext(player, INTERFACE, 164, 30, contentId, false)
                        )
                        FancyDressBoxItem.values().forEachIndexed { index, item ->
                            val key = "set:$index"
                            val hidden = getAttribute(player, key, false)
                            val itemName = getItemName(item.displayId)
                            sendString(player, itemName, INTERFACE, 55 + index * 2)
                            sendInterfaceConfig(player, INTERFACE, item.labelId, hidden)
                            sendInterfaceConfig(player, INTERFACE, item.iconId + 1, hidden)
                        }
                    }
                }
            }
            return@on true
        }
    }
}

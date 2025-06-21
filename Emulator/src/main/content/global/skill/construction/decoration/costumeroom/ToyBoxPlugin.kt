package content.global.skill.construction.decoration.costumeroom

import content.global.skill.construction.decoration.costumeroom.data.Toy
import core.api.*
import core.api.ui.sendInterfaceConfig
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.ContainerPacket
import org.rs.consts.Animations
import org.rs.consts.Scenery

class ToyBoxPlugin: InteractionListener {
    val TOY_BOX_CLOSE = intArrayOf(Scenery.TOY_BOX_18798, Scenery.TOY_BOX_18800, Scenery.TOY_BOX_18802)
    val TOY_BOX_OPEN = intArrayOf(Scenery.TOY_BOX_18799, Scenery.TOY_BOX_18801, Scenery.TOY_BOX_18803)
    val INTERFACE = 467

    override fun defineListeners() {

        /*
         * Handles opening of toy box.
         */

        on(TOY_BOX_CLOSE, IntType.SCENERY, "open") { player, node ->
            animate(player, Animations.OPEN_CHEST_536)
            replaceScenery(node.asScenery(), node.id + 1, -1)
            return@on true
        }

        /*
         * Handles interaction with toy box.
         */

        on(TOY_BOX_OPEN, IntType.SCENERY, "search", "close") { player, node ->
            when (getUsedOption(player)) {
                "close" -> {
                    animate(player, Animations.HUMAN_CLOSE_CHEST_538)
                    replaceScenery(node.asScenery(), node.id - 1, -1)
                }

                else -> {
                    setAttribute(player, "con:toy-box", true)
                    sendString(player, "Toy box", INTERFACE, 225)
                    val visibleItems = Toy.values().filterNot { it == Toy.More || it == Toy.Back }
                    val contentId = visibleItems.map { Item(it.displayId) }.toTypedArray()

                    openInterface(player, INTERFACE).also {
                        PacketRepository.send(
                            ContainerPacket::class.java,
                            OutgoingContext.Container(player, INTERFACE, 164, 30, contentId, false)
                        )
                        visibleItems.forEachIndexed { index, item ->
                            val key = "toybox:${item.name}"
                            val hidden = getAttribute(player, key, false)
                            val itemName = getItemName(item.displayId)
                            sendString(player, itemName, INTERFACE, 55 + index * 2)
                            sendInterfaceConfig(player, INTERFACE, item.iconId + 1, hidden)
                        }
                    }
                }
            }
            return@on true
        }
    }
}
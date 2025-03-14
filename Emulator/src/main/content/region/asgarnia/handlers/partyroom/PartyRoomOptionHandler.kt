package content.region.asgarnia.handlers.partyroom

import core.api.animate
import core.api.replaceScenery
import core.api.sendInputDialogue
import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.container.Container
import core.game.container.ContainerEvent
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations

@Initializable
class PartyRoomOptionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(CLOSED_CHEST).handlers["option:open"] = this
        SceneryDefinition.forId(OPEN_CHEST).handlers["option:deposit"] = this
        SceneryDefinition.forId(OPEN_CHEST).handlers["option:shut"] = this
        SceneryDefinition.forId(LEVER).handlers["option:pull"] = this
        definePlugin(DepositInterfaceHandler())
        definePlugin(BalloonManager())
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        when (node.id) {
            CLOSED_CHEST -> {
                animate(player, Animations.OPEN_CHEST_536)
                replaceScenery(node.asScenery(), OPEN_CHEST, -1)
            }

            OPEN_CHEST ->
                when (option) {
                    "deposit" -> deposit(player)
                    "shut" -> {
                        animate(player, 537)
                        replaceScenery(node.asScenery(), CLOSED_CHEST, -1)
                    }
                }

            LEVER -> PartyRoomUtils.handleLever(player, node.asScenery())
        }
        return true
    }

    private fun deposit(player: Player) {
        if (!viewers.containsKey(player.name)) {
            viewers[player.name] = ChestViewer(player).view()
        } else {
            sendMessage(player, "You are already viewing the chest!.")
        }
    }

    class DepositInterfaceHandler : ComponentPlugin() {
        override fun newInstance(arg: Any?): Plugin<Any> {
            ComponentDefinition.put(647, this)
            ComponentDefinition.put(648, this)
            return this
        }

        override fun handle(
            player: Player,
            component: Component,
            opcode: Int,
            button: Int,
            slot: Int,
            itemId: Int,
        ): Boolean {
            var itemId = itemId
            val viewer = player.getExtension<ChestViewer>(ChestViewer::class.java)
            if (viewer == null || viewer.container == null) {
                player.interfaceManager.close()
                return true
            }
            when (component.id) {
                648 -> {
                    if (itemId == -1) {
                        if (player.inventory[slot] != null) {
                            itemId = player.inventory[slot].id
                        } else {
                            return true
                        }
                    }
                    when (opcode) {
                        155 -> viewer.container.addItem(slot, 1)
                        196 -> viewer.container.addItem(slot, 5)
                        124 -> viewer.container.addItem(slot, 10)
                        199 -> {
                            val ammount = player.inventory.getAmount(Item(itemId))
                            viewer.container.addItem(slot, ammount)
                        }

                        234 -> {
                            sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                                viewer.container.addItem(slot, value as Int)
                            }
                        }
                    }
                }

                647 -> {
                    when (button) {
                        25 -> {
                            viewer.accept()
                            return true
                        }
                    }
                    if (itemId == -1 && viewer.container.freeSlot() != 0) {
                        itemId = viewer.container[slot].id
                    }
                    when (opcode) {
                        155 -> viewer.container.takeItem(slot, 1)
                        196 -> viewer.container.takeItem(slot, 5)
                        124 -> viewer.container.takeItem(slot, 10)
                        199 -> {
                            val ammount = viewer.container.getAmount(Item(itemId))
                            viewer.container.takeItem(slot, ammount)
                        }

                        234 ->
                            sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                                viewer.container.takeItem(slot, value as Int)
                            }
                    }
                }
            }
            return true
        }
    }

    companion object {
        private const val CLOSED_CHEST = 26193
        private const val OPEN_CHEST = 2418
        private const val LEVER = 26194

        @JvmField
        val chestQueue: Container = Container(215)

        @JvmField
        val partyChest: Container = Container(215)

        @JvmStatic
        val viewers: MutableMap<String, ChestViewer> = HashMap()
        val balloonManager = BalloonManager()
        var isDancing = false

        @JvmStatic
        fun update(
            type: Int,
            event: ContainerEvent?,
        ) {
            for (viewer in viewers.values) {
                viewer.update(type, event)
            }
        }

        @JvmStatic
        fun update() {
            update(0, null)
            update(1, null)
        }
    }
}

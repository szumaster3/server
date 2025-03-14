package content.global.handlers.iface

import core.api.sendInputDialogue
import core.api.sendMessage
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.trade.TradeModule.Companion.getExtension
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components

@Initializable
class TradeInterface : ComponentPlugin() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.TRADECONFIRM_334, this)
        ComponentDefinition.put(Components.TRADEMAIN_335, this)
        ComponentDefinition.put(Components.TRADESIDE_336, this)
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
        val module = getExtension(player) ?: return true
        when (component.id) {
            Components.TRADECONFIRM_334 ->
                when (button) {
                    20 -> module.setAccepted(true, true)
                    21 -> module.decline()
                }

            Components.TRADEMAIN_335 ->
                when (opcode) {
                    155 ->
                        when (button) {
                            16 -> module.setAccepted(true, true)
                            18 -> module.decline()
                            30 -> module.container!!.withdraw(slot, 1)
                        }

                    196 -> module.container!!.withdraw(slot, 5)
                    124 -> module.container!!.withdraw(slot, 10)
                    199 -> module.container!!.withdraw(slot, module.container!!.getAmount(module.container!![slot]))
                    234 ->
                        sendInputDialogue(player, false, "Enter the amount:") { value: Any ->
                            var s = value.toString()
                            s = s.replace("k", "000")
                            s = s.replace("K", "000")
                            s = s.replace("m", "000000")
                            s = s.replace("M", "000000")
                            val `val` = s.toInt()
                            module.container!!.withdraw(slot, `val`)
                        }

                    9 -> {
                        if (getExtension(if (button == 32) module.target else player) == null) {
                            return true
                        }
                        sendMessage(
                            player,
                            getExtension(
                                if (button ==
                                    32
                                ) {
                                    module.target
                                } else {
                                    player
                                },
                            )!!.container!![slot].definition.examine,
                        )
                    }
                }

            Components.TRADESIDE_336 ->
                when (opcode) {
                    155 -> module.container!!.offer(slot, 1)
                    196 -> module.container!!.offer(slot, 5)
                    124 -> module.container!!.offer(slot, 10)
                    199 -> module.container!!.offer(slot, player.inventory.getAmount(player.inventory[slot]))
                    234 ->
                        sendInputDialogue(player, false, "Enter the amount:") { value: Any ->
                            var s = value.toString()
                            s = s.replace("k", "000")
                            s = s.replace("K", "000")
                            val `val` = s.toInt()
                            module.container!!.offer(slot, `val`)
                        }

                    9 -> sendMessage(player, player.inventory[slot].definition.examine)
                }
        }
        return true
    }
}

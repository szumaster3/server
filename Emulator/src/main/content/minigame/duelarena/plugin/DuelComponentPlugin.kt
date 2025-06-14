package content.minigame.duelarena.plugin

import core.api.setAttribute
import core.api.setVarp
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Plugin
import org.rs.consts.Components

class DuelComponentPlugin : ComponentPlugin() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.forId(Components.DUEL2_SELECT_TYPE_640)?.plugin = this
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
        when (component.id) {
            Components.DUEL2_SELECT_TYPE_640 -> {
                var staked = false
                when (button) {
                    20 -> {
                        val other = player.getAttribute<Player>("duel:partner")
                        if (other == null || other.getExtension<Any?>(DuelSession::class.java) != null) {
                            player.packetDispatch.sendMessage("Other player is busy at the moment.")
                            return true
                        }
                        if (player.getAttribute(
                                "duel:staked",
                                false,
                            ) &&
                            other.ironmanManager.isIronman &&
                            !GameWorld.settings!!.isDevMode
                        ) {
                            other.sendMessage("You can't accept a staked duel as an Ironman.")
                            player.sendMessage("You can't duel Ironman players.")
                            return true
                        }
                        player.interfaceManager.close()
                        if (!player.getAttribute("duel:staked", false)) {
                            player.requestManager.request(other, DuelArenaActivity.FRIEND_REQUEST)
                        } else {
                            player.requestManager.request(other, DuelArenaActivity.STAKE_REQUEST)
                        }
                        return true
                    }

                    18, 22 -> staked = false
                    19, 21 -> {
                        staked = true
                        if (player.ironmanManager.isIronman) {
                            player.sendMessage("You can't stake as an Iron man.")
                            staked = false
                        }
                    }

                    else -> return false
                }
                setAttribute(player, "duel:staked", staked)
                setVarp(player, 283, (if (staked) 2 else 1) shl 26)
            }
        }
        return true
    }
}

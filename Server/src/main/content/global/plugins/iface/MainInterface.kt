package content.global.plugins.iface

import content.data.GameAttributes
import core.api.*
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.combat.equipment.WeaponInterface.WeaponInterfaces
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.world.GameWorld
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Components

/**
 * Handles the main game interface.
 */
@Initializable
class MainInterface : ComponentPlugin() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.TOPLEVEL_548, this)
        ComponentDefinition.put(Components.SNAPSHOT_MAIN_553, this)
        ComponentDefinition.put(Components.GAME_INTERFACE_740, this)
        ComponentDefinition.put(Components.TOPLEVEL_FULLSCREEN_746, this)
        ComponentDefinition.put(Components.TOPSTAT_RUN_750, this)
        ComponentDefinition.put(Components.FILTERBUTTONS_751, this)
        return this
    }

    override fun handle(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        when (component.id) {
            Components.GAME_INTERFACE_740 -> {
                if (button == 3) {
                    closeChatBox(player)
                }
                return true
            }

            Components.TOPLEVEL_FULLSCREEN_746 -> {
                when (button) {
                    12 -> sendString(player, "When you have finished playing ${GameWorld.settings?.name}, always use the button below to logout safely.", 182, 0)
                    49 -> sendString(player, "Friends List - ${GameWorld.settings?.name} ${GameWorld.settings?.worldId}", 550, 3)
                    110 -> configureWorldMap(player)
                }
                return true
            }

            Components.TOPLEVEL_548 -> {
                if (button in 38..44 || button in 20..26) {
                    player.interfaceManager.currentTabIndex = getTabIndex(button)
                }

                when (button) {
                    21 -> sendString(player, "Friends List - ${GameWorld.settings?.name} ${GameWorld.settings?.worldId}", 550, 3)
                    40 -> updateQuestTab(player)
                    41 -> refreshInventory(player)
                    38 -> {
                        val weaponType = player.getExtension(WeaponInterface::class.java) as? WeaponInterfaces
                        if (weaponType == WeaponInterfaces.STAFF) {
                            val c = Component(WeaponInterfaces.STAFF.interfaceId)
                            player.interfaceManager.openTab(0, c)
                            val inter = player.getExtension(WeaponInterface::class.java) as? WeaponInterface
                            inter?.updateInterface()
                        }
                    }

                    66 -> {
                        if(!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)){
                            openInterface(player, 764)
                        } else {
                            configureWorldMap(player)
                        }
                    }
                    69 -> sendString(player, "When you have finished playing ${GameWorld.settings?.name}, always use the button below to logout safely.", 182, 0)
                }
                return true
            }

            Components.TOPSTAT_RUN_750 -> {
                if (opcode == 155 && button == 1) {
                    player.settings.toggleRun()
                }
                return true
            }

            Components.FILTERBUTTONS_751 -> {
                if (opcode == 155 && button == 27) {
                    openReport(player)
                }
                return true
            }
        }
        return true
    }

    companion object {
        private fun configureWorldMap(player: Player) {
            if (player.inCombat()) {
                sendMessage(player, "It wouldn't be very wise opening the world map during combat.")
                return
            }
            if (player.locks.isInteractionLocked() || player.locks.isMovementLocked()) {
                sendMessage(player, "You can't do this right now.")
                return
            }
            player.interfaceManager.close()
            player.interfaceManager.openWindowsPane(Component(Components.WORLDMAP_755))
            val posHash = (player.location.z shl 28) or (player.location.x shl 14) or player.location.y
            player.packetDispatch.sendScriptConfigs(622, posHash, "", 0)
            player.packetDispatch.sendScriptConfigs(674, posHash, "", 0)
        }

        private fun openReport(player: Player) {
            player.interfaceManager.open(Component(Components.SNAPSHOT_MAIN_553))?.closeEvent = CloseEvent { p, _ ->
                p.packetDispatch.sendRunScript(80, "")
                p.packetDispatch.sendRunScript(137, "")
                true
            }
            player.packetDispatch.sendRunScript(508, "")
            if (player.details.rights != Rights.REGULAR_PLAYER) {
                for (i in 0 until 18) {
                    player.packetDispatch.sendInterfaceConfig(Components.SNAPSHOT_MAIN_553, i, false)
                }
            }
        }

        private fun getTabIndex(button: Int): Int {
            return if (button < 27) (button - 20) + 7 else button - 38
        }
    }
}

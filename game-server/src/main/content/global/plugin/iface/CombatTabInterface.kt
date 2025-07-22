package content.global.plugin.iface

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.combat.equipment.WeaponInterface.WeaponInterfaces
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components

/**
 * Handles the combat tab interface.
 *
 * @author Emperor, Vexia
 */
@Initializable
class CombatTabInterface : ComponentPlugin() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        WeaponInterfaces.values().forEach { inter ->
            ComponentDefinition.put(inter.interfaceId, this)
        }
        ComponentDefinition.put(Components.WEAPON_FISTS_SEL_92, this)
        return this
    }

    override fun handle(
        player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int
    ): Boolean {
        when (button) {
            7, 9, 24, 26, 27 -> {
                GameWorld.Pulser.submit(object : Pulse(1, player) {
                    override fun pulse(): Boolean {
                        player.settings.toggleRetaliating()
                        return true
                    }
                })
            }

            8, 10, 11, 85 -> {
                GameWorld.Pulser.submit(object : Pulse(1, player) {
                    override fun pulse(): Boolean {
                        val inter = player.getExtension(WeaponInterface::class.java) as? WeaponInterface
                        if (inter != null && inter.isSpecialBar) {
                            player.settings.toggleSpecialBar()
                            if (player.settings.isSpecialToggled) {
                                val handler = CombatStyle.MELEE.swingHandler.getSpecial(
                                    player.equipment.getNew(3)?.id ?: -1
                                )
                                if (handler != null) {
                                    @Suppress("UNCHECKED_CAST") val plugin = handler as? Plugin<Any>
                                    if (plugin?.fireEvent("instant_spec", player) == true) {
                                        handleInstantSpec(player, handler, plugin)
                                    }
                                }
                            }
                        }
                        return true
                    }
                })
            }

            0 -> return false
            else -> {
                val inter = player.getExtension(WeaponInterface::class.java) as? WeaponInterface ?: return false
                if (inter.setAttackStyle(button)) {
                    when (button) {
                        4, 5 -> inter.openAutocastSelect()
                        else -> {
                            if (player.properties.autocastSpell != null) {
                                inter.selectAutoSpell(-1, false)
                            }
                        }
                    }
                    return true
                }
                return false
            }
        }
        return true
    }

    companion object {
        private fun handleInstantSpec(player: Player, handler: CombatSwingHandler, plugin: Plugin<Any>) {
            handler.swing(player, player.properties.combatPulse.victim, null)
        }
    }
}
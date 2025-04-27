package content.global.handlers.iface

import core.api.ContainerListener
import core.api.log
import core.api.submitWorldPulse
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.container.Container
import core.game.container.ContainerEvent
import core.game.container.access.InterfaceContainer
import core.game.container.impl.EquipmentContainer
import core.game.global.action.EquipHandler.Companion.unequip
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners.run
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.Log
import org.rs.consts.Components

@Initializable
class EquipmentTabInterface : ComponentPlugin() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.ITEMS_LOSE_ON_DEATH_102, this)
        ComponentDefinition.put(Components.WORNITEMS_387, this)
        ComponentDefinition.put(Components.EQUIP_SCREEN2_667, this)
        ComponentDefinition.put(Components.INVENTORY_WEAR2_670, this)
        return this
    }

    override fun handle(
        p: Player,
        component: Component,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        if (component.id == Components.EQUIP_SCREEN2_667) {
            if (button != 14) {
                return false
            }
            when (opcode) {
                155 -> {
                    p.pulseManager.clear()
                    submitWorldPulse(
                        object : Pulse(1, p) {
                            override fun pulse(): Boolean {
                                unequip(p, slot, itemId)
                                return true
                            }
                        },
                    )
                    return true
                }

                9 -> {
                    p.sendMessage(p.equipment[slot].definition.examine)
                    return true
                }

                196 -> {
                    submitWorldPulse(
                        object : Pulse(1, p) {
                            override fun pulse(): Boolean {
                                operate(p, slot, itemId)
                                return true
                            }
                        },
                    )
                    return true
                }
            }
            return false
        } else if (component.id == Components.INVENTORY_WEAR2_670) {
            when (opcode) {
                155 -> {
                    p.pulseManager.clear()
                    val item = p.inventory[slot]
                    submitWorldPulse(
                        object : Pulse(1, p) {
                            override fun pulse(): Boolean {
                                if (item == null) return true
                                run(item.id, IntType.ITEM, "equip", p, item)
                                return true
                            }
                        },
                    )
                    return true
                }

                9 -> {
                    p.sendMessage(p.inventory[slot].definition.examine)
                    return true
                }
            }
        }
        when (opcode) {
            206 -> {
                if (button != 28) {
                    return false
                }
                submitWorldPulse(
                    object : Pulse(1, p) {
                        override fun pulse(): Boolean {
                            operate(p, slot, itemId)
                            return true
                        }
                    },
                )
                return true
            }

            else -> when (button) {
                52 -> {
                    if (p.interfaceManager.isOpened() && p.interfaceManager.opened?.id == Components.ITEMS_LOSE_ON_DEATH_102) {
                        return true
                    }

                    val zoneType = p.zoneMonitor.type

                    val itemArray = DeathTask.getContainers(p)
                    val kept = itemArray[0]
                    val amtKeptOnDeath = kept.itemCount()
                    if (amtKeptOnDeath > 4 && zoneType == 0) {
                        log(
                            this.javaClass,
                            Log.ERR,
                            "Items kept on death interface should not contain more than 4 items when not in a safe zone!",
                        )
                    }

                    val slot0 = kept.getId(0)
                    val slot1 = kept.getId(1)
                    val slot2 = kept.getId(2)
                    val slot3 = kept.getId(3)
                    val skulled = if (p.skullManager.isSkulled) 1 else 0
                    val hasBoB = if (p.familiarManager.hasFamiliar()) {
                        if (p.familiarManager.familiar.isBurdenBeast) {
                            if ((p.familiarManager.familiar as content.global.skill.summoning.familiar.BurdenBeast).container.isEmpty) {
                                0
                            } else {
                                1
                            }
                        } else {
                            0
                        }
                    } else {
                        0
                    }

                    val params = arrayOf<Any>(
                        hasBoB,
                        skulled,
                        slot3,
                        slot2,
                        slot1,
                        slot0,
                        amtKeptOnDeath,
                        zoneType,
                        "You are skulled.",
                    )
                    p.packetDispatch.sendRunScript(118, "siiooooii", *params)

                    p.interfaceManager.openComponent(Components.ITEMS_LOSE_ON_DEATH_102)
                }

                28 -> if (opcode == 81) {
                    p.pulseManager.clear()
                    submitWorldPulse(
                        object : Pulse(1, p) {
                            override fun pulse(): Boolean {
                                unequip(p, slot, itemId)
                                return true
                            }
                        },
                    )
                    return true
                }

                55 -> {
                    if (p.interfaceManager.isOpened() && p.interfaceManager.opened?.id == Components.EQUIP_SCREEN2_667) {
                        return true
                    }
                    val listener: ContainerListener = object : ContainerListener {
                        override fun update(
                            c: Container?,
                            e: ContainerEvent?,
                        ) {
                            PacketRepository.send(
                                ContainerPacket::class.java,
                                ContainerContext(p, -1, -1, 98, e!!.items, false, *e.slots),
                            )
                        }

                        override fun refresh(c: Container?) {
                            PacketRepository.send(
                                ContainerPacket::class.java,
                                ContainerContext(p, -1, -1, 98, c!!, false),
                            )
                        }
                    }
                    p.interfaceManager.openComponent(Components.EQUIP_SCREEN2_667)
                        ?.setUncloseEvent { player: Player, c: Component? ->
                            player.removeAttribute("equip_stats_open")
                            player.interfaceManager.closeSingleTab()
                            player.inventory.listeners.remove(listener)
                            true
                        }
                    p.setAttribute("equip_stats_open", true)
                    EquipmentContainer.update(p)
                    p.interfaceManager.openSingleTab(Component(Components.INVENTORY_WEAR2_670))
                    InterfaceContainer.generateItems(
                        p,
                        p.inventory.toArray(),
                        arrayOf("Equip"),
                        Components.INVENTORY_WEAR2_670,
                        0,
                        7,
                        4,
                        93,
                    )
                    p.inventory.listeners.add(listener)
                    p.inventory.refresh()
                    ItemDefinition.statsUpdate(p)
                    p.packetDispatch.sendIfaceSettings(1278, 14, Components.EQUIP_SCREEN2_667, 0, 13)
                }
            }
        }
        return true
    }

    fun operate(
        player: Player,
        slot: Int,
        itemId: Int,
    ) {
        if (slot < 0 || slot > 13) {
            return
        }
        val item = player.equipment[slot] ?: return
        if (run(item.id, IntType.ITEM, "operate", player, item)) {
            return
        }
        val handler = item.operateHandler
        if (handler != null && handler.handle(player, item, "operate")) {
            return
        }
        player.packetDispatch.sendMessage("You can't operate that.")
    }
}

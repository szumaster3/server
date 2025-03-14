package core.game.world.map.zone.impl

import content.data.LightSource.Companion.forProductId
import content.data.LightSource.Companion.getActiveLightSource
import core.api.Event.UsedWith
import core.api.getItemName
import core.api.runTask
import core.game.component.Component
import core.game.event.EventHook
import core.game.event.UseWithEvent
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Components
import java.util.*

class DarkZone :
    MapZone("Dark zone", true),
    EventHook<UseWithEvent> {
    override fun configure() {
        register(ZoneBorders(1728, 5120, 1791, 5247))
        registerRegion(12693)
        registerRegion(12949)
        register(ZoneBorders(3262, 9487, 3224, 9533))
        register(ZoneBorders(3223, 9487, 3211, 9506))
        register(ZoneBorders(3306, 9661, 3222, 9600))
        register(ZoneBorders(3717, 9473, 3841, 9346))
    }

    override fun continueAttack(
        e: Entity,
        target: Node,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        if (e is Player) {
            if (e.interfaceManager.overlay !== DARKNESS_OVERLAY) {
                return true
            }
            return false
        }
        return true
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (target is Item) {
            val s = forProductId(target.id)
            if (s != null) {
                val name = option.name.lowercase(Locale.getDefault())
                if (name == "drop") {
                    (e as Player).packetDispatch.sendMessage(
                        "Dropping the " + s.getName() + " would leave you without a light source.",
                    )
                    return true
                }
                if (name == "extinguish") {
                    (e as Player).packetDispatch.sendMessage(
                        "Extinguishing the " + s.getName() + " would leave you without a light source.",
                    )
                    return true
                }
                if (name == "destroy") {
                    (e as Player).packetDispatch.sendMessage(
                        "Destroying the headband would leave you without a light source.",
                    )
                    return true
                }
            }
        }
        return false
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            val player = e
            val source = getActiveLightSource(player)
            if (source == null) {
                player.interfaceManager.openOverlay(DARKNESS_OVERLAY)
            } else if (source.interfaceId > 0) {
                player.interfaceManager.openOverlay(Component(source.interfaceId))
            }
        }
        e.hook(UsedWith, this)
        return true
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (e is Player) {
            e.interfaceManager.closeOverlay()
        }
        e.unhook(this)
        return true
    }

    fun updateOverlay(player: Player) {
        val source = getActiveLightSource(player)

        var overlay = -1
        if (player.interfaceManager.overlay != null) {
            overlay = player.interfaceManager.overlay.id
        }
        if (source == null) {
            if (overlay != DARKNESS_OVERLAY.id) {
                player.interfaceManager.openOverlay(DARKNESS_OVERLAY)
            }
            return
        }
        val pulse = player.getExtension<Pulse>(DarkZone::class.java)
        pulse?.stop()
        if (source.interfaceId != overlay) {
            if (source.interfaceId == -1) {
                player.interfaceManager.closeOverlay()
                return
            }
            player.interfaceManager.openOverlay(Component(source.interfaceId))
        }
    }

    override fun process(
        entity: Entity,
        event: UseWithEvent,
    ) {
        val isTinderbox = getItemName(event.used) == "Tinderbox" || getItemName(event.with) == "Tinderbox"

        if (isTinderbox && entity is Player) {
            runTask(entity, 2, 1) {
                checkDarkArea(entity.asPlayer())
                Unit
            }
        }
    }

    companion object {
        val DARKNESS_OVERLAY: Component =
            object : Component(Components.DARKNESS_DARK_96) {
                override fun open(player: Player) {
                    var pulse = player.getExtension<Pulse>(DarkZone::class.java)
                    if (pulse != null && pulse.isRunning) {
                        return
                    }
                    pulse =
                        object : Pulse(2, player) {
                            var count: Int = 0

                            override fun pulse(): Boolean {
                                if (count == 0) {
                                    player.packetDispatch.sendMessage(
                                        "You hear tiny insects skittering over the ground...",
                                    )
                                } else if (count == 5) {
                                    player.packetDispatch.sendMessage("Tiny biting insects swarm all over you!")
                                } else if (count > 5) {
                                    player.impactHandler.manualHit(player, 1, HitsplatType.NORMAL)
                                }
                                count++
                                return false
                            }
                        }
                    Pulser.submit(pulse)
                    player.addExtension(DarkZone::class.java, pulse)
                    super.open(player)
                }

                override fun close(player: Player): Boolean {
                    if (!super.close(player)) {
                        return false
                    }
                    val pulse = player.getExtension<Pulse>(DarkZone::class.java)
                    pulse?.stop()
                    return true
                }
            }

        fun checkDarkArea(p: Player): Boolean {
            for (r in p.zoneMonitor.zones) {
                if (r.zone is DarkZone) {
                    val zone = r.zone as DarkZone
                    zone.updateOverlay(p)
                    return true
                }
            }
            return false
        }
    }
}

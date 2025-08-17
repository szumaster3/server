package core.game.world.map.zone.impl

import content.data.LightSource.Companion.forProductId
import content.data.LightSource.Companion.getActiveLightSource
import core.api.*
import core.game.component.Component
import core.game.event.EventHook
import core.game.event.UseWithEvent
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import shared.consts.Components
import shared.consts.Items
import java.util.*

/**
 * Handles a dark area.
 *
 * @author Emperor
 */
class DarkZone : MapZone("Dark zone", true), EventHook<UseWithEvent> {

    /**
     * Configures the boundaries and regions for the Dark Zone.
     */
    override fun configure() {
        register(ZoneBorders(1728, 5120, 1791, 5247))
        registerRegion(12693)
        registerRegion(12949)
        register(ZoneBorders(3262, 9487, 3224, 9533))
        register(ZoneBorders(3223, 9487, 3211, 9506))
        register(ZoneBorders(3306, 9661, 3222, 9600))
        register(ZoneBorders(3717, 9473, 3841, 9346))
        register(ZoneBorders(2496, 9408, 2559, 9471)) // Skavid caves.
    }

    override fun continueAttack(entity: Entity, target: Node, style: CombatStyle, message: Boolean): Boolean {
        if (entity is Player) {
            return entity.interfaceManager.overlay != DARKNESS_OVERLAY
        }
        return true
    }

    override fun interact(entity: Entity, target: Node, option: Option): Boolean {
        if (target is Item) {
            val s = forProductId(target.id)
            if (s != null) {
                val name = option.name.lowercase(Locale.getDefault())
                val player = entity.asPlayer()
                if (name == "drop") {
                    sendMessage(player, "Dropping the " + s.getName() + " would leave you without a light source.")
                    return true
                }
                if (name == "extinguish") {
                    sendMessage(
                        player,
                        "Extinguishing the " + s.getName() + " would leave you without a light source.",
                    )
                    return true
                }
                if (name == "destroy") {
                    sendMessage(player, "Destroying the headband would leave you without a light source.")
                    return true
                }
            }
        }
        return false
    }

    override fun enter(entity: Entity): Boolean {
        if (entity is Player) {
            val player = entity.asPlayer()
            val source = getActiveLightSource(player)

            if (hasUnlimitedLightSource(player)) return false

            if (source == null) {
                player.interfaceManager.openOverlay(DARKNESS_OVERLAY)
            } else if (source.interfaceId > 0) {
                player.interfaceManager.openOverlay(Component(source.interfaceId))
            }
        }
        entity.hook(Event.UsedWith, this)
        return true
    }

    override fun leave(entity: Entity, logout: Boolean): Boolean {
        if (entity is Player) {
            entity.interfaceManager.closeOverlay()
        }
        entity.unhook(this)
        return true
    }

    /**
     * Updates the overlay.
     *
     * @param player The player.
     */
    fun updateOverlay(player: Player) {
        val source = getActiveLightSource(player)

        var overlay = -1
        if (player.interfaceManager.overlay != null) {
            overlay = player.interfaceManager.overlay!!.id
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

    override fun process(entity: Entity, event: UseWithEvent, ) {
        val isTinderbox = getItemName(event.used) == "Tinderbox" || getItemName(event.with) == "Tinderbox"

        if (isTinderbox && entity is Player) {
            runTask(entity, 2, 1) {
                checkDarkArea(entity.asPlayer())
                Unit
            }
        }
    }

    companion object {

        /**
         * Checks if the player has any items that provide unlimited light source.
         * TODO:
         *  Headband 1 -> should act as dim light source.
         *  Headband 2 -> should acts as a medium light source.
         *  Headband 3 -> should acts as a bright light source.
         *
         * @param player The player whose equipment is checked.
         * @return Boolean indicating whether the player has an unlimited light source.
         */
        private fun hasUnlimitedLightSource(player: Player): Boolean {
            return player.equipment.containsAtLeastOneItem(
                Item(DiaryManager(player).headband), Item(Items.FIREMAKING_CAPE_9804), Item(Items.FIREMAKING_CAPET_9805)
            )
        }

        /**
         * The darkness overlay component that applies visual effects to the player
         * in the Dark Zone.
         */
        val DARKNESS_OVERLAY: Component = object : Component(Components.DARKNESS_DARK_96) {
            override fun open(player: Player) {
                var pulse = player.getExtension<Pulse>(DarkZone::class.java)
                if (pulse != null && pulse.isRunning) {
                    return
                }
                pulse = object : Pulse(2, player) {
                    var count: Int = 0

                    override fun pulse(): Boolean {
                        if (count == 0) {
                            sendMessage(
                                player,
                                "You hear tiny insects skittering over the ground...",
                            )
                        } else if (count == 5) {
                            sendMessage(player, "Tiny biting insects swarm all over you!")
                        } else if (count > 5) {
                            impact(player, 1, HitsplatType.NORMAL)
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


        /**
         * Checks if the player is in a dark area and will update accordingly.
         *
         * @param player The player.
         */
        fun checkDarkArea(player: Player): Boolean {
            for (r in player.zoneMonitor.getZones()) {
                if (r.zone is DarkZone) {
                    val zone = r.zone as DarkZone
                    if (!hasUnlimitedLightSource(player)) {
                        zone.updateOverlay(player)
                    }
                    return true
                }
            }
            return false
        }
    }
}

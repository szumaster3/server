package content.global.random.event.maze

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.game.event.EventHook
import core.game.event.TickEvent
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

/**
 * Hook implementation for handling the Maze random event.
 * @author Ovenbread
 */
class Maze : EventHook<TickEvent>, MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(getRegionBorders(11591))
    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.FOLLOWERS, ZoneRestriction.TELEPORT)

    override fun process(entity: Entity, event: TickEvent) {
        if (entity is Player) {
            val player = entity.asPlayer()
            var ticks = getAttribute(player, GameAttributes.MAZE_ATTRIBUTE_TICKS_LEFT, 0)
            if (ticks > 0) {
                ticks--
                setAttribute(player, GameAttributes.MAZE_ATTRIBUTE_TICKS_LEFT, ticks)
            }
            setVarp(player, MAZE_TIMER_VARP, ticks / 3, false)
        }
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            val player = entity.asPlayer()
            player.hook(Event.Tick, this)
            /*
             * Temporary solution while finding the var that is responsible for the chest swap.
             * For best results, it needs to match: https://pastebin.com/cJNvkdUa
             */
            for (i in CHEST_LOCATIONS) {
                SceneryBuilder.replace(
                    Scenery(shared.consts.Scenery.WALL_3626, i),
                    Scenery(shared.consts.Scenery.CHEST_3635, i),
                    -1
                )
            }
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            val player = entity.asPlayer()
            closeOverlay(player)
            restoreTabs(player)
            player.unhook(this)
            clearLogoutListener(player, RandomEvent.logout())
            AntiMacro.terminateEventNpc(player)
        }
    }

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        // Unlimited run - 25 February 2009 update.
        if(entity is Player) if (entity.settings.runEnergy < 100.0) {
            entity.settings.updateRunEnergy(-100.0)
        }
    }

    companion object {
        const val MAZE_TIMER_VARP = 531

        val STARTING_POINTS = arrayOf(
            Location(2928, 4553, 0),
            Location(2917, 4553, 0),
            Location(2908, 4555, 0),
            Location(2891, 4589, 0),
            Location(2891, 4595, 0),
            Location(2891, 4595, 0),
            Location(2926, 4597, 0),
            Location(2931, 4597, 0),
        )
        private val CHEST_LOCATIONS = arrayOf(
            Location.create(2925, 4573),
            Location.create(2917, 4590),
            Location.create(2918, 4590),
            Location.create(2899, 4579),
            Location.create(2895, 4592),
            Location.create(2896, 4591),
            Location.create(2900, 4578),
            Location.create(2890, 4599),
            Location.create(2901, 4560),
            Location.create(2930, 4595),
            Location.create(2924, 4572)
        )
    }
}
package content.global.ame.maze

import content.data.GameAttributes
import core.api.*
import core.api.ui.restoreTabs
import core.game.event.EventHook
import core.game.event.TickEvent
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs.consts.Components

/**
 * Maze interface.
 *
 * Author: [Ovenbread](https://gitlab.com/ovenbreado)
 */
class MazeInterface : InteractionListener, EventHook<TickEvent>, MapArea {
    companion object {
        const val MAZE_TIMER_INTERFACE = Components.MAZETIMER_209
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
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(getRegionBorders(11591))
    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.FOLLOWERS, ZoneRestriction.TELEPORT)

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            val player = entity.asPlayer()
            sendMessage(player, "Head for the center of the maze.")
            removeTabs(player, 0, 1, 2, 3, 4, 5, 6, 12)
            openOverlay(player, MAZE_TIMER_INTERFACE)
            player.hook(Event.Tick, this)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean, ) {
        if (entity is Player) {
            val player = entity.asPlayer()
            closeOverlay(player)
            restoreTabs(player)
            player.unhook(this)
        }
    }

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
}

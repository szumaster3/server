package content.region.morytania.phas.quest.ahoy.plugin

import core.api.*
import core.game.component.Component
import core.game.event.EventHook
import core.game.event.TickEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.Entity
import core.game.world.map.zone.ZoneBorders
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.Scenery

@Initializable
class WindSpeedInterface :
    InterfaceListener,
    InteractionListener,
    EventHook<TickEvent>,
    MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(3616, 3545, 3622, 3539, 2))

    override fun areaEnter(entity: Entity) {
        openInterface(entity.asPlayer(), Components.AHOY_WINDSPEED_10)
        setAttribute(entity.asPlayer(), GhostsAhoyUtils.windSpeed, 0)
        entity.asPlayer().hook(Event.Tick, this)
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        entity.asPlayer().unhook(this)
        entity.asPlayer().interfaceManager.close(Component(Components.AHOY_WINDSPEED_10))
        removeAttribute(entity.asPlayer(), GhostsAhoyUtils.windSpeed)
    }

    override fun defineInterfaceListeners() {
        on(Components.AHOY_WINDSPEED_10) { _, _, _, _, _, _ ->
            return@on true
        }
    }

    override fun defineListeners() {
        on(Scenery.MAST_5274, IntType.SCENERY, "search") { player, _ ->

            if (getAttribute(player, GhostsAhoyUtils.windSpeed, 0) == 0) {
                sendDialogue(
                    player,
                    "You can see a tattered flag blowing in the wind. The wind is blowing too hard to make out any details.",
                )
            }

            if (getAttribute(player, GhostsAhoyUtils.windSpeed, 0) == 1) {
                sendDialogue(
                    player,
                    "You can see a tattered flag blowing in the wind. The top half of the flag is coloured red.",
                )
            }

            if (getAttribute(player, GhostsAhoyUtils.windSpeed, 0) == 2) {
                sendDialogue(
                    player,
                    "You can see a tattered flag blowing in the wind. The bottom half of the flag is coloured blue.",
                )
            }

            if (getAttribute(player, GhostsAhoyUtils.windSpeed, 0) == 3) {
                sendDialogue(
                    player,
                    "You can see a tattered flag blowing in the wind. The skull emblem is coloured blue.",
                )
            }
            return@on true
        }
    }

    override fun process(
        entity: Entity,
        event: TickEvent,
    ) {
        if (RandomFunction.random(1, 10) == 1) setAttribute(entity.asPlayer(), GhostsAhoyUtils.windSpeed, 0)
        if (RandomFunction.random(1, 10) == 2) setAttribute(entity.asPlayer(), GhostsAhoyUtils.windSpeed, 1)
        if (RandomFunction.random(1, 10) == 3) setAttribute(entity.asPlayer(), GhostsAhoyUtils.windSpeed, 2)
        if (RandomFunction.random(1, 10) == 4) setAttribute(entity.asPlayer(), GhostsAhoyUtils.windSpeed, 3)
        sendString(
            entity.asPlayer(),
            if (getAttribute(entity.asPlayer(), GhostsAhoyUtils.windSpeed, 0) == 1) " Low" else " High",
            Components.AHOY_WINDSPEED_10,
            2,
        )
    }
}

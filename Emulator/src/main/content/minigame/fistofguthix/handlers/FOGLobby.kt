package content.minigame.fistofguthix.handlers

import core.game.component.Component
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Plugin
import java.util.*

class FOGLobby :
    MapZone("Fog Lobby", true),
    Plugin<Any?> {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun enter(e: Entity): Boolean {
        if (e.isPlayer) {
            sendInterface(e.asPlayer())
        }
        return super.enter(e)
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (!e.isPlayer) {
            return super.interact(e, target, option)
        }
        val player = e.asPlayer()
        when (target.id) {
            30203 -> {
                player.teleport(Location.create(3242, 3574, 0))
                return true
            }
        }
        return super.interact(e, target, option)
    }

    private fun sendInterface(player: Player) {
        player.interfaceManager.openOverlay(Component(FOGActivityPlugin.WAITING_INTERFACE))
        player.packetDispatch.sendInterfaceConfig(FOGActivityPlugin.WAITING_INTERFACE, 17, true)
        player.packetDispatch.sendInterfaceConfig(FOGActivityPlugin.WAITING_INTERFACE, 26, true)
        player.packetDispatch.sendString(
            "Rating: " + player.getSavedData().activityData.fogRating,
            FOGActivityPlugin.WAITING_INTERFACE,
            7,
        )
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? = null

    override fun configure() {
        super.registerRegion(6743)
    }
}

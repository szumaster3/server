package content.global.skill.construction.decoration

import content.data.GameAttributes
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Scenery

/**
 * Handles the Bell-pulls immediately call the servant.
 */
@Initializable
class BellPullPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(Scenery.ROPE_BELL_PULL_13307).handlers["option:ring"] = this
        SceneryDefinition.forId(Scenery.BELL_PULL_13308).handlers["option:ring"] = this
        SceneryDefinition.forId(Scenery.POSH_BELL_PULL_13309).handlers["option:ring"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        val manager = player?.houseManager ?: return true
        val servant = manager.servant
        if (servant == null || !manager.hasServant()) {
            sendMessage(player,"You have no servant to ring.")
            return true
        }

        if (getAttribute(player, GameAttributes.CON_SERVANT_CALL, false)) {
            sendMessage(player,"Your servant has already been called!")
            return true
        }

        playAudio(player, 932)
        val spawn = Location.getRandomLocation(player.location,1,true)
        servant.teleporter.send(player.location.transform(spawn), TeleportManager.TeleportType.INSTANT)

        setAttribute(player, GameAttributes.CON_SERVANT_CALL, true)

        GameWorld.Pulser.submit(object : Pulse(17) {
            override fun pulse(): Boolean {
                removeAttribute(player, GameAttributes.CON_SERVANT_CALL)
                return true
            }
        })
        return true
    }

}
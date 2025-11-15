package content.global.skill.construction.decoration

import core.api.playAudio
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Scenery
import shared.consts.Sounds

/**
 * Handles the bell interaction.
 */
@Initializable
class BellPlugin : OptionHandler() {

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
            player.sendMessage("You have no servant to ring.")
            return true
        }

        if (player.getAttribute("servant:call", false) == true) {
            player.sendMessage("Your servant has already been called!")
            return true
        }

        // Temporary solution because it spawns behind a wall.
        // val destination = getPathableCardinal(servant.asNpc(), player.location)
        playAudio(player, Sounds.BELL_2192)
        servant.teleporter.send(player.location, TeleportManager.TeleportType.INSTANT)
        player.setAttribute("servant:call", true)

        GameWorld.Pulser.submit(object : Pulse(17) {
            override fun pulse(): Boolean {
                player.removeAttribute("servant:call")
                return true
            }
        })
        return true
    }

}
package content.minigame.bountyhunter.plugin

import core.cache.def.impl.SceneryDefinition
import core.game.activity.ActivityManager
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.update.flag.context.Animation
import core.plugin.Plugin

class BHOptionHandler : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(28110).handlers["option:exit"] = this
        SceneryDefinition.forId(28119).handlers["option:enter"] = this
        SceneryDefinition.forId(28120).handlers["option:enter"] = this
        SceneryDefinition.forId(28121).handlers["option:enter"] = this
        SceneryDefinition.forId(28122).handlers["option:exit"] = this
        SceneryDefinition.forId(28115).handlers["option:view"] = this
        SceneryDefinition.forId(28116).handlers["option:view"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val scenery = node as Scenery
        val activity = player.getExtension<BountyHunterActivity>(BountyHunterActivity::class.java)
        when (scenery.id) {
            28119 -> {
                ActivityManager.start(player, "BH low_level", false)
                return true
            }

            28120 -> {
                ActivityManager.start(player, "BH mid_level", false)
                return true
            }

            28121 -> {
                ActivityManager.start(player, "BH high_level", false)
                return true
            }

            28115 -> {
                BHScoreBoard.rogues.open(player)
                return true
            }

            28116 -> {
                BHScoreBoard.hunters.open(player)
                return true
            }

            28122 -> {
                if (activity == null) {
                    return false
                }
                if (player.getAttribute("exit_penalty", 0) > ticks) {
                    player.packetDispatch.sendMessage("You can't leave the crater until the exit penalty is over.")
                    return true
                }
                player.lock(2)
                Pulser.submit(
                    object : Pulse(1) {
                        override fun pulse(): Boolean {
                            player.properties.teleportLocation = activity.type.exitLocation
                            return true
                        }
                    },
                )
                player.animate(Animation.create(7376))
                return true
            }

            28110 -> {
                if (activity == null) {
                    return false
                }
                activity.leaveWaitingRoom(player, false)
                return true
            }
        }
        return false
    }
}

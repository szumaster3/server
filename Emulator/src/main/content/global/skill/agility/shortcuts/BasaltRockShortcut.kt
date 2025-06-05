package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.faceLocation
import core.api.sendMessage
import core.api.submitWorldPulse
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Represents the basalt Rock shortcut.
 */
@Initializable
class BasaltRockShortcut : AgilityShortcut {
    constructor() : super(intArrayOf(), 0, 0.0, "")

    constructor(ids: IntArray?, level: Int, experience: Double, vararg options: String?) : super(ids, level, experience, *options)

    data class RockPath(val id: Int, val option: String, val from: Location, val to: Location)

    companion object {
        private val paths = listOf(
            RockPath(4550, "jump-to",     Location.create(2522, 3595, 0), Location.create(2522, 3597, 0)),
            RockPath(4551, "jump-across", Location.create(2522, 3597, 0), Location.create(2522, 3595, 0)),
            RockPath(4552, "jump-across", Location.create(2522, 3600, 0), Location.create(2522, 3602, 0)),
            RockPath(4553, "jump-across", Location.create(2522, 3602, 0), Location.create(2522, 3600, 0)),
            RockPath(4554, "jump-across", Location.create(2518, 3611, 0), Location.create(2516, 3611, 0)),
            RockPath(4555, "jump-across", Location.create(2516, 3611, 0), Location.create(2518, 3611, 0)),
            RockPath(4556, "jump-across", Location.create(2514, 3613, 0), Location.create(2514, 3615, 0)),
            RockPath(4557, "jump-across", Location.create(2514, 3615, 0), Location.create(2514, 3613, 0)),
            RockPath(4558, "jump-across", Location.create(2514, 3617, 0), Location.create(2514, 3619, 0)),
            RockPath(4559, "jump-to",     Location.create(2514, 3619, 0), Location.create(2514, 3617, 0))
        )

        private val pathMap = paths.associateBy { it.id }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        paths.forEach {
            configure(BasaltRockShortcut(intArrayOf(it.id), 1, 0.0, it.option))
        }
        return this
    }

    override fun run(player: Player, obj: Scenery, option: String, failed: Boolean) {
        val path = pathMap[obj.id] ?: return

        submitWorldPulse(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                val id = path.id
                val pointA = path.from
                val pointB = path.to

                val (start, end) = when (player.location) {
                    pointA -> Pair(pointA, pointB)
                    pointB -> Pair(pointB, pointA)
                    else -> return false.also { sendMessage(player, "I can't reach.") }
                }

                faceLocation(player, end)

                val delay = if (id in listOf(4550, 4551, 4554, 4555, 4559)) 1 else 0
                AgilityHandler.forceWalk(player, -1, start, end, Animation.create(769), 20, 0.0, null, delay)
                return true
            }
        })
    }

    override fun getDestination(n: Node?, node: Node): Location? {
        return pathMap[(node as Scenery).id]?.to ?: super.getDestination(node, node)
    }
}

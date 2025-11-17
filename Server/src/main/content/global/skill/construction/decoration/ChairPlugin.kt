package content.global.skill.construction.decoration

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryType
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Handles the sit-on interaction for chairs and benches in the poh.
 */
@Initializable
class ChairPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        for (chair in Chairs.values()) {
            SceneryDefinition.forId(chair.objectId).handlers["option:sit-on"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val scenery = node as? Scenery ?: return false
        val chair = Chairs.fromId(scenery.id) ?: return false

        val animID = if (scenery.type == SceneryType.CentrepieceDiagonal) chair.anim + 1 else chair.anim
        val sitAnimID = chair.sitAnim

        forceMove(player, player.location, scenery.location, 0, 30, Direction.NORTH, sitAnimID)

        player.locks.lockInteractions(600_000)

        registerLogoutListener(player, "someone else house") {
            replaceScenery(Scenery(if(scenery.id != 83) node.id else 83,node.location,node.rotation,Direction.SOUTH),node.id,-1)
        }

        player.pulseManager.run(object : Pulse(2) {
            override fun pulse(): Boolean {
                player.animate(Animation.create(animID))
                replaceScenery(node.asScenery(),83,-1)
                return false
            }

            override fun stop() {
                super.stop()
                clearLogoutListener(player, "someone else house")
                player.locks.unlockInteraction()
                player.moveStep()
                player.animate(Animation.create(sitAnimID+2))
                replaceScenery(Scenery(83,node.location,node.rotation,Direction.SOUTH),node.id,-1)
            }
        })

        return true
    }
}
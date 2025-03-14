package content.region.kandarin.quest.waterfall.handlers

import core.api.setAttribute
import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.ticks
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction

@Initializable
class MithrilSeedsPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(299).handlers["option:plant"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        if (player.getAttribute("delay:plant", -1) > ticks) {
            return true
        }
        if (getObject(player.location) != null) {
            player.packetDispatch.sendMessage("You can't plant a seed here.")
            return true
        }
        player.animate(ANIMATION)
        player.inventory.remove(ITEM)
        val scenery: Scenery =
            SceneryBuilder.add(
                Scenery(
                    getFlower(if (RandomFunction.random(100) == 1) RARE else FLOWERS),
                    player.location,
                ),
                100,
            )
        player.moveStep()
        player.lock(3)
        player.pulseManager.run(
            object : Pulse(1, player) {
                override fun pulse(): Boolean {
                    player.faceLocation(scenery.getFaceLocation(player.location))
                    player.dialogueInterpreter.open(1 shl 16 or 1, scenery)
                    return true
                }
            },
        )
        setAttribute(player, "delay:plant", ticks + 3)
        player.packetDispatch.sendMessage("You open the small mithril case.")
        player.packetDispatch.sendMessage("You drop a seed by your feet.", 1)
        return true
    }

    override fun isWalk(): Boolean {
        return false
    }

    companion object {
        private val ITEM = Item(299, 1)
        private val FLOWERS = intArrayOf(2980, 2981, 2982, 2983, 2984, 2985, 2986)
        private val RARE = intArrayOf(2987, 2988)
        private val ANIMATION = Animation(827)

        fun getFlower(array: IntArray): Int {
            return array[RandomFunction.random(array.size)]
        }
    }
}

package content.region.kandarin.quest.waterfall.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable

@Initializable
class MithrilSeedsDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var flower: Scenery? = null

    override fun open(vararg args: Any): Boolean {
        flower = args[0] as Scenery
        options("Pick the flowers.", "Leave the flowers.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (interfaceId) {
            228 ->
                when (buttonId) {
                    1 -> {
                        player.lock(2)
                        player.faceLocation(flower!!.getFaceLocation(player.location))
                        player.animate(ANIMATION)
                        Pulser.submit(
                            object : Pulse(2, player, flower) {
                                override fun pulse(): Boolean {
                                    val reward = Item(2460 + (flower!!.id - 2980 shl 1))
                                    if (reward == null || !player.inventory.hasSpaceFor(reward)) {
                                        player.packetDispatch.sendMessage("Not enough space in your inventory.")
                                        return true
                                    }
                                    if (SceneryBuilder.remove(flower)) {
                                        player.inventory.add(reward)
                                        player.packetDispatch.sendMessage("You pick the flowers.")
                                    }
                                    return true
                                }
                            },
                        )
                    }
                }
        }
        end()
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(1 shl 16 or 1)
    }

    companion object {
        private val ANIMATION = Animation(827)
    }
}

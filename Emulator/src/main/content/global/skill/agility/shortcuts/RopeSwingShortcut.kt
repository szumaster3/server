package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.animationDuration
import core.api.playAudio
import core.api.sendMessage
import core.api.withinDistance
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.scenery.Scenery
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.Sounds

@Initializable
class RopeSwingShortcut : UseWithHandler() {
    companion object {
        private var ropeDelay = 0
        private val END_LOCATION = Location.create(2505, 3087, 0)
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val scenery = event.usedWith as? Scenery ?: return false
        val usedItem = event.usedItem ?: throw IllegalArgumentException("Used item cannot be null!")

        val objId = scenery.id
        val usedId = usedItem.id
        val player = event.player

        ropeDelay = GameWorld.ticks + animationDuration(Animation.create(497))

        if (objId == org.rs.consts.Scenery.LONG_BRANCHED_TREE_2327 && usedId == Items.ROPE_954) {
            if (!withinDistance(player, scenery.location, 2)) {
                sendMessage(player, "I can't reach that.")
                return true
            }
            if (ropeDelay > GameWorld.ticks) {
                sendMessage(player, "The rope is being used.")
                return true
            }
            player.faceLocation(END_LOCATION)
            playAudio(player, Sounds.SWING_ACROSS_2494, 1)
            player.packetDispatch.sendSceneryAnimation(scenery, Animation.create(497), true)
            AgilityHandler.forceWalk(
                player,
                0,
                player.location,
                END_LOCATION,
                Animation.create(751),
                50,
                22.0,
                "You skillfully swing across.",
                1,
            )
            return true
        }
        return false
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(org.rs.consts.Scenery.LONG_BRANCHED_TREE_2327, OBJECT_TYPE, this)
        return this
    }
}

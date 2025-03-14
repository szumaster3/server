package content.global.skill.slayer

import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.plugin.Initializable

@Initializable
class SlayerTowerListener : InteractionListener {
    override fun defineListeners() {
        on(sceneryIDs, IntType.SCENERY, "open", "close") { player, node ->
            when (node.id) {
                4490, 4487 ->
                    handleAutowalkDoor(player, node.asScenery()).also {
                        switchStatue()
                    }
            }
            return@on true
        }
    }

    private fun switchStatue() {
        for (l in LOCATIONS) {
            val `object` = getObject(l)
            if (`object` != null) {
                val id = if (`object`.id == OPEN_ID) CLOSED_ID else OPEN_ID
                SceneryBuilder.replace(`object`, `object`.transform(id))
            }
        }
    }

    companion object {
        private val LOCATIONS = arrayOf(Location(3430, 3534, 0), Location(3426, 3534, 0))
        private val sceneryIDs = intArrayOf(4490, 4487, 4492)
        private const val OPEN_ID = 5117
        private const val CLOSED_ID = 5116
    }
}

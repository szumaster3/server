package content.global.activity.godwars

import core.api.getAttribute
import core.api.log
import core.api.sendMessage
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.tools.Log
import core.tools.StringUtils
import shared.consts.Scenery as Objects

/**
 * Handles the entrances to God Wars Dungeon boss chambers.
 */
class GodWarsChamberEntranceListener : InteractionListener {

    override fun defineListeners() {
        (Objects.BIG_DOOR_26425..Objects.BIG_DOOR_26428).forEach { doorId ->
            on(doorId, IntType.SCENERY, "open") { player, node ->
                val scenery = node as Scenery
                val dir = Direction.get((scenery.rotation + 3) % 4)


                if (dir.stepX != 0) {
                    val nextX = scenery.location.transform(dir.stepX, 0, 0).x
                    if (player.location.x == nextX) {
                        sendMessage(player, "You can't leave through this door. The altar can teleport you out.")
                        return@on true
                    }
                } else {
                    val nextY = scenery.location.transform(0, dir.stepY, 0).y
                    if (player.location.y == nextY) {
                        sendMessage(player, "You can't leave through this door. The altar can teleport you out.")
                        return@on true
                    }
                }

                var index = scenery.id - Objects.BIG_DOOR_26425
                if (index < 2) index = 1 - index
                val faction = GodWarsFaction.values()[index]
                val name = faction.name.lowercase()
                val required = 40

                val killCount = getAttribute(player, "gwd_kc_${faction.name.lowercase()}", -1)
                if (killCount < required) {
                    sendMessage(player, "You need $required ${StringUtils.formatDisplayName(name)} kills to enter this.")
                    return@on true
                }

                if (DoorActionHandler.handleAutowalkDoor(player, node)) {
                    log(this.javaClass, Log.INFO,"${player.username} entered ${faction.name} GWD boss room.")
                    GodWarsFaction.increaseKillCount(player, faction, -required)
                    sendMessage(
                        player,
                        "The door devours the life-force of $required followers of ${faction.name} that you have slain."
                    )
                }
                return@on true
            }
        }
    }
}

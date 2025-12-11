package content.region.misthalin.varrock.quest.dragon.plugin

import content.region.misthalin.varrock.quest.dragon.DragonSlayer
import core.api.removeItem
import core.api.sendMessage
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Music

class DragonSlayerKeyDoorPlugin : InteractionListener {

    override fun defineListeners() {

        val keyDoors = mapOf(
            2601 to DragonSlayer.GREEN_KEY,
            2600 to DragonSlayer.PURPLE_KEY,
            2599 to DragonSlayer.BLUE_KEY,
            2598 to DragonSlayer.YELLOW_KEY,
            2596 to DragonSlayer.RED_KEY,
            2597 to DragonSlayer.ORANGE_KEY
        )

        keyDoors.forEach { (doorId, reqKey) ->
            on(doorId, IntType.SCENERY, "open") { player, node ->
                if (!player.inventory.containsItem(reqKey)) {
                    sendMessage(player, "This door is securely locked.")
                } else {
                    removeItem(player, reqKey)
                    sendMessage(player, "The key disintegrates as it unlocks the door.")
                    handleAutowalkDoor(player, node.asScenery())
                }
                return@on true
            }
        }

        on(2595, IntType.SCENERY, "open") { player, node ->
            if(player.location.x == 2940 && player.location.y == 3248) {
                handleAutowalkDoor(player, node.asScenery())
                return@on true
            }
            if(player.inventory.containsItem(DragonSlayer.MAZE_KEY)) {
                sendMessage(player, "You use the key and the door opens.")
                if(!player.musicPlayer.hasUnlocked(Music.MELZARS_MAZE_365))
                    player.musicPlayer.unlock(Music.MELZARS_MAZE_365)
                handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "This door is securely locked.")
            }
            return@on true
        }
    }
}

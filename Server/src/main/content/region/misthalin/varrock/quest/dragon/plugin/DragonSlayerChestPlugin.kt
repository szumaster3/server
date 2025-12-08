package content.region.misthalin.varrock.quest.dragon.plugin

import content.region.misthalin.varrock.quest.dragon.DragonSlayer
import core.api.playAudio
import core.api.replaceScenery
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItemManager
import shared.consts.Scenery
import shared.consts.Sounds

class DragonSlayerChestPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles search & close interaction with end melzar basement chest.
         */

        on(Scenery.CHEST_2603, IntType.SCENERY, "open") { player, node ->
            player.packetDispatch.sendMessage("You open the chest.")
            replaceScenery(node.asScenery(), 2604, -1)
            playAudio(player, Sounds.CHEST_OPEN_52)
            return@on true
        }

        on(Scenery.CHEST_2604, IntType.SCENERY, "search") { player, _ ->
            if (!player.inventory.containsItem(DragonSlayer.MAZE_PIECE)) {
                if (!player.inventory.add(DragonSlayer.MAZE_PIECE)) GroundItemManager.create(DragonSlayer.MAZE_PIECE, player)
                player.dialogueInterpreter.sendItemMessage(
                    DragonSlayer.MAZE_PIECE.id, "You find a map piece in the chest."
                )
            } else {
                sendMessage(player, "You find nothing in the chest.")
            }
            return@on true
        }

        on(Scenery.CHEST_2604, IntType.SCENERY, "close") { player, node ->
            sendMessage(player, "You shut the chest.")
            replaceScenery(node.asScenery(), 2603, -1)
            playAudio(player, Sounds.CHEST_CLOSE_51)
            return@on true
        }
    }
}

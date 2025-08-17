package content.region.kandarin.ardougne.west.plugin

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.DoorActionHandler
import core.game.interaction.OptionHandler
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Sounds

/**
 * Handles interactions with the West Ardougne doors.
 */
@Initializable
class WestArdougneDoorPlugin : OptionHandler() {

    companion object {
        private const val MAIN_DOOR_ID = 9738
        private const val SIDE_DOOR_ID = 9330
        private const val DOOR_OPEN_DURATION = 6
        private const val FORCE_MOVE_DELAY = 0
        private const val FORCE_MOVE_SPEED = 80
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(MAIN_DOOR_ID).handlers["option:open"] = this
        SceneryDefinition.forId(SIDE_DOOR_ID).handlers["option:open"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val door = node as? Scenery ?: return false
        val pairedDoor = DoorActionHandler.getSecondDoor(door)

        if (!isQuestComplete(player, Quests.BIOHAZARD)) {
            sendMessage(player, "You try to open the large wooden doors...")
            sendMessage(player, "...But they will not open.", 1)
            if (player.location.x > 2557) {
                sendNPCDialogue(player, NPCs.MOURNER_2349, "Oi! What are you doing? Get away from there!")
            }
            return true
        }

        val ticks = if (door.id == SIDE_DOOR_ID) {
            val x = if (player.location.x > 2558) 2559 else 2557
            forceWalk(player, Location(x, 3299), "smart")
            2
        } else 1

        sendMessage(player, "You pull on the large wooden doors...")
        sendMessage(player, "...You open them and walk through.")
        player.lock(1)

        queueScript(player, ticks, QueueStrength.WEAK) {
            playAudio(player, Sounds.BIG_WOODEN_DOOR_OPEN_44)
            val doorsToOpen = listOfNotNull(door, pairedDoor)
            doorsToOpen.forEach {
                val newScenery = when (it.id) {
                    MAIN_DOOR_ID -> Scenery(it.id + 2, it.location.transform(-1, 0, 0), 10, 5)
                    SIDE_DOOR_ID -> Scenery(SIDE_DOOR_ID, it.location.transform(-1, 0, 0), 10, 3)
                    else -> it
                }
                SceneryBuilder.replace(it, newScenery, DOOR_OPEN_DURATION)
            }
            playAudio(player, Sounds.BIG_WOODEN_DOOR_CLOSE_43, 2)
            val destination = if (player.location.x > 2558) {
                player.location.transform(Direction.WEST, 3)
            } else {
                player.location.transform(Direction.EAST, 2)
            }
            forceMove(player, player.location, destination, FORCE_MOVE_DELAY, FORCE_MOVE_SPEED, null, 819)
            return@queueScript stopExecuting(player)
        }

        return true
    }
}
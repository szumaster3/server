package content.global.ame.maze

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

/**
 * Handles interactions for Maze random event.
 * @author Ovenbread, szu
 */
class MazeListener : InteractionListener {
    private val rewardIDs = intArrayOf(Items.COINS_995, Items.FEATHER_314, Items.IRON_ARROW_884, Items.CHAOS_RUNE_562, Items.STEEL_ARROW_886, Items.DEATH_RUNE_560, Items.COAL_454, Items.NATURE_RUNE_561, Items.MITHRIL_ORE_448)
    private val itemsDivisor = arrayOf(1.0, 2.0, 3.0, 9.0, 12.0, 18.0, 45.0, 162.0, 180.0)
    private val wallIDs = intArrayOf(Scenery.WALL_3628, Scenery.WALL_3629, Scenery.WALL_3630, Scenery.WALL_3631, Scenery.WALL_3632)

    private fun calculateLoot(player: Player) {
        val randomNumber = (0..8).random()
        val totalLevel = player.getSkills().totalLevel.toDouble()
        val rewardPotential = getAttribute(player, GameAttributes.MAZE_ATTRIBUTE_TICKS_LEFT, 0).toDouble() / 300.0
        val itemDivisor = itemsDivisor[randomNumber]
        val itemQuantity = (totalLevel * rewardPotential * 3.33) / itemDivisor
        if (itemQuantity.toInt() > 0) {
            addItemOrDrop(player, rewardIDs[randomNumber], itemQuantity.toInt())
        }
    }

    private val mazeDropTable = WeightBasedTable.create(
        WeightedItem(Items.AIR_RUNE_556, 15, 15, 1.0),
        WeightedItem(Items.WATER_RUNE_555, 10, 10, 1.0),
        WeightedItem(Items.EARTH_RUNE_557, 10, 10, 1.0),
        WeightedItem(Items.FIRE_RUNE_554, 10, 10, 1.0),
        WeightedItem(Items.BRONZE_ARROW_882, 20, 20, 1.0),
        WeightedItem(Items.BRONZE_BOLTS_877, 10, 10, 1.0),
        WeightedItem(Items.IRON_ARROW_884, 15, 15, 1.0),
        WeightedItem(Items.ATTACK_POTION2_123, 1, 1, 1.0),
        WeightedItem(Items.STRENGTH_POTION2_117, 1, 1, 1.0),
        WeightedItem(Items.DEFENCE_POTION2_135, 1, 1, 1.0),
    )

    override fun defineListeners() {

        /*
         * Handles passages in the maze.
         */

        on(wallIDs, IntType.SCENERY, "open") { player, node ->
            val end = DoorActionHandler.getEndLocation(player, node.asScenery())

            // if(node.id != Scenery.WALL_3628) { // TODO: One-way passages.
            //     sendNPCDialogueLines(player, NPCs.MYSTERIOUS_OLD_MAN_410, FaceAnim.NEUTRAL, false, "That's not the right way.", "Try a different route.")
            //     return@on true
            // }

            DoorActionHandler.open(node.asScenery(), node.asScenery(), Scenery.WALL_3628, 3626, true, 3, false)

            if (node.location == Location(2910, 4576)) // Doors leading to the strange shrine.
                forceMove(player, player.location, end, 0, 30)
            else
                forceWalk(player, end, "")
            return@on true
        }

        /*
         * Handles door lock.
         */

        on(Scenery.WALL_3626, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "That bit doesn't open.")
            return@on false
        }

        /*
         * Handles the interaction with the chest in the maze.
         */

        on(Scenery.CHEST_3635, IntType.SCENERY, "open") { player, node ->
            val ticksLeft = getAttribute(player, GameAttributes.MAZE_ATTRIBUTE_TICKS_LEFT, 0)
            val chestsOpened = getAttribute(player, GameAttributes.MAZE_ATTRIBUTE_CHESTS_OPEN, 0)

            if (ticksLeft > 0 && chestsOpened < 10) {
                animate(player, Animations.OPEN_CHEST_536)
                val reward = mazeDropTable.roll().first()
                val rewardName = getItemName(reward.id).lowercase()
                val message = when (reward.id) {
                    Items.ATTACK_POTION2_123 -> "You've found an attack potion!"
                    Items.STRENGTH_POTION2_117 -> "You've found a strength potion!"
                    Items.DEFENCE_POTION2_135 -> "You've found a defence potion!"
                    else -> "You've found some $rewardName!"
                }
                replaceScenery(node.asScenery(), node.id + 1, -1)
                sendItemDialogue(player, reward.id, message)
                addItemOrBank(player, reward.id, reward.amount)
                player.incrementAttribute(GameAttributes.MAZE_ATTRIBUTE_CHESTS_OPEN, 1)
            } else {
                sendMessage(player, "You find nothing of interest.")
            }
            return@on true
        }

        on(Scenery.CHEST_3636, IntType.SCENERY, "search") { player, _ ->
            if(player.viewport.region!!.id == 11591) {
                sendMessage(player, "You find nothing of interest.")
            }
            return@on true
        }

        /*
         * Handles the interaction with the end point of maze.
         */

        on(Scenery.STRANGE_SHRINE_3634, IntType.SCENERY, "touch") { player, _ ->
            lock(player, 6)
            faceLocation(player, Location.create(2912, 4576, 0))
            animate(player, Animations.CHEER_862)
            playJingle(player, 117)
            queueScript(player, 6, QueueStrength.SOFT) {
                calculateLoot(player)
                clearLogoutListener(player, RandomEvent.logout())
                player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
                removeAttributes(
                    player,
                    RandomEvent.save(),
                    GameAttributes.MAZE_ATTRIBUTE_TICKS_LEFT,
                    GameAttributes.MAZE_ATTRIBUTE_CHESTS_OPEN
                )
                closeOverlay(player)
                return@queueScript stopExecuting(player)
            }
            return@on true
        }
    }
}
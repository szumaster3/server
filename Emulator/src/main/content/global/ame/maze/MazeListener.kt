package content.global.ame.maze

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class MazeListener : InteractionListener {
    private val rewardIDs = intArrayOf(Items.COINS_995, Items.FEATHER_314, Items.IRON_ARROW_884, Items.CHAOS_RUNE_562, Items.STEEL_ARROW_886, Items.DEATH_RUNE_560, Items.COAL_454, Items.NATURE_RUNE_561, Items.MITHRIL_ORE_448)
    private val itemsDivisor = arrayOf(1.0, 2.0, 3.0, 9.0, 12.0, 18.0, 45.0, 162.0, 180.0)

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
        on(Scenery.CHEST_3635, IntType.SCENERY, "open") { player, _ ->
            val ticksLeft = getAttribute(player, GameAttributes.MAZE_ATTRIBUTE_TICKS_LEFT, 0)
            val chestsOpened = getAttribute(player, GameAttributes.MAZE_ATTRIBUTE_CHESTS_OPEN, 0)

            if (ticksLeft > 0 && chestsOpened < 10) {
                animate(player, Animations.OPEN_CHEST_536)
                val reward = mazeDropTable.roll().first()
                val message = when (reward.id) {
                    Items.ATTACK_POTION2_123 -> "You've found an attack potion!"
                    Items.STRENGTH_POTION2_117 -> "You've found a strength potion!"
                    Items.DEFENCE_POTION2_135 -> "You've found a defence potion!"
                    else -> "You've found some ${getItemName(reward.id).lowercase()}!"
                }
                addItemOrBank(player, reward.id)
                sendItemDialogue(player, reward.id, message)
                player.incrementAttribute(GameAttributes.MAZE_ATTRIBUTE_CHESTS_OPEN, 1)
            } else {
                sendMessage(player, "You find nothing of interest.")
            }
            return@on true
        }

        on(Scenery.STRANGE_SHRINE_3634, IntType.SCENERY, "touch") { player, _ ->
            runTask(player, 2) {
                calculateLoot(player)
                clearLogoutListener(player, RandomEvent.logout())
                removeAttributes(player, GameAttributes.MAZE_ATTRIBUTE_TICKS_LEFT, GameAttributes.MAZE_ATTRIBUTE_CHESTS_OPEN)
                player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
                closeOverlay(player)
            }
            return@on true
        }
    }
}
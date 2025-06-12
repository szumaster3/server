package content.region.morytania.port_phasmatys.plugin

import core.api.quest.isQuestComplete
import core.api.removeItem
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.Quests

/**
 * Handles movement through energy barriers.
 */
object EnergyBarrier {
    private val TOKEN_COST = Item(Items.ECTO_TOKEN_4278, 2)
    private val NORTH_GATE = Location(3659, 3508, 0)
    private val SOUTH_GATE = Location(3652, 3485, 0)

    /**
     * Checks if the player can pass without a token based on location.
     */
    private fun shouldForcePass(player: Player, node: Node): Boolean {
        return (node.location == NORTH_GATE && player.location.y < 3508) ||
               (node.location == SOUTH_GATE && player.location.x > 3652)
    }

    /**
     * Gets the destination location after passing the gate.
     */
    private fun getEndLocation(player: Player, node: Node, byTalk: Boolean): Location {
        val p = player.location
        val n = node.location

        if (byTalk) {
            return when (p) {
                Location(3661, 3509, 0) -> n.transform(-1, -1, 0)
                Location(3658, 3509, 0),
                Location(3651, 3487, 0) -> n.transform(1, -1, 0)
                Location(3651, 3484, 0) -> n.transform(1, 1, 0)
                else -> n.transform(Direction.getLogicalDirection(p, n), 2)
            }
        }

        if (!byTalk) {
            return when {
                p.y >= 3508 && n == NORTH_GATE -> n.transform(0, -1, 0)
                n == SOUTH_GATE -> {
                    val offsetX = if (p.x >= 3653) -1 else 1
                    n.transform(offsetX, 0, 0)
                }
                else -> p.transform(Direction.getLogicalDirection(p, n), 2)
            }
        }

        return n // fallback.
    }

    /**
     * Handles movement animation if conditions are met.
     */
    private fun walkThrough(player: Player, node: Node, condition: Boolean, byTalk: Boolean) {
        if (condition) {
            val end = getEndLocation(player, node, byTalk)
            content.global.skill.agility.AgilityHandler.walk(
                player, -1, player.location, end, null, 0.0, null
            )
        }
    }

    /**
     * Passes the barrier using tokens or forced location.
     */
    fun passGate(player: Player, node: Node) {
        val canPass = shouldForcePass(player, node) || removeItem(player, TOKEN_COST)
        walkThrough(player, node, canPass, byTalk = false)
    }

    /**
     * Passes the barrier after talking to a ghost NPC, using tokens.
     */
    fun passGateByTalk(player: Player, node: Node) {
        val canPass = removeItem(player, TOKEN_COST)
        walkThrough(player, node, canPass, byTalk = true)
    }

    /**
     * Passes the barrier for players who completed Ghosts Ahoy or meet force conditions.
     */
    fun passGateAfterQuest(player: Player, node: Node) {
        val canPass = shouldForcePass(player, node) || isQuestComplete(player, Quests.GHOSTS_AHOY)
        walkThrough(player, node, canPass, byTalk = false)
    }
}
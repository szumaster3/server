package content.global.skill.construction.decoration.combatroom

import content.global.skill.agility.shortcuts.StileShortcut.Companion.getInteractLocation
import content.global.skill.agility.shortcuts.StileShortcut.Companion.getOrientation
import core.api.animationCycles
import core.api.forceMove
import core.api.inEquipment
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class CombatRingSpace : InteractionListener {
    private val combatRings =
        intArrayOf(
            Scenery.BOXING_RING_13129,
            Scenery.FENCING_RING_13133,
            Scenery.COMBAT_RING_13137,
        )

    override fun defineListeners() {
        on(combatRings, IntType.SCENERY, "climb-over") { player, node ->
            handleClimbOver(player, node)
            return@on true
        }
        setDest(IntType.SCENERY, combatRings, "climb-over") { player, node ->
            return@setDest getInteractLocation(player.location, node.location, getOrientation(node.direction))
        }
    }

    private fun handleClimbOver(
        player: Player,
        node: Node,
    ) {
        val direction =
            when (node.direction) {
                Direction.WEST -> Direction.SOUTH
                Direction.SOUTH -> Direction.EAST
                Direction.EAST -> Direction.NORTH
                else -> Direction.WEST
            }
        val destination = player.location.transform(direction, 1)
        val animation = setAnimation(player)
        player.walkingQueue.reset()
        forceMove(player, player.location, destination, 0, animationCycles(animation), direction, animation)
    }

    private fun setAnimation(player: Player): Int {
        return when {
            inEquipment(player, Items.BOXING_GLOVES_7671) -> Animations.HUMAN_JUMP_RING_RED_GLOVES_3689
            inEquipment(
                player,
                Items.BOXING_GLOVES_7673,
            ) -> Animations.JUMP_OVER_BOXING_RING_FENCE_WITH_BLUE_GLOVES_3690
            else -> Animations.HUMAN_JUMP_BOXING_RING_3688
        }
    }
}

package content.global.skill.construction.decoration.combatroom

import core.api.animationCycles
import core.api.forceMove
import core.api.inEquipment
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class CombatRingPlugin : InteractionListener {

    override fun defineListeners() {
        on(COMBAT_RING_FURNITURE_IDS, IntType.SCENERY, "climb-over") { player, node ->
            climbOver(player, node)
            return@on true
        }

        setDest(IntType.SCENERY, COMBAT_RING_FURNITURE_IDS, "climb-over") { player, node ->
            return@setDest getInteractLocation(player.location, node.location, getOrientation(node.direction))
        }
    }

    private fun climbOver(player: Player, node: Node) {
        val destination = getInteractLocation(player.location, node.location, getOrientation(node.direction))
        val animation = getJumpAnimation(player)
        player.walkingQueue.reset()
        forceMove(player, player.location, destination, 0, animationCycles(animation), null, animation)
    }

    private fun getJumpAnimation(player: Player): Int = when {
        inEquipment(player, Items.BOXING_GLOVES_7671) -> Animations.HUMAN_JUMP_RING_RED_GLOVES_3689
        inEquipment(player, Items.BOXING_GLOVES_7673) -> Animations.HUMAN_JUMP_RING_BLUE_GLOVES_3690
        else -> Animations.HUMAN_JUMP_BOXING_RING_3688
    }

    private fun getInteractLocation(pLoc: Location, sLoc: Location, orientation: Orientation): Location =
        when (orientation) {
            Orientation.HORIZONTAL -> if (pLoc.x <= sLoc.x) sLoc.transform(-1, 0, 0) else sLoc.transform(2, 0, 0)
            Orientation.VERTICAL -> if (pLoc.y <= sLoc.y) sLoc.transform(0, -1, 0) else sLoc.transform(0, 2, 0)
        }

    private fun getOrientation(rotation: Direction): Orientation =
        when (rotation) {
            Direction.EAST, Direction.WEST -> Orientation.HORIZONTAL
            else -> Orientation.VERTICAL
        }

    private enum class Orientation { HORIZONTAL, VERTICAL }

    companion object {
        private val COMBAT_RING_FURNITURE_IDS = intArrayOf(
            Scenery.BOXING_RING_13129,
            Scenery.FENCING_RING_13133,
            Scenery.COMBAT_RING_13137
        )
    }
}

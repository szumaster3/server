package content.global.activity.ttrail.plugin

import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Items

/**
 * Represents a sliding puzzle boxes.
 * @author szu
 */
enum class PuzzleBox(
    val key: String,
    val item: Item,
    val solutionTiles: List<Int>
) {
    TROLL("troll", Item(Items.PUZZLE_BOX_3571), (Items.SLIDING_PIECE_3643..Items.SLIDING_PIECE_3666).toList()),
    CASTLE("castle", Item(Items.PUZZLE_BOX_2795), (Items.SLIDING_PIECE_2749..Items.SLIDING_PIECE_2772).toList()),
    TREE("tree", Item(Items.PUZZLE_BOX_3565), (Items.SLIDING_PIECE_3619..Items.SLIDING_PIECE_3642).toList());

    val fullSolution: List<Int> = solutionTiles.plus(-1)

    companion object {
        fun fromKey(key: String): PuzzleBox? = values().find { it.key == key }

        fun fromItemId(itemId: Int): PuzzleBox? = values().find { it.item.id == itemId }

        fun getRandomPuzzleBox(): Int = values().random().item.id

        fun hasCompletePuzzleBox(player: Player, key: String): Boolean {
            val box = fromKey(key) ?: return false
            return player.getAttribute("$key:puzzle:done", false) && player.inventory.contains(box.item.id, 1)
        }
    }
}
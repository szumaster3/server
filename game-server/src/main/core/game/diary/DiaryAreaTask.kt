package core.game.diary

import core.api.inBorders
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders

/**
 * Represents a diary task.
 *
 * @property zoneBorders Area where the task applies.
 * @property diaryLevel Diary level this task belongs to.
 * @property taskId Unique task identifier.
 * @property condition Optional predicate that must be true for completion.
 */
class DiaryAreaTask(
    val zoneBorders: ZoneBorders,
    val diaryLevel: DiaryLevel,
    val taskId: Int,
    private val condition: ((Player) -> Boolean)? = null,
) {
    /**
     * Executes [then] if player is inside [zoneBorders] and meets [condition] (if any).
     */
    fun whenSatisfied(player: Player, then: () -> Unit) {
        val inZone = inBorders(player, zoneBorders)
        val meetsCondition = condition?.invoke(player) ?: true
        if (inZone && meetsCondition) then()
    }
}
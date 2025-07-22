package content.data.consumables.effects

import core.api.finishDiaryTask
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType

/**
 * A consumable effect that completes a specific Achievement Diary task
 * when the item is used by the player.
 *
 * @property diary The type of diary (e.g. Varrock, Ardougne).
 * @property level The difficulty level of the diary (e.g. easy, medium).
 * @property task The specific task ID to complete.
 */
class AchievementEffect(
    var diary: DiaryType,
    var level: Int,
    var task: Int,
) : ConsumableEffect() {

    /**
     * Activates the effect by finishing the diary task for the player.
     *
     * @param player The player using the consumable.
     */
    override fun activate(player: Player) {
        finishDiaryTask(player, diary, level, task)
    }
}

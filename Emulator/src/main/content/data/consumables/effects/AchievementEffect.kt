package content.data.consumables.effects

import core.api.finishDiaryTask
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType

class AchievementEffect(
    var diary: DiaryType,
    var level: Int,
    var task: Int,
) : ConsumableEffect() {
    override fun activate(player: Player) {
        finishDiaryTask(player, diary, level, task)
    }
}

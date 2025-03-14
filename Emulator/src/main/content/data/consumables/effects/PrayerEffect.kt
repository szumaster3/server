package content.data.consumables.effects

import core.api.getStatLevel
import core.api.inInventory
import core.api.modPrayerPoints
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs.consts.Items
import kotlin.math.floor

class PrayerEffect(
    var base: Double,
    var bonus: Double,
) : ConsumableEffect() {
    override fun activate(player: Player) {
        val level = getStatLevel(player, Skills.PRAYER)
        var b = bonus
        if (inInventory(player, Items.HOLY_WRENCH_6714)) b += 0.02
        val amount = floor(base + (level * b))
        modPrayerPoints(player, amount)
    }
}

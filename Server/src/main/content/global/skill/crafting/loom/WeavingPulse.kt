package content.global.skill.crafting.loom

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.tools.StringUtils
import shared.consts.Animations
import shared.consts.Sounds

/**
 * Handles weaving crafting pulse.
 */
class WeavingPulse(player: Player?, node: Scenery?, private val type: Weaving, private var amount: Int) : SkillPulse<Scenery?>(player, node) {

    private val required = type.required.name.lowercase().replace("ball", "balls")
    private val article = if (StringUtils.isPlusN(type.product.name.lowercase())) "an" else "a"
    private val suffix = when (type) {
        Weaving.SACK -> "s"
        Weaving.CLOTH -> ""
        else -> "es"
    }

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < type.level) {
            sendMessage(player, "You need a crafting level of at least ${type.level} to do this.")
            return false
        }
        if (!inInventory(player, type.required.id, type.required.amount)) {
            sendMessage(player, "You need ${type.required.amount} $required$suffix to weave $article ${type.product.name.lowercase()}.")
            return false
        }

        return true
    }

    override fun animate() {
        animate(player, ANIMATION)
        playAudio(player, Sounds.LOOM_WEAVE_2587, 1)
    }

    override fun reward(): Boolean {
        if (!clockReady(player, Clocks.SKILLING)) return false
        delayClock(player, Clocks.SKILLING, 5)

        if (removeItem(player, type.required)) {
            player.inventory.add(type.product)
            rewardXP(player, Skills.CRAFTING, type.experience)

            sendMessage(player, "You weave the $required$suffix into $article ${type.product.name.lowercase()}.")

            if (type == Weaving.BASKET && node?.id == 8717 &&
                withinDistance(player, Location(3039, 3287, 0)) &&
                !hasDiaryTaskComplete(player, DiaryType.FALADOR, 1, 0)
                ) {
                finishDiaryTask(player, DiaryType.FALADOR, 1, 0)
            }
        }

        amount--
        return amount < 1
    }

    companion object {
        private const val ANIMATION = Animations.PULLING_ROPE_2270
    }
}
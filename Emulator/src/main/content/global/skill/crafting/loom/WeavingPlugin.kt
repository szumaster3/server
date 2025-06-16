package content.global.skill.crafting.loom

import core.api.*
import core.api.ui.repositionChild
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.tools.StringUtils
import org.rs.consts.Animations
import org.rs.consts.Components

class WeavingPlugin : InteractionListener {

    override fun defineListeners() {

        on(IntType.SCENERY, "weave") { player, node ->
            object : SkillDialogueHandler(player, SkillDialogue.THREE_OPTION, Weaving.SACK.product, Weaving.BASKET.product, Weaving.CLOTH.product) {
                override fun create(amount: Int, index: Int, ) {
                    submitIndividualPulse(
                        entity = player,
                        pulse = WeavingPulse(player, node.asScenery(), Weaving.values()[index], amount),
                    )
                }
            }.open()
            repositionChild(player, Components.SKILL_MAKE_304, 2, 56, 32)
            repositionChild(player, Components.SKILL_MAKE_304, 3, 207, 32)
            return@on true
        }
    }

}

/**
 * Handles weaving pulse.
 */
private class WeavingPulse(player: Player?, node: Scenery?, private val type: Weaving, private var amount: Int, ) : SkillPulse<Scenery?>(player, node) {
    private var ticks = 0

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < type.level) {
            sendMessage(player, "You need a crafting level of at least " + type.level + " in order to do this.")
            return false
        }
        if (!inInventory(player, type.required.id)) {
            sendMessage(
                player,
                "You need " + type.required.amount + " " +
                        type.required.name.lowercase().replace(
                            "ball",
                            "balls",
                        ) + (
                        if (type == Weaving.SACK) {
                            "s"
                        } else if (type == Weaving.CLOTH) {
                            ""
                        } else {
                            "es"
                        }
                        ) + " to weave " +
                        (
                                if (StringUtils.isPlusN(
                                        type.product.name.lowercase(),
                                    )
                                ) {
                                    "an"
                                } else {
                                    "a"
                                }
                                ) + " " + type.product.name.lowercase() + ".",
            )
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0) {
            animate(player, ANIMATION)
        }
    }

    override fun reward(): Boolean {
        if (++ticks % 5 != 0) {
            return false
        }
        if (removeItem(player, type.required)) {
            player.inventory.add(type.product)
            rewardXP(player, Skills.CRAFTING, type.experience)
            sendMessage(
                player,
                "You weave the " +
                        type.required.name
                            .lowercase()
                            .replace("ball", "balls") +
                        (
                                if (type == Weaving.SACK) {
                                    "s"
                                } else if (type == Weaving.CLOTH) {
                                    ""
                                } else {
                                    "es"
                                }
                                ) +
                        " into " + (if (StringUtils.isPlusN(type.product.name.lowercase())) "an" else "a") +
                        " " + type.product.name.lowercase() + ".",
            )
            if (type == Weaving.BASKET &&
                node?.id == 8717 &&
                withinDistance(
                    player,
                    Location(3039, 3287, 0),
                ) &&
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

package content.global.handlers.item

import content.global.travel.glider.Glider
import core.api.*
import core.api.ui.setMinimapState
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.rs.consts.Items

private const val SQUASH_GRAPHICS_BEGIN = 767
private const val SQUASH_GRAPHICS_END = 769
private const val SQUASH_ANIM_BEGIN = 4544
private const val SQUASH_ANIM_END = 4546
private const val LAUNCH_GRAPHICS = 768
private const val LAUNCH_ANIMATION = 4547

class GrandSeedPodListener : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Items.GRAND_SEED_POD_9469), IntType.ITEM, "squash", "launch") { player, _ ->
            val opt = getUsedOption(player)
            if (!removeItem(player, Items.GRAND_SEED_POD_9469)) return@on false
            if (opt == "launch") {
                visualize(player, LAUNCH_ANIMATION, LAUNCH_GRAPHICS)
                delayEntity(player, 7)
                queueScript(player, 3, QueueStrength.SOFT) { stage: Int ->
                    if (stage == 0) {
                        rewardXP(player, Skills.FARMING, 100.0)
                        openOverlay(player, 115)
                        return@queueScript keepRunning(player)
                    }

                    if (stage == 1) {
                        setMinimapState(player, 2)
                        return@queueScript delayScript(player, 3)
                    }

                    if (stage == 2) {
                        teleport(player, Glider.TA_QUIR_PRIW.location)
                        return@queueScript delayScript(player, 2)
                    }

                    if (stage == 3) {
                        closeOverlay(player)
                        setMinimapState(player, 0)
                    }

                    return@queueScript stopExecuting(player)
                }
            }

            if (opt == "squash") {
                visualize(player, SQUASH_ANIM_BEGIN, SQUASH_GRAPHICS_BEGIN)
                delayEntity(player, 12)
                queueScript(player, 3, QueueStrength.SOFT) { stage: Int ->
                    if (stage == 0) {
                        animate(player, 1241, true)
                        return@queueScript keepRunning(player)
                    }

                    if (stage == 1) {
                        teleport(player, Location.create(2464, 3494, 0))
                        return@queueScript keepRunning(player)
                    }

                    if (stage == 2) {
                        visualize(player, 1241, SQUASH_GRAPHICS_END)
                        return@queueScript delayScript(player, 2)
                    }

                    if (stage == 3) {
                        animate(player, SQUASH_ANIM_END, true)
                        adjustLevel(player, Skills.FARMING, -5)
                        return@queueScript keepRunning(player)
                    }

                    return@queueScript stopExecuting(player)
                }
            }

            return@on true
        }
    }
}

package content.global.skill.construction.decoration.bedroom

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.node.scenery.SceneryBuilder
import core.game.world.update.flag.context.Animation
import core.tools.RandomUtils
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class FireplacePlugin : InteractionListener {

    override fun defineListeners() {
        on(FIREPLACE_IDS, IntType.SCENERY, "light") { player, node ->
            if(player.houseManager.isBuildingMode) {
                sendMessage(player, "You cannot do this in building mode.")
                return@on true
            }
            if (!anyInInventory(player, Items.LOGS_1511, Items.TINDERBOX_590)) {
                sendMessage(player, "You need some logs and a tinderbox in order to light the fireplace.")
                return@on true
            }
            if (!inInventory(player, Items.LOGS_1511, 1)) {
                sendMessage(player, "You need some logs in order to light the fireplace.")
                return@on true
            }
            if (!inInventory(player, Items.TINDERBOX_590, 1)) {
                sendMessage(player, "You need a tinderbox in order to light the fireplace.")
                return@on true
            }

            val obj = node.asScenery()
            player.lock(2)
            player.animate(ANIMATION)

            queueScript(player, 2, QueueStrength.SOFT)
            {
                if (!obj.isActive) {
                    return@queueScript stopExecuting(player)
                }
                removeItem(player, Items.LOGS_1511)
                rewardXP(player, Skills.FIREMAKING, 80.0)
                val durationTicks = 70 + RandomUtils.random(20)
                SceneryBuilder.replace(
                    core.game.node.scenery.Scenery(obj.id, obj.location),
                    core.game.node.scenery.Scenery(obj.id + 1, obj.location, obj.rotation),
                    durationTicks
                )
                sendMessage(player, "You light the fireplace.")
                return@queueScript stopExecuting(player)
            }

            return@on true
        }
    }

    companion object {
        private val ANIMATION: Animation = Animation.create(Animations.TINDERBOX_3658)
        private val FIREPLACE_IDS = intArrayOf(Scenery.CLAY_FIREPLACE_13609, Scenery.LIMESTONE_FIREPLACE_13611, Scenery.MARBLE_FIREPLACE_13613)
    }
}

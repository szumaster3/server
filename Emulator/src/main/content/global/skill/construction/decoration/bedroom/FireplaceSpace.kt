package content.global.skill.construction.decoration.bedroom

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.SceneryBuilder
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class FireplaceSpace : InteractionListener {
    private val animationId = Animations.TINDERBOX_3658
    private val fireplaceSpaceFurniture =
        intArrayOf(Scenery.CLAY_FIREPLACE_13609, Scenery.LIMESTONE_FIREPLACE_13611, Scenery.MARBLE_FIREPLACE_13613)

    override fun defineListeners() {
        on(fireplaceSpaceFurniture, IntType.SCENERY, "light") { player, node ->
            val n = node.asScenery()

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

            lock(player, 3)
            animate(player, animationId)
            queueScript(player, 2, QueueStrength.SOFT) {
                if (!node.isActive) {
                    return@queueScript stopExecuting(player)
                }
                removeItem(player, Item(Items.LOGS_1511, 1), Container.INVENTORY)
                rewardXP(player, Skills.FIREMAKING, 80.0)
                SceneryBuilder.replace(
                    core.game.node.scenery
                        .Scenery(n.id, n.location),
                    core.game.node.scenery
                        .Scenery(n.id + 1, n.location, n.rotation),
                    1000,
                )
                sendMessage(player, "You light the fireplace.")
                return@queueScript stopExecuting(player)
            }
            return@on true
        }
    }
}

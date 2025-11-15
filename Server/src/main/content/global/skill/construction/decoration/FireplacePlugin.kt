package content.global.skill.construction.decoration

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomUtils
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery as Obj

/**
 * Handles interaction with fireplace.
 */
@Initializable
class FireplacePlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(Obj.CLAY_FIREPLACE_13609        ).handlers["option:light"] = this
        SceneryDefinition.forId(Obj.LIMESTONE_FIREPLACE_13611   ).handlers["option:light"] = this
        SceneryDefinition.forId(Obj.MARBLE_FIREPLACE_13613      ).handlers["option:light"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if(player.houseManager.isBuildingMode) {
            sendMessage(player, "You cannot do this in building mode.")
            return true
        }
        if (!anyInInventory(player, Items.LOGS_1511, Items.TINDERBOX_590)) {
            sendMessage(player, "You need some logs and a tinderbox in order to light the fireplace.")
            return true
        }
        if (!inInventory(player, Items.LOGS_1511, 1)) {
            sendMessage(player, "You need some logs in order to light the fireplace.")
            return true
        }
        if (!inInventory(player, Items.TINDERBOX_590, 1)) {
            sendMessage(player, "You need a tinderbox in order to light the fireplace.")
            return true
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
                Scenery(obj.id, obj.location),
                Scenery(obj.id + 1, obj.location, obj.rotation),
                durationTicks
            )
            sendMessage(player, "You light the fireplace.")
            return@queueScript stopExecuting(player)
        }

        return true
    }

    companion object {
        private val ANIMATION: Animation = Animation.create(Animations.TINDERBOX_3658)
    }
}
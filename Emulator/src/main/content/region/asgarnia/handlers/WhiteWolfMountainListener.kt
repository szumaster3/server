package content.region.asgarnia.handlers

import content.data.items.SkillingTool
import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import org.rs.consts.Scenery

class WhiteWolfMountainListener : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles interaction with scenery block for snow queen.
         */

        on(Scenery.ROCK_SLIDE_2634, SCENERY, "mine", "investigate") { player, node ->
            val option = getUsedOption(player)
            if (option == "investigate") {
                sendMessage(player, "These rocks contain nothing interesting.")
                sendMessage(player, "They are just in the way.")
                return@on true
            }

            val pickaxe = SkillingTool.getPickaxe(player)
            if (getDynLevel(player, Skills.MINING) < 50) {
                sendMessage(player, "You need a mining level of 50 to mine this rock slide.")
                return@on true
            }
            if (pickaxe == null) {
                sendMessage(player, "You do not have a pickaxe to use.")
                return@on true
            }

            animate(player, pickaxe.animation)
            lock(player, 7)

            val rockScenery = node as core.game.node.scenery.Scenery
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        replaceScenery(rockScenery, Scenery.ROCKSLIDE_472, 3)
                        return@queueScript delayScript(player, 3)
                    }
                    1 -> {
                        replaceScenery(rockScenery, Scenery.ROCKSLIDE_473, 3)
                        return@queueScript delayScript(player, 2)
                    }
                    2 -> {
                        replaceScenery(rockScenery, 476, 3)
                        player.walkingQueue.reset()
                        val x = player.location.x
                        val y = player.location.y
                        val nextX = if (x < 2839) 2840 else 2837
                        val nextY = if (x < 2839) 3517 else 3518
                        player.walkingQueue.addPath(nextX, nextY, true)
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }
    }
}

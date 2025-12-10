package content.region.asgarnia.white_wolf_mountain.plugin

import content.data.skill.SkillingTool
import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import shared.consts.Scenery

class WhiteWolfMountainPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles interaction with scenery block for snow queen.
         */

        on(Scenery.ROCK_SLIDE_2634, SCENERY, "mine", "investigate") { player, node ->
            val option = getUsedOption(player)
            val pickaxe = SkillingTool.getPickaxe(player)
            val rockScenery = node as core.game.node.scenery.Scenery

            if (option == "investigate") {
                sendMessage(player, "These rocks contain nothing interesting.")
                sendMessage(player, "They are just in the way.")
                return@on true
            }

            if (getDynLevel(player, Skills.MINING) < 50) {
                sendMessage(player, "You need a mining level of 50 to mine this rock slide.")
                return@on true
            }

            if (pickaxe == null) {
                sendMessage(player, "You do not have a pickaxe to use.")
                return@on true
            }

            lock(player, 6)
            animate(player, pickaxe.animation)
            queueScript(player, 1, QueueStrength.SOFT) { stage ->
                when (stage) {
                    0 -> {
                        replaceScenery(rockScenery, Scenery.ROCKSLIDE_472, 2)
                        return@queueScript delayScript(player, 2)
                    }
                    1 -> {
                        replaceScenery(rockScenery, Scenery.ROCKSLIDE_473, 2)
                        return@queueScript delayScript(player, 2)
                    }
                    2 -> {
                        player.walkingQueue.reset()
                        replaceScenery(rockScenery, 476, 4)
                        if (player.location.x < 2839) {
                            player.walkingQueue.addPath(2840, 3517, true)
                        } else {
                            player.walkingQueue.addPath(2837, 3518, true)
                        }
                        return@queueScript delayScript(player, 2)
                    }
                    else ->  return@queueScript stopExecuting(player)
                }
            }

            return@on true
        }
    }
}
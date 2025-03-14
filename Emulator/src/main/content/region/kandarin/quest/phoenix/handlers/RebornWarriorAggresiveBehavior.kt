package content.region.kandarin.quest.phoenix.handlers

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.Location
import core.tools.RandomFunction

class RebornWarriorAggresiveBehavior : NPCBehavior(*rebornWarriors) {
    override fun tick(self: NPC): Boolean {
        super.tick(self)
        if (RandomFunction.random(300) < 3) {
            lock(self, 3)
            stopWalk(self)
            self.impactHandler.disabledTicks = 3
            queueScript(self, 0, QueueStrength.STRONG) { stage ->
                when (stage) {
                    0 -> {
                        visualize(self, 11133, 1990)
                        teleport(
                            self,
                            Location.getRandomLocation(self.location, 5, true),
                        )
                        return@queueScript delayScript(self, 6)
                    }

                    1 -> {
                        visualize(self, 11136, 1991)
                        return@queueScript stopExecuting(self)
                    }

                    else -> return@queueScript stopExecuting(self)
                }
            }
        }
        return true
    }
}

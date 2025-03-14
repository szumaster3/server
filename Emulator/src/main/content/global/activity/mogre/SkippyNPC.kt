package content.global.activity.mogre

import core.api.*
import core.api.movement.finishedMoving
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.Graphics
import org.rs.consts.NPCs
import org.rs.consts.Sounds

class SkippyNPC : NPCBehavior(NPCs.SKIPPY_2795) {
    private val forceChat =
        arrayOf(
            "I'll get you, I'll get you all!",
            "The horror...The horror...",
            "They're coming out of the walls!",
            "Mudskippers, thousands of them!",
            "I've got a bottle with your name on it!",
        )

    override fun onCreation(self: NPC) {
        val route =
            arrayOf(
                Location.create(2980, 3198, 0),
                Location.create(2973, 3193, 0),
                Location.create(2977, 3196, 0),
                Location.create(2984, 3192, 0),
                Location.create(2982, 3196, 0),
            )
        self.configureMovementPath(*route)
        self.isWalks = !inBorders(self, SkippyUtils.TUTORIAL_ISLAND)
        self.isNeverWalks = !self.isWalks
    }

    override fun tick(self: NPC): Boolean {
        if (sendTutorialChat(self)) {
            return true
        }

        if (randomRoll(self)) {
            handleThrow(self)
        }
        return super.tick(self)
    }

    private fun sendTutorialChat(self: NPC): Boolean {
        return RandomFunction.random(100) < 10 &&
            inBorders(self, SkippyUtils.TUTORIAL_ISLAND).also {
                if (it) sendChat(self, "You can skip the tutorial by talking to me!")
            }
    }

    private fun randomRoll(self: NPC): Boolean {
        return RandomFunction.random(100) < 5 && inBorders(self, SkippyUtils.PORT_SARIM)
    }

    private fun handleThrow(self: NPC) {
        if (!finishedMoving(self)) return
        lockMovement(self, 1)
        queueScript(self, 0, QueueStrength.SOFT) {
            sendChat(self, "Take this")
            animate(self, SkippyUtils.ANIMATION_THROW)
            faceLocation(self, self.location.transform(Direction.SOUTH))
            playGlobalAudio(self.location, Sounds.SKIPPY_THROWGLASS_1398, 1)
            spawnProjectile(
                self.location,
                self.location.transform(Direction.SOUTH, 4),
                Graphics.THROWING_VIAL_49,
                30,
                20,
                10,
                100,
                0,
            )
            return@queueScript stopExecuting(self)
        }
    }
}

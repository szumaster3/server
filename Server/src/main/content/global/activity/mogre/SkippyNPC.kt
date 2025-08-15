package content.global.activity.mogre

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Graphics
import shared.consts.NPCs
import shared.consts.Sounds

/**
 * Represents Skippy NPC for Mogre lore activity.
 * @author szu
 */
class SkippyNPC : NPCBehavior(NPCs.SKIPPY_2795) {
    private val forceChat = arrayOf(
        "I'll get you, I'll get you all!",
        "The horror...The horror...",
        "They're coming out of the walls!",
        "Mudskippers, thousands of them!",
        "I've got a bottle with your name on it!",
    )

    private val route = arrayOf(
        Location.create(2980, 3198, 0),
        Location.create(2973, 3193, 0),
        Location.create(2977, 3196, 0),
        Location.create(2984, 3192, 0),
        Location.create(2982, 3196, 0),
    )

    private var ticks = 0
    private val TICK_INTERVAL = 6

    override fun onCreation(self: NPC) {
        self.configureMovementPath(*route)
        self.isWalks = true
        self.isNeverWalks = false
    }

    override fun tick(self: NPC): Boolean {
        if (!self.isWalks && !self.locks.isMovementLocked()) {
            self.locks.lockMovement(RandomFunction.random(4, 6))

            if (RandomFunction.roll(2)) {
                sendChat(self, forceChat.random())
            }
        }

        ticks++
        if (ticks >= TICK_INTERVAL) {
            ticks = 0
            if (RandomFunction.roll(3)) {
                sendChat(self, forceChat.random())
                handleThrow(self)
            }
        }

        return super.tick(self)
    }

    private fun handleThrow(self: NPC) {
        if (!finishedMoving(self)) return
        self.walkingQueue.reset()
        self.locks.lockMovement(3)
        queueScript(self, 1, QueueStrength.SOFT) {
            sendChat(self, "Take this")
            animate(self, Animations.THROW_385)
            faceLocation(self, self.location.transform(Direction.SOUTH))
            playGlobalAudio(self.location, Sounds.SKIPPY_THROWGLASS_1398, 1)
            spawnProjectile(
                self.location,
                self.location.transform(Direction.SOUTH, 8),
                Graphics.THROWING_VIAL_49,
                42,
                32,
                15,
                100,
                0,
            )
            return@queueScript stopExecuting(self)
        }
    }
}

package content.global.activity.mogre

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.NPCs
import org.rs.consts.Sounds

/**
 * Represents Skippy NPC for Mogre lore activity.
 * @author szu
 */
class SkippyNPC : NPCBehavior(NPCs.SKIPPY_2795) {

    private var nextChat = 0L

    private val forceChat = arrayOf(
        "I'll get you, I'll get you all!",
        "The horror...The horror...",
        "They're coming out of the walls!",
        "Mudskippers, thousands of them!",
        "I've got a bottle with your name on it!",
    )

    override fun onCreation(self: NPC) {
        val route = arrayOf(
            Location.create(2980, 3198, 0),
            Location.create(2973, 3193, 0),
            Location.create(2977, 3196, 0),
            Location.create(2984, 3192, 0),
            Location.create(2982, 3196, 0),
        )
        self.configureMovementPath(*route)
        self.isWalks = true
    }

    override fun tick(self: NPC): Boolean {
        if (!isPlayerNearby(self)) return true

        val now = System.currentTimeMillis()
        if (now < nextChat || !self.isPlayerNearby(15)) {
            return true
        }

        if (RandomFunction.roll(8)) {
            sendChat(self, forceChat.random())
            handleThrow(self)
            nextChat = now + 15000L
        }

        return true
    }

    private fun handleThrow(self: NPC) {
        self.walkingQueue.reset()
        queueScript(self, 1, QueueStrength.SOFT) {
            sendChat(self, "Take this")
            animate(self, Animations.THROW_385)
            faceLocation(self, self.location.transform(Direction.SOUTH))
            playGlobalAudio(self.location, Sounds.SKIPPY_THROWGLASS_1398)
            spawnProjectile(
                self.location,
                self.location.transform(Direction.SOUTH, 4),
                Graphics.THROWING_VIAL_49,
                30,
                0,
                10,
                100,
                0,
            )
            return@queueScript stopExecuting(self)
        }
    }
}
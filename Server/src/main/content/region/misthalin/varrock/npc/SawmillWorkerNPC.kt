package content.region.misthalin.varrock.npc

import core.api.finishedMoving
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.NPCs

private val movementPath = arrayOf(
    Location(3316, 3506, 0),
    Location(3317, 3513, 0),
)

class SawmillWorkerNPC : NPCBehavior(NPCs.WILL_7737, NPCs.WILL_7738) {

    private var ticks = 0

    /**
     * Index of the last point for npc performed an action.
     * -1 means that haven't done anything yet.
     */
    private var lastProcessedIndex: Int = -1

    override fun onCreation(self: NPC) {
        self.configureMovementPath(*movementPath)
        self.isWalks = true
        self.isNeverWalks = false
        self.walkRadius = 10
    }

    override fun tick(self: NPC): Boolean {
        ticks++
        if (ticks < 6) return true
        ticks = 0

        if (!finishedMoving(self)) return true

        val loc = self.location

        val idx = movementPath.indexOfFirst { it.sameAs(loc) }
        if (idx == -1) {
            lastProcessedIndex = -1
            return true
        }

        if (idx == lastProcessedIndex) return true
        lastProcessedIndex = idx

        when (idx) {
            0 -> {
                self.transform(NPCs.WILL_7737)
                self.animate(Animation(Animations.HUMAN_BURYING_BONES_827))
                self.sendChat("Erf!", 1)
            }
            1 -> {
                self.reTransform()
                self.animate(Animation(Animations.LUMBER_YARD_EMPLOYEE_9218))
                self.sendChat("Oof!", 2)
            }
        }

        return true
    }

    private fun Location.sameAs(other: Location): Boolean =
        this.x == other.x && this.y == other.y && this.z == other.z
}

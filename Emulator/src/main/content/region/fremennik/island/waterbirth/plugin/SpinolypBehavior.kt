package content.region.fremennik.island.waterbirth.plugin

import core.api.playAudio
import core.api.sendGraphics
import core.api.stopWalk
import core.api.unlock
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.GameWorld
import core.game.world.map.RegionManager
import core.game.world.map.path.ClipMaskSupplier
import core.tools.RandomFunction
import org.rs.consts.Graphics
import org.rs.consts.NPCs
import org.rs.consts.Sounds

/**
 * Behavior for spinolyp and suspicious water NPC.
 */
class SpinolypBehavior : NPCBehavior(NPCs.SPINOLYP_2894, NPCs.SUSPICIOUS_WATER_2895, NPCs.SPINOLYP_2896) {

    private var transformationDelay = 0L
    private var splashDelay: Long = 0L

    override fun onCreation(self: NPC) {
        if (self.id == NPCs.SUSPICIOUS_WATER_2895) {
            self.apply {
                isWalks = true
                isNeverWalks = false
                isAggressive = true
            }
        }
    }

    override fun tick(self: NPC): Boolean {
        val nearbyPlayers = RegionManager.getLocalPlayers(self.location, 4)
        if (nearbyPlayers.isEmpty()) return super.tick(self)

        val currentTick = GameWorld.ticks

        // Suspicious Water transform.
        if (self.id == NPCs.SUSPICIOUS_WATER_2895) {
            if (self.inCombat() || currentTick < transformationDelay) return super.tick(self)

            transformationDelay = currentTick + 20L
            stopWalk(self)
            val spinolypNPCs = listOf(NPCs.SPINOLYP_2894, NPCs.SPINOLYP_2896)
            self.transform(spinolypNPCs.random())
            return true
        }

        // Spinolyp splash effect.
        if (currentTick >= splashDelay) {
            splashDelay = currentTick + RandomFunction.random(300, 500).toLong()

            val targetPlayer = nearbyPlayers.firstOrNull { !self.inCombat() && !it.inCombat() }
            if (targetPlayer != null) {
                unlock(self)
                sendGraphics(Graphics.WATER_SPLASH_68, self.location)
                playAudio(targetPlayer, Sounds.WATERSPLASH_2496)
                self.reTransform()
                self.walkingQueue.update()
                return true
            }
        }

        val targetPlayer = nearbyPlayers.firstOrNull { !self.inCombat() && !it.inCombat() }
        if (targetPlayer != null) {
            self.attack(targetPlayer)
            return true
        }

        return super.tick(self)
    }

    override fun onRespawn(self: NPC) {
        if (self.id in arrayOf(NPCs.SPINOLYP_2894, NPCs.SPINOLYP_2896)) {
            self.transform(NPCs.SUSPICIOUS_WATER_2895)
        }
    }

    override fun getClippingSupplier(self: NPC): ClipMaskSupplier = WaterClipping
}

private object WaterClipping : ClipMaskSupplier {
    override fun getClippingFlag(z: Int, x: Int, y: Int): Int = RegionManager.getWaterClipFlag(z, x, y)
}
package content.region.fremennik.handlers.npc.waterbirth

import core.api.playAudio
import core.api.sendGraphics
import core.api.stopWalk
import core.api.unlock
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.RegionManager
import core.game.world.map.path.ClipMaskSupplier
import core.tools.RandomFunction
import org.rs.consts.Graphics
import org.rs.consts.NPCs
import org.rs.consts.Sounds

class SpinolypBehavior :
    NPCBehavior(
        NPCs.SPINOLYP_2894,
        NPCs.SUSPICIOUS_WATER_2895,
        NPCs.SPINOLYP_2896,
    ) {
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
        val spinolypNPCs = listOf(NPCs.SPINOLYP_2894, NPCs.SPINOLYP_2896)
        val randNPC = spinolypNPCs.random()
        val rand = RandomFunction.random(300, 500)

        for (player in nearbyPlayers) {
            val p = player.asPlayer()

            if (nearbyPlayers.isEmpty()) {
                return super.tick(self)
            }

            when {
                self.id == NPCs.SUSPICIOUS_WATER_2895 -> {
                    stopWalk(self)
                    self.transform(randNPC)
                    return true
                }

                rand == 350 && self.id != NPCs.SUSPICIOUS_WATER_2895 -> {
                    unlock(self)
                    sendGraphics(Graphics.WATER_SPLASH_68, self.location)
                    playAudio(player, Sounds.WATERSPLASH_2496)
                    self.reTransform()
                    self.walkingQueue.update()
                    return true
                }

                else -> {
                    if (self.id != NPCs.SUSPICIOUS_WATER_2895 && !self.inCombat()) {
                        self.attack(p)
                    }
                }
            }
        }

        return super.tick(self)
    }

    override fun getClippingSupplier(self: NPC): ClipMaskSupplier = WaterClipping
}

object WaterClipping : ClipMaskSupplier {
    override fun getClippingFlag(
        z: Int,
        x: Int,
        y: Int,
    ): Int {
        return RegionManager.getWaterClipFlag(z, x, y)
    }
}

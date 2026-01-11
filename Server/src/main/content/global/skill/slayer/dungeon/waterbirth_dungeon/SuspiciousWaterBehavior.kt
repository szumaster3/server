package content.global.skill.slayer.dungeon.waterbirth_dungeon

import core.api.playAudio
import core.api.sendGraphics
import core.api.stopWalk
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.RegionManager
import shared.consts.Graphics
import shared.consts.NPCs
import shared.consts.Sounds

class SuspiciousWaterBehavior : NPCBehavior(NPCs.SUSPICIOUS_WATER_2895) {

    companion object {
        private const val ATTR_TRIGGERED = "suspicious_water:triggered"

        private val SPINOLYPS = intArrayOf(
            NPCs.SPINOLYP_2894,
            NPCs.SPINOLYP_2896
        )
    }

    override fun onCreation(self: NPC) {
        self.attributes.remove(ATTR_TRIGGERED)
    }

    override fun tick(self: NPC): Boolean {
        if (self.attributes[ATTR_TRIGGERED] == true) {
            return false
        }

        val players = RegionManager.getLocalPlayers(self.location, 4)
        if (players.isEmpty()) {
            return false
        }

        val target = players.first()
        self.attributes[ATTR_TRIGGERED] = true

        stopWalk(self)
        sendGraphics(Graphics.WATER_SPLASH_68, self.location)
        playAudio(target, Sounds.WATERSPLASH_2496)

        val spinolypId = SPINOLYPS.random()


        self.clear()

        val spinolyp = SpinolypNPC(spinolypId, self.location)
        spinolyp.init()
        spinolyp.isAggressive = true
        spinolyp.aggressiveHandler.selectTarget()

        return true
    }
}

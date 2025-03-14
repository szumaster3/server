package content.global.ame.swarm

import content.global.ame.RandomEventNPC
import core.api.playGlobalAudio
import core.api.utils.WeightBasedTable
import core.api.withinDistance
import core.game.node.entity.npc.NPC
import core.tools.minutesToTicks
import org.rs.consts.NPCs
import org.rs.consts.Sounds

class SwarmNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.SWARM_411) {
    override fun init() {
        super.init()
        this.setAttribute("no-spawn-return", true)
        this.attack(player)
        playGlobalAudio(this.location, Sounds.SWARM_APPEAR_818)
        this.ticksLeft = minutesToTicks(60)
    }

    override fun tick() {
        super.tick()
        if (!this.inCombat()) {
            this.attack(player)
        }
        if (!withinDistance(player, this.location, 8)) {
            this.terminate()
        }
        if (!player.viewport.currentPlane.npcs
                .contains(this)
        ) {
            this.clear()
        }
    }

    override fun talkTo(npc: NPC) {
    }
}

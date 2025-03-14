package content.global.ame.shade

import content.global.ame.RandomEventNPC
import core.api.utils.WeightBasedTable
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs
import kotlin.math.max
import kotlin.math.min

class ShadeNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.SHADE_425) {
    val ids = (425..430).toList()

    override fun talkTo(npc: NPC) {}

    override fun init() {
        super.init()
        val index = max(0, min(ids.size, (player.properties.combatLevel / 20) - 1))
        val id = ids[index]
        this.transform(id)
        this.attack(player)
        sendChat("Leave this place!")
        this.isRespawn = false
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
    }

    override fun tick() {
        if (!player.location.withinDistance(this.location, 8)) {
            this.terminate()
        }
        super.tick()
        if (!player.viewport.currentPlane.npcs
                .contains(this)
        ) {
            this.clear()
        }
    }
}

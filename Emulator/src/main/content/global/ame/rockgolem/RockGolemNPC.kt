package content.global.ame.rockgolem

import content.global.ame.RandomEventNPC
import core.api.utils.WeightBasedTable
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs
import kotlin.math.max

class RockGolemNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.ROCK_GOLEM_413) {
    val ids = (413..418).toList()

    override fun init() {
        super.init()
        val index = max(0, (player.properties.combatLevel / 20) - 1)
        val id = ids.toList()[index]
        this.transform(id)
        this.attack(player)
        sendChat("Raarrrgghh! Flee human!")
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

    override fun talkTo(npc: NPC) {}
}

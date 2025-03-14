package content.global.ame.treespirit

import content.global.ame.RandomEventNPC
import core.api.utils.WeightBasedTable
import core.api.withinDistance
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs
import kotlin.math.max

class TreeSpiritNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.TREE_SPIRIT_438) {
    val ids = (438..443).toList()

    override fun init() {
        super.init()
        val index = max(0, (player.properties.combatLevel / 20) - 1)
        val id = ids.toList()[index]
        this.transform(id)
        this.attack(player)
        sendChat("Leave these woods and never return!")
        this.isRespawn = false
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
    }

    override fun tick() {
        if (!withinDistance(player, this.location, 8)) {
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

package content.global.ame.treespirit

import content.global.ame.RandomEventNPC
import core.api.utils.WeightBasedTable
import core.api.withinDistance
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs
import kotlin.math.max
import kotlin.math.min

/**
 * Handles the Tree Spirit event whilst cutting trees.
 * @author Splinter
 */
class TreeSpiritNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.TREE_SPIRIT_438) {
    val ids = (NPCs.TREE_SPIRIT_438..NPCs.TREE_SPIRIT_443).toList()

    override fun init() {
        super.init()
        val index = max(0, min(ids.size - 1, (player.properties.combatLevel / 20) - 1))
        val id = ids[index]
        this.transform(id)
        this.setAttribute("no-spawn-return", true)
        this.attack(player)
        sendChat("Leave these woods and never return!")
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
    }

    override fun tick() {
        super.tick()
        if (!this.inCombat()) {
            this.attack(player)
        }
        if (!withinDistance(player, this.location, 8)) {
            this.terminate()
        }
        if (!player.viewport.currentPlane!!.npcs
                .contains(this)
        ) {
            this.clear()
        }
    }

    override fun talkTo(npc: NPC) {}
}

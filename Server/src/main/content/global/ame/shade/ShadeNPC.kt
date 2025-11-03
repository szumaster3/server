package content.global.ame.shade

import content.global.ame.RandomEventNPC
import core.api.utils.WeightBasedTable
import core.api.withinDistance
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import shared.consts.NPCs
import kotlin.math.max
import kotlin.math.min

/**
 * Handles the shade npc.
 * @author Vexia
 */
class ShadeNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.SHADE_425) {
    val ids = (NPCs.SHADE_425..NPCs.SHADE_430).toList()

    override fun init() {
        super.init()
        val index = max(0, min(ids.size - 1, (player.properties.currentCombatLevel / 20) - 1))
        val id = ids[index]
        this.transform(id)
        this.setAttribute("no-spawn-return", true)
        this.attack(player)
        sendChat("Leave this place!")
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

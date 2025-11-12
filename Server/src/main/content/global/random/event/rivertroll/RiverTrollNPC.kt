package content.global.random.event.rivertroll

import content.global.random.RandomEventNPC
import core.api.utils.WeightBasedTable
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import shared.consts.NPCs
import java.lang.Integer.max

/**
 * Handles the river troll npc.
 * @author Vexia
 */
class RiverTrollNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.RIVER_TROLL_391) {
    val ids = (NPCs.RIVER_TROLL_391..NPCs.RIVER_TROLL_396).toList()

    override fun init() {
        super.init()
        val index = max(0, (player.properties.currentCombatLevel / 20) - 1)
        val id = ids.toList()[index]
        this.transform(id)
        this.attack(player)
        sendChat("Fishies be mine, leave dem fishies!")
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
        if (!player.viewport.currentPlane!!.npcs
                .contains(this)
        ) {
            this.clear()
        }
    }

    override fun talkTo(npc: NPC) {
        // Empty
    }
}

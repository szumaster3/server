package content.global.skill.slayer.npc

import content.global.skill.slayer.SlayerUtils.hasBroadWeaponEquipped
import content.global.skill.slayer.Tasks
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable

/**
 * The type Kurask npc.
 */
@Initializable
class KuraskNPC : AbstractNPC {
    /**
     * Instantiates a new Kurask npc.
     *
     * @param id       the id
     * @param location the location
     */
    constructor(id: Int, location: Location?) : super(id, location)

    /**
     * Instantiates a new Kurask npc.
     */
    constructor() : super(0, null)

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return KuraskNPC(id, location)
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        var effective = false
        if (state.attacker is Player) {
            val player = state.attacker as Player
            effective = hasBroadWeaponEquipped(player, state)
        }
        if (!effective) {
            state.estimatedHit = 0
            if (state.secondaryHit > 0) {
                state.secondaryHit = 0
            }
        }
    }

    override fun getIds(): IntArray {
        return Tasks.KURASKS.npcs
    }
}

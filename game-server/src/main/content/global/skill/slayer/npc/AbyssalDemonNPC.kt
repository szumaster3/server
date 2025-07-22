package content.global.skill.slayer.npc

import content.global.skill.slayer.Tasks
import core.api.getPathableRandomLocalCoordinate
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MeleeSwingHandler
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction

/**
 * The type Abyssal demon npc.
 */
@Initializable
class AbyssalDemonNPC : AbstractNPC {
    /**
     * Instantiates a new Abyssal demon npc.
     */
    constructor() : super(0, null)

    /**
     * Instantiates a new Abyssal demon npc.
     *
     * @param id       the id
     * @param location the location
     */
    constructor(id: Int, location: Location?) : super(id, location)

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return SWING_HANDLER
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return AbyssalDemonNPC(id, location)
    }

    override fun getIds(): IntArray {
        return Tasks.ABYSSAL_DEMONS.npcs
    }

    companion object {
        private val SWING_HANDLER: MeleeSwingHandler = object : MeleeSwingHandler() {
            override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
                if (victim is Player && RandomFunction.random(8) <= 2) {
                    val npc = RandomFunction.random(100) <= 50
                    val source = if (npc) victim else entity!!
                    val teleported = if (npc) entity else victim
                    val loc = getPathableRandomLocalCoordinate(teleported!!, 1, source.location, 3)
                    teleported.graphics(Graphics(409))
                    teleported.teleport(loc, 1)
                }
                return super.swing(entity, victim, state)
            }
        }
    }
}

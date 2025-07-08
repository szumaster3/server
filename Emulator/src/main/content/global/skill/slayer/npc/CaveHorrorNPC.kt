package content.global.skill.slayer.npc

import content.global.skill.slayer.SlayerEquipmentFlags.hasWitchwoodIcon
import content.global.skill.slayer.Tasks
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * The type Cave horror npc.
 */
@Initializable
class CaveHorrorNPC : AbstractNPC {
    /**
     * Instantiates a new Cave horror npc.
     */
    constructor() : super(0, null)

    /**
     * Instantiates a new Cave horror npc.
     *
     * @param id       the id
     * @param location the location
     */
    constructor(id: Int, location: Location?) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return CaveHorrorNPC(id, location)
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return COMBAT_HANDLER
    }

    override fun getIds(): IntArray {
        return Tasks.CAVE_HORRORS.npcs
    }

    companion object {
        private val COMBAT_HANDLER: MeleeSwingHandler = object : MeleeSwingHandler() {
            override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
                if (victim is Player) {
                    val player = victim
                    if (!hasWitchwood(player)) {
                        if (RandomFunction.random(10) < 5) {
                            state!!.estimatedHit = player.getSkills().lifepoints / 10
                        }
                    }
                }
                super.impact(entity, victim, state)
            }

            override fun isAttackable(entity: Entity, victim: Entity): InteractionType? {
                return CombatStyle.MELEE.swingHandler.isAttackable(entity, victim)
            }
        }

        /**
         * Has witchwood boolean.
         *
         * @param player the player
         * @return the boolean
         */
        fun hasWitchwood(player: Player): Boolean {
            return hasWitchwoodIcon(player)
        }
    }
}

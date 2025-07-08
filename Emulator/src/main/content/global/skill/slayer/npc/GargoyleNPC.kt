package content.global.skill.slayer.npc

import content.global.skill.slayer.Tasks
import core.api.sendMessage
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

/**
 * The type Gargoyle npc.
 */
@Initializable
class GargoyleNPC : AbstractNPC {
    /**
     * Instantiates a new Gargoyle npc.
     *
     * @param id       the id
     * @param location the location
     */
    constructor(id: Int, location: Location?) : super(id, location)

    /**
     * Instantiates a new Gargoyle npc.
     */
    constructor() : super(0, null)

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        val lp = getSkills().lifepoints

        if (state.estimatedHit > -1) {
            if (lp - state.estimatedHit < 1) {
                state.estimatedHit = 0
                if (lp > 1) {
                    state.estimatedHit = lp - 1
                }
            }
        }

        if (state.secondaryHit > -1) {
            if (lp - state.secondaryHit < 1) {
                state.secondaryHit = 0
                if (lp > 1) {
                    state.secondaryHit = lp - 1
                }
            }
        }

        val totalHit = state.estimatedHit + state.secondaryHit
        if (lp - totalHit < 1) {
            state.estimatedHit = 0
            state.secondaryHit = 0
        }
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any): Plugin<Any> {
        definePlugin(RockHammerHandler())
        return super.newInstance(arg)
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return GargoyleNPC(id, location)
    }

    override fun getIds(): IntArray {
        return Tasks.GARGOYLES.npcs
    }

    /**
     * The type Rock hammer handler.
     */
    inner class RockHammerHandler
    /**
     * Instantiates a new Rock hammer handler.
     */
        : UseWithHandler(Items.ROCK_HAMMER_4162) {
        override fun newInstance(arg: Any): Plugin<Any> {
            for (id in Tasks.GARGOYLES.npcs) {
                addHandler(id, NPC_TYPE, this)
            }
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            val npc = event.usedWith as NPC
            if (npc.getSkills().lifepoints > 10) {
                sendMessage(player, "The gargoyle isn't weak enough to be harmed by the hammer.")
            } else {
                sendMessage(player, "The gargoyle cracks apart.")
                npc.impactHandler.manualHit(player, npc.getSkills().lifepoints, HitsplatType.NORMAL)
            }
            return true
        }
    }
}

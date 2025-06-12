package content.region.other.the_abyss.plugin

import content.global.skill.runecrafting.RunePouch
import core.api.asItem
import core.api.hasAnItem
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Represents the Abyss NPCs.
 */
class AbyssalNPC : AbstractNPC {
    constructor() : super(0, null, true) {
        isAggressive = true
    }

    constructor(id: Int, location: Location?) : super(id, location, true)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = AbyssalNPC(id, location)

    override fun init() {
        super.init()
        setDefaultBehavior()
    }

    override fun handleTickActions() {
        super.handleTickActions()
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            val p = killer.asPlayer()
            if (RandomFunction.random(750) < 12) {
                val pouch = getPouch(p)
                if (pouch != null) {
                    definition.dropTables.createDrop(pouch.asItem(), p, this, getLocation())
                }
            }
        }
    }

    private fun getPouch(player: Player): Int? {
        val small = hasAnItem(player, RunePouch.SMALL.pouch).container != null || hasAnItem(
            player,
            RunePouch.SMALL.decayedPouch.id
        ).container != null
        val medium = hasAnItem(player, RunePouch.MEDIUM.pouch).container != null || hasAnItem(
            player,
            RunePouch.MEDIUM.decayedPouch.id
        ).container != null
        val large = hasAnItem(player, RunePouch.LARGE.pouch).container != null || hasAnItem(
            player,
            RunePouch.LARGE.decayedPouch.id
        ).container != null
        val giant = hasAnItem(player, RunePouch.GIANT.pouch).container != null || hasAnItem(
            player,
            RunePouch.GIANT.decayedPouch.id
        ).container != null

        if (!small) {
            return RunePouch.SMALL.pouch
        }
        if (!medium) {
            return RunePouch.MEDIUM.pouch
        }
        if (!large) {
            return RunePouch.LARGE.pouch
        }
        return if (!giant) {
            RunePouch.GIANT.pouch
        } else {
            null
        }
    }

    override fun getIds(): IntArray =
        intArrayOf(NPCs.ABYSSAL_LEECH_2263, NPCs.ABYSSAL_GUARDIAN_2264, NPCs.ABYSSAL_WALKER_2265)
}
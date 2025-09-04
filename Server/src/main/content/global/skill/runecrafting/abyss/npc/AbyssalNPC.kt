package content.global.skill.runecrafting.abyss.npc

import content.global.skill.runecrafting.RunePouch
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.RandomFunction
import shared.consts.NPCs

class AbyssalNPC : AbstractNPC {

    constructor() : super(0, null, true) {
        isAggressive = true
    }

    constructor(id: Int, location: Location?) : super(id, location, true)

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return AbyssalNPC(id, location)
    }

    override fun init() {
        super.init()
        setDefaultBehavior()
    }

    override fun handleTickActions() {
        super.handleTickActions()
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)

        if (killer is Player && RandomFunction.random(750) < 12) {
            val pouch = getPouch(killer)
            pouch?.let {
                definition.dropTables.createDrop(it, killer, this, location)
            }
        }
    }

    private fun getPouch(player: Player): Item? {
        return when {
            !player.hasItem(Item(RunePouch.SMALL.pouch)) -> Item(RunePouch.SMALL.pouch)
            !player.hasItem(Item(RunePouch.MEDIUM.pouch)) && !player.hasItem(RunePouch.MEDIUM.decayedPouch) -> Item(RunePouch.MEDIUM.pouch)
            !player.hasItem(Item(RunePouch.LARGE.pouch)) && !player.hasItem(RunePouch.LARGE.decayedPouch) -> Item(RunePouch.LARGE.pouch)
            !player.hasItem(Item(RunePouch.GIANT.pouch)) && !player.hasItem(RunePouch.GIANT.decayedPouch) -> Item(RunePouch.GIANT.pouch)
            else -> null
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ABYSSAL_LEECH_2263, NPCs.ABYSSAL_GUARDIAN_2264, NPCs.ABYSSAL_WALKER_2265)
    }
}
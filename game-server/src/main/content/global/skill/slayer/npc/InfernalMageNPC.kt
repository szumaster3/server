package content.global.skill.slayer.npc

import content.global.skill.slayer.Tasks
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.map.Location
import core.plugin.Initializable

/**
 * The type Infernal mage npc.
 */
@Initializable
class InfernalMageNPC : AbstractNPC {
    /**
     * Instantiates a new Infernal mage npc.
     */
    constructor() : super(0, null)

    /**
     * Instantiates a new Infernal mage npc.
     *
     * @param id       the id
     * @param location the location
     */
    constructor(id: Int, location: Location?) : super(id, location)

    override fun init() {
        super.init()
        properties.autocastSpell = SpellBook.MODERN.getSpell(8) as CombatSpell?
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return InfernalMageNPC(id, location)
    }

    override fun getIds(): IntArray {
        return Tasks.INFERNAL_MAGES.npcs
    }
}

package content.region.misthalin.draynor_village.wizard_tower.plugin

import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

/*
 * Wizards are humans in blue robes that can be found at
 * the Wizards' Tower, south of Draynor Village, and in the
 * Magic Guild in Yanille.
 */
@Initializable
class WizardNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = WizardNPC(id, location)

    override fun init() {
        super.init()
        properties.combatPulse.style = CombatStyle.MAGIC
        properties.autocastSpell = SpellBook.MODERN.getSpell(8) as CombatSpell
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.WIZARD_13)
}

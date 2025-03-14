package content.global.handlers.npc

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class DarkWizardNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    private val usedSpells = mutableSetOf<Int>()

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return DarkWizardNPC(id, location)
    }

    override fun init() {
        super.init()
        properties.combatPulse.style = CombatStyle.MAGIC
        isAggressive = true
        setDefaultSpell()
    }

    override fun onImpact(
        entity: Entity,
        state: BattleState,
    ) {
        super.onImpact(entity, state)

        if (getAttribute("switched", false)) {
            removeAttribute("switched")
            setDefaultSpell()
            return
        }

        if (RandomFunction.random(6) > 4) {
            castSpell()
            setAttribute("switched", true)
        }
    }

    private fun setDefaultSpell() {
        val defaultSpellId =
            when (id) {
                NPCs.DARK_WIZARD_172, NPCs.DARK_WIZARD_4659 -> 6
                else -> 4
            }
        properties.autocastSpell = SpellBook.MODERN.getSpell(defaultSpellId) as CombatSpell?
        usedSpells.clear()
    }

    private fun castSpell() {
        val availableSpells = spells.filter { it !in usedSpells }
        val spellToCast =
            if (availableSpells.isNotEmpty()) {
                availableSpells.random()
            } else {
                spells.random()
            }

        properties.autocastSpell = SpellBook.MODERN.getSpell(spellToCast) as CombatSpell?
        usedSpells.add(spellToCast)
    }

    private val spells: IntArray
        get() =
            when (id) {
                NPCs.DARK_WIZARD_172, NPCs.DARK_WIZARD_4659 -> intArrayOf(4, 2) // Water Bolt & Confuse
                else -> intArrayOf(6, 7) // Earth Bolt & Weaken
            }

    override fun getIds(): IntArray = ID

    companion object {
        private val ID =
            intArrayOf(NPCs.DARK_WIZARD_172, NPCs.DARK_WIZARD_174, NPCs.DARK_WIZARD_4659, NPCs.DARK_WIZARD_4660)
    }
}

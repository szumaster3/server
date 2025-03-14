package content.region.asgarnia.handlers.npc.portsarim

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.MagicSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class ElementalWizardNPC(id: Int, location: Location?) : AbstractNPC(id, location, true) {

    init {
        properties.combatPulse.style = CombatStyle.MAGIC
    }

    constructor() : this(0, null)

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return ElementalWizardNPC(id, location)
    }

    override fun onImpact(entity: Entity, state: BattleState) {
        state.spell?.takeIf { isSpellType(it) }?.let {
            state.estimatedHit = 0
            state.maximumHit = 0
            sendChat("Gratias tibi ago")
            getSkills().heal(getSkills().getStaticLevel(Skills.HITPOINTS))

            (state.attacker as? Player)?.let { player ->
                player.achievementDiaryManager.getDiary(DiaryType.FALADOR)?.takeIf { !it.isComplete(0, 8) }
                    ?.updateTask(player, 0, 8, true)
            }
        }

        if (getAttribute("switch", false)) {
            setBaseSpell()
            removeAttribute("switch")
        }

        if (RandomFunction.random(6) > 4) {
            setSpell()
        }

        super.onImpact(entity, state)
    }

    private fun setSpell() {
        SPELLS.getOrNull(spellIndex)?.let { spells ->
            properties.autocastSpell = SpellBook.MODERN.getSpell(spells.random()) as? CombatSpell
            setAttribute("switch", true)
        }
    }

    private fun setBaseSpell() {
        SPELLS.getOrNull(spellIndex)?.firstOrNull()?.let { spellId ->
            properties.autocastSpell = SpellBook.MODERN.getSpell(spellId) as? CombatSpell
        }
    }

    private fun isSpellType(spell: MagicSpell): Boolean {
        val prefixes = arrayOf("Fire", "Water", "Earth", "Air")
        return spell.javaClass.simpleName.startsWith(prefixes.getOrNull(spellIndex) ?: "")
    }

    private val spellIndex: Int
        get() = ids.indexOf(id).takeIf { it >= 0 } ?: 0

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FIRE_WIZARD_2709, NPCs.WATER_WIZARD_2710, NPCs.EARTH_WIZARD_2711, NPCs.AIR_WIZARD_2712)
    }

    companion object {
        private val SPELLS = arrayOf(
            intArrayOf(8, 7),  // Fire
            intArrayOf(4, 7),  // Water
            intArrayOf(6, 7),  // Earth
            intArrayOf(1, 7)   // Air
        )
    }
}

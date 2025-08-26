package content.global.skill.magic.spells.ancient

import core.api.hasTimerActive
import core.api.playGlobalAudio
import core.api.registerTimer
import core.api.spawnTimer
import core.game.container.impl.EquipmentContainer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.GameWorld
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items

/**
 * Represents the Miasmic spells.
 */
@Initializable
class MiasmicSpell private constructor(
    private val definition: MiasmicSpellDefinition
) : CombatSpell(
    definition.type,
    SpellBook.ANCIENT,
    definition.level,
    definition.xp,
    definition.castSound,
    definition.impactSound,
    definition.anim,
    definition.start,
    definition.projectile,
    definition.end,
    *definition.runes
) {
    constructor() : this(MiasmicSpellDefinition.RUSH)

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        MiasmicSpellDefinition.values().forEach {
            SpellBook.ANCIENT.register(it.button, MiasmicSpell(it))
        }
        return this
    }

    override fun visualize(entity: Entity, target: Node) {
        entity.graphics(graphics)
        projectile?.transform(entity, target as Entity, false, 58, 10)?.send()
        entity.animate(animation)
        audio?.let { playGlobalAudio(entity.location, it.id, 20) }
    }

    override fun fireEffect(entity: Entity, victim: Entity, state: BattleState) {
        if (!hasTimerActive(victim, "miasmic:immunity")) {
            registerTimer(victim, spawnTimer("miasmic", (spellId - 15) * 20))
        }
    }

    private fun validStaffEquipped(entity: Entity): Boolean {
        val player = entity as? Player ?: return false
        val weapon = player.equipment.getNew(EquipmentContainer.SLOT_WEAPON)
        return VALID_STAFF_IDS.any { it == weapon.id }
    }

    override fun cast(entity: Entity, target: Node): Boolean {
        if (!validStaffEquipped(entity) && !GameWorld.settings?.isDevMode!!) {
            (entity as? Player)?.packetDispatch?.sendMessage("You need to be wielding Zuriel's staff in order to cast this spell.")
            return false
        }
        if (!meetsRequirements(entity, true, false)) return false
        return super.cast(entity, target)
    }

    override fun getTargets(entity: Entity, target: Entity): Array<BattleState> {
        val singleTarget = definition.type == SpellType.RUSH || definition.type == SpellType.BLITZ
        if (singleTarget || !entity.properties.isMultiZone || !target.properties.isMultiZone) {
            return super.getTargets(entity, target)
        }
        val list = getMultihitTargets(entity, target, 9)
        return list.map { BattleState(entity, it) }.toTypedArray()
    }

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int {
        val add = when (definition.type) {
            SpellType.RUSH -> 4
            SpellType.BURST, SpellType.BLITZ -> 6
            else -> 9 // Barrage
        }
        return definition.type.getImpactAmount(entity, victim, add)
    }

    companion object {
        private val VALID_STAFF_IDS = intArrayOf(
            Items.ZURIELS_STAFF_13867,
            Items.ZURIELS_STAFF_DEG_13869,
            Items.NULL_13841,
            Items.NULL_13843
        )
    }
}
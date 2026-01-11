package content.global.skill.slayer.dungeon.waterbirth_dungeon

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * Handles Dagannoth King NPC.
 */
class DagannothKingNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    private lateinit var type: DagannothKingType

    override fun init() {
        super.init()

        type = DagannothKingType.forId(id)
    }

    override fun checkImpact(state: BattleState) {
        val style = state.style ?: state.attacker.properties.combatPulse.style
        if (type.isImmune(style)) {
            state.neutralizeHits()
        }
    }

    override fun getLevelMod(entity: Entity, victim: Entity): Double {
        return when (type) {
            DagannothKingType.PRIME -> 3.5
            else -> 0.0
        }
    }

    override fun sendImpact(state: BattleState) {
        if (state.estimatedHit > type.maxHit) {
            state.estimatedHit = RandomFunction.random(type.maxHit - 5, type.maxHit)
        }

        if (type != DagannothKingType.REX) {
            val players = RegionManager.getLocalPlayers(this, 9)
                .filter { it != null && !DeathTask.isDead(it) }
            if (players.size <= 1) return

            val newTarget = players.randomOrNull() ?: return
            if (newTarget != null && newTarget != state.attacker) {
                properties.combatPulse.stop()
                getAggressiveHandler().pauseTicks = 2
                attack(newTarget)
            }
        }
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        DagannothKingNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(
        NPCs.DAGANNOTH_SUPREME_2881,
        NPCs.DAGANNOTH_PRIME_2882,
        NPCs.DAGANNOTH_REX_2883
    )

    enum class DagannothKingType(
        val id: Int,
        val style: CombatStyle,
        val weakStyle: CombatStyle,
        val immuneStyle: CombatStyle,
        val maxHit: Int
    ) {
        SUPREME(NPCs.DAGANNOTH_SUPREME_2881, CombatStyle.RANGE, CombatStyle.MELEE, CombatStyle.MAGIC, 30),
        PRIME(NPCs.DAGANNOTH_PRIME_2882, CombatStyle.MAGIC, CombatStyle.RANGE, CombatStyle.MELEE, 61),
        REX(NPCs.DAGANNOTH_REX_2883, CombatStyle.MELEE, CombatStyle.MAGIC, CombatStyle.RANGE, 28);

        fun isImmune(style: CombatStyle): Boolean = style == immuneStyle || style == this.style

        companion object {
            fun forId(id: Int): DagannothKingType =
                values().firstOrNull { it.id == id } ?: throw IllegalArgumentException("Invalid Dagannoth King ID: [$id]")
        }
    }
}
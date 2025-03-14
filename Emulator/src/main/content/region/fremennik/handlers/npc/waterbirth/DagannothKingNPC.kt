package content.region.fremennik.handlers.npc.waterbirth

import core.api.utils.BossKillCounter
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayers
import core.tools.RandomFunction
import org.rs.consts.NPCs

class DagannothKingNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    private var type: DagType? = null

    override fun init() {
        type = DagType.forId(id)
        super.init()
    }

    override fun checkImpact(state: BattleState) {
        var style = state.style
        if (style == null) {
            style = state.attacker.properties.combatPulse.style
        }
        if (type!!.isImmune(style)) {
            state.neutralizeHits()
        }
    }

    override fun getLevelMod(
        entity: Entity,
        victim: Entity,
    ): Double {
        if (type == DagType.PRIME) {
            return 3.5
        }
        return 0.0
    }

    override fun sendImpact(state: BattleState) {
        if (state.estimatedHit > type!!.maxHit) {
            state.estimatedHit = RandomFunction.random(type!!.maxHit - 5, type!!.maxHit)
        }
        if (type != DagType.REX && RandomFunction.random(5) <= 2) {
            val players = getLocalPlayers(this, 9)
            if (players.size <= 1) {
                return
            }
            val newPlayer = players[RandomFunction.random(players.size)]
            if (newPlayer != null) {
                properties.combatPulse.stop()
                getAggressiveHandler().pauseTicks = 2
                attack(newPlayer)
            }
        }
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return DagannothKingNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DAGANNOTH_SUPREME_2881, NPCs.DAGANNOTH_PRIME_2882, NPCs.DAGANNOTH_REX_2883)
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (id == NPCs.DAGANNOTH_SUPREME_2881 || id == NPCs.DAGANNOTH_PRIME_2882 || id == NPCs.DAGANNOTH_REX_2883) {
            BossKillCounter.addToKillCount(killer as Player, this.id)
        }
    }

    enum class DagType(
        val id: Int,
        val style: CombatStyle,
        val weakStyle: CombatStyle,
        val immuneStyle: CombatStyle,
        val maxHit: Int,
    ) {
        SUPREME(
            id = NPCs.DAGANNOTH_SUPREME_2881,
            style = CombatStyle.RANGE,
            weakStyle = CombatStyle.MELEE,
            immuneStyle = CombatStyle.MAGIC,
            maxHit = 30,
        ),
        PRIME(
            id = NPCs.DAGANNOTH_PRIME_2882,
            style = CombatStyle.MAGIC,
            weakStyle = CombatStyle.RANGE,
            immuneStyle = CombatStyle.MELEE,
            maxHit = 61,
        ),
        REX(
            id = NPCs.DAGANNOTH_REX_2883,
            style = CombatStyle.MELEE,
            weakStyle = CombatStyle.MAGIC,
            immuneStyle = CombatStyle.RANGE,
            maxHit = 28,
        ),
        ;

        fun isImmune(style: CombatStyle): Boolean {
            return style == immuneStyle || style == this.style
        }

        companion object {
            fun forId(id: Int): DagType? {
                for (type in values()) {
                    if (type.id == id) {
                        return type
                    }
                }
                return null
            }
        }
    }
}

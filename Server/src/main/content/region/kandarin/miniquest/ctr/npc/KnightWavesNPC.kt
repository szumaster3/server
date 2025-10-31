package content.region.kandarin.miniquest.ctr.npc

import content.data.GameAttributes
import core.api.poofClear
import core.api.teleport
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.equipment.Weapon
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayers
import shared.consts.NPCs

/**
 * Represents a Knight NPC participating in the wave battles.
 */
class KnightWavesNPC : AbstractNPC {
    var type: WaveTier? = null
    private var commenced = false
    var player: Player? = null
    private var timer = 0

    constructor() : super(0, null)

    internal constructor(id: Int, location: Location?, player: Player?) : super(id, location) {
        this.isWalks = true
        this.isRespawn = false
        this.type = WaveTier.forId(id)
        this.player = player
        this.isInvisible = false
    }

    /**
     * Called every game tick to manage NPC behavior.
     */
    override fun handleTickActions() {
        super.handleTickActions()
        player?.let {
            if (!it.isActive || !getLocalPlayers(this).contains(it)) {
                it.removeAttribute(GameAttributes.KW_SPAWN)
                pulseManager.clear()
                clear()
            } else if (!properties.combatPulse.isAttacking) {
                properties.combatPulse.attack(it)
            }
        }
        if (timer++ > 5000) {
            pulseManager.clear()
            poofClear(this)
        }
    }

    /**
     * Called when the NPC dies to handle transformation to the next wave.
     */
    override fun finalizeDeath(killer: Entity?) {
        if (killer == player) {
            this.asNpc().isInvisible = true
            (type ?: return poofClear(this)).transform(this, player)
            timer = 0
        } else {
            super.finalizeDeath(killer)
        }
    }

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean = player == entity

    override fun canSelectTarget(target: Entity): Boolean = target is Player && target == player

    /**
     * Modifies damage based on combat style and conditions.
     */
    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        if (state.attacker is Player) {
            when (state.style) {
                CombatStyle.MELEE -> {
                    state.neutralizeHits()
                    state.estimatedHit = state.maximumHit
                }

                CombatStyle.RANGE, CombatStyle.MAGIC -> {
                    val specialAttack = player?.getExtension<WeaponInterface>(WeaponInterface::class.java)
                    if (specialAttack?.isSpecialBar == true && state.style != CombatStyle.MELEE) {
                        if (state.estimatedHit > -1) state.estimatedHit = 0
                    }
                }

                else -> {
                    if (state.weapon.type != Weapon.WeaponType.DEFAULT) {
                        if (state.estimatedHit > -1) state.estimatedHit = 0
                        if (state.secondaryHit > -1) state.secondaryHit = 0
                    }
                }
            }
        }
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = KnightWavesNPC(id, location, null)

    override fun getIds(): IntArray = intArrayOf(
        NPCs.SIR_BEDIVERE_6177,
        NPCs.SIR_PELLEAS_6176,
        NPCs.SIR_TRISTRAM_6175,
        NPCs.SIR_PALOMEDES_1883,
        NPCs.SIR_LUCAN_6173,
        NPCs.SIR_GAWAIN_6172,
        NPCs.SIR_KAY_6171,
        NPCs.SIR_LANCELOT_6170,
    )

    fun setCommenced(commenced: Boolean) {
        this.commenced = commenced
    }

    /**
     * Represents each wave tier in the Knight's Training.
     */
    enum class WaveTier(val id: Int) {
        I(NPCs.SIR_BEDIVERE_6177),
        II(NPCs.SIR_PELLEAS_6176),
        III(NPCs.SIR_TRISTRAM_6175),
        IV(NPCs.SIR_PALOMEDES_1883),
        V(NPCs.SIR_LUCAN_6173),
        VI(NPCs.SIR_GAWAIN_6172),
        VII(NPCs.SIR_KAY_6171),
        VIII(NPCs.SIR_LANCELOT_6170),
        IX(-1),
        ;

        /**
         * Transforms the current NPC into the next wave NPC or ends the training.
         */
        fun transform(npc: KnightWavesNPC, player: Player?) {
            val newType = next()
            npc.lock()
            npc.pulseManager.clear()
            npc.walkingQueue.reset()
            player?.setAttribute(GameAttributes.KW_TIER, this.id)

            Pulser.submit(object : Pulse(3, npc, player) {
                override fun pulse(): Boolean {
                    if (!npc.isActive) return true

                    npc.unlock()
                    npc.animator.reset()
                    npc.fullRestore()
                    npc.impactHandler.disabledTicks = 1
                    npc.isInvisible = false

                    if (newType == IX) {
                        player?.let {
                            teleport(it, Location.create(2750, 3507, 2).transform(Direction.SOUTH))
                            MerlinKnightWavesNPC.spawnMerlin(it)
                        }
                        npc.clear()
                    } else {
                        npc.type = newType
                        npc.transform(newType!!.id)
                        npc.properties.combatPulse.attack(player)
                    }

                    player?.unlock()
                    return true
                }
            })
        }

        /**
         * Gets the next wave tier, or the same if it's the last one.
         */
        fun next(): WaveTier? = if (ordinal + 1 < knightTypes.size) knightTypes[ordinal + 1] else knightTypes[ordinal]

        companion object {
            fun forId(id: Int): WaveTier? = values().find { it.id == id }
            private val knightTypes = values()
        }
    }
}


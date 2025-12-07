package content.region.kandarin.miniquest.ctr

import content.data.GameAttributes
import content.region.kandarin.camelot.dialogue.MerlinDialogue
import core.api.*
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
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents a Knight NPC participating in the wave battles.
 */
class CamelotTrainingRoomNPC : AbstractNPC {
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
        super.finalizeDeath(killer)
        if (killer == player) {
            this.asNpc().isInvisible = true
            (type ?: return poofClear(this)).transform(this, player)
            timer = 0
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

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = CamelotTrainingRoomNPC(id, location, null)

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
        fun transform(npc: CamelotTrainingRoomNPC, player: Player?) {
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
                            MerlinNPC.spawnMerlin(it)
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


/**
 * Represents the Merlin NPC that spawns after the final wave of the miniquest.
 */
@Initializable
private class MerlinNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    private var cleanTime = 0
    private var player: Player? = null

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = MerlinNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.MERLIN_213)

    /**
     * Auto-cleans attributes and removes Merlin after a delay.
     */
    override fun handleTickActions() {
        super.handleTickActions()
        if (cleanTime++ > 300) {
            removeAttributes(player!!, GameAttributes.KW_TIER, GameAttributes.KW_BEGIN)
        }
        poofClear(this)
    }

    companion object {
        /**
         * Spawns Merlin for the final dialogue.
         */
        fun spawnMerlin(player: Player) {
            val merlin = MerlinNPC(NPCs.MERLIN_213)
            merlin.location = Location.create(2750, 3505, 2)
            merlin.isWalks = false
            merlin.isAggressive = false
            merlin.isActive = false

            if (merlin.asNpc() != null && merlin.isActive) {
                merlin.properties.teleportLocation = merlin.properties.spawnLocation
            }
            merlin.isActive = true
            Pulser.submit(
                object : Pulse(1, merlin) {
                    override fun pulse(): Boolean {
                        merlin.init()
                        face(findLocalNPC(player, NPCs.MERLIN_213)!!, player, 3)
                        face(player, findLocalNPC(player, NPCs.MERLIN_213)!!)
                        openDialogue(player, MerlinDialogue())
                        return true
                    }
                },
            )
        }
    }
}


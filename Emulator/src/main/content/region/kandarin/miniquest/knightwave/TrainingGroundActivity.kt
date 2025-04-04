package content.region.kandarin.miniquest.knightwave

import content.data.GameAttributes
import content.region.kandarin.dialogue.camelot.MerlinDialogue
import core.api.*
import core.game.activity.ActivityManager
import core.game.activity.ActivityPlugin
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.equipment.Weapon
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.system.timer.impl.SkillRestore
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayers
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.plugin.ClassScanner
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Contains constants used throughout the Knight Wave activity.
 */
object KnightWaveAttributes {
    const val KW_SPAWN = "knights-training:spawn"
    const val KW_TIER = "knights-training:wave"
    const val KW_BEGIN = "/save:knights-training:complete-tutorial"
    const val KW_COMPLETE = "/save:knights-training:complete"
    const val ACTIVITY = "Knight's training"
}

/**
 * Represents the Training Grounds activity for the Knight's Wave mini-quest.
 * Handles area entry, logout, player deaths, and restrictions.
 */
@Initializable
class TrainingGroundActivity :
    ActivityPlugin(KnightWaveAttributes.ACTIVITY, true, false, true),
    MapArea {
    private var activity: TrainingGroundActivity? = this

    init {
        activity = this
        this.safeRespawn = Location.create(2750, 3507, 2)
    }

    /**
     * Called when an entity dies inside the area.
     */
    override fun death(
        entity: Entity,
        killer: Entity,
    ): Boolean {
        if (entity is Player) {
            entity.getProperties().teleportLocation = Location.create(2751, 3507, 2)
            return true
        }
        return false
    }

    /**
     * Called when an entity leaves the activity area.
     */
    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        super.areaLeave(entity, logout)
        if (entity is Player) {
            removeAttributes(
                entity,
                GameAttributes.PRAYER_LOCK,
                KnightWaveAttributes.KW_SPAWN,
                KnightWaveAttributes.KW_TIER,
                KnightWaveAttributes.KW_BEGIN,
            )
            findLocalNPC(entity, KnightWavesNPC().id)?.let { poofClear(it) }
            clearLogoutListener(entity, KnightWaveAttributes.ACTIVITY)
            entity.hook(Event.PrayerActivated, SkillRestore.PrayerActivatedHook)
        }
    }

    /**
     * Called when the activity starts.
     */
    override fun start(
        player: Player?,
        login: Boolean,
        vararg args: Any?,
    ): Boolean = super.start(player, login, *args)

    /**
     * Called when an entity enters the activity area.
     */
    override fun areaEnter(entity: Entity) {
        super.areaEnter(entity)
        if (entity is Player) {
            setAttribute(entity, GameAttributes.PRAYER_LOCK, true)
            entity.hook(Event.PrayerDeactivated, SkillRestore.PrayerDeactivatedHook)
            registerLogoutListener(entity, KnightWaveAttributes.ACTIVITY) { _ ->
                entity.hook(Event.PrayerActivated, SkillRestore.PrayerActivatedHook)
                teleport(entity, Location.create(2751, 3507, 2))
            }
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(2752, 3502, 2764, 3513, 2, false))

    /**
     * Registers plugins and returns a new instance of this activity.
     */
    override fun newInstance(p: Player?): ActivityPlugin {
        ActivityManager.register(this)
        ClassScanner.definePlugin(KnightWavesNPC())
        ClassScanner.definePlugin(MerlinKnightWavesNPC())
        return this
    }

    override fun getRestrictions(): Array<ZoneRestriction> =
        arrayOf(
            ZoneRestriction.CANNON,
            ZoneRestriction.RANDOM_EVENTS,
            ZoneRestriction.FOLLOWERS,
            ZoneRestriction.TELEPORT,
        )

    override fun getSpawnLocation(): Location? = null

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
                    it.removeAttribute(KnightWaveAttributes.KW_SPAWN)
                    clear()
                } else if (!properties.combatPulse.isAttacking) {
                    properties.combatPulse.attack(it)
                }
            }
            if (timer++ > 5000) poofClear(this)
        }

        /**
         * Called when the NPC dies to handle transformation to the next wave.
         */
        override fun finalizeDeath(killer: Entity?) {
            if (killer == player) {
                this.asNpc().isInvisible = true
                type?.transform(this, player)
                timer = 0
            } else {
                super.finalizeDeath(killer)
            }
        }

        override fun isAttackable(
            entity: Entity,
            style: CombatStyle,
            message: Boolean,
        ): Boolean = player == entity

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
                        val specialAttack = player!!.getExtension<WeaponInterface>(WeaponInterface::class.java)
                        if (specialAttack.isSpecialBar && state.style != CombatStyle.MELEE) {
                            if (state.estimatedHit > -1) state.estimatedHit = 0
                        }
                        if (state.secondaryHit > -1) state.secondaryHit = 0
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

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any,
        ): AbstractNPC = KnightWavesNPC(id, location, null)

        override fun getIds(): IntArray =
            intArrayOf(
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
    }

    /**
     * Represents the Merlin NPC that spawns after the final wave of the miniquest.
     */
    class MerlinKnightWavesNPC(
        id: Int = 0,
        location: Location? = null,
    ) : AbstractNPC(id, location) {
        private var cleanTime = 0
        private var player: Player? = null

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any,
        ): AbstractNPC = MerlinKnightWavesNPC(id, location)

        override fun getIds(): IntArray = intArrayOf(NPCs.MERLIN_213)

        /**
         * Auto-cleans attributes and removes Merlin after a delay.
         */
        override fun handleTickActions() {
            super.handleTickActions()
            if (cleanTime++ > 300) {
                removeAttributes(player!!, KnightWaveAttributes.KW_TIER, KnightWaveAttributes.KW_BEGIN)
            }
            poofClear(this)
        }

        companion object {
            /**
             * Spawns Merlin for the final dialogue.
             */
            fun spawnMerlin(player: Player) {
                val merlin = MerlinKnightWavesNPC(NPCs.MERLIN_213)
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
}

/**
 * Represents each wave tier in the Knight's Training.
 */
enum class WaveTier(
    val id: Int,
) {
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
    fun transform(
        npc: TrainingGroundActivity.KnightWavesNPC,
        player: Player?,
    ) {
        val newType = next()
        npc.lock()
        npc.pulseManager.clear()
        npc.walkingQueue.reset()
        player?.setAttribute(KnightWaveAttributes.KW_TIER, this.id)
        Pulser.submit(
            object : Pulse(3, npc, player) {
                private var counter = 0

                override fun pulse(): Boolean =
                    when (++counter) {
                        1 -> {
                            npc.unlock()
                            npc.animator.reset()
                            npc.fullRestore()
                            npc.type = newType
                            npc.transform(newType!!.id)
                            npc.impactHandler.disabledTicks = 1
                            npc.isInvisible = false
                            if (newType != IX) {
                                npc.properties.combatPulse.attack(player)
                            } else {
                                teleport(player!!, Location.create(2750, 3507, 2).transform(Direction.SOUTH))
                                TrainingGroundActivity.MerlinKnightWavesNPC.spawnMerlin(player)
                                npc.clear()
                            }
                            player?.unlock()
                            true
                        }

                        else -> false
                    }
            },
        )
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

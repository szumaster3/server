package core.game.node.entity.combat

import content.global.skill.summoning.familiar.Familiar
import core.api.log
import core.api.playGlobalAudio
import core.game.component.Component
import core.game.container.impl.EquipmentContainer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.ArmourSet
import core.game.node.entity.combat.equipment.DegradableEquipment
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.node.entity.skill.Skills
import core.game.system.config.ItemConfigParser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.RegionManager.getClippingFlag
import core.game.world.map.path.Pathfinder.*
import core.game.world.update.flag.context.Animation
import core.tools.Log
import core.tools.RandomFunction
import org.rs.consts.Sounds

abstract class CombatSwingHandler(
    var type: CombatStyle?,
) {
    var flags: Array<out SwingHandlerFlag> = emptyArray()

    constructor(type: CombatStyle?, vararg flags: SwingHandlerFlag) : this(type) {
        this.flags = flags
    }

    private var specialHandlers: MutableMap<Int, CombatSwingHandler?>? = null

    abstract fun swing(
        entity: Entity?,
        victim: Entity?,
        state: BattleState?,
    ): Int

    abstract fun impact(
        entity: Entity?,
        victim: Entity?,
        state: BattleState?,
    )

    abstract fun visualizeImpact(
        entity: Entity?,
        victim: Entity?,
        state: BattleState?,
    )

    abstract fun calculateAccuracy(entity: Entity?): Int

    abstract fun calculateHit(
        entity: Entity?,
        victim: Entity?,
        modifier: Double,
    ): Int

    abstract fun calculateDefence(
        victim: Entity?,
        attacker: Entity?,
    ): Int

    abstract fun getSetMultiplier(
        e: Entity?,
        skillId: Int,
    ): Double

    open fun visualize(
        entity: Entity,
        victim: Entity?,
        state: BattleState?,
    ) {
        entity.animate(getAttackAnimation(entity, type))
    }

    fun onImpact(
        entity: Entity?,
        victim: Entity?,
        state: BattleState?,
    ) {
        if (entity is Player && victim != null) {
            DegradableEquipment.degrade(entity as Player?, victim, true)
        }
        if (state == null) {
            return
        }
        if (state.targets != null && state.targets.isNotEmpty()) {
            if (!(state.targets.size == 1 && state.targets[0] == state)) {
                for (s in state.targets) {
                    if (s != null && s != state) {
                        onImpact(entity, s.victim, s)
                    }
                }
                return
            }
        }
        victim!!.onImpact(entity, state)
    }

    open fun getArmourSet(e: Entity?): ArmourSet? {
        return null
    }

    fun isAccurateImpact(
        entity: Entity?,
        victim: Entity?,
    ): Boolean {
        return isAccurateImpact(entity, victim, type, 1.0, 1.0)
    }

    fun isAccurateImpact(
        entity: Entity?,
        victim: Entity?,
        style: CombatStyle?,
    ): Boolean {
        return isAccurateImpact(entity, victim, style, 1.0, 1.0)
    }

    fun isAccurateImpact(
        entity: Entity?,
        victim: Entity?,
        style: CombatStyle?,
        accuracyMod: Double,
        defenceMod: Double,
    ): Boolean {
        var mod = 1.0
        if (victim == null || style == null) {
            return false
        }
        if (victim is Player && entity is Familiar && victim.prayer[PrayerType.PROTECT_FROM_SUMMONING]) {
            mod = 0.0
        }
        val attack = calculateAccuracy(entity) * accuracyMod * mod
        val defence = calculateDefence(victim, entity) * defenceMod
        val chance: Double =
            if (attack > defence) {
                1 - ((defence + 2) / (2 * (attack + 1)))
            } else {
                attack / (2 * (defence + 1))
            }
        return Math.random() < chance
    }

    open fun canSwing(
        entity: Entity,
        victim: Entity,
    ): InteractionType? {
        return isAttackable(entity, victim)
    }

    open fun isAttackable(
        entity: Entity,
        victim: Entity,
    ): InteractionType? {
        if (victim.getAttribute("return-to-spawn", false)) {
            return InteractionType.NO_INTERACT
        }

        if (type == CombatStyle.MELEE) {
            val stepType = canStepTowards(entity, victim)
            if (stepType != InteractionType.STILL_INTERACT) return stepType
        }

        val comp = entity.getAttribute("autocast_component", null) as Component?
        if ((comp != null || type == CombatStyle.MAGIC) &&
            (entity.properties.autocastSpell == null || entity.properties.autocastSpell.spellId == 0) &&
            entity is Player
        ) {
            val weapEx = entity.getExtension<Any>(WeaponInterface::class.java) as WeaponInterface?
            if (comp != null) {
                entity.interfaceManager.close(comp)
                entity.interfaceManager.openTab(weapEx)
                entity.properties.combatPulse.stop()
                entity.attack(victim)
                entity.removeAttribute("autocast_component")
            }
            weapEx?.updateInterface()
            entity.debug("Adjusting attack style")
        }
        if (entity.location == victim.location) {
            return if (entity is Player &&
                victim is Player &&
                entity.clientIndex < victim.clientIndex &&
                victim.properties.combatPulse.getVictim() === entity
            ) {
                InteractionType.STILL_INTERACT
            } else {
                InteractionType.NO_INTERACT
            }
        }
        val el = entity.location
        val vl = victim.location
        val evl = vl.transform(victim.size(), victim.size(), 0)
        if (el.x >= vl.x && el.x < evl.x && el.y >= vl.y && el.y < evl.y || el.z != vl.z) {
            return InteractionType.NO_INTERACT
        }
        return InteractionType.STILL_INTERACT
    }

    private fun canStepTowards(
        entity: Entity,
        victim: Entity,
    ): InteractionType {
        val closestVictimTile = victim.getClosestOccupiedTile(entity.location)
        val closestEntityTile = entity.getClosestOccupiedTile(closestVictimTile)
        val dir =
            closestEntityTile.deriveDirection(closestVictimTile)
                ?: return InteractionType.STILL_INTERACT // if we cannot derive a direction, it's because both tiles are the same, so hand off control to the main logic which already handles this case
        var next = closestEntityTile

        while (next.getDistance(closestVictimTile) > 3) { // skip the initial gap in distance if it exists, because standard pathfinding would already stop us before this point if something was between us and the NPC or vice versa
            next = next.transform(dir)
        }

        var result: InteractionType = InteractionType.STILL_INTERACT
        val maxIterations = next.getDistance(closestVictimTile).toInt()
        for (i in 0 until maxIterations) { // step towards the target tile, checking if anything would obstruct us on the way, and immediately breaking + returning if it does.
            next = next.transform(dir)
            result = checkStepInterval(dir, next)
            if (result == InteractionType.NO_INTERACT) break
        }

        return result
    }

    private fun checkStepInterval(
        dir: Direction,
        next: Location,
    ): InteractionType {
        val components = next.getStepComponents(dir)

        when (dir) {
            Direction.NORTH -> if (getClippingFlag(next) and PREVENT_NORTH != 0) return InteractionType.NO_INTERACT
            Direction.EAST -> if (getClippingFlag(next) and PREVENT_EAST != 0) return InteractionType.NO_INTERACT
            Direction.SOUTH -> if (getClippingFlag(next) and PREVENT_SOUTH != 0) return InteractionType.NO_INTERACT
            Direction.WEST -> if (getClippingFlag(next) and PREVENT_WEST != 0) return InteractionType.NO_INTERACT

            Direction.NORTH_EAST -> {
                if (getClippingFlag(components[0]) and PREVENT_EAST != 0 ||
                    getClippingFlag(components[1]) and PREVENT_NORTH != 0 ||
                    getClippingFlag(next) and PREVENT_NORTHEAST != 0
                ) {
                    return InteractionType.NO_INTERACT
                }
            }

            Direction.NORTH_WEST -> {
                if (getClippingFlag(components[0]) and PREVENT_WEST != 0 ||
                    getClippingFlag(components[1]) and PREVENT_NORTH != 0 ||
                    getClippingFlag(next) and PREVENT_NORTHWEST != 0
                ) {
                    return InteractionType.NO_INTERACT
                }
            }

            Direction.SOUTH_EAST -> {
                if (getClippingFlag(components[0]) and PREVENT_EAST != 0 ||
                    getClippingFlag(components[1]) and PREVENT_SOUTH != 0 ||
                    getClippingFlag(next) and PREVENT_SOUTHEAST != 0
                ) {
                    return InteractionType.NO_INTERACT
                }
            }

            Direction.SOUTH_WEST -> {
                if (getClippingFlag(components[0]) and PREVENT_WEST != 0 ||
                    getClippingFlag(components[1]) and PREVENT_SOUTH != 0 ||
                    getClippingFlag(next) and PREVENT_SOUTHWEST != 0
                ) {
                    return InteractionType.NO_INTERACT
                }
            }
        }

        return InteractionType.STILL_INTERACT
    }

    fun getDragonfireMessage(
        protection: Int,
        fireName: String,
    ): String {
        if (protection and 0x4 != 0) {
            if (protection and 0x2 != 0) {
                return "Your potion and shield fully protects you from the dragon's $fireName."
            }
            return "Your shield absorbs most of the dragon's $fireName."
        }
        if (protection and 0x2 != 0) {
            return "Your antifire potion helps you defend the against the dragon's $fireName."
        }
        if (protection and 0x8 != 0) {
            return "Your magic prayer absorbs some of the dragon's $fireName."
        }
        return "You are horribly burnt by the dragon's $fireName."
    }

    fun visualizeAudio(
        entity: Entity,
        victim: Entity,
        state: BattleState,
    ) {
        if (entity is Player) {
            val styleIndex = entity.settings.attackStyleIndex
            if (state.weapon != null && state.weapon.item != null) {
                val weapon = state.weapon.item
                val audios = weapon?.definition?.getConfiguration<Array<Audio>>(ItemConfigParser.ATTACK_AUDIO, null)
                if (audios != null) {
                    var audio: Audio? = null
                    if (styleIndex < audios.size) {
                        audio = audios[styleIndex]
                    }
                    if (audio == null || audio.id == 0) {
                        audio = audios[0]
                    }
                    playGlobalAudio(entity.location, audio.id)
                }
            } else if (type == CombatStyle.MELEE) {
                // plays a punching sound when no weapon is equipped
                playGlobalAudio(entity.location, Sounds.HUMAN_ATTACK_2564)
            }
        } else if (entity is NPC) {
            val npc = entity.asNpc()
            val audio = npc.getAudio(0)
            if (audio != null) playGlobalAudio(entity.location, audio.id)
        }
    }

    open fun getCombatDistance(
        e: Entity,
        v: Entity,
        rawDistance: Int,
    ): Int {
        var distance = rawDistance
        if (e is NPC) {
            if (e.definition.combatDistance > 0) {
                distance = e.definition.combatDistance
            }
        }
        return (e.size() shr 1) + (v.size() shr 1) + distance
    }

    fun formatHit(
        victim: Entity,
        rawHit: Int,
    ): Int {
        var hit = rawHit
        if (hit < 1) {
            return hit
        }
        if (hit > victim.skills.lifepoints) {
            hit = victim.skills.lifepoints
        }
        return hit
    }

    open fun adjustBattleState(
        entity: Entity,
        victim: Entity,
        state: BattleState,
    ) {
        EXPERIENCE_MOD = 4.0
        var totalHit = 0
        if (entity is Player) {
            entity.familiarManager.adjustBattleState(state)
        }
        entity.sendImpact(state)
        victim.checkImpact(state)

        if (victim.id == 4474 && type == CombatStyle.MAGIC || victim.id == 7891 && type == CombatStyle.MELEE) {
            EXPERIENCE_MOD = 0.1
            victim.fullRestore()
            if (state.estimatedHit >= 15) {
                state.estimatedHit = 14
            }
            if (state.secondaryHit >= 15) {
                state.secondaryHit = 14
            }
        }
        if (victim.id == 757) {
            EXPERIENCE_MOD = 0.01
        }

        if (state.targets != null && state.targets.isNotEmpty()) {
            if (!(state.targets.size == 1 && state.targets[0] == state)) {
                for (s in state.targets) {
                    if (s != null && s != state) {
                        adjustBattleState(entity, victim, s)
                    }
                }
            }
        }
        if (state.estimatedHit > 0) {
            state.estimatedHit = getFormattedHit(entity, victim, state, state.estimatedHit)
            totalHit += state.estimatedHit
        }
        if (state.secondaryHit > 0) {
            state.secondaryHit = getFormattedHit(entity, victim, state, state.secondaryHit)
            totalHit += state.secondaryHit
        }
        if (entity is Player) {
            entity.degrader.checkWeaponDegrades(entity)
            if (totalHit > 0 && entity.prayer[PrayerType.SMITE] && victim.skills.prayerPoints > 0) {
                victim.skills.decrementPrayerPoints(totalHit * 0.25)
            }
            if (entity.getAttribute("1hko", false)) {
                state.estimatedHit = victim.skills.lifepoints
            }
        }
        if (victim is NPC) {
            if (victim.properties.protectStyle != null && state.style == victim.properties.protectStyle) {
                state.neutralizeHits()
            }
        }
    }

    open fun addExperience(
        entity: Entity?,
        victim: Entity?,
        state: BattleState?,
    ) {
        if (entity == null || (victim is Player && entity is Player && entity.asPlayer().ironmanManager.isIronman)) {
            return
        }
        var player: Player
        var attStyle: Int

        when (entity) {
            is Familiar -> {
                player = entity.owner
                attStyle = entity.attackStyle
            }

            is Player -> {
                player = entity
                attStyle = entity.properties.attackStyle.style
            }

            else -> return
        }
        if (victim is NPC) EXPERIENCE_MOD *= victim.behavior.getXpMultiplier(victim, player)
        if (state != null) {
            val hit = state.totalDamage
            if (entity is Player) {
                var experience = hit * 1.33
                if (victim is NPC) experience *= victim.behavior.getXpMultiplier(victim, player)
                player.skills.addExperience(Skills.HITPOINTS, experience, true)
            }

            var skill = -1
            when (attStyle) {
                WeaponInterface.STYLE_DEFENSIVE -> skill = Skills.DEFENCE
                WeaponInterface.STYLE_ACCURATE -> skill = Skills.ATTACK
                WeaponInterface.STYLE_AGGRESSIVE -> skill = Skills.STRENGTH
                WeaponInterface.STYLE_CONTROLLED -> {
                    var experience = hit * EXPERIENCE_MOD
                    experience /= 3.0
                    player.skills.addExperience(Skills.ATTACK, experience, true)
                    player.skills.addExperience(Skills.STRENGTH, experience, true)
                    player.skills.addExperience(Skills.DEFENCE, experience, true)
                    return
                }

                WeaponInterface.STYLE_RANGE_ACCURATE -> skill = Skills.RANGE
                WeaponInterface.STYLE_RAPID -> skill = Skills.RANGE
                WeaponInterface.STYLE_LONG_RANGE -> {
                    player.skills.addExperience(Skills.RANGE, hit * (EXPERIENCE_MOD / 2), true)
                    player.skills.addExperience(Skills.DEFENCE, hit * (EXPERIENCE_MOD / 2), true)
                    return
                }

                WeaponInterface.STYLE_CAST -> skill = Skills.MAGIC
                WeaponInterface.STYLE_DEFENSIVE_CAST -> {
                    var experience = hit.toDouble()
                    if (victim is NPC) experience *= victim.behavior.getXpMultiplier(victim, player)
                    player.skills.addExperience(Skills.MAGIC, experience * 1.33, true)
                    player.skills.addExperience(Skills.DEFENCE, experience, true)
                    return
                }
            }
            if (skill < 0) return
            player.skills.addExperience(skill, hit * EXPERIENCE_MOD, true)
        }
    }

    protected open fun getFormattedHit(
        attacker: Entity,
        victim: Entity,
        state: BattleState,
        rawHit: Int,
    ): Int {
        var hit = rawHit
        hit = attacker.getFormattedHit(state, hit).toInt()
        if (victim is Player) {
            val player = victim.asPlayer()
            val shield = player.equipment[EquipmentContainer.SLOT_SHIELD]
            if (shield != null && shield.id == 13742) {
                if (RandomFunction.random(100) < 71) {
                    hit -= (hit.toDouble() * 0.25).toInt()
                    if (hit < 1) {
                        hit = 0
                    }
                }
            }
            if (shield != null && shield.id == 13740) {
                val reduce = hit.toDouble() * 0.30
                var drain = hit * 0.15
                if (player.skills.prayerPoints > drain && drain > 0) {
                    if (drain < 1) {
                        drain = 1.0
                    }
                    hit -= reduce.toInt()
                    if (hit < 1) {
                        hit = 0
                    }
                    player.skills.decrementPrayerPoints(drain)
                }
            }
        }
        if (attacker is Player) {
            val player = attacker.asPlayer()
            if (player.equipment[3] != null && player.equipment[3].id == 14726 && state.style == CombatStyle.MAGIC) {
                hit += (hit.toDouble() * 0.15).toInt()
            }
        }
        if (attacker is Familiar && victim is Player) {
            if (victim.prayer[PrayerType.PROTECT_FROM_SUMMONING]) {
                hit = 0
            }
        }
        return formatHit(victim, hit)
    }

    fun getAttackAnimation(
        e: Entity,
        style: CombatStyle?,
    ): Animation {
        var anim: Animation? = null
        if (type != null && e is NPC) {
            anim = e.properties.getCombatAnimation(style!!.ordinal % 3)
        }
        return anim ?: e.properties.attackAnimation
    }

    fun register(
        itemId: Int,
        handler: CombatSwingHandler,
    ): Boolean {
        if (specialHandlers == null) {
            specialHandlers = HashMap()
        }
        if (specialHandlers!!.containsKey(itemId)) {
            log(
                this::class.java,
                Log.ERR,
                "Already contained special attack handler for item " + itemId + " - [old=" +
                    specialHandlers!![itemId]!!::class.java.simpleName +
                    ", new=" +
                    handler.javaClass.simpleName +
                    "].",
            )
            return false
        }
        return specialHandlers!!.put(itemId, handler) == null
    }

    fun getSpecial(itemId: Int): CombatSwingHandler? {
        if (specialHandlers == null) {
            specialHandlers = HashMap()
        }
        return specialHandlers!![itemId]
    }

    companion object {
        @JvmField
        var EXPERIENCE_MOD = 4.0

        @JvmStatic
        fun isProjectileClipped(
            entity: Node,
            victim: Node?,
            checkClose: Boolean,
        ): Boolean {
            for (x1 in 0 until entity.size()) {
                for (y1 in 0 until entity.size()) {
                    val src = entity.location.transform(x1, y1, 0)
                    for (x2 in 0 until victim!!.size()) {
                        for (y2 in 0 until victim!!.size()) {
                            val dst = victim!!.location.transform(x2, y2, 0)
                            val path =
                                PROJECTILE.find(src, 1, dst, 1, 1, 0, 0, 0, false, RegionManager::getClippingFlag)
                            if (path.isSuccessful && (!checkClose || path.points.size <= 1)) {
                                return true
                            }
                        }
                    }
                }
            }
            return false
        }
    }
}

enum class SwingHandlerFlag {
    IGNORE_STAT_BOOSTS_DAMAGE,
    IGNORE_STAT_BOOSTS_ACCURACY,
    IGNORE_PRAYER_BOOSTS_DAMAGE,
    IGNORE_PRAYER_BOOSTS_ACCURACY,
}

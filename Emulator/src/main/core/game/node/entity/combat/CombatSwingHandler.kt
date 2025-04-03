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
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

/**
 * Abstract handler for managing combat swings in the game.
 *
 * @property type The combat style to be used in combat.
 * @property flags Additional flags to customize the behavior of the combat swing handler.
 */
abstract class CombatSwingHandler(
    var type: CombatStyle?,
) {

    /**
     * Flags associated with the combat swing handler.
     */
    var flags: Array<out SwingHandlerFlag> = emptyArray()

    /**
     * Creates a combat swing handler with the specified combat style and flags.
     *
     * @param type The combat style to use.
     * @param flags Additional flags to customize the behavior.
     */
    constructor(type: CombatStyle?, vararg flags: SwingHandlerFlag) : this(type) {
        this.flags = flags
    }

    private var specialHandlers: MutableMap<Int, CombatSwingHandler?>? = null

    /**
     * Executes a swing of the combat style.
     *
     * @param entity The entity performing the swing.
     * @param victim The target of the swing.
     * @param state The current battle state.
     * @return The result of the swing.
     */
    abstract fun swing(
        entity: Entity?,
        victim: Entity?,
        state: BattleState?,
    ): Int

    /**
     * Applies the impact of a combat swing.
     *
     * @param entity The entity performing the swing.
     * @param victim The target of the swing.
     * @param state The current battle state.
     */
    abstract fun impact(
        entity: Entity?,
        victim: Entity?,
        state: BattleState?,
    )

    /**
     * Visualizes the impact of a combat swing.
     *
     * @param entity The entity performing the swing.
     * @param victim The target of the swing.
     * @param state The current battle state.
     */
    abstract fun visualizeImpact(
        entity: Entity?,
        victim: Entity?,
        state: BattleState?,
    )

    /**
     * Calculates the accuracy of the entity's attack.
     *
     * @param entity The entity performing the attack.
     * @return The calculated accuracy.
     */
    abstract fun calculateAccuracy(entity: Entity?): Int

    /**
     * Calculates the damage dealt by the swing.
     *
     * @param entity The entity performing the swing.
     * @param victim The target of the swing.
     * @param modifier A modifier to adjust the hit damage.
     * @return The calculated damage.
     */
    abstract fun calculateHit(
        entity: Entity?,
        victim: Entity?,
        modifier: Double,
    ): Int

    /**
     * Calculates the defense value of the victim.
     *
     * @param victim The target of the swing.
     * @param attacker The entity performing the attack.
     * @return The calculated defense value.
     */
    abstract fun calculateDefence(
        victim: Entity?,
        attacker: Entity?,
    ): Int

    /**
     * Gets the multiplier for the armor set.
     *
     * @param e The entity performing the attack.
     * @param skillId The skill id to be used.
     * @return The multiplier based on the armor set.
     */
    abstract fun getSetMultiplier(
        e: Entity?,
        skillId: Int,
    ): Double

    /**
     * Visualizes the combat action, such as animating the entity's attack.
     *
     * @param entity The entity performing the attack.
     * @param victim The target of the attack.
     * @param state The current battle state.
     */
    open fun visualize(
        entity: Entity,
        victim: Entity?,
        state: BattleState?,
    ) {
        entity.animate(getAttackAnimation(entity, type))
    }

    /**
     * Handles the impact after a swing, including [DegradableEquipment] and damage calculation.
     *
     * @param entity The entity performing the swing.
     * @param victim The target of the swing.
     * @param state The current battle state.
     */
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

    /**
     * Retrieves the armor set for the entity, if any.
     *
     * @param e The entity to get the armor set for.
     * @return The armor set, or null if none is available.
     */
    open fun getArmourSet(e: Entity?): ArmourSet? = null

    /**
     * Determines if the combat swing is accurate based on various factors.
     *
     * @param entity The entity performing the swing.
     * @param victim The target of the swing.
     * @return True if the swing is accurate, false otherwise.
     */
    fun isAccurateImpact(
        entity: Entity?,
        victim: Entity?,
    ): Boolean = isAccurateImpact(entity, victim, type, 1.0, 1.0)

    /**
     * Determines if the combat swing is accurate based on various factors, including combat style.
     *
     * @param entity The entity performing the swing.
     * @param victim The target of the swing.
     * @param style The combat style being used.
     * @return True if the swing is accurate, false otherwise.
     */
    fun isAccurateImpact(
        entity: Entity?,
        victim: Entity?,
        style: CombatStyle?,
    ): Boolean = isAccurateImpact(entity, victim, style, 1.0, 1.0)

    /**
     * Determines if the combat swing is accurate based on various factors, including combat style and modifiers.
     *
     * @param entity The entity performing the swing.
     * @param victim The target of the swing.
     * @param style The combat style being used.
     * @param accuracyMod A modifier for the accuracy.
     * @param defenceMod A modifier for the defense.
     * @return True if the swing is accurate, false otherwise.
     */
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
        val chance: Double = if (attack > defence) {
            1 - ((defence + 2) / (2 * (attack + 1)))
        } else {
            attack / (2 * (defence + 1))
        }
        return Math.random() < chance
    }

    /**
     * Determines if the entity can swing at the victim based on certain conditions.
     *
     * @param entity The entity performing the swing.
     * @param victim The target of the swing.
     * @return The interaction type, which can indicate if the attack is allowed.
     */
    open fun canSwing(
        entity: Entity,
        victim: Entity,
    ): InteractionType? = isAttackable(entity, victim)

    /**
     * Determines if the victim is attackable by the entity based on various factors.
     *
     * @param entity The entity performing the swing.
     * @param victim The target of the swing.
     * @return The interaction type, which can indicate if the victim is attackable.
     */
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
        if ((comp != null || type == CombatStyle.MAGIC) && (entity.properties.autocastSpell == null || entity.properties.autocastSpell.spellId == 0) && entity is Player) {
            val weapEx = entity.getExtension<Any>(WeaponInterface::class.java) as WeaponInterface?
            if (comp != null) {
                entity.interfaceManager.close(comp)
                entity.interfaceManager.openTab(weapEx)
                entity.properties.combatPulse.stop()
                entity.attack(victim)
                entity.removeAttribute("autocast_component")
            }
            weapEx?.updateInterface()
            entity.debug("[COMBAT SWING HANDLER] - Adjusting attack style")
        }
        if (entity.location == victim.location) {
            return if (entity is Player && victim is Player && entity.clientIndex < victim.clientIndex && victim.properties.combatPulse.getVictim() === entity) {
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

    /**
     * Determines if the entity can step towards the victim.
     *
     * @param entity The entity attempting to step towards the victim.
     * @param victim The entity that the stepping entity is attempting to approach.
     * @return The interaction type, either [InteractionType.NO_INTERACT] or [InteractionType.STILL_INTERACT].
     */
    private fun canStepTowards(
        entity: Entity,
        victim: Entity,
    ): InteractionType {
        val closestVictimTile = victim.getClosestOccupiedTile(entity.location)
        val closestEntityTile = entity.getClosestOccupiedTile(closestVictimTile)
        val dir = closestEntityTile.deriveDirection(closestVictimTile)
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

    /**
     * Checks the step interval for the given direction and location.
     *
     * @param dir The direction the entity is moving towards.
     * @param next The next location the entity intends to step to.
     * @return The interaction type, either [InteractionType.NO_INTERACT] or [InteractionType.STILL_INTERACT].
     */
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
                if (getClippingFlag(components[0]) and PREVENT_EAST != 0 || getClippingFlag(components[1]) and PREVENT_NORTH != 0 || getClippingFlag(
                        next
                    ) and PREVENT_NORTHEAST != 0
                ) {
                    return InteractionType.NO_INTERACT
                }
            }

            Direction.NORTH_WEST -> {
                if (getClippingFlag(components[0]) and PREVENT_WEST != 0 || getClippingFlag(components[1]) and PREVENT_NORTH != 0 || getClippingFlag(
                        next
                    ) and PREVENT_NORTHWEST != 0
                ) {
                    return InteractionType.NO_INTERACT
                }
            }

            Direction.SOUTH_EAST -> {
                if (getClippingFlag(components[0]) and PREVENT_EAST != 0 || getClippingFlag(components[1]) and PREVENT_SOUTH != 0 || getClippingFlag(
                        next
                    ) and PREVENT_SOUTHEAST != 0
                ) {
                    return InteractionType.NO_INTERACT
                }
            }

            Direction.SOUTH_WEST -> {
                if (getClippingFlag(components[0]) and PREVENT_WEST != 0 || getClippingFlag(components[1]) and PREVENT_SOUTH != 0 || getClippingFlag(
                        next
                    ) and PREVENT_SOUTHWEST != 0
                ) {
                    return InteractionType.NO_INTERACT
                }
            }
        }

        return InteractionType.STILL_INTERACT
    }

    /**
     * Returns a formatted message based on the type of protection the entity has against dragonfire.
     *
     * @param protection An integer representing the entity's protection against dragonfire.
     * @param fireName The fire name.
     * @return A string containing the message about the protection.
     */
    fun getDragonfireMessage(protection: Int, fireName: String): String {
        return when {
            protection and 0x4 != 0 && protection and 0x2 != 0 -> "Your potion and shield fully protects you from the dragon's $fireName."
            protection and 0x4 != 0 -> "Your shield absorbs most of the dragon's $fireName."
            protection and 0x2 != 0 -> "Your antifire potion helps you defend against the dragon's $fireName."
            protection and 0x8 != 0 -> "Your magic prayer absorbs some of the dragon's $fireName."
            else -> "You are horribly burnt by the dragon's $fireName."
        }
    }

    /**
     * Visualizes the audio played during combat.
     *
     * @param entity The entity that is performing the attack.
     * @param victim The entity that is being attacked.
     * @param state The current state of the battle.
     */
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
            } else if (type == CombatStyle.MELEE) {/*
                 * Plays a punching sound when no weapon is equipped.
                 */
                playGlobalAudio(entity.location, Sounds.HUMAN_ATTACK_2564)
            }
        } else if (entity is NPC) {
            val npc = entity.asNpc()
            val audio = npc.getAudio(0)
            if (audio != null) playGlobalAudio(entity.location, audio.id)
        }
    }

    /**
     * Returns the combat distance between the attacker and the victim.
     *
     * @param e The attacking entity.
     * @param v The victim entity.
     * @param rawDistance The raw distance between the two entities.
     * @return The adjusted combat distance.
     */
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

    /**
     * Formats the hit points based on the victim's current health.
     *
     * @param victim The entity being attacked.
     * @param rawHit The raw damage to be dealt.
     * @return The final formatted hit value.
     */
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

    /**
     * Adjusts the battle state based on the entities involved in the combat.
     *
     * @param entity The attacking entity.
     * @param victim The victim entity.
     * @param state The current state of the battle.
     */
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

        if (victim.id == NPCs.MAGIC_DUMMY_4474 && type == CombatStyle.MAGIC || victim.id == NPCs.MELEE_DUMMY_7891 && type == CombatStyle.MELEE) {
            EXPERIENCE_MOD = 0.1
            victim.fullRestore()
            if (state.estimatedHit >= 15) {
                state.estimatedHit = 14
            }
            if (state.secondaryHit >= 15) {
                state.secondaryHit = 14
            }
        }
        if (victim.id == NPCs.COUNT_DRAYNOR_757) {
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
            entity.degrade.checkWeaponDegrades(entity)
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

    /**
     * Adds experience based on the damage dealt in combat.
     *
     * @param entity The entity performing the attack.
     * @param victim The entity receiving the attack.
     * @param state The current state of the battle.
     */
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

    /**
     * Returns the formatted hit after applying any relevant modifiers.
     *
     * @param attacker The entity performing the attack.
     * @param victim The entity being attacked.
     * @param state The current state of the battle.
     * @param rawHit The raw hit value.
     * @return The final formatted hit value.
     */
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
            if (shield != null && shield.id == Items.ELYSIAN_SPIRIT_SHIELD_13742) {
                if (RandomFunction.random(100) < 71) {
                    hit -= (hit.toDouble() * 0.25).toInt()
                    if (hit < 1) {
                        hit = 0
                    }
                }
            }
            if (shield != null && shield.id == Items.DIVINE_SPIRIT_SHIELD_13740) {
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
        if (attacker is Familiar && victim is Player) {
            if (victim.prayer[PrayerType.PROTECT_FROM_SUMMONING]) {
                hit = 0
            }
        }
        return formatHit(victim, hit)
    }

    /**
     * Returns the attack animation associated with the specified combat style.
     *
     * @param e The entity performing the attack.
     * @param style The combat style being used by the entity.
     * @return The animation associated with the attack.
     */
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

    /**
     * Registers a special attack handler for a specific item id.
     *
     * @param itemId The item id for which the handler is being registered.
     * @param handler The combat swing handler to be registered for the item.
     * @return `true` if the handler was successfully registered, `false` if a handler already exists for the item.
     */
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
                "Already contained special attack handler for item " + itemId + " - [old=" + specialHandlers!![itemId]!!::class.java.simpleName + ", new=" + handler.javaClass.simpleName + "].",
            )
            return false
        }
        return specialHandlers!!.put(itemId, handler) == null
    }

    /**
     * Retrieves the special combat swing handler associated with a specific item id.
     *
     * @param itemId The id of the item for which the special handler is being requested.
     * @return The combat swing handler associated with the item id, or `null` if none exists.
     */
    fun getSpecial(itemId: Int): CombatSwingHandler? {
        if (specialHandlers == null) {
            specialHandlers = HashMap()
        }
        return specialHandlers!![itemId]
    }

    companion object {
        @JvmField
        var EXPERIENCE_MOD = 4.0

        /**
         * Determines if the projectile from the given entity is clipped when targeting a victim.
         *
         * This method checks if there is any clipping (obstacles or other entities) between the entity and victim
         * that would block the projectile. Optionally, it can check for close clipping as well.
         *
         * @param entity The entity that is firing the projectile.
         * @param victim The entity that is being targeted by the projectile.
         * @param checkClose If `true`, it also checks for close clipping.
         * @return `true` if the projectile is clipped, `false` otherwise.
         */
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

/**
 * Represents different flags for swing handlers that can modify combat behavior.
 */
enum class SwingHandlerFlag {
    /**
     * Flag indicating that stat boosts should be ignored for damage calculation.
     */
    IGNORE_STAT_BOOSTS_DAMAGE,

    /**
     * Flag indicating that stat boosts should be ignored for accuracy calculation.
     */
    IGNORE_STAT_BOOSTS_ACCURACY,

    /**
     * Flag indicating that prayer boosts should be ignored for damage calculation.
     */
    IGNORE_PRAYER_BOOSTS_DAMAGE,

    /**
     * Flag indicating that prayer boosts should be ignored for accuracy calculation.
     */
    IGNORE_PRAYER_BOOSTS_ACCURACY,
}

package core.game.node.entity.combat

import core.api.applyPoison
import core.api.log
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.*
import core.game.node.entity.combat.equipment.Weapon.WeaponType
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Graphics
import core.tools.Log
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Handles the range combat swings.
 *
 * @author Emperor, Ceikry, conversion to Kotlin + cleanup
 */
open class RangeSwingHandler(vararg flags: SwingHandlerFlag) : CombatSwingHandler(CombatStyle.RANGE, *flags) {
    override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
        if (!isProjectileClipped(entity, victim, false)) {
            return InteractionType.NO_INTERACT
        }
        var distance = 7
        if (entity is Player && (entity.getExtension<Any>(WeaponInterface::class.java) as WeaponInterface).weaponInterface?.interfaceId == Components.WEAPON_THROWN_SEL_91) {
            distance -= 2
        }
        if (entity.properties.attackStyle!!.style == WeaponInterface.STYLE_LONG_RANGE) {
            distance += 2
        }
        if (entity is Player) {
            val rw = RangeWeapon.get(entity.equipment.getNew(EquipmentContainer.SLOT_WEAPON).id)
            if (rw != null && (rw.weaponType == WeaponType.DOUBLE_SHOT || rw.weaponType == WeaponType.DEGRADING)) {
                // Dark bow and crystal bow have a 10-square range,
                // independent of whether longrange stance is used.
                distance = 10
            }
        }
        var goodRange = victim.centerLocation.withinDistance(
            entity.centerLocation,
            getCombatDistance(entity, victim, distance),
        )
        var type = InteractionType.STILL_INTERACT
        if (victim.walkingQueue.isMoving && !goodRange) {
            goodRange = victim.centerLocation.withinDistance(
                entity.centerLocation,
                getCombatDistance(entity, victim, ++distance),
            )
            type = InteractionType.MOVE_INTERACT
        }
        if (goodRange && super.canSwing(entity, victim) != InteractionType.NO_INTERACT) {
            if (type == InteractionType.STILL_INTERACT) {
                entity.walkingQueue.reset()
            }
            return type
        }
        return InteractionType.NO_INTERACT
    }

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        configureRangeData(entity, state)
        if (state!!.weapon == null || !hasAmmo(entity, state)) {
            entity!!.properties.combatPulse.lastUsedStyle = CombatStyle.RANGE
            entity!!.properties.combatPulse.stop()
            return -1
        }
        var hit = 0
        if (isAccurateImpact(entity, victim, CombatStyle.RANGE)) {
            val max = calculateHit(entity, victim, 1.0).also {
                if (entity?.name?.lowercase() == "test10") {
                    log(this::class.java, Log.FINE, "Damage: $it")
                }
            }
            state.maximumHit = max
            hit = RandomFunction.random(max + 1)
        }
        state.estimatedHit = hit
        if (state.weapon.type == WeaponType.DOUBLE_SHOT) {
            if (isAccurateImpact(entity, victim, CombatStyle.RANGE)) {
                hit = RandomFunction.random(calculateHit(entity, victim, 1.0))
            }
            state.secondaryHit = hit
        }
        if (entity == null || victim!!.location == null) {
            return -1
        }
        if (state.estimatedHit > victim.skills.lifepoints) state.estimatedHit = victim.skills.lifepoints
        if (state.estimatedHit + state.secondaryHit > victim.skills.lifepoints) {
            state.secondaryHit -= ((state.estimatedHit + state.secondaryHit) - victim.skills.lifepoints)
        }
        useAmmo(entity, state, victim.location)
        return 1 + ceil(entity.location.getDistance(victim.location) * 0.3).toInt()
    }

    fun configureRangeData(entity: Entity?, state: BattleState?) {
        if (entity == null || state == null) return
        var style = entity.properties.combatPulse.lastUsedStyle
        state.style = CombatStyle.RANGE

        if (entity is Player) {
            val weaponItem = entity.equipment.getAsId(EquipmentContainer.SLOT_WEAPON)
            if (weaponItem == null) {
                entity.properties.combatPulse.stop()
                log(this::class.java, Log.ERR, "No $weaponItem for player ${entity}.")
                return
            }

            val rangeWeapon = RangeWeapon.get(weaponItem)
            if (rangeWeapon == null) {
                log(this::class.java, Log.ERR, "Unhandled range weapon used - [item id=${weaponItem}].")
                entity.properties.combatPulse.stop()
                return
            }

            if (rangeWeapon.weaponType == WeaponType.THROWN) {
                val weaponObj = Weapon(Item(weaponItem), -1, null, WeaponType.THROWN)
                weaponObj.type = WeaponType.THROWN
                state.weapon = weaponObj
                state.rangeWeapon = rangeWeapon
                state.ammunition = null
                entity.properties.combatPulse.lastUsedStyle = CombatStyle.RANGE
                return
            }

            val ammoItem = entity.equipment.getNew(rangeWeapon.ammunitionSlot)
            val w = Weapon(Item(weaponItem), rangeWeapon.ammunitionSlot, ammoItem)
            w.type = rangeWeapon.weaponType
            state.weapon = w
            state.rangeWeapon = rangeWeapon
            state.ammunition = ammoItem?.let { Ammunition.get(it.id) }


            entity.properties.combatPulse.lastUsedStyle = CombatStyle.RANGE
        } else {
            val w = Weapon(null)
            state.weapon = w
        }
    }

    override fun adjustBattleState(entity: Entity, victim: Entity, state: BattleState) {
        if (state.ammunition != null && entity is Player) {
            val damage = state.ammunition.poisonDamage
            if (state.estimatedHit > 0 && damage > 8 && RandomFunction.random(10) < 4) {
                applyPoison(victim, entity, damage)
            }
        }
        super.adjustBattleState(entity, victim, state)
    }

    override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
        if (state == null || victim == null) return

        var start: Graphics? = null
        var projectile: Projectile? = null

        state.ammunition?.let { ammo ->
            start = ammo.startGraphics
            projectile = ammo.projectile
        }

        if (projectile == null && state.weapon != null) {
            RangeWeapon.get(state.weapon.id)?.let { rw ->
                rw.ammunition.forEach { ammoId ->
                    Ammunition.get(ammoId)?.let { ammo ->
                        start = ammo.startGraphics
                        projectile = ammo.projectile
                        return@let
                    }
                }
            }
        }

        projectile?.copy(entity, victim, 5.0)?.send() ?: run {
            if (state.weapon?.type == WeaponType.THROWN) {
                Projectile.create(entity, victim, state.weapon.id, 40, 36, 41, 50, 25).send()
            }
        }

        if (state.weapon?.type == WeaponType.DOUBLE_SHOT) {
            state.ammunition?.darkBowGraphics?.let { dbow ->
                start = dbow
                val speed = (55 + entity.location.getDistance(victim.location) * 10).toInt()
                Projectile.create(entity, victim, state.ammunition?.projectile?.projectileId ?: state.weapon.id,
                    40, 36, 41, speed, 25).send()
            }
        }

        if (entity is NPC) {
            entity.definition.combatGraphics.getOrNull(0)?.let { start = it }
            entity.definition.combatGraphics.getOrNull(1)?.let { g ->
                Projectile.ranged(entity, victim, g.id, g.height, 36, 41, 5).send()
            }
        }

        val weaponAnim = RangeWeapon.get(state.weapon?.id ?: -1)?.animation
        if (weaponAnim != null && (entity.properties.attackAnimation.id == Animations.PUNCH_422 || entity.properties.attackAnimation.id == Animations.KICK_423)) {
            entity.visualize(weaponAnim, start)
            return
        }

        entity.visualize(entity.properties.attackAnimation, start)
    }

    override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (state!!.ammunition != null && state.ammunition.effect != null && state.ammunition!!.effect!!.canFire(state)) {
            state.ammunition!!.effect!!.impact(state)
        }
        val hit = state.estimatedHit
        victim!!.impactHandler.handleImpact(entity, hit, CombatStyle.RANGE, state)
        if (state.secondaryHit > -1) {
            val hitt = state.secondaryHit
            GameWorld.Pulser.submit(
                object : Pulse(1, victim) {
                    override fun pulse(): Boolean {
                        victim.impactHandler.handleImpact(entity, hitt, CombatStyle.RANGE, state)
                        return true
                    }
                },
            )
        }
    }

    override fun visualizeImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
        victim!!.animate(victim.properties.defenceAnimation)
    }

    override fun calculateAccuracy(entity: Entity?): Int {
        entity ?: return 0
        var effectiveRangedLevel = entity.skills.getLevel(Skills.RANGE).toDouble()
        if (entity is Player && !flags.contains(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_ACCURACY)) {
            effectiveRangedLevel =
                floor(effectiveRangedLevel + (entity.prayer.getSkillBonus(Skills.RANGE) * effectiveRangedLevel))
        }
        if (entity.properties.attackStyle!!.style == WeaponInterface.STYLE_RANGE_ACCURATE) effectiveRangedLevel += 3
        effectiveRangedLevel += 8
        effectiveRangedLevel *= getSetMultiplier(entity, Skills.RANGE)
        effectiveRangedLevel = floor(effectiveRangedLevel)
        if (!flags.contains(SwingHandlerFlag.IGNORE_STAT_BOOSTS_ACCURACY)) {
            effectiveRangedLevel *= (entity.properties.bonuses[entity.properties.attackStyle!!.bonusType] + 64)
        } else {
            effectiveRangedLevel *= 64
        }

        return floor(effectiveRangedLevel).toInt()
    }

    override fun calculateHit(entity: Entity?, victim: Entity?, modifier: Double): Int {
        val level = entity!!.skills.getLevel(Skills.RANGE)
        val bonus = entity.properties.bonuses[14]
        var prayer = 1.0
        if (entity is Player && !flags.contains(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_DAMAGE)) {
            prayer += entity.prayer.getSkillBonus(Skills.RANGE)
        }
        var cumulativeStr = floor(level * prayer)
        if (entity.properties.attackStyle!!.style == WeaponInterface.STYLE_RANGE_ACCURATE) {
            cumulativeStr += 3.0
        }
        cumulativeStr *= getSetMultiplier(entity, Skills.RANGE)

        if (!flags.contains(SwingHandlerFlag.IGNORE_STAT_BOOSTS_DAMAGE)) {
            cumulativeStr *= (bonus + 64)
        } else {
            cumulativeStr *= 64
        }

        return floor((1.5 + (ceil(cumulativeStr) / 640.0)) * modifier).toInt()
        // return ((14 + cumulativeStr + bonus / 8 + cumulativeStr * bonus * 0.016865) * modifier).toInt() / 10 + 1
    }

    override fun calculateDefence(victim: Entity?, attacker: Entity?): Int {
        victim ?: return 0
        attacker ?: return 0

        val defLevel = victim.skills.getLevel(Skills.DEFENCE)
        val styleDefenceBonus = victim.properties.bonuses[attacker.properties.attackStyle!!.bonusType + 5] + 64
        return defLevel * styleDefenceBonus
    }

    override fun getSetMultiplier(e: Entity?, skillId: Int): Double {
        if (skillId == Skills.RANGE) {
            if (e is Player && e.isWearingVoid(CombatStyle.RANGE)) {
                return 1.1
            }
        }
        return 1.0
    }

    override fun getArmourSet(e: Entity?): ArmourSet? = if (ArmourSet.KARIL.isUsing(e)) {
        ArmourSet.KARIL
    } else {
        super.getArmourSet(e)
    }

    companion object {
        /**
         * Checks if the player has enough ammunition to use their ranged weapon.
         *
         * @param e The entity performing the attack.
         * @param state The current battle state containing weapon and ammo info.
         * @return True if the player has enough ammo, false otherwise.
         */
        fun hasAmmo(e: Entity?, state: BattleState?): Boolean {
            if (e !is Player) return true
            val weaponType = state?.weapon?.type ?: return false
            if (weaponType == WeaponType.DEGRADING) {
                return true
            }
            if (weaponType == WeaponType.THROWN) {
                val weaponItem = e.equipment[EquipmentContainer.SLOT_WEAPON]
                if (weaponItem != null && weaponItem.amount > 0) {
                    return true
                }
                e.packetDispatch.sendMessage("You do not have enough ammo left.")
                return false
            }
            val ammoItem = e.equipment[state.weapon.ammunitionSlot]
            val amountNeeded = if (weaponType == WeaponType.DOUBLE_SHOT) 2 else 1

            if (ammoItem != null && ammoItem.amount >= amountNeeded) {
                if (!state.rangeWeapon.ammunition.contains(ammoItem.id)) {
                    e.packetDispatch.sendMessage("You can't use this type of ammunition with this bow.")
                    return false
                }
                return true
            }

            if (weaponType == WeaponType.DOUBLE_SHOT) {
                state.weapon.type = WeaponType.DEFAULT
                return hasAmmo(e, state)
            }

            e.packetDispatch.sendMessage("You do not have enough ammo left.")
            return false
        }

        /**
         * Consumes ammunition for a ranged attack and handles dropping ammo on the ground.
         *
         * @param e The entity performing the attack. Must be a Player.
         * @param state The current battle state containing weapon and ammo info.
         * @param location The location to drop ammo, if applicable.
         */
        fun useAmmo(e: Entity, state: BattleState, location: Location?) {
            if (e !is Player) return
            val weaponType = state.weapon.type
            val amount = if (weaponType == WeaponType.DOUBLE_SHOT) 2 else 1

            if (weaponType == WeaponType.DEGRADING) {
                e.debug("Degrading weapon used. No ammo consumed.")
                return
            }

            val (itemId, slot) = when (weaponType) {
                WeaponType.THROWN -> e.equipment[EquipmentContainer.SLOT_WEAPON]?.let { it.id to EquipmentContainer.SLOT_WEAPON }
                else -> state.weapon.ammunition?.id?.let { it to state.weapon.ammunitionSlot }
            } ?: return

            val currentItem = e.equipment[slot] ?: run {
                e.packetDispatch.sendMessage("You do not have enough ammo left.")
                return
            }

            if (currentItem.amount < amount) {
                e.packetDispatch.sendMessage("You do not have enough ammo left.")
                return
            }

            e.equipment.replace(Item(itemId, currentItem.amount - amount, currentItem.charge), slot)
            e.debug("${if (weaponType == WeaponType.THROWN) "Thrown weapon" else "Ammo"} used: id=$itemId, amount=$amount, remaining=${e.equipment[slot]?.amount ?: 0}")

            location?.let { loc ->
                val dropRate = getDropRate(e)
                if (dropRate != -1.0) {
                    val flag = RegionManager.getClippingFlag(loc)
                    val isWater = flag and 0x200000 != 0
                    if (!isWater && state.rangeWeapon.dropAmmo) {
                        val chance = 5 * (1.0 + e.skills.getLevel(Skills.RANGE) * 0.01) * dropRate
                        e.debug("Ammo drop chance calculated: $chance")
                        if (RandomFunction.randomize(chance.toInt()) == 0) {
                            val drop = GroundItemManager.increase(Item(itemId, amount), loc, e)
                            drop?.let {
                                e.getAttribute<ArrayList<GroundItem?>>("duel:ammo")?.add(it)
                                e.debug("Ammo dropped: id=$itemId, amount=$amount, location=$loc")
                            }
                        }
                    } else if (isWater) {
                        e.debug("Ammo not dropped because location is water: $loc")
                    }
                }
            }

            if (e.equipment[slot] == null || e.equipment[slot]?.amount == 0) {
                e.packetDispatch.sendMessage("You have no ammo left in your quiver!")
                e.debug("Quiver empty after usage.")
            }
        }

        /**
         * Gets the drop rate modifier for ammo, based on player equipment.
         *
         * @param e The entity performing the attack.
         * @return A double representing the drop rate, -1.0 if dropping is disabled, or 1.0 for normal drop.
         */
        private fun getDropRate(e: Entity?): Double {
            if (e is Player) {
                val cape = e.equipment[EquipmentContainer.SLOT_CAPE]
                val weapon = e.equipment[EquipmentContainer.SLOT_WEAPON]
                if (cape != null && (cape.id == Items.AVAS_ATTRACTOR_10498 || cape.id == Items.AVAS_ACCUMULATOR_10499) &&
                    weapon != null && weapon.id != Items.RED_CHINCHOMPA_10034 && weapon.id != Items.CHINCHOMPA_10033) {
                    val rate = 80
                    if (RandomFunction.random(100) < rate) {
                        val torso = e.equipment[EquipmentContainer.SLOT_CHEST]
                        val modelId = torso?.definition?.maleWornModelId1 ?: -1
                        if (modelId == 301 || modelId == 306 || modelId == 3379) {
                            e.packetDispatch.sendMessage("Your armour interferes with Ava's device.")
                            return 1.0
                        }
                        return (-1).toDouble()
                    }
                    return 0.33
                }
            }
            return 1.0
        }
    }
}

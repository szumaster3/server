package core.game.node.entity.combat

import content.global.ame.RandomEventNPC
import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.combat.equipment.special.SalamanderSwingHandler
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.system.timer.impl.*
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction

/**
 * Handles combat logic and attack cycles for an entity.
 *
 * @author Emperor
 */
class CombatPulse(val entity: Entity?) : Pulse(1, entity, null) {

    /**
     * The current combat target.
     */
    var victim: Entity? = null

    /**
     * The current combat style being used (melee, range, magic).
     */
    var style = CombatStyle.MELEE

    /**
     * Temporary handler for the current combat swing.
     */
    var temporaryHandler: CombatSwingHandler? = null

    /**
     * The default handler for the current combat style.
     */
    var handler = style.swingHandler

    /**
     * The last entity attacked by this entity.
     */
    var lastVictim: Entity? = null

    /**
     * Tick when the next attack can be performed.
     */
    private var nextAttack = -1

    /**
     * Counter for combat timeout when not interacting.
     */
    private var combatTimeOut = 0

    /**
     * Movement logic for pathfinding towards the victim.
     */
    private val movement: MovementPulse

    /**
     * Last attack sent timestamp or counter.
     */
    var lastSentAttack = 0

    /**
     * Last attack received timestamp or counter.
     */
    var lastReceivedAttack = 0

    init {
        running = false
        movement = object : MovementPulse(entity!!, entity) {
            override fun pulse(): Boolean = false
        }
    }

    /**
     * Handles the combat logic on each tick.
     *
     * @return true if combat pulse should stop, false otherwise.
     */
    override fun pulse(): Boolean {
        val e = entity ?: return true
        val v = victim ?: return true
        if (DeathTask.isDead(e) || DeathTask.isDead(v)) return true
        if (!e.viewport.region!!.active || !v.viewport.region!!.active) return true

        if (!interactable()) {
            return if (e.walkingQueue.isMoving) false
            else ++combatTimeOut > e.properties.combatTimeOut
        }

        combatTimeOut = 0
        e.face(v)

        if (nextAttack <= GameWorld.ticks) {
            val handler = temporaryHandler ?: e.getSwingHandler(true)

            if (!v.isAttackable(e, handler.type, true) && e != v.getAttribute<RandomEventNPC?>(
                    AntiMacro.EVENT_NPC, null
                )
            ) return true

            if (!swing(e, v, handler)) {
                temporaryHandler = null
                updateStyle()
                return false
            }

            var speed = e.properties.attackSpeed
            val isMagic = handler.type == CombatStyle.MAGIC
            val isSalamander = handler is SalamanderSwingHandler

            if (e is Player && isMagic) {
                speed = 5
            } else if (e.properties.attackStyle!!.style == WeaponInterface.STYLE_RAPID || (isSalamander && e.properties.attackStyle!!.style == WeaponInterface.STYLE_RANGE_ACCURATE)) {
                speed--
            }

            if (!isMagic && hasTimerActive<Miasmic>(e)) {
                speed = (speed * 1.5).toInt()
            }

            setNextAttack(speed)
            temporaryHandler = null
            setCombatFlags(v)
        }

        return (victim?.skills?.lifepoints ?: 1) < 1 || (entity.skills?.lifepoints ?: 1) < 1
    }

    /**
     * Sets combat flags for the victim and manages interface closing.
     *
     * @param victim The entity receiving combat.
     */
    fun setCombatFlags(victim: Entity) {
        val e = entity ?: return

        if (e is Player && !e.attributes.containsKey("keepDialogueAlive")) {
            e.interfaceManager.close()
            e.interfaceManager.closeChatbox()
        }

        if (victim is Player) {
            if (e is Player && e.skullManager.isWilderness) {
                e.skullManager.checkSkull(victim)
            }
            if (!victim.attributes.containsKey("keepDialogueAlive")) {
                victim.interfaceManager.closeChatbox()
                victim.interfaceManager.close()
            }
        }

        if (!victim.pulseManager.isMovingPulse) {
            victim.pulseManager.clear()
        }

        victim.setAttribute("combat-time", System.currentTimeMillis() + 10_000)
        victim.setAttribute("combat-attacker", e)
    }

    /**
     * Checks if the attacker can interact with the victim.
     *
     * @return true if interaction is possible.
     */
    private fun interactable(): Boolean {
        val e = entity ?: return false
        val v = victim ?: return false

        if (e is NPC && v is Player && e.isHidden(v)) {
            stop()
            return false
        }

        if (v is NPC && e is Player && v.isHidden(e)) {
            stop()
            return false
        }

        if (e is NPC && !e.canStartCombat(v)) {
            stop()
            return false
        }

        val type = canInteract() ?: return false
        if (type == InteractionType.STILL_INTERACT) return true
        if (e.locks.isMovementLocked()) return false

        movement.updatePath()
        return type == InteractionType.MOVE_INTERACT
    }

    /**
     * Updates the combat style based on player current weapon or spells.
     */
    fun updateStyle() {
        val p = entity as? Player ?: return
        style = when {
            p.properties.spell != null -> CombatStyle.MAGIC
            p.properties.autocastSpell != null -> CombatStyle.MAGIC
            p.properties.attackStyle!!.bonusType == WeaponInterface.BONUS_MAGIC -> CombatStyle.MAGIC
            p.properties.attackStyle!!.bonusType == WeaponInterface.BONUS_RANGE -> CombatStyle.RANGE
            else -> CombatStyle.MELEE
        }
    }

    /**
     * Handles an attack on a given target node.
     *
     * @param victim The target node to attack.
     */
    fun attack(victim: Node?) {
        val e = entity ?: return
        if (e.locks.isInteractionLocked() || victim == null) return
        if (victim == this.victim && isAttacking) return

        if (victim is Player && (e.id == 4474 || e.id == 7891)) return
        if (e is Player && (victim.id == 4474 || victim.id == 7891) && e.properties.currentCombatLevel >= 30) {
            e.sendMessage("You are too experienced to gain anything from these.")
            return
        }

        if (victim is NPC) {
            if (e is Player && victim !== this.victim && victim !== lastVictim) {
                val mask = e.equipment[EquipmentContainer.SLOT_HAT]
                if (victim.id == 1240) {
                    victim.animate(Animation(1288, 0, Animator.Priority.VERY_HIGH))
                    victim.transform(1241)
                }

                if (mask != null && mask.id in 8901 until 8920 && RandomFunction.random(50) == 0) {
                    val charges = if (mask.id == 8919) "no" else ((8920 - mask.id) / 2).toString()
                    e.packetDispatch.sendMessage("Your black mask startles your enemy, you have $charges charges left.")
                    e.equipment.replace(Item(mask.id + 2), EquipmentContainer.SLOT_HAT)
                    val drain = (3 + victim.skills.getLevel(Skills.DEFENCE) / 14).coerceAtMost(10)
                    victim.skills.updateLevel(
                        Skills.DEFENCE, -drain, victim.skills.getStaticLevel(Skills.DEFENCE) - drain
                    )
                }
            }
            if (!victim.locks.isMovementLocked()) {
                victim.walkingQueue.reset()
            }
        }

        setVictim(victim)
        e.onAttack(victim as Entity)
        victim.scripts.removeWeakScripts()
        if (!isAttacking) e.pulseManager.run(this)
    }

    /**
     * Sets a new victim to attack and updates movement accordingly.
     *
     * @param victim The new target node.
     */
    fun setVictim(victim: Node?) {
        this.victim = victim as? Entity
        if (victim != null) {
            addNodeCheck(1, victim)
            lastVictim?.location?.let { movement.setLast(it) }
            movement.setDestination(victim)
        }
        combatTimeOut = 0
    }

    /**
     * Sets the tick delay until the next attack.
     *
     * @param ticks Number of ticks to wait.
     */
    fun setNextAttack(ticks: Int) {
        nextAttack = GameWorld.ticks + ticks
    }

    /**
     * Delays the next attack by additional ticks.
     */
    fun delayNextAttack(ticks: Int) {
        nextAttack += ticks
    }

    /**
     * Gets the tick when the next attack can occur.
     */
    fun getNextAttack(): Int = nextAttack

    /**
     * Checks if combat interaction can occur.
     *
     * @return InteractionType or null if no interaction possible.
     */
    fun canInteract(): InteractionType? {
        val e = entity ?: return null
        val v = victim ?: return null
        return temporaryHandler?.canSwing(e, v) ?: e.getSwingHandler(false).canSwing(e, v)
    }

    /**
     * Starts the combat pulse and resets movement/facing.
     */
    override fun start() {
        super.start()
        entity?.face(victim)
        entity?.walkingQueue?.reset()
    }

    /**
     * Stops the combat pulse and clears target info.
     */
    override fun stop() {
        super.stop()
        entity?.setAttribute("combat-stop", GameWorld.ticks)
        lastVictim = victim
        victim = null
        addNodeCheck(1, null as Node?)
        entity?.resetFace()
        entity?.properties?.spell = null
    }

    /**
     * Determines whether to remove this pulse based on pulse type.
     *
     * @param pulseType The pulse type string.
     * @return true if pulse should be removed, false otherwise.
     */
    override fun removeFor(pulseType: String): Boolean {
        if (isAttacking && pulseType.lowercase().startsWith("interaction:attack")) {
            val id = pulseType.substringAfter("interaction:attack:").toIntOrNull()
            if (victim?.hashCode() == id) return false
        }
        return true
    }

    val isAttacking: Boolean
        /**
         * Indicates if the entity is currently attacking.
         */
        get() = victim?.isActive == true && isRunning

    val isInCombat: Boolean
        /**
         * Indicates if the entity is in combat (has attacker attacking).
         */
        get() = entity?.getAttribute<Entity>("combat-attacker")?.properties?.combatPulse?.isAttacking == true


    private fun Entity.resetFace() {
        face(null)
    }

    companion object {
        /**
         * A combat swing from entity to victim using a handler.
         *
         * @param entity The attacker.
         * @param victim The target.
         * @param handler The combat swing handler.
         * @return true if swing started successfully.
         */
        fun swing(entity: Entity, victim: Entity, handler: CombatSwingHandler): Boolean {
            val state = BattleState(entity, victim)
            val set = handler.getArmourSet(entity)
            entity.properties.armourSet = set

            val delay = handler.swing(entity, victim, state)
            if (delay < 0) return false

            entity.faceTemporary(victim, 1)
            handler.adjustBattleState(entity, victim, state)
            handler.addExperience(entity, victim, state)
            handler.visualize(entity, victim, state)

            if (delay - 1 < 1) {
                handler.visualizeImpact(entity, victim, state)
            }

            handler.visualizeAudio(entity, victim, state)

            if (set != null && set.effect(entity, victim, state)) {
                set.visualize(entity, victim)
            }

            GameWorld.Pulser.submit(object : Pulse(delay - 1, entity, victim) {
                var impact = false
                override fun pulse(): Boolean {
                    if (DeathTask.isDead(victim) || DeathTask.isDead(entity)) return true
                    if (entity is NPC) {
                        entity.behavior.beforeAttackFinalized(entity, victim, state)
                    }
                    if (impact || getDelay() == 0) {
                        if (state.estimatedHit != 0) {
                            when (victim) {
                                is NPC -> victim.getAudio(1)?.let {
                                    victim.location?.let { loc -> playGlobalAudio(loc, it.id) }
                                }

                                is Player -> playHurtAudio(victim)
                            }
                        }
                        handler.impact(entity, victim, state)
                        handler.onImpact(entity, victim, state)
                        return true
                    }
                    setDelay(1)
                    impact = true
                    handler.visualizeImpact(entity, victim, state)
                    return false
                }
            })

            return true
        }
    }
}

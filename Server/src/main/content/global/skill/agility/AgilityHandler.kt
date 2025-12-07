package content.global.skill.agility

import content.data.LightSources
import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.interaction.MovementPulse
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RED
import core.tools.RandomFunction
import shared.consts.Items
import kotlin.random.Random

/**
 * Holds agility-related utility methods.
 *
 * @author Emperor
 */
object AgilityHandler {
    /**
     * Delay between interactions.
     */
    var delay = 0

    /**
     * Determines if the player fails an agility obstacle.
     */
    @JvmStatic
    fun hasFailed(player: Player, level: Int, failChance: Double): Boolean {
        val levelDiff = player.skills.getLevel(Skills.AGILITY) - level
        if (levelDiff > 69) return false
        val chance = (1 + levelDiff) * 0.01 * Random.nextDouble()
        return chance <= (Random.nextDouble() * failChance)
    }

    /**
     * Executes a failing force movement and hits/damages the player after.
     */
    @JvmStatic
    fun failWalk(
        player: Player,
        delay: Int,
        start: Location,
        end: Location,
        destination: Location,
        anim: Animation,
        speed: Int,
        hit: Int,
        message: String?,
        direction: Direction? = null
    ): ForceMovement {
        val movement = object : ForceMovement(player, start, end, anim, speed) {
            override fun stop() {
                super.stop()
                player.properties.teleportLocation = destination
                if (hit > 0) {
                    player.impactHandler.disabledTicks = 0
                    player.impactHandler.manualHit(player, hit, HitsplatType.NORMAL)
                }
                if (message != null) {
                    player.packetDispatch.sendMessage(message)
                }
            }
        }
        direction?.let { movement.direction = it }
        movement.delay = delay
        movement.start()
        GameWorld.Pulser.submit(movement)
        return movement
    }

    @JvmStatic
    fun failWalk(
        player: Player,
        delay: Int,
        start: Location,
        end: Location,
        destination: Location,
        anim: Animation,
        speed: Int,
        hit: Int,
        message: String?
    ):
            ForceMovement = failWalk(player, delay, start, end, destination, anim, speed, hit, message, null)

    /**
     * Teleports player and deals damage after a delay on failure.
     */
    @JvmStatic
    fun fail(player: Player, delay: Int, destination: Location, anim: Animation?, hit: Int, message: String?) {
        if (anim == null) return
        animate(player, anim, true)
        submitWorldPulse(object : Pulse(animationDuration(anim), player) {
            var dmg = false

            override fun pulse(): Boolean {
                teleport(player, destination, TeleportManager.TeleportType.INSTANT)
                animate(player, Animation.RESET, true)
                if (!dmg) {
                    if (hit > 0) {
                        player.impactHandler.disabledTicks = 0
                        impact(player, hit, HitsplatType.NORMAL)
                    }
                    message?.let { sendMessage(player, it) }
                    dmg = true
                }
                this.delay = 0
                return player.location == destination
            }
        })
    }

    /**
     * Forces player to walk with animation and gain XP.
     */
    @JvmStatic
    fun forceWalk(player: Player, courseIndex: Int, start: Location, end: Location, anim: Animation, speed: Int, experience: Double, message: String?): ForceMovement {
        registerLogoutListener(player, "forcewalk") { p ->
            p.location = player.location.transform(0, 0, 0)
        }

        lock(player, (start.getDistance(end).toInt()) * 3)

        val movement = object : ForceMovement(player, start, end, anim, speed) {
            override fun stop() {
                super.stop()
                message?.let { player.packetDispatch.sendMessage(it) }
                if (experience > 0.0) player.skills.addExperience(Skills.AGILITY, experience, true)
                setObstacleFlag(player, courseIndex)
                clearLogoutListener(player, "forcewalk")
            }
        }
        movement.start()
        GameWorld.Pulser.submit(movement)
        return movement
    }

    @JvmStatic
    fun forceWalk(player: Player, courseIndex: Int, start: Location, end: Location, anim: Animation, speed: Int, experience: Double, message: String?, delay: Int): ForceMovement {
        registerLogoutListener(player, "forcewalk") { p ->
            p.location = player.location.transform(0, 0, 0)
        }
        lock(player, (start.getDistance(end).toInt()) * 3)

        if (delay < 1) {
            return forceWalk(player, courseIndex, start, end, anim, speed, experience, message)
        }

        val movement = object : ForceMovement(player, start, end, anim, speed) {
            override fun stop() {
                super.stop()
                message?.let { sendMessage(player, it) }
                if (experience > 0.0) rewardXP(player, Skills.AGILITY, experience)
                setObstacleFlag(player, courseIndex)
                clearLogoutListener(player, "forcewalk")
            }
        }

        GameWorld.Pulser.submit(object : Pulse(delay, player) {
            override fun pulse(): Boolean {
                movement.start()
                GameWorld.Pulser.submit(movement)
                return true
            }
        })

        return movement
    }

    /**
     * Performs climbing movement.
     */
    @JvmStatic
    fun climb(player: Player, courseIndex: Int, animation: Animation, destination: Location, experience: Double, message: String?, delay: Int = 2) {
        lock(player, delay + 1)
        animate(player, animation)
        queueScript(player, delay, QueueStrength.SOFT) {
            message?.let { sendMessage(player, it) }
            if (experience > 0.0) rewardXP(player, Skills.AGILITY, experience)
            teleport(player, destination)
            setObstacleFlag(player, courseIndex)
            return@queueScript stopExecuting(player)
        }
    }

    /**
     * Performs walking animation over obstacle.
     */
    @JvmStatic
    fun walk(player: Player, courseIndex: Int, start: Location, end: Location, animation: Animation?, experience: Double, message: String?, infiniteRun: Boolean = false) {
        if (player.location != start) {
            player.pulseManager.run(object : MovementPulse(player, start) {
                override fun pulse(): Boolean {
                    walk(player, courseIndex, start, end, animation, experience, message, infiniteRun)
                    return true
                }
            }, PulseType.STANDARD)
            return
        }

        player.walkingQueue.reset()
        player.walkingQueue.addPath(end.x, end.y, !infiniteRun)

        val ticks = player.walkingQueue.queue.size
        player.impactHandler.disabledTicks = ticks
        lock(player, 1 + ticks)

        registerLogoutListener(player, "agility") { p ->
            p.location = start
        }

        animation?.let { player.appearance.setAnimations(it) }

        GameWorld.Pulser.submit(object : Pulse(ticks, player) {
            override fun pulse(): Boolean {
                animation?.let {
                    player.appearance.setAnimations()
                    refreshAppearance(player)
                }
                message?.let { sendMessage(player, it) }
                if (experience > 0.0) rewardXP(player, Skills.AGILITY, experience)
                setObstacleFlag(player, courseIndex)
                clearLogoutListener(player, "agility")
                return true
            }
        })
    }

    /**
     * Flags obstacle as completed.
     */
    @JvmStatic
    fun setObstacleFlag(player: Player, courseIndex: Int) {
        if (courseIndex < 0) return
        val course = player.getExtension<AgilityCourse>(AgilityCourse::class.java)
        if (course != null && courseIndex < course.getPassedObstacles().size) {
            course.flag(courseIndex)
        }
    }

    /**
     * Handles the potential break of a mithril grapple after use.
     */
    @JvmStatic
    fun checkGrappleBreak(player: Player) {
        val ammo = player.equipment.get(EquipmentContainer.SLOT_ARROWS) ?: return
        if (ammo.id != Items.MITH_GRAPPLE_9419) return

        if (RandomFunction.random(25) == 0) {
            player.equipment.replace(null, EquipmentContainer.SLOT_ARROWS)

            val broken = Item(Items.MITH_GRAPPLE_9418)
            if (player.inventory.hasSpaceFor(broken)) {
                player.inventory.add(broken)
            } else {
                GroundItemManager.create(broken, player.location, player)
            }

            sendMessage(player, "${RED}After untying the rope, you find that your grapple is broken and useless.")
        }
    }

    /**
     * Called when a player falls into water after failing an Agility shortcut.
     * Extinguishes any temporary light source the player has.
     */
    @JvmStatic
    fun extinguishLightOnWater(player: Player) {
        val source = LightSources.getActiveLightSource(player) ?: return
        if (player.inventory.contains(source.litId, 1)) {
            player.inventory.remove(Item(source.litId))
            if (source.emptyId > 0) player.inventory.add(Item(source.emptyId))
        } else if (player.equipment.contains(source.litId, 1)) {
            player.equipment.remove(Item(source.litId))
            if (source.emptyId > 0) player.inventory.add(Item(source.emptyId))
        }
        sendMessage(player, "${RED}You fall into the water and your ${source.name.lowercase()} goes out!")
        // checkDarkArea(player)
    }
}

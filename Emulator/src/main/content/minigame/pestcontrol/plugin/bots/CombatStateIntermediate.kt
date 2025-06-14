package content.minigame.pestcontrol.plugin.bots

import content.minigame.pestcontrol.plugin.PCHelper.GATE_ENTRIES
import content.minigame.pestcontrol.plugin.PCHelper.getMyPestControlSession2
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import core.tools.RandomFunction
import java.util.*

/**
 * Handles combat behavior for an intermediate-level Pest Control bot.
 *
 * @param bot The [PestControlIntermediateBot].
 */
class CombatStateIntermediate(
    val bot: PestControlIntermediateBot,
) {
    private val random = Random()
    private val randomType = random.nextInt(100)

    companion object {
        const val STATE_MOVING_TO_PORTALS = "MOVING_TO_PORTALS"
        const val STATE_ATTACKING_PORTAL = "ATTACKING_PORTAL"
        const val STATE_ATTACKING_SPINNER = "ATTACKING_SPINNER"
        const val STATE_OPENING_GATE = "OPENING_GATE"
        const val STATE_IDLE_RANDOM_WALK = "IDLE_RANDOM_WALK"
        const val STATE_ATTACKING_NPC = "ATTACKING_NPC"
        const val STATE_NO_TARGETS = "NO_TARGETS_AVAILABLE"
    }

    /**
     * Moves the bot to the nearest portal or performs an action depending on its current state.
     */
    fun goToPortals() {
        bot.customState = STATE_MOVING_TO_PORTALS

        val gate = bot.getClosestNodeWithEntry(75, GATE_ENTRIES)
        val session = getMyPestControlSession2(bot)

        var portal: Node? = null
        if (session != null && session.aportals.isNotEmpty()) {
            val removeList = ArrayList<NPC>()
            for (port in session.aportals) {
                if (!port.isActive) {
                    removeList.add(port)
                } else {
                    portal = port
                    break
                }
            }
            session.aportals.removeAll(removeList)
        }

        if (bot.pulseManager.hasPulseRunning() && portal == null) {
            return
        }

        if (bot.justStartedGame) {
            bot.customState = STATE_IDLE_RANDOM_WALK
            bot.justStartedGame = false
            bot.randomWalkAroundPoint(session?.squire?.location ?: bot.location, 15)
            bot.movetimer = random.nextInt(7) + 6
            return
        }

        if (gate != null && session?.aportals?.isEmpty() == true) {
            bot.customState = "$STATE_OPENING_GATE ID=${gate.id}"
            InteractionListeners.run(gate.id, IntType.SCENERY, "open", bot, gate)
            bot.openedGate = true
            if (random.nextInt(4) == 1 && randomType < 40) {
                bot.movetimer = random.nextInt(2) + 1
            }
            return
        }

        if (portal != null) {
            if (bot.location.withinDistance(portal.location, 10) && portal.isActive) {
                val spinners = ArrayList<NPC>()
                RegionManager.getLocalNpcs(bot).forEach {
                    if (it.name.equals("spinner", ignoreCase = true) && it.location.withinDistance(bot.location, 10)) {
                        spinners.add(it)
                    }
                }
                if (spinners.isNotEmpty()) {
                    bot.customState = STATE_ATTACKING_SPINNER
                    bot.attack(spinners.random())
                } else {
                    bot.customState = STATE_ATTACKING_PORTAL
                    bot.attack(portal)
                }
            } else {
                bot.customState = STATE_MOVING_TO_PORTALS
                randomWalkTo(portal.location, 5)
            }
            bot.movetimer = random.nextInt(10) + 5
            return
        } else {
            bot.customState = STATE_ATTACKING_NPC
            bot.AttackNpcsInRadius(50)
        }

        bot.customState = STATE_NO_TARGETS
    }

    /**
     * Makes the bot fight NPCs within a specified radius.
     */
    fun fightNPCs() {
        bot.customState = STATE_ATTACKING_NPC
        bot.AttackNpcsInRadius(8)
        bot.eat(379)

        if (!bot.inCombat()) {
        }
    }

    /**
     * Moves the bot to a random location within a specified radius of a target location.
     *
     * @param loc The target location to walk to.
     * @param radius The radius within which the bot will move randomly.
     */
    fun randomWalkTo(loc: Location, radius: Int) {
        val newLoc = loc.transform(
            RandomFunction.random(radius, -radius),
            RandomFunction.random(radius, -radius),
            0,
        )
        if (!bot.walkingQueue.isMoving) {
            walkToIterator(newLoc)
        }
    }

    /**
     * Makes the bot walk to a specific location.
     *
     * @param loc The location that the bot will walk to.
     */
    private fun walkToIterator(loc: Location) {
        var diffX = loc.x - bot.location.x
        var diffY = loc.y - bot.location.y

        GameWorld.Pulser.submit(
            object : MovementPulse(bot, bot.location.transform(diffX, diffY, 0), Pathfinder.SMART) {
                override fun pulse(): Boolean = true
            },
        )
    }
}

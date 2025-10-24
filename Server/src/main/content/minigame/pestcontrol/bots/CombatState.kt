package content.minigame.pestcontrol.bots

import content.minigame.pestcontrol.plugin.PCUtils
import content.minigame.pestcontrol.plugin.PestControlSession
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
import kotlin.random.Random

/**
 * Represents the combat controller for Pest control script.
 * @property bot The [PestControlScript] instance using this combat controller.
 */
class CombatState(private val bot: PestControlScript) {

    /**
     * The combat-related states.
     */
    enum class State {
        MOVING_TO_PORTAL,
        ATTACKING_PORTAL,
        ATTACKING_SPINNER,
        ATTACKING_NPC,
        OPENING_GATE,
    }

    /**
     * Main offensive logic.
     */
    fun handleCombat() {
        val session = PCUtils.getMyPestControlSession(bot) ?: return
        val gate = bot.getClosestNodeWithEntry(75, PCUtils.GATE_ENTRIES.toMutableList())
        val portal = session.aportals.firstOrNull { it.isActive }

        when {
            bot.start -> startRound(session)
            gate != null && session.aportals.isEmpty() -> {
                if (!bot.location.withinDistance(gate.location, 1)) {
                    move(gate.location, 1)
                    bot.customState = "Moving to gate ${gate.id}"
                } else {
                    openGate(gate)
                }
            }
            portal != null -> attackOrMoveToPortal(portal)
            else -> fallbackToNearbyNPCs()
        }
    }

    /**
     * Defensive logic used primarily for bots assigned to protect the squire.
     */
    fun handleDefense() {
        bot.customState = State.ATTACKING_NPC.name
        bot.AttackNpcsInRadius(aggressionRange())
        if (bot.skills.lifepoints < bot.skills.maximumLifepoints * 0.6) bot.eat(379)
    }

    /**
     * Initializes the bots behavior.
     *
     * @param session The current [PestControlSession] the bot belongs to.
     */
    private fun startRound(session: PestControlSession?) {
        bot.customState = "Starting round"
        bot.start = false
        val squireLoc = session?.squire?.location ?: bot.location
        bot.randomWalkAroundPoint(squireLoc, 10)
        bot.moveTimer = Random.nextInt(5, 10)
    }

    /**
     * Attempts to open a nearby gate if one is found within range.
     *
     * @param gate The gate [Node] to interact with.
     */
    private fun openGate(gate: Node) {
        if (bot.openedGate || gatesInUse.contains(gate.id)) return
        gatesInUse.add(gate.id)

        if (!bot.location.withinDistance(gate.location, 1)) {
            move(gate.location, 1)
            bot.customState = "Moving to gate ${gate.id}"
            return
        }

        InteractionListeners.run(gate.id, IntType.SCENERY, "open", bot, gate)
        bot.customState = "Activated gate ${gate.id}"
        bot.openedGate = true
        bot.moveTimer = Random.nextInt(3, 6)

        GameWorld.Pulser.submit(object : MovementPulse(bot, bot.location, Pathfinder.SMART) {
            private var ticks = 0
            override fun pulse(): Boolean {
                ticks++
                if (ticks >= 5) {
                    gatesInUse.remove(gate.id)
                    return true
                }
                return false
            }
        })
    }

    /**
     * Handles approaching and attacking Pest Control portals.
     *
     * @param portal The portal [Node] to engage with.
     */
    private fun attackOrMoveToPortal(portal: Node) {
        if (bot.location.withinDistance(portal.location, aggressionRange())) {
            val spinner = findSpinnerNPC()
            if (spinner != null) attack(spinner, State.ATTACKING_SPINNER)
            else attack(portal, State.ATTACKING_PORTAL)
        } else {
            move(portal.location)
        }
        bot.moveTimer = Random.nextInt(5, 12)
    }

    /**
     * Fallback behavior when no portals are available.
     */
    fun fallbackToNearbyNPCs() {
        bot.customState = State.ATTACKING_NPC.name
        bot.AttackNpcsInRadius(aggressionRange())
        if (Random.nextInt(100) < 30) bot.eat(379)
    }

    /**
     * Finds the nearest spinner NPC within 10 tiles of the bot.
     *
     * @return The [NPC] instance if found, otherwise null.
     */
    private fun findSpinnerNPC(): NPC? =
        RegionManager.getLocalNpcs(bot, 10).firstOrNull { it.name.equals("spinner", ignoreCase = true) }

    /**
     * Moves the bot toward a specified location.
     *
     * @param target The [Location] to move toward.
     * @param radius Optional random offset radius for more natural movement.
     */
    fun move(target: Location, radius: Int = moveRadius()) {
        if (bot.walkingQueue.isMoving) return

        bot.customState = State.MOVING_TO_PORTAL.name
        val dest = Location(
            target.x + RandomFunction.random(-radius, radius),
            target.y + RandomFunction.random(-radius, radius),
            target.z
        )

        GameWorld.Pulser.submit(object : MovementPulse(bot, dest, Pathfinder.SMART) {
            override fun pulse(): Boolean = true
        })
    }

    /**
     * Performs a direct attack on a given target.
     *
     * @param target The target [Node] (NPC or portal).
     * @param action The current [State] describing the type of attack.
     */
    fun attack(target: Node, action: State) {
        bot.customState = action.name
        bot.attack(target)
    }

    /**
     * Defines the bot combat aggression range based on its lander type.
     *
     * @return The attack range (in tiles).
     */
    private fun aggressionRange(): Int = when (bot.lander) {
        PCUtils.LanderZone.NOVICE -> 8
        PCUtils.LanderZone.INTERMEDIATE -> 10
        PCUtils.LanderZone.VETERAN -> 12
    }

    /**
     * Defines how far the bot will move when navigating randomly or repositioning.
     *
     * @return The movement radius (in tiles).
     */
    private fun moveRadius(): Int = when (bot.lander) {
        PCUtils.LanderZone.NOVICE -> 5
        PCUtils.LanderZone.INTERMEDIATE -> 7
        PCUtils.LanderZone.VETERAN -> 9
    }

    companion object {
        private val gatesInUse = mutableSetOf<Int>()
    }
}

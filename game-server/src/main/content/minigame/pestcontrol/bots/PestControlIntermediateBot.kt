package content.minigame.pestcontrol.bots

import content.minigame.pestcontrol.plugin.PCHelper
import content.minigame.pestcontrol.plugin.PestControlActivityPlugin
import core.game.bots.CombatBotAssembler
import core.game.bots.PvMBots
import core.game.world.map.Location
import core.tools.RandomFunction
import java.util.*

/**
 * Represents an intermediate difficulty Pest Control bot.
 *
 * This bot can attack portals, defend the squire, and navigate between
 * the boat and the Pest Control battlefield.
 *
 * @param l The initial spawn location for the bot.
 */
class PestControlIntermediateBot(
    l: Location,
) : PvMBots(legitimizeLocation(l)) {

    /**
     * General tick counter.
     */
    var tick = 0

    /**
     * Timer tracking when to perform a combat move.
     */
    var combatMoveTimer = 0

    /**
     * Flag indicating whether the bot has just started a game.
     */
    var justStartedGame = true

    /**
     * Timer controlling movement actions.
     */
    var movetimer = 0

    /**
     * Whether the bot has opened the gate at the start.
     */
    var openedGate = false

    /**
     * Internal action counter for miscellaneous logic.
     */
    var myCounter = 0

    /**
     * Random number used for bot behavior variation.
     */
    val num = Random().nextInt(4)

    /**
     * The Pest Control boat this bot uses (Intermediate boat).
     */
    val myBoat = PCHelper.BoatInfo.INTERMEDIATE

    /**
     * The bot's combat behavior handler.
     */
    val combathandler = CombatStateIntermediate(this)

    /**
     * Counter used for time-based transitions.
     */
    var time = 0

    /**
     * States representing what the bot is currently doing.
     */
    enum class State {
        /**
         * Bot is outside the gangplank waiting to enter the boat.
         */
        OUTSIDE_GANGPLANK,

        /**
         * Bot is refreshing its state, e.g., teleporting to the boat.
         */
        REFRESH,

        /**
         * Bot is waiting inside the boat for the game to start.
         */
        WAITING_IN_BOAT,

        /**
         * Bot is actively participating in the Pest Control game.
         */
        PLAY_GAME,

        /**
         * Bot is trying to get to the Pest Control area.
         */
        GET_TO_PC,
    }

    init {
        val random100 = Random().nextInt(100)
        if (random100 < 30) {
            setAttribute("pc_role", "defend_squire")
        } else {
            setAttribute("pc_role", "attack_portals")
            this.customState = "Fighting NPCs"
        }
        if (num <= 2) {
            CombatBotAssembler().gearPCiMeleeBot(this)
        } else {
            CombatBotAssembler().gearPCiRangedBot(this, Random().nextInt() % 2 == 0)
        }
    }

    /**
     * Main tick method called each game cycle to update bot behavior.
     */
    override fun tick() {
        super.tick()
        tick++
        time++
        movetimer--
        if (movetimer <= 0) {
            movetimer = 0
            customState = state.toString() + movetimer
            when (state) {
                State.GET_TO_PC -> handlePestControl()
                State.OUTSIDE_GANGPLANK -> handleBoatInteraction()
                State.WAITING_IN_BOAT -> handleBoatIdle()
                State.PLAY_GAME -> handleAttack()
                State.REFRESH -> handlePestControl()
            }
        }
    }

    /**
     * Retrieves the current state of the bot based on its location.
     */
    private val state: State
        get() {
            if (PCHelper.landerContainsLoc2(this.getLocation())) {
                return State.WAITING_IN_BOAT
            }
            if (PCHelper.isInPestControlInstance(this)) {
                return State.PLAY_GAME
            }
            if (PCHelper.outsideGangplankContainsLoc2(this.getLocation())) {
                return State.OUTSIDE_GANGPLANK
            }
            if (time == 1200) {
                return State.GET_TO_PC
            }
            return State.GET_TO_PC
        }

    /**
     * Handles bot behavior during the Pest Control game (attacking portals, defending squire).
     */
    private fun handleAttack() {
        if (PCHelper.outsideGangplankContainsLoc2(getLocation())) {
            PestControlActivityPlugin().leave(this, false)
            val test = getClosestNodeWithEntry(50, myBoat.ladderId)
            test ?: println("PC: Gangplank Null")
            test!!.interaction.handle(this, test.interaction[0])
        }
        walkingQueue.isRunning = true

        if (getAttribute("pc_role", "E") == "attack_portals") {
            combathandler.goToPortals()
        } else {
            movetimer = RandomFunction.random(2, 10)
            val location = PCHelper.getMyPestControlSession2(this)?.squire?.location ?: location
            if (location != null) {
                randomWalkAroundPoint(location, 5)
            }
            combathandler.fightNPCs()
        }
    }

    /**
     * Internal counter for controlling random movement inside the boat.
     */
    private var insideBoatWalks = 3

    /**
     * Handles bot behavior while waiting inside the boat.
     */
    private fun handleBoatIdle() {
        justStartedGame = true
        openedGate = false
        time = 0
        if (prayer.active.isNotEmpty()) {
            prayer.reset()
        }
        if (PCHelper.outsideGangplankContainsLoc2(getLocation())) {
            val test = getClosestNodeWithEntry(15, myBoat.ladderId)
            test!!.interaction.handle(this, test.interaction[0])
            handleBoatInteraction()
        }
        if (Random().nextInt(100) < 40) {
            if (Random().nextInt(insideBoatWalks) <= 1) {
                (insideBoatWalks * 1.5).toInt()
                if (Random().nextInt(4) == 1) {
                    this.walkingQueue.isRunning = !this.walkingQueue.isRunning
                }
                if (Random().nextInt(7) >= 4) {
                    this.walkToPosSmart(myBoat.boatBorder.randomLoc)
                }
            }
            if (Random().nextInt(3) == 1) {
                insideBoatWalks += 2
            }
        }
    }

    /**
     * Handles interactions outside the boat and movement towards the gangplank.
     */
    private fun handleBoatInteraction() {
        if (PCHelper.outsideGangplankContainsLoc2(getLocation())) {
            movetimer = Random().nextInt(10)
            combathandler.randomWalkTo(PCHelper.PestControlLanderIntermediate, 1)
        }
        if (prayer.active.isNotEmpty()) {
            prayer.reset()
        }
        if (Random().nextInt(8) >= 3) {
            val pclocs = Location.create(2652, 2646, 0)
            combathandler.randomWalkTo(pclocs, 8)
            movetimer = Random().nextInt(180) + 15
        }
        if (Random().nextInt(8) >= 2) {
            randomWalk(3, 3)
            movetimer = Random().nextInt(10)
        }
        if (Random().nextInt(100) > 50) {
            if (Random().nextInt(10) <= 5) {
                this.walkToPosSmart(myBoat.outsideBoatBorder.randomLoc)
                movetimer += RandomFunction.normalPlusWeightRandDist(400, 200)
            }
            movetimer = RandomFunction.normalPlusWeightRandDist(100, 50)
            return
        }
        val test = getClosestNodeWithEntry(15, myBoat.ladderId)
        test ?: randomWalk(1, 1)
        test?.interaction?.handle(this, test.interaction[0])
        insideBoatWalks = 3
    }

    /**
     * Flag used to manage teleportation logic when returning to the boat.
     */
    var switch = false

    /**
     * Handles returning to Pest Control if the bot is misplaced or too far.
     */
    private fun handlePestControl() {
        time = 0
        if (!switch) {
            this.teleport(PCHelper.PestControlLanderIntermediate)
            switch = true
            return
        }
        if (switch) {
            val test = getClosestNodeWithEntry(30, myBoat.ladderId)
            if (test == null) {
                switch = false
                this.teleport(PCHelper.PestControlLanderIntermediate)
                State.OUTSIDE_GANGPLANK
            } else {
                switch = false
                test.interaction.handle(this, test.interaction[0])
                State.OUTSIDE_GANGPLANK
            }
        }
    }

    companion object {
        /**
         * Adjusts the location to the center of the Intermediate lander if the bot's location is invalid.
         *
         * @param l The current location.
         * @return A valid location inside the Intermediate lander if necessary, otherwise the original location.
         */
        fun legitimizeLocation(l: Location): Location =
            if (PCHelper.landerContainsLoc(l)) Location(2648, 2648, 0) else l
    }
}

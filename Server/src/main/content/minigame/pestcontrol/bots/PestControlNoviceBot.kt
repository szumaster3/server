package content.minigame.pestcontrol.bots

import content.minigame.pestcontrol.plugin.PCHelper
import content.minigame.pestcontrol.plugin.PestControlActivityPlugin
import core.game.bots.CombatBotAssembler
import core.game.bots.PvMBots
import core.game.world.map.Location
import core.tools.RandomFunction
import java.util.*

/**
 * A novice-level Pest Control bot that participates in the Pest Control minigame.
 */
class PestControlNoviceBot(
    l: Location,
) : PvMBots(legitimizeLocation(l)) {
    var tick = 0
    var combatMoveTimer = 0
    var justStartedGame = true
    var movetimer = 0
    var openedGate = false
    var myCounter = 0
    val num = Random().nextInt(4)
    val myBoat = PCHelper.BoatInfo.NOVICE
    val combathandler = CombatState(this)

    var time = 0

    enum class State {
        REFRESH,
        OUTSIDE_GANGPLANK,
        WAITING_IN_BOAT,
        PLAY_GAME,
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
            CombatBotAssembler().meleeBotNovice(this)
        } else {
            CombatBotAssembler().rangeBotNovice(this, Random().nextInt() % 2 == 0)
        }
    }

    /**
     * Called every game tick. Handles moving between states and performing actions based on current state.
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
                State.OUTSIDE_GANGPLANK -> handleBoat()
                State.WAITING_IN_BOAT -> handleBoatIdle()
                State.PLAY_GAME -> handleAttack()
                State.REFRESH -> handlePestControl()
            }
        }
    }

    /**
     * Returns the current state of the bot based on its location and internal timers.
     */
    val state: State
        get() {
            if (PCHelper.landerContainsLoc(this.getLocation())) {
                return State.WAITING_IN_BOAT
            }
            if (PCHelper.isInPestControlInstance(this)) {
                return State.PLAY_GAME
            }
            if (PCHelper.outsideGangplankContainsLoc(this.getLocation())) {
                return State.OUTSIDE_GANGPLANK
            }
            if (time == 600) {
                return State.GET_TO_PC
            }
            return State.GET_TO_PC
        }

    fun handleGate(): Boolean {
        if (!openedGate) {
            val gate = getClosestNodeWithEntry(75, PCHelper.GATE_ENTRIES)
            if (gate != null) {
                this.customState = "Opening gate ID ${gate.id}"
                this.interact(gate)
                movetimer = RandomFunction.random(2, 10)
                openedGate = true
                return true
            }
        }
        return false
    }
    
    /**
     * Handles combat behavior when the bot is inside the Pest Control game instance.
     */
    fun handleAttack() {
        if (PCHelper.outsideGangplankContainsLoc(getLocation())) {
            PestControlActivityPlugin().leave(this, false)
            val test = getClosestNodeWithEntry(50, myBoat.ladderId)
            test?.interaction?.handle(this, test.interaction[0])
        }

        if (handleGate()) {
            return
        }

        walkingQueue.isRunning = true

        if (getAttribute("pc_role", "E") == "attack_portals") {
            combathandler.handlePortal()
        } else {
            movetimer = RandomFunction.random(2, 10)
            randomWalkAroundPoint(PCHelper.getMyPestControlSession1(this)?.squire?.location ?: location, 5)
            combathandler.handleAttack()
        }
    }

    var insideBoatWalks = 3

    /**
     * Handles idle behavior while waiting inside the Pest Control boat.
     */
    fun handleBoatIdle() {
        justStartedGame = true
        openedGate = false
        time = 0
        if (!prayer.active.isEmpty()) {
            prayer.reset()
        }
        if (PCHelper.outsideGangplankContainsLoc(getLocation())) {
            val test = getClosestNodeWithEntry(15, myBoat.ladderId)
            test!!.interaction.handle(this, test.interaction[0])
            handleBoat()
        }
        if (Random().nextInt(100) < 40) {
            if (Random().nextInt(insideBoatWalks) <= 1) {
                (insideBoatWalks * 1.5).toInt()

                if (Random().nextInt(4) == 1) {
                    this.walkingQueue.isRunning = !this.walkingQueue.isRunning
                }
                if (Random().nextInt(7) >= 4) {
                    this.walkToPosSmart(myBoat.boatBorder.randomLoc!!)
                }
            }
            if (Random().nextInt(3) == 1) {
                insideBoatWalks += 2
            }
        }
    }

    /**
     * Handles behavior when the bot is outside the boat but not yet inside the game.
     */
    fun handleBoat() {
        if (PCHelper.outsideGangplankContainsLoc(getLocation())) {
            movetimer = Random().nextInt(10)
            combathandler.randomWalkTo(PCHelper.PestControlLanderNovice, 1)
        }
        if (!prayer.active.isEmpty()) {
            prayer.reset()
        }
        if (Random().nextInt(8) >= 4) {
            val pclocs = Location.create(2658, 2659, 0)
            combathandler.randomWalkTo(pclocs, 12)
            movetimer = Random().nextInt(300) + 30
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

    var switch = false

    /**
     * Handles teleportation and boarding when the bot needs to return to the Pest Control area.
     */
    fun handlePestControl() {
        time = 0
        if (!switch) {
            this.teleport(PCHelper.PestControlLanderNovice)
            switch = true
            return
        }
        if (switch) {
            val test = getClosestNodeWithEntry(30, myBoat.ladderId)
            if (test == null) {
                switch = false
                this.teleport(PCHelper.PestControlLanderNovice)
                State.OUTSIDE_GANGPLANK
            } else {
                switch = false
                test.interaction.handle(this, test.interaction[0])
                State.OUTSIDE_GANGPLANK
            }
        }
    }

    companion object {
        fun legitimizeLocation(l: Location): Location = if (PCHelper.landerContainsLoc(l)) Location(2660, 2648, 0) else l
    }
}

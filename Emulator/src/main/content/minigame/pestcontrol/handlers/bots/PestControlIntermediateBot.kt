package content.minigame.pestcontrol.handlers.bots

import content.minigame.pestcontrol.handlers.PCHelper
import content.minigame.pestcontrol.handlers.PestControlActivityPlugin
import core.game.bots.CombatBotAssembler
import core.game.bots.PvMBots
import core.game.world.map.Location
import core.tools.RandomFunction
import java.util.*

class PestControlIntermediateBot(
    l: Location,
) : PvMBots(legitimizeLocation(l)) {
    var tick = 0
    var combatMoveTimer = 0
    var justStartedGame = true
    var movetimer = 0
    var openedGate = false
    var myCounter = 0
    val num = Random().nextInt(4)
    val myBoat = PCHelper.BoatInfo.INTERMEDIATE
    val combathandler = CombatStateIntermediate(this)

    var time = 0

    enum class State {
        OUTSIDE_GANGPLANK,
        REFRESH,
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
            CombatBotAssembler().gearPCiMeleeBot(this)
        } else {
            CombatBotAssembler().gearPCiRangedBot(this, Random().nextInt() % 2 == 0)
        }
    }

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

    private fun handleAttack() {
        if (PCHelper.outsideGangplankContainsLoc2(getLocation())) {
            PestControlActivityPlugin().leave(this, false)
            val test = getClosestNodeWithEntry(50, myBoat.ladderId)
            test ?: println("PC: Gangplank Null")
            test!!.interaction.handle(
                this,
                test.interaction[0],
            )
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

    private var insideBoatWalks = 3

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

    private fun handleBoatInteraction() {
        if (PCHelper.outsideGangplankContainsLoc2(getLocation())) {
            movetimer = Random().nextInt(10)
            combathandler.randomWalkTo(PCHelper.PestControlLanderIntermediate, 1)
        }
        if (!prayer.active.isEmpty()) {
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

    var switch = false

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
        fun legitimizeLocation(l: Location): Location = if (PCHelper.landerContainsLoc(l)) Location(2648, 2648, 0) else l
    }
}

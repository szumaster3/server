package content.minigame.pest_control.bots

import content.minigame.pest_control.plugin.PCUtils
import content.minigame.pest_control.plugin.PestControlActivityPlugin
import core.game.bots.CombatBotAssembler
import core.game.bots.PvMBots
import core.game.world.map.Location
import core.tools.RandomFunction
import kotlin.random.Random

class PestControlScript(location: Location, val lander: PCUtils.LanderZone) :
    PvMBots(landerLocation(location, lander)) {

    private var tick = 0
    private val random = Random(System.nanoTime())
    private var insideBoatWalks = 3
    private var justTeleported = false
    var moveTimer = 0
    var openedGate = false
    var start = true
    private var inPostGame = false
    private var switch = false

    private val combatHandler = CombatState(this)
    private val role = if (random.nextInt(100) < 30) "defend_squire" else "attack_portals"

    init {
        setAttribute("pc_role", role)
        customState = if (role == "attack_portals") "Fighting NPCs" else "Defending squire"
        assignCombatLoadOut()
    }

    enum class State {
        OUTSIDE_GANGPLANK,
        WAITING_IN_BOAT,
        PLAY_GAME,
        GET_TO_PC,
        POST_GAME
    }

    override fun tick() {
        super.tick()
        tick++
        if (moveTimer > 0) moveTimer--

        when (state) {
            State.GET_TO_PC -> handleTeleportOrMove()
            State.OUTSIDE_GANGPLANK -> handleOutsideBoat()
            State.WAITING_IN_BOAT -> handleBoatIdle()
            State.PLAY_GAME -> handleInGame()
            State.POST_GAME -> handlePostGameReturn()
        }
    }

    private val state: State
        get() =
            when {
                inPostGame -> State.POST_GAME
                PCUtils.isInLander(getLocation(), lander) -> State.WAITING_IN_BOAT
                PCUtils.isInPestControlInstance(this) -> State.PLAY_GAME
                PCUtils.isOutsideGangplank(getLocation(), lander) -> State.OUTSIDE_GANGPLANK
                else -> State.GET_TO_PC
            }

    private fun startPostGameReturn() {
        inPostGame = true
        openedGate = false
        start = true
        moveTimer = Random.nextInt(2, 5)
        customState = "Returning to lander..."
    }

    private fun handlePostGameReturn() {
        if (PCUtils.isInLander(location, lander)) {
            inPostGame = false
            moveTimer = Random.nextInt(2, 5)
            customState = "Waiting in lander for next round..."
            return
        }

        if (!walkingQueue.isMoving && moveTimer <= 0) {
            walkToPosSmart(lander.boatBorder.randomLoc)
            moveTimer = Random.nextInt(3, 7)
            customState = "Returning to lander..."
        }
    }

    private fun handleInGame() {
        val session = PCUtils.getMyPestControlSession(this)

        if (session?.isActive != true) {
            PestControlActivityPlugin().leave(this, false)
            val ladderNode = getClosestNodeWithEntry(50, lander.ladderId)
            if (ladderNode != null) {
                ladderNode.interaction.handle(this, ladderNode.interaction[0])
            } else {
                teleport(lander.landerLocation)
            }

            startPostGameReturn()
            return
        }

        if (start) {
            customState = "Game started – initializing..."
            start = false
            moveTimer = RandomFunction.random(3, 7)
        }

        when (role) {
            "attack_portals" -> {
                if (moveTimer <= 0) {
                    customState = "Attacking portals"
                    combatHandler.handleCombat()
                }
            }
            "defend_squire" -> {
                if (moveTimer <= 0) {
                    val squireLoc = session?.squire?.location ?: location
                    randomWalkAroundPoint(squireLoc, 5)
                    customState = "Defending squire near ${squireLoc.x},${squireLoc.y}"
                    combatHandler.handleDefense()
                    moveTimer = RandomFunction.random(2, 8)
                }
            }
        }
    }

    private fun handleBoatIdle() {
        openedGate = false
        if (prayer.active.isNotEmpty()) prayer.reset()

        if (random.nextInt(100) < 40 && random.nextInt(insideBoatWalks) <= 1) {
            if (random.nextInt(4) == 1) walkingQueue.isRunning = !walkingQueue.isRunning
            if (random.nextInt(7) >= 4) walkToPosSmart(lander.boatBorder.randomLoc)
            if (random.nextInt(3) == 1) insideBoatWalks += 2
        }
    }

    private fun handleOutsideBoat() {
        if (prayer.active.isNotEmpty()) prayer.reset()

        if (random.nextInt(8) >= 4) {
            combatHandler.move(defaultTargetForLander(), 10)
            moveTimer = random.nextInt(300) + 30
        }

        if (random.nextInt(8) >= 2) {
            randomWalk(3, 3)
            moveTimer = random.nextInt(10)
        }

        if (random.nextInt(100) > 50 && random.nextInt(10) <= 5) {
            walkToPosSmart(lander.outsideBoatBorder.randomLoc)
            moveTimer += RandomFunction.normalPlusWeightRandDist(400, 200)
        } else {
            getClosestNodeWithEntry(15, lander.ladderId)?.let {
                it.interaction.handle(this, it.interaction[0])
            }
            insideBoatWalks = 3
        }
    }

    private fun handleTeleportOrMove() {
        if (!justTeleported) {
            teleport(lander.landerLocation)
            justTeleported = true
            return
        }

        val node = getClosestNodeWithEntry(30, lander.ladderId)
        justTeleported = false
        if (node == null) teleport(lander.landerLocation)
        else node.interaction.handle(this, node.interaction[0])
    }

    private fun assignCombatLoadOut() {
        val assembler = CombatBotAssembler()
        val rangeMode = random.nextBoolean()
        val melee = random.nextInt(4) <= 2

        when {
            melee ->
                when (lander) {
                    PCUtils.LanderZone.NOVICE -> assembler.meleeBotNovice(this)
                    PCUtils.LanderZone.INTERMEDIATE -> assembler.meleeBotIntermediate(this)
                    PCUtils.LanderZone.VETERAN -> assembler.meleeBotIntermediate(this)
                }
            else ->
                when (lander) {
                    PCUtils.LanderZone.NOVICE -> assembler.rangeBotNovice(this, rangeMode)
                    PCUtils.LanderZone.INTERMEDIATE -> assembler.rangeBotIntermediate(this, rangeMode)
                    PCUtils.LanderZone.VETERAN -> assembler.rangeBotIntermediate(this, rangeMode)
                }
        }
    }

    private fun defaultTargetForLander(): Location =
        when (lander) {
            PCUtils.LanderZone.NOVICE -> Location(2658, 2659, 0)
            PCUtils.LanderZone.INTERMEDIATE -> Location(2652, 2646, 0)
            PCUtils.LanderZone.VETERAN -> Location(2638, 2648, 0)
        }

    companion object {
        fun landerLocation(l: Location, lander: PCUtils.LanderZone): Location =
            when {
                PCUtils.isInLander(l, lander) ->
                    when (lander) {
                        PCUtils.LanderZone.NOVICE -> Location(2660, 2648, 0)
                        PCUtils.LanderZone.INTERMEDIATE -> Location(2648, 2648, 0)
                        else -> Location(2634, 2648, 0)
                    }
                else -> l
            }
    }
}

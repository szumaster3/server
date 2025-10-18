package content.minigame.pestcontrol.bots

import content.minigame.pestcontrol.plugin.PCUtils
import content.minigame.pestcontrol.plugin.PestControlActivityPlugin
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
    var start = false

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
        GET_TO_PC
    }

    override fun tick() {
        super.tick()
        if (--moveTimer > 0) return
        tick++

        when (state) {
            State.GET_TO_PC -> handleTeleportOrMove()
            State.OUTSIDE_GANGPLANK -> handleOutsideBoat()
            State.WAITING_IN_BOAT -> handleBoatIdle()
            State.PLAY_GAME -> handleInGame()
        }
    }

    private val state: State
        get() =
            when {
                PCUtils.isInLander(getLocation(), lander) -> State.WAITING_IN_BOAT
                PCUtils.isInPestControlInstance(this) -> State.PLAY_GAME
                PCUtils.isOutsideGangplank(getLocation(), lander) ->
                    State.OUTSIDE_GANGPLANK
                else -> State.GET_TO_PC
            }

    private fun handleInGame() {
        start = true

        /*
         * If the bot accidentally finds itself
         * outside the battlefield, it returns.
         */

        if (PCUtils.isOutsideGangplank(getLocation(), lander)) {
            PestControlActivityPlugin().leave(this, false)
            getClosestNodeWithEntry(50, lander.ladderId)?.let {
                it.interaction.handle(this, it.interaction[0])
            }
        }

        walkingQueue.isRunning = true

        when (role) {
            "attack_portals" -> {
                combatHandler.handleCombat()
            }

            "defend_squire" -> {
                moveTimer = RandomFunction.random(2, 10)
                val squireLoc = PCUtils.getMyPestControlSession(this)?.squire?.location ?: location
                randomWalkAroundPoint(squireLoc, 5)
                combatHandler.handleDefense()
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

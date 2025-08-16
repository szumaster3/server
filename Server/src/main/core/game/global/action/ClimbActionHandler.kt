package core.game.global.action

import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import shared.consts.Animations

object ClimbActionHandler {

    val CLIMB_UP = Animation(Animations.USE_LADDER_828)
    val CLIMB_DOWN = Animation(Animations.MULTI_BEND_OVER_827)
    var CLIMB_DIALOGUE: Dialogue = ClimbDialogue()

    /**
     * Handles the climbing action for ropes.
     *
     * @param player The player interacting with the rope.
     * @param scenery The rope scenery object.
     * @param option The climbing option.
     */
    @JvmStatic
    fun climbRope(player: Player, scenery: Scenery, option: String) {
        val currentLoc = scenery.location
        var destination: Location? = null
        val animation: Animation?

        when (option.lowercase()) {
            "climb-up" -> {
                for (z in (currentLoc.z + 1)..3) {
                    val obj = RegionManager.getObject(currentLoc.transform(0, 0, z - currentLoc.z))
                    if (obj != null && isClimbable(obj)) {
                        destination = obj.location
                        break
                    }
                }
                animation = CLIMB_UP
            }
            "climb-down" -> {
                for (z in (currentLoc.z - 1) downTo 0) {
                    val obj = RegionManager.getObject(currentLoc.transform(0, 0, z - currentLoc.z))
                    if (obj != null && isClimbable(obj)) {
                        destination = obj.location
                        break
                    }
                }
                animation = CLIMB_DOWN
            }
            "climb" -> {
                var upLoc: Location? = null
                var downLoc: Location? = null

                for (z in (currentLoc.z + 1)..3) {
                    val obj = RegionManager.getObject(currentLoc.transform(0, 0, z - currentLoc.z))
                    if (obj != null && isClimbable(obj)) {
                        upLoc = obj.location
                        break
                    }
                }
                for (z in (currentLoc.z - 1) downTo 0) {
                    val obj = RegionManager.getObject(currentLoc.transform(0, 0, z - currentLoc.z))
                    if (obj != null && isClimbable(obj)) {
                        downLoc = obj.location
                        break
                    }
                }

                when {
                    upLoc != null && downLoc == null -> climbRope(player, scenery, "climb-up")
                    downLoc != null && upLoc == null -> climbRope(player, scenery, "climb-down")
                    upLoc != null && downLoc != null -> {
                        val dialogue = CLIMB_DIALOGUE.newInstance(player)
                        if (dialogue != null && dialogue.open(scenery)) {
                            player.dialogueInterpreter.dialogue = dialogue
                        }
                    }
                    else -> sendMessage(player, "You can't climb that way.")
                }
                return
            }
            else -> {
                sendMessage(player, "You can't climb that way.")
                return
            }
        }

        if (destination == null || !RegionManager.isTeleportPermitted(destination)) {
            sendMessage(player, "You can't seem to climb there.")
            return
        }

        climb(player, animation, destination)
    }

    @JvmStatic
    fun climbTrapdoor(player: Player, scenery: Scenery, option: String) {
       //
    }

    /**
     * Handles the climbing action for ladders.
     * @param player The player interacting with the ladder.
     * @param startLadder The ladder scenery object the player is climbing.
     * @param option The climbing option chosen by the player: "climb-up", "climb-down", "walk-up", "walk-down", or "climb".
     * @return True if the climbing action was successfully started, false otherwise.
     */
    @JvmStatic
    fun climbLadder(player: Player, startLadder: Scenery?, option: String): Boolean {
        var endLadder: Scenery? = null
        var animation: Animation? = CLIMB_UP

        if (startLadder!!.name.startsWith("Stair")) {
            animation = null
        }

        SpecialLadder.getDestination(startLadder.location)?.let { destination ->
            climb(player, animation, destination)
            SpecialLadder.getSpecialLadder(startLadder.location)?.checkAchievement(player)
            return true
        }

        when (option) {
            "climb-up", "walk-up" -> endLadder = getLadder(startLadder, down = false)
            "climb-down", "walk-down" -> {
                if (startLadder.name == "Ladder" || startLadder.name == "Trapdoor") {
                    animation = CLIMB_DOWN
                }
                endLadder = getLadder(startLadder, down = true)
            }
            "climb" -> {
                val upper = getLadder(startLadder, down = false)
                val lower = getLadder(startLadder, down = true)
                return when {
                    upper == null && lower != null -> climbLadder(player, startLadder, "climb-down")
                    upper != null && lower == null -> climbLadder(player, startLadder, "climb-up")
                    else -> {
                        val dialogue = CLIMB_DIALOGUE.newInstance(player)
                        if (dialogue != null && dialogue.open(startLadder)) {
                            player.dialogueInterpreter.dialogue = dialogue
                        }
                        false
                    }
                }
            }
        }

        val destination = endLadder?.let { getDestination(it) }
        if (endLadder == null || destination == null) {
            sendMessage(player, "The ladder doesn't seem to lead anywhere.")
            return false
        }

        climb(player, animation, destination)
        return true
    }

    @JvmStatic
    fun getDestination(scenery: Scenery): Location? {
        var sizeX = scenery.definition.sizeX
        var sizeY = scenery.definition.sizeY

        if (scenery.rotation % 2 != 0) {
            sizeX = scenery.definition.sizeY.also { sizeY = scenery.definition.sizeX }
        }

        val dir = Direction.forWalkFlag(scenery.definition.blockFlag, scenery.rotation)
        return dir?.let { getDestination(scenery, sizeX, sizeY, it, 0) }
            ?: when (scenery.rotation) {
                0 -> getDestination(scenery, sizeX, sizeY, Direction.SOUTH, 0)
                1 -> getDestination(scenery, sizeX, sizeY, Direction.EAST, 0)
                2 -> getDestination(scenery, sizeX, sizeY, Direction.NORTH, 0)
                3 -> getDestination(scenery, sizeX, sizeY, Direction.WEST, 0)
                else -> null
            }
    }

    @JvmStatic
    private fun getDestination(scenery: Scenery, sizeX: Int, sizeY: Int, dir: Direction, count: Int): Location? {
        val loc = scenery.location

        if (dir.toInteger() % 2 != 0) {
            var x = dir.stepX
            if (x > 0) x *= sizeX
            repeat(sizeY) { y ->
                val l = loc.transform(x, y, 0)
                if (RegionManager.isTeleportPermitted(l) && dir.canMove(l)) return l
            }
        } else {
            var y = dir.stepY
            if (y > 0) y *= sizeY
            repeat(sizeX) { x ->
                val l = loc.transform(x, y, 0)
                if (RegionManager.isTeleportPermitted(l) && dir.canMove(l)) return l
            }
        }

        return if (count >= 3) null
        else getDestination(scenery, sizeX, sizeY, Direction.get((dir.toInteger() + 1) % 4), count + 1)
    }

    @JvmStatic
    private fun isClimbable(scenery: Scenery): Boolean {
        scenery.definition.options.forEach { if (it.contains("Climb", ignoreCase = true)) return true }
        return scenery.name.equals("Trapdoor", ignoreCase = true) || scenery.name.equals("Rope", ignoreCase = true)
    }

    @JvmStatic
    fun climb(player: Player, animation: Animation?, destination: Location, vararg messages: String) {
        player.lock(2)
        animation?.let { player.animate(it) }

        GameWorld.Pulser.submit(object : Pulse(1) {
            override fun pulse(): Boolean {
                player.properties.teleportLocation = destination
                messages.forEach { msg -> player.packetDispatch.sendMessage(msg) }
                return true
            }
        })
    }

    @JvmStatic
    private fun getLadder(scenery: Scenery, down: Boolean): Scenery? {
        val mod = if (down) -1 else 1
        var ladder = RegionManager.getObject(scenery.location.transform(0, 0, mod))

        if (ladder == null || !isLadder(ladder)) {
            if (ladder?.name == scenery.name) {
                ladder = RegionManager.getObject(ladder!!.location.transform(0, 0, mod))
                if (ladder != null) return ladder
            }
            ladder = findLadder(scenery.location.transform(0, 0, mod))
            if (ladder == null) {
                ladder = RegionManager.getObject(scenery.location.transform(0, mod * -6400, 0))
                    ?: findLadder(scenery.location.transform(0, mod * -6400, 0))
            }
        }

        return ladder
    }

    @JvmStatic
    private fun findLadder(center: Location): Scenery? {
        for (x in -5..5) {
            for (y in -5..5) {
                val obj = RegionManager.getObject(center.transform(x, y, 0))
                if (obj != null && isLadder(obj)) return obj
            }
        }
        return null
    }

    @JvmStatic
    private fun isLadder(scenery: Scenery): Boolean {
        scenery.definition.options.forEach {
            if (it != null && it.contains("Climb")) return true
        }
        return scenery.name == "Trapdoor"
    }

    internal class ClimbDialogue : Dialogue {
        constructor()

        constructor(player: Player?) : super(player)

        private var scenery: Scenery? = null

        override fun newInstance(player: Player?): Dialogue {
            return ClimbDialogue(player)
        }

        override fun open(vararg args: Any?): Boolean {
            scenery = args[0] as Scenery?
            interpreter.sendOptions("What would you like to do?", "Climb Up.", "Climb Down.")
            stage = 0
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            if (stage == 0) {
                when (buttonId) {
                    1 -> {
                        player.lock(1)
                        GameWorld.Pulser.submit(object : Pulse(1) {
                            override fun pulse(): Boolean {
                                climbLadder(player, scenery, "climb-up")
                                return true
                            }
                        })
                        end()
                    }
                    2 -> {
                        player.lock(1)
                        GameWorld.Pulser.submit(object : Pulse(1) {
                            override fun pulse(): Boolean {
                                climbLadder(player, scenery, "climb-down")
                                return true
                            }
                        })
                        end()
                    }
                }
            }
            return true
        }

        override fun getIds(): IntArray = intArrayOf(ID)

        companion object {
            const val ID = 8 shl 16
        }
    }
}

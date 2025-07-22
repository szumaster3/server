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
import org.rs.consts.Animations

object ClimbActionHandler {

    val CLIMB_UP = Animation(Animations.USE_LADDER_828)
    val CLIMB_DOWN = Animation(Animations.MULTI_BEND_OVER_827)
    var CLIMB_DIALOGUE: Dialogue = ClimbDialogue()

    fun climbRope(player: Player, scenery: Scenery, option: String) {
        // TODO
    }

    fun climbTrapdoor(player: Player, scenery: Scenery, option: String) {
        // TODO
    }

    @JvmStatic
    fun climbLadder(player: Player, startLadder: Scenery, option: String): Boolean {
        var endLadder: Scenery? = null
        var animation: Animation? = CLIMB_UP

        if (startLadder.name.startsWith("Stair")) {
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

        val dir = Direction.forWalkFlag(scenery.definition.walkingFlag, scenery.rotation)
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

    class ClimbDialogue() : Dialogue() {

        constructor(player: Player) : this() {
            this.player = player
        }

        private lateinit var scenery: Scenery

        override fun newInstance(player: Player): Dialogue {
            return ClimbDialogue(player)
        }

        override fun open(vararg args: Any?): Boolean {
            scenery = args[0] as Scenery
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

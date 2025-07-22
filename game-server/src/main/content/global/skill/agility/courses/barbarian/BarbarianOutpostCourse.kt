package content.global.skill.agility.courses.barbarian

import content.data.GameAttributes
import content.global.skill.agility.AgilityCourse
import content.global.skill.agility.AgilityHandler
import content.region.kandarin.miniquest.barcrawl.BarcrawlManager
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds
import org.rs.consts.Scenery as Objs

@Initializable
class BarbarianOutpostCourse : AgilityCourse {

    override fun configure() {
        SceneryDefinition.forId(org.rs.consts.Scenery.GATE_2115).handlers["option:open"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.GATE_2116).handlers["option:open"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.ROPESWING_2282).handlers["option:swing-on"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.LOG_BALANCE_2294).handlers["option:walk-across"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.OBSTACLE_NET_20211).handlers["option:climb-over"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.BALANCING_LEDGE_2302).handlers["option:walk-across"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.CRUMBLING_WALL_1948).handlers["option:climb-over"] = this
        ItemDefinition.forId(Items.BARCRAWL_CARD_455).handlers["option:read"] = this
        NPCDefinition.forId(NPCs.KHARID_SCORPION_385).handlers["option:pick-up"] = this
        NPCDefinition.forId(NPCs.KHARID_SCORPION_386).handlers["option:pick-up"] = this
        NPCDefinition.forId(NPCs.KHARID_SCORPION_387).handlers["option:pick-up"] = this
    }

    constructor() : super(null, 6, 46.2)

    constructor(player: Player?) : super(player, 6, 46.2)

    override fun createInstance(player: Player): AgilityCourse = BarbarianOutpostCourse(player)

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val id = node.id
        getCourse(player)
        when (id) {
            Objs.GATE_2115, Objs.GATE_2116 -> {
                if (!BarcrawlManager.getInstance(player).isFinished || BarcrawlManager.getInstance(player).isStarted()) {
                    player.dialogueInterpreter.open(NPCs.BARBARIAN_GUARD_384)
                } else {
                    DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                }
            }
            Objs.ROPESWING_2282 -> handleRopeSwing(player, node as Scenery)
            Objs.LOG_BALANCE_2294 -> handleLogBalance(player, node as Scenery)
            Objs.BALANCING_LEDGE_2302 -> handleLedgeBalance(player, node as Scenery)
            Objs.OBSTACLE_NET_20211 -> {
                if (player.location.x < node.location.x) {
                    return true
                }
                AgilityHandler.climb(player, 2, ClimbActionHandler.CLIMB_UP, player.location.transform(-1, 0, 1), 8.0, "You climb the netting...")
                trackLaps(player, true, 20211)
            }
            Objs.CRUMBLING_WALL_1948 -> {
                if (player.location.x > node.location.x) {
                    sendMessage(player, "You cannot climb from this side.")
                    return true
                }
                val flag = when (node.location) {
                    Location(2536, 3553, 0) -> 4
                    Location(2539, 3553, 0) -> 5
                    else -> 6
                }
                sendMessage(player, "You climb the low wall...")
                AgilityHandler.forceWalk(player, flag, node.location.transform(-1, 0, 0), node.location.transform(1, 0, 0), Animation.create(Animations.CLIMB_OBJECT_839), 10, 13.5, null)
                trackLaps(player, true, 1948)
            }
            Items.BARCRAWL_CARD_455 -> BarcrawlManager.getInstance(player).read()
            NPCs.KHARID_SCORPION_385, NPCs.KHARID_SCORPION_386, NPCs.KHARID_SCORPION_387 -> {
                sendMessage(player, "The scorpion stings you!")
                impact(player, 3, HitsplatType.NORMAL)
            }
        }
        return true
    }

    /**
     * Rope swing.
     */
    private fun handleRopeSwing(player: Player, scenery: Scenery) {
        if (player.location.y < 3554) {
            sendMessage(player, "You cannot do that from here.")
            return
        }
        if (ropeDelay > GameWorld.ticks) {
            sendMessage(player, "The rope is being used.")
            return
        }
        val failed = AgilityHandler.hasFailed(player, 1, 0.1)
        if (failed) {
            AgilityHandler.fail(player, 0, Location.create(2549, 9951, 0), null, getHitAmount(player), "You slip and fall to the pit below.")
            return
        }
        ropeDelay = GameWorld.ticks + 2
        AgilityHandler.forceWalk(player, 0, player.location, Location.create(2551, 3549, 0), Animation.create(Animations.ROPE_SWING_751), 50, 22.0, "You skillfully swing across.", 1)
        playAudio(player, Sounds.SWING_ACROSS_2494, 1)
        animateScenery(player, scenery, 497, true)
        trackLaps(player, !failed, scenery.id)
    }

    /**
     * Log balance.
     */
    private fun handleLogBalance(player: Player, scenery: Scenery) {
        val failed = AgilityHandler.hasFailed(player, 1, 0.5)
        val end = if (failed) Location.create(2545, 3546, 0) else Location.create(2541, 3546, 0)
        sendMessage(player, "You walk carefully across the slippery log...")
        playAudio(player, Sounds.LOG_BALANCE_2470)
        AgilityHandler.walk(
            player,
            if (failed) -1 else 1,
            Location.create(2551, 3546, 0),
            end,
            Animation.create(155),
            if (failed) 0.0 else 13.5,
            if (failed) null else "...You make it safely to the other side.",
        )
        if (failed) {
            AgilityHandler.walk(player, -1, player.location, Location.create(2545, 3546, 0), Animation.create(155), 0.0, null)
            GameWorld.Pulser.submit(getSwimPulse(player))
            return
        }
        trackLaps(player, !failed, scenery.id)
    }

    /**
     * Swimming.
     */
    private fun getSwimPulse(player: Player): Pulse {
        return object : Pulse(1, player) {
            var counter = 0

            override fun pulse(): Boolean {
                when (++counter) {
                    6 -> {
                        player.animate(Animation.create(771))
                        sendMessage(
                            player,
                            "...You loose your footing and fall into the water. Something in the water bites you.",
                        )
                    }

                    7 -> {
                        player.graphics(Graphics.create(org.rs.consts.Graphics.WATER_SPLASH_68))
                        player.properties.teleportLocation = Location.create(2545, 3545, 0)
                        player.impactHandler.manualHit(player, getHitAmount(player), HitsplatType.NORMAL)
                        AgilityHandler.walk(player, -1, Location.create(2545, 3545, 0), Location.create(2545, 3543, 0), Animation.create(152), 0.0, null)
                    }

                    11 -> {
                        player.properties.teleportLocation = Location.create(2545, 3542, 0)
                        return true
                    }
                }
                return false
            }
        }
    }

    /**
     * Ledge balance.
     */
    private fun handleLedgeBalance(player: Player, scenery: Scenery) {
        val failed = AgilityHandler.hasFailed(player, 1, 0.3)
        val end = if (failed) Location.create(2534, 3547, 1) else Location.create(2532, 3547, 1)
        sendMessage(player, "You put your foot on the ledge and try to edge across...")
        AgilityHandler.walk(
            player,
            if (failed) -1 else 3,
            Location.create(2536, 3547, 1),
            end,
            Animation.create(157),
            if (failed) 0.0 else 22.0,
            if (failed) null else "You skillfully edge across the gap.",
        )
        if (failed) {
            AgilityHandler.fail(player, 3, Location.create(2534, 3545, 0), Animation(Animations.FALL_FACE_DOWN_A_760), getHitAmount(player), "You slip and fall to the pit below.")
            return
        }
        trackLaps(player, !failed, scenery.id)
    }

    private val COURSE_OBSTACLES = intArrayOf(
        Objs.ROPESWING_2282,
        Objs.LOG_BALANCE_2294,
        Objs.OBSTACLE_NET_20211,
        Objs.BALANCING_LEDGE_2302,
        Objs.CRUMBLING_WALL_1948
    )

    private fun getLastObstacle(player: Player): Int {
        return getAttribute(player, GameAttributes.BARBARIAN_OUTPOST_LAST_OBSTACLE, -1)
    }

    private fun setLastObstacle(player: Player, obstacleId: Int) {
        setAttribute(player, GameAttributes.BARBARIAN_OUTPOST_LAST_OBSTACLE, obstacleId)
    }

    private fun removeLastObstacle(player: Player) {
        player.attributes.remove(GameAttributes.BARBARIAN_OUTPOST_LAST_OBSTACLE)
    }

    /**
     * Tracks the progress through the course.
     */
    private fun trackLaps(player: Player, success: Boolean, id: Int) {
        if (getAttribute(player, GameAttributes.BARBARIAN_OUTPOST_PERFECT_LAPS, 0) >= 250) {
            return
        }

        val lastObstacle = getLastObstacle(player)
        if (lastObstacle == id) {
            return
        }

        if (success) {
            setLastObstacle(player, id)

            val currentPoints = getAttribute(player, GameAttributes.BARBARIAN_OUTPOST_COURSE_LAPS, 0)
            val nextPoints = currentPoints + 1
            val courseSize = COURSE_OBSTACLES.size
            val perfectLaps = nextPoints / courseSize

            setAttribute(player, GameAttributes.BARBARIAN_OUTPOST_COURSE_LAPS, nextPoints)

            if (nextPoints % courseSize == 0) {
                setAttribute(player, GameAttributes.BARBARIAN_OUTPOST_PERFECT_LAPS, perfectLaps)
                removeLastObstacle(player) // Reset
            }

            if (perfectLaps >= 250 && player.attributes[GameAttributes.BARBARIAN_OUTPOST_COURSE_REWARD] != true) {
                setAttribute(player, GameAttributes.BARBARIAN_OUTPOST_COURSE_REWARD, true)
                sendMessage(
                    player,
                    core.tools.RED + "You've completed 250 full laps! Speak to Gunnjorn to collect your reward."
                )
            }
        }
    }

    override fun getDestination(node: Node, n: Node): Location? {
        if (n is Scenery) {
            when (n.id) {
                2282 -> return Location.create(2551, 3554, 0)
            }
        }
        return null
    }

    companion object {
        private var ropeDelay = 0
    }
}

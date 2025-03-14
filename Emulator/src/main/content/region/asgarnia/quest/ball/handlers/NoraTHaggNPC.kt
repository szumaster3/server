package content.region.asgarnia.quest.ball.handlers

import core.api.*
import core.api.ui.setMinimapState
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatSwingHandler.Companion.isProjectileClipped
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class NoraTHaggNPC : AbstractNPC {
    var walkdir = false

    constructor() : super(896, Location.create(2904, 3463, 0))

    private val respawnLocation: Location
        private get() = Location.create(2900, 3473, 0)

    private constructor(id: Int, location: Location) : super(id, location)

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        configure()
        init()
        return super.newInstance(arg)
    }

    private fun canTeleport(t: Entity): Boolean {
        val playerX = t.location.x
        val npcX = getLocation().x
        val sectors = intArrayOf(2904, 2907, 2910, 2914, 2918, 2922, 2926, 2928)
        for (i in 0 until sectors.size - 1) {
            if (npcX >= sectors[i] && npcX <= sectors[i + 1] && playerX >= sectors[i] && playerX <= sectors[i + 1]) {
                if (playerX < npcX &&
                    getDirection() == Direction.WEST ||
                    playerX > npcX &&
                    getDirection() == Direction.EAST
                ) {
                    return true
                }
                if (playerX == npcX) {
                    return true
                }
            }
        }
        return false
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return NoraTHaggNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NORA_T_HAGG_896)
    }

    override fun getWalkRadius(): Int {
        return 50
    }

    override fun configure() {
        super.configure()
        isWalks = false
        isPathBoundMovement = true
    }

    private fun sendTeleport(player: Player) {
        lock(player, 4)
        Pulser.submit(
            object : Pulse(1) {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            face(player)
                            removeItem(player, Items.BALL_2407)
                            removeItem(player, Items.KEY_2411)
                            removeItem(player, Items.DOOR_KEY_2409)
                            sendChat("Stop! Thief!")
                            sendMessage(player, "You've been spotted by the witch.")
                            visualize(
                                player,
                                -1,
                                Graphics(org.rs.consts.Graphics.CURSE_IMPACT_110, 100),
                            )
                            setMinimapState(player, 2)
                        }

                        2 -> {
                            sendChat("Klarata... Seppteno... Valkan!")
                            face(null)
                        }

                        4 -> {
                            player.properties.teleportLocation = Location.create(respawnLocation)
                            setMinimapState(player, 0)
                            face(null)
                            unlock(player)
                            return true
                        }
                    }
                    return false
                }
            },
        )
    }

    override fun tick() {
        super.tick()
        val players = viewport.currentPlane.players
        if (getLocation().x == 2930) {
            walkdir = false
        } else if (getLocation().x == 2904) {
            walkdir = true
        }
        for (player in players) {
            if (player == null ||
                !player.isActive ||
                player.locks.isInteractionLocked ||
                DeathTask.isDead(player) ||
                !canTeleport(
                    player,
                ) ||
                !isProjectileClipped(this, player, false)
            ) {
                continue
            }
            animate(Animation(5803))
            sendTeleport(player)
            sendMessage(player, getLocation().toString() + " matches? " + (getLocation().x == 2904))
        }
        if (location.x != 2930 && walkdir) {
            walkingQueue.reset()
            walkingQueue.addPath(location.x + 1, 3463, true)
        } else if (location.x != 2904 && !walkdir) {
            walkingQueue.reset()
            walkingQueue.addPath(location.x - 1, 3463, true)
        }
    }
}

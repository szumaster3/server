package content.region.misc.handlers

import core.api.*
import core.api.quest.hasRequirement
import core.api.ui.setMinimapState
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Quests

object MinecartTravel {
    @JvmStatic
    fun goToKeldagrim(player: Player) {
        if (!hasRequirement(player, Quests.THE_GIANT_DWARF)) {
            return
        }
        submitWorldPulse(TravelToKeldagrimPulse(player))
    }

    @JvmStatic
    fun leaveKeldagrimTo(
        player: Player,
        dest: Location,
    ) {
        if (!hasRequirement(player, Quests.THE_GIANT_DWARF)) {
            return
        }
        submitWorldPulse(TravelFromKeldagrimPulse(player, dest))
    }

    class TravelFromKeldagrimPulse(
        val player: Player,
        val dest: Location,
    ) : Pulse() {
        var counter = 0

        override fun pulse(): Boolean {
            when (counter++) {
                0 ->
                    lock(player, 25).also {
                        openInterface(player, Components.FADE_TO_BLACK_120)
                        setMinimapState(player, 2)
                    }

                4 -> {
                    player.properties.teleportLocation = Location.create(2911, 10171, 0)
                    player.appearance.rideCart(true)
                }

                5 -> {
                    player.walkingQueue.reset()
                    player.walkingQueue.addPath(2936, 10171)
                }

                6 -> {
                    closeInterface(player)
                    openInterface(player, Components.FADE_FROM_BLACK_170)
                }

                14 -> openInterface(player, Components.FADE_TO_BLACK_120)

                21 -> {
                    player.walkingQueue.reset()
                    player.properties.teleportLocation = dest
                    player.appearance.rideCart(false)
                }

                23 -> {
                    closeInterface(player)
                    openInterface(player, Components.FADE_FROM_BLACK_170)
                }

                25 -> {
                    unlock(player)
                    setMinimapState(player, 0)
                    closeInterface(player)
                    return true
                }
            }
            return false
        }
    }

    class TravelToKeldagrimPulse(
        val player: Player,
    ) : Pulse() {
        var counter = 0
        private var cartNPC = NPC(NPCs.MINE_CART_1544)

        override fun pulse(): Boolean {
            when (counter++) {
                0 ->
                    lock(player, 20).also {
                        openInterface(player, Components.FADE_TO_BLACK_115)
                        setMinimapState(player, 2)
                    }

                3 ->
                    player.properties.teleportLocation =
                        Location.create(2942, 10175, 0).also { player.appearance.rideCart(true) }

                5 -> {
                    player.walkingQueue.reset()
                    player.walkingQueue.addPath(2915, 10175)
                }

                7 -> {
                    closeInterface(player)
                    openInterface(player, Components.FADE_FROM_BLACK_170)
                }

                19 -> {
                    closeInterface(player)
                    setMinimapState(player, 0)
                    unlock(player)
                    player.appearance.rideCart(false)
                    cartNPC.location = player.location
                    cartNPC.direction = Direction.WEST
                    cartNPC.init()
                    player.properties.teleportLocation = player.location.transform(0, 1, 0)
                }

                33 -> return true.also { cartNPC.clear() }
            }
            return false
        }
    }
}

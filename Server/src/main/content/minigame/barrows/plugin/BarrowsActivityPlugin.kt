package content.minigame.barrows.plugin

import content.minigame.barrows.npc.BarrowBrotherNPC
import content.minigame.barrows.plugin.RewardChest.reward
import core.api.*
import core.api.utils.PlayerCamera
import core.game.activity.ActivityPlugin
import core.game.component.Component
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.agg.AggressiveBehavior
import core.game.node.entity.npc.agg.AggressiveHandler
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getRegionPlayers
import core.game.world.map.RegionManager.getTeleportLocation
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Graphics
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.Components

/**
 * Handles the Barrows minigame activity.
 */
@Initializable
class BarrowsActivityPlugin : ActivityPlugin("Barrows", false, false, false) {
    /**
     * Updates the location in the Barrows area,
     * handling tunnel visibility for the minimap.
     */
    override fun locationUpdate(e: Entity, last: Location) {
        if (e is Player && e.getViewport().region!!.id == 14231) {
            var tunnel = false
            for (border in MINI_TUNNELS) {
                if (border.insideBorder(e)) {
                    tunnel = true
                    break
                }
            }
            val player = e.asPlayer()
            if (getVarp(player, 1270) == 1 != tunnel) {
                setVarp(player, 1270, if (tunnel) 3 else 0, true)
            }
        }
    }

    /**
     * Handles an entity entering the Barrows area.
     */
    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            val player = e.asPlayer()
            setMinimapState(player, 2)
            player.interfaceManager.openOverlay(OVERLAY)
            setVarp(player, 0, 1)
            if (getVarp(player, 452) == 0) {
                shuffleCatacombs(player)
            }
            sendConfiguration(player)
            if (!PULSE.isRunning) {
                PULSE.restart()
                PULSE.start()
                Pulser.submit(PULSE)
            }
        } else {
            (e as NPC).isAggressive = true
            e.aggressiveHandler =
                AggressiveHandler(
                    e,
                    object : AggressiveBehavior() {
                        override fun canSelectTarget(
                            entity: Entity?,
                            target: Entity,
                        ): Boolean {
                            if (!target.isActive || DeathTask.isDead(target)) {
                                return false
                            }
                            return !(!target.properties.isMultiZone && target.inCombat())
                        }
                    },
                )
        }
        return super.enter(e)
    }

    /**
     * Handles an entity leaving.
     */
    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (e is Player) {
            val player = e.asPlayer()
            setMinimapState(player, 0)
            player.interfaceManager.closeOverlay()
            val npc = player.getAttribute<NPC>("barrow:npc")
            if (npc != null && !DeathTask.isDead(npc)) {
                npc.clear()
            }
            removeAttribute(player, "barrow:solvedpuzzle")
            removeAttribute(player, "barrow:opened_chest")
            removeAttribute(player, "crusade-delay")
            if (!logout && player.getAttribute("barrow:looted", false)) {
                for (i in 0..5) {
                    removeAttribute(player, "brother:$i")
                    player.getSavedData().activityData.barrowBrothers[i] = false
                }
                removeAttribute(player, "barrow:looted")
                shuffleCatacombs(player)
                player.getSavedData().activityData.barrowTunnelIndex = RandomFunction.random(6)
                player.getSavedData().activityData.barrowKills = 0
                PlayerCamera(player).reset()
            }
        }
        return super.leave(e, logout)
    }

    /**
     * Handles the death of an entity.
     */
    override fun death(e: Entity, killer: Entity): Boolean {
        var player: Player? = null
        if (killer is Player) {
            player = killer
        } else if (killer is content.global.skill.summoning.familiar.Familiar) {
            player = killer.owner
        }
        if (player != null && e is NPC) {
            player.getSavedData().activityData.barrowKills = player.getSavedData().activityData.barrowKills + 1
            sendConfiguration(player)
        }
        return false
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (target is Scenery) {
            val `object` = target
            val player = e as Player
            if (`object`.id >= 6702 && `object`.id <= 6707) {
                ClimbActionHandler.climb(
                    e,
                    ClimbActionHandler.CLIMB_UP,
                    BarrowsCrypt.getCrypt(`object`.id - 6702).exitLocation,
                )
                return true
            }
            if (`object`.id >= 6708 && `object`.id <= 6712) {
                ClimbActionHandler.climb(
                    e,
                    ClimbActionHandler.CLIMB_UP,
                    BarrowsCrypt.getCrypt(player.getSavedData().activityData.barrowTunnelIndex).exitLocation,
                )
                return true
            }
            when (`object`.wrapper.id) {
                6727, 6724, 6746, 6743 -> {
                    if (player.getAttribute("barrow:solvedpuzzle", false)) {
                        return false
                    } else {
                        setAttribute(player, "barrow:puzzledoor", `object`)
                        BarrowsPuzzle.open(player)
                        return true
                    }
                }
            }

            when (`object`.id) {
                6714, 6733 -> {
                    DoorActionHandler.handleAutowalkDoor(e, target)
                    if (RandomFunction.random(15) == 0) {
                        val brothers = player.getSavedData().activityData.barrowBrothers
                        val alive = (0..5).filter { i -> !brothers[i] }.toIntArray()
                        if (alive.isNotEmpty()) {
                            var index = 0
                            if (alive.size > 1) {
                                index = RandomFunction.random(0, alive.size)
                            }
                            BarrowsCrypt
                                .getCrypt(alive[index])
                                .spawnBrother(player, getTeleportLocation(target.getLocation(), 1))
                        }
                    }
                    return true
                }

                6821 -> {
                    BarrowsCrypt.getCrypt(BarrowsCrypt.AHRIM).openSarcophagus(e, `object`)
                    return true
                }

                6771 -> {
                    BarrowsCrypt.getCrypt(BarrowsCrypt.DHAROK).openSarcophagus(e, `object`)
                    return true
                }

                6773 -> {
                    BarrowsCrypt.getCrypt(BarrowsCrypt.GUTHAN).openSarcophagus(e, `object`)
                    return true
                }

                6822 -> {
                    BarrowsCrypt.getCrypt(BarrowsCrypt.KARIL).openSarcophagus(e, `object`)
                    return true
                }

                6772 -> {
                    BarrowsCrypt.getCrypt(BarrowsCrypt.TORAG).openSarcophagus(e, `object`)
                    return true
                }

                6823 -> {
                    BarrowsCrypt.getCrypt(BarrowsCrypt.VERAC).openSarcophagus(e, `object`)
                    return true
                }

                6774 -> {
                    player.lock(1)
                    val brother = player.getSavedData().activityData.barrowTunnelIndex
                    if (!player.getSavedData().activityData.barrowBrothers[brother] &&
                        !player.getAttribute(
                            "brother:$brother",
                            false,
                        )
                    ) {
                        BarrowsCrypt
                            .getCrypt(brother)
                            .spawnBrother(player, getTeleportLocation(target.getCenterLocation(), 4))
                    }
                    setAttribute(player, "barrow:opened_chest", true)
                    sendConfiguration(player)
                    return true
                }

                6775 -> {
                    if (option.name == "Close") {
                        removeAttribute(player, "barrow:opened_chest")
                        sendConfiguration(player)
                        return true
                    }
                    if (player.getAttribute("barrow:looted", false)) {
                        sendMessage(player, "The chest is empty.")
                        return true
                    }
                    setAttribute(player, "/save:barrow:looted", true)
                    playJingle(player, 77)
                    reward(player)
                    PacketRepository.send(
                        CameraViewPacket::class.java,
                        CameraContext(player, CameraContext.CameraType.SHAKE, 3, 2, 2, 2, 2),
                    )
                    return true
                }
            }
        }
        return false
    }

    override fun actionButton(player: Player, interfaceId: Int, buttonId: Int, slot: Int, itemId: Int, opcode: Int): Boolean = false

    /**
     * Prevents attacking a BarrowBrother NPC unless it is your target.
     */
    override fun continueAttack(e: Entity, target: Node, style: CombatStyle, message: Boolean): Boolean {
        if (target is BarrowBrotherNPC) {
            var p: Player? = null
            if (e is Player) {
                p = e
            } else if (e is content.global.skill.summoning.familiar.Familiar) {
                p = e.owner
            }
            if (p != null && p !== target.player) {
                p.packetDispatch.sendMessage("He's not after you.")
                return false
            }
        }
        return super.continueAttack(e, target, style, message)
    }

    @Throws(Throwable::class)
    override fun newInstance(p: Player): ActivityPlugin = this

    override fun getSpawnLocation(): Location? = null

    /**
     * Configures Barrows plugins.
     */
    override fun configure() {
        definePlugin(BarrowsPuzzle.SHAPES)
        registerRegion(14231)
        BarrowsCrypt.init()
        PULSE.stop()
    }

    companion object {
        /**
         * Barrows tunnel configurations.
         */
        private val TUNNEL_CONFIGS =
            intArrayOf(55328769, 2867201, 44582944, 817160, 537688072, 40763408, 44320784, 23478274)

        /**
         * Mini tunnel borders.
         */
        private val MINI_TUNNELS =
            arrayOf(
                ZoneBorders(3532, 9665, 3570, 9671),
                ZoneBorders(3575, 9676, 3570, 9671),
                ZoneBorders(3575, 9676, 3581, 9714),
                ZoneBorders(3534, 9718, 3570, 9723),
                ZoneBorders(3523, 9675, 3528, 9712),
                ZoneBorders(3541, 9711, 3545, 9712),
                ZoneBorders(3558, 9711, 3562, 9712),
                ZoneBorders(3568, 9701, 3569, 9705),
                ZoneBorders(3551, 9701, 3552, 9705),
                ZoneBorders(3534, 9701, 3535, 9705),
                ZoneBorders(3541, 9694, 3545, 9695),
                ZoneBorders(3558, 9694, 3562, 9695),
                ZoneBorders(3568, 9684, 3569, 9688),
                ZoneBorders(3551, 9684, 3552, 9688),
                ZoneBorders(3534, 9684, 3535, 9688),
                ZoneBorders(3541, 9677, 3545, 9678),
                ZoneBorders(3558, 9677, 3562, 9678),
            )

        /**
         * Component overlay used in Barrows
         */
        private val OVERLAY = Component(Components.BARROWS_OVERLAY_24)

        private val PULSE: Pulse =
            object : Pulse(0) {
                override fun pulse(): Boolean {
                    var end = true
                    for (p in getRegionPlayers(14231)) {
                        end = false
                        var index = p.getAttribute("barrow:drain-index", -1)
                        if (index > -1) {
                            p.removeAttribute("barrow:drain-index")
                            p.packetDispatch.sendItemOnInterface(-1, 1, 24, index)
                            continue
                        }
                        if (p.location.z == 0 && p.getAttribute("barrow:looted", false) && getWorldTicks() % 3 == 0) {
                            if (RandomFunction.random(15) == 0) {
                                p.impactHandler.manualHit(
                                    p,
                                    RandomFunction.random(5),
                                    ImpactHandler.HitsplatType.NORMAL,
                                )
                                Graphics.send(Graphics.create(405), p.location)
                            }
                        }
                        var drain = 8

                        for (killed in p.getSavedData().activityData.barrowBrothers) {
                            if (killed) {
                                drain += 1
                            }
                        }
                        if (getWorldTicks() % 30 == 0) {
                            p.getSkills().decrementPrayerPoints(drain.toDouble())
                            p.locks.lock("barrow:drain", (3 + RandomFunction.random(15)) * 3)
                            index = 1 + RandomFunction.random(6)
                            p.setAttribute("barrow:drain-index", index)
                            p.packetDispatch.sendItemZoomOnInterface(4761 + RandomFunction.random(12), 100, 24, index)
                            p.packetDispatch.sendAnimationInterface(9810, 24, index)
                        }
                    }
                    return end
                }
            }

        /**
         * Randomly shuffles the catacomb layout for a player.
         */
        fun shuffleCatacombs(player: Player?) {
            var value = TUNNEL_CONFIGS[RandomFunction.random(TUNNEL_CONFIGS.size)]
            value = value or (1 shl 6 + RandomFunction.random(4))
            setVarp(player!!, 452, value)
        }

        /**
         * Sends Barrows configuration to player.
         */
        fun sendConfiguration(player: Player) {
            val data = player.getSavedData().activityData
            var config = data.barrowKills shl 17
            for (i in data.barrowBrothers.indices) {
                if (data.barrowBrothers[i]) {
                    config = config or (1 shl i)
                }
            }
            if (player.getAttribute<Boolean>("barrow:opened_chest", false)) {
                config = config or (1 shl 16)
            }
            setVarp(player, 453, config)
        }
    }
}

package content.minigame.pestcontrol.plugin

import content.minigame.pestcontrol.npc.*
import core.ServerConstants
import core.api.curePoison
import core.api.isPoisoned
import core.api.sendString
import core.game.activity.ActivityManager
import core.game.activity.ActivityPlugin
import core.game.component.Component
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.impl.PulseManager
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.build.DynamicRegion
import core.game.world.map.zone.RegionZone
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneRestriction
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.tools.RandomFunction
import core.tools.StringUtils
import java.util.PriorityQueue
import kotlin.collections.ArrayList
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Regions

/**
 * Represents the pest control activity plugin.
 */
@Initializable
class PestControlActivityPlugin @JvmOverloads constructor(val type: BoatType = BoatType.NOVICE) :
    ActivityPlugin(
        "pest control ${type.name.lowercase()}",
        false,
        true,
        true,
        ZoneRestriction.CANNON
    ) {

    companion object {
        const val MAX_TEAM_SIZE = 25
        const val MIN_TEAM_SIZE = 5
    }

    val waitingPlayers: PriorityQueue<Player> = PriorityQueue { p1, p2 ->
        val pr1 = p1.getAttribute("pc_prior", 0)
        val pr2 = p2.getAttribute("pc_prior", 0)
        pr2 - pr1
    }

    private val sessions: MutableList<PestControlSession> = ArrayList(20)
    private var ticks: Int = 0

    private val pulse: Pulse =
        object : Pulse(1) {
            override fun pulse(): Boolean {
                sessions.removeIf { session -> session != null && session.update() }
                ticks++
                if (waitingPlayers.size >= MAX_TEAM_SIZE && ticks < 475) {
                    ticks = 495
                }

                if ((ticks < 450 && ticks % 100 == 0) || (ticks % 50 == 0) || ticks == 495) {
                    for (p in waitingPlayers) updateTime(p)
                }

                if (ticks >= 500) {
                    if (waitingPlayers.size >= MIN_TEAM_SIZE) {
                        this@PestControlActivityPlugin.start()
                    } else {
                        ticks = 400
                    }
                }
                return false
            }
        }

    init {
        safeRespawn = Location.create(2657, 2646, 0)
    }

    /** Starts a new Pest Control session (region + game instances). */
    fun start() {
        val region = DynamicRegion.create(Regions.PEST_CONTROL_10536)
        val session = PestControlSession(region, this)
        session.startGame(waitingPlayers)
        session.region.regionZones.add(RegionZone(this, session.region.borders))
        sessions.add(session)
        ticks = 0
        updatePlayerCount()
    }

    /**
     * Ends given [session], performing reward distribution (if success) and cleanup.
     *
     * @param session session to end
     * @param success true if the players succeeded
     */
    fun end(session: PestControlSession, success: Boolean) {
        if (!session.isActive) return

        val players = session.region.planes[0].players.toList()
        for (p in players) {
            p.properties.teleportLocation = leaveLocation
            if (!success) {
                p.dialogueInterpreter.open(NPCs.SQUIRE_3781, true, 0, true)
            } else if (success && p.getAttribute("pc_zeal", 0) >= 50) {
                val amount = type.ordinal + 2
                p.savedData.activityData.increasePestPoints(amount)
                val coins = Item(Items.COINS_995, p.properties.currentCombatLevel * 10)
                if (!p.inventory.add(coins)) GroundItemManager.create(coins, p)
                val ordinalText =
                    when (type.ordinal) {
                        0 -> "two"
                        1 -> "three"
                        else -> "four"
                    }
                p.dialogueInterpreter.open(NPCs.SQUIRE_3781, true, 1, ordinalText)
            } else {
                p.dialogueInterpreter.open(NPCs.SQUIRE_3781, true, 2, true)
            }
            p.removeAttribute("pc_zeal")
            p.removeExtension(PestControlSession::class.java)
            p.fullRestore()
            if (isPoisoned(p)) curePoison(p)
            PulseManager.cancelDeathTask(p)

            GameWorld.Pulser.submit(
                object : Pulse(1, p) {
                    override fun pulse(): Boolean {
                        p.skills.restore()
                        return true
                    }
                }
            )
        }

        session.region.regionZones.clear()
        session.isActive = false
    }

    /**
     * Calculated leave location for boat type.
     */
    val leaveLocation: Location
        get() =
            when (type) {
                BoatType.NOVICE -> Location.create(2657, 2639, 0)
                BoatType.INTERMEDIATE -> Location.create(2644, 2644, 0)
                BoatType.VETERAN -> Location.create(2638, 2653, 0)
            }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (e is Player) {
            val p = e
            if (!logout) {
                p.interfaceManager.closeOverlay()
            } else {
                e.location = leaveLocation
                e.properties.teleportLocation = leaveLocation
            }
            waitingPlayers.remove(p)
            updatePlayerCount()
        }
        return super.leave(e, logout)
    }

    override fun register() {
        // Register the other boat instances once.
        if (type == BoatType.NOVICE) {
            val activities =
                arrayOf(
                    this,
                    PestControlActivityPlugin(BoatType.INTERMEDIATE),
                    PestControlActivityPlugin(BoatType.VETERAN)
                )

            // Register the other activity instances with ActivityManager.
            ActivityManager.register(activities[1])
            ActivityManager.register(activities[2])

            // Load NPC plugins and object handlers.
            ClassScanner.definePlugin(PCPortalNPC())
            ClassScanner.definePlugin(PCSquireNPC())
            ClassScanner.definePlugin(PCTorcherNPC())
            ClassScanner.definePlugin(PCDefilerNPC())
            ClassScanner.definePlugin(PCRavagerNPC())
            ClassScanner.definePlugin(PCShifterNPC())
            ClassScanner.definePlugin(PCSplatterNPC())
            ClassScanner.definePlugin(PCSpinnerNPC())
            ClassScanner.definePlugin(PCBrawlerNPC())
            ClassScanner.definePlugin(PCObjectHandler())

            // Configure lander zone and island zone.
            ZoneBuilder.configure(PCLanderZone(activities))
            ZoneBuilder.configure(PCIslandZone())
        }

        // start and submit global pulse.
        pulse.start()
        GameWorld.Pulser.submit(pulse)
    }

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        return super.interact(e, target, option)
    }

    /**
     * Called when a player attempts to join this activity.
     *
     * @param p player attempting to join
     * @param login whether this call is due to a player login
     * @param args optional extra args
     * @return true if join accepted
     */
    override fun start(p: Player, login: Boolean, vararg args: Any?): Boolean {
        if (
            p.properties.currentCombatLevel < type.requirement && p.rights != Rights.ADMINISTRATOR
        ) {
            p.packetDispatch.sendMessage(
                "You need a combat level of ${type.requirement} or higher to board this lander."
            )
            return false
        }
        waitingPlayers.add(p)
        openLanderInterface(p)
        return true
    }

    /** Opens the lander waiting interface for a player. */
    private fun openLanderInterface(p: Player) {
        p.interfaceManager.openOverlay(Component(Components.PEST_LANDER_OVERLAY_407))
        updateTime(p)
        updatePlayerCount()
        sendString(
            p,
            "Points: ${p.savedData.activityData.pestPoints}",
            Components.PEST_LANDER_OVERLAY_407,
            16
        )
        sendString(
            p,
            StringUtils.formatDisplayName(type.name),
            Components.PEST_LANDER_OVERLAY_407,
            3
        )
    }

    /** Updates the countdown shown to a waiting player. */
    fun updateTime(p: Player) {
        val remaining = 500 - this.ticks
        val text =
            when {
                remaining > 99 -> "Next Departure: ${remaining / 100} min"
                remaining > 50 -> "Next Departure: 1 min"
                else -> "Next Departure: 30 seconds"
            }
        sendString(p, text, Components.PEST_LANDER_OVERLAY_407, 13)
    }

    /** Notify all waiting players about current queue size. */
    private fun updatePlayerCount() {
        for (p in waitingPlayers) {
            sendString(p, "Players Ready: ${waitingPlayers.size?: 0}", Components.PEST_LANDER_OVERLAY_407, 14)
        }
    }

    override fun death(e: Entity, killer: Entity?): Boolean {
        if (e is Player && e.viewport.region!!.regionId == Regions.PEST_CONTROL_10536) {
            val session: PestControlSession? =
                e.getExtension<PestControlSession>(PestControlSession::class.java)
            if (session != null) {
                val base = session.region.baseLocation
                e.properties.teleportLocation =
                    base.transform(
                        32 + RandomFunction.RANDOM.nextInt(4),
                        49 + RandomFunction.RANDOM.nextInt(6),
                        0
                    )
                return true
            }
        }
        return super.death(e, killer)
    }

    override fun newInstance(p: Player): ActivityPlugin = this

    override fun getSpawnLocation(): Location = ServerConstants.HOME_LOCATION!!

    override fun configure() {
        registerRegion(Regions.PEST_CONTROL_10536)
    }

    /**
     * Represents the boat types.
     */
    enum class BoatType(val requirement: Int) {
        NOVICE(40),
        INTERMEDIATE(70),
        VETERAN(100)
    }
}

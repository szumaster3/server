package content.region.wilderness.plugin

import com.google.gson.JsonObject
import core.ServerStore.Companion.getArchive
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.activity.ActivityManager
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.WarningManager
import core.game.node.entity.player.link.Warnings
import core.game.node.scenery.Scenery
import core.game.world.GameWorld
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Graphics
import core.tools.Log
import core.tools.RandomFunction
import core.tools.SystemLogger
import shared.consts.Quests
import java.util.*
import shared.consts.Scenery as Objects

/**
 * Handles all teleportation and area interactions within the Chaos Tunnels region.
 */
class ChaosTunnelPlugin : MapArea, InteractionListener {
    override fun defineListeners() {

        /*
         * Handles entrances and exits between surface and tunnel portals.
         */

        on(Objects.EXIT_28782, IntType.SCENERY, "climb-up") { player, _ ->
            val loc = when {
                player.location.withinDistance(Location(3182, 5471, 0)) -> Location(3059, 3549, 0)
                player.location.withinDistance(Location(3292, 5479, 0)) -> Location(3166, 3561, 0)
                player.location.withinDistance(Location(3248, 5489, 0)) -> Location(3120, 3571, 0)
                player.location.withinDistance(Location(3234, 5558, 0)) -> Location(3107, 3640, 0)
                player.location.withinDistance(Location(3290, 5538, 0)) -> Location(3165, 3617, 0)
                else -> null
            }

            if (loc != null) teleport(player, loc)

            return@on true
        }

        on(intArrayOf(Objects.RIFT_28891, Objects.RIFT_28892, Objects.RIFT_28893), IntType.SCENERY, "enter") { player, node ->
            val WARNING_MAP = mapOf(
               Objects.RIFT_28891 to Warnings.CHAOS_TUNNELS_WEST,
               Objects.RIFT_28892 to Warnings.CHAOS_TUNNELS_CENTRAL,
               Objects.RIFT_28893 to Warnings.CHAOS_TUNNELS_EAST
            )

            if (player.inCombat()) {
                sendMessage(player, "You can't enter the rift when you've recently been in combat.")
                return@on true
            }

            val cannonIds = ItemDefinition.getDefinitions().values
                .filter { it.name.contains("cannon", ignoreCase = true) }
                .map { it.id }

            if (cannonIds.any { inInventory(player, it) }) {
                sendMessage(player, "The cannon is too heavy to take it down there - you'll have to leave it behind.")
                return@on true
            }

            val loc = when (node.id) {
                Objects.RIFT_28891 -> Location(3182, 5471, 0)
                Objects.RIFT_28892 -> Location(3292, 5479, 0)
                Objects.RIFT_28893 -> Location(3248, 5489, 0)
                else -> null
            }

            val warning = WARNING_MAP[node.id]
            if (warning != null && !WarningManager.isWarningDisabled(player, warning)) {
                WarningManager.openWarningInterface(player, warning)
                return@on true
            }

            if (loc != null) teleport(player, loc)
            return@on true
        }

        /*
         * Handles climbing out of the Chaos Tunnels to surface.
         */

        on(Objects.STAIRS_23074, IntType.SCENERY, "climb") { player, _ ->
            teleport(player, Location(3283, 3467, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles entering magical portals inside the tunnels.
         */

        on(Objects.PORTAL_28779, IntType.SCENERY, "use") { player, node ->
            val scenery = node as Scenery

            when (scenery.location) {
                Location(3326, 5469, 0) -> {
                    sendMessage(player, "You can't go back through this portal.")
                    return@on true
                }
                Location(3142, 5545, 0) -> {
                    if (hasRequirement(player, Quests.WHAT_LIES_BELOW)) {
                        commenceBorkBattle(player)
                    }
                    return@on true
                }
                else -> {
                    var loc = getLinkedLocation(scenery.location)
                    if (loc == null) {
                        SystemLogger.processLogEntry(this.javaClass, Log.INFO, "Unhandled portal=${scenery.location}")
                        return@on false
                    }

                    if (!isFixed(player)) {
                        var stained = isStained(scenery)
                        if (!stained && RandomFunction.random(100) <= 3) {
                            stained = true
                            setStainedTime(scenery)
                        }
                        if (stained) {
                            sendMessage(player, "This portal doesn't seem to be working right now.")
                            return@on true
                        }
                        if (RandomFunction.random(100) <= 3) {
                            loc = randomLocation
                            sendMessage(player, "The chaos teleporter transports you to an unknown portal.")
                        }
                    }

                    player.teleport(loc)
                    visualize(player, -1, Graphics(shared.consts.Graphics.CURSE_IMPACT_110, 100))
                    return@on true
                }
            }
        }
    }

    override fun areaEnter(entity: Entity) {
        if (entity is NPC && !entity.isAggressive) {
            entity.isAggressive = true
        }
        super.areaEnter(entity)
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(3116, 5412, 3362, 5584))
    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.CANNON)

    /**
     * Handles the Bork boss battle sequence.
     */
    private fun commenceBorkBattle(player: Player) {
        val usernameKey = player.username.lowercase(Locale.getDefault())
        val storeFile = getBorkStoreFile()
        val isPortalWeak = storeFile.get(usernameKey)?.asBoolean ?: false

        if (isPortalWeak && GameWorld.settings?.isHosted == true) {
            sendMessage(player, "The portal's magic is too weak to teleport you right now.")
            return
        }

        lock(player, 10)
        visualize(player, -1, Graphics(shared.consts.Graphics.CURSE_IMPACT_110, 100))
        storeFile.addProperty(usernameKey, true)
        ActivityManager.start(player, "Bork cutscene", false)
    }

    /**
     * Gets random valid chaos tunnels portal destination.
     */
    private val randomLocation: Location
        get() = RandomFunction.getRandomElement(PORTALS.values.toTypedArray()) as Location

    /**
     * Checks if the given scenery portal is currently stained by dark magic.
     */
    private fun isStained(scenery: Scenery): Boolean = getStainedTime(scenery) > ticks

    /**
     * Gets the stained time attribute of the portal scenery.
     */
    private fun getStainedTime(scenery: Scenery): Int = scenery.attributes.getAttribute("stained", 0)

    /**
     * Sets the stained time attribute for the scenery portal.
     */
    private fun setStainedTime(scenery: Scenery) {
        scenery.attributes.setAttribute("stained", ticks + RandomFunction.random(50, 150))
    }

    /**
     * Checks if the portal for the player is fixed (immune to random teleport effects).
     */
    private fun isFixed(player: Player): Boolean = false

    /**
     * Gets the destination for the given [location].
     */
    private fun getLinkedLocation(location: Location): Location? =
        PORTALS[location] ?: PORTALS.entries.find { it.value == location }?.key

    companion object {
        private val PORTALS = mutableMapOf<Location, Location>()

        fun getBorkStoreFile(): JsonObject = getArchive("daily-bork-killed")

        init {
            fun addLink(x1: Int, y1: Int, x2: Int, y2: Int) {
                PORTALS[Location(x1, y1, 0)] = Location(x2, y2, 0)
            }

            addLink(3158, 5561, 3162, 5557)
            addLink(3162, 5545, 3166, 5553)
            addLink(3147, 5541, 3143, 5535)
            addLink(3148, 5533, 3153, 5537)
            addLink(3152, 5520, 3156, 5523)
            addLink(3165, 5515, 3173, 5530)
            addLink(3169, 5510, 3159, 5501)
            addLink(3181, 5517, 3185, 5518)
            addLink(3182, 5530, 3187, 5531)
            addLink(3190, 5519, 3190, 5515)
            addLink(3196, 5512, 3202, 5515)
            addLink(3197, 5529, 3201, 5531)
            addLink(3190, 5549, 3190, 5554)
            addLink(3180, 5557, 3174, 5558)
            addLink(3171, 5542, 3168, 5541)
            addLink(3206, 5553, 3204, 5546)
            addLink(3226, 5553, 3230, 5547)
            addLink(3214, 5533, 3211, 5533)
            addLink(3208, 5527, 3211, 5523)
            addLink(3238, 5507, 3232, 5501)
            addLink(3241, 5529, 3243, 5526)
            addLink(3261, 5536, 3268, 5534)
            addLink(3252, 5543, 3249, 5546)
            addLink(3262, 5552, 3266, 5552)
            addLink(3256, 5561, 3253, 5561)
            addLink(3297, 5536, 3299, 5533)
            addLink(3285, 5556, 3291, 5555)
            addLink(3288, 5536, 3289, 5533)
            addLink(3285, 5527, 3282, 5531)
            addLink(3285, 5508, 3280, 5501)
            addLink(3300, 5514, 3297, 5510)
            addLink(3325, 5518, 3323, 5531)
            addLink(3321, 5554, 3315, 5552)
            addLink(3142, 5489, 3141, 5480)
            addLink(3142, 5462, 3154, 5462)
            addLink(3143, 5443, 3155, 5449)
            addLink(3167, 5478, 3171, 5478)
            addLink(3171, 5473, 3167, 5471)
            addLink(3168, 5456, 3178, 5460)
            addLink(3187, 5460, 3189, 5444)
            addLink(3192, 5472, 3186, 5472)
            addLink(3185, 5478, 3191, 5482)
            addLink(3197, 5448, 3204, 5445)
            addLink(3191, 5482, 3185, 5478)
            addLink(3191, 5495, 3194, 5490)
            addLink(3214, 5456, 3212, 5452)
            addLink(3229, 5454, 3235, 5457)
            addLink(3233, 5445, 3241, 5445)
            addLink(3239, 5498, 3244, 5495)
            addLink(3233, 5470, 3241, 5469)
            addLink(3241, 5445, 3233, 5445)
            addLink(3259, 5446, 3265, 5491)
            addLink(3260, 5491, 3266, 5446)
            addLink(3218, 5478, 3215, 5475)
            addLink(3208, 5471, 3210, 5477)
            addLink(3283, 5448, 3287, 5448)
            addLink(3296, 5455, 3299, 5450)
            addLink(3302, 5469, 3290, 5463)
            addLink(3286, 5470, 3285, 5474)
            addLink(3322, 5480, 3318, 5481)
            addLink(3317, 5496, 3307, 5496)
            addLink(3299, 5484, 3303, 5477)
            addLink(3280, 5460, 3273, 5460)
            addLink(3285, 5474, 3286, 5470)
            addLink(3222, 5474, 3224, 5479)
            addLink(3222, 5488, 3218, 5497)
        }
    }
}

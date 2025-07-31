package content.minigame.puropuro.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.node.scenery.Scenery
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import org.rs.consts.Sounds

class CropCircleController :
    TickListener,
    InteractionListener {
    private val activeScenery = mutableMapOf<Location, Scenery>()
    override fun tick() {
        if (getWorldTicks() < nextCircle) return
        deconstructOldCircle()

        val (name, loc) = possibleLocations.random()
        constructCircle(loc)
        sendNews("A crop circle has appeared near $name.")
        val ticks = if(GameWorld.settings!!.isDevMode) 100 else 1500
        nextCircle = getWorldTicks() + ticks
        currentLocName = name
    }

    override fun defineListeners() {
        on(center, IntType.SCENERY, "enter") { player, node ->
            if (hasImpBox(player)) {
                sendDialogue(
                    player,
                    "Something prevents you from entering. You think the portal is offended by your imp box. They are not popular on imp and impling planes.",
                )
                return@on true
            }
            lock(player, 1)
            runTask(player, 0) {
                forceWalk(player, node.centerLocation, "")
                playAudio(player, fairyTeleport)
                teleport(player, puroLocation, TeleportType.PURO_PURO, 1)
                setAttribute(player, exitLocation, Location.create(node.centerLocation))
            }
            return@on true
        }

        on(puroExit, IntType.SCENERY, "leave", "quick-leave") { player, node ->
            var exit = getAttribute(player, exitLocation, Location.create(3158, 3300, 0))
            lock(player, 1)
            runTask(player, 0) {
                forceWalk(player, node.centerLocation, "")
                playAudio(player, fairyTeleport)
                teleport(player, exit, TeleportType.PURO_PURO, 1)
            }
            return@on true
        }
    }

    private fun constructCircle(location: Location) {
        RegionManager.getObject(location)?.let { activeScenery[location] = it }
        addScenery(center, Location(location.x, location.y, location.z), rotation = 0, type = 10)

        for ((index, tile) in location.getSurroundingTiles().withIndex()) {
            RegionManager.getObject(tile)?.let { activeScenery[tile] = it }
            addScenery(surrounding[index % 4], Location(tile.x, tile.y, tile.z), rotation = (index / 4) * 2, type = 10)
        }
    }

    private fun deconstructOldCircle() {
        for ((loc, originalScenery) in activeScenery) {
            RegionManager.getObject(loc)?.let { removeScenery(it) }
            addScenery(originalScenery.id, Location(loc.x, loc.y, loc.z), rotation = originalScenery.rotation, type = originalScenery.type)
        }
        activeScenery.clear()
    }

    private fun hasImpBox(player: Player): Boolean = inInventory(player, 10025) || inInventory(player, 10027) || inInventory(player, 10028)

    companion object {
        var currentLocName = ""
        val exitLocation = "/save:puro-exit"
        val possibleLocations =
            arrayOf(
                Pair("Doric's Hut", Location.create(2953, 3444, 0)),
                Pair("Yanille", Location.create(2583, 3104, 0)),
                Pair("Draynor", Location.create(3113, 3274, 0)),
                Pair("Rimmington", Location.create(2978, 3216, 0)),
                Pair("The Grand Exchange", Location.create(3141, 3461, 0)),
                Pair("Northern Lumbridge", Location.create(3160, 3296, 0)),
                Pair("Southern Varrock", Location.create(3218, 3348, 0)),
                Pair("Northern Ardougne", Location.create(2644, 3350, 0)),
            )
        val surrounding = arrayOf(24984, 24985, 24986, 24987)
        val center = 24991
        val puroExit = 25014
        val fairyTeleport = Sounds.FT_FAIRY_TP_1098
        val puroLocation = Location.create(2591, 4320, 0)
        var nextCircle = 0
    }
}

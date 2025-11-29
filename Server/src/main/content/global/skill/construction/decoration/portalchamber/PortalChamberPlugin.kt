package content.global.skill.construction.decoration.portalchamber

import content.data.GameAttributes
import content.global.skill.construction.Decoration
import content.global.skill.runecrafting.Rune
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.DARK_RED
import shared.consts.Components
import shared.consts.Quests
import shared.consts.Sounds
import shared.consts.Scenery as Objects

/**
 * Handles interactions and construction
 * features of the Portal Chamber room.
 */
@Initializable
class PortalChamberPlugin : OptionHandler() {

    /**
     * Supported portal destinations together with required runes.
     */
    private enum class Locations(val location: Location, vararg val runes: Item) {
        VARROCK(
            Location.create(3213, 3428, 0),
            Item(Rune.FIRE.rune.id, 100),
            Item(Rune.AIR.rune.id, 300),
            Item(Rune.LAW.rune.id, 100)
        ),
        LUMBRIDGE(
            Location.create(3222, 3217, 0),
            Item(Rune.EARTH.rune.id, 100),
            Item(Rune.AIR.rune.id, 300),
            Item(Rune.LAW.rune.id, 100)
        ),
        FALADOR(
            Location.create(2965, 3380, 0),
            Item(Rune.WATER.rune.id, 100),
            Item(Rune.AIR.rune.id, 300),
            Item(Rune.LAW.rune.id, 100)
        ),
        CAMELOT(Location.create(2730, 3485, 0), Item(Rune.AIR.rune.id, 500), Item(Rune.LAW.rune.id, 100)), ARDOUGNE(
            Location.create(2663, 3305, 0),
            Item(Rune.WATER.rune.id, 200),
            Item(Rune.LAW.rune.id, 200)
        ),
        YANILLE(Location.create(2554, 3114, 0), Item(Rune.EARTH.rune.id, 200), Item(Rune.LAW.rune.id, 200)), KHARYRLL(
            Location.create(3493, 3474, 0),
            Item(Rune.BLOOD.rune.id, 100),
            Item(Rune.LAW.rune.id, 200)
        )
    }

    companion object {
        /**
         * Redirects a selected portal hotspot to a new teleport destination.
         *
         * @param player The player.
         * @param identifier The destination name (e.g., "VARROCK").
         */
        fun direct(player: Player, identifier: String) {
            closeSingleTab(player)
            val dpId = getAttribute(player, "con:dp-id", 1)
            val hotspots = player.houseManager.getRoom(player.location).hotspots

            hotspots.firstOrNull { it.hotspot.name.equals("PORTAL$dpId", ignoreCase = true) }?.let { h ->
                if (h.decorationIndex == -1) {
                    sendMessage(player, "You must build a portal frame first!")
                    return
                }

                val previousName = h.hotspot.decorations[h.decorationIndex].name
                val prefix = when {
                    previousName.contains("mahogany", ignoreCase = true) -> "MAHOGANY"
                    previousName.contains("marble", ignoreCase = true) -> "MARBLE"
                    else -> "TEAK"
                }

                val location = Locations.values().firstOrNull { it.name.contains(identifier, true) } ?: return

                if (location == Locations.ARDOUGNE && !isQuestComplete(player, Quests.PLAGUE_CITY)) {
                    sendMessage(player, "You must complete the Plague City quest to direct the portal there.")
                    return
                }

                if (!player.inventory.containsItems(*location.runes)) {
                    sendMessage(player, "You do not have the required runes to build this portal.")
                    return
                }

                player.inventory.remove(*location.runes)
                player.animate(Animation.create(3645))
                h.decorationIndex = h.hotspot.getDecorationIndex(Decoration.forName("${prefix}_${identifier}_PORTAL"))
                player.houseManager.reload(player, player.houseManager.isBuildingMode)
            }
        }

        private fun getPortalName(player: Player, id: Int): String {
            val room = player.houseManager.getRoom(player.location)
            val hotspot = room.hotspots.firstOrNull { it.hotspot.name.equals("PORTAL$id", ignoreCase = true) }
                ?: return "$id: Nowhere"
            if (hotspot.decorationIndex == -1) return "$id: Nowhere"

            val decoName = hotspot.hotspot.decorations[hotspot.decorationIndex].name.lowercase()
            if (!decoName.contains("portal")) return "$id: Nowhere"

            val parts = decoName.split("_")
            if (parts.size < 3) return "$id: Nowhere"

            val locationPart = parts[1].lowercase()

            val location = Locations.values().firstOrNull { it.name.equals(locationPart, ignoreCase = true) }?.name
                ?: return "$id: Nowhere"

            return location.lowercase().replaceFirstChar { it.uppercase() }
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(Objects.SCRYING_POOL_13639).handlers["option:direct-portal"] = this
        SceneryDefinition.forId(Objects.TPATION_FOCUS_13640).handlers["option:direct-portal"] = this
        SceneryDefinition.forId(Objects.GREATER_TP_FOCUS_13641).handlers["option:direct-portal"] = this
        SceneryDefinition.forId(Objects.SCRYING_POOL_13639).handlers["option:scry"] = this
        for (i in Objects.VARROCK_PORTAL_13615..Objects.KHARYRLL_PORTAL_13635) {
            SceneryDefinition.forId(i).handlers["option:enter"] = this
        }
        ClassScanner.definePlugin(GazeIntoDialogue())
        ClassScanner.definePlugin(DirectPortalDialogue())
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val scenery = node.asScenery()
        when (option) {
            "scry" -> {
                openDialogue(player, "con:observe-location")
                return true
            }

            "direct-portal" -> {
                if (!player.houseManager.isBuildingMode) {
                    sendMessage(player, "You can currently only do this in building mode.")
                } else {
                    openDialogue(player, GameAttributes.CON_PORTAL_DIR)
                }
                return true
            }

            "enter" -> {
                Locations.values().firstOrNull {
                    scenery.name.contains(it.name, true)
                }?.let {
                    playAudio(player, Sounds.POH_TP_984)
                    teleport(player, it.location, TeleportManager.TeleportType.INSTANT)
                }
                return true
            }

            else -> return false
        }
    }

    /**
     * The dialogue shown when redirecting a portal.
     */
    private class DirectPortalDialogue(player: Player? = null) : Dialogue(player) {
        override fun open(vararg args: Any?): Boolean {
            sendDialogue(
                "To direct a portal you need enough runes for$DARK_RED 100</col> castings of that",
                "teleport spell.",
                "(Combination runes and staffs cannot be used.)"
            )
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (stage) {
                0 -> {
                    setTitle(player, 4)
                    val p1 = getPortalName(player, 1)
                    val p2 = getPortalName(player, 2)
                    val p3 = getPortalName(player, 3)
                    sendOptions(player, "Redirect which portal?", p1, p2, p3, "Cancel.")
                    stage = 1
                }

                1 -> {
                    val portalNames =
                        listOf(getPortalName(player, 1), getPortalName(player, 2), getPortalName(player, 3))
                    if (buttonId == 4 || portalNames.getOrNull(buttonId - 1) == "Nowhere") {
                        end()
                    } else {
                        end()
                        setAttribute(player, "con:dp-id", buttonId)
                        openDialogue(player, 394857)
                    }
                }
            }
            return true
        }

        override fun newInstance(player: Player) = DirectPortalDialogue(player)
        override fun getIds() = intArrayOf(DialogueInterpreter.getDialogueKey("con:directportal"))
    }

    private class GazeIntoDialogue(player: Player? = null) : Dialogue(player) {
        override fun open(vararg args: Any?): Boolean {
            setTitle(player, 4)
            val options = arrayOf(
                getPortalName(player, 1), getPortalName(player, 2), getPortalName(player, 3), "Cancel."
            )
            sendOptions(player, "Observe which location?", *options)
            stage = 0
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            if (stage != 0) return false
            // Cancel.
            if (buttonId == 4) {
                end()
                return true
            }

            val portalName = getPortalName(player, buttonId)
            if (portalName.equals("Nowhere", true)) {
                sendMessage(player, "This portal has no destination.")
                end()
                return true
            }

            val location = Locations.values().firstOrNull { it.name.equals(portalName, true) }
            if (location == null) {
                sendMessage(player, "Cannot observe this portal.")
                end()
                return true
            }

            val players = RegionManager.getViewportPlayers(location.location)
            if (players.size > 3) {
                end()
                sendMessage(player, "Unable to complete action - system busy.")
                return true
            }

            val originalLocation = player.location
            setAttribute(player, GameAttributes.ORIGINAL_LOCATION, originalLocation)
            registerLogoutListener(player, GameAttributes.LOGOUT) { p ->
                p.location = getAttribute(p, GameAttributes.ORIGINAL_LOCATION, Location.create(2953, 3224, 0))
            }

            player.lock(30)
            player.isInvisible = true
            setMinimapState(player, 2)
            openOverlay(player, Components.POH_SCRYING_POOL_404)
            setAttribute(player, GameAttributes.CON_GAZE_INTO, true)

            player.teleporter.send(location.location, TeleportManager.TeleportType.INSTANT)
            RegionManager.move(player)

            //player.viewport.updateViewport(player)

            sendPlainDialogue(player, true, "You view $portalName...")

            Pulser.submit(object : Pulse(3, player) {
                private var ticks = 0
                override fun pulse(): Boolean {
                    animate(player, 2590)
                    return ++ticks >= 10
                }

                override fun stop() {
                    super.stop()
                    player.unlock()
                    teleport(player, originalLocation, TeleportManager.TeleportType.INSTANT)
                    setMinimapState(player, 0)
                    player.isInvisible = false
                    player.dialogueInterpreter.close()
                    clearLogoutListener(player, GameAttributes.ORIGINAL_LOCATION)
                    player.viewport.updateViewport(player)
                    removeAttribute(player, GameAttributes.CON_GAZE_INTO)
                    closeOverlay(player)
                    resetAnimator(player)
                }
            })

            return true
        }

        override fun newInstance(player: Player) = GazeIntoDialogue(player)
        override fun getIds() = intArrayOf(DialogueInterpreter.getDialogueKey("con:observe-location"))
    }
}

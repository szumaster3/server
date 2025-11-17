package content.global.skill.construction.decoration.portalchamber

import content.global.skill.construction.Decoration
import content.global.skill.runecrafting.Rune
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.DialogueFile
import core.game.dialogue.Topic
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.DARK_RED
import core.tools.END_DIALOGUE
import shared.consts.Animations
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
        VARROCK(  Location.create(3213, 3428, 0), Item(Rune.FIRE.rune.id,   100), Item(Rune.AIR.rune.id, 300), Item(Rune.LAW.rune.id, 100)),
        LUMBRIDGE(Location.create(3222, 3217, 0), Item(Rune.EARTH.rune.id,  100), Item(Rune.AIR.rune.id, 300), Item(Rune.LAW.rune.id, 100)),
        FALADOR(  Location.create(2965, 3380, 0), Item(Rune.WATER.rune.id,  100), Item(Rune.AIR.rune.id, 300), Item(Rune.LAW.rune.id, 100)),
        CAMELOT(  Location.create(2730, 3485, 0), Item(Rune.AIR.rune.id,    500), Item(Rune.LAW.rune.id, 100)),
        ARDOUGNE( Location.create(2663, 3305, 0), Item(Rune.WATER.rune.id,  200), Item(Rune.LAW.rune.id, 200)),
        YANILLE(  Location.create(2554, 3114, 0), Item(Rune.EARTH.rune.id,  200), Item(Rune.LAW.rune.id, 200)),
        KHARYRLL( Location.create(3493, 3474, 0), Item(Rune.BLOOD.rune.id,  100), Item(Rune.LAW.rune.id, 200))
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
            val dpId = getAttribute(player,"con:dp-id", 1)
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
                player.animate(Animation.create(Animations.MAKING_TELE_TAB_3705))
                h.decorationIndex = h.hotspot.getDecorationIndex(Decoration.forName("${prefix}_${identifier}_PORTAL"))
                player.houseManager.reload(player, player.houseManager.isBuildingMode)
            }
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
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val scenery = node.asScenery()
        when (option) {
            "direct-portal" -> {
                if (!player.houseManager.isBuildingMode) {
                    sendMessage(player,"You can currently only do this in building mode.")
                } else {
                    openDialogue(player,PortalChamberDialogue())
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

    class PortalChamberDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> sendDialogueLines(player!!,"To direct a portal you need enough runes for$DARK_RED 100</col> castings of that", "teleport spell.", "(Combination runes and staffs cannot be used.)").also { stage++ }
                1 -> {
                    val room = player!!.houseManager.getRoom(player!!.location)
                    val portalNames = (1..3).map { id ->
                        val hotspot = room.hotspots.firstOrNull { it.hotspot.name.equals("PORTAL$id", ignoreCase = true) }
                        hotspot?.let { h ->
                            if (h.decorationIndex != -1) h.hotspot.decorations[h.decorationIndex].name else "Empty Portal"
                        } ?: "Nowhere"
                    }
                    setTitle(player!!, 3)
                    showTopics(
                        Topic("1: ${portalNames[0]}", 2, true),
                        Topic("2: ${portalNames[1]}", 2, true),
                        Topic("3: ${portalNames[2]}", 2, true),
                        Topic("Cancel.", END_DIALOGUE, true),
                        title = "Redirect which portal?"
                    )
                    setAttribute(player!!, "con:dp-id", buttonID)
                }
                2 -> showTopics(
                    Topic("Varrock", 4, true),
                    Topic("Lumbridge", 5, true),
                    Topic("Falador", 6, true),
                    Topic("More...", 3, true),
                    Topic("Back...", 1, true)
                )
                3 -> showTopics(
                    Topic("Camelot", 7, true),
                    Topic("Ardougne", 8, true),
                    Topic("Yanille", 9, true),
                    Topic("Kharyrll", 10, true),
                    Topic("Back...", 2, true)
                )
                4 -> directPortal("varrock")
                5 -> directPortal("lumbridge")
                6 -> directPortal("falador")
                7 -> directPortal("camelot")
                8 -> directPortal("ardougne")
                9 -> directPortal("yanille")
                10 -> directPortal("kharyrll")
            }
        }

        private fun directPortal(destination: String) {
            end()
            direct(player!!, destination.uppercase())
        }
    }

}

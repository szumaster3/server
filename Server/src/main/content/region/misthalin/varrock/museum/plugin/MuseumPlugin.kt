package content.region.misthalin.varrock.museum.plugin

import content.region.misthalin.dig_site.dialogue.GateGuardDialogue
import content.region.misthalin.varrock.museum.dialogue.*
import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import shared.consts.*

class MuseumPlugin :
    InteractionListener,
    InterfaceListener,
    MapArea {
    override fun areaEnter(entity: Entity) {
        /*
         * Handles when a player enters the museum area.
         */
        if (entity is Player) {
            val player = entity.asPlayer()
            openOverlay(player, Components.VM_KUDOS_532)
        }
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        /*
         * Handles when a player leaves the museum area.
         */
        if (entity is Player) {
            val player = entity.asPlayer()
            closeOverlay(player)
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        /*
         * Defines the borders of the museum area.
         */
        return VARROCK_MUSEUM
    }

    /*
     * Defines all interaction listeners for the museum.
     */

    override fun defineListeners() {
        /*
         * Handles interactions with the plaques in the museum.
         */

        on(BUTTON_PLUS_PLAQUES, IntType.SCENERY, "study", "push") { player, node ->
            when (node.id) {
                24605 -> openDialogue(player, TalkAboutLizards())
                24606 -> openDialogue(player, TalkAboutTortoises())
                24607 -> openDialogue(player, TalkAboutDragons())
                24608 -> openDialogue(player, TalkAboutWyverns())

                24609 -> openDialogue(player, TalkAboutCamels())
                24610 -> openDialogue(player, TalkAboutLeeches())
                24611 -> openDialogue(player, TalkAboutMoles())
                24612 -> openDialogue(player, TalkAboutPenguins())

                24613 -> openDialogue(player, TalkAboutSnails())
                24614 -> openDialogue(player, TalkAboutSnakes())
                24615 -> openDialogue(player, TalkAboutMonkeys())
                24616 -> openDialogue(player, TalkAboutSeaSlugs())

                24617 -> openDialogue(player, TalkAboutTerrorBirds())
                24618 -> openDialogue(player, TalkAboutKalphiteQueen())
            }
            return@on true
        }

        /*
         * Handles looking at the museum map item.
         */

        on(Items.MUSEUM_MAP_11184, IntType.ITEM, "look-at") { player, _ ->
            openInterface(player, Components.VM_MUSEUM_MAP_527)
            return@on true
        }

        /*
         * Handles walking up and down the museum stairs.
         */

        on(MUSEUM_STAIRS, IntType.SCENERY, "walk-up", "walk-down") { player, node ->
            when (node.id) {
                24427 -> ClimbActionHandler.climb(player, Animation(-1), Location(3258, 3452, 0))
                else -> ClimbActionHandler.climb(player, Animation(-1), Location(1759, 4958, 0))
            }
            return@on true
        }

        /*
         * Handles opening the museum door.
         */

        on(MUSEUM_DOOR, IntType.SCENERY, "open") { player, node ->
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        /*
         * Handles opening the museum gate, depending on the player's location.
         */

        on(MUSEUM_GATE, IntType.SCENERY, "open") { player, node ->
            if (player.location.y >= 3447) {
                openDialogue(player, GateGuardDialogue())
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }

        /*
         * Handles taking tools from the museum's tool rack.
         */

        on(TOOL_RACK, IntType.SCENERY, "take") { player, node ->
            face(player, node)
            setTitle(player, 5)
            sendOptions(
                player,
                "Which tool would you like?",
                "Trowel",
                "Rock pick",
                "Specimen brush",
                "Leather gloves",
                "Leather boots",
            )
            addDialogueAction(player) { _, button ->
                val item: Int =
                    when (button) {
                        2 -> Items.TROWEL_676
                        3 -> Items.ROCK_PICK_675
                        4 -> Items.SPECIMEN_BRUSH_670
                        5 -> Items.LEATHER_GLOVES_1059
                        6 -> Items.LEATHER_BOOTS_1061
                        else -> return@addDialogueAction
                    }
                val name: String = getItemName(item).lowercase()
                val word: String = if (name.startsWith("leather")) "pair of " else ""
                if (!addItem(player, item)) {
                    sendMessage(player, "You don't have enough space in your inventory.")
                } else {
                    sendItemDialogue(player, item, "You take a $word$name from the rack.")
                }
            }
            return@on true
        }

        /*
         * Handles opening the Workmen's gate,
         * only if the player has completed the Dig Site quest.
         */

        on(intArrayOf(Scenery.GATE_24560, Scenery.GATE_24561), IntType.SCENERY, "open") { player, node ->
            if (player.viewport.region!!.id == 6483) return@on true
            if (!isQuestComplete(player, Quests.THE_DIG_SITE)) {
                sendMessage(player, "You can't go through there, it's for Dig Site workmen only.")
                sendChat(findLocalNPC(player, NPCs.MUSEUM_GUARD_5942)!!, "Sorry - workman's gate only.")
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }

        /*
         * Handles looking at the information booth in the museum.
         */

        on(Scenery.INFORMATION_BOOTH_24452, IntType.SCENERY, "look-at") { player, _ ->
            openDialogue(player, NPCs.INFORMATION_CLERK_5938)
            return@on true
        }

        /*
         * Handles looking at or taking the museum floor maps.
         */

        on(
            intArrayOf(Scenery.MAP_24390, Scenery.MAP_24391, Scenery.MAP_24392),
            IntType.SCENERY,
            "look-at",
            "take",
        ) { player, node ->
            if (getUsedOption(player) == "take") {
                if (!addItem(player, Items.MUSEUM_MAP_11184)) {
                    sendMessage(player, "You don't have enough space in your inventory.")
                }
            } else {
                when (node.id) {
                    Scenery.MAP_24390 -> setAttribute(player, FLOOR_MAP_ATTRIBUTE, "main")
                    Scenery.MAP_24391 -> setAttribute(player, FLOOR_MAP_ATTRIBUTE, "second")
                    Scenery.MAP_24392 -> setAttribute(player, FLOOR_MAP_ATTRIBUTE, "top")
                }
                openInterface(player, Components.VM_MUSEUM_MAP_527)
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        /*
         * Handles opening the museum map interface.
         */

        onOpen(Components.VM_MUSEUM_MAP_527) { player, _ ->
            showMapFloor(player, getAttribute(player, FLOOR_MAP_ATTRIBUTE, "main"))
            removeAttribute(player, FLOOR_MAP_ATTRIBUTE)
            return@onOpen true
        }

        /*
         * Handles switching between floors in the museum map interface.
         */

        on(Components.VM_MUSEUM_MAP_527) { player, _, _, buttonID, _, _ ->
            showMapFloor(
                player,
                when (buttonID) {
                    in mapButtonsToBasement -> "basement"
                    in mapButtonsToMainFloor -> "main"
                    in mapButtonsToSecondFloor -> "second"
                    in mapButtonsToTopFloor -> "top"
                    else -> return@on true
                },
            )
            return@on true
        }

        /*
         * Handles opening the Natural History exam interface.
         */

        onOpen(NATURAL_HISTORY_EXAM_533) { player, component ->
            val model = getScenery(1763, 4937, 0)?.definition?.modelIds?.first()
            player.packetDispatch.sendModelOnInterface(model!!, component.id, 3, 0)
            setComponentVisibility(player, component.id, 27, false)
            sendString(player, "1", component.id, 25)
            sendString(player, "Question", component.id, 28)
            sendString(player, "1.", component.id, 29)
            sendString(player, "2.", component.id, 30)
            sendString(player, "3.", component.id, 31)
            return@onOpen true
        }

        /*
         * Handles the response to the Natural History exam questions.
         */

        on(NATURAL_HISTORY_EXAM_533) { player, _, _, buttonID, _, _ ->
            if (buttonID in 29..31) {
                closeInterface(player)
                setVarbit(player, 3637, 1, false)
                playAudio(player, Sounds.VM_GAIN_KUDOS_3653)
                sendNPCDialogue(player, NPCs.ORLANDO_SMITH_5965, "Nice job, mate. That looks about right.")
            }
            return@on true
        }

        /*
         * Handles actions related to the lectern interface in the museum.
         */

        on(Components.VM_LECTERN_794) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                2 -> updateVarbit(player, 1)
                3 -> updateVarbit(player, -1)
                else -> return@on true
            }
            return@on true
        }

        /*
         * Handles closing the lectern interface in the museum.
         */

        onClose(Components.VM_LECTERN_794) { player, _ ->
            resetVarbit(player)
            return@onClose true
        }
    }

    companion object {
        private val VARROCK_MUSEUM = arrayOf(ZoneBorders(3253, 3442, 3267, 3455), ZoneBorders(1730, 4932, 1788, 4988))
        private val MUSEUM_DOOR = intArrayOf(24565, 24567)
        private val MUSEUM_STAIRS = intArrayOf(24427, 24428)
        private const val MUSEUM_GATE = 24536
        private val BUTTON_PLUS_PLAQUES = (24588..24618).toIntArray()

        private const val TOOL_RACK = 24535
        private const val NATURAL_HISTORY_EXAM_533 = 533
        private val FLOOR_MAP_ATTRIBUTE = "iface:527:floor"

        private val mapButtonsToBasement = intArrayOf(41, 186)
        private val mapButtonsToMainFloor = intArrayOf(117, 120, 187, 188)
        private val mapButtonsToSecondFloor = intArrayOf(42, 44, 152, 153)
        private val mapButtonsToTopFloor = intArrayOf(42, 44, 118, 119)

        private fun updateVarbit(
            player: Player,
            value: Int,
        ) {
            val currentVarbitValue = getVarbit(player, Vars.VARBIT_VARROCK_MUSEUM_CENSUS_5390)
            setVarbit(player, Vars.VARBIT_VARROCK_MUSEUM_CENSUS_5390, currentVarbitValue + value)
        }

        private fun resetVarbit(player: Player) {
            setVarbit(player, Vars.VARBIT_VARROCK_MUSEUM_CENSUS_5390, 0)
        }

        private fun showMapFloor(
            player: Player,
            floor: String,
        ) {
            when (floor) {
                "basement" -> {
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 2, true)
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 7, false)
                }

                "main" -> {
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 3, true)
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 7, true)
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 2, false)
                }

                "second" -> {
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 2, true)
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 5, true)
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 3, false)
                }

                "top" -> {
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 3, true)
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 5, false)
                }
            }
        }
    }
}

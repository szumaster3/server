package content.region.misthalin.varrock.museum.plugin

import content.data.GameAttributes
import content.region.misthalin.dig_site.dialogue.GateGuardDialogue
import content.region.misthalin.varrock.museum.plugin.MusemInterfaceListener.Companion.NATURAL_HISTORY_EXAM_533
import core.api.*
import core.api.utils.PlayerCamera
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.*

class MuseumPlugin : InteractionListener{

    override fun defineListeners() {

        /*
         * Handles Natural history quiz.
         */

        on((24605..24618).toIntArray(), IntType.SCENERY, "study") { player, _ ->
            handleStudy(player)
            return@on true
        }

        /*
         * Handles animation support for display cases.
         */

        on((24590..24603).toIntArray(), IntType.SCENERY, "push") { player, node ->
            handleDisplayPush(player, node)
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
                else  -> ClimbActionHandler.climb(player, Animation(-1), Location(1759, 4958, 0))
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

        on(MUSEUM_GATE, IntType.SCENERY, "open") { player, _ ->
            if (player.location.y > 3446) openDialogue(player, GateGuardDialogue())
            else openMuseumGate(player)
            return@on true
        }

        /*
         * Handles taking tools from the museum's tool rack.
         */

        on(TOOL_RACK, IntType.SCENERY, "take") { player, node ->
            handleToolRack(player, node)
            return@on true
        }

        /*
         * Handles opening the Workmen's gate (digsite area).
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

        on(intArrayOf(Scenery.MAP_24390, Scenery.MAP_24391, Scenery.MAP_24392), IntType.SCENERY, "look-at", "take") { player, node ->
            if (getUsedOption(player) == "take") {
                if (!addItem(player, Items.MUSEUM_MAP_11184)) {
                    sendMessage(player, "You don't have enough space in your inventory.")
                }
            } else {
                when (node.id) {
                    Scenery.MAP_24390 -> setAttribute(player, GameAttributes.MUSEUM_FLOOR_MAP_ATTRIBUTE, "main")
                    Scenery.MAP_24391 -> setAttribute(player, GameAttributes.MUSEUM_FLOOR_MAP_ATTRIBUTE, "second")
                    Scenery.MAP_24392 -> setAttribute(player, GameAttributes.MUSEUM_FLOOR_MAP_ATTRIBUTE, "top")
                }
                openInterface(player, Components.VM_MUSEUM_MAP_527)
            }
            return@on true
        }
    }

    private fun handleDisplayPush(player: Player, node: Node) {
        val id = node.id - 24590
        val watchTime = animationDuration(Animation(6448))
        val pLoc = player.location
        val nLoc = node.location
        player.faceLocation(nLoc)
        playGlobalAudio(player.location, Sounds.VM_PUSH_BUTTON_3664)
        player.animate(Animation(Animations.PUSH_BUTTON_VARROCK_MUSEUM_6462))

        val camera = PlayerCamera(player)
        camera.setPosition(pLoc.x, pLoc.y, pLoc.z)
        camera.rotateTo(pLoc.x, pLoc.y, 400, 300)
        camera.panTo(pLoc.x, pLoc.y, 500, 300)

        when (NPC_IDS[id]) {
            -1   -> animateScenery(getScenery(1781, 4964, 0)!!, DISPLAY_ANIMATION[id])
            -2   -> animateScenery(getScenery(1763, 4937, 0)!!, DISPLAY_ANIMATION[id])
            else -> findNPC(NPC_IDS[id])?.animate(Animation(DISPLAY_ANIMATION[id]))
        }
        runTask(player, watchTime) { resetCamera(player) }
    }

    private fun handleStudy(player: Player) {// Author: Bonesy.
        openInterface(player, NATURAL_HISTORY_EXAM_533)
        val model = getScenery(1763, 4937, 0)?.definition?.modelIds?.first()
        player.packetDispatch.sendModelOnInterface(model!!, NATURAL_HISTORY_EXAM_533, 3, 0)
        setComponentVisibility(player, NATURAL_HISTORY_EXAM_533, 27, false)
        sendString(player, "1", NATURAL_HISTORY_EXAM_533, 25)
        sendString(player, "Question", NATURAL_HISTORY_EXAM_533, 28)
        sendString(player, "1.", NATURAL_HISTORY_EXAM_533, 29)
        sendString(player, "2.", NATURAL_HISTORY_EXAM_533, 30)
        sendString(player, "3.", NATURAL_HISTORY_EXAM_533, 31)
    }

    private fun handleToolRack(player: Player, node: Node) {// Author: Bonesy.
        setTitle(player, 5)
        sendOptions(player, "Which tool would you like?", "Trowel", "Rock pick", "Specimen brush", "Leather gloves", "Leather boots")
        addDialogueAction(player) { _, button ->
            val item = when (button) {
                2 -> Items.TROWEL_676
                3 -> Items.ROCK_PICK_675
                4 -> Items.SPECIMEN_BRUSH_670
                5 -> Items.LEATHER_GLOVES_1059
                6 -> Items.LEATHER_BOOTS_1061
                else -> return@addDialogueAction
            }
            val name = getItemName(item).lowercase()
            val word = if (name.startsWith("leather")) "pair of " else ""
            if (!addItem(player, item)) sendMessage(player, "You don't have enough space in your inventory.")
            else sendItemDialogue(player, item, "You take a $word$name from the rack.")
        }
    }

    companion object {
        private val MUSEUM_DOOR       = intArrayOf(Scenery.DOOR_24565, Scenery.DOOR_24567)
        private val MUSEUM_STAIRS     = intArrayOf(Scenery.STAIRS_24427, Scenery.STAIRS_24428)
        private const val MUSEUM_GATE = Scenery.GATE_24536
        private const val TOOL_RACK   = Scenery.TOOLS_24535
        private val DISPLAY_ANIMATION = intArrayOf(6436, 6448, 6446, 6444, 6440, 6428, 6450, 6438, 6430, 6452, 6434, 6432, 6442, 6240)
        private val NPC_IDS           = intArrayOf(NPCs.LIZARD_DISPLAY_5975, NPCs.BATTLE_TORTOISE_DISPLAY_5981, NPCs.DRAGON_DISPLAY_5979, NPCs.WYVERN_DISPLAY_5980, NPCs.CAMEL_DISPLAY_5977, NPCs.LEECH_DISPLAY_5971, NPCs.MOLE_DISPLAY_5982, NPCs.PENGUIN_DISPLAY_5976, NPCs.SNAIL_DISPLAY_5973, -1, NPCs.MONKEY_DISPLAY_5974, NPCs.SEA_SLUGS_DISPLAY_5972, NPCs.TERRORBIRD_DISPLAY_5978, -2)

        fun openMuseumGate(player: Player) {
            val gate = getScenery(Location(3261, 3446, 0)) ?: return
            val fromNorth = player.location.y > 3446
            val direction = if (fromNorth) Direction.SOUTH else Direction.NORTH
            val target = player.location.transform(direction, 1)
            val guard = findNPC(NPCs.MUSEUM_GUARD_5941)

            replaceScenery(gate, gate.id, 3, Direction.NORTH_WEST, gate.location)

            runTask(player, 1) {
                guard?.let { animate(it, if (fromNorth) 6392 else 6391) }

                player.walkingQueue.run {
                    reset()
                    addPath(target.x, target.y)
                }
            }
        }
    }
}

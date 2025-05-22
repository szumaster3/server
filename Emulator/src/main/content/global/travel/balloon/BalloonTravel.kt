package content.global.travel.balloon

import content.data.GameAttributes
import core.api.*
import core.api.quest.hasRequirement
import core.api.quest.isQuestComplete
import core.api.ui.setMinimapState
import core.cache.def.impl.ItemDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.*

/**
 * Handles the balloon travel system.
 */
class BalloonTravel : InterfaceListener, InteractionListener {
    companion object {

        /**
         * Map of assistant NPCs and their respective locations where they initialize.
         */
        private val assistants = mapOf(
            NPCs.AUGUSTE_5050 to Location.create(2938, 3424, 0),
            NPCs.ASSISTANT_SERF_5053 to Location.create(3298, 3484, 0),
            NPCs.ASSISTANT_LE_SMITH_5056 to Location.create(2480, 3458, 0),
            NPCs.ASSISTANT_STAN_5057 to Location.create(2938, 3424, 0)
        )

        /**
         * ids for all balloon assistants for interaction listening.
         */
        private val allAssistants = (5049..5057).toIntArray()

        /**
         * Scenery object ids representing balloon stations.
         */
        private val balloonIds = intArrayOf(19128, 19129, 19133, 19135, 19143, 19141, 19137, 19139)

        /**
         * Shows the balloon map interface with certain destination(s) hidden depending on NPC.
         *
         * @param player The player to show the map to.
         * @param npc The assistant NPC initiating the interface.
         */
        fun showBalloonLocation(player: Player, npc: NPC) {
            val componentId = when (npc.id) {
                5049 -> 12
                5050 -> 22
                5053 -> 21
                5054, 5065 -> 20
                5055, 5063 -> 24
                5056 -> 23
                5057 -> 22
                else -> return
            }
            setComponentVisibility(player, Components.ZEP_BALLOON_MAP_469, componentId, false)
        }

        /**
         * Handles the process of taking a balloon flight from the player's current location
         * to the selected destination.
         *
         * @param player The player taking the flight.
         * @param flight The destination balloon location.
         */
        @JvmStatic
        fun handleFlight(player: Player, flight: Balloons) {
            lock(player, 4)
            lockInteractions(player, 4)

            if (inBorders(player, getRegionBorders(13110))) {
                finishDiaryTask(player, DiaryType.VARROCK, 2, 17)
            }

            val origin = player.getAttribute<Balloons>(GameAttributes.BALLOON_ORIGIN) ?: run {
                player.debug("null location.")
                return
            }

            val route = BalloonRoute.values().firstOrNull { it.from == origin && it.to == flight }
            val animationId = route?.animationId ?: -1

            playJingle(player, 118)
            setComponentVisibility(player, Components.ZEP_BALLOON_MAP_469, flight.componentId, false)
            animateInterface(player, Components.ZEP_BALLOON_MAP_469, flight.componentId, animationId)
            sendMessage(player, "You board the balloon and fly to ${flight.areaName}.")

            submitWorldPulse(object : Pulse(1) {
                private var count = 0
                override fun pulse(): Boolean {
                    when (count++) {
                        0 -> {
                            closeInterface(player)
                            openInterface(player, Components.FADE_TO_BLACK_120)
                        }
                        3 -> {
                            setMinimapState(player, 2)
                            openInterface(player, Components.ZEP_BALLOON_MAP_469)
                        }
                        4 -> teleport(player, flight.flightDestination)
                        6 -> {
                            closeInterface(player)
                            setMinimapState(player, 0)
                            openOverlay(player, Components.FADE_FROM_BLACK_170)
                        }
                        9 -> {
                            closeAllInterfaces(player)
                            sendPlainDialogue(player, false, "You arrive safely in ${flight.areaName}.")
                            unlock(player)
                            return true
                        }
                    }
                    return false
                }
            })
        }
    }

    init {
        assistants.forEach { (npcId, location) ->
            NPC(npcId, location).apply {
                init()
                isWalks = true
            }
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.ZEP_BALLOON_MAP_469) { player, _, _, buttonID, _, _ ->
            val destination = Balloons.values().firstOrNull { it.button == buttonID } ?: return@on true

            if (!hasLevelStat(player, Skills.FIREMAKING, destination.requiredLevel)) {
                sendDialogue(player, "You require a Firemaking level of ${destination.requiredLevel} to travel to ${destination.areaName}.")
                return@on true
            }

            if (!inInventory(player, destination.logId, 1)) {
                sendDialogue(player, "You need at least one ${getItemName(destination.logId).lowercase().removeSuffix("s").trim()}.")
                return@on true
            }

            sendMessage(player, "You are quickly searched.")

            if (destination == Balloons.ENTRANA && !ItemDefinition.canEnterEntrana(player)) {
                sendDialogue(player, "You can't take flight with weapons and armour to Entrana.")
                return@on true
            }

            if (player.familiarManager.hasFamiliar()) {
                sendMessage(player, "You can't take a follower on a ride.")
                return@on true
            }

            if (removeItem(player, Item(destination.logId, 1))) {
                handleFlight(player, destination)
            }
            return@on true
        }
    }

    override fun defineListeners() {
        on(balloonIds, IntType.SCENERY, "use") { player, node ->
            val wrapperId = node.asScenery().getWrapper().id
            val origin = Balloons.values().firstOrNull { wrapperId in it.sceneryIds } ?: return@on false

            player.setAttribute(GameAttributes.BALLOON_ORIGIN, origin)

            if (!hasRequirement(player, Quests.ENLIGHTENED_JOURNEY)) return@on true
            openInterface(player, Components.ZEP_BALLOON_MAP_469)

            val componentId = when (wrapperId) {
                19128, 19133 -> 12
                19135 -> 22
                19137 -> 24
                19139 -> 23
                19141 -> 20
                19143 -> 21
                else -> return@on false
            }
            setComponentVisibility(player, Components.ZEP_BALLOON_MAP_469, componentId, false)
            return@on true
        }

        on(allAssistants, IntType.NPC, "fly") { player, node ->
            if (!isQuestComplete(player, Quests.ENLIGHTENED_JOURNEY)) {
                sendMessage(player, "You must complete ${Quests.ENLIGHTENED_JOURNEY} before you can use it.")
            } else {
                showBalloonLocation(player, node.asNpc())
                openInterface(player, Components.ZEP_BALLOON_MAP_469)
            }
            return@on true
        }
    }
}

/**
 * Enum class representing balloon travel destinations and their associated metadata.
 */
enum class Balloons(
    val areaName: String,
    val flightDestination: Location,
    val logId: Int,
    val requiredLevel: Int,
    val varbitId: Int,
    val componentId: Int,
    val button: Int,
    val sceneryIds: List<Int> = emptyList()
) {
    ENTRANA("Entrana", Location(2809, 3356), Items.LOGS_1511, 20, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_ENTRANA_BALLOON_2867, 25, 17, listOf(19133)),
    TAVERLEY("Taverley", Location(2940, 3420), Items.LOGS_1511, 20, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_TAVERLEY_BALLOON_2868, 22, 18, listOf(19139)),
    CRAFT_GUILD("Crafting Guild", Location(2924, 3303), Items.OAK_LOGS_1521, 30, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_CRAFTING_GUILD_BALLOON_2871, 20, 16, listOf(19137)),
    VARROCK("Varrock", Location(3298, 3481), Items.WILLOW_LOGS_1519, 40, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_VARROCK_BALLOON_2872, 21, 19, listOf(19143)),
    CASTLE_WARS("Castle Wars", Location(2462, 3108), Items.YEW_LOGS_1515, 50, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_CASTLE_WARS_BALLOON_2869, 24, 14, listOf(19128)),
    GRAND_TREE("Grand Tree", Location(2480, 3458), Items.MAGIC_LOGS_1513, 60, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_GRAND_TREE_BALLOON_2870, 23, 15, listOf(19141));

}

/**
 * Enum class representing specific flight routes between two balloon locations.
 */
enum class BalloonRoute(val from: Balloons, val to: Balloons, val animationId: Int = -1) {
    ENTRANA_TO_TAVERLEY(Balloons.ENTRANA, Balloons.TAVERLEY, 5110),
    TAVERLEY_TO_ENTRANA(Balloons.TAVERLEY, Balloons.ENTRANA, 5111),
    ENTRANA_TO_CRAFT_GUILD(Balloons.ENTRANA, Balloons.CRAFT_GUILD, 5112),
    CRAFT_GUILD_TO_ENTRANA(Balloons.CRAFT_GUILD, Balloons.ENTRANA, 5113),
    ENTRANA_TO_VARROCK(Balloons.ENTRANA, Balloons.VARROCK, 5114),
    VARROCK_TO_ENTRANA(Balloons.VARROCK, Balloons.ENTRANA, 5115),
    ENTRANA_TO_GRAND_TREE(Balloons.ENTRANA, Balloons.GRAND_TREE, 5116),
    GRAND_TREE_TO_ENTRANA(Balloons.GRAND_TREE, Balloons.ENTRANA, 5117),
    ENTRANA_TO_CASTLE_WARS(Balloons.ENTRANA, Balloons.CASTLE_WARS, 5118),
    CASTLE_WARS_TO_ENTRANA(Balloons.CASTLE_WARS, Balloons.ENTRANA, 5119),
    VARROCK_TO_CRAFT_GUILD(Balloons.VARROCK, Balloons.CRAFT_GUILD, 5120),
    CRAFT_GUILD_TO_VARROCK(Balloons.CRAFT_GUILD, Balloons.VARROCK, 5121),
    VARROCK_TO_TAVERLEY(Balloons.VARROCK, Balloons.TAVERLEY, 5122),
    TAVERLEY_TO_VARROCK(Balloons.TAVERLEY, Balloons.VARROCK, 5123),
    TAVERLEY_TO_CRAFT_GUILD(Balloons.TAVERLEY, Balloons.CRAFT_GUILD, 5124),
    CRAFT_GUILD_TO_TAVERLEY(Balloons.CRAFT_GUILD, Balloons.TAVERLEY, 5125),
    TAVERLEY_TO_CASTLE_WARS(Balloons.TAVERLEY, Balloons.CASTLE_WARS, 5126),
    CASTLE_WARS_TO_TAVERLEY(Balloons.CASTLE_WARS, Balloons.TAVERLEY, 5127),
    CRAFT_GUILD_TO_CASTLE_WARS(Balloons.CRAFT_GUILD, Balloons.CASTLE_WARS, 5128),
    CASTLE_WARS_TO_CRAFT_GUILD(Balloons.CASTLE_WARS, Balloons.CRAFT_GUILD, 5129),
    VARROCK_TO_CASTLE_WARS(Balloons.VARROCK, Balloons.CASTLE_WARS, 5130),
    CASTLE_WARS_TO_VARROCK(Balloons.CASTLE_WARS, Balloons.VARROCK, 5131),
    GRAND_TREE_TO_CASTLE_WARS(Balloons.GRAND_TREE, Balloons.CASTLE_WARS, 5132),
    CASTLE_WARS_TO_GRAND_TREE(Balloons.CASTLE_WARS, Balloons.GRAND_TREE, 5133),
    GRAND_TREE_TO_CRAFT_GUILD(Balloons.GRAND_TREE, Balloons.CRAFT_GUILD, 5134),
    CRAFT_GUILD_TO_GRAND_TREE(Balloons.CRAFT_GUILD, Balloons.GRAND_TREE, 5135),
    TAVERLEY_TO_GRAND_TREE(Balloons.TAVERLEY, Balloons.GRAND_TREE, 5136),
    GRAND_TREE_TO_TAVERLEY(Balloons.GRAND_TREE, Balloons.TAVERLEY, 5137),
    VARROCK_TO_GRAND_TREE(Balloons.VARROCK, Balloons.GRAND_TREE, 5138),
    GRAND_TREE_TO_VARROCK(Balloons.GRAND_TREE, Balloons.VARROCK, 5139);
}
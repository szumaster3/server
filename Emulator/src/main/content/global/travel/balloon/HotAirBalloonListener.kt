package content.global.travel.balloon

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

class HotAirBalloonListener :
    InterfaceListener,
    InteractionListener {
    companion object {
        private val assistants =
            mapOf(
                NPCs.AUGUSTE_5050 to Location.create(2938, 3424, 0),
                NPCs.ASSISTANT_SERF_5053 to Location.create(3298, 3484, 0),
                NPCs.ASSISTANT_LE_SMITH_5056 to Location.create(2480, 3458, 0),
                NPCs.ASSISTANT_STAN_5057 to Location.create(2938, 3424, 0),
            )
        private val allAssistants = (5049..5057).toIntArray()
        private val balloonIds = intArrayOf(19128, 19129, 19133, 19135, 19143, 19141, 19137, 19139)

        fun showBalloonLocation(
            player: Player,
            npc: NPC,
        ) {
            val componentId =
                when (npc.id) {
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

        @JvmStatic
        fun handleFlight(
            player: Player,
            flight: FlightDestination,
        ) {
            lock(player, 4)
            lockInteractions(player, 4)

            if (inBorders(player, getRegionBorders(13110))) {
                finishDiaryTask(player, DiaryType.VARROCK, 2, 17)
            }

            animateInterface(player, Components.ZEP_BALLOON_MAP_469, flight.button, flight.flyAnim)
            sendMessage(player, "You board the balloon and fly to ${flight.areaName}.")

            submitWorldPulse(
                object : Pulse(1) {
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

                            4 -> {
                                teleport(player, flight.flightDestination)
                            }

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
                },
            )
        }

        enum class FlightDestination(
            val areaName: String,
            val flightDestination: Location,
            val flyAnim: Int,
            val button: Int,
            val logId: Int,
            val varbitId: Int,
            val requiredLevel: Int,
        ) {
            CASTLE_WARS(
                "Castle Wars",
                Location.create(2462, 3108, 0),
                5140,
                14,
                Items.YEW_LOGS_1515,
                Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_CASTLE_WARS_BALLOON_2869,
                50,
            ),
            GRAND_TREE(
                "Grand Tree",
                Location.create(2480, 3458, 0),
                5141,
                15,
                Items.MAGIC_LOGS_1513,
                Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_GRAND_TREE_BALLOON_2870,
                60,
            ),
            CRAFT_GUILD(
                "Crafting Guild",
                Location.create(2924, 3303, 0),
                5142,
                16,
                Items.OAK_LOGS_1521,
                Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_CRAFTING_GUILD_BALLOON_2871,
                30,
            ),
            VARROCK(
                "Varrock",
                Location.create(3298, 3481, 0),
                5142,
                19,
                Items.WILLOW_LOGS_1519,
                Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_VARROCK_BALLOON_2872,
                40,
            ),
            ENTRANA(
                "Entrana",
                Location.create(2809, 3356, 0),
                5142,
                17,
                Items.LOGS_1511,
                Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_ENTRANA_BALLOON_2867,
                20,
            ),
            TAVERLEY(
                "Taverley",
                Location.create(2940, 3420, 0),
                5142,
                18,
                Items.LOGS_1511,
                Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_TAVERLEY_BALLOON_2868,
                20,
            ),
            ;

            companion object {
                val flightMap = values().associateBy { it.button }
            }
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
            val button = FlightDestination.flightMap[buttonID] ?: return@on true

            if (!hasLevelStat(player, Skills.FIREMAKING, button.requiredLevel)) {
                sendDialogue(
                    player,
                    "You require a Firemaking level of ${button.requiredLevel} to travel to ${button.areaName}.",
                )
                return@on true
            }

            if (!inInventory(player, button.logId, 1)) {
                sendDialogue(
                    player,
                    "You need at least one ${getItemName(button.logId).lowercase().replace("s", "").trim()}.",
                )
                return@on true
            }

            if (buttonID == 17 && !ItemDefinition.canEnterEntrana(player)) {
                sendDialogue(player, "You can't take flight with weapons and armour to Entrana.")
                return@on true
            }

            if (player.familiarManager.hasFamiliar()) {
                sendMessage(player, "You can't take a follower on a ride.")
                return@on true
            }

            if (removeItem(player, Item(button.logId, 1))) {
                handleFlight(player, button)
            }
            return@on true
        }
    }

    override fun defineListeners() {
        on(balloonIds, IntType.SCENERY, "use") { player, node ->
            when (node.id) {
                5054 -> openDialogue(player, 5065)
                else -> {
                    if (!hasRequirement(player, Quests.ENLIGHTENED_JOURNEY)) return@on true
                    openInterface(player, Components.ZEP_BALLOON_MAP_469)
                    val componentId =
                        when (node.asScenery().getWrapper().id) {
                            19128, 19133 -> 12
                            19135 -> 22
                            19137 -> 24
                            19139 -> 23
                            19141 -> 20
                            19143 -> 21
                            else -> return@on false
                        }
                    setComponentVisibility(player, Components.ZEP_BALLOON_MAP_469, componentId, false)
                }
            }
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

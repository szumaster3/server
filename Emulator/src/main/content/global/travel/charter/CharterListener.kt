package content.global.travel.charter

import core.api.*
import core.api.quest.isQuestComplete
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.repository.Repository.findNPC
import core.game.world.update.flag.context.Animation
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class CharterListener :
    InteractionListener,
    InterfaceListener {
    override fun defineListeners() {
        on(SAILORS, IntType.NPC, "charter") { player, _ ->
            CharterUtils.open(player)
            return@on true
        }

        on(LADDER, IntType.SCENERY, "climb-down", "climb-up") { player, _ ->
            when (getUsedOption(player)) {
                "climb-up" -> ClimbActionHandler.climb(player, Animation(828), Location(3048, 3208, 1))
                else -> ClimbActionHandler.climb(player, Animation(828), Location(3048, 9640, 1))
            }
            return@on true
        }

        on(Scenery.GANGPLANK_69, IntType.SCENERY, "cross") { player, _ ->
            player.dialogueInterpreter.open(NPCs.ARHEIN_563, findNPC(NPCs.ARHEIN_563), true)
            return@on true
        }

        on(Scenery.GANGPLANK_2593, IntType.SCENERY, "cross") { player, _ ->
            if (isQuestComplete(player, Quests.DRAGON_SLAYER)) {
                player.dialogueInterpreter.open(NPCs.KLARENSE_744, findNPC(NPCs.KLARENSE_744), true)
                return@on true
            }
            if (!player.getSavedData().questData.getDragonSlayerAttribute("ship")) {
                openDialogue(player, NPCs.KLARENSE_744, findNPC(NPCs.KLARENSE_744)!!, true)
            } else {
                sendMessage(player, "You board the ship.")
                cross(player, Location(3047, 3207, 1))
            }
            return@on true
        }

        on(Scenery.GANGPLANK_11209, IntType.SCENERY, "cross") { player, _ ->
            sendDialogueLines(
                player,
                "I don't think that whoever owns this ship will be happy",
                "with me wandering all over it.",
            )
            return@on true
        }

        on(PLANK, IntType.SCENERY, "cross") { player, node ->
            lock(player, 1)
            ForceMovement.run(
                player,
                player.location,
                node.location,
                ForceMovement.WALK_ANIMATION,
                ForceMovement.WALKING_SPEED,
            )
            if (getUsedOption(player) == "cross") {
                when (node.id) {
                    Scenery.GANGPLANK_2081 -> cross(player, KARAMJA[0])
                    Scenery.GANGPLANK_2082 -> cross(player, KARAMJA[1])
                    Scenery.GANGPLANK_2083 -> cross(player, PORT_SARIM[4])
                    Scenery.GANGPLANK_2084 -> cross(player, PORT_SARIM[5])
                    Scenery.GANGPLANK_2085 ->
                        cross(player, ARDOUGNE[0]).also {
                            sendMessage(
                                player,
                                "You must speak to Captain Barnaby before it will set sail.",
                            )
                        }

                    Scenery.GANGPLANK_2086 -> cross(player, ARDOUGNE[1])
                    Scenery.GANGPLANK_2087 ->
                        cross(player, BRIMHAVEN[2]).also {
                            sendMessage(
                                player,
                                "You must speak to the Customs Officer before it will set sail.",
                            )
                        }

                    Scenery.GANGPLANK_2088 -> cross(player, BRIMHAVEN[3])
                    Scenery.GANGPLANK_2412 -> cross(player, PORT_SARIM[0])
                    Scenery.GANGPLANK_2413 -> cross(player, PORT_SARIM[1])
                    Scenery.GANGPLANK_2414 -> cross(player, ENTRANA[0])
                    Scenery.GANGPLANK_2415 -> cross(player, ENTRANA[1])
                    Scenery.GANGPLANK_2594 -> cross(player, Location(3047, 3204, 0))
                    Scenery.GANGPLANK_11211 -> cross(player, MOS_SHIP[0])
                    Scenery.GANGPLANK_11212 -> cross(player, MOS_SHIP[1])
                    Scenery.GANGPLANK_14304 ->
                        cross(player, PORT_SARIM[6]).also {
                            sendMessage(player, "You board the ship.")
                        }

                    Scenery.GANGPLANK_14305 ->
                        cross(player, PORT_SARIM[7]).also {
                            sendMessage(player, "You disembark the ship.")
                        }

                    Scenery.GANGPLANK_14306 ->
                        cross(player, PEST_CONTROL[0]).also {
                            sendMessage(player, "You board the ship.")
                        }

                    Scenery.GANGPLANK_14307 ->
                        cross(player, PEST_CONTROL[1]).also {
                            sendMessage(player, "You disembark the ship.")
                        }

                    Scenery.GANGPLANK_17392 -> cross(player, PORT_PHASMATYS[0])
                    Scenery.GANGPLANK_17393 -> cross(player, PORT_PHASMATYS[1])
                    Scenery.GANGPLANK_17394 -> cross(player, CATHERBY[0])
                    Scenery.GANGPLANK_17395 -> cross(player, CATHERBY[1])
                    Scenery.GANGPLANK_17396 -> cross(player, SHIP_YARD[0])
                    Scenery.GANGPLANK_17397 -> cross(player, SHIP_YARD[1])
                    Scenery.GANGPLANK_17398 ->
                        cross(player, KARAMJA[2]).also {
                            sendMessage(player, "You must speak to the Customs Officer before it will set sail.")
                        }

                    Scenery.GANGPLANK_17399 ->
                        cross(player, KARAMJA[3]).also {
                            sendMessage(player, "You must speak to the Customs Officer before it will set sail.")
                        }

                    Scenery.GANGPLANK_17400 -> cross(player, BRIMHAVEN[0])
                    Scenery.GANGPLANK_17401 -> cross(player, BRIMHAVEN[1])
                    Scenery.GANGPLANK_17402 -> cross(player, PORT_KHAZARD[0])
                    Scenery.GANGPLANK_17403 -> cross(player, PORT_KHAZARD[1])
                    Scenery.GANGPLANK_17404 -> cross(player, PORT_SARIM[3])
                    Scenery.GANGPLANK_17405 -> cross(player, PORT_SARIM[2])
                    Scenery.GANGPLANK_17406 -> cross(player, MOS_LE_HARMESS[0])
                    Scenery.GANGPLANK_17407 -> cross(player, MOS_LE_HARMESS[1])
                    Scenery.GANGPLANK_17408 -> cross(player, TYRAS[0])
                    Scenery.GANGPLANK_17409 -> cross(player, TYRAS[1])
                    Scenery.GANGPLANK_29168 -> cross(player, OO_GLOG[0])
                    Scenery.GANGPLANK_29169 -> cross(player, OO_GLOG[1])
                    else ->
                        sendDialogueLines(
                            player,
                            "I don't think that whoever owns this ship will be happy",
                            "with me wandering all over it.",
                        )
                }
            }
            return@on true
        }
    }

    companion object {
        private val PLANK =
            intArrayOf(
                Scenery.GANGPLANK_2081,
                Scenery.GANGPLANK_2082,
                Scenery.GANGPLANK_2083,
                Scenery.GANGPLANK_2084,
                Scenery.GANGPLANK_2085,
                Scenery.GANGPLANK_2086,
                Scenery.GANGPLANK_2087,
                Scenery.GANGPLANK_2088,
                Scenery.GANGPLANK_2412,
                Scenery.GANGPLANK_2413,
                Scenery.GANGPLANK_2414,
                Scenery.GANGPLANK_2415,
                Scenery.GANGPLANK_2594,
                Scenery.GANGPLANK_11211,
                Scenery.GANGPLANK_11212,
                Scenery.GANGPLANK_14304,
                Scenery.GANGPLANK_14305,
                Scenery.GANGPLANK_14306,
                Scenery.GANGPLANK_14307,
                Scenery.GANGPLANK_17392,
                Scenery.GANGPLANK_17393,
                Scenery.GANGPLANK_17394,
                Scenery.GANGPLANK_17395,
                Scenery.GANGPLANK_17398,
                Scenery.GANGPLANK_17399,
                Scenery.GANGPLANK_17400,
                Scenery.GANGPLANK_17401,
                Scenery.GANGPLANK_17402,
                Scenery.GANGPLANK_17403,
                Scenery.GANGPLANK_17404,
                Scenery.GANGPLANK_17405,
                Scenery.GANGPLANK_17406,
                Scenery.GANGPLANK_17407,
                Scenery.GANGPLANK_17408,
                Scenery.GANGPLANK_17409,
                Scenery.GANGPLANK_29168,
                Scenery.GANGPLANK_29169,
            )
        private val LADDER = intArrayOf(Scenery.LADDER_2590, Scenery.LADDER_2592)
        private val PORT_SARIM =
            arrayOf(
                Location(3048, 3231, 1),
                Location.create(3048, 3234, 0),
                Location(3038, 3192, 0),
                Location(3038, 3189, 1),
                Location(3032, 3217, 1),
                Location(3029, 3217, 0),
                Location(3041, 3199, 1),
                Location(3041, 3202, 0),
            )
        private val ENTRANA = arrayOf(Location(2834, 3331, 1), Location(2834, 3335, 0))
        private val KARAMJA =
            arrayOf(Location(2956, 3143, 1), Location(2956, 3146, 0), Location(2957, 3158, 1), Location(2954, 3158, 0))
        private val CATHERBY = arrayOf(Location(2792, 3417, 1), Location(2792, 3414, 0))
        private val BRIMHAVEN =
            arrayOf(
                Location(2763, 3238, 1),
                Location.create(2760, 3238, 0),
                Location(2775, 3234, 1),
                Location(2772, 3234, 0),
            )
        private val ARDOUGNE = arrayOf(Location(2683, 3268, 1), Location(2683, 3271, 0))
        private val PORT_KHAZARD = arrayOf(Location(2674, 3141, 1), Location(2674, 3144, 0))
        private val PORT_PHASMATYS = arrayOf(Location(3705, 3503, 1), Location(3702, 3503, 0))
        private val PEST_CONTROL = arrayOf(Location(2662, 2676, 1), Location(2659, 2676, 0))
        private val MOS_LE_HARMESS = arrayOf(Location(3668, 2931, 1), Location(3671, 2931, 0))
        private val MOS_SHIP = arrayOf(Location(3684, 2950, 1), Location(3684, 2953, 0))
        private val TYRAS = arrayOf(Location(2142, 3125, 1), Location(2142, 3122, 0))
        private val SHIP_YARD = arrayOf(Location.create(2998, 3032, 1), Location(3000, 3032, 0))
        private val OO_GLOG = arrayOf(Location.create(2626, 2857, 1), Location.create(2623, 2857, 0))

        private val SAILORS =
            intArrayOf(
                NPCs.TRADER_STAN_4650,
                NPCs.TRADER_CREWMEMBER_4651,
                NPCs.TRADER_CREWMEMBER_4652,
                NPCs.TRADER_CREWMEMBER_4653,
                NPCs.TRADER_CREWMEMBER_4654,
                NPCs.TRADER_CREWMEMBER_4655,
                NPCs.TRADER_CREWMEMBER_4656,
            )

        fun cross(
            player: Player,
            location: Location?,
        ) {
            queueScript(player, 2, QueueStrength.STRONG) {
                player.properties.teleportLocation = location
                return@queueScript stopExecuting(player)
            }
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.SAILING_TRANSPORT_WORLD_MAP_95) { player, _, _, buttonID, _, _ ->
            CharterUtils.handle(player, buttonID)
            return@on true
        }
    }
}

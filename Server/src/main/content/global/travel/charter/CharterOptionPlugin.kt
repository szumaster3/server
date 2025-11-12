package content.global.travel.charter

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.repository.Repository.findNPC
import shared.consts.Components
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery

class CharterOptionPlugin : InteractionListener, InterfaceListener {

    override fun defineListeners() {

        /*
         * Handles quick travel option for Sailors NPC.
         */

        on(SAILORS, IntType.NPC, "charter") { player, _ ->
            CharterShipTransportation.open(player)
            return@on true
        }

        /*
         * Handles interaction with gangplank.
         */

        on(Gangplank.values().map { it.objectId }.toIntArray(), IntType.SCENERY, "cross") { player, node ->
            handleCrossGangplank(player, node)
            return@on true
        }

        /*
         * Handles special interaction with gangplank.
         */

        on(Scenery.GANGPLANK_69, IntType.SCENERY, "cross") { player, _ ->
            player.dialogueInterpreter.open(NPCs.ARHEIN_563, findNPC(NPCs.ARHEIN_563), true)
            return@on true
        }

        /*
         * Handles special interaction with gangplank 2.
         */

        on(Scenery.GANGPLANK_2593, IntType.SCENERY, "cross") { player, _ ->
            when {
                isQuestComplete(player, Quests.DRAGON_SLAYER) -> player.dialogueInterpreter.open(
                    NPCs.KLARENSE_744,
                    findNPC(NPCs.KLARENSE_744),
                    true
                )

                !player.getSavedData().questData.getDragonSlayerAttribute("ship") -> openDialogue(
                    player,
                    NPCs.KLARENSE_744,
                    findNPC(NPCs.KLARENSE_744)!!,
                    true
                )

                else -> {
                    sendMessage(player, "You board the ship.")
                    cross(player, Location(3047, 3207, 1))
                }
            }
            return@on true
        }

        /*
         * Handles special interaction with gangplank 3.
         */

        on(Scenery.GANGPLANK_11209, IntType.SCENERY, "cross") { player, _ ->
            sendDialogueLines(
                player, "I don't think that whoever owns this ship will be happy", "with me wandering all over it."
            )
            return@on true
        }
    }

    companion object {
        /**
         * Represents the sailor NPC.
         */
        private val SAILORS = intArrayOf(
            NPCs.TRADER_STAN_4650,
            NPCs.TRADER_CREWMEMBER_4651,
            NPCs.TRADER_CREWMEMBER_4652,
            NPCs.TRADER_CREWMEMBER_4653,
            NPCs.TRADER_CREWMEMBER_4654,
            NPCs.TRADER_CREWMEMBER_4655,
            NPCs.TRADER_CREWMEMBER_4656
        )

        /**
         * Handles crossing the gangplank between boat and pier.
         */
        private fun handleCrossGangplank(player: Player, node: Node) {
            lock(player, 1)
            forceMove(player, player.location, node.location, 30, 30, null)

            val data = Gangplank.forId(node.id)
            if (data != null) {
                data.message?.let { sendMessage(player, it) }
                cross(player, data.destination)
            } else {
                sendDialogueLines(
                    player, "I don't think that whoever owns this ship will be happy", "with me wandering all over it."
                )
            }
        }

        private fun cross(player: Player, location: Location) {
            registerLogoutListener(player, "cross-gangplank") {
                player.properties.teleportLocation = location
            }
            queueScript(player, 2, QueueStrength.STRONG) {
                player.properties.teleportLocation = location
                return@queueScript stopExecuting(player)
            }
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.SAILING_TRANSPORT_WORLD_MAP_95) { player, _, _, buttonID, _, _ ->
            CharterShipTransportation.handle(player, buttonID)
            return@on true
        }
    }
}

/**
 * Represents gangplank objects used for boarding
 * and disembarking ships in various locations.
 */
private enum class Gangplank(val objectId: Int, val destination: Location, val message: String? = null) {
    ARD_0(Scenery.GANGPLANK_2085,  Location(2683, 3268, 1), "You must speak to Captain Barnaby before it will set sail."),
    ARD_1(Scenery.GANGPLANK_2086,  Location(2683, 3271, 0)),
    BRI_0(Scenery.GANGPLANK_2087,  Location(2775, 3234, 1), "You must speak to the Customs Officer before it will set sail."),
    BRI_1(Scenery.GANGPLANK_2088,  Location(2772, 3234, 0)),
    BRI_2(Scenery.GANGPLANK_17400, Location(2763, 3238, 1)),
    BRI_3(Scenery.GANGPLANK_17401, Location(2760, 3238, 0)),
    CAT_0(Scenery.GANGPLANK_17394, Location(2792, 3417, 1)),
    CAT_1(Scenery.GANGPLANK_17395, Location(2792, 3414, 0)),
    ENT_0(Scenery.GANGPLANK_2414,  Location(2834, 3331, 1)),
    ENT_1(Scenery.GANGPLANK_2415,  Location(2834, 3335, 0)),
    KAR_0(Scenery.GANGPLANK_2081,  Location(2956, 3143, 1)),
    KAR_1(Scenery.GANGPLANK_2082,  Location(2956, 3146, 0)),
    KAR_2(Scenery.GANGPLANK_17398, Location(2957, 3158, 1), "You must speak to the Customs Officer before it will set sail."),
    KAR_3(Scenery.GANGPLANK_17399, Location(2954, 3158, 0), "You must speak to the Customs Officer before it will set sail."),
    MOS_0(Scenery.GANGPLANK_17406, Location(3668, 2931, 1)),
    MOS_1(Scenery.GANGPLANK_17407, Location(3671, 2931, 0)),
    MSS_0(Scenery.GANGPLANK_11211, Location(3684, 2950, 1)),
    MSS_1(Scenery.GANGPLANK_11212, Location(3684, 2953, 0)),
    OOG_0(Scenery.GANGPLANK_29168, Location(2626, 2857, 1)),
    OOG_1(Scenery.GANGPLANK_29169, Location(2623, 2857, 0)),
    PCT_0(Scenery.GANGPLANK_14306, Location(2662, 2676, 1), "You board the ship."),
    PCT_1(Scenery.GANGPLANK_14307, Location(2659, 2676, 0), "You disembark the ship."),
    PKH_0(Scenery.GANGPLANK_17402, Location(2674, 3141, 1)),
    PKH_1(Scenery.GANGPLANK_17403, Location(2674, 3144, 0)),
    PPH_0(Scenery.GANGPLANK_17392, Location(3705, 3503, 1)),
    PPH_1(Scenery.GANGPLANK_17393, Location(3702, 3503, 0)),
    PSA_0(Scenery.GANGPLANK_2083,  Location(3032, 3217, 1)),
    PSA_1(Scenery.GANGPLANK_2084,  Location(3029, 3217, 0)),
    PSA_2(Scenery.GANGPLANK_2412,  Location(3048, 3231, 1)),
    PSA_3(Scenery.GANGPLANK_2413,  Location(3048, 3234, 0)),
    PSA_4(Scenery.GANGPLANK_2594,  Location(3047, 3204, 0)),
    PSA_5(Scenery.GANGPLANK_14304, Location(3041, 3199, 1), "You board the ship."),
    PSA_6(Scenery.GANGPLANK_14305, Location(3041, 3202, 0), "You disembark the ship."),
    PSA_7(Scenery.GANGPLANK_17404, Location(3038, 3189, 1)),
    PSA_8(Scenery.GANGPLANK_17405, Location(3038, 3192, 0)),
    SHY_0(Scenery.GANGPLANK_17396, Location(2998, 3032, 1)),
    SHY_1(Scenery.GANGPLANK_17397, Location(3000, 3032, 0)),
    TYR_0(Scenery.GANGPLANK_17408, Location(2142, 3125, 1)),
    TYR_1(Scenery.GANGPLANK_17409, Location(2142, 3122, 0));

    companion object {
        private val map = values().associateBy { it.objectId }
        fun forId(id: Int): Gangplank? = map[id]
    }
}
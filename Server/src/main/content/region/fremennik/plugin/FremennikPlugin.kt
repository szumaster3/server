package content.region.fremennik.plugin

import content.region.fremennik.dialogue.JarvaldTravelDialogue
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery

/**
 * Handles Fremennik region travel options.
 */
class FremennikPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles travel between Rellekka and Neitiznot.
         */

        on(NPCs.MARIA_GUNNARS_5508, IntType.NPC, "ferry-neitiznot") { player, _ ->
            if (!requireQuest(player, Quests.THE_FREMENNIK_TRIALS, "")) return@on true
            FremennikShipHelper.sail(player, Travel.RELLEKKA_TO_NEITIZNOT)
            return@on true
        }

        /*
         * Handles travel between Neitiznot and Rellekka.
         */

        on(NPCs.MARIA_GUNNARS_5507, IntType.NPC, "ferry-rellekka") { player, _ ->
            FremennikShipHelper.sail(player, Travel.NEITIZNOT_TO_RELLEKKA)
            return@on true
        }

        /*
         * Handles travel between Rellekka and Jatizso.
         */

        on(NPCs.MORD_GUNNARS_5481, IntType.NPC, "ferry-jatizso") { player, _ ->
            if (!requireQuest(player, Quests.THE_FREMENNIK_TRIALS, "")) return@on true
            FremennikShipHelper.sail(player, Travel.RELLEKKA_TO_JATIZSO)
            return@on true
        }

        /*
         * Handles travel between Jatizso and Rellekka.
         */

        on(NPCs.MORD_GUNNARS_5482, IntType.NPC, "ferry-rellekka") { player, _ ->
            FremennikShipHelper.sail(player, Travel.JATIZSO_TO_RELLEKKA)
            return@on true
        }

        /*
         * Handles travel between Rellekka and Miscellania.
         */

        on(NPCs.SAILOR_1385, IntType.NPC, "travel") { player, _ ->
            if (!requireQuest(player, Quests.THE_FREMENNIK_TRIALS, "")) return@on true
            FremennikShipHelper.sail(player, Travel.RELLEKKA_TO_MISC)
            return@on true
        }

        /*
         * Handles travel between Miscellania and Rellekka.
         */

        on(NPCs.SAILOR_1304, IntType.NPC, "travel") { player, _ ->
            if (!requireQuest(player, Quests.THE_FREMENNIK_TRIALS, "")) return@on true
            FremennikShipHelper.sail(player, Travel.MISC_TO_RELLEKKA)
            return@on true
        }

        /*
         * Handles travel between Iceberg and Rellekka.
         */

        on(Scenery.BOAT_21175, IntType.SCENERY, "travel") { player, obj ->
            if (obj.location == Location(2654, 3985, 1)) {
                FremennikShipHelper.sail(player, Travel.ICEBERG_TO_RELLEKKA)
            }
            return@on true
        }

        /*
         * Handles travel between Rellekka and Iceberg.
         */

        on(Scenery.BOAT_21176, IntType.SCENERY, "travel", "iceberg") { player, obj ->
            val option = getUsedOption(player)
            if (obj.location == Location(2708, 3732)) {
                if (option.equals("iceberg", true)) {
                    FremennikShipHelper.sail(player, Travel.RELLEKKA_TO_ICEBERG)
                } else {
                    setTitle(player, 2)
                    sendOptions(player, "Where would you like to travel?", "Iceberg", "Stay here")
                    addDialogueAction(player) { _, button ->
                        if (button == 1) FremennikShipHelper.sail(player, Travel.RELLEKKA_TO_ICEBERG)
                        else closeDialogue(player)
                    }
                }
            }
            return@on true
        }

        /*
         * Handles travel between Waterbirth Island and Rellekka.
         */

        on(intArrayOf(NPCs.NULL_2435, NPCs.JARVALD_2436, NPCs.JARVALD_2437, NPCs.JARVALD_2438), IntType.NPC, "travel") { player, node ->
            openDialogue(player, JarvaldTravelDialogue(), node.id)
            return@on true
        }
    }

}

package content.region.misthalin.handlers.rc_guild

import content.global.skill.runecrafting.items.Talisman
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.world.GameWorld
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Scenery

class RuneAltarMapInterface :
    InterfaceListener,
    InteractionListener {
    // Components for each altar icon that shows on the map table interface.
    private val altarComponents = intArrayOf(35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 47, 48)

    // Item IDs of all talismans. In-game command [::talismankit] add all talisman items to inventory :).
    val talismanIDs = Talisman.values().map { it.item.id }.toIntArray()

    // Map to link talisman item IDs with interface component IDs.
    // (The component 45 and 46 IDs was for: Elemental talisman [ID: 5516] and Soul talisman [ID: 1460]).
    private val talismanToComponentMap =
        mapOf(
            Items.AIR_TALISMAN_1438 to 35,
            Items.BODY_TALISMAN_1446 to 36,
            Items.MIND_TALISMAN_1448 to 37,
            Items.EARTH_TALISMAN_1440 to 38,
            Items.WATER_TALISMAN_1444 to 39,
            Items.FIRE_TALISMAN_1442 to 40,
            Items.CHAOS_TALISMAN_1452 to 41,
            Items.LAW_TALISMAN_1458 to 42,
            Items.BLOOD_TALISMAN_1450 to 43,
            Items.NATURE_TALISMAN_1462 to 44,
            Items.DEATH_TALISMAN_1456 to 47,
            Items.COSMIC_TALISMAN_1454 to 48,
        )

    override fun defineInterfaceListeners() {
        /*
         * Handles the interaction with the map table scenery to open the study interface.
         */

        on(Scenery.MAP_TABLE_38315, IntType.SCENERY, "Study") { player, _ ->
            openInterface(player, Components.RCGUILD_MAP_780)
            return@on true
        }

        /*
         * Handles the opening of the study interface.
         */

        onOpen(Components.RCGUILD_MAP_780) { player, _ ->
            if (inEquipment(player, Items.OMNI_TALISMAN_STAFF_13642) || inEquipment(player, Items.OMNI_TIARA_13655)) {
                for (rune in altarComponents) {
                    setComponentVisibility(player, Components.RCGUILD_MAP_780, rune, false).also {
                        sendString(
                            player,
                            "All the altars of " + GameWorld.settings!!.name + ".",
                            Components.RCGUILD_MAP_780,
                            33,
                        )
                    }
                }
            }
            return@onOpen true
        }

        /*
         * Handles use talisman item to reveal altar on the map.
         */

        onUseWith(IntType.SCENERY, talismanIDs, Scenery.MAP_TABLE_38315) { player, used, _ ->
            openInterface(player, Components.RCGUILD_MAP_780)
            val componentID = talismanToComponentMap[used.id] ?: 0
            if (componentID != 0) {
                setComponentVisibility(player, Components.RCGUILD_MAP_780, componentID, false)
            }
            return@onUseWith true
        }

        /*
         * Handles the interaction with the Omni items on the map table.
         * If the Omni Talisman or Omni Tiara equipped, gain access to all
         * altar locations on the map.
         */

        onUseWith(IntType.SCENERY, Items.OMNI_TALISMAN_13649, Scenery.MAP_TABLE_38315) { player, _, _ ->
            if (!inEquipment(player, Items.OMNI_TALISMAN_13649) || !inEquipment(player, Items.OMNI_TIARA_13655)) {
                openInterface(player, Components.RCGUILD_MAP_780)
                for (componentID in altarComponents) {
                    setComponentVisibility(player, Components.RCGUILD_MAP_780, componentID, false).also {
                        sendString(
                            player,
                            "All the altars of " + GameWorld.settings!!.name + ".",
                            Components.RCGUILD_MAP_780,
                            33,
                        )
                    }
                }
            }
            return@onUseWith true
        }
    }
}

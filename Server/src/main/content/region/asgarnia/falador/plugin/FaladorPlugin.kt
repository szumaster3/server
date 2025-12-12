package content.region.asgarnia.falador.plugin

import content.region.asgarnia.falador.dialogue.*
import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.DARK_RED
import shared.consts.*

class FaladorPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Before and after the quest they depict a generic person.
         * During While Guthix Sleeps they are replaced by wanted
         * posters which show a picture of the Dark Squall.
         */

        on(POSTER, IntType.SCENERY, "look-at") { player, _ ->
            openInterface(player, 819)
            return@on true
        }

        /*
         * Handles interaction with noticeboard at Falador bank.
         */

        on(Scenery.NOTICEBOARD_11755, IntType.SCENERY, "read") { player, _ ->
            val notice =
                arrayOf(
                    "If you're worried about someone getting on",
                    "your account and stealing items from your",
                    "bank, why not protect yourself with a Bank",
                    "PIN? A Bank PIN is a four-digit number.",
                    "",
                    "It's like an extra password that protects ",
                    "your bank account. If you set one, you'll",
                    "be asked to enter it before you can take",
                    "items out of the bank.",
                    "",
                    "If you're interested, speak to the bankers",
                    "and ask speak to the bankers and ask to see",
                    "your PIN settings. But remember,",
                    "${DARK_RED}KEEP YOUR PIN SECRET!",
                )
            openInterface(player, Components.BLANK_SCROLL_222)
            sendString(player, notice.joinToString("<br>"), Components.BLANK_SCROLL_222, 2)
            return@on true
        }

        on(DOORS, IntType.SCENERY, "close") { player, node ->
            DoorActionHandler.handleDoor(player, node.asScenery())
            return@on true
        }

        on(CUPBOARD_CLOSED, IntType.SCENERY, "open") { player, node ->
            face(player, node)
            animate(player, Animations.HUMAN_OPEN_WARDROBE_542)
            playAudio(player, Sounds.CUPBOARD_OPEN_58)
            replaceScenery(node.asScenery(), CUPBOARD_OPEN, -1)
            return@on true
        }

        /*
         * Handles use the coins on Tina NPC.
         */

        onUseWith(IntType.NPC, Items.COINS_995, NPCs.TINA_3218) { player, used, with ->
            removeItem(player, Item(used.id, 1), Container.INVENTORY)
            sendNPCDialogue(player, with.id, "Thanks!")
            return@onUseWith true
        }

        /*
         * Handles quest interaction for cupboard.
         */

        on(CUPBOARD_OPEN, IntType.SCENERY, "search", "shut") { player, node ->
            when (getUsedOption(player)) {
                "shut" -> {
                    face(player, node)
                    animate(player, Animations.CLOSE_CUPBOARD_543)
                    playAudio(player, Sounds.CUPBOARD_CLOSE_57)
                    replaceScenery(node.asScenery(), CUPBOARD_CLOSED, -1)
                    return@on true
                }

                "search" -> {
                    if (!inInventory(player, QUEST_ITEM)) {
                        sendItemDialogue(player, QUEST_ITEM, "You find a small portrait in here which you take.")
                        addItem(player, QUEST_ITEM)
                    } else {
                        sendDialogue(player, "There is just a load of junk in here.")
                    }
                }
            }
            return@on true
        }

        /*
         * Handles white castle ladders.
         */

        on(CASTLE_STAIRS, IntType.SCENERY, "climb-up", "climb-down") { player, node ->
            val option = getUsedOption(player)
            val loc = node.location

            when (node.id) {
                11729 -> if (option == "climb-up") {
                    if (loc == Location(3011, 3338, 0)) {
                        teleport(player, Location(3011, 3337, 1))
                    } else {
                        when (player.location.z) {
                            0 -> teleport(player, Location(2956, 3338, 1))
                            1 -> teleport(player, Location(2959, 3339, 2))
                            2 -> teleport(player, Location(2959, 3338, 3))
                        }
                    }
                }

                11731 -> if (option == "climb-down") {
                    if (loc == Location(3011, 3338, 1)) {
                        teleport(player, Location(3011, 3337, 0))
                    } else {
                        when (player.location.z) {
                            3 -> teleport(player, Location(2959, 3338, 2))
                            2 -> teleport(player, Location(2959, 3339, 1))
                            1 -> teleport(player, Location(2956, 3338, 0))
                        }
                    }
                }
            }
            return@on true
        }

        /*
         * Handles talking to NPCs around city.
         */

        on(NPCs.KNIGHT_660, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "He is too busy dancing to talk!")
            return@on true
        }

        on(NPCs.AMBASSADOR_SPANFIPPLE_4581, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, AmbassadorSpanfippleDialogue(), node.id)
            return@on true
        }

        on(NPCs.APPRENTICE_WORKMAN_3235, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, ApprenticeWorkmanDialogue(), node.id)
            return@on true
        }

        on(NPCs.CASSIE_577, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, CassieDialogue(), node.id)
            return@on true
        }

        on(NPCs.DROGO_DWARF_579, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, DrogoDwarfDialogue(), node.id)
            return@on true
        }

        on(NPCs.DRORKAR_7723, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, DrorkarDialogue(), node.id)
            return@on true
        }

        on(NPCs.DRUNKEN_MAN_3222, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, FaladorDrunkenManDialogue(), node.id)
            return@on true
        }

        on(intArrayOf(NPCs.GARDENER_1217, NPCs.GARDENER_3234), IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, FaladorGardenerDialogue(), node.id)
            return@on true
        }

        on(NPCs.MAN_3223, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, FaladorManDialogue(), node.id)
            return@on true
        }

        on(intArrayOf(NPCs.SHOPKEEPER_526, NPCs.SHOP_ASSISTANT_527), IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, FaladorShopkeeperDialogue(), node.id)
            return@on true
        }

        on(NPCs.WOMAN_3226, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, FaladorParkWomanDialogue(), node.id)
            return@on true
        }

        on(NPCs.FLYNN_580, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, FlynnDialogue(), node.id)
            return@on true
        }

        on(NPCs.GARDEN_SUPPLIER_4251, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, GardenSupplierDialogue(), node.id)
            return@on true
        }

        on(NPCs.HERQUIN_584, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, HerquinDialogue(), node.id)
            return@on true
        }

        on(NPCs.HURA_4563, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, HuraDialogue(), node.id)
            return@on true
        }

        on(NPCs.JEFF_3240, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, JeffDialogue(), node.id)
            return@on true
        }

        on(intArrayOf(NPCs.LAKKI_THE_DELIVERY_DWARF_7722, 7728), IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, LakkiTheDeliveryDwarfDialogue(), node.id)
            return@on true
        }

        on(intArrayOf(NPCs.LUCY_662, NPCs.MEGAN_661), IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, LucyPartyRoomDialogue(), node.id)
            return@on true
        }

        on(NPCs.NURMOF_594, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, NurmofDialogue(), node.id)
            return@on true
        }

        on(NPCs.ORACLE_746, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, OracleDialogue(), node.id)
            return@on true
        }

        on(intArrayOf(NPCs.EMILY_736, NPCs.KAYLEE_3217, NPCs.TINA_3218), IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, RisingSunInnBartenderDialogue(), node.id)
            return@on true
        }

        on(NPCs.ROLAD_1841, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, RoladDialogue(), node.id)
            return@on true
        }

        on(NPCs.RUSTY_3239, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, RustyDialogue(), node.id)
            return@on true
        }

        on(NPCs.SARAH_2304, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, SarahFarmingDialogue(), node.id)
            return@on true
        }

        on(NPCs.WAYNE_581, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, WayneDialogue(), node.id)
            return@on true
        }

        on(NPCs.WORKMAN_3236, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, FaladorWorkmanDialogue(), node.id)
            return@on true
        }

        on(NPCs.NARF_3238, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, NarfsDialogue(), node.id)
            return@on true
        }

        on(NPCs.CUFFS_3237, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, CuffsDialogue(), node)
            return@on true
        }

        on(NPCs.ZANDAR_HORFYRE_3308, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, ZandarHorfyreDialogue(), node)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {

        /*
         * Rising Sun Inn.
         *
         * Borders refer to the Emily bar counter area.
         *
         * When the player is outside of this area, the only possible side
         * for interaction remains the northern side point.
         */

        val northPoint = Location.create(2955, 3375, 0)

        setDest(IntType.NPC, NPCs.EMILY_736) { player, _ ->
            if(inBorders(player, 2953, 3368, 2956, 3374)) {
                player.location
            } else {
                northPoint
            }
        }
    }

    companion object {
        private const val CUPBOARD_CLOSED = Scenery.CUPBOARD_2271
        private const val CUPBOARD_OPEN = Scenery.CUPBOARD_2272
        private const val DOORS = Scenery.DOOR_11708
        private const val POSTER = Scenery.POSTER_40992
        private const val QUEST_ITEM = Items.PORTRAIT_666
        val CASTLE_STAIRS = intArrayOf(Scenery.STAIRCASE_11729, Scenery.STAIRCASE_11731)
    }
}

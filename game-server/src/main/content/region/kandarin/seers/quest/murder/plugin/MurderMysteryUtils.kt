package content.region.kandarin.seers.quest.murder.plugin

import core.api.getAttribute
import core.api.getRegionBorders
import core.api.setQuestStage
import core.api.setAttribute
import core.game.node.entity.player.Player
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Scenery

object MurderMysteryUtils {
    val ATTRIBUTE_ELIZABETH = "/save:murdermystery:elizabeth"
    val ATTRIBUTE_ANNA = "/save:murdermystery:anna"
    val ATTRIBUTE_DAVID = "/save:murdermystery:david"

    val MANSION_ROAD_ZONE = getRegionBorders(10807)

    val EVIDENCE_ITEMS =
        intArrayOf(
            Items.SILVER_NECKLACE_1796,
            Items.SILVER_CUP_1798,
            Items.SILVER_BOTTLE_1800,
            Items.SILVER_BOOK_1802,
            Items.SILVER_NEEDLE_1804,
            Items.SILVER_POT_1806,
            Items.PUNGENT_POT_1812,
            Items.CRIMINALS_DAGGER_1813,
        )
    val EVIDENCE_ITEMS_2 =
        intArrayOf(
            Items.SILVER_NECKLACE_1797,
            Items.SILVER_CUP_1799,
            Items.SILVER_BOTTLE_1801,
            Items.SILVER_BOOK_1803,
            Items.SILVER_NEEDLE_1805,
            Items.SILVER_POT_1807,
            Items.PUNGENT_POT_1812,
            Items.CRIMINALS_DAGGER_1814,
        )
    val GUILTY_NPC_PRINT_ITEMS =
        intArrayOf(
            Items.KILLERS_PRINT_1815,
            Items.ANNAS_PRINT_1816,
            Items.BOBS_PRINT_1817,
            Items.CAROLS_PRINT_1818,
            Items.DAVIDS_PRINT_1819,
            Items.ELIZABETHS_PRINT_1820,
            Items.FRANKS_PRINT_1821,
            Items.UNKNOWN_PRINT_1822,
        )
    val MANSION_OBJECTS =
        intArrayOf(
            Scenery.ANNA_S_BARREL_2656,
            Scenery.BOB_S_BARREL_2657,
            Scenery.CAROL_S_BARREL_2658,
            Scenery.DAVID_S_BARREL_2659,
            Scenery.ELIZABETH_S_BARREL_2660,
            Scenery.FRANK_S_BARREL_2661,
        )
    val CRIME_SCENE_OBJECTS =
        intArrayOf(
            Scenery.SINCLAIR_FAMILY_FOUNTAIN_2654,
            Scenery.SINCLAIR_MANSION_DRAIN_2843,
            Scenery.SINCLAIR_FAMILY_CREST_2655,
            Scenery.SPIDERS_NEST_26109,
            Scenery.SINCLAIR_FAMILY_BEEHIVE_26121,
            Scenery.SINCLAIR_FAMILY_COMPOST_HEAP_26120,
        )

    fun initialSuspects(player: Player) {
        if (player.inventory.containItems(Items.CRIMINALS_THREAD_1808)) {
            setAttribute(player, ATTRIBUTE_ELIZABETH, true)
        } else if (player.inventory.containItems(Items.CRIMINALS_THREAD_1809)) {
            setAttribute(player, ATTRIBUTE_ANNA, true)
        } else if (player.inventory.containItems(Items.CRIMINALS_THREAD_1810)) {
            setAttribute(player, ATTRIBUTE_DAVID, true)
        }
        setQuestStage(player, Quests.MURDER_MYSTERY, 2)
    }

    fun getGuiltyPerson(player: Player): String? =
        when {
            getAttribute(player, ATTRIBUTE_ELIZABETH, false) -> "Elizabeth"
            getAttribute(player, ATTRIBUTE_ANNA, false) -> "Anna"
            getAttribute(player, ATTRIBUTE_DAVID, false) -> "David"
            else -> null
        }

    fun getGuiltyColor(player: Player): String? =
        when {
            getAttribute(player, ATTRIBUTE_ELIZABETH, false) -> "red"
            getAttribute(player, ATTRIBUTE_ANNA, false) -> "green"
            getAttribute(player, ATTRIBUTE_DAVID, false) -> "blue"
            else -> null
        }
}

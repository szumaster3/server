package content.global.activity.creation

import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the creature creation combinations used in the Creature Creation activity.
 */
enum class CreatureCreation(
    val material: String,
    val npcId: Int,
    val location: Location,
    val firstMaterial: Int,
    val secondMaterial: Int,
) {
    /**
     * Creates Newtroost using a feather and an eye of newt at the north-east altar.
     */
    NORTH_EAST(
        "Feather of chicken and eye of newt",
        NPCs.NEWTROOST_5597,
        Location(3058, 4410, 0),
        Items.FEATHER_314,
        Items.EYE_OF_NEWT_221,
    ),

    /**
     * Creates Unicow using a unicorn horn and a cowhide at the north-west altar.
     */
    NORTH_WEST(
        "Horn of unicorn and hide of cow",
        NPCs.UNICOW_5603,
        Location(3018, 4410, 0),
        Items.COWHIDE_1739,
        Items.UNICORN_HORN_237,
    ),

    /**
     * Creates Spidine using red spiders' eggs and a raw sardine at the south-east altar.
     */
    SOUTH_EAST(
        "Red spiders' eggs and a sardine raw",
        NPCs.SPIDINE_5594,
        Location(3043, 4361, 0),
        Items.RED_SPIDERS_EGGS_223,
        Items.RAW_SARDINE_327,
    ),

    /**
     * Creates Swordchick using a raw swordfish and raw chicken at the south-west altar.
     */
    SOUTH_WEST(
        "Swordfish raw and chicken uncooked",
        NPCs.SWORDCHICK_5595,
        Location(3034, 4361, 0),
        Items.RAW_SWORDFISH_371,
        Items.RAW_CHICKEN_2138,
    ),

    /**
     * Creates Jubster using raw jubbly meat and a raw lobster at the east altar.
     */
    EAST(
        "Raw meat of jubbly bird and a lobster raw",
        NPCs.JUBSTER_5596,
        Location(3066, 4380, 0),
        Items.RAW_JUBBLY_7566,
        Items.RAW_LOBSTER_377,
    ),

    /**
     * Creates Frogeel using giant frog legs and a raw cave eel at the west altar.
     */
    WEST(
        "Legs of giant frog and a cave eel uncooked",
        NPCs.FROGEEL_5593,
        Location(3012, 4380, 0),
        Items.GIANT_FROG_LEGS_4517,
        Items.RAW_CAVE_EEL_5001,
    );

    /**
     * A convenience list of the two item IDs required to create the creature.
     */
    val materials: List<Int> = listOf(firstMaterial, secondMaterial)

    companion object {
        /**
         * Retrieves the [CreatureCreation] enum for the specified [location], if it exists.
         *
         * @param location The location to search for.
         * @return The matching [CreatureCreation] or null if none matches.
         */
        @JvmStatic
        fun forLocation(location: Location): CreatureCreation? =
            values().find { it.location == location }
    }
}

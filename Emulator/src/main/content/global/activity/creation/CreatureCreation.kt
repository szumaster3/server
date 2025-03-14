package content.global.activity.creation

import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.NPCs

enum class CreatureCreation(
    val material: String,
    val npcId: Int,
    val location: Location,
    val firstMaterial: Int,
    val secondMaterial: Int,
) {
    NORTH_EAST(
        "Feather of chicken and eye of newt",
        NPCs.NEWTROOST_5597,
        Location(3058, 4410, 0),
        Items.FEATHER_314,
        Items.EYE_OF_NEWT_221,
    ),
    NORTH_WEST(
        "Horn of unicorn and hide of cow",
        NPCs.UNICOW_5603,
        Location(3018, 4410, 0),
        Items.COWHIDE_1739,
        Items.UNICORN_HORN_237,
    ),
    SOUTH_EAST(
        "Red spiders' eggs and a sardine raw",
        NPCs.SPIDINE_5594,
        Location(3043, 4361, 0),
        Items.RED_SPIDERS_EGGS_223,
        Items.RAW_SARDINE_327,
    ),
    SOUTH_WEST(
        "Swordfish raw and chicken uncooked",
        NPCs.SWORDCHICK_5595,
        Location(3034, 4361, 0),
        Items.RAW_SWORDFISH_371,
        Items.RAW_CHICKEN_2138,
    ),
    EAST(
        "Raw meat of jubbly bird and a lobster raw",
        NPCs.JUBSTER_5596,
        Location(3066, 4380, 0),
        Items.RAW_JUBBLY_7566,
        Items.RAW_LOBSTER_377,
    ),
    WEST(
        "Legs of giant frog and a cave eel uncooked",
        NPCs.FROGEEL_5593,
        Location(3012, 4380, 0),
        Items.GIANT_FROG_LEGS_4517,
        Items.RAW_CAVE_EEL_5001,
    ),
    ;

    val materials: List<Int> = listOf(firstMaterial, secondMaterial)

    companion object {
        @JvmStatic
        fun forLocation(location: Location): CreatureCreation? {
            return values().find { it.location == location }
        }
    }
}

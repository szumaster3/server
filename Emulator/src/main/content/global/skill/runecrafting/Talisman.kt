package content.global.skill.runecrafting

import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents the various talismans used in Runecrafting.
 *
 * @property item The [Item] representing the talisman.
 * @property ruin The associated [MysteriousRuins] where this talisman can be used,
 * or `null` for special talismans that do not have a corresponding ruin.
 */
enum class Talisman(
    val item: Item,
    private val ruin: MysteriousRuins?,
) {
    AIR(item = Item(Items.AIR_TALISMAN_1438), ruin = MysteriousRuins.AIR),
    MIND(item = Item(Items.MIND_TALISMAN_1448), ruin = MysteriousRuins.MIND),
    WATER(item = Item(Items.WATER_TALISMAN_1444), ruin = MysteriousRuins.WATER),
    EARTH(item = Item(Items.EARTH_TALISMAN_1440), ruin = MysteriousRuins.EARTH),
    FIRE(item = Item(Items.FIRE_TALISMAN_1442), ruin = MysteriousRuins.FIRE),
    ELEMENTAL(item = Item(Items.ELEMENTAL_TALISMAN_5516), ruin = null),
    BODY(item = Item(Items.BODY_TALISMAN_1446), ruin = MysteriousRuins.BODY),
    COSMIC(item = Item(Items.COSMIC_TALISMAN_1454), ruin = MysteriousRuins.COSMIC),
    CHAOS(item = Item(Items.CHAOS_TALISMAN_1452), ruin = MysteriousRuins.CHAOS),
    NATURE(item = Item(Items.NATURE_TALISMAN_1462), ruin = MysteriousRuins.NATURE),
    LAW(item = Item(Items.LAW_TALISMAN_1458), ruin = MysteriousRuins.LAW),
    DEATH(item = Item(Items.DEATH_TALISMAN_1456), ruin = MysteriousRuins.DEATH),
    BLOOD(item = Item(Items.BLOOD_TALISMAN_1450), ruin = MysteriousRuins.BLOOD),
    SOUL(item = Item(Items.SOUL_TALISMAN_1460), ruin = null),
    ;

    /**
     * Locates the direction towards the associated mysterious ruins based on the player's current location.
     * Sends a message to the player indicating the general direction of the corresponding ruins.
     *
     * @param player The [Player] who is attempting to locate the ruin.
     */
    fun locate(player: Player) {
        if (this == ELEMENTAL || ruin == null) {
            player.packetDispatch.sendMessage("You cannot tell which direction the Talisman is pulling...")
            return
        }
        val loc = ruin.base
        val direction = when {
            player.location.y > loc.y && player.location.x - 1 > loc.x -> "south-west"
            player.location.x < loc.x && player.location.y > loc.y -> "south-east"
            player.location.x > loc.x + 1 && player.location.y < loc.y -> "north-west"
            player.location.x < loc.x && player.location.y < loc.y -> "north-east"
            player.location.y < loc.y -> "north"
            player.location.y > loc.y -> "south"
            player.location.x < loc.x + 1 -> "east"
            player.location.x > loc.x + 1 -> "west"
            else -> "unknown direction"
        }
        player.packetDispatch.sendMessage("The talisman pulls towards the $direction.")
    }

    /**
     * Retrieves the associated [MysteriousRuins] for this talisman, or `null` if there is none.
     *
     * @return The [MysteriousRuins] associated with this talisman, or `null`.
     */
    fun getRuin(): MysteriousRuins? = ruin

    /**
     * Gets the corresponding [Tiara] for this talisman, if one exists.
     *
     * The name of the talisman enum constant is matched against the names of the [Tiara] enum.
     */
    val tiara: Tiara?
        get() = Tiara.values().find { it.name == name }

    companion object {
        /**
         * Retrieves the [Talisman] for the given [Item].
         *
         * @param item The item to check.
         * @return The matching [Talisman], or `null` if no match is found.
         */
        @JvmStatic
        fun forItem(item: Item): Talisman? = values().find { it.item.id == item.id }

        /**
         * Retrieves the [Talisman] for the given talisman name.
         *
         * @param name The name of the talisman to find.
         * @return The matching [Talisman], or `null` if no match is found.
         */
        @JvmStatic
        fun forName(name: String): Talisman? = values().find { it.name == name }
    }
}

package content.global.skill.runecrafting

import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents the various talismans used in Runecrafting.
 */
enum class Talisman(val item: Item, private val ruin: MysteriousRuins?) {
    AIR(Item(Items.AIR_TALISMAN_1438), MysteriousRuins.AIR),
    MIND(Item(Items.MIND_TALISMAN_1448), MysteriousRuins.MIND),
    WATER(Item(Items.WATER_TALISMAN_1444), MysteriousRuins.WATER),
    EARTH(Item(Items.EARTH_TALISMAN_1440), MysteriousRuins.EARTH),
    FIRE(Item(Items.FIRE_TALISMAN_1442), MysteriousRuins.FIRE),
    ELEMENTAL(Item(Items.ELEMENTAL_TALISMAN_5516), null),
    BODY(Item(Items.BODY_TALISMAN_1446), MysteriousRuins.BODY),
    COSMIC(Item(Items.COSMIC_TALISMAN_1454), MysteriousRuins.COSMIC),
    CHAOS(Item(Items.CHAOS_TALISMAN_1452), MysteriousRuins.CHAOS),
    NATURE(Item(Items.NATURE_TALISMAN_1462), MysteriousRuins.NATURE),
    LAW(Item(Items.LAW_TALISMAN_1458), MysteriousRuins.LAW),
    DEATH(Item(Items.DEATH_TALISMAN_1456), MysteriousRuins.DEATH),
    BLOOD(Item(Items.BLOOD_TALISMAN_1450), MysteriousRuins.BLOOD),
    SOUL(Item(Items.SOUL_TALISMAN_1460), null),
    ;

    /**
     * Sends the player a message indicating the direction of the associated ruins.
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
     * Returns the associated [MysteriousRuins], or null if none.
     */
    fun getRuin(): MysteriousRuins? = ruin

    /**
     * Gets the matching [Tiara] by enum name, if any.
     */
    val tiara: Tiara?
        get() = Tiara.values().find { it.name == name }

    companion object {
        private val itemMap = values().associateBy { it.item.id }
        private val nameMap = values().associateBy { it.name }

        /**
         * Finds a [Talisman] by item, or null if not found.
         */
        @JvmStatic
        fun forItem(item: Item): Talisman? = itemMap[item.id]

        /**
         * Finds a [Talisman] by name, or null if not found.
         */
        @JvmStatic
        fun forName(name: String): Talisman? = nameMap[name]
    }
}

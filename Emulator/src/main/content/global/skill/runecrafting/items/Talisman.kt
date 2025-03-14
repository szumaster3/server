package content.global.skill.runecrafting.items

import content.global.skill.runecrafting.scenery.MysteriousRuins
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

enum class Talisman(
    val item: Item,
    private val ruin: MysteriousRuins?,
) {
    AIR(
        item = Item(Items.AIR_TALISMAN_1438),
        ruin = MysteriousRuins.AIR,
    ),
    MIND(
        item = Item(Items.MIND_TALISMAN_1448),
        ruin = MysteriousRuins.MIND,
    ),
    WATER(
        item = Item(Items.WATER_TALISMAN_1444),
        ruin = MysteriousRuins.WATER,
    ),
    EARTH(
        item = Item(Items.EARTH_TALISMAN_1440),
        ruin = MysteriousRuins.EARTH,
    ),
    FIRE(
        item = Item(Items.FIRE_TALISMAN_1442),
        ruin = MysteriousRuins.FIRE,
    ),
    ELEMENTAL(
        item = Item(Items.ELEMENTAL_TALISMAN_5516),
        ruin = null,
    ),
    BODY(
        item = Item(Items.BODY_TALISMAN_1446),
        ruin = MysteriousRuins.BODY,
    ),
    COSMIC(
        item = Item(Items.COSMIC_TALISMAN_1454),
        ruin = MysteriousRuins.COSMIC,
    ),
    CHAOS(
        item = Item(Items.CHAOS_TALISMAN_1452),
        ruin = MysteriousRuins.CHAOS,
    ),
    NATURE(
        item = Item(Items.NATURE_TALISMAN_1462),
        ruin = MysteriousRuins.NATURE,
    ),
    LAW(
        item = Item(Items.LAW_TALISMAN_1458),
        ruin = MysteriousRuins.LAW,
    ),
    DEATH(
        item = Item(Items.DEATH_TALISMAN_1456),
        ruin = MysteriousRuins.DEATH,
    ),
    BLOOD(
        item = Item(Items.BLOOD_TALISMAN_1450),
        ruin = MysteriousRuins.BLOOD,
    ),
    SOUL(
        item = Item(Items.SOUL_TALISMAN_1460),
        ruin = null,
    ),
    ;

    fun locate(player: Player) {
        if (this == ELEMENTAL || ruin == null) {
            player.packetDispatch.sendMessage("You cannot tell which direction the Talisman is pulling...")
            return
        }
        val loc = ruin.base
        val direction =
            when {
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

    fun getRuin(): MysteriousRuins? {
        return ruin
    }

    val tiara: Tiara?
        get() = Tiara.values().find { it.name == name }

    companion object {
        @JvmStatic
        fun forItem(item: Item): Talisman? {
            return values().find { it.item.id == item.id }
        }

        @JvmStatic
        fun forName(name: String): Talisman? {
            return values().find { it.name == name }
        }
    }
}

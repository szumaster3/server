package content.data

import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Components
import org.rs.consts.Items

enum class LightSource(
    val level: Int,
    val raw: Item,
    val product: Item,
    val open: Boolean,
    val interfaceId: Int,
) {
    CANDLE(
        level = 1,
        raw = Item(Items.CANDLE_36, 1),
        product = Item(Items.LIT_CANDLE_33, 1),
        open = true,
        interfaceId = Components.DARKNESS_MEDIUM_98,
    ),

    BLACK_CANDLE(
        level = 1,
        raw = Item(Items.BLACK_CANDLE_38, 1),
        product = Item(Items.LIT_BLACK_CANDLE_32, 1),
        open = true,
        interfaceId = Components.DARKNESS_MEDIUM_98,
    ),

    TORCH(
        level = 1,
        raw = Item(Items.UNLIT_TORCH_596, 1),
        product = Item(Items.LIT_TORCH_594, 1),
        open = true,
        interfaceId = Components.DARKNESS_MEDIUM_98,
    ),

    WHITE_CANDLE_LANTERN(
        level = 4,
        raw = Item(Items.CANDLE_LANTERN_4527, 1),
        product = Item(Items.CANDLE_LANTERN_4531, 1),
        open = false,
        interfaceId = Components.DARKNESS_MEDIUM_98,
    ),

    BLACK_CANDLE_LANTERN(
        level = 4,
        raw = Item(Items.CANDLE_LANTERN_4527, 1),
        product = Item(Items.CANDLE_LANTERN_4532, 1),
        open = false,
        interfaceId = Components.DARKNESS_MEDIUM_98,
    ),

    OIL_LAMP(
        level = 12,
        raw = Item(Items.OIL_LAMP_4522, 1),
        product = Item(Items.OIL_LAMP_4524, 1),
        open = true,
        interfaceId = Components.DARKNESS_LIGHT_97,
    ),

    OIL_LANTERN(
        level = 26,
        raw = Item(Items.OIL_LANTERN_4535, 1),
        product = Item(Items.OIL_LANTERN_4539, 1),
        open = false,
        interfaceId = Components.DARKNESS_LIGHT_97,
    ),

    BUG_LANTERN(
        level = 33,
        raw = Item(Items.UNLIT_BUG_LANTERN_7051, 1),
        product = Item(Items.LIT_BUG_LANTERN_7053, 1),
        open = false,
        interfaceId = -1,
    ),

    BULLSEYE_LANTERN(
        level = 49,
        raw = Item(Items.BULLSEYE_LANTERN_4548, 1),
        product = Item(Items.BULLSEYE_LANTERN_4550, 1),
        open = false,
        interfaceId = -1,
    ),

    SAPPHIRE_LANTERN(
        level = 49,
        raw = Item(Items.SAPPHIRE_LANTERN_4701, 1),
        product = Item(Items.SAPPHIRE_LANTERN_4702, 1),
        open = false,
        interfaceId = -1,
    ),

    EMERALD_LANTERN(
        level = 49,
        raw = Item(Items.EMERALD_LANTERN_9064, 1),
        product = Item(Items.EMERALD_LANTERN_9065, 1),
        open = false,
        interfaceId = -1,
    ),

    MINING_HELMET(
        level = 65,
        raw = Item(Items.MINING_HELMET_5014, 1),
        product = Item(Items.MINING_HELMET_5013, 1),
        open = false,
        interfaceId = Components.DARKNESS_LIGHT_97,
    ),

    SEERS_HEADBAND(
        level = 0,
        raw = Item(Items.SEERS_HEADBAND_1_14631, 1),
        product = Item(Items.SEERS_HEADBAND_1_14631, 1),
        open = false,
        interfaceId = -1,
    ),
    ;

    fun getStrength(): Int {
        return when (interfaceId) {
            Components.DARKNESS_LIGHT_97 -> 1
            Components.DARKNESS_MEDIUM_98 -> 2
            -1 -> 3
            else -> 0
        }
    }

    fun getName(): String {
        return name.lowercase().replace("_", " ")
    }

    companion object {
        @JvmStatic
        fun hasActiveLightSource(player: Player): Boolean {
            return getActiveLightSource(player) != null
        }

        @JvmStatic
        fun getActiveLightSource(player: Player): LightSource? {
            for (item in player.inventory.toArray()) {
                item?.let { it -> forProductId(it.id)?.let { return it } }
            }
            for (item in player.equipment.toArray()) {
                item?.let { it -> forProductId(it.id)?.let { return it } }
            }
            return null
        }

        @JvmStatic
        fun forId(id: Int): LightSource? {
            return values().firstOrNull { it.raw.id == id }
        }

        @JvmStatic
        fun forProductId(id: Int): LightSource? {
            return values().firstOrNull { it.product.id == id }
        }

        @JvmStatic
        fun getRawIds(): IntArray {
            return values().map { it.raw.id }.toIntArray()
        }
    }
}

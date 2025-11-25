package content.global.skill.crafting.items.lamps

import shared.consts.Items
import shared.consts.Sounds

import core.api.inEquipment
import core.api.inInventory
import core.game.node.entity.player.Player
import shared.consts.Components

/**
 * Represents light sources.
 */
enum class LightSources(val level: Int, val emptyId: Int, val fullId: Int = -1, val litId: Int, val sfxId: Int = -1, val open: Boolean = false, val interfaceId: Int = -1) {
    // Brightness level 1 (dim)
    CANDLE(1, 0, Items.CANDLE_36, Items.LIT_CANDLE_33, Sounds.SKILL_LIGHT_CANDLE_3226, true, Components.DARKNESS_MEDIUM_98),
    BLACK_CANDLE(1, 0, Items.BLACK_CANDLE_38, Items.LIT_BLACK_CANDLE_32, Sounds.SKILL_LIGHT_CANDLE_3226, true, Components.DARKNESS_MEDIUM_98),
    TORCH(1, 0, Items.UNLIT_TORCH_596, Items.LIT_TORCH_594, Sounds.SLUG_TORCH_LIT_3028, true, Components.DARKNESS_MEDIUM_98),
    CANDLE_LANTERN(4, Items.CANDLE_LANTERN_4527, Items.CANDLE_LANTERN_4529, Items.CANDLE_LANTERN_4531, Sounds.LIGHT_CANDLE_2305, true, Components.DARKNESS_MEDIUM_98),
    CANDLE_LANTERN_BLACK(4, Items.CANDLE_LANTERN_4527, Items.CANDLE_LANTERN_4532, Items.CANDLE_LANTERN_4534, Sounds.LIGHT_CANDLE_2305, true, Components.DARKNESS_MEDIUM_98),

    // Brightness level 2 (medium)
    OIL_LAMP(12, Items.OIL_LAMP_4525, Items.OIL_LAMP_4522, Items.OIL_LAMP_4524, Sounds.LIGHT_CANDLE_2305, true, Components.DARKNESS_LIGHT_97),
    OIL_LANTERN(26, Items.OIL_LANTERN_4535, Items.OIL_LANTERN_4537, Items.OIL_LANTERN_4539, Sounds.LIGHT_CANDLE_2305, false, Components.DARKNESS_LIGHT_97),
    SAPPHIRE_LANTERN(49, Items.SAPPHIRE_LANTERN_4700, Items.SAPPHIRE_LANTERN_4701, Items.SAPPHIRE_LANTERN_4702, Sounds.LIGHT_CANDLE_2305, false, -1),
    MINING_HELMET(65, 0, Items.MINING_HELMET_5014, Items.MINING_HELMET_5013, Sounds.LIGHT_CANDLE_2305, false, Components.DARKNESS_LIGHT_97),

    // Brightness level 3 (bright)
    BULLSEYE_LANTERN(49, Items.BULLSEYE_LANTERN_4546, Items.BULLSEYE_LANTERN_4548, Items.BULLSEYE_LANTERN_4550, Sounds.LIGHT_CANDLE_2305, false, -1),
    EMERALD_LANTERN(49, Items.EMERALD_LANTERN_9064, Items.EMERALD_LANTERN_9064, Items.EMERALD_LANTERN_9065, Sounds.LIGHT_CANDLE_2305, false, -1),

    // Permanent light sources.
    HEADBAND_1(1, -1, -1, Items.SEERS_HEADBAND_1_14631, -1, false, Components.DARKNESS_MEDIUM_98),
    HEADBAND_2(1, -1, -1, Items.SEERS_HEADBAND_2_14640, -1, false, Components.DARKNESS_LIGHT_97),
    HEADBAND_3(1, -1, -1, Items.SEERS_HEADBAND_3_14641, -1, false, -1),
    GLOWING_FUNGUS(1, -1, -1, Items.GLOWING_FUNGUS_4075, -1, false, -1);

    /**
     * Light intensity used for darkness overlay: 1 = dim, 2 = medium, 3 = bright.
     */
    val strength: Int
        get() = when (interfaceId) {
            Components.DARKNESS_LIGHT_97 -> 1
            Components.DARKNESS_MEDIUM_98 -> 2
            -1 -> 3
            else -> Components.DARKNESS_DARK_96
        }

    fun isPermanent(): Boolean = this in listOf(HEADBAND_1, HEADBAND_2, HEADBAND_3, GLOWING_FUNGUS)

    companion object {
        private val byRaw: Map<Int, LightSources> = values().flatMap { listOf(it.emptyId, it.fullId).filter { id -> id > 0 }.map { id -> id to it } }.toMap()
        private val byLit: Map<Int, LightSources> = values().associateBy { it.litId }

        @JvmStatic
        fun forId(id: Int): LightSources? = byRaw[id]

        @JvmStatic
        fun forLitId(id: Int): LightSources? = byLit[id]

        @JvmStatic
        fun hasActiveLightSource(player: Player): Boolean = getActiveLightSource(player) != null

        @JvmStatic
        fun getAnyActiveLightSource(player: Player): LightSources? =
            values().firstOrNull { inInventory(player, it.litId) || inEquipment(player, it.litId) }

        @JvmStatic
        fun getActiveLightSource(player: Player): LightSources? =
            values().firstOrNull { !it.isPermanent() && (inInventory(player, it.litId) || inEquipment(player, it.litId)) }
    }


}

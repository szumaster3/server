package content.global.skill.crafting.glassblowing.lamps

import org.rs.consts.Items
import org.rs.consts.Sounds

enum class LightSources(val emptyId: Int, val fullId: Int, val litId: Int, val sfxId: Int, val levelRequired: Int, val openFlame: Boolean, ) {
    WHITE_CANDLE(0, Items.CANDLE_36, Items.LIT_CANDLE_33, Sounds.SKILL_LIGHT_CANDLE_3226, 0, true),
    BLACK_CANDLE(0, Items.BLACK_CANDLE_38, Items.LIT_BLACK_CANDLE_32, Sounds.SKILL_LIGHT_CANDLE_3226, 0, true),
    TORCH(0, Items.UNLIT_TORCH_596, Items.LIT_TORCH_594, Sounds.SLUG_TORCH_LIT_3028, 0, true),
    WHITE_CANDLE_LANTERN(Items.CANDLE_LANTERN_4527, Items.CANDLE_LANTERN_4529, Items.CANDLE_LANTERN_4531, Sounds.LIGHT_CANDLE_2305, 4, true),
    BLACK_CANDLE_LANTERN(Items.CANDLE_LANTERN_4527, Items.CANDLE_LANTERN_4532, Items.CANDLE_LANTERN_4534, Sounds.LIGHT_CANDLE_2305, 4, true),
    OIL_LAMP(Items.OIL_LAMP_4525, Items.OIL_LAMP_4522, Items.OIL_LAMP_4524, Sounds.LIGHT_CANDLE_2305, 12, true),
    BUG_LANTERN(Items.UNLIT_BUG_LANTERN_7051, Items.UNLIT_BUG_LANTERN_7051, Items.LIT_BUG_LANTERN_7053, Sounds.LIGHT_CANDLE_2305, 33, false),
    OIL_LANTERN(Items.OIL_LANTERN_4535, Items.OIL_LANTERN_4537, Items.OIL_LANTERN_4539, Sounds.LIGHT_CANDLE_2305, 26, false),
    BULLSEYE_LANTERN(Items.BULLSEYE_LANTERN_4546, Items.BULLSEYE_LANTERN_4548, Items.BULLSEYE_LANTERN_4550, Sounds.LIGHT_CANDLE_2305, 49, false),
    SAPPHIRE_LANTERN(0, Items.SAPPHIRE_LANTERN_4701, Items.SAPPHIRE_LANTERN_4702, Sounds.LIGHT_CANDLE_2305, 49, false),
    MINING_HELMET(0, Items.MINING_HELMET_5014, Items.MINING_HELMET_5013, Sounds.LIGHT_CANDLE_2305, 65, false),
    EMERALD_LANTERN(0, Items.EMERALD_LANTERN_9064, Items.EMERALD_LANTERN_9065, Sounds.LIGHT_CANDLE_2305, 49, false),
    ;

    companion object {
        fun forId(id: Int): LightSources? =
            when (id) {
                36, 33 -> WHITE_CANDLE
                38, 32 -> BLACK_CANDLE
                596, 594 -> TORCH
                4529, 4531 -> WHITE_CANDLE_LANTERN
                4532, 4534 -> BLACK_CANDLE_LANTERN
                4522, 4524 -> OIL_LAMP
                4537, 4539 -> OIL_LANTERN
                4548, 4550 -> BULLSEYE_LANTERN
                4701, 4702 -> SAPPHIRE_LANTERN
                5014, 5013 -> MINING_HELMET
                7051, 7053 -> BUG_LANTERN
                9064, 9065 -> EMERALD_LANTERN
                else -> null
            }
    }
}

package content.global.skill.crafting.jewellery

import shared.consts.Items

/**
 * Represents the silver products.
 */
enum class Silver(val buttonId: Int, val required: Int, val product: Int, val amount: Int, val level: Int, val experience: Double, val strung: Int) {
    HOLY(16, Items.HOLY_MOULD_1599, Items.UNSTRUNG_SYMBOL_1714, 1, 16, 50.0, Items.UNBLESSED_SYMBOL_1716),
    UNHOLY(23, Items.UNHOLY_MOULD_1594, Items.UNSTRUNG_EMBLEM_1720, 1, 17, 50.0, Items.UNHOLY_SYMBOL_1724),
    SICKLE(30, Items.SICKLE_MOULD_2976, Items.SILVER_SICKLE_2961, 1, 18, 50.0, -1),
    TIARA(44, Items.TIARA_MOULD_5523, Items.TIARA_5525, 1, 23, 52.5, -1),
    SILVTHRIL_CHAIN(59, Items.CHAIN_LINK_MOULD_13153, Items.SILVTHRIL_CHAIN_13154, 1, 47, 100.0, -1),
    LIGHTNING_ROD(73, Items.CONDUCTOR_MOULD_4200, Items.CONDUCTOR_4201, 1, 20, 50.0, -1),
    SILVTHRILL_ROD(37, Items.ROD_CLAY_MOULD_7649, Items.SILVTHRILL_ROD_7637, 1, 25, 55.0, -1),
    CROSSBOW_BOLTS(52, Items.BOLT_MOULD_9434, Items.SILVER_BOLTS_UNF_9382, 10, 21, 50.0, -1),
    DEMONIC_SIGIL(66, Items.DEMONIC_SIGIL_MOULD_6747, Items.DEMONIC_SIGIL_6748, 1, 30, 50.0, -1), ;

    companion object {
        fun forId(itemId: Int): Silver? = values().find { it.required == itemId }
        fun forButton(button: Int): Silver? = values().find { it.buttonId == button }
    }
}

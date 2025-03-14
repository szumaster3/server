package content.global.skill.crafting.casting.silver

import org.rs.consts.Items

private const val BUTTON_UNBLESSED = 16
private const val BUTTON_UNHOLY = 23
private const val BUTTON_SICKLE = 30
private const val BUTTON_TIARA = 44
private const val BUTTON_DEMONIC_SIGIL = 59
private const val BUTTON_SILVTHRIL_CHAIN = 73
private const val BUTTON_LIGHTNING_ROD = 37
private const val BUTTON_SILVTHRILL_ROD = 52
private const val BUTTON_CROSSBOW_BOLTS = 66

enum class Silver(
    val buttonId: Int,
    val required: Int,
    val product: Int,
    val amount: Int,
    val level: Int,
    val experience: Double,
    val strung: Int,
) {
    HOLY(
        buttonId = BUTTON_UNBLESSED,
        required = Items.HOLY_MOULD_1599,
        product = Items.UNSTRUNG_SYMBOL_1714,
        amount = 1,
        level = 16,
        experience = 50.0,
        strung = Items.UNBLESSED_SYMBOL_1716,
    ),
    UNHOLY(
        buttonId = BUTTON_UNHOLY,
        required = Items.UNHOLY_MOULD_1594,
        product = Items.UNSTRUNG_EMBLEM_1720,
        amount = 1,
        level = 17,
        experience = 50.0,
        strung = Items.UNHOLY_SYMBOL_1724,
    ),
    SICKLE(
        buttonId = BUTTON_SICKLE,
        required = Items.SICKLE_MOULD_2976,
        product = Items.SILVER_SICKLE_2961,
        amount = 1,
        level = 18,
        experience = 50.0,
        strung = -1,
    ),
    TIARA(
        buttonId = BUTTON_TIARA,
        required = Items.TIARA_MOULD_5523,
        product = Items.TIARA_5525,
        amount = 1,
        level = 23,
        experience = 52.5,
        strung = -1,
    ),
    SILVTHRIL_CHAIN(
        buttonId = BUTTON_SILVTHRIL_CHAIN,
        required = Items.CHAIN_LINK_MOULD_13153,
        product = Items.SILVTHRIL_CHAIN_13154,
        amount = 1,
        level = 47,
        experience = 100.0,
        strung = -1,
    ),
    LIGHTNING_ROD(
        buttonId = BUTTON_LIGHTNING_ROD,
        required = Items.CONDUCTOR_MOULD_4200,
        product = Items.CONDUCTOR_4201,
        amount = 1,
        level = 20,
        experience = 50.0,
        strung = -1,
    ),
    SILVTHRILL_ROD(
        buttonId = BUTTON_SILVTHRILL_ROD,
        required = Items.ROD_CLAY_MOULD_7649,
        product = Items.SILVTHRILL_ROD_7637,
        amount = 1,
        level = 25,
        experience = 55.0,
        strung = -1,
    ),
    CROSSBOW_BOLTS(
        buttonId = BUTTON_CROSSBOW_BOLTS,
        required = Items.BOLT_MOULD_9434,
        product = Items.SILVER_BOLTS_UNF_9382,
        amount = 10,
        level = 21,
        experience = 50.0,
        strung = -1,
    ),
    DEMONIC_SIGIL(
        buttonId = BUTTON_DEMONIC_SIGIL,
        required = Items.DEMONIC_SIGIL_MOULD_6747,
        product = Items.DEMONIC_SIGIL_6748,
        amount = 1,
        level = 30,
        experience = 50.0,
        strung = -1,
    ),
    ;

    companion object {
        @JvmStatic
        fun forId(itemId: Int): Silver? = values().find { it.required == itemId }

        @JvmStatic
        fun forButton(button: Int): Silver? = values().find { it.buttonId == button }
    }
}

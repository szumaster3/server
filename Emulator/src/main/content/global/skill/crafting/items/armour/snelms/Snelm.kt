package content.global.skill.crafting.items.armour.snelms

import org.rs.consts.Items

enum class Snelm(
    val shell: Int,
    val product: Int,
) {
    MYRE_ROUNDED(
        shell = Items.BLAMISH_MYRE_SHELL_3345,
        product = Items.MYRE_SNELM_3327,
    ),
    MYRE_POINTED(
        shell = Items.BLAMISH_MYRE_SHELL_3355,
        product = Items.MYRE_SNELM_3337,
    ),
    BLOOD_ROUNDED(
        shell = Items.BLAMISH_RED_SHELL_3347,
        product = Items.BLOODNTAR_SNELM_3329,
    ),
    BLOOD_POINTED(
        shell = Items.BLAMISH_RED_SHELL_3357,
        product = Items.BLOODNTAR_SNELM_3339,
    ),
    OCHRE_ROUNDED(
        shell = Items.BLAMISH_OCHRE_SHELL_3349,
        product = Items.OCHRE_SNELM_3331,
    ),
    OCHRE_POINTED(
        shell = Items.BLAMISH_OCHRE_SHELL_3359,
        product = Items.OCHRE_SNELM_3341,
    ),
    BRUISE_ROUNDED(
        shell = Items.BLAMISH_BLUE_SHELL_3351,
        product = Items.BRUISE_BLUE_SNELM_3333,
    ),
    BRUISE_POINTED(
        shell = Items.BLAMISH_BLUE_SHELL_3361,
        product = Items.BRUISE_BLUE_SNELM_3343,
    ),
    BROKEN_ROUNDED(
        shell = Items.BLAMISH_BARK_SHELL_3353,
        product = Items.BROKEN_BARK_SNELM_3335,
    ),
    ;

    companion object {
        @JvmStatic
        private val shellToSnelmMap: Map<Int, Snelm> = values().associateBy { it.shell }

        @JvmStatic
        fun fromShellId(shellId: Int): Snelm? {
            return shellToSnelmMap[shellId]
        }

        @JvmStatic
        fun getProductByShellId(shellId: Int): Int? {
            return fromShellId(shellId)?.product
        }
    }
}

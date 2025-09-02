package content.global.skill.crafting.items.armour

import shared.consts.Items

enum class Snelms(val shell: Int, val product: Int) {
    MYRE_ROUNDED(Items.BLAMISH_MYRE_SHELL_3345, Items.MYRE_SNELM_3327),
    MYRE_POINTED(Items.BLAMISH_MYRE_SHELL_3355, Items.MYRE_SNELM_3337),
    BLOOD_ROUNDED(Items.BLAMISH_RED_SHELL_3347, Items.BLOODNTAR_SNELM_3329),
    BLOOD_POINTED(Items.BLAMISH_RED_SHELL_3357, Items.BLOODNTAR_SNELM_3339),
    OCHRE_ROUNDED(Items.BLAMISH_OCHRE_SHELL_3349, Items.OCHRE_SNELM_3331),
    OCHRE_POINTED(Items.BLAMISH_OCHRE_SHELL_3359, Items.OCHRE_SNELM_3341),
    BRUISE_ROUNDED(Items.BLAMISH_BLUE_SHELL_3351, Items.BRUISE_BLUE_SNELM_3333),
    BRUISE_POINTED(Items.BLAMISH_BLUE_SHELL_3361, Items.BRUISE_BLUE_SNELM_3343),
    BROKEN_ROUNDED(Items.BLAMISH_BARK_SHELL_3353, Items.BROKEN_BARK_SNELM_3335);

    companion object {
        private val shellMap = values().associateBy { it.shell }
        val SHELLS = values().map { it.shell }.toIntArray()
        fun fromShellId(id: Int): Snelms? = shellMap[id]
    }
}
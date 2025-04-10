package content.global.skill.crafting.items.armour.snelms

import org.rs.consts.Items

/**
 * Represetns different types of Snelms..
 */
enum class Snelm(
    val shell: Int,
    val product: Int,
) {
    /**
     * Crafting a Myre Rounded Snelm using a Blamish Myre Shell.
     */
    MYRE_ROUNDED(
        shell = Items.BLAMISH_MYRE_SHELL_3345,
        product = Items.MYRE_SNELM_3327,
    ),

    /**
     * Crafting a Myre Pointed Snelm using a Blamish Myre Shell.
     */
    MYRE_POINTED(
        shell = Items.BLAMISH_MYRE_SHELL_3355,
        product = Items.MYRE_SNELM_3337,
    ),

    /**
     * Crafting a Blood Rounded Snelm using a Blamish Red Shell.
     */
    BLOOD_ROUNDED(
        shell = Items.BLAMISH_RED_SHELL_3347,
        product = Items.BLOODNTAR_SNELM_3329,
    ),

    /**
     * Crafting a Blood Pointed Snelm using a Blamish Red Shell.
     */
    BLOOD_POINTED(
        shell = Items.BLAMISH_RED_SHELL_3357,
        product = Items.BLOODNTAR_SNELM_3339,
    ),

    /**
     * Crafting an Ochre Rounded Snelm using a Blamish Ochre Shell.
     */
    OCHRE_ROUNDED(
        shell = Items.BLAMISH_OCHRE_SHELL_3349,
        product = Items.OCHRE_SNELM_3331,
    ),

    /**
     * Crafting an Ochre Pointed Snelm using a Blamish Ochre Shell.
     */
    OCHRE_POINTED(
        shell = Items.BLAMISH_OCHRE_SHELL_3359,
        product = Items.OCHRE_SNELM_3341,
    ),

    /**
     * Crafting a Bruise Rounded Snelm using a Blamish Blue Shell.
     */
    BRUISE_ROUNDED(
        shell = Items.BLAMISH_BLUE_SHELL_3351,
        product = Items.BRUISE_BLUE_SNELM_3333,
    ),

    /**
     * Crafting a Bruise Pointed Snelm using a Blamish Blue Shell.
     */
    BRUISE_POINTED(
        shell = Items.BLAMISH_BLUE_SHELL_3361,
        product = Items.BRUISE_BLUE_SNELM_3343,
    ),

    /**
     * Crafting a Broken Rounded Snelm using a Blamish Bark Shell.
     */
    BROKEN_ROUNDED(
        shell = Items.BLAMISH_BARK_SHELL_3353,
        product = Items.BROKEN_BARK_SNELM_3335,
    ),
    ;

    companion object {
        /**
         * A map of shell item ids to corresponding [Snelm] enum entries.
         */
        @JvmStatic
        private val shellToSnelmMap: Map<Int, Snelm> = values().associateBy { it.shell }

        /**
         * Returns the [Snelm] enum entry corresponding to the given shell item id.
         *
         * @param shellId The id of the shell.
         * @return The corresponding [Snelm] enum entry, or `null` if no match is found.
         */
        @JvmStatic
        fun fromShellId(shellId: Int): Snelm? = shellToSnelmMap[shellId]

        /**
         * Returns the product id corresponding to the given shell item id.
         *
         * @param shellId The id of the shell.
         * @return The product id of the corresponding [Snelm], or `null` if no match is found.
         */
        @JvmStatic
        fun getProductByShellId(shellId: Int): Int? = fromShellId(shellId)?.product
    }
}

package content.global.skill.cooking.recipes

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

/**
 * Handles nettle tea related cooking recipes.
 */
class NettleTeaRecipePlugin : InteractionListener {

    override fun defineListeners() {

        val recipes = arrayOf(
            Recipe(NETTLES, BOWL_OF_WATER, NETTLE_WATER),
            Recipe(BUCKET_OF_MILK, BOWL_OF_NETTLE_TEA, BOWL_OF_NETTLE_TEA_MILKY, EMPTY_BUCKET),
            Recipe(BUCKET_OF_MILK, PORCELAIN_CUP_OF_NETTLE_TEA, PORCELAIN_CUP_OF_NETTLE_TEA_MILKY, EMPTY_BUCKET),
            Recipe(BOWL_OF_NETTLE_TEA, EMPTY_CUP, CUP_OF_NETTLE_TEA, EMPTY_BOWL),
            Recipe(BOWL_OF_NETTLE_TEA_MILKY, EMPTY_CUP, CUP_OF_NETTLE_TEA_MILKY, EMPTY_BOWL),
            Recipe(BOWL_OF_NETTLE_TEA, EMPTY_PORCELAIN_CUP, PORCELAIN_CUP_OF_NETTLE_TEA, EMPTY_BOWL),
            Recipe(BOWL_OF_NETTLE_TEA_MILKY, EMPTY_PORCELAIN_CUP, PORCELAIN_CUP_OF_NETTLE_TEA_MILKY, EMPTY_BOWL)
        )

        recipes.forEach { recipe ->
            onUseWith(IntType.ITEM, recipe.itemUsed, recipe.itemWith) { player, used, with ->
                if (removeItem(player, used.asItem(), Container.INVENTORY) &&
                    removeItem(player, with.asItem(), Container.INVENTORY)) {
                    recipe.returnItem?.let { addItemOrDrop(player, it, 1) }
                    addItem(player, recipe.product, 1)
                    sendMessage(player, "You combine the items.")
                }
                return@onUseWith true
            }
        }
    }

    private data class Recipe(
        val itemUsed: Int,
        val itemWith: Int,
        val product: Int,
        val returnItem: Int? = null
    )

    companion object {
        private const val EMPTY_BUCKET = Items.BUCKET_1925
        private const val BUCKET_OF_MILK = Items.BUCKET_OF_MILK_1927

        private const val NETTLES = Items.NETTLES_4241
        private const val NETTLE_WATER = Items.NETTLE_WATER_4237
        private const val BOWL_OF_WATER = Items.BOWL_OF_WATER_1921

        private const val EMPTY_BOWL = Items.BOWL_1923
        private const val BOWL_OF_NETTLE_TEA = Items.NETTLE_TEA_4239
        private const val BOWL_OF_NETTLE_TEA_MILKY = Items.NETTLE_TEA_4240

        private const val EMPTY_CUP = Items.EMPTY_CUP_1980
        private const val CUP_OF_NETTLE_TEA = Items.CUP_OF_TEA_4242
        private const val CUP_OF_NETTLE_TEA_MILKY = Items.CUP_OF_TEA_4243

        private const val EMPTY_PORCELAIN_CUP = Items.PORCELAIN_CUP_4244
        private const val PORCELAIN_CUP_OF_NETTLE_TEA = Items.CUP_OF_TEA_4245
        private const val PORCELAIN_CUP_OF_NETTLE_TEA_MILKY = Items.CUP_OF_TEA_4246
    }
}
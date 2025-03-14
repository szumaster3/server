package content.minigame.gnomecook.handlers

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Items

class GnomeCocktailInterface : InterfaceListener {

    private val cookingGnomeCocktailsInterface = 436

    override fun defineInterfaceListeners() {
        onOpen(cookingGnomeCocktailsInterface) { player, component ->
            sendItemOnInterface(player, component.id, 3, Items.WIZARD_BLIZZARD_2054, 1)
            sendItemOnInterface(player, component.id, 16, Items.SHORT_GREEN_GUY_2080, 1)
            sendItemOnInterface(player, component.id, 23, Items.FRUIT_BLAST_2084, 1)
            sendItemOnInterface(player, component.id, 32, Items.PINEAPPLE_PUNCH_2048, 1)
            sendItemOnInterface(player, component.id, 41, Items.DRUNK_DRAGON_2092, 1)
            sendItemOnInterface(player, component.id, 50, Items.CHOC_SATURDAY_2074, 1)
            sendItemOnInterface(player, component.id, 61, Items.BLURBERRY_SPECIAL_2064, 1)
            return@onOpen true
        }

        /*
         * Handles make a cocktail.
         */

        on(cookingGnomeCocktailsInterface) { player, _, _, buttonID, _, _ ->
            var hasAll = true
            val cocktail: FruitCocktail? = when (buttonID) {
                3 -> FruitCocktail.WIZARD_BLIZZARD
                16 -> FruitCocktail.SHORT_GREEN_GUY
                23 -> FruitCocktail.FRUIT_BLAST
                32 -> FruitCocktail.PINEAPPLE_PUNCH
                41 -> FruitCocktail.DRUNK_DRAGON
                50 -> FruitCocktail.CHOC_SATURDAY
                61 -> FruitCocktail.BLURBERRY_SPEC
                else -> null
            }

            if (cocktail != null) {
                val cookingLevel = getStatLevel(player, Skills.COOKING)
                if (cookingLevel < cocktail.levelReq) {
                    sendDialogue(player, "You don't have the necessary level to make that.")
                    return@on true
                }

                val requiredItems = cocktail.requiredItems.map { it }
                for (ingredient in requiredItems) {
                    if (!inInventory(player, ingredient)) {
                        hasAll = false
                        break
                    }
                }

                if (!hasAll) {
                    sendDialogue(player, "You don't have the ingredients to make that.")
                    return@on true
                }

                requiredItems.forEach {
                    removeItem(player, it)
                }

                removeItem(player, Items.COCKTAIL_SHAKER_2025, Container.INVENTORY)
                addItem(player, cocktail.product, 1)
                rewardXP(player, Skills.COOKING, cocktail.experience)
                closeInterface(player)
            }

            return@on true
        }
    }

    internal enum class FruitCocktail(
        val levelReq: Int,
        val experience: Double,
        val product: Int,
        vararg val requiredItems: Int
    ) {
        FRUIT_BLAST(6, 50.0, Items.MIXED_BLAST_9568, Items.PINEAPPLE_2114, Items.LEMON_2102, Items.ORANGE_2108),
        PINEAPPLE_PUNCH(8, 70.0, Items.MIXED_PUNCH_9569, Items.PINEAPPLE_2114, Items.PINEAPPLE_2114, Items.LEMON_2102, Items.ORANGE_2108),
        WIZARD_BLIZZARD(18, 110.0, Items.MIXED_BLIZZARD_9566, Items.VODKA_2015, Items.VODKA_2015, Items.GIN_2019, Items.LIME_2120, Items.LEMON_2102, Items.ORANGE_2108),
        SHORT_GREEN_GUY(20, 120.0, Items.MIXED_SGG_9567, Items.VODKA_2015, Items.LIME_2120, Items.LIME_2120, Items.LIME_2120),
        DRUNK_DRAGON(32, 160.0, Items.MIXED_DRAGON_9574, Items.VODKA_2015, Items.GIN_2019, Items.DWELLBERRIES_2126),
        CHOC_SATURDAY(33, 170.0, Items.MIXED_SATURDAY_9571, Items.WHISKY_2017, Items.CHOCOLATE_BAR_1973, Items.EQUA_LEAVES_2128, Items.BUCKET_OF_MILK_1927),
        BLURBERRY_SPEC(37, 180.0, Items.MIXED_BLURBERRY_SPECIAL_9570, Items.VODKA_2015, Items.BRANDY_2021, Items.GIN_2019, Items.LEMON_2102, Items.LEMON_2102, Items.ORANGE_2108),
    }
}
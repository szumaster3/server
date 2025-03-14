package content.minigame.gnomecook.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class GnomeCocktailsListener : InteractionListener {
    private val uncookedProduct = intArrayOf(Items.MIXED_SATURDAY_9572, Items.MIXED_DRAGON_9576)
    private val sceneryID = intArrayOf(Scenery.GNOME_COOKER_17131, Scenery.RANGE_2728)

    private val mixers = mapOf(
        Items.MIXED_BLIZZARD_9566 to Pair(Items.WIZARD_BLIZZARD_9508, intArrayOf(Items.PINEAPPLE_CHUNKS_2116, Items.LIME_SLICES_2124)),
        Items.MIXED_SGG_9567 to Pair(Items.SHORT_GREEN_GUY_9510, intArrayOf(Items.LIME_SLICES_2124, Items.EQUA_LEAVES_2128)),
        Items.MIXED_BLAST_9568 to Pair(Items.FRUIT_BLAST_9514, intArrayOf(Items.LEMON_SLICES_2106)),
        Items.MIXED_PUNCH_9569 to Pair(Items.PINEAPPLE_PUNCH_9512, intArrayOf(Items.LIME_CHUNKS_2122, Items.PINEAPPLE_CHUNKS_2116, Items.ORANGE_SLICES_2112)),
        Items.MIXED_BLURBERRY_SPECIAL_9570 to Pair(Items.BLURBERRY_SPECIAL_9520, intArrayOf(Items.LEMON_CHUNKS_2104, Items.ORANGE_CHUNKS_2110, Items.EQUA_LEAVES_2128, Items.LIME_SLICES_2124)),
        Items.MIXED_SATURDAY_9571 to Pair(Items.MIXED_SATURDAY_9572, intArrayOf(Items.WHISKY_2017, Items.EQUA_LEAVES_2128, Items.BUCKET_OF_MILK_1927, Items.CHOCOLATE_DUST_1975, Items.POT_OF_CREAM_2130, Items.CHOCOLATE_BAR_1973)),
        Items.MIXED_DRAGON_9574 to Pair(Items.MIXED_DRAGON_9575, intArrayOf(Items.GIN_2019, Items.VODKA_2015, Items.DWELLBERRIES_2126, Items.PINEAPPLE_CHUNKS_2116, Items.POT_OF_CREAM_2130))
    )

    private val finishedDrinks = mapOf(
        Items.MIXED_SATURDAY_9573 to Pair(Items.CHOC_SATURDAY_2074, intArrayOf(Items.CHOCOLATE_DUST_1975, Items.POT_OF_CREAM_2130)),
        Items.MIXED_DRAGON_9575 to Pair(Items.MIXED_DRAGON_9576, intArrayOf(Items.PINEAPPLE_CHUNKS_2116, Items.POT_OF_CREAM_2130))
    )

    override fun defineListeners() {
        on(Items.COCKTAIL_SHAKER_2025, IntType.ITEM, "mix-cocktail") { player, _ ->
            openInterface(player, 436)
            return@on true
        }

        mixers.forEach { (mixerItemID, pouredDrink) ->
            on(mixerItemID, IntType.ITEM, "pour") { player, node ->
                if (!inInventory(player, Items.COCKTAIL_GLASS_2026)) {
                    sendDialogue(player, "You need a glass to pour this into.")
                    return@on true
                }

                val (product, requiredItems) = pouredDrink
                val hasAllIngredients = requiredItems.all { inInventory(player, it) }
                if (!hasAllIngredients) {
                    sendDialogue(player, "You don't have the garnishes for this.")
                    return@on true
                }

                requiredItems.forEach { removeItem(player, it) }
                removeItem(player, node.asItem())
                removeItem(player, Items.COCKTAIL_GLASS_2026)

                addItem(player, product)
                addItem(player, Items.COCKTAIL_SHAKER_2025)

                rewardXP(player, Skills.COOKING, 50.0)
                return@on true
            }
        }

        finishedDrinks.forEach { (unfItemID, finishedDrink) ->
            on(unfItemID, IntType.ITEM, "add-ingreds") { player, node ->
                val (product, requiredItems) = finishedDrink
                val hasAll = requiredItems.all { inInventory(player, it) }

                if (!hasAll) {
                    sendDialogue(player, "You don't have the ingredients for this.")
                    return@on true
                }

                requiredItems.forEach { removeItem(player, it) }
                removeItem(player, node.asItem())

                addItem(player, product)
                return@on true
            }
        }

        onUseWith(IntType.SCENERY, uncookedProduct, *sceneryID) { player, used, _ ->
            lock(player, 2)
            animate(player, Animations.HUMAN_MAKE_PIZZA_883)
            queueScript(player, 2, QueueStrength.SOFT) {
                if (removeItem(player, used)) {
                    unlock(player)
                    when (used.id) {
                        Items.MIXED_DRAGON_9576 -> addItem(player, Items.DRUNK_DRAGON_2092)
                        else -> addItem(player, Items.MIXED_SATURDAY_9573)
                    }
                }
                return@queueScript stopExecuting(player)
            }
            return@onUseWith true
        }
    }
}

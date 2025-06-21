package content.minigame.gnomecook.plugin

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Components
import org.rs.consts.Items

class GnomeCookingInterface : InterfaceListener {

    private val interfaceIds = mapOf(
        Components.GNOME_RESTAURANT_BATTAS_434   to GnomeProductType.BATTA,
        Components.GNOME_RESTAURANT_BOWL_435     to GnomeProductType.BOWL,
        Components.GNOME_RESTAURANT_COCKTAIL_436 to GnomeProductType.COCKTAIL,
        Components.GNOME_RESTAURANT_CRUNCHY_437  to GnomeProductType.CRUNCHY
    )

    override fun defineInterfaceListeners() {
        interfaceIds.forEach { (interfaceId, productType) ->
            onOpen(interfaceId) { player, component ->
                productType.products.forEach { (buttonId, product) ->
                    sendItemOnInterface(player, component.id, buttonId, product.productId, 1)
                }
                return@onOpen true
            }

            on(interfaceId) { player, _, _, buttonId, _, _ ->
                val product = productType.products[buttonId] ?: return@on true

                if (getStatLevel(player, Skills.COOKING) < product.levelReq) {
                    sendDialogue(player, "You don't have the required Cooking level.")
                    return@on true
                }

                if (product.requiresSpices && !inInventory(player, Items.GNOME_SPICE_2169)) {
                    sendDialogue(player, "You need gnome spices for this.")
                    return@on true
                }

                if (!product.requiredItems.all { inInventory(player, it) }) {
                    sendDialogue(player, "You don't have all the ingredients.")
                    return@on true
                }

                product.requiredItems.forEach { removeItem(player, it) }
                removeItem(player, product.containerId, Container.INVENTORY)
                addItem(player, product.productId, 1)

                product.experience?.let { rewardXP(player, Skills.COOKING, it) }
                closeInterface(player)

                return@on true
            }
        }
    }

    private enum class GnomeProductType(
        val products: Map<Int, CookingProduct>
    ) {
        BATTA(
            mapOf(
                3 to CookingProduct(Items.HALF_MADE_BATTA_9480, 25, 40.0,
                    intArrayOf(Items.EQUA_LEAVES_2128, Items.EQUA_LEAVES_2128, Items.LIME_CHUNKS_2122, Items.ORANGE_CHUNKS_2110, Items.PINEAPPLE_CHUNKS_2116),
                    Items.HALF_BAKED_BATTA_2249, requiresSpices = true),
                14 to CookingProduct(Items.HALF_MADE_BATTA_9482, 26, 40.0,
                    intArrayOf(Items.EQUA_LEAVES_2128, Items.CHEESE_1985, Items.TOADS_LEGS_2152),
                    Items.HALF_BAKED_BATTA_2249, requiresSpices = false),
                25 to CookingProduct(Items.HALF_MADE_BATTA_9485, 27, 40.0,
                    intArrayOf(Items.KING_WORM_2162, Items.CHEESE_1985),
                    Items.HALF_BAKED_BATTA_2249, requiresSpices = false),
                34 to CookingProduct(Items.HALF_MADE_BATTA_9483, 28, 40.0,
                    intArrayOf(Items.TOMATO_1982, Items.TOMATO_1982, Items.CHEESE_1985, Items.DWELLBERRIES_2126, Items.ONION_1957, Items.CABBAGE_1965),
                    Items.HALF_BAKED_BATTA_2249, requiresSpices = true),
                47 to CookingProduct(Items.HALF_MADE_BATTA_9478, 29, 40.0,
                    intArrayOf(Items.TOMATO_1982, Items.CHEESE_1985),
                    Items.HALF_BAKED_BATTA_2249, requiresSpices = true)
            )
        ),
        BOWL(
            mapOf(
                3 to CookingProduct(Items.HALF_MADE_BOWL_9563, 30, null,
                    intArrayOf(Items.KING_WORM_2162, Items.KING_WORM_2162, Items.KING_WORM_2162, Items.KING_WORM_2162, Items.ONION_1957, Items.ONION_1957),
                    Items.HALF_BAKED_BOWL_2177),
                12 to CookingProduct(Items.HALF_MADE_BOWL_9561, 35, null,
                    intArrayOf(Items.POTATO_1942, Items.POTATO_1942, Items.ONION_1957, Items.ONION_1957),
                    Items.HALF_BAKED_BOWL_2177),
                21 to CookingProduct(Items.HALF_MADE_BOWL_9559, 40, null,
                    intArrayOf(Items.TOADS_LEGS_2152, Items.TOADS_LEGS_2152, Items.TOADS_LEGS_2152, Items.TOADS_LEGS_2152, Items.CHEESE_1985, Items.CHEESE_1985, Items.DWELLBERRIES_2126, Items.EQUA_LEAVES_2128, Items.EQUA_LEAVES_2128),
                    Items.HALF_BAKED_BOWL_2177),
                34 to CookingProduct(Items.HALF_MADE_BOWL_9558, 42, null,
                    intArrayOf(Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_BAR_1973, Items.EQUA_LEAVES_2128),
                    Items.HALF_BAKED_BOWL_2177)
            )
        ),
        COCKTAIL(
            mapOf(
                3 to CookingProduct(Items.MIXED_BLIZZARD_9566, 18, 110.0,
                    intArrayOf(Items.VODKA_2015, Items.VODKA_2015, Items.GIN_2019, Items.LIME_2120, Items.LEMON_2102, Items.ORANGE_2108),
                    Items.COCKTAIL_SHAKER_2025),
                16 to CookingProduct(Items.MIXED_SGG_9567, 20, 120.0,
                    intArrayOf(Items.VODKA_2015, Items.LIME_2120, Items.LIME_2120, Items.LIME_2120),
                    Items.COCKTAIL_SHAKER_2025),
                23 to CookingProduct(Items.MIXED_BLAST_9568, 6, 50.0,
                    intArrayOf(Items.PINEAPPLE_2114, Items.LEMON_2102, Items.ORANGE_2108),
                    Items.COCKTAIL_SHAKER_2025),
                32 to CookingProduct(Items.MIXED_PUNCH_9569, 8, 70.0,
                    intArrayOf(Items.PINEAPPLE_2114, Items.PINEAPPLE_2114, Items.LEMON_2102, Items.ORANGE_2108),
                    Items.COCKTAIL_SHAKER_2025),
                41 to CookingProduct(Items.MIXED_DRAGON_9574, 32, 160.0,
                    intArrayOf(Items.VODKA_2015, Items.GIN_2019, Items.DWELLBERRIES_2126),
                    Items.COCKTAIL_SHAKER_2025),
                50 to CookingProduct(Items.MIXED_SATURDAY_9571, 33, 170.0,
                    intArrayOf(Items.WHISKY_2017, Items.CHOCOLATE_BAR_1973, Items.EQUA_LEAVES_2128, Items.BUCKET_OF_MILK_1927),
                    Items.COCKTAIL_SHAKER_2025),
                61 to CookingProduct(Items.MIXED_BLURBERRY_SPECIAL_9570, 37, 180.0,
                    intArrayOf(Items.VODKA_2015, Items.BRANDY_2021, Items.GIN_2019, Items.LEMON_2102, Items.LEMON_2102, Items.ORANGE_2108),
                    Items.COCKTAIL_SHAKER_2025)
            )
        ),
        CRUNCHY(
            mapOf(
                3 to CookingProduct(Items.HALF_MADE_CRUNCHY_9577, 16, 30.0,
                    intArrayOf(Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_BAR_1973),
                    Items.HALF_BAKED_CRUNCHY_2201),
                10 to CookingProduct(Items.HALF_MADE_CRUNCHY_9579, 12, 30.0,
                    intArrayOf(Items.EQUA_LEAVES_2128, Items.EQUA_LEAVES_2128),
                    Items.HALF_BAKED_CRUNCHY_2201),
                17 to CookingProduct(Items.HALF_MADE_CRUNCHY_9581, 10, 30.0,
                    intArrayOf(Items.TOADS_LEGS_2152, Items.TOADS_LEGS_2152),
                    Items.HALF_BAKED_CRUNCHY_2201),
                26 to CookingProduct(Items.HALF_MADE_CRUNCHY_9583, 14, 30.0,
                    intArrayOf(Items.EQUA_LEAVES_2128, Items.KING_WORM_2162, Items.KING_WORM_2162),
                    Items.HALF_BAKED_CRUNCHY_2201)
            )
        )
    }

    private data class CookingProduct(
        val productId: Int,
        val levelReq: Int,
        val experience: Double?,
        val requiredItems: IntArray,
        val containerId: Int,
        val requiresSpices: Boolean = true
    )
}
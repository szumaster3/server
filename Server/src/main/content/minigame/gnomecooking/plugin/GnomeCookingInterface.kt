package content.minigame.gnomecooking.plugin

import core.api.*
import core.game.component.Component
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items

/**
 * Represents gnome cooking interfaces.
 */
class GnomeCookingInterface : InterfaceListener {

    private data class ProductData(val productId: Int, val levelReq: Int, val experience: Double = 0.0, val requiredItems: Array<Item>, val needsSpice: Boolean = false, val isBowl: Boolean = false, val isBatta: Boolean = false, val isCocktail: Boolean = false, val isCrunchy: Boolean = false)

    companion object {
        private val bowlMap = mapOf(
            3  to ProductData(Items.HALF_MADE_BOWL_9563, 30, 0.0, arrayOf(Item(Items.KING_WORM_2162, 4), Item(Items.ONION_1957, 2)), needsSpice = true, isBowl = true),
            12 to ProductData(Items.HALF_MADE_BOWL_9561, 35, 0.0, arrayOf(Item(Items.POTATO_1942, 2), Item(Items.ONION_1957, 2)), isBowl = true),
            21 to ProductData(Items.HALF_MADE_BOWL_9559, 40, 0.0, arrayOf(Item(Items.TOADS_LEGS_2152, 4), Item(Items.CHEESE_1985, 2), Item(Items.DWELLBERRIES_2126), Item(Items.EQUA_LEAVES_2128, 2)), isBowl = true),
            34 to ProductData(Items.HALF_MADE_BOWL_9558, 42, 0.0, arrayOf(Item(Items.CHOCOLATE_BAR_1973, 4), Item(Items.EQUA_LEAVES_2128)), isBowl = true)
        )

        private val battaMap = mapOf(
            3  to ProductData(Items.HALF_MADE_BATTA_9480, 25, 40.0, arrayOf(Item(Items.PINEAPPLE_CHUNKS_2116), Item(Items.ORANGE_CHUNKS_2110), Item(Items.LIME_CHUNKS_2122), Item(Items.EQUA_LEAVES_2128, 4)), needsSpice = false, isBatta = true),
            14 to ProductData(Items.HALF_MADE_BATTA_9482, 26, 40.0, arrayOf(Item(Items.EQUA_LEAVES_2128), Item(Items.TOADS_LEGS_2152), Item(Items.CHEESE_1985)), needsSpice = true, isBatta = true),
            25 to ProductData(Items.HALF_MADE_BATTA_9485, 27, 40.0, arrayOf(Item(Items.KING_WORM_2162), Item(Items.CHEESE_1985)), needsSpice = true, isBatta = true),
            34 to ProductData(Items.HALF_MADE_BATTA_9483, 28, 40.0, arrayOf(Item(Items.TOMATO_1982, 2), Item(Items.ONION_1957), Item(Items.CABBAGE_1965), Item(Items.DWELLBERRIES_2126), Item(Items.CHEESE_1985)), isBatta = true),
            47 to ProductData(Items.HALF_MADE_BATTA_9478, 29, 40.0, arrayOf(Item(Items.TOMATO_1982), Item(Items.CHEESE_1985)), isBatta = true)
        )

        private val cocktailMap = mapOf(
            3  to ProductData(Items.MIXED_BLIZZARD_9566, 18, 110.0, arrayOf(Item(Items.VODKA_2015, 2), Item(Items.GIN_2019), Item(Items.ORANGE_2108), Item(Items.LIME_2120), Item(Items.LEMON_2102)), isCocktail = true),
            16 to ProductData(Items.MIXED_SGG_9567, 20, 120.0, arrayOf(Item(Items.VODKA_2015), Item(Items.LIME_2120, 3)), isCocktail = true),
            23 to ProductData(Items.MIXED_BLAST_9568, 6, 50.0, arrayOf(Item(Items.PINEAPPLE_2114), Item(Items.LEMON_2102), Item(Items.ORANGE_2108)), isCocktail = true),
            32 to ProductData(Items.MIXED_PUNCH_9569, 8, 70.0, arrayOf(Item(Items.PINEAPPLE_2114, 2), Item(Items.ORANGE_2108), Item(Items.LEMON_2102)), isCocktail = true),
            41 to ProductData(Items.MIXED_DRAGON_9574, 32, 160.0, arrayOf(Item(Items.VODKA_2015), Item(Items.GIN_2019), Item(Items.DWELLBERRIES_2126)), isCocktail = true),
            50 to ProductData(Items.MIXED_SATURDAY_9571, 33, 170.0, arrayOf(Item(Items.WHISKY_2017), Item(Items.EQUA_LEAVES_2128), Item(Items.BUCKET_OF_MILK_1927), Item(Items.CHOCOLATE_BAR_1973)), isCocktail = true),
            61 to ProductData(Items.MIXED_BLURBERRY_SPECIAL_9570, 37, 180.0, arrayOf(Item(Items.VODKA_2015), Item(Items.BRANDY_2021), Item(Items.GIN_2019), Item(Items.ORANGE_2108), Item(Items.LEMON_2102, 2)), isCocktail = true)
        )

        private val crunchyMap = mapOf(
            3  to ProductData(Items.HALF_MADE_CRUNCHY_9581, 10, 30.0, arrayOf(Item(Items.TOADS_LEGS_2152, 2)), needsSpice = true, isCrunchy = true),
            10 to ProductData(Items.HALF_MADE_CRUNCHY_9579, 12, 30.0, arrayOf(Item(Items.EQUA_LEAVES_2128, 2)), needsSpice = true, isCrunchy = true),
            17 to ProductData(Items.HALF_MADE_CRUNCHY_9583, 14, 30.0, arrayOf(Item(Items.EQUA_LEAVES_2128), Item(Items.KING_WORM_2162, 2)), needsSpice = true, isCrunchy = true),
            26 to ProductData(Items.HALF_MADE_CRUNCHY_9577, 16, 30.0, arrayOf(Item(Items.CHOCOLATE_BAR_1973, 2)), needsSpice = true, isCrunchy = true)
        )

        /**
         * Updates the cocktail interface for the player.
         */
        fun updateCocktailInterface(player: Player, component: Component) {
            cocktailMap.forEach { (buttonID, productData) ->
                val itemId = if (productData.requiredItems.all { player.inventory.containsItem(it) })
                {
                    productData.productId
                } else {
                    9489
                }
                sendItemOnInterface(player, component.id, buttonID, itemId)
            }
        }

        /**
         * Updates the batta interface for the player.
         */
        fun updateBattaInterface(player: Player, component: Component) {
            battaMap.forEach { (buttonID, productData) ->
                val itemID = if (productData.requiredItems.all { player.inventory.containsItem(it) })
                {
                    when (buttonID) {
                        3  -> Items.PICTURE_2275
                        14 -> Items.PICTURE_2193
                        25 -> Items.PICTURE_2251
                        34 -> Items.PICTURE_2179
                        47 -> Items.PICTURE_2257
                        else -> Items.NULL_9526
                    }
                } else {
                    Items.NULL_9526
                }
                sendItemOnInterface(player, component.id, buttonID, itemID)
            }
        }

        /**
         * Updates the bowl interface for the player.
         */
        fun updateBowlInterface(player: Player, component: Component) {
            bowlMap.forEach { (buttonID, productData) ->
                val itemID = if (productData.requiredItems.all { player.inventory.containsItem(it) })
                {
                    when (buttonID) {
                        3  -> Items.PICTURE_2189
                        12 -> Items.PICTURE_2193
                        21 -> Items.PICTURE_2193
                        34 -> Items.PICTURE_2181
                        else -> Items.NULL_9546
                    }
                } else {
                    Items.NULL_9546
                }
                sendItemOnInterface(player, component.id, buttonID, itemID)
            }
        }

        /**
         * Updates the crunchy interface for the player.
         */
        fun updateCrunchyInterface(player: Player, component: Component) {
            crunchyMap.forEach { (buttonID, productData) ->
                val itemID = if (productData.requiredItems.all { player.inventory.containsItem(it) })
                {
                    productData.productId
                } else {
                    Items.NULL_9537
                }
                sendItemOnInterface(player, component.id, buttonID, itemID)
            }
        }
    }

    override fun defineInterfaceListeners() {

        /*
         * Battas.
         */

        onOpen(Components.GNOME_RESTAURANT_BATTAS_434) { player, component ->
            updateBattaInterface(player, component)
            return@onOpen true
        }

        on(Components.GNOME_RESTAURANT_BATTAS_434) { player, _, _, buttonID, _, _ ->
            battaMap[buttonID]?.let { attemptMake(player, it) }
            return@on true
        }

        /*
         * Bowls.
         */

        onOpen(Components.GNOME_RESTAURANT_BOWL_435) { player, component ->
            updateBowlInterface(player, component)
            return@onOpen true
        }

        on(Components.GNOME_RESTAURANT_BOWL_435) { player, _, _, buttonID, _, _ ->
            bowlMap[buttonID]?.let { attemptMake(player, it) }
            return@on true
        }

        /*
         * Cocktails.
         */

        onOpen(Components.GNOME_RESTAURANT_COCKTAIL_436) { player, component ->
            updateCocktailInterface(player, component)
            return@onOpen true
        }

        on(Components.GNOME_RESTAURANT_COCKTAIL_436) { player, _, _, buttonID, _, _ ->
            cocktailMap[buttonID]?.let { attemptMake(player, it) }
            return@on true
        }

        /*
         * Crunches.
         */

        onOpen(Components.GNOME_RESTAURANT_CRUNCHY_437) { player, component ->
            updateCrunchyInterface(player, component)
            return@onOpen true
        }

        on(Components.GNOME_RESTAURANT_CRUNCHY_437) { player, _, _, buttonID, _, _ ->
            crunchyMap[buttonID]?.let { attemptMake(player, it) }
            return@on true
        }
    }

    private fun attemptMake(player: Player, data: ProductData) {
        if (getStatLevel(player, Skills.COOKING) < data.levelReq) {
            sendDialogue(player, "You don't have the required cooking level.")
            return
        }

        if (data.needsSpice && !inInventory(player, Items.GNOME_SPICE_2169)) {
            sendDialogue(player, "You need gnome spices for this.")
            return
        }

        if (!data.requiredItems.all { inInventory(player, it.id, it.amount) }) {
            sendDialogue(player, "You don't have all the ingredients for this.")
            return
        }

        data.requiredItems.forEach { removeItem(player, it) }

        if(data.productId == Items.MIXED_SATURDAY_9571)
            addItem(player, Items.BUCKET_1925)

        when {
            data.isBowl     -> removeItem(player, Items.HALF_BAKED_BOWL_2177)
            data.isBatta    -> removeItem(player, Items.HALF_BAKED_BATTA_2249)
            data.isCocktail -> removeItem(player, Items.COCKTAIL_SHAKER_2025)
            data.isCrunchy  -> removeItem(player, Items.HALF_BAKED_CRUNCHY_2201)
        }

        addItem(player, data.productId)
        rewardXP(player, Skills.COOKING, data.experience)
        closeInterface(player)
    }
}

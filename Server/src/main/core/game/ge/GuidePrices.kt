package core.game.ge

import core.api.sendInterfaceConfig
import core.api.sendString
import core.game.component.Component
import core.game.ge.GrandExchange.Companion.getRecommendedPrice
import core.game.node.entity.player.Player
import core.tools.StringUtils.formatDisplayName
import shared.consts.Components
import shared.consts.Items

/**
 * Provides functionality for managing and displaying guide prices in the Grand Exchange.
 */
object GuidePrices {
    private val COMPONENT = Component(Components.EXCHANGE_GUIDE_PRICE_642)

    /**
     * Opens the guide price interface for the specified player and type.
     *
     * @param player the player requesting the guide prices.
     * @param type   the type of guide prices to display.
     */
    @JvmStatic
    fun open(player: Player, type: GuideType) {
        player.interfaceManager.open(COMPONENT)
        type.display(player)
    }

    /**
     * Clears the guide price interface for the specified player.
     *
     * @param player the player whose guide price interface should be cleared.
     */
    fun clear(player: Player) {
        for (i in 135..164) {
            player.packetDispatch.sendInterfaceConfig(COMPONENT.id, i, true)
        }
    }

    /**
     * Represents an item in the guide price list.
     *
     * @param item      the item ID.
     * @param childData the child data for the item's display.
     */
    class GuideItem(
        /**
         * The item ID.
         */
        @JvmField val item: Int,

        /**
         * The child data for the item's display.
         */
        vararg val childData: Int
    )

    /**
     * Represents different types of guide price categories, such as logs, ores, etc.
     *
     * @param childData the range of child data indices to display for the guide type.
     * @param items     the items associated with the guide type.
     */
    enum class GuideType(
        /**
         * The child data indices for the guide type.
         */
        val childData: IntArray, vararg items: GuideItem
    ) {
        /**
         * Guide prices for logs.
         */
        LOGS(
            intArrayOf(0, 0),
            GuideItem(Items.LOGS_1511, 155),
            GuideItem(Items.ACHEY_TREE_LOGS_2862, 156),
            GuideItem(Items.OAK_LOGS_1521, 157),
            GuideItem(Items.WILLOW_LOGS_1519, 158),
            GuideItem(Items.TEAK_LOGS_6333, 159),
            GuideItem(Items.MAPLE_LOGS_1517, 160),
            GuideItem(Items.MAHOGANY_LOGS_6332, 161),
            GuideItem(Items.EUCALYPTUS_LOGS_12581, 162),
            GuideItem(Items.YEW_LOGS_1515, 163),
            GuideItem(Items.MAGIC_LOGS_1513, 164)
        ),

        /**
         * Guide prices for ores.
         */
        ORES(
            intArrayOf(40, 44),
            GuideItem(Items.COPPER_ORE_436, 33),
            GuideItem(Items.TIN_ORE_438, 34),
            GuideItem(Items.IRON_ORE_440, 35),
            GuideItem(Items.SILVER_ORE_442, 36),
            GuideItem(Items.COAL_453, 37),
            GuideItem(Items.GOLD_ORE_444, 38),
            GuideItem(Items.MITHRIL_ORE_447, 39),
            GuideItem(Items.ADAMANTITE_ORE_449, 40),
            GuideItem(Items.RUNITE_ORE_451, 41)
        ),

        /**
         * Guide prices for runes.
         */
        RUNES(
            intArrayOf(215, 216),
            GuideItem(Items.RUNE_ESSENCE_1436, 183),
            GuideItem(Items.PURE_ESSENCE_7936, 184),
            GuideItem(Items.AIR_RUNE_556, 185),
            GuideItem(Items.MIND_RUNE_558, 186),
            GuideItem(Items.WATER_RUNE_555, 187),
            GuideItem(Items.EARTH_RUNE_557, 188),
            GuideItem(Items.FIRE_RUNE_554, 189),
            GuideItem(Items.BODY_RUNE_559, 190),
            GuideItem(Items.COSMIC_RUNE_564, 191),
            GuideItem(Items.CHAOS_RUNE_562, 192),
            GuideItem(Items.ASTRAL_RUNE_9075, 193),
            GuideItem(Items.NATURE_RUNE_561, 194),
            GuideItem(Items.LAW_RUNE_563, 195),
            GuideItem(Items.DEATH_RUNE_560, 196),
            GuideItem(Items.BLOOD_RUNE_565, 197),
            GuideItem(Items.SOUL_RUNE_566, 198)
        ),

        /**
         * Guide prices for herbs.
         */
        HERBS(
            intArrayOf(130, 135),
            GuideItem(Items.CLEAN_GUAM_249, 119),
            GuideItem(Items.CLEAN_MARRENTILL_251, 120),
            GuideItem(Items.CLEAN_TARROMIN_253, 121),
            GuideItem(Items.CLEAN_HARRALANDER_255, 122),
            GuideItem(Items.CLEAN_RANARR_257, 123),
            GuideItem(Items.CLEAN_TOADFLAX_2998, 124),
            GuideItem(Items.CLEAN_IRIT_259, 125),
            GuideItem(Items.CLEAN_SPIRIT_WEED_12172, 126),
            GuideItem(Items.CLEAN_AVANTOE_261, 127),
            GuideItem(Items.CLEAN_KWUARM_263, 128),
            GuideItem(Items.CLEAN_SNAPDRAGON_3000, 129),
            GuideItem(Items.CLEAN_CADANTINE_265, 130),
            GuideItem(Items.CLEAN_LANTADYME_2481, 131),
            GuideItem(Items.CLEAN_DWARF_WEED_267, 132),
            GuideItem(Items.CLEAN_TORSTOL_269, 133)
        ),

        /**
         * Guide prices for weapons and armour.
         */
        WEAPONS_AND_ARMOUR(
            intArrayOf(88, 89),
            GuideItem(Items.ADAMANT_ARMOUR_SET_L_11834, 73),
            GuideItem(Items.RUNE_ARMOUR_SET_L_11838, 74),
            GuideItem(Items.DRAGON_CHAIN_ARMOUR_SET_L_11842, 75),
            GuideItem(Items.BLUE_DRAGONHIDE_SET_11866, 76),
            GuideItem(Items.BLACK_DRAGONHIDE_SET_11870, 77),
            GuideItem(Items.AHRIMS_SET_11846, 78),
            GuideItem(Items.DHAROKS_SET_11848, 79),
            GuideItem(Items.GUTHANS_SET_11850, 80),
            GuideItem(Items.VERACS_SET_11856, 81),
            GuideItem(Items.DRAGON_BOOTS_11732, 82),
            GuideItem(Items.ABYSSAL_WHIP_4151, 83),
            GuideItem(Items.DARK_BOW_11235, 84),
            GuideItem(Items.DRAGON_AXE_6739, 85),
            GuideItem(Items.DRAGON_SCIMITAR_4587, 86),
            GuideItem(Items.GRANITE_MAUL_4153, 87)
        );

        /**
         * The list of items for this guide type.
         */
        val items: Array<GuideItem> = items as Array<GuideItem>

        /**
         * Displays the guide prices for the player.
         *
         * @param player the player to display the guide prices for.
         */
        fun display(player: Player) {
            player.setAttribute("guide-price", this)
            if (this != LOGS) {
                clear(player)
            }
            sendString(player, "Guide Prices: " + formatDisplayName(name), COMPONENT.id, 14)
            for (i in childData[0] until childData[1]) {
                sendInterfaceConfig(player, Components.EXCHANGE_GUIDE_PRICE_642, i, false)
            }
            for (item in items) {
                sendString(player,
                    "" + getRecommendedPrice(item.item, false) + " gp", COMPONENT.id, item.childData[0]
                )
            }
        }
    }
}

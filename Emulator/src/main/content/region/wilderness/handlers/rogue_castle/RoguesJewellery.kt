package content.region.wilderness.handlers.rogue_castle

import org.rs.consts.Items

/**
 * The enum Rogues jewellery.
 */
enum class RoguesJewellery(
    /**
     * Gets item.
     *
     * @return the item
     */
    val item: Int,
    /**
     * Gets amount.
     *
     * @return the amount
     */
    val amount: Int,
    /**
     * Gets price.
     *
     * @return the price
     */
    val price: Int
) {
    /**
     * Gold ring rogues jewellery.
     */
    GOLD_RING(Items.GOLD_RING_1635, 1, 350),

    /**
     * Gold ring noted rogues jewellery.
     */
    GOLD_RING_NOTED(Items.GOLD_RING_1636, 1, 350),

    /**
     * Sapphire ring rogues jewellery.
     */
    SAPPHIRE_RING(Items.SAPPHIRE_RING_1637, 1, 900),

    /**
     * Sapphire ring noted rogues jewellery.
     */
    SAPPHIRE_RING_NOTED(Items.SAPPHIRE_RING_1638, 1, 900),

    /**
     * Emerald ring rogues jewellery.
     */
    EMERALD_RING(Items.EMERALD_RING_1639, 1, 1275),

    /**
     * Emerald ring noted rogues jewellery.
     */
    EMERALD_RING_NOTED(Items.EMERALD_RING_1640, 1, 1275),

    /**
     * Ruby ring rogues jewellery.
     */
    RUBY_RING(Items.RUBY_RING_1641, 1, 2025),

    /**
     * Ruby ring noted rogues jewellery.
     */
    RUBY_RING_NOTED(Items.RUBY_RING_1642, 1, 2025),

    /**
     * Diamond ring rogues jewellery.
     */
    DIAMOND_RING(Items.DIAMOND_RING_1643, 1, 3525),

    /**
     * Diamond ring noted rogues jewellery.
     */
    DIAMOND_RING_NOTED(Items.DIAMOND_RING_1644, 1, 3525),

    /**
     * Dragonstone ring rogues jewellery.
     */
    DRAGONSTONE_RING(Items.DRAGONSTONE_RING_1645, 1, 17625),

    /**
     * Dragonstone ring noted rogues jewellery.
     */
    DRAGONSTONE_RING_NOTED(Items.DRAGONSTONE_RING_1646, 1, 17625),

    /**
     * Gold necklace rogues jewellery.
     */
    GOLD_NECKLACE(Items.GOLD_NECKLACE_1654, 1, 450),

    /**
     * Gold necklace noted rogues jewellery.
     */
    GOLD_NECKLACE_NOTED(Items.GOLD_NECKLACE_1655, 1, 450),

    /**
     * Sapphire necklace rogues jewellery.
     */
    SAPPHIRE_NECKLACE(Items.SAPPHIRE_NECKLACE_1656, 1, 1050),

    /**
     * Sapphire necklace noted rogues jewellery.
     */
    SAPPHIRE_NECKLACE_NOTED(Items.SAPPHIRE_NECKLACE_1657, 1, 1050),

    /**
     * Emerald necklace rogues jewellery.
     */
    EMERALD_NECKLACE(Items.EMERALD_NECKLACE_1658, 1, 1425),

    /**
     * Emerald necklace noted rogues jewellery.
     */
    EMERALD_NECKLACE_NOTED(Items.EMERALD_NECKLACE_1659, 1, 1425),

    /**
     * Ruby necklace rogues jewellery.
     */
    RUBY_NECKLACE(Items.RUBY_NECKLACE_1660, 1, 2175),

    /**
     * Ruby necklace noted rogues jewellery.
     */
    RUBY_NECKLACE_NOTED(Items.RUBY_NECKLACE_1661, 1, 2175),

    /**
     * Diamond necklace rogues jewellery.
     */
    DIAMOND_NECKLACE(Items.DIAMOND_NECKLACE_1662, 1, 3675),

    /**
     * Diamond necklace noted rogues jewellery.
     */
    DIAMOND_NECKLACE_NOTED(Items.DIAMOND_NECKLACE_1663, 1, 3675),

    /**
     * Dragon necklace rogues jewellery.
     */
    DRAGON_NECKLACE(Items.DRAGON_NECKLACE_1664, 1, 18375),

    /**
     * Dragon necklace noted rogues jewellery.
     */
    DRAGON_NECKLACE_NOTED(Items.DRAGON_NECKLACE_1665, 1, 18375),

    /**
     * Gold bracelet rogues jewellery.
     */
    GOLD_BRACELET(Items.GOLD_BRACELET_11069, 1, 550),

    /**
     * Gold bracelet noted rogues jewellery.
     */
    GOLD_BRACELET_NOTED(Items.GOLD_BRACELET_11070, 1, 550),

    /**
     * Sapphire bracelet rogues jewellery.
     */
    SAPPHIRE_BRACELET(Items.SAPPHIRE_BRACELET_11072, 1, 1150),

    /**
     * Sapphire bracelet noted rogues jewellery.
     */
    SAPPHIRE_BRACELET_NOTED(Items.SAPPHIRE_BRACELET_11073, 1, 1150),

    /**
     * Emerald bracelet rogues jewellery.
     */
    EMERALD_BRACELET(Items.EMERALD_BRACELET_11076, 1, 1525),

    /**
     * Emerald bracelet noted rogues jewellery.
     */
    EMERALD_BRACELET_NOTED(Items.EMERALD_BRACELET_11077, 1, 1525),

    /**
     * Ruby bracelet rogues jewellery.
     */
    RUBY_BRACELET(Items.RUBY_BRACELET_11085, 1, 2325),

    /**
     * Ruby bracelet noted rogues jewellery.
     */
    RUBY_BRACELET_NOTED(Items.RUBY_BRACELET_11086, 1, 2325),

    /**
     * Diamond bracelet rogues jewellery.
     */
    DIAMOND_BRACELET(Items.DIAMOND_BRACELET_11092, 1, 3825),

    /**
     * Diamond bracelet noted rogues jewellery.
     */
    DIAMOND_BRACELET_NOTED(Items.DIAMOND_BRACELET_11093, 1, 3825),

    /**
     * Dragon bracelet rogues jewellery.
     */
    DRAGON_BRACELET(Items.DRAGON_BRACELET_11115, 1, 19125),

    /**
     * Dragon bracelet noted rogues jewellery.
     */
    DRAGON_BRACELET_NOTED(Items.DRAGON_BRACELET_11116, 1, 19125),

    /**
     * Gold amulet rogues jewellery.
     */
    GOLD_AMULET(Items.GOLD_AMULET_1692, 1, 350),

    /**
     * Gold amulet noted rogues jewellery.
     */
    GOLD_AMULET_NOTED(Items.GOLD_AMULET_1693, 1, 350),

    /**
     * Sapphire amulet rogues jewellery.
     */
    SAPPHIRE_AMULET(Items.SAPPHIRE_AMULET_1694, 1, 900),

    /**
     * Sapphire amulet noted rogues jewellery.
     */
    SAPPHIRE_AMULET_NOTED(Items.SAPPHIRE_AMULET_1695, 1, 900),

    /**
     * Emerald amulet rogues jewellery.
     */
    EMERALD_AMULET(Items.EMERALD_AMULET_1696, 1, 1275),

    /**
     * Emerald amulet noted rogues jewellery.
     */
    EMERALD_AMULET_NOTED(Items.EMERALD_AMULET_1697, 1, 1275),

    /**
     * Ruby amulet rogues jewellery.
     */
    RUBY_AMULET(Items.RUBY_AMULET_1698, 1, 2025),

    /**
     * Ruby amulet noted rogues jewellery.
     */
    RUBY_AMULET_NOTED(Items.RUBY_AMULET_1699, 1, 2025),

    /**
     * Diamond amulet rogues jewellery.
     */
    DIAMOND_AMULET(Items.DIAMOND_AMULET_1700, 1, 3525),

    /**
     * Diamond amulet noted rogues jewellery.
     */
    DIAMOND_AMULET_NOTED(Items.DIAMOND_AMULET_1701, 1, 3525),

    /**
     * Dragonstone ammy rogues jewellery.
     */
    DRAGONSTONE_AMMY(Items.DRAGONSTONE_AMMY_1702, 1, 17625),

    /**
     * Dragonstone ammy noted rogues jewellery.
     */
    DRAGONSTONE_AMMY_NOTED(Items.DRAGONSTONE_AMMY_1703, 1, 17625);

    companion object {
        /**
         * The constant JewelleryMap.
         */
        val JewelleryMap: MutableMap<Int, RoguesJewellery> = HashMap()

        init {
            for (jewellery in values()) {
                JewelleryMap[jewellery.item] = jewellery
            }
        }
    }
}

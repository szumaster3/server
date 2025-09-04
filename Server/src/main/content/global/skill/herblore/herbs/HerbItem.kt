package content.global.skill.herblore.herbs

import core.game.node.item.Item
import shared.consts.Items

/**
 * Represents types of herbs.
 */
enum class HerbItem(@JvmField val herb: Item, val experience: Double, val level: Int, @JvmField val product: Item) {
    GUAM(Item(Items.GRIMY_GUAM_199), 2.5, 3, Item(Items.CLEAN_GUAM_249)),
    MARRENTILL(Item(Items.GRIMY_MARRENTILL_201), 3.8, 5, Item(Items.CLEAN_MARRENTILL_251)),
    TARROMIN(Item(Items.GRIMY_TARROMIN_203), 5.0, 11, Item(Items.CLEAN_TARROMIN_253)),
    HARRALANDER(Item(Items.GRIMY_HARRALANDER_205), 6.3, 20, Item(Items.CLEAN_HARRALANDER_255)),
    RANARR(Item(Items.GRIMY_RANARR_207), 7.5, 25, Item(Items.CLEAN_RANARR_257)),
    TOADFLAX(Item(Items.GRIMY_TOADFLAX_3049), 8.0, 30, Item(Items.CLEAN_TOADFLAX_2998)),
    SPIRIT_WEED(Item(Items.GRIMY_SPIRIT_WEED_12174), 7.8, 35, Item(Items.CLEAN_SPIRIT_WEED_12172)),
    IRIT(Item(Items.GRIMY_IRIT_209), 8.8, 40, Item(Items.CLEAN_IRIT_259)),
    AVANTOE(Item(Items.GRIMY_AVANTOE_211), 10.0, 48, Item(Items.CLEAN_AVANTOE_261)),
    KWUARM(Item(Items.GRIMY_KWUARM_213), 11.3, 54, Item(Items.CLEAN_KWUARM_263)),
    SNAPDRAGON(Item(Items.GRIMY_SNAPDRAGON_3051), 11.8, 59, Item(Items.CLEAN_SNAPDRAGON_3000)),
    CADANTINE(Item(Items.GRIMY_CADANTINE_215), 12.5, 65, Item(Items.CLEAN_CADANTINE_265)),
    LANTADYME(Item(Items.GRIMY_LANTADYME_2485), 13.1, 67, Item(Items.CLEAN_LANTADYME_2481)),
    DWARF_WEED(Item(Items.GRIMY_DWARF_WEED_217), 13.8, 70, Item(Items.CLEAN_DWARF_WEED_267)),
    TORSTOL(Item(Items.GRIMY_TORSTOL_219), 15.0, 75, Item(Items.CLEAN_TORSTOL_269)),
    SNAKE_WEED(Item(Items.GRIMY_SNAKE_WEED_1525), 2.5, 3, Item(Items.CLEAN_SNAKE_WEED_1526)),
    ARDRIGAL(Item(Items.GRIMY_ARDRIGAL_1527), 2.5, 3, Item(Items.CLEAN_ARDRIGAL_1528)),
    SITO_FOIL(Item(Items.GRIMY_SITO_FOIL_1529), 2.5, 3, Item(Items.CLEAN_SITO_FOIL_1530)),
    VOLENCIA_MOSS(Item(Items.GRIMY_VOLENCIA_MOSS_1531), 2.5, 3, Item(Items.CLEAN_VOLENCIA_MOSS_1532)),
    ROGUES_PUSE(Item(Items.GRIMY_ROGUES_PURSE_1533), 2.5, 3, Item(Items.CLEAN_ROGUES_PURSE_1534)),
    ;

    companion object {
        /**
         * A map of herb IDs to their respective [HerbItem] enum values.
         */
        private val herbMap =
            HashMap<Int, HerbItem>().apply {
                values().forEach { herbData -> put(herbData.herb.id, herbData) }
            }

        /**
         * Finds the [HerbItem] enum value for the given [Item].
         *
         * @param item The [Item] representing the herb.
         * @return The corresponding [HerbItem] enum value or null if no match is found.
         */
        fun forItem(item: Item): HerbItem? = herbMap[item.id]
    }
}
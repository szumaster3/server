package content.global.skill.crafting

import core.api.*
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import shared.consts.Sounds

object CraftingDefinition {
    /**
     * Base item ids for crafting.
     */
    const val GOLD_BAR: Int = Items.GOLD_BAR_2357
    const val PERFECT_GOLD_BAR: Int = Items.PERFECT_GOLD_BAR_2365
    const val SAPPHIRE: Int = Items.SAPPHIRE_1607
    const val EMERALD: Int = Items.EMERALD_1605
    const val RUBY: Int = Items.RUBY_1603
    const val DIAMOND: Int = Items.DIAMOND_1601
    const val DRAGONSTONE: Int = Items.DRAGONSTONE_1615
    const val ONYX: Int = Items.ONYX_6573

    /**
     * Base moulds for crafting.
     */
    const val RING_MOULD: Int = Items.RING_MOULD_1592
    const val AMULET_MOULD: Int = Items.AMULET_MOULD_1595
    const val NECKLACE_MOULD: Int = Items.NECKLACE_MOULD_1597
    const val BRACELET_MOULD: Int = Items.BRACELET_MOULD_11065

    /**
     * Represents a gold jewellery items.
     */
    enum class JewelleryItem(val level: Int, experience: Int, val componentId: Int, val sendItem: Int, vararg val items: Int) {
        GOLD_RING(5, 15, 19, Items.GOLD_RING_1635, Items.GOLD_BAR_2357),
        SAPPHIRE_RING(20, 40, 21, Items.SAPPHIRE_RING_1637, Items.SAPPHIRE_1607, Items.GOLD_BAR_2357),
        EMERALD_RING(27, 55, 23, Items.EMERALD_RING_1639, Items.EMERALD_1605, Items.GOLD_BAR_2357),
        RUBY_RING(34, 70, 25, Items.RUBY_RING_1641, Items.RUBY_1603, Items.GOLD_BAR_2357),
        PERFECT_RING(40, 70, 25, Items.PERFECT_RING_773, Items.RUBY_1603, Items.PERFECT_GOLD_BAR_2365),
        DIAMOND_RING(43, 85, 27, Items.DIAMOND_RING_1643, Items.DIAMOND_1601, Items.GOLD_BAR_2357),
        DRAGONSTONE_RING(55, 100, 29, Items.DRAGONSTONE_RING_1645, Items.DRAGONSTONE_1615, Items.GOLD_BAR_2357),
        ONYX_RING(67, 115, 31, Items.ONYX_RING_6575, Items.ONYX_6573, Items.GOLD_BAR_2357),

        GOLD_NECKLACE(6, 20, 41, Items.GOLD_NECKLACE_1654, Items.GOLD_BAR_2357),
        SAPPHIRE_NECKLACE(22, 55, 43, Items.SAPPHIRE_NECKLACE_1656, Items.SAPPHIRE_1607, Items.GOLD_BAR_2357),
        EMERALD_NECKLACE(29, 60, 45, Items.EMERALD_NECKLACE_1658, Items.EMERALD_1605, Items.GOLD_BAR_2357),
        RUBY_NECKLACE(40, 75, 47, Items.RUBY_NECKLACE_1660, Items.RUBY_1603, Items.GOLD_BAR_2357),
        PERFECT_NECKLACE(40, 75, 47, Items.PERFECT_NECKLACE_774, Items.RUBY_1603, Items.PERFECT_GOLD_BAR_2365),
        DIAMOND_NECKLACE(56, 90, 49, Items.DIAMOND_NECKLACE_1662, Items.DIAMOND_1601, Items.GOLD_BAR_2357),
        DRAGONSTONE_NECKLACE(72, 105, 51, Items.DRAGON_NECKLACE_1664, Items.DRAGONSTONE_1615, Items.GOLD_BAR_2357),
        ONYX_NECKLACE(82, 120, 53, Items.ONYX_NECKLACE_6577, Items.ONYX_6573, Items.GOLD_BAR_2357),
        SLAYER_RING(75, 15, 34, Items.RING_OF_SLAYING8_13281, Items.ENCHANTED_GEM_4155, Items.GOLD_BAR_2357),

        GOLD_AMULET(8, 30, 60, Items.GOLD_AMULET_1673, Items.GOLD_BAR_2357),
        SAPPHIRE_AMULET(24, 63, 62, Items.SAPPHIRE_AMULET_1675, Items.SAPPHIRE_1607, Items.GOLD_BAR_2357),
        EMERALD_AMULET(31, 70, 64, Items.EMERALD_AMULET_1677, Items.EMERALD_1605, Items.GOLD_BAR_2357),
        RUBY_AMULET(50, 85, 66, Items.RUBY_AMULET_1679, Items.RUBY_1603, Items.GOLD_BAR_2357),
        DIAMOND_AMULET(70, 100, 68, Items.DIAMOND_AMULET_1681, Items.DIAMOND_1601, Items.GOLD_BAR_2357),
        DRAGONSTONE_AMULET(80, 150, 70, Items.DRAGONSTONE_AMMY_1683, Items.DRAGONSTONE_1615, Items.GOLD_BAR_2357),
        ONYX_AMULET(90, 165, 72, Items.ONYX_AMULET_6579, Items.ONYX_6573, Items.GOLD_BAR_2357),

        GOLD_BRACELET(7, 25, 79, Items.GOLD_BRACELET_11069, Items.GOLD_BAR_2357),
        SAPPHIRE_BRACELET(23, 60, 81, Items.SAPPHIRE_BRACELET_11072, Items.SAPPHIRE_1607, Items.GOLD_BAR_2357),
        EMERALD_BRACELET(30, 65, 83, Items.EMERALD_BRACELET_11076, Items.EMERALD_1605, Items.GOLD_BAR_2357),
        RUBY_BRACELET(42, 80, 85, Items.RUBY_BRACELET_11085, Items.RUBY_1603, Items.GOLD_BAR_2357),
        DIAMOND_BRACELET(58, 95, 87, Items.DIAMOND_BRACELET_11092, Items.DIAMOND_1601, Items.GOLD_BAR_2357),
        DRAGONSTONE_BRACELET(74, 110, 89, Items.DRAGON_BRACELET_11115, Items.DRAGONSTONE_1615, Items.GOLD_BAR_2357),
        ONYX_BRACELET(84, 125, 91, Items.ONYX_BRACELET_11130, Items.ONYX_6573, Items.GOLD_BAR_2357);

        val experience: Double = experience.toDouble()

        companion object {
            var productMap = HashMap<Int, JewelleryItem>()

            init {
                val jewelleryArray = values()
                for (jewelleryItem in jewelleryArray) {
                    productMap.putIfAbsent(jewelleryItem.sendItem, jewelleryItem)
                }
            }

            @JvmStatic
            fun forProduct(id: Int): JewelleryItem? {
                return productMap[id]
            }
        }
    }

    /**
     * Represents the silver products.
     */
    enum class Silver(val buttonId: Int, val required: Int, val product: Int, val amount: Int, val level: Int, val experience: Double) {
        HOLY(16, Items.HOLY_MOULD_1599, Items.UNSTRUNG_SYMBOL_1714, 1, 16, 50.0),
        UNHOLY(23, Items.UNHOLY_MOULD_1594, Items.UNSTRUNG_EMBLEM_1720, 1, 17, 50.0),
        SICKLE(30, Items.SICKLE_MOULD_2976, Items.SILVER_SICKLE_2961, 1, 18, 50.0),
        TIARA(44, Items.TIARA_MOULD_5523, Items.TIARA_5525, 1, 23, 52.5),
        SILVTHRIL_CHAIN(73, Items.CHAIN_LINK_MOULD_13153, Items.SILVTHRIL_CHAIN_13154, 1, 47, 100.0),
        LIGHTNING_ROD(37, Items.CONDUCTOR_MOULD_4200, Items.CONDUCTOR_4201, 1, 20, 50.0),
        SILVTHRILL_ROD(52, Items.ROD_CLAY_MOULD_7649, Items.SILVTHRILL_ROD_7637, 1, 25, 55.0),
        CROSSBOW_BOLTS(66, Items.BOLT_MOULD_9434, Items.SILVER_BOLTS_UNF_9382, 10, 21, 50.0),
        DEMONIC_SIGIL(59, Items.DEMONIC_SIGIL_MOULD_6747, Items.DEMONIC_SIGIL_6748, 1, 30, 50.0);

        companion object {
            fun forId(itemId: Int): Silver? = values().find { it.required == itemId }
            fun forButton(button: Int): Silver? = values().find { it.buttonId == button }
        }
    }

    /**
     * Represents different types of gems that can be cut.
     */
    enum class Gem(val uncut: Int, val cut: Int, val animation: Int, val level: Int, val xp: Double) {
        OPAL(Items.UNCUT_OPAL_1625, Items.OPAL_1609, Animations.CUT_OPAL_890, 1, 10.0),
        JADE(Items.UNCUT_JADE_1627, Items.JADE_1611, Animations.CUT_JADE_891, 13, 20.0),
        RED_TOPAZ(Items.UNCUT_RED_TOPAZ_1629, Items.RED_TOPAZ_1613, Animations.CUT_TOPAZ_892,16, 25.0),
        SAPPHIRE(Items.UNCUT_SAPPHIRE_1623, Items.SAPPHIRE_1607, Animations.CUT_SAPPHIRE_888,20, 50.0),
        EMERALD(Items.UNCUT_EMERALD_1621, Items.EMERALD_1605, Animations.CUT_EMERALD_889, 27,67.0),
        RUBY(Items.UNCUT_RUBY_1619, Items.RUBY_1603, Animations.CUT_RUBY_887,34, 85.0),
        DIAMOND(Items.UNCUT_DIAMOND_1617, Items.DIAMOND_1601, Animations.CUT_DIAMOND_886, 43, 107.5),
        DRAGONSTONE(Items.UNCUT_DRAGONSTONE_1631, Items.DRAGONSTONE_1615, Animations.CUT_DRAGONSTONE_885,55, 137.5),
        ONYX(Items.UNCUT_ONYX_6571, Items.ONYX_6573, Animations.CHISEL_ONYX_2717,67, 168.0),
        ;

        companion object {
            fun forId(uncutId: Int): Gem? {
                return values().firstOrNull { it.uncut == uncutId }
            }
        }
    }

    /**
     * Represents different types of glass products that can be crafted.
     */
    enum class Glass(val buttonId: Int, val productId: Int, val amount: Int, val requiredLevel: Int, val experience: Double) {
        EMPTY_VIAL(38, Items.VIAL_229, 1, 33, 35.0),
        UNPOWERED_ORB(39, Items.UNPOWERED_ORB_567, 1, 46, 52.5),
        BEER_GLASS(40, Items.BEER_GLASS_1919, 1, 1, 17.5),
        EMPTY_CANDLE_LANTERN(41, Items.CANDLE_LANTERN_4527, 1, 4, 19.0),
        EMPTY_OIL_LAMP(42, Items.OIL_LAMP_4525, 1, 12, 25.0),
        LANTERN_LENS(43, Items.LANTERN_LENS_4542, 1, 49, 55.0),
        FISHBOWL(44, Items.FISHBOWL_6667, 1, 42, 42.5),
        EMPTY_LIGHT_ORB(45, Items.LIGHT_ORB_10973, 1, 87, 70.0);

        companion object {
            private val lookupMap: MutableMap<Int, Glass> = HashMap()

            init {
                for (glass in values()) {
                    lookupMap[glass.buttonId] = glass
                    lookupMap[glass.productId] = glass
                }
            }

            fun getById(id: Int): Glass? {
                return lookupMap[id]
            }
        }
    }

    /**
     * Represents the different types of leather.
     */
    enum class Leather(val input: Int, val product: Int, val amount: Int, val level: Int, val xp: Double, val studded: Boolean = false, val pair: Boolean = false, val diary: DiaryTask? = null, val type: Type = Type.SOFT) {
        LEATHER_BODY(Items.LEATHER_1741, Items.LEATHER_BODY_1129, 1, 14, 25.0, type = Type.SOFT),
        LEATHER_GLOVES(Items.LEATHER_1741, Items.LEATHER_GLOVES_1059, 1, 1, 13.8, pair = true, diary = DiaryTask(DiaryType.LUMBRIDGE, 1, 3), type = Type.SOFT),
        LEATHER_BOOTS(Items.LEATHER_1741, Items.LEATHER_BOOTS_1061, 1, 7, 16.0, pair = true, type = Type.SOFT),
        LEATHER_COWL(Items.LEATHER_1741, Items.LEATHER_COWL_1167, 1, 9, 18.5, type = Type.SOFT),
        LEATHER_VAMBRACES(Items.LEATHER_1741, Items.LEATHER_VAMBRACES_1063, 1, 11, 22.0, pair = true, type = Type.SOFT),
        LEATHER_CHAPS(Items.LEATHER_1741, Items.LEATHER_CHAPS_1095, 1, 18, 27.0, type = Type.SOFT),
        LEATHER_COIF(Items.LEATHER_1741, Items.COIF_1169, 1, 38, 37.0, type = Type.SOFT),
        HARD_LEATHER_BODY(Items.HARD_LEATHER_1743, Items.HARDLEATHER_BODY_1131, 1, 28, 35.0, type = Type.HARD),
        STUDDED_BODY(Items.HARD_LEATHER_1743, Items.STUDDED_BODY_1133, 1, 41, 40.0, studded = true, type = Type.STUDDED),
        STUDDED_CHAPS(Items.HARD_LEATHER_1743, Items.STUDDED_CHAPS_1097, 1, 44, 42.0, studded = true, type = Type.STUDDED),
        SNAKESKIN_BODY(Items.SNAKESKIN_6289, Items.SNAKESKIN_BODY_6322, 15, 53, 55.0, type = Type.SNAKESKIN),
        SNAKESKIN_CHAPS(Items.SNAKESKIN_6289, Items.SNAKESKIN_CHAPS_6324, 12, 51, 50.0, type = Type.SNAKESKIN),
        SNAKESKIN_VAMBRACES(Items.SNAKESKIN_6289, Items.SNAKESKIN_VBRACE_6330, 8, 47, 35.0, pair = true, type = Type.SNAKESKIN),
        SNAKESKIN_BANDANA(Items.SNAKESKIN_6289, Items.SNAKESKIN_BANDANA_6326, 5, 48, 45.0, type = Type.SNAKESKIN),
        SNAKESKIN_BOOTS(Items.SNAKESKIN_6289, Items.SNAKESKIN_BOOTS_6328, 6, 45, 30.0, pair = true, type = Type.SNAKESKIN),
        GREEN_DHIDE_VAMBRACES(Items.GREEN_D_LEATHER_1745, Items.GREEN_DHIDE_VAMB_1065, 1, 57, 62.0, pair = true, type = Type.DRAGON),
        GREEN_DHIDE_CHAPS(Items.GREEN_D_LEATHER_1745, Items.GREEN_DHIDE_CHAPS_1099, 2, 60, 124.0, type = Type.DRAGON),
        GREEN_DHIDE_BODY(Items.GREEN_D_LEATHER_1745, Items.GREEN_DHIDE_BODY_1135, 3, 63, 186.0, type = Type.DRAGON),
        BLUE_DHIDE_VAMBRACES(Items.BLUE_D_LEATHER_2505, Items.BLUE_DHIDE_VAMB_2487, 1, 66, 70.0, pair = true, type = Type.DRAGON),
        BLUE_DHIDE_CHAPS(Items.BLUE_D_LEATHER_2505, Items.BLUE_DHIDE_CHAPS_2493, 2, 68, 140.0, type = Type.DRAGON),
        BLUE_DHIDE_BODY(Items.BLUE_D_LEATHER_2505, Items.BLUE_DHIDE_BODY_2499, 3, 71, 210.0, type = Type.DRAGON),
        RED_DHIDE_VAMBRACES(Items.RED_DRAGON_LEATHER_2507, Items.RED_DHIDE_VAMB_2489, 1, 73, 78.0, pair = true, type = Type.DRAGON),
        RED_DHIDE_CHAPS(Items.RED_DRAGON_LEATHER_2507, Items.RED_DHIDE_CHAPS_2495, 2, 73, 156.0, type = Type.DRAGON),
        RED_DHIDE_BODY(Items.RED_DRAGON_LEATHER_2507, Items.RED_DHIDE_BODY_2501, 3, 77, 234.0, type = Type.DRAGON),
        BLACK_DHIDE_VAMBRACES(Items.BLACK_D_LEATHER_2509, Items.BLACK_DHIDE_VAMB_2491, 1, 79, 86.0, pair = true, type = Type.DRAGON),
        BLACK_DHIDE_CHAPS(Items.BLACK_D_LEATHER_2509, Items.BLACK_DHIDE_CHAPS_2497, 2, 82, 172.0, type = Type.DRAGON),
        BLACK_DHIDE_BODY(Items.BLACK_D_LEATHER_2509, Items.BLACK_DHIDE_BODY_2503, 3, 84, 258.0, type = Type.DRAGON),
        YAK_BODY(Items.CURED_YAK_HIDE_10820, Items.YAK_HIDE_ARMOUR_10822, 2, 46, 32.0, type = Type.YAK),
        YAK_LEGS(Items.CURED_YAK_HIDE_10820, Items.YAK_HIDE_ARMOUR_10824, 1, 43, 32.0, type = Type.YAK),
        SPIKED_VAMBRACES_LEATHER(Items.LEATHER_VAMBRACES_1063, Items.SPIKY_VAMBRACES_10077, 1, 32, 6.0, type = Type.SPIKED),
        SPIKED_VAMBRACES_GREEN(Items.GREEN_DHIDE_VAMB_1065, Items.GREEN_SPIKY_VAMBS_10079, 1, 32, 6.0, type = Type.SPIKED),
        SPIKED_VAMBRACES_BLUE(Items.BLUE_DHIDE_VAMB_2487, Items.BLUE_SPIKY_VAMBS_10081, 1, 32, 6.0, type = Type.SPIKED),
        SPIKED_VAMBRACES_RED(Items.RED_DHIDE_VAMB_2489, Items.RED_SPIKY_VAMBS_10083, 1, 32, 6.0, type = Type.SPIKED),
        SPIKED_VAMBRACES_BLACK(Items.BLACK_DHIDE_VAMB_2491, Items.BLACK_SPIKY_VAMBS_10085, 1, 32, 6.0, type = Type.SPIKED);

        enum class Type {
            SOFT, HARD, STUDDED, SNAKESKIN, DRAGON, YAK, SPIKED
        }

        data class DiaryTask(val type: DiaryType, val stage: Int, val step: Int)

        companion object {
            fun forInput(input: Int) = values().filter { it.input == input }
            fun forProduct(product: Int) = values().find { it.product == product }
        }
    }

    /**
     * Represents the different types of spinning.
     */
    enum class Spinning(val button: Int, val need: Int, val product: Int, val level: Int, val exp: Double) {
        WOOL(19, Items.WOOL_1737, Items.BALL_OF_WOOL_1759, 1, 2.5),
        FLAX(17, Items.FLAX_1779, Items.BOW_STRING_1777, 10, 15.0),
        ROOT(23, Items.MAGIC_ROOTS_6051, Items.MAGIC_STRING_6038, 19, 30.0),
        ROOT_OAK(23, Items.OAK_ROOTS_6043, Items.MAGIC_STRING_6038, 19, 30.0),
        ROOT_WILLOW(23, Items.WILLOW_ROOTS_6045, Items.MAGIC_STRING_6038, 19, 30.0),
        ROOT_MAPLE(23, Items.MAPLE_ROOTS_6047, Items.MAGIC_STRING_6038, 19, 30.0),
        ROOT_YEW(23, Items.YEW_ROOTS_6049, Items.MAGIC_STRING_6038, 19, 30.0),
        ROOT_SPIRIT(23, Items.SPIRIT_ROOTS_6053, Items.MAGIC_STRING_6038, 19, 30.0),
        SINEW(27, Items.SINEW_9436, Items.CROSSBOW_STRING_9438, 10, 15.0),
        TREE_ROOTS(31, Items.OAK_ROOTS_6043, Items.CROSSBOW_STRING_9438, 10, 15.0),
        YAK(35, Items.HAIR_10814, Items.ROPE_954, 30, 25.0);

        companion object {
            private val buttonMap: Map<Int, Spinning> = Spinning.values().associateBy { it.button }
            fun forId(id: Int): Spinning? = buttonMap[id]
        }
    }

    /**
     * Represents the different types of tanning.
     */
    enum class Tan(val item: Int, val product: Int, val button: Int, val costPerItem: Int) {
        SOFT_LEATHER(Items.COWHIDE_1739, Items.LEATHER_1741, 1, 1),
        HARD_LEATHER(Items.COWHIDE_1739, Items.HARD_LEATHER_1743, 2, 3),
        SNAKESKIN(Items.SNAKE_HIDE_6287, Items.SNAKESKIN_6289, 3, 20),
        SNAKESKIN2(Items.SNAKE_HIDE_7801, Items.SNAKESKIN_6289, 4, 15),
        GREEN_DRAGONHIDE(Items.GREEN_DRAGONHIDE_1753, Items.GREEN_D_LEATHER_1745, 5, 20),
        BLUE_DRAGONHIDE(Items.BLUE_DRAGONHIDE_1751, Items.BLUE_D_LEATHER_2505, 6, 20),
        RED_DRAGONHIDE(Items.RED_DRAGONHIDE_1749, Items.RED_DRAGON_LEATHER_2507, 7, 20),
        BLACK_DRAGONHIDE(Items.BLACK_DRAGONHIDE_1747, Items.BLACK_D_LEATHER_2509, 8, 20);

        companion object {
            private val buttonMap = values().associateBy { it.button }

            fun forId(id: Int): Tan? = buttonMap[id]

            fun open(player: Player, npc: Int) {
                player.interfaceManager.open(Component(Components.TANNER_324))
            }

            fun tan(player: Player, amount: Int, def: Tan) {
                val availableAmount = minOf(amount, player.inventory.getAmount(Item(def.item)))
                if (availableAmount <= 0) {
                    sendMessage(player, "You don't have any ${getItemName(def.item).lowercase()} to tan.")
                    return
                }

                val coinsRequired = def.costPerItem * availableAmount
                if (!inInventory(player, Items.COINS_995, coinsRequired)) {
                    sendMessage(player, "You don't have enough coins to tan that many.")
                    return
                }

                val removed = removeItem(player, Item(Items.COINS_995, coinsRequired)) &&
                        removeItem(player, Item(def.item, availableAmount))
                if (!removed) {
                    sendMessage(player, "You don't have enough coins to tan that many.")
                    return
                }

                addItem(player, def.product, availableAmount)
                val itemName = getItemName(def.item).lowercase()
                sendMessage(
                    player,
                    "The tanner tans ${if (availableAmount > 1) "$availableAmount ${itemName}s" else itemName} for you."
                )

                if (def == SOFT_LEATHER) finishDiaryTask(player, DiaryType.LUMBRIDGE, 1, 2)
            }
        }
    }

    /**
     * Represents the pottery data.
     */
    enum class Pottery(val unfinished: Item, val product: Item, val level: Int, val exp: Double, val fireExp: Double) {
        POT(Item(Items.UNFIRED_POT_1787), Item(Items.EMPTY_POT_1931), 1, 6.3, 6.3),
        DISH(Item(Items.UNFIRED_PIE_DISH_1789), Item(Items.PIE_DISH_2313), 7, 15.0, 10.0),
        BOWL(Item(Items.UNFIRED_BOWL_1791), Item(Items.BOWL_1923), 8, 18.0, 15.0),
        PLANT(Item(Items.UNFIRED_PLANT_POT_5352), Item(Items.PLANT_POT_5350), 19, 20.0, 17.5),
        LID(Item(Items.UNFIRED_POT_LID_4438), Item(Items.POT_LID_4440), 25, 20.0, 20.0),
        ;

        companion object {
            private val unfinishedMap: Map<Int, Pottery> = Pottery.values().associateBy { it.unfinished.id }
            fun forId(id: Int): Pottery? = unfinishedMap[id]
        }
    }

    /**
     * Represents weaving items.
     */
    enum class Weaving(val product: Item, val required: Item, val level: Int, val experience: Double) {
        SACK(Item(Items.EMPTY_SACK_5418), Item(Items.JUTE_FIBRE_5931, 4), 21, 38.0),
        BASKET(Item(Items.BASKET_5376), Item(Items.WILLOW_BRANCH_5933, 6), 36, 56.0),
        CLOTH(Item(Items.STRIP_OF_CLOTH_3224), Item(Items.BALL_OF_WOOL_1759, 4), 10, 12.0),
    }

    /**
     * Represents the snelm products.
     */
    enum class SnelmItem(val shell: Int, val product: Int) {
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
            private val shellMap = SnelmItem.values().associateBy { it.shell }
            val SHELLS = SnelmItem.values().map { it.shell }.toIntArray()
            fun fromShellId(id: Int): SnelmItem? = shellMap[id]
        }
    }

    /**
     * Represents the feather headdress items.
     */
    enum class FeatherHeaddress(val base: Int, val product: Int) {
        FEATHER_HEADDRESS_BLUE(Items.BLUE_FEATHER_10089, Items.FEATHER_HEADDRESS_12210),
        FEATHER_HEADDRESS_ORANGE(Items.ORANGE_FEATHER_10091, Items.FEATHER_HEADDRESS_12222),
        FEATHER_HEADDRESS_RED(Items.RED_FEATHER_10088, Items.FEATHER_HEADDRESS_12216),
        FEATHER_HEADDRESS_STRIPY(Items.STRIPY_FEATHER_10087, Items.FEATHER_HEADDRESS_12219),
        FEATHER_HEADDRESS_YELLOW(Items.YELLOW_FEATHER_10090, Items.FEATHER_HEADDRESS_12213);

        companion object {
            val baseToHeaddressMap = values().associateBy { it.base }
            val baseIds = values().map { it.base }.toIntArray()
            fun forBase(baseId: Int): FeatherHeaddress? = baseToHeaddressMap[baseId]
        }
    }

    /**
     * Represents the battlestaves.
     */
    enum class Battlestaff(val required: Int, val productId: Int, val amount: Int = 1, val requiredLevel: Int, val experience: Double) {
        WATER_BATTLESTAFF(Items.WATER_ORB_571, Items.WATER_BATTLESTAFF_1395, requiredLevel = 54, experience = 100.0),
        EARTH_BATTLESTAFF(Items.EARTH_ORB_575, Items.EARTH_BATTLESTAFF_1399, requiredLevel = 58, experience = 112.5),
        FIRE_BATTLESTAFF(Items.FIRE_ORB_569, Items.FIRE_BATTLESTAFF_1393, requiredLevel = 62, experience = 125.0),
        AIR_BATTLESTAFF(Items.AIR_ORB_573, Items.AIR_BATTLESTAFF_1397, requiredLevel = 66, experience = 137.5), ;

        companion object {
            private val requiredMap: Map<Int, Battlestaff> = Battlestaff.values().associateBy { it.required }
            val BATTLESTAFF_ID = Items.BATTLESTAFF_1391
            val ORB_ID = Battlestaff.values().map { it.required }.toIntArray()

            @JvmStatic
            fun forId(itemId: Int): Battlestaff? = requiredMap[itemId]
        }
    }

    /**
     * Represents light sources.
     */
    enum class LightSources(val level: Int, val emptyId: Int, val fullId: Int = -1, val litId: Int, val sfxId: Int = -1, val open: Boolean = false, val interfaceId: Int = -1) {
        CANDLE(1, 0, Items.CANDLE_36, Items.LIT_CANDLE_33, Sounds.SKILL_LIGHT_CANDLE_3226, true, Components.DARKNESS_MEDIUM_98),
        BLACK_CANDLE(1, 0, Items.BLACK_CANDLE_38, Items.LIT_BLACK_CANDLE_32, Sounds.SKILL_LIGHT_CANDLE_3226, true, Components.DARKNESS_MEDIUM_98),
        TORCH(1, 0, Items.UNLIT_TORCH_596, Items.LIT_TORCH_594, Sounds.SLUG_TORCH_LIT_3028, true, Components.DARKNESS_MEDIUM_98),
        CANDLE_LANTERN(4, Items.CANDLE_LANTERN_4527, Items.CANDLE_LANTERN_4529, Items.CANDLE_LANTERN_4531, Sounds.LIGHT_CANDLE_2305, true, Components.DARKNESS_MEDIUM_98),
        CANDLE_LANTERN_BLACK(4, Items.CANDLE_LANTERN_4527, Items.CANDLE_LANTERN_4532, Items.CANDLE_LANTERN_4534, Sounds.LIGHT_CANDLE_2305, true, Components.DARKNESS_MEDIUM_98),

        OIL_LAMP(12, Items.OIL_LAMP_4525, Items.OIL_LAMP_4522, Items.OIL_LAMP_4524, Sounds.LIGHT_CANDLE_2305, true, Components.DARKNESS_LIGHT_97),
        OIL_LANTERN(26, Items.OIL_LANTERN_4535, Items.OIL_LANTERN_4537, Items.OIL_LANTERN_4539, Sounds.LIGHT_CANDLE_2305, false, Components.DARKNESS_LIGHT_97),
        SAPPHIRE_LANTERN(49, Items.SAPPHIRE_LANTERN_4700, Items.SAPPHIRE_LANTERN_4701, Items.SAPPHIRE_LANTERN_4702, Sounds.LIGHT_CANDLE_2305, false, -1),
        MINING_HELMET(65, 0, Items.MINING_HELMET_5014, Items.MINING_HELMET_5013, Sounds.LIGHT_CANDLE_2305, false, Components.DARKNESS_LIGHT_97),

        BULLSEYE_LANTERN(49, Items.BULLSEYE_LANTERN_4546, Items.BULLSEYE_LANTERN_4548, Items.BULLSEYE_LANTERN_4550, Sounds.LIGHT_CANDLE_2305, false, -1),
        EMERALD_LANTERN(49, Items.EMERALD_LANTERN_9064, Items.EMERALD_LANTERN_9064, Items.EMERALD_LANTERN_9065, Sounds.LIGHT_CANDLE_2305, false, -1),

        HEADBAND_1(1, -1, -1, Items.SEERS_HEADBAND_1_14631, -1, false, Components.DARKNESS_MEDIUM_98),
        HEADBAND_2(1, -1, -1, Items.SEERS_HEADBAND_2_14640, -1, false, Components.DARKNESS_LIGHT_97),
        HEADBAND_3(1, -1, -1, Items.SEERS_HEADBAND_3_14641, -1, false, -1),
        GLOWING_FUNGUS(1, -1, -1, Items.GLOWING_FUNGUS_4075, -1, false, -1);

        val strength: Int
            get() = when (interfaceId) {
                Components.DARKNESS_LIGHT_97 -> 1
                Components.DARKNESS_MEDIUM_98 -> 2
                -1 -> 3
                else -> Components.DARKNESS_DARK_96
            }

        fun isPermanent(): Boolean = this in listOf(HEADBAND_1, HEADBAND_2, HEADBAND_3, GLOWING_FUNGUS)

        companion object {
            private val byRaw: Map<Int, LightSources> = values().flatMap { listOf(it.emptyId, it.fullId).filter { id -> id > 0 }.map { id -> id to it } }.toMap()
            private val byLit: Map<Int, LightSources> = values().associateBy { it.litId }

            @JvmStatic
            fun forId(id: Int): LightSources? = byRaw[id]

            @JvmStatic
            fun forLitId(id: Int): LightSources? = byLit[id]

            @JvmStatic
            fun hasActiveLightSource(player: Player): Boolean = getActiveLightSource(player) != null

            @JvmStatic
            fun getAnyActiveLightSource(player: Player): LightSources? =
                values().firstOrNull { inInventory(player, it.litId) || inEquipment(player, it.litId) }

            @JvmStatic
            fun getActiveLightSource(player: Player): LightSources? =
                values().firstOrNull { !it.isPermanent() && (inInventory(player, it.litId) || inEquipment(player, it.litId)) }
        }
    }

    /**
     * Checks if the player has a thread charged at or above 1004.
     */
    @JvmStatic
    fun isLastThread(player: Player): Boolean = getThread(player)?.charge?.let { it >= 1004 } ?: false

    /**
     * Increases the thread's charge by 1 if present.
     */
    @JvmStatic
    fun decayThread(player: Player) {
        getThread(player)?.let { thread ->
            thread.charge += 1
        }
    }

    /**
     * Removes one thread item from player inventory and sets charge to 1000.
     */
    @JvmStatic
    fun removeThread(player: Player) {
        if (removeItem(player, Item(Items.THREAD_1734, 1))) {
            sendMessage(player, "You use up one of your reels of thread.")
            getThread(player)?.charge = 1000
        }
    }

    /**
     * Gets the thread item from player inventory, or null if not found.
     */
    @JvmStatic
    fun getThread(player: Player): Item? = player.inventory[player.inventory.getSlot(Item(Items.THREAD_1734, 1))]


    val CRAB_ITEM_IDS = mapOf(
        Items.FRESH_CRAB_CLAW_7536 to Pair(Items.CRAB_CLAW_7537, 32.5),
        Items.FRESH_CRAB_SHELL_7538 to Pair(Items.CRAB_HELMET_7539, 32.5)
    )

    val TRIBAL_ITEM_IDS = mapOf(
        Items.TRIBAL_MASK_6335 to Items.BROODOO_SHIELD_10_6215,
        Items.TRIBAL_MASK_6337 to Items.BROODOO_SHIELD_10_6237,
        Items.TRIBAL_MASK_6339 to Items.BROODOO_SHIELD_10_6259
    )

    val UNFIRED_POTTERY_ITEM_IDS = intArrayOf(
        Items.UNFIRED_POT_1787,
        Items.UNFIRED_PIE_DISH_1789,
        Items.UNFIRED_BOWL_1791,
        Items.UNFIRED_PLANT_POT_5352,
        Items.UNFIRED_POT_LID_4438
    )

    val LIGHTABLE_ITEM_IDS = intArrayOf(
        // Brightness 1
        Items.CANDLE_36,               // candle
        Items.BLACK_CANDLE_38,         // black candle
        Items.UNLIT_TORCH_596,         // torch
        Items.CANDLE_LANTERN_4529,     // candle lantern (full)
        Items.CANDLE_LANTERN_4532,     // black candle lantern (full)

        // Brightness 2
        Items.OIL_LAMP_4522,           // oil lamp (full)
        Items.OIL_LANTERN_4537,        // oil lantern (full)
        Items.SAPPHIRE_LANTERN_4701,   // sapphire lantern (full)
        Items.MINING_HELMET_5014,      // mining helmet

        // Brightness 3
        Items.BULLSEYE_LANTERN_4548,   // bullseye lantern (full)
        Items.EMERALD_LANTERN_9064     // emerald lantern (full)
    )
}
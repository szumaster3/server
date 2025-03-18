package content.custom.handlers

import core.api.*
import core.api.item.itemDefinition
import core.game.container.access.InterfaceContainer
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.quest.QuestReq
import core.game.node.entity.player.link.quest.QuestRequirements
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.*
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * The lost property shop.
 */
class LostPropertyInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.LOST_PROPERTY_834) { player, _ ->
            val itemsToDisplay = LostPropertyStock.values().map { Item(it.item, 1) }.toTypedArray()
            InterfaceContainer.generateItems(player, itemsToDisplay, arrayOf("Value", "Buy"), 834, 16, 6, 6, 9999)
            return@onOpen true
        }

        on(Components.LOST_PROPERTY_834) { player, _, op, _, slot, _ ->
            val lostItem = LostPropertyStock.itemsMap[slot]
            if (lostItem == null) {
                SystemLogger.processLogEntry(this.javaClass, Log.WARN, "Good moaning everyone, look who was pissing by The Ship today.")
                return@on true
            }
            when (op) {
                196 -> sendMessage(player, lostItem.value)
                9 -> sendMessage(player, itemDefinition(lostItem.item).examine)
                155 -> {
                    val price = lostItem.currency
                    // Quest requirements for purchase.
                    if (lostItem.reqs != null && !lostItem.reqs?.let { QuestReq(it) }?.let { hasRequirement(player, it, false) }!!) {
                        sendDialogue(player, "You must complete $DARK_RED${lostItem.reqs.questName} Quest</col> to purchase this.")
                        return@on true
                    }
                    // Diary requirements for purchase.
                    if (isDiaryIncomplete(player, lostItem)) return@on true
                    // Mini-quest requirements for purchase.
                    if (lostItem.otherReqs != null && !getAttribute(player, lostItem.otherReqs, false)) {
                        sendDialogue(player, "You must complete a$DARK_RED Curse of Zaros</col> to purchase this.")
                        return@on true
                    }

                    // Skill requirements for purchase.
                    if (lostItem.item == Items.AVAS_ACCUMULATOR_10499 && getStatLevel(player, Skills.RANGE) < 50) {
                        sendDialogue(player, "You need at least$DARK_BLUE 50 range level</col> to purchase this.")
                        return@on true
                    }

                    if (!inInventory(player, Items.COINS_995, price)) {
                        sendMessage(player, "You don't have enough coins.")
                        return@on true
                    }
                    if (freeSlots(player) == 0) {
                        sendMessage(player, "You don't have enough inventory space.")
                        return@on true
                    }
                    removeItem(player, Item(Items.COINS_995, price))
                    addItem(player, lostItem.item, 1)
                    val itemName = getItemName(lostItem.item).lowercase()
                    sendMessage(player, "You purchased the $itemName.")
                    return@on true
                }
            }

            return@on true
        }
    }

    private fun isDiaryIncomplete(
        player: Player,
        lostItem: LostPropertyStock,
    ): Boolean {
        val diaryRequirements =
            mapOf(
                Items.FALADOR_SHIELD_3_14579 to
                    Pair(DiaryType.FALADOR, "You need to complete the$DARK_RED Falador diary (level 2)</col> to purchase this."),
                Items.FREMENNIK_SEA_BOOTS_3_14573 to
                    Pair(DiaryType.FREMENNIK, "You need to complete the$DARK_RED Fremennik diary (level 2)</col> to purchase this."),
                Items.EXPLORERS_RING_3_13562 to
                    Pair(DiaryType.LUMBRIDGE, "You need to complete the$DARK_RED Lumbridge diary (level 2)</col> to purchase this."),
                Items.VARROCK_ARMOUR_3_11758 to
                    Pair(DiaryType.VARROCK, "You need to complete the$DARK_RED Varrock diary (level 2)</col> to purchase this."),
            )

        val requirement = diaryRequirements[lostItem.item] ?: return false
        val (diaryType, message) = requirement

        if (!isDiaryComplete(player, diaryType, 2)) {
            sendDialogue(player, message)
            return true
        }

        return false
    }
}

/**
 * Lost Property stock.
 *
 * @param item      The item.
 * @param currency  The gold coins.
 * @param slot      The slot number.
 * @param value     The value of item.
 */
enum class LostPropertyStock(
    val item: Int,
    val currency: Int,
    val slot: Int,
    val value: String,
    val reqs: QuestRequirements? = null,
    val otherReqs: String? = null,
) {
    Ectophial(Items.ECTOPHIAL_4251, 4600, 0, "The Ectophial costs 4600 coins.", QuestRequirements.GHOSTS_AHOY),
    GuthixCape(Items.GUTHIX_CAPE_2413, 23000, 1, "The Guthix Cape costs 23000 coins."),
    BookOfBalance(Items.BOOK_OF_BALANCE_3844, 12000, 2, "The Book of Balance costs 12000 coins.", QuestRequirements.HORROR_DEEP),
    HolyBook(Items.HOLY_BOOK_3840, 12000, 3, "The Holy Book costs 12000 coins.", QuestRequirements.HORROR_DEEP), // GameAttributes.GOD_BOOKS
    KaramjaGloves(Items.KARAMJA_GLOVES_3_11140, 200, 4, "Karamja Gloves cost 200 coins."),
    FaladorShield(Items.FALADOR_SHIELD_3_14579, 200, 5, "Falador Shield costs 200 coins."),
    FremennikSeaBoots(Items.FREMENNIK_SEA_BOOTS_3_14573, 200, 6, "Fremennik Sea Boots cost 200 coins."),
    ExplorersRing(Items.EXPLORERS_RING_3_13562, 200, 7, "Explorer's Ring costs 200 coins."),
    VarrockArmour(Items.VARROCK_ARMOUR_3_11758, 200, 8, "Varrock Armour costs 200 coins."),
    GhostlyHood(Items.GHOSTLY_HOOD_6109, 15000, 9, "Ghostly Hood costs 15000 coins.", null, "zaros:complete"),
    GhostlyRobeTop(Items.GHOSTLY_ROBE_6107, 45000, 10, "Ghostly Robe Top costs 45000 coins.", null, "zaros:complete"),
    GhostlyRobeBottom(Items.GHOSTLY_ROBE_6108, 43000, 11, "Ghostly Robe Bottom costs 43000 coins.", null, "zaros:complete"),
    GhostlyGloves(Items.GHOSTLY_GLOVES_6110, 13500, 12, "Ghostly Gloves cost 13500 coins.", null, "zaros:complete"),
    GhostlyBoots(Items.GHOSTLY_BOOTS_6106, 13500, 13, "Ghostly Boots cost 13500 coins.", null, "zaros:complete"),
    GhostlyCloak(Items.GHOSTLY_CLOAK_6111, 15000, 14, "Ghostly Cloak costs 15000 coins.", null, "zaros:complete"),
    LunarStaff(Items.LUNAR_STAFF_9084, 60000, 15, "Lunar Staff costs 60000 coins.", QuestRequirements.LUNAR_DIPLOMACY),
    LunarHelm(Items.LUNAR_HELM_9096, 22000, 16, "Lunar Helm costs 22000 coins.", QuestRequirements.LUNAR_DIPLOMACY),
    LunarTorso(Items.LUNAR_TORSO_9097, 140000, 17, "Lunar Torso costs 140000 coins.", QuestRequirements.LUNAR_DIPLOMACY),
    LunarLegs(Items.LUNAR_LEGS_9098, 95000, 18, "Lunar Legs cost 95000 coins.", QuestRequirements.LUNAR_DIPLOMACY),
    LunarGloves(Items.LUNAR_GLOVES_9099, 15000, 19, "Lunar Gloves cost 15000 coins.", QuestRequirements.LUNAR_DIPLOMACY),
    LunarBoots(Items.LUNAR_BOOTS_9100, 15000, 20, "Lunar Boots cost 15000 coins.", QuestRequirements.LUNAR_DIPLOMACY),
    LunarCape(Items.LUNAR_CAPE_9101, 18000, 21, "Lunar Cape costs 18000 coins.", QuestRequirements.LUNAR_DIPLOMACY),
    LunarAmulet(Items.LUNAR_AMULET_9102, 9000, 22, "Lunar Amulet costs 9000 coins.", QuestRequirements.LUNAR_DIPLOMACY),
    LunarRing(Items.LUNAR_RING_9104, 8000, 23, "Lunar Ring costs 8000 coins.", QuestRequirements.LUNAR_DIPLOMACY),
    BarrelchestAnchor(
        Items.BARRELCHEST_ANCHOR_10887,
        290000,
        24,
        "Barrelchest Anchor costs 290000 coins.",
        QuestRequirements.BRAIN_ROBBERY,
    ),
    BootsOfLightness(Items.BOOTS_OF_LIGHTNESS_88, 9800, 25, "Boots of Lightness cost 9800 coins.", QuestRequirements.IKOV),
    CapeOfLegends(Items.CAPE_OF_LEGENDS_1052, 1900, 26, "Cape of Legends costs 1900 coins.", QuestRequirements.LEGEND),
    Keris(Items.KERIS_10581, 25000, 27, "Keris costs 25000 coins.", QuestRequirements.CONTACT),
    Bearhead(Items.BEARHEAD_4502, 10000, 28, "Bearhead costs 10000 coins.", QuestRequirements.MOUNTAIN_DAUGHTER),
    BeaconRing(Items.BEACON_RING_11014, 6000, 29, "Beacon Ring costs 6000 coins.", QuestRequirements.WHAT_LIES_BELOW),
    DwarvenHelmet(Items.DWARVEN_HELMET_11200, 115000, 30, "Dwarven Helmet costs 115000 coins.", QuestRequirements.GRIM_TALES),
    HelmOfNeitiznot(Items.HELM_OF_NEITIZNOT_10828, 95000, 31, "Helm of Neitiznot costs 95000 coins.", QuestRequirements.FREM_TRIALS),
    IvandisFlail(Items.IVANDIS_FLAIL28_13119, 30000, 32, "Ivandis Flail costs 30000 coins.", QuestRequirements.SEERGAZE),
    AvasAttractor(Items.AVAS_ATTRACTOR_10498, 7000, 33, "Ava's Attractor costs 7000 coins.", QuestRequirements.ANMA),
    AvasAccumulator(Items.AVAS_ACCUMULATOR_10499, 7000, 34, "Ava's Accumulator costs 7000 coins.", QuestRequirements.ANMA),
    ;

    companion object {
        val itemsMap: MutableMap<Int, LostPropertyStock> = mutableMapOf()

        init {
            for (exchange in values()) {
                itemsMap[exchange.slot] = exchange
            }
        }
    }
}

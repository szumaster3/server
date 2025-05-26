package content.global.skill.summoning.items

import content.global.skill.summoning.SummoningScroll
import content.region.asgarnia.dialogue.taverley.PikkupstixDialogueExtension
import core.api.*
import core.api.quest.isQuestComplete
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class EnchantedHeadgearListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.PIKKUPSTIX_6970, IntType.NPC, "Enchant") { player, _ ->
            if (!isQuestComplete(player, Quests.WOLF_WHISTLE)) {
                sendMessage(player, "You need to complete Wolf Whistle to Enchant the headgear.")
                return@on true
            }
            sendNPCDialogue(player, NPCs.PIKKUPSTIX_6970, "What would you like disenchanted or enchanted?")
            addDialogueAction(player) { _, _ ->
                sendItemSelect(player, "Choose") { slot, index ->
                    val item = player.inventory[slot] ?: return@sendItemSelect
                    enchant(player, item, index)
                }
            }
            return@on true
        }

        on(chargedIDs, IntType.ITEM, "Uncharge") { player, node ->
            val item = node.asItem() ?: return@on true
            val headgear = EnchantedHeadgear.forItem(item) ?: return@on true
            clearScrolls(player, item)
            sendMessages(player, "You remove the scrolls. You will need to use a Summoning scroll on it to charge the", "headgear up once more.")
            replaceSlot(player, item.index, headgear.enchantedItem)
            return@on true
        }

        on(chargedIDs, IntType.ITEM, "Commune") { player, node ->
            val item = node.asItem() ?: return@on true
            val scrolls = getScrolls(player, item)
            if (scrolls.isEmpty()) {
                sendDialogue(player, "There are no scrolls stored in this headgear.")
            } else {
                val desc = scrolls.entries.joinToString(", ") { (scrollId, amount) ->
                    "${amount}x ${SummoningScroll.forId(scrollId)?.name ?: "Unknown scroll"}"
                }
                sendDialogue(player, "The headgear contains: $desc")
            }
            return@on true
        }

        onUseWith(IntType.NPC, allowedScrollIDs, *enchantIDs) { player, used, with ->
            val item = used.asItem() ?: return@onUseWith true
            val scrollItem = with.asItem() ?: return@onUseWith true

            val headgear = EnchantedHeadgear.forItem(item) ?: return@onUseWith true

            if (!isSummoningScroll(scrollItem)) return@onUseWith false

            if (!canStoreScroll(scrollItem.id)) {
                sendMessage(player, "This scroll cannot be stored in enchanted headgear.")
                return@onUseWith true
            }

            val currentScrolls = getScrolls(player, item)
            val totalStored = currentScrolls.values.sum()
            val capacityLeft = headgear.scrollCapacity - totalStored

            if (capacityLeft <= 0) {
                sendMessage(player, "Your headgear cannot hold any more scrolls.")
                return@onUseWith true
            }

            if (getStatLevel(player, Skills.SUMMONING) < headgear.requiredLevel) {
                sendMessage(player, "You need a Summoning level of ${headgear.requiredLevel} to enchant this headgear.")
                return@onUseWith true
            }

            val scrollsAmount = capacityLeft.coerceAtMost(scrollItem.amount)

            repeat(scrollsAmount) {
                addScrollToHeadgear(player, item, scrollItem.id)
            }

            removeItem(player, Item(scrollItem.id, scrollsAmount))

            if (item.id != headgear.chargedItem.id) {
                replaceSlot(player, item.index, headgear.chargedItem)
            }

            sendMessage(player, "You add $scrollsAmount scroll${if (scrollsAmount > 1) "s" else ""} to the enchanted headgear.")
            return@onUseWith true
        }
    }

    companion object {
        /**
         * Array of item ids representing enchanted headgear items that can be enchanted.
         */
        private val enchantIDs = EnchantedHeadgear.values().map { it.enchantedItem.id }.toIntArray()

        /**
         * Array of item ids representing charged enchanted headgear items.
         */
        private val chargedIDs = EnchantedHeadgear.values().map { it.chargedItem.id }.toIntArray()

        /**
         * Set of allowed scroll ids that can be stored inside enchanted headgear.
         */
        private val allowedScrollIDs = intArrayOf(
            Items.HOWL_SCROLL_12425,
            Items.DREADFOWL_STRIKE_SCROLL_12445,
            Items.SLIME_SPRAY_SCROLL_12459,
            Items.PESTER_SCROLL_12838,
            Items.ELECTRIC_LASH_SCROLL_12460, // Electric lash
            Items.FIREBALL_ASSAULT_SCROLL_12839, // Fireball assault
            Items.SANDSTORM_SCROLL_12446, // Sandstorm
            Items.VAMPIRE_TOUCH_SCROLL_12447, // Vampyre touch
            Items.BRONZE_BULL_RUSH_SCROLL_12461, // Bronze bull rush
            Items.EVIL_FLAMES_SCROLL_12448, // Evil flames
            Items.PETRIFYING_GAZE_SCROLL_12458, // Petrifying gaze
            Items.IRON_BULL_RUSH_SCROLL_12462, // Iron bull rush
            Items.ABYSSAL_DRAIN_SCROLL_12454, // Abyssal drain
            Items.DISSOLVE_SCROLL_12453, // Dissolve
            Items.AMBUSH_SCROLL_12836, // Ambush
            Items.RENDING_SCROLL_12840, // Rending
            Items.DOOMSPHERE_SCROLL_12455, // Doomsphere
            Items.DUST_CLOUD_SCROLL_12468, // Dust cloud
            Items.STEEL_BULL_RUSH_SCROLL_12463, // Steel bull rush
            Items.POISONOUS_BLAST_SCROLL_12467, // Poisonous blast
            Items.MITH_BULL_RUSH_SCROLL_12464, // Mithril bull rush
            Items.TOAD_BARK_SCROLL_12452, // Toad bark
            Items.FAMINE_SCROLL_12830, // Famine
            Items.ARCTIC_BLAST_SCROLL_12451, // Arctic blast
            Items.RISE_FROM_THE_ASHES_SCROLL_14622, // Rise from the ashes
            Items.CRUSHING_CLAW_SCROLL_12449, // Crushing claw
            Items.MANTIS_STRIKE_SCROLL_12450, // Mantis strike
            Items.INFERNO_SCROLL_12841, // Inferno
            Items.ADDY_BULL_RUSH_SCROLL_12465, // Adamant bull rush
            Items.DEADLY_CLAW_SCROLL_12831, // Deadly claw
            Items.ACORN_MISSILE_SCROLL_12457, // Acorn missile
            Items.SPIKE_SHOT_SCROLL_12456, // Spike shot
            Items.EBON_THUNDER_SCROLL_12837, // Ebon thunder
            Items.SWAMP_PLAGUE_SCROLL_12832, // Swamp plague
            Items.RUNE_BULL_RUSH_SCROLL_12466, // Rune bull rush
            Items.BOIL_SCROLL_12833,
            Items.IRON_WITHIN_SCROLL_12828,
            Items.STEEL_OF_LEGENDS_SCROLL_12825
        )

        /**
         * Checks if the given item is a summoning scroll that can be stored.
         * @param item the item to check
         * @return true if the item is a summoning scroll allowed to be stored, false otherwise
         */
        fun isSummoningScroll(item: Item): Boolean = allowedScrollIDs.contains(item.id)

        /**
         * Checks if the scroll id can be stored in enchanted headgear.
         * @param scrollId the scroll id to check
         * @return true if the scroll id is allowed to be stored, false otherwise
         */
        fun canStoreScroll(scrollId: Int): Boolean = allowedScrollIDs.contains(scrollId)

        /**
         * Maps player ids to a map of scroll IDs and their respective amounts stored in the enchanted headgear.
         */
        private val playerScrollStorage = mutableMapOf<Int, MutableMap<Int, Int>>()

        /**
         * Gets the stored scrolls for a player.
         * @param player the player whose scrolls to get
         * @param item the enchanted headgear item (not used currently but can be for future extensions)
         * @return a map of scroll ID to amount stored
         */
        fun getScrolls(player: Player, item: Item): Map<Int, Int> {
            return playerScrollStorage[player.id] ?: emptyMap()
        }

        /**
         * Clears all scrolls stored for a player.
         * @param player the player whose scrolls to clear
         * @param item the enchanted headgear item (not used currently but can be for future extensions)
         */
        fun clearScrolls(player: Player, item: Item) {
            playerScrollStorage.remove(player.id)
        }

        /**
         * Adds a scroll to the player's enchanted headgear storage.
         * @param player the player adding the scroll
         * @param item the enchanted headgear item
         * @param scrollId the scroll id to add
         */
        fun addScrollToHeadgear(player: Player, item: Item, scrollId: Int) {
            val map = playerScrollStorage.getOrPut(player.id) { mutableMapOf() }
            map[scrollId] = (map[scrollId] ?: 0) + 1
        }

        /**
         * Handles enchant process on the given headgear item.
         *
         * @param player the player enchanting the item
         * @param slot the item to enchant
         * @param option the option selected (e.g., dialog option)
         * @return true if enchantment succeeded, false otherwise
         */
        @JvmStatic
        fun enchant(player: Player, slot: Item, option: Int): Boolean {
            val item = EnchantedHeadgear.forItem(slot) ?: return false

            if (getStatLevel(player, Skills.SUMMONING) < item.requiredLevel) return false
            if (option == 1) lock(player, 1)
            animate(findLocalNPC(player, NPCs.PIKKUPSTIX_6970)!!, Animations.CAST_SPELL_711)
            sendGraphics(Graphics(1574, 100), player.location)

            return when {
                slot.id == item.defaultItem.id -> {
                    removeItem(player, item.defaultItem.id)
                    addItem(player, item.enchantedItem.id)
                    openDialogue(player, PikkupstixDialogueExtension())
                    true
                }
                else -> false
            }
        }
    }
}

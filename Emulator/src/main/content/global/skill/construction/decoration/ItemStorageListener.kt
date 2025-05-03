package content.global.skill.construction.decoration

import core.api.*
import core.api.item.allInInventory
import core.api.ui.sendInterfaceConfig
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import core.tools.YELLOW
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class ItemStorageListener : InterfaceListener, InteractionListener {

    override fun defineInterfaceListeners() {

        /*
         * Handles storage boxes.
         */

        on(INTERFACE) { player, _, _, buttonID, _, _ ->
            val setIndex = (buttonID - 56) / 2
            if (getAttribute(player, "con:bookcase", false)) {
                addItem(player, getBook(setIndex))
            } else if (getAttribute(player, "con:fancy-dress-box", false)) {
                if (setIndex in DRESS_BOX_CONTENT.indices) {
                    val c = (166..224 step 2)
                    val isHidden = 166 + (setIndex * 2)
                    sendInterfaceConfig(player, INTERFACE, c.first, false)
                    toggleFancyDressSet(player, setIndex)
                } else {
                    player.debug("Invalid costume set.")
                }
            }
            return@on true
        }

        /*
         * Remove previous interaction attribute.
         */

        onClose(INTERFACE) { player, _ ->
            removeAttributes(player, "con:fancy-dress-box", "con:bookcase")
            return@onClose true
        }
    }

    override fun defineListeners() {

        /*
         * Handles opening of fancy dress box.
         */

        on(DRESS_BOX_CLOSE, IntType.SCENERY, "open") { player, node ->
            animate(player, Animations.OPEN_CHEST_536)
            replaceScenery(node.asScenery(), node.id + 1, -1)
            return@on true
        }

        /*
         * Handles interaction with fancy dress box.
         */

        on(DRESS_BOX_OPEN, IntType.SCENERY, "search", "close") { player, node ->
            val option = getUsedOption(player)
            if (option == "close") {
                animate(player, Animations.CLOSE_CHEST_539)
                replaceScenery(node.asScenery(), node.id - 1, -1)
            } else {
                setAttribute(player, "con:fancy-dress-box", true)
                openInterface(player, INTERFACE).also {
                    sendString(player, "Fancy dress box", INTERFACE, 225)
                    sendString(player, "Mime costume", INTERFACE, 55)
                    sendString(player, "Royal frog costume", INTERFACE, 57)
                    sendString(player, "Frog mask", INTERFACE, 59)
                    sendString(player, "Zombie outfit", INTERFACE, 61)
                    sendString(player, "Camo outfit", INTERFACE, 63)
                    sendString(player, "Lederhosen outfit", INTERFACE, 65)
                    sendString(player, "Shade robes", INTERFACE, 67)
                    PacketRepository.send(ContainerPacket::class.java, ContainerContext(player, INTERFACE, 164, 30, DRESS_BOX_CONTENT.map { Item(it) }.toTypedArray(), false))
                }
            }
            return@on true
        }

        /*
         * Handles interaction with bookcase.
         */

        on(BOOKCASE, IntType.SCENERY, "search") { player, _ ->
            animate(player, Animations.USE_OBJECT_POH_3659)
            setAttribute(player, "con:bookcase", true)
            openInterface(player, INTERFACE).also {
                sendString(player, "Bookcase", INTERFACE, 225)
                BOOKCASE_CONTENT.forEachIndexed { index, (itemId, description) ->
                    sendString(player, "$YELLOW${getItemName(itemId)}</col> <br>$description", INTERFACE, 55 + index * 2)
                }
                PacketRepository.send(ContainerPacket::class.java, ContainerContext(player, INTERFACE, 164, 30, BOOKCASE_CONTENT.map { Item(it.first) }.toTypedArray(), false))
            }
            return@on true
        }

        /*
         * Handles use the restricted book on bookcase.
         */

        onUseWith(IntType.SCENERY, BOOKCASE_RESTRICTED_CONTENT, *BOOKCASE) { player, used, _ ->
            if (used.id in BOOKCASE_RESTRICTED_CONTENT) {
                sendMessage(player, "There doesn't seem to be space for that on the bookcase.")
            }
            return@onUseWith true
        }
    }

    companion object {
        private const val INTERFACE = 467
        // Fancy dress box.
        private val DRESS_BOX_CLOSE = intArrayOf(Scenery.FANCY_DRESS_BOX_18772, Scenery.FANCY_DRESS_BOX_18774, Scenery.FANCY_DRESS_BOX_18776)
        private val DRESS_BOX_OPEN = intArrayOf(Scenery.FANCY_DRESS_BOX_18773, Scenery.FANCY_DRESS_BOX_18775, Scenery.FANCY_DRESS_BOX_18777)
        private val DRESS_BOX_CONTENT = intArrayOf(Items.MIME_MASK_10629, Items.PRINCESS_BLOUSE_10630, Items.FROG_MASK_10721, Items.ZOMBIE_SHIRT_10631, Items.CAMO_TOP_10632, Items.LEDERHOSEN_TOP_10633, Items.SHADE_ROBE_10634)
        // Bookcase.
        private val BOOKCASE = intArrayOf(Scenery.BOOKCASE_13597, Scenery.BOOKCASE_13598, Scenery.BOOKCASE_13599)
        private val BOOKCASE_CONTENT = listOf(Items.ARENA_BOOK_6891 to "Magic Training Arena Lore Book", Items.MY_NOTES_11339 to "Discoveries from the ancient cavern beneath the lake", Items.CRUMBLING_TOME_4707 to "Legend of the Brothers", Items.PIE_RECIPE_BOOK_7162 to "Pie Recipes", Items.GIANNES_COOK_BOOK_2167 to "The collected recipes of Aluft Gianne", Items.GAME_BOOK_7681 to "Party Pete's Bumper Book of Games", Items.STRONGHOLD_NOTES_9004 to "Stronghold of Security - Notes", Items.COCKTAIL_GUIDE_2023 to "The Blurberry Cocktail Guide", Items.TARNS_DIARY_10587 to "The Diary of Tarn Razorlor", Items.CONSTRUCTION_GUIDE_8463 to "Guide to Construction", Items.GLASSBLOWING_BOOK_11656 to "Ultimate Guide to Glassblowing", Items.BREWIN_GUIDE_8989 to "Brewin' Guide", Items.SECURITY_BOOK_9003 to "${GameWorld.settings?.name} Account Security", Items.QUEEN_HELP_BOOK_10562 to "Queen Help", Items.ABYSSAL_BOOK_5520 to "Abyssal Research Notes", Items.EXPLORERS_NOTES_11677 to "Beyond Trollheim", Items.GOBLIN_BOOK_10999 to "The Book of the Big High War God (Another Slice of H.A.M.)", Items.DWARVEN_LORE_4568 to "The Arzinian Being of Bordanzan (Between a Rock...)", Items.BOOK_O_PIRACY_7144 to "The Little Book o' Piracy (Cabin Fever)", Items.CLOCKWORK_BOOK_10594 to "Clockwork Toys - Chapter 1.0 (Cold War)", Items.SCABARITE_NOTES_11975 to "'My Notes' (Dealing with Scabaras)", Items.TRANSLATION_4655 to "Translation Primer (Desert Treasure)", Items.BOOK_ON_CHEMICALS_711 to "Volatile Chemicals (Digsite)", Items.INSTRUCTION_MANUAL_5 to "Dwarf Multicannon Manual (Dwarf Cannon)", Items.BIRD_BOOK_10173 to "William Oddity's Guide to the Avian (Eagles' Peak)", Items.FEATHERED_JOURNAL_10179 to "The Journal of Arthur Artimus (Eagles' Peak)", Items.BATTERED_BOOK_2886 to "Book of the Elemental Shield (Elemental Workshop I)", Items.BEATEN_BOOK_9717 to "Book of the Elemental Helm (Elemental Workshop II)", Items.A_HANDWRITTEN_BOOK_9627 to "Crystal Singing for Beginners (The Eyes of Glouphrie)", Items.VARMENS_NOTES_4616 to "The Ruins of Uzer (The Golem)")
        private val BOOKCASE_RESTRICTED_CONTENT = intArrayOf(Items.HOLY_BOOK_3840, Items.DAMAGED_BOOK_3839, Items.BOOK_OF_BALANCE_3844, Items.DAMAGED_BOOK_3843, Items.UNHOLY_BOOK_3842, Items.DAMAGED_BOOK_3841, Items.STRANGE_BOOK_5507, Items.BOOK_ON_CHICKENS_7464, Items.BOOK_OF_FOLKLORE_5508, Items.PVP_WORLDS_MANUAL_14056)

        /**
         * Retrieves the first element of a book's details based on the provided id.
         *
         * @param id The book item id.
         * @return The first element if the `id` is valid; otherwise, returns `0`.
         */
        private fun getBook(id: Int): Int =
            if (id in 56..114 step 2) {
                BOOKCASE_CONTENT[(id - 56) / 2].first
            } else {
                0
            }

        /**
         * Generates an element based on the given `id` and the provided `items` array.
         *
         * @param id The identifier of the item to be generated.
         * @param items The `IntArray` containing the data used to generate the item.
         * @return The value from the `items` array corresponding to the given `id`, or `0` if the `id` is out of range.
         */
        private fun getItem(id: Int, items: IntArray): Int {
            return if (id in 56..114 step 2) {
                items[(id - 56) / 2]
            } else {
                0
            }
        }

        /**
         * Toggles a fancy sets.
         */
        private fun toggleFancyDressSet(player: Player, setIndex: Int) {
            if (setIndex !in DRESS_BOX_CONTENT.indices) {
                player.debug("Invalid outfit selection.")
                return
            }

            val set = when (DRESS_BOX_CONTENT[setIndex]) {
                // Set 1: Mime
                Items.MIME_MASK_10629 -> intArrayOf(Items.MIME_MASK_3057, Items.MIME_TOP_3058, Items.MIME_LEGS_3059, Items.MIME_BOOTS_3061, Items.MIME_GLOVES_3060)
                // Set 2: Royal costume (male/female).
                Items.PRINCESS_BLOUSE_10630 -> if (player.isMale)  intArrayOf(Items.PRINCE_TUNIC_6184, Items.PRINCE_LEGGINGS_6185) else intArrayOf(Items.PRINCESS_BLOUSE_6186, Items.PRINCESS_SKIRT_6187)
                // Set 3: Frog mask.
                Items.FROG_MASK_10721 -> intArrayOf(Items.FROG_MASK_6188)
                // Set 4. Zombie outfit.
                Items.ZOMBIE_SHIRT_10631 -> intArrayOf(Items.ZOMBIE_MASK_7594, Items.ZOMBIE_SHIRT_7592, Items.ZOMBIE_TROUSERS_7593, Items.ZOMBIE_GLOVES_7595, Items.ZOMBIE_BOOTS_7596)
                // Set 5. Camo costume.
                Items.CAMO_TOP_10632 -> intArrayOf(Items.CAMO_HELMET_6656, Items.CAMO_TOP_6654, Items.CAMO_BOTTOMS_6655)
                // Set 6. Lederhosen set.
                Items.LEDERHOSEN_TOP_10633 -> intArrayOf(Items.LEDERHOSEN_HAT_6182, Items.LEDERHOSEN_TOP_6180, Items.LEDERHOSEN_SHORTS_6181)
                // Set 7. Shade robes.
                Items.SHADE_ROBE_10634 -> intArrayOf(Items.SHADE_ROBE_546, Items.SHADE_ROBE_548)
                else -> return
            }

            val isHidden = 166 + (setIndex * 2)

            if (!allInInventory(player, *set)) {
                sendMessage(player, "You don't have the necessary items to take the outfit.")
                return
            }

            if (getAttribute(player, "set:$setIndex", false)) {
                if (freeSlots(player) >= set.size) {
                    set.forEach { addItem(player, it, 1) }
                    removeAttribute(player, "set:$setIndex")
                    sendMessage(player, "You take the outfit from the wardrobe.")
                } else {
                    sendMessage(player, "You don't have enough inventory space.")
                }
            } else {
                if (allInInventory(player, *set)) {
                    set.forEach { removeItem(player, Item(it, 1)) }
                    setAttribute(player, "/save:set:$setIndex", true)
                    sendInterfaceConfig(player, INTERFACE, isHidden, true)
                    sendMessage(player, "You put your outfit into the wardrobe.")
                } else {
                    sendMessage(player, "You need to have the full set to store it.")
                }
            }
        }
    }
}

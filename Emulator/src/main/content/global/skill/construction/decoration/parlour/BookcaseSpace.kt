package content.global.skill.construction.decoration.parlour

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.world.GameWorld
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class BookcaseSpace :
    InteractionListener,
    InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(BOOKCASE_INTERFACE) { player, _ ->
            val c = (163..224 step 2)
            bookDetails.forEachIndexed { index, (itemId, description) ->
                sendString(
                    player,
                    "${core.tools.YELLOW}${getItemName(itemId)}</col> <br>$description",
                    BOOKCASE_INTERFACE,
                    55 + index * 2,
                )
            }

            return@onOpen true
        }

        on(BOOKCASE_INTERFACE) { player, _, _, buttonID, _, _ ->
            if (buttonID in 56..114) {
                addItem(player, getBook(buttonID))
            }
            return@on true
        }
    }

    override fun defineListeners() {
        on(BOOKCASE, IntType.SCENERY, "search") { player, _ ->
            animate(player, Animations.USE_OBJECT_POH_3659)
            openInterface(player, BOOKCASE_INTERFACE)
            return@on true
        }

        onUseWith(IntType.SCENERY, RESTRICTED_BOOKS, *BOOKCASE) { player, used, _ ->
            if (used.id in RESTRICTED_BOOKS) {
                sendMessage(player, "There doesn't seem to be space for that on the bookcase.")
            }
            return@onUseWith true
        }
    }

    companion object {
        const val BOOKCASE_INTERFACE = 467

        val BOOKCASE = intArrayOf(Scenery.BOOKCASE_13597, Scenery.BOOKCASE_13598, Scenery.BOOKCASE_13599)

        val RESTRICTED_BOOKS =
            intArrayOf(
                Items.HOLY_BOOK_3840,
                Items.DAMAGED_BOOK_3839,
                Items.BOOK_OF_BALANCE_3844,
                Items.DAMAGED_BOOK_3843,
                Items.UNHOLY_BOOK_3842,
                Items.DAMAGED_BOOK_3841,
                Items.STRANGE_BOOK_5507,
                Items.BOOK_ON_CHICKENS_7464,
                Items.BOOK_OF_FOLKLORE_5508,
                Items.PVP_WORLDS_MANUAL_14056,
            )

        val bookDetails =
            listOf(
                Items.ARENA_BOOK_6891 to "Magic Training Arena Lore Book",
                Items.MY_NOTES_11339 to "Discoveries from the ancient cavern beneath the lake",
                Items.CRUMBLING_TOME_4707 to "Legend of the Brothers",
                Items.PIE_RECIPE_BOOK_7162 to "Pie Recipes",
                Items.GIANNES_COOK_BOOK_2167 to "The collected recipes of Aluft Gianne",
                Items.GAME_BOOK_7681 to "Party Pete's Bumper Book of Games",
                Items.STRONGHOLD_NOTES_9004 to "Stronghold of Security - Notes",
                Items.COCKTAIL_GUIDE_2023 to "The Blurberry Cocktail Guide",
                Items.TARNS_DIARY_10587 to "The Diary of Tarn Razorlor",
                Items.CONSTRUCTION_GUIDE_8463 to "Guide to Construction",
                Items.GLASSBLOWING_BOOK_11656 to "Ultimate Guide to Glassblowing",
                Items.BREWIN_GUIDE_8989 to "Brewin' Guide",
                Items.SECURITY_BOOK_9003 to "${GameWorld.settings?.name} Account Security",
                Items.QUEEN_HELP_BOOK_10562 to "Queen Help",
                Items.ABYSSAL_BOOK_5520 to "Abyssal Research Notes",
                Items.EXPLORERS_NOTES_11677 to "Beyond Trollheim",
                Items.GOBLIN_BOOK_10999 to "The Book of the Big High War God (Another Slice of H.A.M.)",
                Items.DWARVEN_LORE_4568 to "The Arzinian Being of Bordanzan (Between a Rock...)",
                Items.BOOK_O_PIRACY_7144 to "The Little Book o' Piracy (Cabin Fever)",
                Items.CLOCKWORK_BOOK_10594 to "Clockwork Toys - Chapter 1.0 (Cold War)",
                Items.SCABARITE_NOTES_11975 to "'My Notes' (Dealing with Scabaras)",
                Items.TRANSLATION_4655 to "Translation Primer (Desert Treasure)",
                Items.BOOK_ON_CHEMICALS_711 to "Volatile Chemicals (Digsite)",
                Items.INSTRUCTION_MANUAL_5 to "Dwarf Multicannon Manual (Dwarf Cannon)",
                Items.BIRD_BOOK_10173 to "William Oddity's Guide to the Avian (Eagles' Peak)",
                Items.FEATHERED_JOURNAL_10179 to "The Journal of Arthur Artimus (Eagles' Peak)",
                Items.BATTERED_BOOK_2886 to "Book of the Elemental Shield (Elemental Workshop I)",
                Items.BEATEN_BOOK_9717 to "Book of the Elemental Helm (Elemental Workshop II)",
                Items.A_HANDWRITTEN_BOOK_9627 to "Crystal Singing for Beginners (The Eyes of Glouphrie)",
                Items.VARMENS_NOTES_4616 to "The Ruins of Uzer (The Golem)",
            )
    }

    fun getBook(id: Int): Int {
        return if (id in 56..114 step 2) {
            bookDetails[(id - 56) / 2].first
        } else {
            0
        }
    }
}

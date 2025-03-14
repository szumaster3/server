package content.global.handlers.item

import core.api.openInterface
import core.api.sendDialogueLines
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items

class NewsPaperListener : InteractionListener {
    companion object {
        private const val NEWSPAPER_INTERFACE_530 = Components.V_NEWSPAPER_530

        private val leftPageContent =
            """
            Varrock gets Makeover
            <br><br>
            The city of Varrock is the latest recipient of a complete makeover. When interviewed, King Roald said, 'In order to keep visitors coming to see the sights of our beautiful capital, we felt that tidying-up the city would be more effective than just issuing a decree - make sure you visit the new museum while you are here.'
            """.trimIndent()

        private val rightPageContent =
            """
            Obituaries
            <br><br>
            Goblin-Died<br>
            Giant Rat-Died<br>
            Unicorn-Died<br>
            Varrock Guard-Died<br>
            Varrock Guard-Died<br>
            Varrock Guard-Died<br>
            Bear-Died.
            <br><br>
            Classifieds
            <br><br>
            Lowe's Archery Emporium for the finest ranging weapons in town!
            <br><br>
            Time to party! Visit the Fancy Dress Shop for all your party outfits.
            <br><br>
            The Dancing Donkey - cold beer always in stock.
            """.trimIndent()
    }

    override fun defineListeners() {
        on(Items.NEWSPAPER_11169, IntType.ITEM, "read") { player, _ ->
            openInterface(player, NEWSPAPER_INTERFACE_530)
            sendString(player, leftPageContent, NEWSPAPER_INTERFACE_530, 7)
            sendString(player, rightPageContent, NEWSPAPER_INTERFACE_530, 8)
            true
        }

        on(Items.AL_KHARID_FLYER_7922, IntType.ITEM, "read") { player, _ ->
            sendDialogueLines(
                player,
                "Come to the Al Kharid Market place! High quality",
                "produce at low, low prices! Show this flyer to a",
                "merchant for money off your next purchase,",
                "courtesy of Ali Morrisane!",
            )
            true
        }
    }
}

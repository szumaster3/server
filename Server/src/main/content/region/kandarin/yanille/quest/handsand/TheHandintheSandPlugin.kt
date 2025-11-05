package content.region.kandarin.yanille.quest.handsand

import core.api.openInterface
import core.api.sendNPCDialogue
import core.api.sendString
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

/**
 * Handles all interactions in The Hand in the Sand quest.
 */
class TheHandintheSandPlugin : InteractionListener {

    companion object {
        val BEER_IDS = intArrayOf(Items.GREENMANS_ALE_1909, Items.DRAGON_BITTER_1911)

        val fakeContent = arrayOf(
            "Sandy's Sand Corp - Brimhaven",
                    "",
            "    Bert's Rota - Copy   ",
                    "",
            "Week 1 - 6am-10pm - 50gps",
                    "",
            "Week 2 - 6am-10pm - 50gps",
                    "",
            "Week 3 - 6am-10pm - 50gps",
                    "",
            "Week 4 - 6am-10pm - 50gps",
                    "",
            "Week 5 - 6am-10pm - 50gps",
                    "",
            "Week 6 - 6am-10pm - 50gps"
        )

        val originalContent = arrayOf(
            "Sandy's Sand Corp - Brimhaven",
            "",
            "    Bert's Rota - Original   ",
            "",
            "Week 1 - 9am-6pm - 50gps",
            "",
            "Week 2 - 9am-6pm - 50gps",
            "",
            "Week 3 - 9am-6pm - 50gps",
            "",
            "Week 4 - 9am-6pm - 50gps",
            "",
            "Week 5 - 9am-6pm - 50gps",
            "",
            "Week 6 - 9am-10pm - 50gps"
        )
    }

    override fun defineListeners() {

        /*
         * Handles using dif beers on guard captain.
         */

        onUseWith(IntType.NPC, BEER_IDS, NPCs.GUARD_CAPTAIN_3109) { player, _, with ->
            sendNPCDialogue(player, with.id, "Yeeeuuuch! I hatesh that shtuff, jusht bring ush a beer! Mmmmm beer!", FaceAnim.OLD_DRUNK_LEFT)
            return@onUseWith true
        }

        /*
         * Handles read the Bert rota.
         */

        on(Items.BERTS_ROTA_6947, IntType.ITEM, "Read") { player, _ ->
            openInterface(player, Components.BLANK_SCROLL_222)
            sendString(player, fakeContent.joinToString("<br>"), Components.BLANK_SCROLL_222, 5)
            return@on true
        }

        /*
         * Handles read the Sandy rota.
         */

        on(Items.SANDYS_ROTA_6948, IntType.ITEM, "Read") { player, _ ->
            openInterface(player, Components.BLANK_SCROLL_222)
            sendString(player, originalContent.joinToString("<br>"), Components.BLANK_SCROLL_222, 5)
            return@on true
        }

    }

}
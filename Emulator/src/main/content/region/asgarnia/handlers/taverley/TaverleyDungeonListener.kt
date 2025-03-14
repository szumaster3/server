package content.region.asgarnia.handlers.taverley

import core.api.*
import core.api.quest.isQuestComplete
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Scenery

class TaverleyDungeonListener : InteractionListener {

    private val GATES = intArrayOf(Scenery.DOOR_31844, Scenery.DOOR_31841)

    override fun defineListeners() {
        on(Scenery.GATE_2623, IntType.SCENERY, "open") { player, node ->
            if (!inInventory(player, Items.DUSTY_KEY_1590)) {
                sendMessage(player, "This gate seems to be locked.")
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.DUSTY_KEY_1590, Scenery.GATE_2623) { player, _, with ->
            DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            return@onUseWith true
        }

        /*
         * Handles the gates to Cauldron of thunder.
         * Sources: https://runescape.wiki/w/Suit_of_armour_(Taverley_Dungeon)?oldid=1622127
         */

        on(GATES, IntType.SCENERY, "open") { player, node ->
            if (!isQuestComplete(player, Quests.DRUIDIC_RITUAL)) {
                if (ArmourSuitNPC.activeSuits.size < 2) {
                    ArmourSuitNPC.spawnArmourSuit(player)
                }
            }
            DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
            return@on true
        }
    }
}

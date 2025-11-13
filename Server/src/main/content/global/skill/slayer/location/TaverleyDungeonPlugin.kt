package content.global.skill.slayer.dungeon

import content.region.asgarnia.taverley.npc.ArmourSuitNPC
import core.api.inInventory
import core.api.isQuestComplete
import core.api.sendMessage
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Scenery

class TaverleyDungeonPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles opening the gate.
         */

        on(Scenery.GATE_2623, IntType.SCENERY, "open") { player, node ->
            if (inInventory(player, Items.DUSTY_KEY_1590)) {
                sendMessage(player, "You unlock the door.")
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                sendMessage(player, "The door locks shut behind you.")
            } else {
                sendMessage(player, "This gate seems to be locked.")
            }
            return@on true
        }

        /*
         * Handles use of dusty key on gate.
         */

        onUseWith(IntType.SCENERY, Items.DUSTY_KEY_1590, Scenery.GATE_2623) { player, _, with ->
            sendMessage(player, "You unlock the door.")
            DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            sendMessage(player, "The door locks shut behind you.")
            return@onUseWith true
        }

        /*
         * Handles the gates to Cauldron of thunder.
         */

        on(TAVERLEY_GATES, IntType.SCENERY, "open") { player, node ->
            if (!isQuestComplete(player, Quests.DRUIDIC_RITUAL) && ArmourSuitNPC.activeSuits.size < 2) {
                ArmourSuitNPC.spawnArmourSuit(player)
            }

            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }
    }

    companion object {
        private val TAVERLEY_GATES = intArrayOf(Scenery.DOOR_31844, Scenery.DOOR_31841)
    }
}

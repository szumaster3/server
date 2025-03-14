package content.region.desert.handlers

import content.region.desert.dialogue.alkharid.AlKharidHealDialogue
import content.region.desert.dialogue.alkharid.AliTheLeafletDropperDialogue
import content.region.desert.dialogue.alkharid.BorderGuardDialogue
import core.api.*
import core.api.interaction.openBankAccount
import core.api.interaction.openGrandExchangeCollectionBox
import core.api.interaction.openNpcShop
import core.api.quest.getQuestStage
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class AlkharidListener : InteractionListener {
    override fun defineListeners() {
        on(BORDER_GUARD, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, BorderGuardDialogue())
            return@on true
        }

        on(TOLL_GATES, IntType.SCENERY, "open", "pay-toll(10gp)") { player, node ->
            if (getUsedOption(player) == "pay-toll(10gp)") {
                if (getQuestStage(player, Quests.PRINCE_ALI_RESCUE) > 50) {
                    sendMessage(player, "The guards let you through for free.")
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                } else {
                    if (!removeItem(player, Item(Items.COINS_995, 10))) {
                        sendMessage(player, "You need 10 gold to pass through the gates.")
                    } else {
                        sendMessage(player, "You quickly pay the 10 gold toll and go through the gates.")
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                        return@on true
                    }
                }
            } else {
                openDialogue(player, BorderGuardDialogue())
            }
            return@on true
        }

        on(HEALERS_NPC, IntType.NPC, "heal") { player, _ ->
            openDialogue(player, AlKharidHealDialogue(false))
            return@on true
        }

        on(FADLI_NPC, IntType.NPC, "buy") { player, _ ->
            openNpcShop(player, NPCs.FADLI_958)
            return@on true
        }

        on(FADLI_NPC, IntType.NPC, "bank", "collect") { player, _ ->
            val option = getUsedOption(player)
            when (option) {
                "bank" -> openBankAccount(player)
                else -> openGrandExchangeCollectionBox(player)
            }
            return@on true
        }

        on(LEAFLET_DROPPER, IntType.NPC, "Take-flyer") { player, _ ->
            if (player.inventory.containItems(Items.AL_KHARID_FLYER_7922)) {
                openDialogue(player, AliTheLeafletDropperDialogue(2))
            } else {
                if (addItem(player, Items.AL_KHARID_FLYER_7922)) {
                    openDialogue(player, AliTheLeafletDropperDialogue(1))
                } else {
                    return@on false
                }
            }
            return@on true
        }
    }

    companion object {
        private const val FADLI_NPC = NPCs.FADLI_958
        private const val LEAFLET_DROPPER = NPCs.ALI_THE_LEAFLET_DROPPER_3680
        private val HEALERS_NPC =
            intArrayOf(NPCs.AABLA_959, NPCs.SABREEN_960, NPCs.SURGEON_GENERAL_TAFANI_961, NPCs.JARAAH_962)
        private val TOLL_GATES = intArrayOf(Scenery.GATE_35551, Scenery.GATE_35549)
        private const val BORDER_GUARD = NPCs.BORDER_GUARD_7912
    }
}

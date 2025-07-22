package content.region.fremennik.misc.plugin

import core.api.openNpcShop
import core.api.hasRequirement
import core.api.sendNPCDialogue
import core.api.sendNPCDialogueLines
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.NPCs
import org.rs.consts.Quests

class MiscellaniaPlugin : InteractionListener {

    companion object {
        val MISCELLANIA_NPC = intArrayOf(NPCs.ALRIK_1381, NPCs.RUNOLF_3924, NPCs.BRODDI_1390, NPCs.EINAR_1380, NPCs.INGRID_3926, NPCs.RAGNAR_1379, NPCs.RAGNVALD_1392, NPCs.RANNVEIG_1386, NPCs.THORA_3927, NPCs.THORA_1387, NPCs.THORHILD_1382, NPCs.VALGERD_1388, NPCs.TJORVI_3925)
    }

    override fun defineListeners() {
        on(MISCELLANIA_NPC, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Good day, Your Royal Highness.")
            return@on true
        }

        on(NPCs.FISHERMAN_FRODI_1397, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Hello!")
            return@on true
        }

        on(NPCs.FARMER_FROMUND_3917, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Hey! This is the Queen's farm. You'll need her approval to make use of it.")
            return@on true
        }

        on(NPCs.FISHMONGER_1393, IntType.NPC, "talk-to") { player, node ->
            if (!hasRequirement(player, Quests.THRONE_OF_MISCELLANIA)) {
                sendNPCDialogue(player, node.id, "Greetings, Sir. Get your fresh fish here! I've heard that the Etceterian fish is stored in a cow shed.",)
            } else {
                sendNPCDialogue(player, node.id, "Greetings, Your Highness. Have some fresh fish! I've heard that the Etceterian fish is stored in a cow shed.",)
            }
            return@on true
        }

        on(NPCs.GREENGROCER_1394, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogueLines(player, node.id, FaceAnim.NEUTRAL, false,"Welcome, Sir.", "I sell only the finest and freshest vegetables!")
            openNpcShop(player, node.id)
            return@on true
        }


    }
}

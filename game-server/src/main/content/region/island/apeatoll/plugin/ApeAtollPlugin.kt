package content.region.island.apeatoll.plugin

import core.api.sendNPCDialogue
import core.api.toIntArray
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.NPCs

class ApeAtollPlugin : InteractionListener {

    val ELDER_GUARDS = intArrayOf(NPCs.ELDER_GUARD_1461, NPCs.ELDER_GUARD_1462)

    override fun defineListeners() {

        /*
         * Handles talking to NPC.
         */

        on(NPCs.ABERAB_1432, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Grr ... Get out of my way...", FaceAnim.OLD_ANGRY1)
            return@on true
        }

        on(NPCs.MURUWOI_1450, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Grr ... Get out of my way...", FaceAnim.OLD_ANGRY1)
            return@on true
        }

        on(ELDER_GUARDS, IntType.NPC, "talk-to") { player, node ->
            val ids = 4025..4031
            val wearingMonkeyItems = player.equipment.containsAtLeastOneItem(ids.toIntArray())
            val outside = true
            val cornerGuard = false

            if (!wearingMonkeyItems) return@on true

            dialogue(player) {
                when {
                    outside -> {
                        npc(node.id, FaceAnim.OLD_ANGRY1, "Grrr ... What do you want?")
                        player(FaceAnim.ASKING, "I must speak with Awowogei on a subject of great import.")
                        npc(node.id, FaceAnim.OLD_NORMAL, "Only the Captain of the Monkey Guard or those he authorizes may enter this building. You will need his permission to enter.")
                        player(FaceAnim.ASKING, "Who is the Captain of the Monkey Guard?")
                        player(FaceAnim.ASKING, "He goes by the name of Kruk.")
                    }
                    cornerGuard -> {
                        npc(node.id, FaceAnim.OLD_ANGRY1, "Grrr ... What do you want?")
                        player(FaceAnim.ASKING, "I would like to leave now.")
                        npc(node.id, FaceAnim.OLD_NORMAL, "As you wish.")
                    }
                    else -> {
                        npc(node.id, FaceAnim.OLD_ANGRY1, "Move!")
                    }
                }
            }
            return@on true
        }
    }
}
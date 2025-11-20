package content.region.island.ape_atoll.plugin

import core.api.openDialogue
import core.api.sendNPCDialogue
import core.api.toIntArray
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.NPCs

class ApeAtollPlugin : InteractionListener {

    companion object {
        private val ELDER_GUARDS = intArrayOf(NPCs.ELDER_GUARD_1461, NPCs.ELDER_GUARD_1462)
    }

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
            openDialogue(player, ElderGuardDialogue(true), node.id)
            return@on true
        }
    }

    inner class ElderGuardDialogue(val outside: Boolean) : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            val outside = true
            val cornerGuard = false
            when(stage) {
                0 -> npcl(FaceAnim.OLD_ANGRY1, "Grrr ... What do you want?")
                1 -> playerl(FaceAnim.ASKING, "I must speak with Awowogei on a subject of great import.")
                2 -> npcl(FaceAnim.OLD_NORMAL, "Only the Captain of the Monkey Guard or those he authorizes may enter this building. You will need his permission to enter.")
                3 -> playerl(FaceAnim.ASKING, "Who is the Captain of the Monkey Guard?")
                4 -> playerl(FaceAnim.ASKING, "He goes by the name of Kruk.")

                5 -> npcl(FaceAnim.OLD_ANGRY1, "Grrr ... What do you want?")
                6 -> playerl(FaceAnim.ASKING, "I would like to leave now.")
                7 -> npc(FaceAnim.OLD_NORMAL, "As you wish.")
                else -> {
                    npc(FaceAnim.OLD_ANGRY1, "Move!")
                }
            }
        }
    }
}
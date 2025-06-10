package content.global.activity.mogre

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Vars

/**
 * Handles interaction for lore activity that teaches players how to fight mogre npc.
 * @author szu
 */
class SkippyListener : InteractionListener {
    private val SKIPPY_NPC = intArrayOf(2795, 2796, 2797, 2798, 2799)

    override fun defineListeners() {

        /*
         * Handles second option for Skippy NPC.
         */

        on(SKIPPY_NPC, IntType.NPC, "sober-up") { player, node ->
            openDialogue(player, SkippyDialogueFile(), node)
            return@on false
        }

        /*
         * Handles using forlorn boot on Skippy NPC.
         */

        onUseWith(IntType.NPC, Items.FORLORN_BOOT_6663, *SKIPPY_NPC) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                sendNPCDialogue(player, NPCs.SKIPPY_2796, "Thanks, now I have two right boots!")
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        /*
         * Handles use water on Skippy NPC.
         */

        onUseWith(IntType.NPC, Items.BUCKET_OF_WATER_1929, *SKIPPY_NPC) { player, _, _ ->
            if (getVarbit(player, Vars.VARBIT_MINI_QUEST_MOGRE_AND_SKIPPY_1344) > 1) {
                sendPlayerDialogue(player, "I think he's sober enough. And I don't want to use another bucket of water.")
            } else {
                sendMessage(player, "I can't do that.")
            }
            return@onUseWith true
        }
    }
}

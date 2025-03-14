package content.global.activity.mogre

import content.region.misc.dialogue.tutorial.SkippyTutorialDialogue
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.NPCs

class SkippyListener : InteractionListener {
    private val SKIPPY_NPC_WRAPPER_IDS = intArrayOf(2795, 2796, 2797, 2798, 2799)

    override fun defineListeners() {
        on(SKIPPY_NPC_WRAPPER_IDS, IntType.NPC, "sober-up", "talk-to") { player, node ->
            if (inBorders(player, SkippyUtils.TUTORIAL_ISLAND)) {
                openDialogue(player, SkippyTutorialDialogue())
                return@on true
            }
            player.dialogueInterpreter.open(node.id)
            return@on true
        }

        onUseWith(IntType.NPC, Items.FORLORN_BOOT_6663, *SKIPPY_NPC_WRAPPER_IDS) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                sendNPCDialogue(player, NPCs.SKIPPY_2796, "Thanks, now I have two right boots!")
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, Items.BUCKET_OF_WATER_1929, *SKIPPY_NPC_WRAPPER_IDS) { player, used, _ ->
            if (getVarbit(player, SkippyUtils.SKIPPY_VARBIT) > 1 && used.id == 1929) {
                sendPlayerDialogue(
                    player,
                    "I think he's sober enough. And I don't want to use another bucket of water.",
                )
            } else {
                sendMessage(player, "I can't do that.")
            }
            return@onUseWith true
        }
    }
}

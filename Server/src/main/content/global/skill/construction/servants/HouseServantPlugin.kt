package content.global.skill.construction.servants

import core.api.sendNPCDialogue
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items
import shared.consts.NPCs

/**
 * Plugin handling interactions with house servants.
 */
@Initializable
class HouseServantPlugin : UseWithHandler(*IDS) {

    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(NPCs.RICK_4236, NPC_TYPE, this)
        addHandler(NPCs.MAID_4237, NPC_TYPE, this)
        addHandler(NPCs.COOK_4239, NPC_TYPE, this)
        addHandler(NPCs.BUTLER_4241, NPC_TYPE, this)
        addHandler(NPCs.DEMON_BUTLER_4243, NPC_TYPE, this)
        definePlugin(HouseServantDialogue())
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        if (event.usedItem == null || event.usedWith == null) {
            return true
        }

        if(event.usedItem.id == Items.BONES_526 && event.usedWith.id == NPCs.DEMON_BUTLER_4244) {
            sendNPCDialogue(event.player, event.usedWith.id, core.tools.DARK_BLUE + "Bones are a tool of prayer, and demonic magic prevents me from touching them, my Lord.")
            return true
        }

        event.player.dialogueInterpreter.open(
            event.usedWith.asNpc().id,
            event.usedWith.asNpc(),
            true,
            event.usedItem
        )
        return true
    }

    companion object {
        /**
         * The item ids of logs that can be used with house servants.
         */
        val IDS: IntArray = intArrayOf(Items.LOGS_1511, Items.OAK_LOGS_1521, Items.TEAK_LOGS_6333, Items.MAHOGANY_LOGS_6332)
    }
}

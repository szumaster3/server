package content.region.kandarin.handlers.guilds.wizards

import content.region.kandarin.quest.zogre.dialogue.ZavisticRarveDefaultDialogue
import content.region.kandarin.quest.zogre.dialogue.ZavisticRarveDialogueFiles
import core.api.openDialogue
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Zavistic Rarve dialogue.
 */
@Initializable
class ZavisticRarveDialogue(
    player: Player? = null,
) : Dialogue(player) {
    /*
     * Info: Grand Secretary of the Wizards' Guild.
     */

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        end()
        if (isQuestComplete(player, "Zogre Flesh Eaters")) {
            openDialogue(player, ZavisticRarveDefaultDialogue())
        } else {
            openDialogue(player, ZavisticRarveDialogueFiles())
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ZAVISTIC_RARVE_2059)
    }
}

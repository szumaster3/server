package content.region.kandarin.handlers.guilds.wizards

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Wizard Frumscone dialogue.
 */
@Initializable
class WizardFrumsconeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(
            "Do you like my magic Zombies? Feel free to kill them,",
            "there's plenty more where these came from!",
        ).also {
            stage =
                END_DIALOGUE
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
        return intArrayOf(NPCs.WIZARD_FRUMSCONE_460)
    }
}

package content.region.kandarin.handlers.guilds.wizards

import content.global.travel.EssenceTeleport
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Wizard Distentor dialogue.
 */
@Initializable
class WizardDistentorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    /*
     * Info: leader of the Wizards' Guild in Yanille.
     * Due to his proximity to a bank, he is considered to be one
     * of the best for transport to the Rune Essence mine.
     */

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("Welcome to the Magicians' Guild!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player("Hello there.").also { stage++ }
            1 -> npc("What can I do for you?").also { stage++ }
            2 -> {
                if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                    player("Nothing thanks, I'm just looking around.").also { stage = 4 }
                } else {
                    options(
                        "Nothing thanks, I'm just looking around.",
                        "Can you teleport me to Rune Essence?",
                    ).also { stage++ }
                }
            }

            3 ->
                when (buttonId) {
                    1 -> player("Nothing thanks, I'm just looking around.").also { stage++ }
                    2 -> player("Can you teleport me to the Rune Essence?").also { stage = 5 }
                }

            4 -> npc("That's fine with me.").also { stage = END_DIALOGUE }
            5 -> {
                end()
                EssenceTeleport.teleport(npc, player)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WIZARD_DISTENTOR_462)
    }
}

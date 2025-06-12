package content.region.morytania.port_phasmatys.dialogue

import content.region.morytania.quest.ahoy.dialogue.VelorinaDialogueFile
import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class VelorinaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        // If the player is not wearing the Ghostspeak Amulet.
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            npc("Woooo wooo wooooo woooo")
            stage = 4
        }

        // Handling quest completion and dialogue options.
        when {
            isQuestComplete(player, Quests.GHOSTS_AHOY) -> {
                options(
                    "I thought you were going to pass over to the next world.",
                    "Can I have another Ectophial?",
                )
            }

            // If the quest is not complete.
            !isQuestComplete(player, Quests.GHOSTS_AHOY) -> {
                openDialogue(player, VelorinaDialogueFile())
            }

            // Default case, if none of the above conditions are met.
            else -> {
                sendMessage(player, "She is ignoring you.")
                stage = END_DIALOGUE
            }
        }

        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val hasEctophial = hasAnItem(player, Items.ECTOPHIAL_4251, Items.ECTOPHIAL_4252).container != null
        when (stage) {
            0 ->
                when (buttonId) {
                    1 -> player("I thought you were going to pass over", " to the next world.").also { stage++ }
                    2 -> player("Can I have another Ectophial?").also { stage = 2 }
                }

            1 ->
                npc(
                    "All in good time, Player. We stand forever",
                    "in your debt, and will certainly put in a good word",
                    "for you when we pass over.",
                ).also { stage = END_DIALOGUE }

            2 -> {
                if (!hasEctophial) {
                    npc("Of course you can, you have helped us more than we", "could ever have hoped.").also { stage++ }
                } else {
                    npc("You already have an ectophial.").also { stage = END_DIALOGUE }
                }
            }

            3 -> {
                end()
                sendItemDialogue(player, Items.ECTOPHIAL_4251, "Velorina gives you a vial of bright green ectoplasm.")
                addItemOrDrop(player, Items.ECTOPHIAL_4251)
            }

            4 -> sendDialogue(player, "You cannot understand the ghost.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.VELORINA_1683)
}

package content.region.morytania.phas.dialogue

import core.api.getQuestStage
import core.api.inEquipment
import core.api.openDialogue
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Gravingas dialogue.
 */
@Initializable
class GravingasDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        when {
            // If the player has the bedsheet and has completed the quest.
            inEquipment(player, Items.BEDSHEET_4285) && getQuestStage(player, Quests.GHOSTS_AHOY) >= 1 -> {
                end()
                openDialogue(player, GravingasDialogue())
                return true
            }

            // If the player does not have the Ghostspeak Amulet.
            !inEquipment(player, Items.GHOSTSPEAK_AMULET_552) -> {
                npcl(FaceAnim.FRIENDLY, "Woooo wooo wooooo woooo")
                return true
            }

            else -> {
                npc(FaceAnim.FRIENDLY, "Will you join with me and protect against the evil ban", "of Necrovarus and his disciples?").also { stage = 1 }
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> sendDialogue(player!!, "You cannot understand the ghost.").also { stage = END_DIALOGUE }
            1 -> player("I'm sorry, I don't really think I should get involved.").also { stage++ }
            2 -> npc("Ah, the youth of today - so apathetic to politics.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GRAVINGAS_1685)
}

package content.region.kandarin.dialogue.stronghold

import content.region.kandarin.quest.tree.dialogue.KingBolrenDialogueFile
import core.api.addItemOrDrop
import core.api.hasAnItem
import core.api.openDialogue
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class KingBolrenDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.TREE_GNOME_VILLAGE)) {
            playerl(FaceAnim.FRIENDLY, "Hello again Bolren.")
        } else {
            end()
            openDialogue(player, KingBolrenDialogueFile())
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        var hasAnGnomeAmulet = hasAnItem(player, Items.GNOME_AMULET_589).container != null
        when (stage) {
            0 ->
                if (!hasAnGnomeAmulet) {
                    npc(FaceAnim.OLD_NORMAL, "Well hello, it's good to see you again.").also { stage++ }
                } else {
                    npcl(FaceAnim.OLD_NORMAL, "Thank you for your help traveler.").also { stage = END_DIALOGUE }
                }

            1 -> player(FaceAnim.SAD, "I've lost my amulet.").also { stage++ }
            2 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Oh dear. Here, take another. We are truly indebted",
                    "to you.",
                ).also { stage++ }

            3 -> {
                end()
                addItemOrDrop(player, Items.GNOME_AMULET_589)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return KingBolrenDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KING_BOLREN_469)
    }
}

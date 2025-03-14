package content.region.morytania.dialogue.phasmatys

import content.region.morytania.quest.ahoy.dialogue.AkharanuDialogueFile
import core.api.inInventory
import core.api.interaction.openNpcShop
import core.api.openDialogue
import core.api.quest.getQuestStage
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
class AkharanuDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (inInventory(player!!, Items.SIGNED_OAK_BOW_4236) || getQuestStage(player, Quests.GHOSTS_AHOY) >= 5) {
            end()
            openDialogue(player, AkharanuDialogueFile())
        } else {
            npc(FaceAnim.FRIENDLY, "Hello, there, friend!")
        }
        return true
    }

    override fun handle(
        componentId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Why are you, errr, so stiff?", "Do you sell anything?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Why are you, errr, so stiff?").also { stage++ }
                    2 -> player("Do you sell anything?").also { stage = 5 }
                }

            2 -> npc(FaceAnim.FRIENDLY, "I have extremely severe arthritis. It really sucks.").also { stage++ }
            3 -> player("Oh. Well I'm sorry to hear that.").also { stage++ }
            4 -> npc(FaceAnim.FRIENDLY, "Yes, thank you for your concern.").also { stage = END_DIALOGUE }
            5 -> npc(FaceAnim.FRIENDLY, "Why, yes I do!").also { stage++ }
            6 -> {
                end()
                openNpcShop(player, NPCs.AK_HARANU_1688)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AK_HARANU_1688, NPCs.AK_HARANU_1689)
    }
}

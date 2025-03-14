package content.region.kandarin.quest.grandtree.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class HazelmereDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.THE_GRAND_TREE)) {
            10 -> {
                if (player!!.hasItem(Item(Items.BARK_SAMPLE_783))) {
                    when (stage) {
                        0 -> sendDialogue(player!!, "The mage starts to speak but all you hear is").also { stage++ }
                        1 -> npcl(FaceAnim.NEUTRAL, "Blah. Blah, blah, blah, blah...blah!").also { stage++ }
                        2 ->
                            sendDialogue(
                                player!!,
                                "You give the bark sample to Hazelmere. The mage carefully examines the sample.",
                            ).also {
                                stage++
                            }
                        3 -> npcl(FaceAnim.NEUTRAL, "Blah, blah...Daconia...blah, blah.").also { stage++ }
                        4 ->
                            playerl(
                                FaceAnim.ASKING,
                                "Can you write this down and I'll try and translate it?",
                            ).also { stage++ }
                        5 -> npcl(FaceAnim.NEUTRAL, "Blah, blah?").also { stage++ }
                        6 ->
                            sendDialogue(
                                player!!,
                                "You make a writing motion. The mages scribbles something down on a scroll. Hazelmere has given you the scroll.",
                            ).also {
                                if (removeItem(player!!, Items.BARK_SAMPLE_783)) {
                                    addItemOrDrop(player!!, Items.HAZELMERES_SCROLL_786)
                                }
                                setQuestStage(player!!, Quests.THE_GRAND_TREE, 20)
                                stage = END_DIALOGUE
                            }
                    }
                }
            }

            20 -> {
                when (stage) {
                    0 -> npcl(FaceAnim.NEUTRAL, "Blah, blah....Daconia...blah, blah.").also { stage++ }
                    1 ->
                        sendDialogue(
                            player!!,
                            "You still can't understand Hazelmere. The mage wrote it down for you on a scroll.",
                        ).also {
                            if (!inInventory(player!!, Items.HAZELMERES_SCROLL_786)) {
                                addItemOrDrop(player!!, Items.HAZELMERES_SCROLL_786)
                            }
                            stage = END_DIALOGUE
                        }
                }
            }

            else -> npcl(FaceAnim.NEUTRAL, "Blah, blah...blah, blah.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HAZELMERE_669)
    }
}

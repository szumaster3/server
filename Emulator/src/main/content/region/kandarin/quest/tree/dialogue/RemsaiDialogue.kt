package content.region.kandarin.quest.tree.dialogue

import core.api.inInventory
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
class RemsaiDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (inInventory(player!!, Items.ORBS_OF_PROTECTION_588) ||
            getQuestStage(
                player,
                Quests.TREE_GNOME_VILLAGE,
            ) > 40
        ) {
            playerl(FaceAnim.FRIENDLY, "I've returned.").also { stage++ }
        } else if (getQuestStage(player, Quests.TREE_GNOME_VILLAGE) == 40) {
            playerl(FaceAnim.ASKING, "Are you ok?").also { stage++ }
        } else {
            playerl(FaceAnim.FRIENDLY, "Hello Remsai.").also { stage++ }
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        val questStage = getQuestStage(player!!, Quests.TREE_GNOME_VILLAGE)
        when {
            inInventory(player!!, Items.ORBS_OF_PROTECTION_588) -> {
                when (stage) {
                    1 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "You're back, well done brave adventurer. Now the orbs are safe we can perform the ritual for the spirit tree. We can live in peace once again.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            }

            inInventory(player!!, Items.ORB_OF_PROTECTION_587) -> {
                when (stage) {
                    1 -> npcl(FaceAnim.OLD_DEFAULT, "Hello, did you find the orb?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "I have it here.").also { stage++ }
                    3 -> npcl(FaceAnim.OLD_DEFAULT, "You're our saviour.").also { stage = END_DIALOGUE }
                }
            }

            questStage < 40 -> {
                when (stage) {
                    1 -> npcl(FaceAnim.OLD_DEFAULT, "Hello, did you find the orb?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "No, I'm afraid not.").also { stage++ }
                    3 ->
                        npcl(FaceAnim.OLD_DEFAULT, "Please, we must have the orb if we are to survive.").also {
                            stage = END_DIALOGUE
                        }
                }
            }

            questStage == 40 -> {
                when (stage) {
                    1 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "Khazard's men came. Without the orb we were defenceless. They killed many and then took our last hope, the other orbs. Now surely we're all doomed. Without them the spirit tree is useless.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            }

            questStage > 40 -> {
                when (stage) {
                    1 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "You're back, well done brave adventurer. Now the orbs are safe we can perform the ritual for the spirit tree. We can live in peace once again.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.REMSAI_472)
    }
}

package content.region.asgarnia.dialogue.trollheim

import content.region.asgarnia.quest.death.dialogue.SabaDialogueFile
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SabaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.DEATH_PLATEAU)) {
            openDialogue(player!!, SabaDialogueFile(), npc)
        } else if (isQuestComplete(player, Quests.DEATH_PLATEAU)) {
            playerl(FaceAnim.HALF_GUILTY, "Hello.").also { stage = 1 }
        } else {
            playerl(FaceAnim.HALF_GUILTY, "Hi!").also { stage = 0 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.ANNOYED, "Why won't people leave me alone?!").also { stage = END_DIALOGUE }
            1 -> npcl(FaceAnim.HALF_ASKING, "Have you got rid of those pesky trolls yet?").also { stage++ }
            2 -> {
                if (getQuestStage(player, Quests.TROLL_STRONGHOLD) >= 1) {
                    playerl(FaceAnim.HALF_GUILTY, "I'm afraid there's been some trouble...").also { stage = 5 }
                } else {
                    playerl(
                        FaceAnim.HALF_GUILTY,
                        "They will be gone soon! The Imperial Guard will use a secret way that starts from the back of the Sherpa's hut to destroy the troll camp!",
                    ).also {
                        stage++
                    }
                }
            }
            3 -> npcl(FaceAnim.ANNOYED, "I shall have peace again at last!").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "If those pesky humans don't start trampling all over Death Plateau again that is!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            5 -> npcl(FaceAnim.ANNOYED, "You told me you'd get rid of the trolls!").also { stage++ }
            6 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "I'm sure the Imperial Guard will deal with them in due course.",
                ).also { stage++ }
            7 -> npcl(FaceAnim.ANNOYED, "Hmph! They'd better!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SABA_1070)
    }
}

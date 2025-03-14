package content.region.kandarin.quest.arena.dialogue

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class JeremyServilDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.FIGHT_ARENA)) {
            20 ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.CHILD_SAD,
                            "Please " + (if (player!!.isMale) "Sir" else "Madam") + ", don't hurt me.",
                        ).also {
                            stage++
                        }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Sshh. This uniform is a disguise. I'm here to help. Where do they keep the keys?",
                        ).also {
                            stage++
                        }
                    3 -> npcl(FaceAnim.CHILD_SAD, "The guard always keeps hold of them.").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "Don't lose heart, I'll be back.").also { stage++ }
                    5 -> {
                        end()
                        setQuestStage(player!!, Quests.FIGHT_ARENA, 40)
                    }
                }

            in 90..100 ->
                when (stage) {
                    0 ->
                        npcl(FaceAnim.CHILD_NORMAL, "You need to kill the creatures in the arena").also {
                            stage = END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JEREMY_SERVIL_265)
    }
}

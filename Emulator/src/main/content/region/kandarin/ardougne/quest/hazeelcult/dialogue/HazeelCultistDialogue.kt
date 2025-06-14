package content.region.kandarin.ardougne.quest.hazeelcult.dialogue

import core.api.getAttribute
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class HazeelCultistDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        val questStage = getQuestStage(player!!, Quests.HAZEEL_CULT)

        when {
            (questStage in 1..99) -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Hi.").also { stage++ }
                1 -> npcl(
                    FaceAnim.ANNOYED,
                    "What? Who are you and why did you come here? It doesn't matter, leave now adventurer. While you still can.",
                ).also { stage = END_DIALOGUE }
            }

            (questStage == 100) -> when (stage) {
                0 -> {
                    if (getAttribute(player, "hazeelcult:mahjarrat", true) && !getAttribute(
                            player, "hazeelcult:carnillean", true
                        )
                    ) {
                        playerl(FaceAnim.FRIENDLY, "Hello.").also { stage = 1 }
                    } else {
                        playerl(FaceAnim.FRIENDLY, "Hello.").also { stage = 2 }
                    }
                }

                1 -> npcl(FaceAnim.FRIENDLY, "The adventurer returns! Glory to you for your efforts in bringing about the return of Lord Hazeel!").also {
                    stage = END_DIALOGUE
                }

                2 -> npcl(FaceAnim.ANNOYED, "It's the meddler! The one who blew Jones' cover! I don't know why you came to this place but it is here that you will die.").also {
                    stage++
                }

                3 -> {
                    end()
                    npc.attack(player)
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HAZEEL_CULTIST_894)
}

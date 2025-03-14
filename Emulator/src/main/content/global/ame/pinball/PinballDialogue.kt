package content.global.ame.pinball

import core.api.findNPC
import core.api.runTask
import core.api.sendUnclosablePlainDialogue
import core.api.unlock
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.BLUE
import org.rs.consts.NPCs

class PinballDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MYSTERIOUS_OLD_MAN_410)
        when (stage) {
            0 -> {
                npc(
                    "The rules of the game are quite simple. You have to",
                    "score 10 points by tagging the flashing pillars.",
                ).also {
                    stage++
                }
            }

            1 ->
                npc(
                    "Don't tag the ones that do not have rings around the",
                    "base, as that will reset your points, and don't try and",
                    "get past those trolls until you are done!",
                ).also {
                    stage++
                }

            2 -> npc("See you later!").also { stage++ }
            3 -> playerl(FaceAnim.SCARED, "Wait, I didn't ask to play this game!").also { stage++ }
            4 -> {
                end()
                unlock(player!!)
                PinballUtils.generateTag(player!!)
                sendUnclosablePlainDialogue(
                    player!!,
                    true,
                    "",
                    "Tag the post with the " + BLUE + "flashing rings</col>.",
                )
                runTask(player!!, 3) {
                    findNPC(NPCs.MYSTERIOUS_OLD_MAN_410)!!.clear()
                }
            }
        }
    }
}

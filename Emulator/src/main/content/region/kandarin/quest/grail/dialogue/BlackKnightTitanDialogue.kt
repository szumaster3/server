package content.region.kandarin.quest.grail.dialogue

import core.api.sendMessage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.world.repository.Repository
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class BlackKnightTitanDialogue(
    val forced: Boolean,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.BLACK_KNIGHT_TITAN_221)

        if (!forced) {
            when (stage) {
                0 ->
                    npc(
                        FaceAnim.OLD_HAPPY,
                        "<col=ff0000>I am the Black Knight Titan! <col=ff0000>You must pass through",
                        "<col=ff0000>me before you can <col=ff0000>continue in this realm!",
                    ).also {
                        stage++
                    }

                1 ->
                    showTopics(
                        Topic(FaceAnim.ANGRY, "Okay, have at ye oh evil knight!", 2),
                        Topic(FaceAnim.NEUTRAL, "Actually I think I'll run away!", END_DIALOGUE),
                    )

                2 -> {
                    attackPlayer()
                    stage = END_DIALOGUE
                    end()
                }
            }
        } else {
            when (stage) {
                0 -> {
                    player!!.properties.combatPulse.stop()
                    sendMessage(player!!, "Maybe you ned something more to beat the titan?")
                    npc(FaceAnim.OLD_HAPPY, "<col=ff0000>Puny mortal... You cannot defeat me...").also { stage++ }
                }

                1 -> {
                    attackPlayer()
                    stage = END_DIALOGUE
                    end()
                }
            }
        }
    }

    private fun attackPlayer() {
        var titan = Repository.findNPC(NPCs.BLACK_KNIGHT_TITAN_221)

        if (titan != null && titan.canStartCombat(player!!)) {
            titan.attack(player!!)
        }

        stage = END_DIALOGUE
    }
}

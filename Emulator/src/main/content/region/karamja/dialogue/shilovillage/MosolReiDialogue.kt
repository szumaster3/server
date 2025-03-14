package content.region.karamja.dialogue.shilovillage

import core.api.quest.isQuestComplete
import core.api.sendMessage
import core.api.sendMessageWithDelay
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class MosolReiDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.SHILO_VILLAGE)) {
            player("Greetings")
        } else {
            npc(
                FaceAnim.FRIENDLY,
                "Sorry bwana, I cannot help you at this time. Go and talk",
                "to Trufitus at Tai Bwo Wannai, I believe he needs some",
                "help.",
            ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    "Greetings Bwana! We've removed the threat of Rashiliyia",
                    "and though there are still some random outbreaks of undead",
                    "activity we are more than able to deal with it.",
                ).also { stage++ }

            1 -> npc("You're welcome to enter the village now Bwana,", "shall I show you the way?").also { stage++ }
            2 -> options("Yes, Ok, I'll go into the village!", "I think I'll see it some other time.").also { stage++ }
            3 ->
                when (buttonId) {
                    1 -> {
                        end()
                        sendMessage(player, "Mosol leads you into the village.")
                        sendMessageWithDelay(
                            player,
                            "Mosol leaves you by the gate and walks back out into the jungle.",
                            1,
                        )
                    }

                    2 -> {
                        end()
                        sendMessage(player, "You decide not to visit the village.")
                    }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MOSOL_REI_500)
    }
}

package content.region.misthalin.dialogue.lumbridge

import core.api.forceWalk
import core.api.sendChat
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HansDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.NEUTRAL, "Hello. What are you doing here?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "I'm looking for whoever is in charge of this place.",
                    "I have come to kill everyone in this castle!",
                    "I don't know. I'm lost. Where am I?",
                    "Have you been here as long as me?",
                ).also {
                    stage++
                }

            1 ->
                when (buttonId) {
                    1 ->
                        npc(FaceAnim.NEUTRAL, "Who, the Duke? He's in his study, on the first floor.").also {
                            stage = END_DIALOGUE
                        }
                    2 -> {
                        sendChat(npc, "Help! Help!")
                        forceWalk(npc, npc.location.transform(npc!!.direction.opposite, 1), "dumb")
                        end()
                    }

                    3 -> npc(FaceAnim.NEUTRAL, "You are in Lumbridge Castle.").also { stage = END_DIALOGUE }
                    4 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "I've been patrolling this castle for years, I remember you...",
                        ).also { stage++ }
                }
            2 -> {
                val timePlayed = PlayerDetails.getDetails(player.username).timePlayed
                val playTime = PlayerDetails.getDetails(player.username).getPlayerTime(player)
                npcl(
                    FaceAnim.THINKING,
                    "You've spent $timePlayed in the world since you arrived $playTime ago.",
                ).also { stage++ }
            }
            3 -> player(FaceAnim.ASKING, "You must be old then?").also { stage++ }
            4 ->
                npc(
                    FaceAnim.LAUGH,
                    "Haha, you could say I'm quite the veteran of these lands.",
                    "Yes, I've been here a fair while...",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HANS_0)
    }
}

package content.region.misthalin.lumbridge.dialogue

import core.api.forceWalk
import core.api.sendChat
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import java.util.concurrent.TimeUnit

/**
 * Represents the Hans dialogue.
 */
@Initializable
class HansDialogue(player: Player? = null) : Dialogue(player) {

    private fun formatPlayTime(millis: Long): String {
        val days = TimeUnit.MILLISECONDS.toDays(millis)
        val hours = TimeUnit.MILLISECONDS.toHours(millis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        return "%d days, %d hours, %d minutes".format(days, hours, minutes)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.NEUTRAL, "Hello. What are you doing here?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                options(
                    "I'm looking for whoever is in charge of this place.",
                    "I have come to kill everyone in this castle!",
                    "I don't know. I'm lost. Where am I?",
                    "Have you been here as long as me?",
                ).also { stage++ }
            }

            1 -> {
                when (buttonId) {
                    1 -> {
                        npc(FaceAnim.NEUTRAL, "Who, the Duke? He's in his study, on the first floor.")
                        stage = END_DIALOGUE
                    }
                    2 -> {
                        sendChat(npc, "Help! Help!")
                        forceWalk(npc, npc.location.transform(npc!!.direction.opposite, 1), "dumb")
                        end()
                    }
                    3 -> {
                        npc(FaceAnim.NEUTRAL, "You are in Lumbridge Castle.")
                        stage = END_DIALOGUE
                    }
                    4 -> {
                        npcl(FaceAnim.NEUTRAL, "I've been patrolling this castle for years, I remember you...")
                        stage++
                    }
                }
            }

            2 -> {
                val details = player!!.details
                val totalPlayTime = formatPlayTime(details.timePlayed)
                val timeSinceLastLogin = formatPlayTime(System.currentTimeMillis() - details.lastLogin) + " ago"
                npcl(FaceAnim.THINKING, "You've spent $totalPlayTime in the world since you arrived $timeSinceLastLogin.")
                stage++
            }

            3 -> {
                player(FaceAnim.ASKING, "You must be old then?")
                stage++
            }

            4 -> {
                npc(FaceAnim.LAUGH, "Haha, you could say I'm quite the veteran of these lands.", "Yes, I've been here a fair while...")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HANS_0)
}

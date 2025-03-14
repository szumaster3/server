package content.region.misthalin.handlers.museum.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class SchoolChildDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        npc(FaceAnim.CHILD_FRIENDLY, randomForceChat)
        return true
    }

    private val randomForceChat: String
        get() {
            val forceChat = arrayOf(
                "Can you find my teacher? I need the toilet!",
                "I wonder what they're doing behind that rope.",
                "Teacher! Can we go to the Natural History Exhibit now?",
                "*sniff* They won't let me take an arrowhead as a souvenir.",
                "Yaaay! A day off school.",
                "I wanna be an archaeologist when I grow up!",
                "Sada... Sram... Sa-ra-do-min is bestest!",
                "*cough* It's so dusty in here.",
                "Maz... Zar... Za-mor-ak is bestest!"
            )
            val randomIndex = (Math.random() * forceChat.size).toInt()
            return forceChat[randomIndex]
        }

    override fun newInstance(player: Player): Dialogue {
        return SchoolChildDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.SCHOOLGIRL_10, NPCs.SCHOOLBOY_5945, NPCs.SCHOOLBOY_5946, NPCs.SCHOOLGIRL_5984
        )
    }
}

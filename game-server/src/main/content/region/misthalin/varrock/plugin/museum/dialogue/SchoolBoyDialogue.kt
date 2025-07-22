package content.region.misthalin.varrock.plugin.museum.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class SchoolBoyDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        npc?.let {
            npc(FaceAnim.CHILD_FRIENDLY, randomForceChat(it.id))
        }
        return true
    }

    private val forceChat = arrayOf(
        "Can you find my teacher? I need the toilet!",
        "Yaaay! A day off school.",
        "The Kalphite Queen is soo scary!",
        "I want a pet sea slug for my birthday. They are so cute!",
        "The monkeys are so funny-looking!",
        "I bet a wyvern could beat a dragon in a fight!",
        "My dad says he got bitten by a giant snail once.",
        "Do you know where the moles are?",
        "We're going to see the dragons! They're my favourite!"
    )

    private val groundForceTalk = arrayOf(
        "Can you find my teacher? I need the toilet!",
        "Yaaay! A day off school.",
        "I wonder what they're doing behind that rope.",
        "Teacher! Can we go to the Natural History exhibit now?",
        "*sniff* They won't let me take an arrowhead as a souvenir.",
        "I wanna be an archaeologist when I grow up!",
        "Sada...Sram...Sa-ra-do-min is bestest!",
        "*cough* It's so dusty in here.",
        "Maz...Zar...Za-mor-ak is bestest!"
    )

    private fun randomForceChat(npcId: Int): String {
        val chatArray = if (npcId == NPCs.SCHOOLBOY_5945) forceChat else groundForceTalk
        val randomIndex = (Math.random() * chatArray.size).toInt()
        return chatArray[randomIndex]
    }

    override fun newInstance(player: Player?): Dialogue = SchoolBoyDialogue(player)

    override fun getIds(): IntArray = intArrayOf(
        NPCs.SCHOOLGIRL_10,
        NPCs.SCHOOLBOY_5945,
        NPCs.SCHOOLBOY_5946,
        NPCs.SCHOOLGIRL_5984
    )
}

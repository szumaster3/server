package content.region.kandarin.ardougne.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents the Chief Servant dialogue.
 */
@Initializable
class ChiefServantDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HAPPY, "Welcome to the Ardougne Domestic Service Agency!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> showTopics(
                Topic("Tell me about servants", 1, false),
                Topic("Can I hire a servant?", 4, false)
            )
            1 -> npcl(FaceAnim.FRIENDLY, "Do you need help around your house? Our trained, efficient workers are ready to serve you. They can cook meals for you and your friends, make tea, serve drinks, greet guests, and even take items to and from the bank for you.").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "The more expensive servants will be able to make trips to the bank more quickly. Some of them can also go to the sawmill to change logs to planks for you.").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Remember, you'll need to keep paying your servant. You pay them once when you hire them, and then they'll periodically ask for wages. You can dismiss your servant at any time.").also { stage = 5 }
            4 -> {
                val hasServant = player.houseManager.hasServant()
                if(hasServant) {
                    npcl(FaceAnim.FRIENDLY, "According to our records, one of our people is already working for you. If you want to hire a different one you will have to dismiss them.").also { stage = 5 }
                } else {
                    npc(FaceAnim.NOD_YES, "Certainly! We have a number of servants here now","looking for work. Why not have a chat to them and see", "which one you would like to hire?").also { stage = 5 }
                }
            }
            5 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ChiefServantDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.CHIEF_SERVANT_4245)
}

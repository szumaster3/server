package content.region.kandarin.dialogue.ardougne

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ZookeeperDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hi!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Welcome to the Ardougne Zoo! How can I help you?").also { stage++ }
            1 -> options("Do you have any quests for me?", "Where did you get the animals from?").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Do you have any quests for me?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Where did you get the animals from?").also { stage = 5 }
                }
            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Not at the moment. The explorers that come back from far away lands tell such amazing tales. Make sure you keep eyes and ears open as you may find new places to explore!",
                ).also {
                    stage++
                }
            4 -> playerl(FaceAnim.FRIENDLY, "Ooh, I will!").also { stage = END_DIALOGUE }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "We get them from all over the place! The most exotic creatures have been brought back by explorers and sold to us.",
                ).also {
                    stage++
                }
            6 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Where on Gielinor did you get that scary looking Cyclops?!",
                ).also { stage++ }
            7 -> npcl(FaceAnim.LAUGH, "Yes he is scary looking!").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "He's from a very far away land, we couldn't find out more as the explorer who brought him back died shortly afterwards!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ZookeeperDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ZOO_KEEPER_28)
    }
}

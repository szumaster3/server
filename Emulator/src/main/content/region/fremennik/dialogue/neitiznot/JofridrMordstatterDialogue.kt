package content.region.fremennik.dialogue.neitiznot

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class JofridrMordstatterDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.NEUTRAL, "Hello there. Would you like to see the goods I have for sale?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Yes please, Jofridr.",
                    "No thank you, Jofridr.",
                    "Why do you have so much wool in your store?",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.NEUTRAL, "Yes please, Jofridr.").also { stage = 6 }
                    2 -> playerl(FaceAnim.NEUTRAL, "No thank you, Jofridr.").also { stage = 5 }
                    3 ->
                        playerl(
                            FaceAnim.THINKING,
                            "Why do you have so much wool in your store? I haven't seen any sheep anywhere.",
                        ).also {
                            stage =
                                11
                        }
                }
            5 -> npcl(FaceAnim.NEUTRAL, "Fair thee well.").also { stage = END_DIALOGUE }
            6 -> {
                end()
                openNpcShop(player, NPCs.JOFRIDR_MORDSTATTER_5509)
            }
            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah, I have contacts on the mainland. I have a sailor friend who brings me crates of wool on a regular basis.",
                ).also {
                    stage++
                }
            12 -> playerl(FaceAnim.ASKING, "What do you trade for it?").also { stage++ }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Rope of course! What else can we sell? Fish would go off before it got so far south.",
                ).also {
                    stage++
                }
            14 -> playerl(FaceAnim.ASKING, "Where does all this rope go?").also { stage++ }
            15 ->
                npcl(
                    FaceAnim.THINKING,
                    "Err, I don't remember the name of the place very well. Dreinna? Drennor? Something like that.",
                ).also {
                    stage++
                }
            16 -> playerl(FaceAnim.NEUTRAL, "That's very interesting. Thanks Jofridr.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return JofridrMordstatterDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JOFRIDR_MORDSTATTER_5509)
    }
}

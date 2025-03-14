package content.region.morytania.dialogue.burgh

import core.api.openInterface
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs

@Initializable
class DalcianFangDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello there. What are you doing here?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I'm waiting here hoping that someone can guide me to the Paterdomus, the temple on the Salve.",
                ).also {
                    stage++
                }
            1 ->
                options(
                    "Where is Paterdomus?",
                    "Why do you want to go to Paterdomus?",
                    "Could you defend yourself if we met enemies...?",
                    "I can take you to Paterdomus.",
                    "Ok, thanks.",
                ).also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Where is Paterdomus?").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Why do you want to go to Paterdomus?").also { stage = 4 }
                    3 ->
                        player(FaceAnim.HALF_GUILTY, "Could you defend yourself if we met enemies...?").also {
                            stage =
                                5
                        }
                    4 -> player(FaceAnim.HALF_GUILTY, "I can take you to Paterdomus.").also { stage = 6 }
                    5 -> player(FaceAnim.HALF_GUILTY, "Ok, thanks.").also { stage = END_DIALOGUE }
                }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh, Paterdomus is that rather impressive looking temple on the Salve... I'm sure you must have passed it on your way into Morytania.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I'm hoping to get back to civilisation and see what the lands of Misthalin look like.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Let's just say that I'm somewhat experienced and I'm happy to take on my fair share of bad guys. You don't need to worry about that.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            6 ->
                if (!player.familiarManager.hasFamiliar()) {
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "Oh really? That would be great! I'll give you a small reward once we reach our destination.",
                    ).also {
                        stage++
                    }
                } else {
                    end()
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "Hmmm, it seems that you have a follower. I think it may get lost on this journey if it's left to wander on its own. Let's leave when your follower is safe.",
                    )
                }
            7 -> npcl(FaceAnim.HALF_ASKING, "Ok, which route should we take?").also { stage++ }
            8 -> {
                end()
                openInterface(player, Components.TEMPLETREK_MAP_329)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return DalcianFangDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DALCIAN_FANG_3632)
    }
}

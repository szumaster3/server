package content.region.desert.dialogue.alkharid

import core.api.interaction.openBankAccount
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FadliDialogue(
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
            0 -> npcl(FaceAnim.HALF_ASKING, "What?").also { stage++ }
            1 ->
                showTopics(
                    Topic("What do you do?", 101),
                    Topic("What is this place?", 201),
                    Topic(FaceAnim.FRIENDLY, "I'd like to store some items, please.", 301),
                    Topic("Do you watch any matches?", 401),
                )
            101 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You can store your stuff here if you want. You can dump anything you don't want to carry whilst your fighting duels and then pick it up again on the way out.",
                ).also {
                    stage++
                }
            102 -> npcl(FaceAnim.SAD, "To be honest I'm wasted here.").also { stage++ }
            103 ->
                npcl(
                    FaceAnim.EVIL_LAUGH,
                    "I should be winning duels in an arena! I'm the best warrior in Al-Kharid!",
                ).also {
                    stage++
                }
            104 -> player(FaceAnim.HALF_WORRIED, "Easy, tiger!").also { stage = END_DIALOGUE }
            201 -> npcl(FaceAnim.LAUGH, "Isn't it obvious?").also { stage++ }
            202 -> npcl(FaceAnim.ANNOYED, "This is the Duel Arena...duh!").also { stage = END_DIALOGUE }
            301 -> npcl(FaceAnim.FRIENDLY, "Sure.").also { stage++ }
            302 -> {
                end()
                openBankAccount(player)
            }
            401 ->
                npcl(FaceAnim.LAUGH, "Most aren't any good so I throw rotten fruit at them!").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FadliDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FADLI_958)
    }
}

package content.minigame.barrows.dialogue

import core.api.addItemOrDrop
import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class StrangeOldManDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var conversationNum = RandomFunction.getRandom(4)

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (inInventory(player, Items.SPADE_952)) {
            when (conversationNum) {
                1 ->
                    when (stage) {
                        0 -> npcl(FaceAnim.ASKING, "Pst, wanna hear a secret?").also { stage++ }
                        1 -> options("Sure!", "No, thanks.").also { stage++ }
                        2 ->
                            when (buttonId) {
                                1 -> player("Sure!").also { stage++ }
                                2 -> player("No, thanks.").also { stage = END_DIALOGUE }
                            }

                        3 -> npcl(FaceAnim.LAUGH, "They're not normal!").also { stage = END_DIALOGUE }
                    }

                2 ->
                    when (stage) {
                        0 -> npcl(FaceAnim.NEUTRAL, "Knock, knock.").also { stage++ }
                        1 -> playerl(FaceAnim.ASKING, "Who's there?").also { stage++ }
                        2 -> npcl(FaceAnim.LAUGH, "A big scary monster, HAHAHAHAHAHAHAHAHAHA!").also { stage++ }
                        3 -> playerl(FaceAnim.HALF_ROLLING_EYES, "Okay...").also { stage = END_DIALOGUE }
                    }

                3 ->
                    when (stage) {
                        0 -> npcl(FaceAnim.HALF_ASKING, "What? I didn't ask for a book!").also { stage++ }
                        1 -> {
                            end()
                            addItemOrDrop(player, Items.CRUMBLING_TOME_4707, 1)
                            stage = END_DIALOGUE
                        }
                    }

                4 ->
                    when (stage) {
                        0 -> npcl(FaceAnim.FURIOUS, "AAAAAAAAARRRRRRGGGGGHHHHHHHH!").also { stage++ }
                        1 -> options("What's wrong?", "I'll leave you to it, then...").also { stage++ }
                        2 ->
                            when (buttonId) {
                                1 ->
                                    npcl(
                                        FaceAnim.FURIOUS,
                                        "AAAAAAAAARRRRRRGGGGGHHHHHHHH!",
                                    ).also { stage = END_DIALOGUE }
                                2 ->
                                    playerl(FaceAnim.STRUGGLE, "I'll leave you to it, then...").also {
                                        stage =
                                            END_DIALOGUE
                                    }
                            }
                    }
            }
        } else {
            when (stage) {
                0 -> npcl(FaceAnim.HALF_ASKING, "Dig, dig, DIG! You want to dig? You need a spade!").also { stage++ }
                1 -> playerl(FaceAnim.ASKING, "Yes you're right, I probably do. Where can I get one?").also { stage++ }
                2 ->
                    npcl(
                        FaceAnim.FRIENDLY,
                        "Ohh... Spades have cost me so much hassle - always being pestered for them! I ended up giving in and just putting one at each mound for you forgetful adventurers to use.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.STRANGE_OLD_MAN_2024)
}

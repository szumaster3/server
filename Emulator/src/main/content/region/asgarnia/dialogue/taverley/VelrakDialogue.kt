package content.region.asgarnia.dialogue.taverley

import core.api.addItem
import core.api.inInventory
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class VelrakDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        if (inInventory(player, Items.DUSTY_KEY_1590)) {
            playerl(FaceAnim.HALF_THINKING, "Are you still here?").also { stage = 100 }
        } else {
            npcl(FaceAnim.FRIENDLY, "Thank you for rescuing me! It isn't very comfy in this cell.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "So... do you know anywhere good to explore?",
                    "Do I get a reward for freeing you?",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 ->
                        playerl(FaceAnim.HALF_THINKING, "So... do you know anywhere good to explore?").also {
                            stage =
                                10
                        }
                    2 -> playerl(FaceAnim.FRIENDLY, "Do I get a reward for freeing you?").also { stage = 20 }
                }

            10 ->
                npcl(
                    FaceAnim.HALF_THINKING,
                    "Well, this dungeon was quite good to explore...until I got captured, anyway. I was given a key to an inner part of this dungeon by a mysterious stranger!",
                ).also { stage++ }

            11 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "It's rather tough for me to get that far into the dungeon however. I just keep getting captured! Would you like to give it a go?",
                ).also { stage++ }

            12 -> options("Yes, please!", "No, it's too dangerous for me too.").also { stage++ }
            13 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes, please!").also { stage++ }
                    2 -> playerl(FaceAnim.NEUTRAL, "No, it's too dangerous for me too.").also { stage = 15 }
                }

            14 ->
                sendItemDialogue(
                    player,
                    Items.DUSTY_KEY_1590,
                    "Velrak reaches somewhere mysterious and passes you a key.",
                ).also {
                    addItem(player, Items.DUSTY_KEY_1590, 1)
                    stage = END_DIALOGUE
                }

            15 -> npcl(FaceAnim.FRIENDLY, "I don't blame you!").also { stage = END_DIALOGUE }
            20 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, not really. The Black Knights took all of my stuff before throwing me in here to rot!",
                ).also { stage = 0 }

            100 ->
                npcl(
                    FaceAnim.HALF_THINKING,
                    "Yes... I'm still plucking up the courage to run out past those Black Knights.",
                ).also { stage++ }

            101 -> playerl(FaceAnim.FRIENDLY, "Oh, go on. You can do it!").also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.VELRAK_THE_EXPLORER_798)
    }
}

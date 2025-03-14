package content.region.desert.dialogue.sophanem

import core.api.sendChat
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SphinxDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (player.familiarManager.hasPet() &&
            player.familiarManager.familiar.id >= 761 &&
            player.familiarManager.familiar.id < 767
        ) {
            player("Good day.").also { stage = 7 }
        } else {
            player("Good day.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("You have the feel of a cat person", "about you. Do you look after one?").also { stage++ }
            1 ->
                options(
                    "Yes, but I don't bring it to harsh places like this.",
                    "No, you are mistaken.",
                    "Yes, but I have left mine in the bank.",
                ).also {
                    stage++
                }
            2 ->
                when (buttonId) {
                    1 -> player("Yes, but I don't bring it to harsh places like this.").also { stage++ }
                    2 -> player("No, you are mistaken.").also { stage = 5 }
                    3 -> player("Yes, but I have left mine in the bank.").also { stage = 6 }
                }
            3 ->
                npc(
                    FaceAnim.SAD,
                    "A pity, they can be of great help in",
                    "some adventures. I would like to talk to",
                    "your cat. Would you bring it to me?",
                ).also {
                    stage++
                }
            4 -> player("I might, but I have a few things to sort out first.").also { stage = END_DIALOGUE }
            5 ->
                npc(FaceAnim.SUSPICIOUS, "Really? I'm generally quite good at knowing", "these things.").also {
                    stage =
                        END_DIALOGUE
                }
            6 -> npc(FaceAnim.AFRAID, "What?  That's no place for a cat!").also { stage = END_DIALOGUE }
            7 -> sendDialogue(player, "The Sphinx ignores you.").also { stage++ }
            8 -> npc("Ah, how interesting... a cat. Come here to", "me, kitty.").also { stage++ }
            9 -> sendChat(player.familiarManager.familiar, "Meow").also { stage++ }
            10 -> sendDialogue(player, "The Sphinx and the cat have a chat.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPHINX_1990)
    }
}

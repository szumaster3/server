package content.region.misthalin.handlers.stronghold

import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import org.rs.consts.Items

/**
 * Represents the Box of health dialogue.
 */
class BoxOfHealthDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        sendDialogue(player, "The box hinges creak and appear to be forming audible words....")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                if (!addItem(player, Items.COINS_995, 5000, Container.INVENTORY)) {
                    sendMessage(player, "You don't have enough inventory space.")
                    end()
                }
                stage = 1
                sendDialogueLines(
                    player,
                    "...congratulations adventurer, you have been deemed worthy of this",
                    "reward. You have also unlocked the Idea emote!",
                )
                player.emoteManager.unlock(Emotes.IDEA)
                player.getSavedData().globalData.getStrongHoldRewards()[2] = true
            }

            1 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(96878)
    }
}

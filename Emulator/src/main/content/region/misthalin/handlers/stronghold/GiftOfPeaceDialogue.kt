package content.region.misthalin.handlers.stronghold

import core.api.Container
import core.api.addItem
import core.api.sendDialogueLines
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import org.rs.consts.Items

/**
 * Represents the Gift of peace dialogue.
 */
class GiftOfPeaceDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        interpreter.sendDialogue("The box hinges creak and appear to be forming audible words....")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                if (!addItem(player, Items.COINS_995, 2000, Container.INVENTORY)) {
                    sendMessage(player, "You don't have enough inventory space.")
                    end()
                }
                sendDialogueLines(
                    player,
                    "...congratulations adventurer, you have been deemed worthy of this",
                    "reward. You have also unlocked the Flap emote!",
                )
                stage = 1
                player.emoteManager.unlock(Emotes.FLAP)
                player.getSavedData().globalData.getStrongHoldRewards()[0] = true
            }

            1 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(54678)
    }
}

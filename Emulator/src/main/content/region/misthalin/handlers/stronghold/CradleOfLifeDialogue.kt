package content.region.misthalin.handlers.stronghold

import core.api.*
import core.game.container.Container
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items

/**
 * Represents the Cradle of life dialogue.
 */
class CradleOfLifeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (player.getSavedData().globalData.getStrongHoldRewards()[3] &&
            player.getSavedData().globalData.getStrongHoldRewards()[2] &&
            player.getSavedData().globalData.getStrongHoldRewards()[1] &&
            player.getSavedData().globalData.getStrongHoldRewards()[0]
        ) {
            /*
             * Unlocks emotes for older accounts who don't have the previous level emotes.
             */
            player.emoteManager.unlock(Emotes.FLAP)
            player.emoteManager.unlock(Emotes.SLAP_HEAD)
            player.emoteManager.unlock(Emotes.IDEA)
        }
        if (player.inventory.freeSlots() == 0) {
            player.packetDispatch.sendMessage("You don't have enough inventory space.")
            end()
            return true
        }
        if (player.getSavedData().globalData.getStrongHoldRewards()[3]) {
            sendDialogueLines(
                player,
                "As your hand touches the cradle, you hear a voice in your head of a",
                "million dead adventurers....",
            )
            stage = 100
            return true
        }
        sendDialogueLines(
            player,
            "As your hand touches the cradle, you hear a voice in your head of a",
            "million dead adventurers....",
        )
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                sendDialogue(player, "....welcome adventurer... you have a choice....")
                stage = 1
            }

            1 -> {
                sendDoubleItemDialogue(
                    player,
                    Items.FANCY_BOOTS_9005,
                    Items.FIGHTING_BOOTS_9006,
                    "You can choose between these two pair of boots.",
                )
                stage = 2
            }

            2 -> {
                sendDialogueLines(
                    player,
                    "They will both protect your feet exactly the same, however they look",
                    "very different. You can always come back and get another pair if",
                    "you lose them, or even swap them for the other style!",
                )
                stage = 4
            }

            4 -> {
                sendDialogueOptions(
                    player,
                    "Choose your style of boots",
                    "I'll take the colourful ones!",
                    "I'll take the fighting ones!",
                )
                stage = 5
            }

            5 ->
                when (buttonId) {
                    1 -> {
                        playerl(FaceAnim.HALF_GUILTY, "I'll take the colourful ones!")
                        player.inventory.add(ITEMS[0])
                        player.emoteManager.unlock(Emotes.STOMP)
                        stage = 6
                        player.getSavedData().globalData.getStrongHoldRewards()[3] = true
                    }

                    2 -> {
                        playerl(FaceAnim.HALF_GUILTY, "I'll take the fighting ones!")
                        player.inventory.add(ITEMS[1])
                        player.emoteManager.unlock(Emotes.STOMP)
                        player.getSavedData().globalData.getStrongHoldRewards()[3] = true
                        stage = 6
                    }
                }

            6 -> {
                sendDialogueLines(
                    player,
                    "Congratulations! You have succesfully navigated the Stronghold of",
                    "Security and learned to secure your account. You have unlocked",
                    "the 'Stamp Foot' emote. Remember to keep your account secure in",
                    "the future!",
                )
                stage = 7
            }

            7 -> end()
            100 ->
                if (!inInventory(player, Items.FANCY_BOOTS_9005, 1) &&
                    !inBank(player, Items.FIGHTING_BOOTS_9006, 1) &&
                    !inEquipment(player, Items.FANCY_BOOTS_9005, 1) &&
                    !inInventory(player, Items.FIGHTING_BOOTS_9006, 1) &&
                    !inBank(player, Items.FIGHTING_BOOTS_9006, 1) &&
                    !inBank(player, Items.FIGHTING_BOOTS_9006, 1)
                ) {
                    sendDialogue(player, "You appear to have lost your boots!").also { stage = 101 }
                } else {
                    options("Yes, I'd like the other pair instead please!", "No thanks, I'll keep these!").also {
                        stage =
                            200
                    }
                }

            200 ->
                when (buttonId) {
                    1 ->
                        playerl(FaceAnim.HALF_GUILTY, "Yes, I'd like the other pair instead please!").also {
                            stage =
                                800
                        }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "No thanks, I'll keep these!").also { stage = END_DIALOGUE }
                }

            800 -> {
                if (!swap(player.inventory)) {
                    if (!swap(player.equipment)) {
                        swap(player.bank)
                    }
                }
                end()
            }
            101 -> {
                sendDialogue(player, "....welcome adventurer... you have a choice....").also { stage = 1 }
            }
        }
        return true
    }

    fun swap(container: Container): Boolean {
        if (container.contains(9005, 1)) {
            container.replace(Item(9006), container.getSlot(ITEMS[0]))
            return true
        }
        if (container.contains(9006, 1)) {
            container.replace(ITEMS[0], container.getSlot(ITEMS[1]))
            return true
        }
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(96873)
    }

    companion object {
        private val ITEMS = arrayOf(Item(9005, 1), Item(9006, 1))
    }
}

package content.region.kandarin.dialogue.ardougne

import content.global.travel.charter.Charter
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items

class CaptainBarnabyDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        var amount = if (isDiaryComplete(player!!, DiaryType.KARAMJA, 0)) 15 else 30
        when (stage) {
            0 -> npcl("Do you want to go on a trip to Brimhaven?").also { stage++ }
            1 -> npcl("The trip will cost you 30 coins.").also { stage++ }
            2 -> options("Yes please.", "No, thank you.").also { stage++ }
            3 ->
                when (buttonID) {
                    1 ->
                        if (isDiaryComplete(player!!, DiaryType.KARAMJA, 0)) {
                            npcl(
                                "Wait a minute, didn't you earn Karamja gloves? Thought I'd seen you helping around the island. You can go on half price.",
                            ).also {
                                stage++
                            }
                        } else {
                            playerl("Yes please.").also { stage++ }
                        }

                    2 -> playerl("No, thank you.").also { stage = END_DIALOGUE }
                }

            4 -> {
                end()
                if (!inInventory(player!!, Items.COINS_995, amount)) {
                    sendMessage(player!!, "You can not afford that.").also { stage = END_DIALOGUE }
                }
                removeItem(player!!, Item(Items.COINS_995, amount))
                sendMessage(player!!, "You pay $amount coins and board the ship.")
                playJingle(player!!, 171)
                sendDialogue(player!!, "The ship arrives at Brimhaven.")
                Charter.ARDOUGNE_TO_BRIMHAVEN.sail(player!!)
            }
        }
    }
}

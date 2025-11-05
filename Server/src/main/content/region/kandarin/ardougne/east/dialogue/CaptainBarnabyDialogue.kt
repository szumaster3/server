package content.region.kandarin.ardougne.east.dialogue

import content.global.travel.ship.CharterShip
import core.api.inEquipment
import core.api.isDiaryComplete
import core.api.removeItem
import core.api.sendMessage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import shared.consts.Items

/**
 * Represents dialogue extension for Captain barnaby who takes players
 * from East Ardougne to Brimhaven by his ship.
 */
class CaptainBarnabyDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val hasCharosRing = inEquipment(player!!, Items.RING_OF_CHAROSA_6465)
        val amount = if (isDiaryComplete(player!!, DiaryType.KARAMJA, 0)) 15 else 30
        when (stage) {
            0 -> {
                npcl("Do you want to go on a trip to Brimhaven?")
                stage++
            }

            1 -> {
                npcl("The trip will cost you 30 coins.")
                stage++
            }

            2 -> {
                if (hasCharosRing) {
                    options("Yes please.", "No, thank you.", "Or I could pay you nothing at all...")
                } else {
                    options("Yes please.", "No, thank you.")
                }
                stage++
            }

            3 -> {
                when (buttonID) {
                    1 -> {
                        if (isDiaryComplete(player!!, DiaryType.KARAMJA, 0) || player!!.achievementDiaryManager.hasGlove()) {
                            npcl("Wait a minute, didn't you earn Karamja gloves? Thought I'd seen you helping around the island. You can go on half price.")
                            stage = 4
                        } else {
                            playerl("Yes please.")
                            stage = 4
                        }
                    }

                    2 -> {
                        playerl("No, thank you.")
                        stage = END_DIALOGUE
                    }

                    3 -> {
                        playerl("Or I could pay you nothing at all...")
                        stage = 5
                    }
                }
            }

            4 -> {
                end()
                if (!removeItem(player!!, Item(Items.COINS_995, amount))) {
                    player(FaceAnim.SAD, "Oh dear, I don't seem to have enough money.")
                    sendMessage(player!!, "You can not afford that.")
                } else {
                    sendMessage(player!!, "You pay $amount coins and board the ship.")
                    CharterShip.ARDOUGNE_TO_BRIMHAVEN.sail(player!!)
                }
            }

            5 -> {
                npcl("Mmmm, nothing at all, you say...")
                stage++
            }

            6 -> {
                npcl("Yes, why not - jump aboard then.")
                stage++
            }

            7 -> {
                end()
                sendMessage(player!!, "You board the ship.")
                CharterShip.ARDOUGNE_TO_BRIMHAVEN.sail(player!!)
            }
        }
    }
}

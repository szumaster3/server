package content.region.kandarin.ardougne.east.dialogue

import content.global.travel.charter.Charter
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items

/**
 * Represents dialogue extension for Captain barnaby who takes players
 * from East Ardougne to Brimhaven by his ship.
 */
class CaptainBarnabyDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        // Check for charos ring.
        val hasCharosRing = inEquipment(player!!, Items.RING_OF_CHAROSA_6465)
        // Complete diary.
        val amount = if (isDiaryComplete(player!!, DiaryType.KARAMJA, 0)) 15 else 30

        // val quest = isQuestComplete(player!!, Quests.MY_ARMS_BIG_ADVENTURE)
        // npcl("Oh no, it's you again!")

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
                        if (isDiaryComplete(player!!, DiaryType.KARAMJA, 0)) {
                            npcl("Wait a minute, didn't you earn Karamja gloves? Thought I'd seen you helping around the island. You can go on half price.")
                            stage = 5
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
                        stage = 6
                    }
                }
            }

            4 -> {
                end()
                if (!removeItem(player!!, Item(Items.COINS_995, amount))) {
                    sendMessage(player!!, "You do not have enough coins to pay for the trip.")
                } else {
                    sendMessage(player!!, "You pay $amount coins and board the ship.")
                    playJingle(player!!, 171)
                    sendDialogue(player!!, "The ship arrives at Brimhaven.")
                    Charter.ARDOUGNE_TO_BRIMHAVEN.sail(player!!)
                }
            }

            5 -> {
                end()
                if (!removeItem(player!!, Item(Items.COINS_995, amount))) {
                    sendMessage(player!!, "You do not have enough coins to pay for the trip.")
                } else {
                    sendMessage(player!!, "You pay $amount coins and board the ship.")
                    playJingle(player!!, 171)
                    sendDialogue(player!!, "The ship arrives at Brimhaven.")
                    Charter.ARDOUGNE_TO_BRIMHAVEN.sail(player!!)
                }
            }

            6 -> {
                npcl("Mmmm, nothing at all, you say...")
                stage++
            }

            7 -> {
                npcl("Yes, why not - jump aboard then.")
                stage++
            }

            8 -> {
                end()
                sendMessage(player!!, "You board the ship.")
                playJingle(player!!, 171)
                sendDialogue(player!!, "The ship arrives at Brimhaven.")
                Charter.ARDOUGNE_TO_BRIMHAVEN.sail(player!!)
            }
        }
    }
}

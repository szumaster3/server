package content.region.asgarnia.port_sarim.dialogue

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Gerrant dialogue.
 */
class GerrantDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.HAPPY, "Welcome! You can buy fishing equipment at my store.", "We'll also buy anything you catch off you.").also { stage++ }
            1 -> showTopics(
                Topic("Let's see what you've got then.", 2, false),
                Topic("Sorry, I'm not interested.", END_DIALOGUE),
                IfTopic(FaceAnim.HALF_GUILTY, "I've lost my oily fishing rod.", 7, isQuestComplete(player!!, Quests.HEROES_QUEST) && !inInventory(player!!, Items.OILY_FISHING_ROD_1585) && !inBank(player!!, Items.OILY_FISHING_ROD_1585)),
                IfTopic("I want to find out how to catch a lava eel.", 3, isQuestInProgress(player!!, Quests.HEROES_QUEST, 1, 99)),
            )
            2 -> end().also { openNpcShop(player!!, NPCs.GERRANT_558) }
            3 -> npc(FaceAnim.HALF_THINKING,"Lava eels eh? That's a tricky one that is, you'll need a", "lava-proof fishing line. The method for making this would", "be to take an ordinary fishing rod, and then cover it", "with the fire-proof Blamish Oil.").also { stage++ }
            4 -> if (isQuestComplete(player!!, Quests.HEROES_QUEST)) {
                npc(FaceAnim.FRIENDLY,"Of course, you knew that already.").also { stage = 7 }
            } else {
                npc(FaceAnim.THINKING,"You know... thinking about it... I may have a jar of", "Blamish Slime around here somewhere... Now where did", "I put it?").also { stage++ }
            }
            5 -> sendDialogue(player!!, "Gerrant search around a bit.").also { stage++ }
            6 -> {
                end()
                npc(FaceAnim.HAPPY,"Aha! Here it is! Take this slime, mix it with some", "Harralander and water and you'll have the Blamish Oil", "you need for treating your fishing rod.")
                addItem(player!!, Items.BLAMISH_SNAIL_SLIME_1581, 1)
            }

            7 -> player(FaceAnim.HALF_ASKING, "So where can I fish lava eels?").also { stage++ }
            8 -> npc(FaceAnim.NEUTRAL,"Taverley dungeon or the lava maze in the Wilderness.").also { stage = END_DIALOGUE }
            9 -> {
                end()
                sendItemDialogue(player!!, Items.OILY_FISHING_ROD_1585, "Gerrant replaces your oily fishing rod.")
                addItem(player!!, Items.OILY_FISHING_ROD_1585, 1)
            }
        }
    }
}

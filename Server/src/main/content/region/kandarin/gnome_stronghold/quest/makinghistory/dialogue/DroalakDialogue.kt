package content.region.kandarin.gnome_stronghold.quest.makinghistory.dialogue

import content.region.kandarin.gnome_stronghold.quest.makinghistory.MHUtils
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class DroalakDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        val questStage = getQuestStage(player, Quests.MAKING_HISTORY)
        val droalakProgress = getVarbit(player, MHUtils.DROALAK_PROGRESS)

        return when {
            !inEquipment(player, Items.GHOSTSPEAK_AMULET_552) -> {
                npcl(FaceAnim.FRIENDLY, "wooo wooo")
                stage = 31
                true
            }

            inEquipment(player, Items.GHOSTSPEAK_AMULET_552) && questStage < 1 -> {
                npcl(FaceAnim.FRIENDLY, "Please, leave me alone.")
                stage = 24
                true
            }

            droalakProgress == 0 -> {
                playerl(FaceAnim.FRIENDLY, "Hello. Are you Droalak?")
                stage = 1
                true
            }

            droalakProgress == 2 && !inInventory(player, Items.SAPPHIRE_AMULET_1694) -> {
                playerl(FaceAnim.FRIENDLY, "What do you want me to do again?")
                stage = 17
                true
            }

            droalakProgress == 2 && inInventory(player, Items.SAPPHIRE_AMULET_1694) -> {
                playerl(FaceAnim.FRIENDLY, "I have a sapphire amulet!")
                stage = 16
                true
            }

            droalakProgress == 4 -> {
                playerl(FaceAnim.FRIENDLY, "I've given her the amulet. She was very pleased and said she just wanted to know you still cared.")
                stage = 19
                true
            }

            droalakProgress == 5 -> {
                val hasScroll = hasAnItem(player, Items.SCROLL_6758).exists()
                val dialogueText = if (hasScroll)
                    "Take that scroll to Jorral in the outpost."
                else
                    "Thanks for the scroll, but I seem to have lost it."

                npcl(FaceAnim.FRIENDLY, dialogueText)
                stage = if (hasScroll) END_DIALOGUE else 25
                true
            }

            droalakProgress == 6 -> {
                playerl(FaceAnim.FRIENDLY, "I have delivered the scroll; you can rest in peace now.")
                stage = 27
                true
            }

            questStage >= 1 -> {
                playerl(FaceAnim.FRIENDLY, "Hello. Are you Droalak?")
                stage = 1
                true
            }

            else -> true
        }
    }


    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> sendDialogue(player, "You cannot understand the ghost").also { stage = END_DIALOGUE }
            1 -> npcl(FaceAnim.FRIENDLY, "Wow. I haven't spoken to the living for... for... I don't remember how long.").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "So your name IS Droalak?").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Sorry, yes. I am he.").also { stage++ }
            4 -> playerl(FaceAnim.FRIENDLY, "Great. Do you know anything about the outpost north of Ardougne?").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "I don't really like to talk about it, but I died there.").also { stage++ }
            6 -> playerl(FaceAnim.FRIENDLY, "Oh dear.").also { stage++ }
            7 -> npcl(FaceAnim.FRIENDLY, "I do have a scroll which might interest you that describes the timeline of the outpost. But first I wonder if I could ask you to tie up a problem?").also { stage++ }
            8 -> playerl(FaceAnim.FRIENDLY, "Like what?").also { stage++ }
            9 -> npcl(FaceAnim.FRIENDLY, "Well, I left to go to the outpost against the wishes of my wife. I promised I would return to her, but obviously I did not as I died there. She's a ghost nearby, but won't listen to my apologies.").also { stage++ }
            10 -> playerl(FaceAnim.FRIENDLY, "You want me to patch things up?").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "Yes, how'd you guess?").also { stage++ }
            12 -> playerl(FaceAnim.FRIENDLY, "Call it 'traveller's intuition'.").also { stage++ }
            13 -> npcl(FaceAnim.FRIENDLY, "OK, well perhaps you could give her a strung sapphire amulet, because this is what I gave her the day I left. Her name is Melina by the way.").also { stage++ }
            14 -> playerl(FaceAnim.FRIENDLY, "No problem.").also { stage++ }
            15 -> {
                end()
                setVarbit(player, MHUtils.DROALAK_PROGRESS, 2, true)
            }
            16 -> npcl(FaceAnim.FRIENDLY, "Good work. Just give it to Melina, who's wandering somewhere nearby.").also { stage = END_DIALOGUE }
            17 -> npcl(FaceAnim.FRIENDLY, "Make a strung sapphire amulet and give it to Melina!").also { stage++ }
            18 -> playerl(FaceAnim.FRIENDLY, "Ok. Ok.").also { stage = END_DIALOGUE }
            19 -> npcl(FaceAnim.FRIENDLY, "Excellent! I am so glad she believes me. I can finally rest in peace.").also { stage++ }
            20 -> playerl(FaceAnim.FRIENDLY, "Could I have that scroll you mentioned first?").also { stage++ }
            21 -> npcl(FaceAnim.FRIENDLY, "Of course. Let me know if it was of any use and then I can be forever free.").also { stage++ }
            22 -> playerl(FaceAnim.FRIENDLY, "Thank you.").also { stage++ }
            23 -> {
                end()
                addItemOrDrop(player, Items.SCROLL_6758)
                setVarbit(player, MHUtils.DROALAK_PROGRESS, 5, true)
                setAttribute(player, MHUtils.ATTRIBUTE_DROALAK_PROGRESS, true)
            }
            24 -> playerl(FaceAnim.FRIENDLY, "Your loss!").also { stage = END_DIALOGUE }
            25 -> npcl(FaceAnim.FRIENDLY, "It's a good job I stuck around then, isn't it! Have another copy.").also { stage++ }
            26 -> {
                end()
                addItemOrDrop(player, Items.SCROLL_6758)
            }
            27 -> npcl(FaceAnim.FRIENDLY, "Thanks for telling me! I've been waiting for ages!").also { stage++ }
            28 -> playerl(FaceAnim.FRIENDLY, "Goodbye.").also { stage++ }
            29 -> npcl(FaceAnim.FRIENDLY, "Bye!").also { stage++ }
            30 -> {
                end()
                transformNpc(NPC(NPCs.DROALAK_2938), NPCs.DROALAK_2937, 10)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.DROALAK_2938)
}

package content.global.skill.slayer.waterbirth_dungeon

import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.*
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents Bardur dialogue.
 */
class BardurDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npcl(FaceAnim.ANNOYED, "Ah! Outlander! Do not interrupt me in my business! I must cull these fiends!")
            return true
        }

        val name = FremennikTrials.getFremennikName(player)
        if(name.equals(npc.name, true)) {
            npcl(FaceAnim.HAPPY, if (!player.isMale) {
                "Ah, Bardur my sister-in-name! It is good fortune that we meet here like this!"
            } else {
                "Ah, Bardur my brother-in-name! It is good fortune that we meet here like this!" }
            )
            stage = 4
        } else {
            npcl(FaceAnim.FRIENDLY, "Hello there Bardur. How's it going?")
            stage = 4
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "What are you doing down here?").also { stage++ }
            1 -> npcl(FaceAnim.NEUTRAL, "Only an Outlander would ask such a question! Is it not obvious what I am doing?").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "I kill the daggermouths for glory and for sport! When I have had my fill, I move on to the daggermouths' lair!").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "Erm... okay then. I'll leave you to it.").also { stage = END_DIALOGUE }
            4 -> npcl(FaceAnim.NEUTRAL, "Listen, ${FremennikTrials.getFremennikName(player)}. I'm sorry about the way I acted while you were exiled.").also { stage++ }
            5 -> sendDialogue(player!!, "Bardur gets hostile around outlanders.").also { stage++ }
            6 -> playerl(FaceAnim.FRIENDLY, "Don't worry about it, I understand! All's well that ends well! How've you been?").also { stage++ }
            7 -> npcl(FaceAnim.NEUTRAL, "I have been here all the long day, slaughtering the daggermouth spawns so that none may pass!").also { stage++ }
            8 -> npcl(FaceAnim.NEUTRAL, "My sword arm grows weary, and the blood of my foes hangs heavy upon my clothing!").also { stage++ }
            9 -> playerl(FaceAnim.FRIENDLY, "Can you tell me anything about the caves down here?").also { stage++ }
            10 -> npcl(FaceAnim.NEUTRAL, "Aye, that I can ${FremennikTrials.getFremennikName(player)}! Yonder lies the lair of the daggermouth kings, the three beasts of legend!").also { stage++ }
            11 -> npcl(FaceAnim.NEUTRAL, "I train my skills upon its foul brood, to prepare myself for the fight ahead!").also { stage++ }
            12 -> playerl(FaceAnim.FRIENDLY, "Okay, thanks Bardur. Do you have any food with you?").also { stage++ }
            13 -> npcl(FaceAnim.NEUTRAL, "Ah, you did not come prepared ${FremennikTrials.getFremennikName(player)}?").also { stage++ }
            14 -> npcl(FaceAnim.NEUTRAL, "I have nothing to spare, but my equipment grows weaker under the onslaught of the dagger-mouth spawns!").also { stage++ }
            15 -> npcl(FaceAnim.FRIENDLY, "I will trade you a finely cooked shark if you have a replacement for my helm, my shield or my blade...").also { stage++ }
            16 -> if (!anyInInventory(player, *BardurPlugin.exchangeItemIDs)) {
                playerl(FaceAnim.SAD, "Sorry, I don't have anything you'd be after...").also { stage = END_DIALOGUE }
            } else {
                npcl(FaceAnim.NEUTRAL, "Give me any equipment you wish to trade, and I will pay you a shark for it.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = BardurDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.BARDUR_2879)
}

/**
 * Represents Bardur exchange dialogue.
 */
class BardurExchangeDialogue : DialogueFile() {

    private val tradableItems = listOf(
        Items.FREMENNIK_HELM_3748,
        Items.FREMENNIK_BLADE_3757,
        Items.FREMENNIK_SHIELD_3758
    )

    override fun handle(componentID: Int, buttonID: Int) {
        val player = player ?: return
        npc = NPC(NPCs.BARDUR_2879)
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Ah, just what I was looking for! You wish to trade me that for a cooked shark?").also { stage++ }
            1 -> sendOptions(player, "Trade with Bardur?", "YES", "NO").also { stage++ }
            2 -> when (buttonID) {
                1 -> {
                    var tradedCount = 0
                    for (item in tradableItems) {
                        while (inInventory(player, item) && removeItem(player, item)) {
                            tradedCount++
                        }
                    }
                    if (tradedCount > 28) {
                        end()
                        return
                    }
                    if (tradedCount > 0) {
                        addItemOrDrop(player, Items.SHARK_385, tradedCount)
                        npcl(FaceAnim.FRIENDLY, "Ah, this will serve me well as a backup! My thanks to you ${FremennikTrials.getFremennikName(player)}, I trust we will one day sing songs together of glorious battles in the Rellekka longhall!")
                    } else {
                        npcl(FaceAnim.FRIENDLY, "It seems you have nothing I can trade for, ${FremennikTrials.getFremennikName(player)}.")
                    }
                    stage = 3
                }
                2 -> npcl(FaceAnim.FRIENDLY, "As you wish, ${FremennikTrials.getFremennikName(player)}. My weapons have lasted me this long, I will keep my trust in them yet.").also { stage = 3 }
            }
            3 -> end()
        }
    }
}
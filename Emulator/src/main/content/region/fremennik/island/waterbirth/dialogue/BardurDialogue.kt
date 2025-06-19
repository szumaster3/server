package content.region.fremennik.island.waterbirth.dialogue

import content.region.fremennik.island.waterbirth.plugin.BardurPlugin
import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.anyInInventory
import core.api.quest.isQuestComplete
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BardurDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npcl(FaceAnim.FRIENDLY, "Ah! Outlander! Do not interrupt me in my business! I must cull these fiends!")
            return true
        }

        if (player.username.equals("Bardur", true)) {
            npcl(
                FaceAnim.HAPPY,
                if (!player.isMale) {
                    "Ah, Bardur my sister-in-name! It is good fortune that we meet here like this!"
                } else {
                    "Ah, Bardur my brother-in-name! It is good fortune that we meet here like this!"
                },
            ).also { stage = 4 }
        } else {
            npcl(FaceAnim.FRIENDLY, "Hello there Bardur. How's it going?").also { stage = 4 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "What are you doing down here?").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Only an Outlander would ask such a question! Is it not obvious what I am doing?").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "I kill the daggermouths for glory and for sport! When I have had my fill, I move on to the daggermouths' lair!").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "Erm... okay then. I'll leave you to it.").also { stage = END_DIALOGUE }
            5 -> npcl(FaceAnim.FRIENDLY, "Listen, ${FremennikTrials.getFremennikName(player)}. I'm sorry about the way I acted while you were exiled.").also { stage++ }
            6 -> sendDialogue(player!!, "Bardur gets hostile around outlanders.").also { stage++ }
            7 -> playerl(FaceAnim.FRIENDLY, "Don't worry about it, I understand! All's well that ends well! How've you been?").also { stage++ }
            8 -> npcl(FaceAnim.FRIENDLY, "I have been here all the long day, slaughtering the daggermouth spawns so that none may pass!").also { stage++ }
            9 -> npcl(FaceAnim.FRIENDLY, "My sword arm grows weary, and the blood of my foes hangs heavy upon my clothing!").also { stage++ }
            10 -> playerl(FaceAnim.FRIENDLY, "Can you tell me anything about the caves down here?").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "Aye, that I can ${FremennikTrials.getFremennikName(player)}! Yonder lies the lair of the daggermouth kings, the three beasts of legend!").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "I train my skills upon its foul brood, to prepare myself for the fight ahead!").also { stage++ }
            13 -> playerl(FaceAnim.FRIENDLY, "Okay, thanks Bardur. Do you have any food with you?").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "Ah, you did not come prepared ${FremennikTrials.getFremennikName(player)}?").also { stage++ }
            15 -> npcl(FaceAnim.FRIENDLY, "I have nothing to spare, but my equipment grows weaker under the onslaught of the dagger-mouth spawns!").also { stage++ }
            16 -> npcl(FaceAnim.FRIENDLY, "I will trade you a finely cooked shark if you have a replacement for my helm, my shield or my blade...").also { stage++ }
            17 -> if (!anyInInventory(player, *BardurPlugin.exchangeItemIDs)) {
                playerl(FaceAnim.SAD, "Sorry, I don't have anything you'd be after...").also { stage = END_DIALOGUE }
            } else {
                npcl(FaceAnim.FRIENDLY, "Give me any equipment you wish to trade, and I will pay you a shark for it.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = BardurDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.BARDUR_2879)
}

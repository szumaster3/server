package content.region.asgarnia.trollheim.dialogue

import content.region.asgarnia.burthope.quest.death.dialogue.TenzingDialogueFile
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Tenzing dialogue.
 */
@Initializable
class TenzingDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestInProgress(player!!, Quests.DEATH_PLATEAU, 20, 29)) {
            openDialogue(player!!, TenzingDialogueFile(), npc)
        } else {
            player(FaceAnim.FRIENDLY, "Hello Tenzing!").also { stage = 1 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            1 -> npc(FaceAnim.FRIENDLY, "Hello traveler. What can I do for you?").also { stage++ }
            2 -> showTopics(
                Topic("What does a Sherpa do?",6),
                Topic("How did you find out about the secret way?",7),
                IfTopic("Can I buy some Climbing boots?",3, isQuestComplete(player, Quests.DEATH_PLATEAU)),
                Topic("Nice place you have here.",8),
                Topic("Nothing, thanks!", END_DIALOGUE),
            )
            3 -> npcl(FaceAnim.NEUTRAL, "Sure, I'll sell you some in your size for 12 gold.").also { stage++ }
            4 -> showTopics(
                Topic("OK, sounds good.",5, true),
                Topic("No, thanks.", END_DIALOGUE),
            )
            5 -> {
                if (freeSlots(player) < 1) {
                    playerl(FaceAnim.NEUTRAL, "I don't have enough space in my backpack right this second.")
                    stage = END_DIALOGUE
                    end()
                } else if (!removeItem(player, Item(Items.COINS_995, 12))) {
                    playerl(FaceAnim.NEUTRAL, "I don't have enough coins right now.")
                    stage = END_DIALOGUE
                } else {
                    addItemOrDrop(player, Items.CLIMBING_BOOTS_3105, 1)
                    sendItemDialogue(player, Items.CLIMBING_BOOTS_3105, "Tenzing has given you some Climbing boots.")
                    sendMessage(player, "Tenzing has given you some Climbing boots.")
                    stage = 9
                }
            }
            6 -> npc(FaceAnim.FRIENDLY, "We are expert guides that take adventurers such as", "yourself, on mountaineering expeditions.").also {  stage = END_DIALOGUE }
            7 -> npcl(FaceAnim.FRIENDLY, "I used to take adventurers up Death Plateau and further north before the trolls came. I know these mountains well.").also {  stage = END_DIALOGUE }
            8 -> npcl(FaceAnim.FRIENDLY, "Thanks, I built it myself! I'm usually self sufficient but I can't earn any money with the trolls camped on Death Plateau,").also {  stage = END_DIALOGUE }
            9 -> npc("Was there anything else?").also { stage = 2 }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = TenzingDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.TENZING_1071)
}

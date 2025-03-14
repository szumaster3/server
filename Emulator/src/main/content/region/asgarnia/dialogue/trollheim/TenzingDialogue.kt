package content.region.asgarnia.dialogue.trollheim

import content.region.asgarnia.quest.death.dialogue.TenzingDialogueFile
import core.api.*
import core.api.quest.isQuestComplete
import core.api.quest.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class TenzingDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestInProgress(player!!, Quests.DEATH_PLATEAU, 20, 29)) {
            openDialogue(player!!, TenzingDialogueFile(), npc)
        } else {
            player(FaceAnim.FRIENDLY, "Hello Tenzing!").also { stage = 1 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> npc(FaceAnim.FRIENDLY, "Hello traveler. What can I do for you?").also { stage++ }
            2 ->
                if (isQuestComplete(player, Quests.DEATH_PLATEAU)) {
                    options(
                        "Can I buy some Climbing boots?",
                        "What does a Sherpa do?",
                        "How did you find out about the secret way?",
                        "Nice place you have here.",
                        "Nothing, thanks!",
                    ).also {
                        stage =
                            3
                    }
                } else {
                    options(
                        "What does a Sherpa do?",
                        "How did you find out about the secret way?",
                        "Nice place you have here.",
                        "Nothing, thanks!",
                    ).also {
                        stage =
                            4
                    }
                }

            3 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Can I buy some Climbing boots?").also { stage = 5 }
                    2 -> playerl(FaceAnim.FRIENDLY, "What does a Sherpa do?").also { stage = 9 }
                    3 -> playerl(FaceAnim.FRIENDLY, "How did you find out about the secret way?").also { stage = 10 }
                    4 -> playerl(FaceAnim.FRIENDLY, "Nice place you have here.").also { stage = 11 }
                    5 -> playerl(FaceAnim.FRIENDLY, "Nothing, thanks!").also { stage = END_DIALOGUE }
                }
            4 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "What does a Sherpa do?").also { stage = 9 }
                    2 -> playerl(FaceAnim.FRIENDLY, "How did you find out about the secret way?").also { stage = 10 }
                    3 -> playerl(FaceAnim.FRIENDLY, "Nice place you have here.").also { stage = 11 }
                    4 -> playerl(FaceAnim.FRIENDLY, "Nothing, thanks!").also { stage = END_DIALOGUE }
                }
            5 -> npcl(FaceAnim.NEUTRAL, "Sure, I'll sell you some in your size for 12 gold.").also { stage++ }
            6 -> options("OK, sounds good.", "No, thanks.").also { stage++ }
            7 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "OK, sounds good.").also { stage = 8 }
                    2 -> playerl(FaceAnim.FRIENDLY, "No, thanks.").also { stage = END_DIALOGUE }
                }
            8 -> {
                if (freeSlots(player) < 1) {
                    playerl(FaceAnim.NEUTRAL, "I don't have enough space in my backpack right this second.").also {
                        stage =
                            END_DIALOGUE
                    }
                } else if (!removeItem(player!!, Item(Items.COINS_995, 12))) {
                    playerl(FaceAnim.NEUTRAL, "I don't have enough coins right now.").also { stage = END_DIALOGUE }
                    sendItemDialogue(player, Items.CLIMBING_BOOTS_3105, "Tenzing has given you some Climbing boots.")
                    addItemOrDrop(player, Items.CLIMBING_BOOTS_3105, 1)
                    sendMessage(player, "Tenzing has given you some Climbing boots.")
                    stage = 12
                }
            }
            9 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "We are expert guides that take adventurers such as",
                    "yourself, on mountaineering expeditions.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I used to take adventurers up Death Plateau and further north before the trolls came. I know these mountains well.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Thanks, I built it myself! I'm usually self sufficient but I can't earn any money with the trolls camped on Death Plateau,",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            12 -> npc("Was there anything else?").also { stage = 2 }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return TenzingDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TENZING_1071)
    }
}

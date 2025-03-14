package content.region.kandarin.quest.makinghistory.dialogue

import content.region.kandarin.quest.makinghistory.handlers.MakingHistoryUtils
import core.api.*
import core.api.interaction.transformNpc
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class MelinaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        val questStage = getQuestStage(player, Quests.MAKING_HISTORY)
        val droalakProgress = getVarbit(player, MakingHistoryUtils.DROALAK_PROGRESS)

        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            npcl(FaceAnim.FRIENDLY, "wooo wooo")
            return true
        }

        if (questStage >= 1 && inInventory(player, Items.SAPPHIRE_AMULET_1694) && droalakProgress == 2) {
            playerl(FaceAnim.FRIENDLY, "If you don't mind me asking, are you Melina?").also { stage = 4 }
            return true
        }

        if (questStage >= 1) {
            playerl(FaceAnim.FRIENDLY, "Hi.").also { stage = 1 }
        } else {
            npcl(FaceAnim.HALF_GUILTY, "Leave me be.").also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> sendDialogue(player, "You cannot understand the ghost.").also { stage = END_DIALOGUE }
            1 -> npcl(FaceAnim.FRIENDLY, "Oh why did he leave me? Did he truly love me?").also { stage++ }
            2 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Erm. I think you're talking about Droalak. I believe he did love you and he's very sorry for leaving you!",
                ).also { stage++ }

            3 -> npcl(FaceAnim.FRIENDLY, "You're giving me empty words. That is all.").also { stage = END_DIALOGUE }
            4 -> npcl(FaceAnim.FRIENDLY, "That I am. What's it to you?").also { stage++ }
            5 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "I've been talking to Droalak. I believe he left you but never returned.",
                ).also { stage++ }

            6 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "He did. I suppose he has asked you to tell me he's sorry. What an empty gesture!",
                ).also { stage++ }

            7 -> playerl(FaceAnim.FRIENDLY, "Well actually he told me to give you this amulet.").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "A sapphire amulet! He remembers! It's just like the one he gave me before he left.",
                ).also { stage++ }

            9 -> playerl(FaceAnim.FRIENDLY, "I honestly believe he's sorry.").also { stage++ }
            10 -> npcl(FaceAnim.FRIENDLY, "I'm so glad. Please, tell him I forgive him!").also { stage++ }
            11 -> playerl(FaceAnim.FRIENDLY, "I will.").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "At last I feel complete. Farewell.").also { stage++ }
            13 -> playerl(FaceAnim.FRIENDLY, "Goodbye.").also { stage++ }
            14 -> {
                end()
                if (removeItem(player, Items.SAPPHIRE_AMULET_1694)) {
                    setVarbit(player, MakingHistoryUtils.DROALAK_PROGRESS, 4, true)
                    transformNpc(NPC(NPCs.MELINA_2935), NPCs.MELINA_2934, 10)
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MELINA_2935)
    }
}

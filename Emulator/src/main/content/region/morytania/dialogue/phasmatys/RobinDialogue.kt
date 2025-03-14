package content.region.morytania.dialogue.phasmatys

import content.region.morytania.quest.ahoy.dialogue.RobinDialogueFile
import core.api.inInventory
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.removeItem
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
class RobinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when {
            getQuestStage(player, Quests.GHOSTS_AHOY) >= 10 -> openDialogue(player, RobinDialogueFile())
            else -> playerl(FaceAnim.FRIENDLY, "It's nice to see another human face around here.").also { stage = 1 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> npcl(FaceAnim.FRIENDLY, "Leave me be, peasant - I am relaxing.").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "Well, that's nice!").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Do you know who I am?").also { stage++ }
            4 -> playerl(FaceAnim.FRIENDLY, "I'm sorry, I haven't had the privilege.").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I, peasant, am Robin, Master Bowman. I am very famous you know.",
                ).also { stage++ }

            6 -> playerl(FaceAnim.FRIENDLY, "Oh, Robin, Master Bowman, I see.").also { stage++ }
            7 -> npcl(FaceAnim.FRIENDLY, "So have you heard of me?").also { stage++ }
            8 -> playerl(FaceAnim.FRIENDLY, "No.").also { stage++ }
            9 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Would you do me a favour? I appear to have run out of bed linen. Can you run along to the innkeeper and get me a clean bedsheet?",
                ).also { stage++ }

            10 ->
                if (!inInventory(player, Items.BEDSHEET_4284)) {
                    options(
                        "Anything for a famous person such as yourself.",
                        "Go and get it yourself.",
                    ).also { stage++ }
                } else {
                    playerl(FaceAnim.FRIENDLY, "I've brought you a clean bedsheet, Robin.").also {
                        removeItem(player, Items.BEDSHEET_4284)
                        stage = 14
                    }
                }

            11 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Anything for a famous person such as yourself.").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "Go and get it yourself.").also { stage = 13 }
                }

            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Now that sounds more like it. Run along now, get me my sheet.",
                ).also { stage = END_DIALOGUE }

            13 ->
                npcl(FaceAnim.FRIENDLY, "Oh charming. You just can't get the staff these days.").also {
                    stage = END_DIALOGUE
                }

            14 ->
                npcl(FaceAnim.FRIENDLY, "Well it's about time. Run along now, I need some Robin time.").also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ROBIN_1694)
    }
}

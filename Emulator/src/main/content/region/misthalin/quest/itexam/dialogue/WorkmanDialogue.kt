package content.region.misthalin.quest.itexam.dialogue

import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.removeItem
import core.game.dialogue.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class WorkmanDialogue(
    player: Player? = null,
) : Dialogue(player),
    InteractionListener {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        npc!!
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello!").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Why hello there! What can I do you for?").also { stage++ }
            2 ->
                showTopics(
                    Topic(FaceAnim.FRIENDLY, "What do you do here?", 3),
                    Topic(FaceAnim.FRIENDLY, "I'm not sure.", 6),
                    Topic(FaceAnim.FRIENDLY, "Can I dig around here?", 7),
                )

            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, my job involved digging for finds, cleaning them and transporting them for identification.",
                ).also { stage++ }

            4 -> playerl(FaceAnim.FRIENDLY, "Sounds interesting.").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I find it very interesting and very rewarding. So glad to see you're taking an interest in the digsite. Hope to see you out here digging sometime!",
                ).also {
                    stage = END_DIALOGUE
                }

            6 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, let me know when you are and I'll do my very best to help you!",
                ).also {
                    stage = END_DIALOGUE
                }

            7 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You can only use a site you have the appropriate exam level for.",
                ).also { stage++ }

            8 -> playerl(FaceAnim.FRIENDLY, "Appropriate exam level?").also { stage++ }
            9 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Oh yes, you need to have been trained in the various techniques before you can be allowed to dig for artefacts.",
                ).also { stage++ }

            10 ->
                playerl(FaceAnim.FRIENDLY, "Ah yes, I understand.").also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return WorkmanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.DIGSITE_WORKMAN_613,
            NPCs.DIGSITE_WORKMAN_4564,
            NPCs.DIGSITE_WORKMAN_4565,
        )
    }

    override fun defineListeners() {
        onUseWith(
            IntType.NPC,
            Items.INVITATION_LETTER_696,
            NPCs.DIGSITE_WORKMAN_613,
            NPCs.DIGSITE_WORKMAN_4564,
            NPCs.DIGSITE_WORKMAN_4565,
        ) { player, used, with ->
            openDialogue(player, DigsiteWorkmanDialogueFile(), with as NPC)
            return@onUseWith false
        }
    }
}

class DigsiteWorkmanDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onPredicate { _ -> true }
            .playerl(FaceAnim.FRIENDLY, "Here, have a look at this...")
            .npc(
                FaceAnim.FRIENDLY,
                "I give permission... blah de blah... err. Okay, that's all in",
                "order, you may use the mineshaft now. I'll hang onto",
                "this scroll, shall I?",
            ).endWith { _, player ->
                removeItem(player, Items.INVITATION_LETTER_696)
                if (getQuestStage(player, Quests.THE_DIG_SITE) == 7) {
                    setQuestStage(player, Quests.THE_DIG_SITE, 8)
                }
            }
    }
}

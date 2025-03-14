package content.region.misthalin.quest.fluffs.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
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
class GertrudesChildrenDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var id = 0
    private var switchId = if (id == NPCs.SHILOP_781) NPCs.WILOUGH_783 else NPCs.SHILOP_781

    override fun open(vararg args: Any): Boolean {
        id =
            when (val arg = args[0]) {
                is NPC -> arg.id
                is Int -> arg
                else -> 0
            }
        val quest = getQuestStage(player, Quests.GERTRUDES_CAT)
        stage =
            when (quest) {
                0 -> {
                    player(FaceAnim.HALF_GUILTY, "Hello again.")
                    0
                }

                10 -> {
                    player(FaceAnim.HALF_GUILTY, "Hello there, I've been looking for you.")
                    100
                }

                20, 30, 40, 50 -> {
                    player("Where did you say you saw Fluffs?")
                    130
                }

                else -> {
                    player(FaceAnim.HALF_GUILTY, "Hello again.")
                    0
                }
            }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                sendNPCDialogueLines(
                    player,
                    id,
                    FaceAnim.CHILD_THINKING,
                    false,
                    "You think you're tough do you?",
                ).also { stage++ }

            1 -> player(FaceAnim.HALF_GUILTY, "Pardon?").also { stage++ }
            2 ->
                sendNPCDialogueLines(
                    player,
                    id,
                    FaceAnim.CHILD_FRIENDLY,
                    false,
                    "I can beat anyone up!",
                ).also { stage++ }

            3 ->
                sendNPCDialogueLines(
                    player,
                    NPCs.WILOUGH_783,
                    FaceAnim.CHILD_FRIENDLY,
                    false,
                    "He can you know!",
                ).also { stage++ }

            4 -> player(FaceAnim.HALF_GUILTY, "Really?").also { stage++ }
            5 ->
                sendDialogueLines(
                    player,
                    "The boy begins to jump around with his fists up.",
                    "You wonder what sort of desperado he'll grow up to be.",
                ).also { stage = END_DIALOGUE }

            100 ->
                sendNPCDialogueLines(
                    player,
                    id,
                    FaceAnim.CHILD_FRIENDLY,
                    false,
                    "I didn't mean to take it! I just forgot to pay.",
                ).also { stage++ }

            101 -> player(FaceAnim.HALF_GUILTY, "What? I'm trying to help your mum find Fluffs.").also { stage++ }
            102 ->
                sendNPCDialogueLines(
                    player,
                    switchId,
                    FaceAnim.CHILD_FRIENDLY,
                    false,
                    "I might be able to help. Fluffs followed me to our secret",
                    "play area and I haven't seen her since.",
                ).also { stage++ }

            103 -> player(FaceAnim.HALF_GUILTY, "Where is this play area?").also { stage++ }
            104 ->
                sendNPCDialogueLines(
                    player,
                    switchId,
                    FaceAnim.CHILD_FRIENDLY,
                    false,
                    "If I told you that, it wouldn't be a secret.",
                ).also { stage++ }

            105 -> player(FaceAnim.HALF_GUILTY, "What will make you tell me?").also { stage++ }
            106 ->
                sendNPCDialogueLines(
                    player,
                    switchId,
                    FaceAnim.CHILD_FRIENDLY,
                    false,
                    "Well...now you ask, I am a bit short on cash.",
                ).also { stage++ }

            107 -> player(FaceAnim.HALF_ASKING, "How much?").also { stage++ }
            108 -> sendNPCDialogueLines(player, switchId, FaceAnim.CHILD_FRIENDLY, false, "10 coins.").also { stage++ }
            109 -> player(FaceAnim.HALF_ASKING, "10 coins?!").also { stage++ }
            110 -> player(FaceAnim.CHILD_FRIENDLY, "I'll handle this.").also { stage++ }
            111 ->
                sendNPCDialogueLines(
                    player,
                    switchId,
                    FaceAnim.CHILD_FRIENDLY,
                    false,
                    "100 coins should cover it.",
                ).also { stage++ }

            112 -> player(FaceAnim.ANNOYED, "100 coins! Why should I pay you?").also { stage++ }
            113 ->
                sendNPCDialogueLines(
                    player,
                    switchId,
                    FaceAnim.CHILD_FRIENDLY,
                    false,
                    "You shouldn't, but we won't help otherwise. We never",
                    "liked that cat anyway, so what do you say?",
                ).also { stage++ }

            114 -> options("I'm not paying you a penny.", "Okay then, I'll pay.").also { stage++ }
            115 ->
                when (buttonId) {
                    1 -> player(FaceAnim.NEUTRAL, "I'm not paying you a penny.").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "Okay then, I'll pay.").also { stage = 118 }
                }

            116 ->
                sendNPCDialogueLines(
                    player,
                    switchId,
                    FaceAnim.CHILD_FRIENDLY,
                    false,
                    "Okay then, I'll find another way to make money.",
                ).also { stage = END_DIALOGUE }

            118 -> {
                if (!removeItem(player, Item(Items.COINS_995, 100))) {
                    end()
                    player(FaceAnim.SAD, "Sorry, I don't seem to have enough coins.")
                    return true
                } else {
                    sendItemDialogue(player, Items.COINS_8896, "You give the lad 100 coins.")
                    setQuestStage(player, Quests.GERTRUDES_CAT, 20)
                    stage++
                }
            }

            119 -> player(FaceAnim.HALF_ASKING, "There you go, now where did you see Fluffs?").also { stage++ }
            120 ->
                sendNPCDialogueLines(
                    player,
                    switchId,
                    FaceAnim.CHILD_FRIENDLY,
                    false,
                    "We play at an abandoned lumber mill to the north east.",
                    "Just beyond the Jolly Boar Inn. I saw Fluffs running",
                    "around in there.",
                ).also { stage++ }

            121 -> player(FaceAnim.ASKING, "Anything else?").also { stage++ }
            122 ->
                sendNPCDialogueLines(
                    player,
                    switchId,
                    FaceAnim.CHILD_FRIENDLY,
                    false,
                    "Well, you'll have to find the broken fence to get in. I'm",
                    "sure you can manage that.",
                ).also { stage = END_DIALOGUE }

            130 ->
                sendNPCDialogueLines(
                    player,
                    id,
                    FaceAnim.CHILD_FRIENDLY,
                    false,
                    "Weren't you listening? I saw the flea bag in the old",
                    "lumber mill just north east of here. Just walk past the",
                    "Jolly Boar Inn and you should find it.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return GertrudesChildrenDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHILOP_781, NPCs.WILOUGH_783)
    }
}

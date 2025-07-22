package content.region.desert.bedabin.quest.deserttreasure.dialogue

import content.region.desert.bedabin.quest.deserttreasure.DTUtils
import content.region.desert.bedabin.quest.deserttreasure.DesertTreasure
import core.api.getQuestStage
import core.api.getVarbit
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class MotherTrollDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        println(
            getQuestStage(player, Quests.DESERT_TREASURE) == 9 && DTUtils.getSubStage(
                player, DesertTreasure.iceStage
            ) >= 5,
        )
        if (DTUtils.getSubStage(player, DesertTreasure.iceStage) == 3 && getVarbit(
                player, DesertTreasure.varbitFrozenFather
            ) == 1 && getVarbit(player, DesertTreasure.varbitFrozenMother) == 1
        ) {
            openDialogue(player!!,
                content.region.desert.bedabin.quest.deserttreasure.dialogue.ChatFatherAndMotherTrollDialogueFile(), npc)
        } else if (DTUtils.getSubStage(player, DesertTreasure.iceStage) == 3) {
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 -> npcl(
                                FaceAnim.OLD_CALM_TALK2,
                                "Wow, thanks for breaking me out of that ice! But please, my husband is still trapped in there!",
                            ).also { stage = END_DIALOGUE }
                        }
                    }
                },
                npc,
            )
        } else if (DTUtils.getSubStage(player, DesertTreasure.iceStage) == 4) {
            openDialogue(player!!,
                content.region.desert.bedabin.quest.deserttreasure.dialogue.ChatFatherAndMotherTrollAfterDialogueFile(), npc)
        } else if ((getQuestStage(player, Quests.DESERT_TREASURE) == 9 && DTUtils.getSubStage(
                player, DesertTreasure.iceStage
            ) >= 5) || getQuestStage(player, Quests.DESERT_TREASURE) >= 10
        ) {
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 -> npcl(
                                FaceAnim.OLD_CALM_TALK2,
                                "Thanks again for freeing me from that ice block! I don't know what my little snookums would have done without us!",
                            ).also { stage = END_DIALOGUE }
                        }
                    }
                },
                npc,
            )
        }
        return false
    }

    override fun newInstance(player: Player?): Dialogue = MotherTrollDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.TROLL_MOTHER_1950)
}
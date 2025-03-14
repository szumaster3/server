package content.region.kandarin.quest.ikov.dialogue

import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LucienEndingDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun newInstance(player: Player): Dialogue {
        return LucienEndingDialogue(player)
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        openDialogue(player, LucienEndingDialogueFile(), npc)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LUCIEN_272)
    }
}

class LucienEndingDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onQuestStages(Quests.TEMPLE_OF_IKOV, 100)
            .endWith { _, player ->
                sendMessage(player, "You have completed the Temple of Ikov quest.")
            }
        b
            .onQuestStages(Quests.TEMPLE_OF_IKOV, 1, 2, 3, 4, 5, 6, 7)
            .npcl(FaceAnim.FRIENDLY, "Have you got the Staff of Armadyl yet?")
            .branch { player ->
                return@branch if (inInventory(player, Items.STAFF_OF_ARMADYL_84)) {
                    1
                } else {
                    0
                }
            }.let { branch ->
                branch
                    .onValue(1)
                    .options()
                    .let { optionBuilder ->
                        optionBuilder
                            .option_playerl("Yes! Here it is.")
                            .betweenStage { _, player, _, _ ->
                                removeItem(player, Items.STAFF_OF_ARMADYL_84)
                            }.iteml(Items.STAFF_OF_ARMADYL_84, "You give Lucien the Staff of Armadyl.")
                            .npcl(FaceAnim.FRIENDLY, "Muhahahhahahaha!")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "I can feel the power of the staff running through me! I will be more powerful and they shall bow down to me!",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "I suppose you want your reward? I shall grant you much power!",
                            ).endWith { _, player ->
                                if (getQuestStage(player, Quests.TEMPLE_OF_IKOV) == 6) {
                                    finishQuest(player, Quests.TEMPLE_OF_IKOV)
                                }
                            }
                        optionBuilder
                            .option_playerl("No, not yet.")
                            .end()
                    }
                branch
                    .onValue(0)
                    .playerl(FaceAnim.FRIENDLY, "No, not yet.")
                    .end()
            }
        b
            .onPredicate { _ -> true }
            .npcl("Not here. Meet me at the Flying Horse Inn in East Ardougne.")
            .end()
    }
}

package content.region.karamja.dialogue.brimhaven

import core.api.inEquipment
import core.api.inInventory
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GarvDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        openDialogue(player, GarvDialogueFile(), npc)
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Can I go in there?", "I want for nothing!").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Can I go in there?").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "I want for nothing!").also { stage = 3 }
                }

            2 -> npc(FaceAnim.HALF_GUILTY, "No. In there is private.").also { stage = END_DIALOGUE }
            3 -> npc(FaceAnim.HALF_GUILTY, "You're one of a very lucky few then.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return GarvDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GARV_788)
    }
}

class GarvDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onQuestStages(Quests.HEROES_QUEST, 0, 1, 2)
            .npcl("Hello. What do you want?")
            .options()
            .let { optionBuilder ->
                optionBuilder
                    .option_playerl("Can I go in there?")
                    .npcl("No. In there is private.")
                    .end()
                optionBuilder
                    .option_playerl("I want for nothing!")
                    .npcl("You're one of a very lucky few then.")
                    .end()
            }

        b
            .onQuestStages(Quests.HEROES_QUEST, 3, 4, 5, 6, 100)
            .npcl("Hello. What do you want?")
            .playerl("Hi. I'm Hartigen. I've come to work here.")
            .branch { player ->
                return@branch if (inEquipment(player, Items.BLACK_FULL_HELM_1165) &&
                    inEquipment(
                        player,
                        Items.BLACK_PLATEBODY_1125,
                    ) &&
                    inEquipment(player, Items.BLACK_PLATELEGS_1077)
                ) {
                    1
                } else {
                    0
                }
            }.let { branch ->
                branch
                    .onValue(1)
                    .npcl("I assume you have your I.D. papers then?")
                    .branch { player ->
                        return@branch if (inInventory(player, Items.ID_PAPERS_1584)) {
                            1
                        } else {
                            0
                        }
                    }.let { branch2 ->
                        branch2
                            .onValue(1)
                            .npcl("You'd better come in then, Grip will want to talk to you.")
                            .endWith { _, player ->
                                if (getQuestStage(player, Quests.HEROES_QUEST) == 3) {
                                    setQuestStage(player, Quests.HEROES_QUEST, 4)
                                }
                            }
                        branch2
                            .onValue(0)
                            .playerl("Uh... Yeah. About that...I must have left them in my other suit of armour.")
                            .end()
                    }
                branch
                    .onValue(0)
                    .npcl("Hartigen the Black Knight? I don't think so. He doesn't dress like that.")
                    .end()
            }
    }
}

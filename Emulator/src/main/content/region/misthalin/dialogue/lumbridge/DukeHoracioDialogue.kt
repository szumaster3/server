package content.region.misthalin.dialogue.lumbridge

import content.region.misthalin.quest.dragon.DragonSlayer
import content.region.misthalin.quest.dragon.dialogue.DukeDragonSlayerDialogue
import content.region.misthalin.quest.losttribe.dialogue.DukeHoracioLostTribeDialogue
import content.region.misthalin.quest.runemysteries.DukeHoracioRMDialogue
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.DIALOGUE_INITIAL_OPTIONS_HANDLE
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Duke Horacio dialogues.
 * @author Ceikry
 */
class DukeHoracioDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if ((player.questRepository.getQuest(Quests.DRAGON_SLAYER).getStage(player) == 100 && !player.inventory.containsItem(DragonSlayer.SHIELD) && !player.bank.containsItem(DragonSlayer.SHIELD) )|| (player.questRepository.getQuest(Quests.DRAGON_SLAYER).isStarted(player) && !player.questRepository.getQuest(Quests.DRAGON_SLAYER).isCompleted(player))) {
            addOption(Quests.DRAGON_SLAYER, DukeDragonSlayerDialogue(player.questRepository.getStage(Quests.DRAGON_SLAYER)))
        }
        if (!player.questRepository.isComplete(Quests.THE_LOST_TRIBE) && player.questRepository.getQuest(Quests.THE_LOST_TRIBE).isStarted(player)) {
            addOption(Quests.THE_LOST_TRIBE, DukeHoracioLostTribeDialogue(player.questRepository.getStage(Quests.THE_LOST_TRIBE)))
        }
        if (!sendChoices()) {
            interpreter.sendDialogues(npc, FaceAnim.HALF_GUILTY, "Greetings. Welcome to my castle.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {

            DIALOGUE_INITIAL_OPTIONS_HANDLE -> {
                npc("Greetings. Welcome to my castle.")
                loadFile(optionFiles[buttonId - 1])
            }

            0 -> {
                options("Have you any quests for me?", "Where can I find money?")
                stage = 1
            }
            1 -> when (buttonId) {
                1 -> {
                    player(FaceAnim.HALF_GUILTY, "Have any quests for me?")
                    stage = 20
                }
                2 -> {
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "I hear many of the local people earn money by learning a",
                        "skill. Many people get by in life by becoming accomplished",
                        "smiths, cooks, miners and woodcutters.",
                    )
                    stage = END_DIALOGUE
                }
            }
            20 -> {
                npc("Let me see...")
                if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                    loadFile(DukeHoracioRMDialogue(player.questRepository.getStage(Quests.RUNE_MYSTERIES)))
                } else {
                    stage++
                }
            }
            21 -> {
                npc("Nope, I've got everything under control", "in the castle at the moment.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return DukeHoracioDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DUKE_HORACIO_741)
    }

}
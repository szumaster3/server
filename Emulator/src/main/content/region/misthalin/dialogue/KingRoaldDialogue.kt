package content.region.misthalin.dialogue

import content.minigame.allfiredup.dialogue.KingRoaldAFUDialogue
import content.region.misthalin.varrock.quest.firedup.dialogue.KingRoaldDialogueFile
import content.region.misthalin.varrock.quest.phoenixgang.dialogue.KingRoaldDialogue
import content.region.misthalin.paterdomus.quest.priest.dialogue.KingRoaldPriestInPerilDialogue
import core.api.getAttribute
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.DIALOGUE_INITIAL_OPTIONS_HANDLE
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class KingRoaldDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getAttribute(player, "afu-mini:ring", false) ||
            getAttribute(
                player,
                "afu-mini:gloves",
                false,
            ) ||
            getAttribute(player, "afu-mini:adze", false)
        ) {
            player("Your Majesty! I did it!")
            loadFile(KingRoaldAFUDialogue())
            return true
        }
        if (isQuestComplete(player, Quests.PRIEST_IN_PERIL)) {
            if (!player.questRepository.hasStarted(Quests.ALL_FIRED_UP) ||
                getQuestStage(player, Quests.ALL_FIRED_UP) == 90
            ) {
                addOption(Quests.ALL_FIRED_UP, KingRoaldDialogueFile(getQuestStage(player, Quests.ALL_FIRED_UP)))
            }
        } else {
            addOption(
                Quests.PRIEST_IN_PERIL,
                KingRoaldPriestInPerilDialogue(getQuestStage(player, Quests.PRIEST_IN_PERIL)),
            )
        }
        if (player.questRepository.getQuest(Quests.SHIELD_OF_ARRAV).isStarted(player) &&
            !isQuestComplete(player, Quests.SHIELD_OF_ARRAV)
        ) {
            addOption(Quests.SHIELD_OF_ARRAV, KingRoaldDialogue())
        }
        if (!sendChoices()) {
            player("Greetings, your Majesty.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            DIALOGUE_INITIAL_OPTIONS_HANDLE -> {
                player("Greetings, your Majesty.")
                loadFile(optionFiles[buttonId - 1])
            }
            START_DIALOGUE -> npc(FaceAnim.HALF_GUILTY, "Do you have anything of importance to say?").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "...Not really.").also { stage++ }
            2 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You will have to excuse me, then. I am very busy as I",
                    "have a kingdom to run!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            END_DIALOGUE -> end()
        }
        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.KING_ROALD_648,
            NPCs.KING_ROALD_2590,
            NPCs.KING_ROALD_5838,
        )
}

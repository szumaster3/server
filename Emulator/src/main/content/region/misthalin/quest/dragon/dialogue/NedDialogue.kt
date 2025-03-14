package content.region.misthalin.quest.dragon.dialogue

import content.region.misthalin.quest.dragon.DragonSlayer
import core.api.quest.setQuestStage
import core.api.removeItem
import core.api.sendItemDialogue
import core.api.sendMessage
import core.game.dialogue.DialogueFile
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Quests

private const val SHIP_DIALOGUE = 2000

class NedDialogue(
    val questStage: Int,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        if (stage >= SHIP_DIALOGUE) {
            when (stage) {
                SHIP_DIALOGUE ->
                    if (player!!.getSavedData().questData.getDragonSlayerAttribute("ship")) {
                        player("It's the Lady Lumbridge, in Port Sarim.").also { stage++ }
                    } else {
                        player("I'm still looking...").also { stage = END_DIALOGUE }
                    }

                2001 -> npc("That old pile of junk? Last I heard, she wasn't", "seaworthy.").also { stage++ }
                2002 ->
                    if (player!!.savedData.questData.getDragonSlayerAttribute("repaired")) {
                        player("I fixed her up!").also { stage++ }
                    } else {
                        player("Oh, I better go inspect her.").also { stage = END_DIALOGUE }
                    }

                2003 -> npc("You did? Excellent!").also { stage++ }
                2004 -> npc("Just show me the map and we can get ready to go!").also { stage++ }
                2005 -> {
                    if (!removeItem(player!!, DragonSlayer.CRANDOR_MAP)) {
                        sendMessage(player!!, "You don't have the map to Crandor.")
                        stage = END_DIALOGUE
                    } else {
                        player("Here you go.")
                        stage++
                    }
                }

                2006 ->
                    sendItemDialogue(player!!, DragonSlayer.CRANDOR_MAP, "You hand the map to Ned.").also {
                        setQuestStage(player!!, Quests.DRAGON_SLAYER, 30)
                        stage++
                    }

                2007 -> npc("Excellent! I'll meet you at the ship, then.").also { stage = END_DIALOGUE }
            }
        } else if (questStage == 20 && player!!.savedData.questData.getDragonSlayerAttribute("ned")) {
            when (stage) {
                START_DIALOGUE -> player("Will you take me to Crandor now, then?").also { stage++ }
                1 -> npc("I Said I would and old Ned is a man of his word!").also { stage++ }
                2 -> npc("So, where's your ship?").also { stage = SHIP_DIALOGUE }
            }
        } else if (questStage == 20) {
            when (stage) {
                START_DIALOGUE -> player("You're a sailor? Could you take me to Crandor?").also { stage++ }
                1 ->
                    npc(
                        "Well, I was a sailor. I've not been able to get work at",
                        "sea these days, though. They say I'm too old.",
                    ).also {
                        stage++
                    }
                2 -> npc("Sorry, where was it you said you wanted to go?").also { stage++ }
                3 -> player("To the island of Crandor.").also { stage++ }
                4 -> npc("Crandor? You've got to be out of your mind!").also { stage++ }
                5 ->
                    npc(
                        "But... It would be a chance to sail a ship once more.",
                        "I'd sail anywhere if it was a chance to sail again.",
                    ).also {
                        stage++
                    }
                6 -> npc("Then again, no captain in his right mind would sail to", "that island...").also { stage++ }
                7 ->
                    npc("Ah, you only live once! I'll do it!").also {
                        player!!.savedData.questData.setDragonSlayerAttribute("ned", true)
                        stage++
                    }

                8 -> npc("So, where's your ship?").also { stage = SHIP_DIALOGUE }
            }
        } else if (questStage == 30) {
            when (stage) {
                START_DIALOGUE -> player("So will you take me to Crandor now then?").also { stage++ }
                1 ->
                    npc(
                        "I Said I would and old Ned is a man of his word! I'll",
                        "meet you on board the Lady Lumbridge in Port Sarim.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
            }
        }
    }
}

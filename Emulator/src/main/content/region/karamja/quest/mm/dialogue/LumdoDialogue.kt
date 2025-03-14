package content.region.karamja.quest.mm.dialogue

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendItemDialogue
import core.game.dialogue.DialogueFile
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.Quests

class LumdoDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 ->
                if (getQuestStage(player!!, Quests.MONKEY_MADNESS) == 23) {
                    npcl("Who are you two?").also { stage++ }
                } else if (getQuestStage(player!!, Quests.MONKEY_MADNESS) == 24) {
                    playerl("You will take me to the atoll?").also { stage = 23 }
                } else {
                    end()
                }

            1 ->
                playerl(
                    "We are on a mission for King Narnode Shareen. I am ${player!!.username} and this is the flight Commander Wayder.",
                ).also {
                    stage++
                }
            2 -> npcl("What business do you have here?").also { stage++ }
            3 ->
                playerl(
                    "We are to investigate the disappearance of the 10th squad of the Royal Guard. Am i right in suspecting that you Lumdo, are a member of the 10th squad?",
                ).also {
                    stage++
                }
            4 -> npcl("I might be. Do you have any way to prove that you are who yo say you are?").also { stage++ }
            5 -> playerl("I have the Gnome Royal Seal.").also { stage++ }
            6 ->
                sendItemDialogue(
                    player!!,
                    Items.GNOME_ROYAL_SEAL_4004,
                    "You show Lumdo the Royal Seal.",
                ).also { stage++ }

            7 -> npcl("I see. Sorry for my distrust.").also { stage++ }
            8 -> playerl("So are you Lumdo of the 10th squad?").also { stage++ }
            9 -> npcl("I am indeed - ").also { stage++ }
            10 -> playerl("Where are the rest of your squad? Where is your Sergeant?").also { stage++ }
            11 -> npcl("Let me begin at the beginning human.").also { stage++ }
            12 ->
                npcl(
                    "We were one gnome to a glider, so each was extremely light. Like leaves in a wind we were blown south before we even landed on the island.",
                ).also {
                    stage++
                }
            13 -> playerl("Did you crash straight here?").also { stage++ }
            14 ->
                npcl(
                    "Yes. The winds drove us into the treetops, which destroyed the canvas of our gliders. We dragged what remained of the gliders out onto this beach.",
                ).also {
                    stage++
                }
            15 -> playerl("What did you do then?").also { stage++ }
            16 ->
                npcl(
                    "Whilst we were falling, Sergeant Garkor noticed a large populated atoll to our west. You cannot see it from here, but it is within sailing distance.",
                ).also {
                    stage++
                }
            17 -> playerl("Presumably you are to guard the gliders until they return?").also { stage++ }
            18 -> npcl("Affirmative.").also { stage++ }
            19 ->
                playerl(
                    "You must take us to the island. I have orders from the High Tree Guardians to make contact with your Sergeant.",
                ).also {
                    stage++
                }
            20 -> npcl("And i have order from the Sergeant to stay here.").also { stage++ }
            21 -> playerl("You will not take me?").also { stage++ }
            22 ->
                npcl("I will not take orders from you.").also {
                    stage = END_DIALOGUE
                    setQuestStage(player!!, Quests.MONKEY_MADNESS, 24)
                }

            23 -> npcl("I am under direct orders to remain here.").also { stage = END_DIALOGUE }
        }
    }
}

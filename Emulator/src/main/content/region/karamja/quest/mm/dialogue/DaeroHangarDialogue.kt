package content.region.karamja.quest.mm.dialogue

import core.api.getAttribute
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendNPCDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class DaeroHangarDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> {
                if (getAttribute(player!!, "mm:puzzle:done", false)) {
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "Well done adventurer. You have managed to break Glough's code. Now the process of reinitialisation is complete, you can truly begin your journey into the unknown.",
                    ).also { stage = 21 }
                } else if (getQuestStage(player!!, Quests.MONKEY_MADNESS) == 21) {
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "Welcome adventurer, to the Underground Military Glider Hangar.",
                    ).also { stage++ }
                } else if (getQuestStage(player!!, Quests.MONKEY_MADNESS) == 22) {
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "${player!!.username}, you will have to wait till reinitialization is complete.",
                    ).also { stage = END_DIALOGUE }
                }
            }

            1 -> playerl("Wow! Why would Gnomes need such a place?").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "We do not. If the truth be told, this hangar was part of Glough's contingency planning. Had de attacks by land and sea failed he would turned to air.",
                ).also { stage++ }

            3 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "It is fortunate indeed that you managed to expose him. The military gliders in this hangar are a prototype for a much refined version of the standard variety we currently use.",
                ).also { stage++ }

            4 -> npcl(FaceAnim.OLD_DEFAULT, "Let me introduce you to Flight Commander Waydar.").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Flight Commander Waydar, i would like you to meet ${player!!.username}",
                ).also { stage++ }

            6 -> sendNPCDialogue(player!!, NPCs.WAYDAR_1408, "Greetings High Tree Guardian.").also { stage++ }
            7 -> sendNPCDialogue(player!!, NPCs.WAYDAR_1408, "And greetings to you too, visitor.").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Not any old visitor Waydar; this is the person who exposed Glough and defeated his demon.",
                ).also { stage++ }

            9 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.WAYDAR_1408,
                    "I see. Well, there are no more demon left here.",
                ).also { stage++ }

            10 -> npcl(FaceAnim.OLD_DEFAULT, "Quite. She is on a secret mission for the King.").also { stage++ }
            11 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "As you know, the 10th squad went missing during their mission to decommission the eastern shipyard of Karamja.",
                ).also { stage++ }

            12 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "We still do not know what happened, but evidence suggests they were blow far off course to the south.",
                ).also { stage++ }

            13 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.WAYDAR_1408,
                    "Their standard gliders must have fallen prey on the tropical weather.",
                ).also { stage++ }

            14 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "When reinitialization has been completed, you are to fly to the south of Karamja with ${player!!.username} and accompany her on the mission.",
                ).also { stage++ }

            15 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.WAYDAR_1408,
                    "We are no close to reinitialize sir - the code is too hard. It is likely the only person who could do it is Glough.",
                ).also { stage++ }

            16 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "That Gnome is never stepping foot in this hangar again. He always was and still is a menace. Do you understand me?",
                ).also { stage++ }

            17 -> sendNPCDialogue(player!!, NPCs.WAYDAR_1408, "Yes sir.").also { stage++ }
            18 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Very well. Notify me when you have managed to reinitialize.",
                ).also { stage++ }

            19 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "${player!!.username}, you will have to wait till reinitialization is complete.",
                ).also {
                    setQuestStage(player!!, Quests.MONKEY_MADNESS, 22)
                    stage = END_DIALOGUE
                }

            21 -> playerl("I've had some practice in the past.").also { stage++ }
            22 -> npcl(FaceAnim.OLD_DEFAULT, "You are clearly a human of many talents.").also { stage++ }
            23 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Flight Commander Waydar, now the reinitialisation is complete, I order you to fly to the south of Karamja with ${player!!.username}.",
                ).also { stage++ }

            24 -> sendNPCDialogue(player!!, NPCs.WAYDAR_1408, "Yes sir.").also { stage++ }
            25 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "You are to safeguard ${if (player!!.isMale) "him" else "her"} on this potentially dangerous mission.",
                ).also { stage++ }

            26 -> sendNPCDialogue(player!!, NPCs.WAYDAR_1408, "Understood.").also { stage++ }
            27 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "${player!!.username}, speak to Waydar when you are ready to leave.",
                ).also { stage++ }

            28 -> npcl(FaceAnim.OLD_DEFAULT, "And ... good luck.").also { stage++ }
            29 -> {
                end()
                setQuestStage(player!!, Quests.MONKEY_MADNESS, 23)
            }
        }
    }
}

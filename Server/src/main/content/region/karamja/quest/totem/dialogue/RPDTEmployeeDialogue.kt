package content.region.karamja.quest.totem.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

@Initializable
class RPDTEmployeeDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npcl(FaceAnim.HAPPY, "Welcome to R.P.D.T.!")
        stage =
            if (getQuestStage(player, Quests.TRIBAL_TOTEM) == 20) {
                5
            } else if(isQuestComplete(player, Quests.TRIBAL_TOTEM) && getQuestStage(player, Quests.BACK_TO_MY_ROOTS) >= 2) {
                8
            } else if(getVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055) >= 20) {
                24
            } else {
                0
            }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.HAPPY, "Thank you very much.").also { stage = 7 }
            5 -> playerl(FaceAnim.ASKING, "So, when are you going to deliver this crate?").also { stage++ }
            6 -> {
                npcl(FaceAnim.THINKING, "Well... I guess we could do it now...")
                setQuestStage(player, Quests.TRIBAL_TOTEM, 25)
                stage = 7
            }
            7 -> end()

            8  -> playerl(FaceAnim.HALF_ASKING, "What's the holdup here?").also { stage++ }
            9  -> npcl(FaceAnim.FRIENDLY, "It's stinkin' up the place.").also { stage++ }
            10 -> playerl(FaceAnim.HALF_ASKING, "What is?").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "The package... over there on the table.").also { stage++ }
            12 -> playerl(FaceAnim.HALF_ASKING, "So why not deliver it then? You guys always seem to be sitting around while Postie Pete is out there pushing the envelope in mail delivery performance.").also { stage++ }
            13 -> npc(FaceAnim.FRIENDLY, "Can't. The label's unreadable, think it got a bit water", "damage, came in on the shipment from Karamja last", "week in that big storm.").also { stage++ }
            14 -> playerl(FaceAnim.HALF_ASKING, "So... if you can't deliver it, why not open it and find out what's inside?").also { stage++ }
            15 -> npc(FaceAnim.FRIENDLY, "Ooooh...can't do that, more than my job's worth! See,", "policy 387.29 sub paragraph 4 states that no package is", "to be tampered with or opened by R.P.D.T. workers", "whilst in the care of the R.P.D.T... So, you see, there's").also { stage++ }
            16 -> npc(FaceAnim.FRIENDLY, "no way we can open it - we'll just have to have another", "cup of tea and stay out of the stink.").also { stage++ }
            17 -> playerl(FaceAnim.HALF_ASKING, "Erm, look, I hate to point out the obvious, but... I'm not an R.P.D.T. worker.").also { stage++ }
            18 -> npcl(FaceAnim.FRIENDLY, "'Ere, I just thought of somethin'. You're not an R.P.D.D. worker.").also { stage++ }
            19 -> playerl(FaceAnim.HALF_ASKING, "That's what I jus-").also { stage++ }
            20 -> npcl(FaceAnim.FRIENDLY, "You can open it! Then we can get back to work, we got about a week's backlog now 'coz of the stench.").also { stage++ }
            21 -> playerl(FaceAnim.HALF_ASKING, "*sighs* Why do I have to think of everything?").also { stage++ }
            22 -> npcl(FaceAnim.FRIENDLY, "Was my idea! So... Are you going to open it?").also { stage++ }
            23 -> playerl(FaceAnim.HALF_ASKING, "I guess I'll have to if I'm not to disillusion the wizard.").also {
                // Unhidden options for smelly package.
                setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 15, true)
                setQuestStage(player, Quests.BACK_TO_MY_ROOTS, 3)
                stage = END_DIALOGUE
            }

            24 -> playerl(FaceAnim.HALF_ASKING, "Well, I got it open.. not sure I wanted to, though.").also { stage++ }
            25 -> npcl(FaceAnim.FRIENDLY, "So's I see. Looked pretty nasty to me. Hope you can dispose of it.").also { stage++ }
            26 -> playerl(FaceAnim.HALF_ASKING, "Yes... I think it belongs to a wizard I sort of know.").also { stage++ }
            27 -> npcl(FaceAnim.FRIENDLY, "Sort of?").also { stage++ }
            28 -> playerl(FaceAnim.HALF_ASKING, "Well, I didn't really get to know him till after he was dead. But he ended up being fairly handy as I now get lots of sand.").also { stage++ }
            29 -> npcl(FaceAnim.FRIENDLY, "Errr... yeah.. right. Look, I got work to do, so you can go now.").also { stage++ }
            30 -> playerl(FaceAnim.HALF_ASKING, "Oh yes, Wizard Cromperty will so look forward to his parcel. Will you deliver that now?").also { stage++ }
            31 -> npcl(FaceAnim.FRIENDLY, "Of course. We'll make it our first priority.").also { stage++ }
            32 -> playerl(FaceAnim.HALF_ASKING, "I'll go see him then and get rid of this... err...appendage. Zavistic might be happy to see his apprentice.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.RPDT_EMPLOYEE_843)
}

package content.region.kandarin.dialogue.observatory

import content.region.kandarin.quest.itgronigen.dialogue.ProfessorDialogueFile
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ProfessorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.OBSERVATORY_QUEST)) {
            setTitle(player, 2)
            sendDialogueOptions(
                player,
                "What would you like to talk about?",
                "Talk about the Observatory Quest.",
                "Talk about Treasure Trails.",
            ).also {
                stage =
                    4
            }
        } else {
            npc("What would you like to talk about?").also { stage = -1 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            -1 -> options("Talk about Treasure Trails.", "I'm just passing through.").also { stage = 1 }
            0 -> options("Talk about the Observatory Quest.", "Talk about Treasure Trails.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.ASKING, "Can you teach me to solve Treasure Trails clues?").also { stage = 9 }
                    2 -> player("I'm just passing through.").also { stage++ }
                }
            2 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Fair enough. Not everyone is interested in this place, I suppose.",
                ).also { stage++ }
            3 -> sendDialogue(player, "The professor carries on with his studies.").also { stage = END_DIALOGUE }
            4 ->
                when (buttonId) {
                    1 ->
                        if (getQuestStage(player, Quests.OBSERVATORY_QUEST) <
                            100
                        ) {
                            openDialogue(player, ProfessorDialogueFile())
                        } else {
                            npcl(
                                FaceAnim.ASKING,
                                "Hello, friend. Welcome back. Thanks for all your help with the telescope. What can I do for you?",
                            ).also {
                                stage++
                            }
                        }
                    2 -> playerl(FaceAnim.ASKING, "Can you teach me to solve Treasure Trails clues?").also { stage = 9 }
                }
            5 -> options("Do you need any more help with the telescope?", "Nothing, thanks.").also { stage++ }
            6 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.ASKING, "Do you need any more help with the telescope?").also { stage = 8 }
                    2 -> playerl(FaceAnim.CALM, "Nothing, thanks.").also { stage++ }
                }
            7 -> npcl(FaceAnim.NEUTRAL, "Okay, no problem. See you again.").also { stage = END_DIALOGUE }
            8 ->
                npcl(FaceAnim.NEUTRAL, "Not right now, but the stars may hold a secret for you.").also {
                    stage =
                        END_DIALOGUE
                }
            9 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Ah, I get asked about Treasure Trails all the time!",
                    "Listen carefully and I shall tell you what I know.",
                    "Lots of clues have " + RED + "degrees</col> and " + RED + "minutes</col> written",
                    "on them.",
                ).also { stage++ }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "These are coordinates of the place where the treasure is buried. You will have to walk to the correct spot, so that your coordinates are exactly the same as the values written on the clue scroll.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "To do this, you must use a " + RED + "sextant</col>, a " + RED + "watch</col> and ",
                    "a$RED chart</col> to find the coordinates of where you are.",
                ).also { stage++ }
            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Once you know the coordinates of your position, you know which way you have to walk to get to the treasure's coordinates!",
                ).also {
                    stage++
                }
            13 -> playerl(FaceAnim.THINKING, "Riiight. So, where do I get these items from?").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I think Murphy, the owner of the Fishing Trawler moored at Port Khazard, might be able to spare you a sextant. After that, the nearest clock tower is south of Ardougne - you could probably get a watch there.",
                ).also {
                    stage++
                }
            15 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I've got plenty of charts myself; just come back here when you've got the sextant and watch, and I'll give you one and teach you how to use them.",
                ).also {
                    stage++
                }
            16 -> playerl(FaceAnim.FRIENDLY, "Thanks, I'll see you later.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ProfessorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OBSERVATORY_PROFESSOR_488)
    }
}

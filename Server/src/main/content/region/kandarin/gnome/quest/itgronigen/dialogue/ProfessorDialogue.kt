package content.region.kandarin.gnome.quest.itgronigen.dialogue

import core.api.*
import core.api.getQuestStage
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.DARK_RED
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class ProfessorDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.OBSERVATORY_QUEST)) {
            setTitle(player, 2)
            sendOptions(player, "What would you like to talk about?", "Talk about the Observatory Quest.", "Talk about Treasure Trails.").also { stage = 4 }
        } else {
            npc("What would you like to talk about?").also { stage = -1 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
           -1 -> options("Talk about Treasure Trails.", "I'm just passing through.").also { stage = 1 }
            0 -> options("Talk about the Observatory Quest.", "Talk about Treasure Trails.").also { stage++ }
            1 -> when (buttonId) {
                1 -> options("I've lost my chart.", "How do these work again?").also { stage = 10 }
                2 -> player("I'm just passing through.").also { stage++ }
            }
            2 -> npcl(FaceAnim.NEUTRAL, "Fair enough. Not everyone is interested in this place, I suppose.").also { stage++ }
            3 -> sendDialogue(player, "The professor carries on with his studies.").also { stage = 21 }
            4 -> when (buttonId) {
                1 -> if (getQuestStage(player, Quests.OBSERVATORY_QUEST) < 100) {
                    openDialogue(player, ProfessorDialogueFile())
                } else {
                    npcl(FaceAnim.ASKING, "Hello, friend. Welcome back. Thanks for all your help with the telescope. What can I do for you?").also { stage++ }
                }
                2 -> options("I've lost my chart.", "How do these work again?").also { stage = 10 }
            }

            5 -> options("Do you need any more help with the telescope?", "Nothing, thanks.").also { stage++ }
            6 -> when (buttonId) {
                1 -> playerl(FaceAnim.ASKING, "Do you need any more help with the telescope?").also { stage = 8 }
                2 -> playerl(FaceAnim.CALM, "Nothing, thanks.").also { stage++ }
            }

            7 -> npcl(FaceAnim.NEUTRAL, "Okay, no problem. See you again.").also { stage = 21 }
            8 -> npcl(FaceAnim.NEUTRAL, "Not right now, but the stars may hold a secret for you.").also { stage = 21 }

            9 -> options("I've lost my chart.", "How do these work again?").also { stage++ }
            10 -> when (buttonId) {
                1 -> if (!inInventory(player, Items.CHART_2576)) {
                    player("I've lost my chart.")
                    stage = 19
                } else {
                    npcl(FaceAnim.THINKING, "Um... Are you sure? I think you've got one stored somewhere.")
                    stage = 21
                }
                2 -> player("How do these work again?").also { stage++ }
            }
            11 -> npc(FaceAnim.FRIENDLY, "Ah, I get asked about Treasure Trails all the time!", "Listen carefully and I shall tell you what I know.", "Lots of clues have " + DARK_RED + "degrees</col> and " + DARK_RED + "minutes</col> written", "on them.").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "These are coordinates of the place where the treasure is buried. You will have to walk to the correct spot, so that your coordinates are exactly the same as the values written on the clue scroll.").also { stage++ }
            13 -> npc(FaceAnim.FRIENDLY, "To do this, you must use a " + DARK_RED + "sextant</col>, a " + DARK_RED + "watch</col> and ", "a$DARK_RED chart</col> to find the coordinates of where you are.").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "Once you know the coordinates of your position, you know which way you have to walk to get to the treasure's coordinates!").also { stage++ }
            15 -> playerl(FaceAnim.THINKING, "Riiight. So, where do I get these items from?").also { stage++ }
            16 -> npcl(FaceAnim.FRIENDLY, "I think Murphy, the owner of the Fishing Trawler moored at Port Khazard, might be able to spare you a sextant. After that, the nearest clock tower is south of Ardougne - you could probably get a watch there.").also { stage++ }
            17 -> npcl(FaceAnim.FRIENDLY, "I've got plenty of charts myself; just come back here when you've got the sextant and watch, and I'll give you one and teach you how to use them.").also { stage++ }
            18 -> playerl(FaceAnim.FRIENDLY, "Thanks, I'll see you later.").also { stage = 21 }
            19 -> {
                if (freeSlots(player) == 0) {
                    npcl(FaceAnim.NEUTRAL, "You don't have enough space for the chart. Come back to me when you do.")
                    return true
                }
                npcl(FaceAnim.HAPPY, "That's not a problem, I've got lots of copies.").also { stage++ }
            }
            20 -> {
                end()
                sendItemDialogue(player, Items.CHART_2576, "The professor has given you a navigation chart.")
                addItem(player, Items.CHART_2576, 1)
            }
            21 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ProfessorDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.OBSERVATORY_PROFESSOR_488)
}

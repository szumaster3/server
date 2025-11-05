package content.region.kandarin.gnome.dialogue

import content.region.kandarin.gnome.quest.grandtree.plugin.TheGrandTreeUtils
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Femi dialogue.
 */
@Initializable
class FemiDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val grandTreeStage = getQuestStage(player, Quests.THE_GRAND_TREE)
        val inArea = inBorders(player!!, 2456, 3406, 2462, 3410)

        when {
            inArea && grandTreeStage == 55 -> {
                npc(FaceAnim.OLD_DEFAULT, "Now, to get this lot to the Grand Tree!")
                stage = 7
            }
            !inArea && isQuestInProgress(player, Quests.THE_GRAND_TREE, 1, 54) -> {
                npc(FaceAnim.OLD_DEFAULT, "Hello there.")
            }
            else -> {
                sendDialogue(player, "The little gnome is too busy to talk.")
                stage = 10
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player("Hi!").also { stage++ }
            1 -> npc(FaceAnim.OLD_DEFAULT, "Could you help me lift these boxes? They are", "really heavy!").also { stage++ }
            2 -> options("Sorry, I'm a bit busy.", "OK then.").also { stage++ }
            3 -> when (buttonId) {
                1 -> player("Sorry, I'm a bit busy.").also { stage++ }
                2 -> player("OK then.").also { stage = 5 }
            }
            4 -> npc(FaceAnim.OLD_DEFAULT, "Oh, OK, I'll do it myself.").also { stage = 10 }
            5 -> npc(FaceAnim.OLD_DEFAULT, "Thanks traveller!").also { stage++ }
            6 -> {
                end()
                TheGrandTreeUtils.sneakIn(player)
                setAttribute(player, TheGrandTreeUtils.FEMI_HELP_TRUE, true)
            }
            7 -> options("Can I help?", "I'd better get going!").also { stage++ }
            8 -> when (buttonId) {
                1 -> player("Can I help?").also { stage++ }
                2 -> end()
            }
            9 -> npc(FaceAnim.OLD_DEFAULT, "No, you're OK traveller. I can manage from here.").also { stage++ }
            10 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = FemiDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.FEMI_676)
}
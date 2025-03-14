package content.region.kandarin.dialogue.stronghold

import content.region.kandarin.quest.grandtree.handlers.TheGrandTreeUtils
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class FemiDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (inBorders(player!!, 2456, 3406, 2462, 3410)) {
            if (getQuestStage(player, Quests.THE_GRAND_TREE) == 55) {
                npc(FaceAnim.OLD_DEFAULT, "Now, to get this lot to the Grand Tree!").also { stage = 7 }
            } else {
                sendDialogue(player, "The little gnome is too busy to talk.").also { stage = END_DIALOGUE }
            }
        } else {
            if (isQuestInProgress(player, Quests.THE_GRAND_TREE, 1, 54)) {
                npc(FaceAnim.OLD_DEFAULT, "Hello there.").also { stage++ }
            } else {
                sendDialogue(player, "The little gnome is too busy to talk.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player("Hi!").also { stage++ }
            1 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Could you help me lift these boxes? They are",
                    "really heavy!",
                ).also { stage++ }

            2 -> options("Sorry, I'm a bit busy.", "OK then.").also { stage++ }
            3 ->
                when (buttonId) {
                    1 -> player("Sorry, I'm a bit busy.").also { stage++ }
                    2 -> player("OK then.").also { stage = 5 }
                }

            4 -> npc(FaceAnim.OLD_DEFAULT, "Oh, OK, I'll do it myself.").also { stage = END_DIALOGUE }
            5 -> npc(FaceAnim.OLD_DEFAULT, "Thanks traveller!").also { stage++ }
            6 -> {
                end()
                TheGrandTreeUtils.sneakIn(player)
                setAttribute(player, TheGrandTreeUtils.FEMI_HELP_TRUE, true)
            }

            7 -> options("Can I help?", "I'd better get going!").also { stage++ }
            8 ->
                when (buttonId) {
                    1 -> player("Can I help?").also { stage++ }
                    2 -> end()
                }

            9 ->
                npc(FaceAnim.OLD_DEFAULT, "No, you're OK traveller. I can manage from here.").also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FemiDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FEMI_676)
    }
}

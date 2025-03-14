package content.region.kandarin.quest.chompybird.dialogue

import content.region.kandarin.quest.chompybird.BigChompyBirdHunting
import core.api.getAttribute
import core.api.getItemName
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class FycieChompyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        val chompyBird = player.questRepository.getQuest(Quests.BIG_CHOMPY_BIRD_HUNTING)
        val chompyStage = chompyBird.getStage(player)

        when (chompyStage) {
            in 0 until 100 -> loadFile(FycieChompyDialogueFile(chompyBird))
        }
        player.dialogueInterpreter.handle(0, 0)
        return true
    }

    override fun handle(
        componentId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FYCIE_1011)
    }
}

class FycieChompyDialogueFile(
    val quest: Quest,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (quest.getStage(player)) {
            in 0 until 70 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "You's better talk to Dad, We not talk to wierdly 'umans.",
                ).also { stage = END_DIALOGUE }

            in 70 until 90 -> handleIngredientDialogue(player, buttonID)
        }
    }

    private fun handleIngredientDialogue(
        player: Player?,
        buttonId: Int,
    ) {
        val fycieIngredient = getAttribute(player!!, BigChompyBirdHunting.ATTR_ING_FYCIE, -1)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Dad say's you's roasting da chompy for us! Slurp! Me's wants ${getItemName(
                        fycieIngredient,
                    )} wiv mine! Yummy can't wait to eats it.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        setAttribute(player, BigChompyBirdHunting.ATTR_FYCIE_ASKED, true)
    }
}

package content.global.skill.summoning.familiar.dialogue

import content.global.skill.summoning.familiar.BurdenBeast
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.RemoteViewer
import core.api.animate
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Macaw familiar dialogues.
 */
@Initializable
class MacawDialogue(player: Player? = null) : Dialogue(player) {

    private var familiar: Familiar? = null

    override fun open(vararg args: Any?): Boolean {
        familiar = args[0] as Familiar
        options("Chat", "Remote view", "Withdraw")
        return true
    }

    override fun handle(
        interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> when (buttonId) {
                1 -> player("I don't think you'll like the stuff. Besides, I think there", "is a law about feeding birds alcohol.").also { stage++ }

                2 -> {
                    end()
                    getViewAnimation()
                    RemoteViewer.openDialogue(player, familiar)
                }

                3 -> {
                    end()
                    (familiar as BurdenBeast?)!!.openInterface()
                }
            }

            2 -> {
                end()
                openDialogue(player, MacawDialogueFile())
            }
        }
        return true
    }

    private fun getViewAnimation(): Animation = Animation.create(8013)

    fun getRandom(): Int = 40

    override fun getIds(): IntArray = intArrayOf(NPCs.MACAW_6851, NPCs.MACAW_6852)
}

private class MacawDialogueFile : DialogueFile() {
    private var branch: Int? = null

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.MACAW_6851)
        branch = (0..2).random()
        when (branch) {
            0 -> when (stage) {
                0 -> { npcl(FaceAnim.CHILD_NORMAL, "Awk! Gimme the rum! Gimme the rum!"); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "I don't think you'll like the stuff. Besides, I think there is a law about feeding birds alcohol."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { npcl(FaceAnim.CHILD_NORMAL, "Awk! I'm a pirate! Awk! Yo, ho ho!"); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "I'd best not keep you around any customs officers!"); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { npcl(FaceAnim.CHILD_NORMAL, "Awk! Caw! Shiver me timbers!"); stage++ }
                1 -> { playerl(FaceAnim.HALF_ASKING, "I wonder where you picked up all these phrases?"); stage = END_DIALOGUE }
            }
        }
    }
}
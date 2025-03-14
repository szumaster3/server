package content.global.skill.summoning.familiar.dialogue

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
import org.rs.consts.NPCs

@Initializable
class MacawDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var familiar: content.global.skill.summoning.familiar.Familiar? = null

    override fun open(vararg args: Any?): Boolean {
        familiar = args[0] as content.global.skill.summoning.familiar.Familiar
        options("Chat", "Remote view", "Withdraw")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 ->
                        player(
                            "I don't think you'll like the stuff. Besides, I think there",
                            "is a law about feeding birds alcohol.",
                        ).also { stage++ }

                    2 -> {
                        end()
                        animate(player, 8013)
                        content.global.skill.summoning.familiar.RemoteViewer
                            .openDialogue(player, familiar)
                    }

                    3 -> {
                        end()
                        (familiar as content.global.skill.summoning.familiar.BurdenBeast?)!!.openInterface()
                    }
                }

            2 -> {
                end()
                openDialogue(player, MacawDialogueFile())
            }
        }
        return true
    }

    fun getViewAnimation(): Animation {
        return Animation.create(8013)
    }

    fun getRandom(): Int {
        return 40
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MACAW_6851, NPCs.MACAW_6852)
    }
}

class MacawDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MACAW_6851)
        when ((0..2).random()) {
            0 ->
                when (stage) {
                    0 -> npcl(FaceAnim.CHILD_NORMAL, "Awk! Gimme the rum! Gimme the rum!").also { stage++ }
                    1 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I don't think you'll like the stuff. Besides, I think there is a law about feeding birds alcohol.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            1 ->
                when (stage) {
                    0 -> npcl(FaceAnim.CHILD_NORMAL, "Awk! I'm a pirate! Awk! Yo, ho ho!").also { stage++ }
                    1 ->
                        playerl(FaceAnim.FRIENDLY, "I'd best not keep you around any customs officers!").also {
                            stage =
                                END_DIALOGUE
                        }
                }

            2 ->
                when (stage) {
                    0 -> npcl(FaceAnim.CHILD_NORMAL, "Awk! Caw! Shiver me timbers!").also { stage++ }
                    1 ->
                        playerl(FaceAnim.HALF_ASKING, "I wonder where you picked up all these phrases?").also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
    }
}

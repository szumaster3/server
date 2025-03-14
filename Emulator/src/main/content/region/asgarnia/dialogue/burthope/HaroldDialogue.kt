package content.region.asgarnia.dialogue.burthope

import core.api.*
import core.api.quest.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Harold dialogue.
 */
@Initializable
class HaroldDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (isQuestInProgress(player!!, Quests.DEATH_PLATEAU, 10, 29)) {
            openDialogue(player!!, HaroldDialogue(), npc)
            return true
        }

        when (stage) {
            START_DIALOGUE -> player(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
            1 -> npc(FaceAnim.FRIENDLY, "Hi.").also { stage++ }
            2 -> player(FaceAnim.FRIENDLY, "Can I buy you a drink?").also { stage++ }
            3 -> npc(FaceAnim.HAPPY, "Now you're talking! An Asgarnian Ale, please!").also { stage++ }
            4 -> {
                if (!removeItem(player!!, Items.ASGARNIAN_ALE_1905)) {
                    player(FaceAnim.FRIENDLY, "I'll go and get you one.").also { stage = END_DIALOGUE }
                } else {
                    sendMessage(player!!, "You give Harold an Asgarnian Ale.")
                    sendItemDialogue(
                        player!!,
                        Items.ASGARNIAN_ALE_1905,
                        "You give Harold an Asgarnian Ale.",
                    ).also { stage++ }
                }
            }
            5 -> {
                end()
                animate(npc!!, Animation(Animations.EAT_OLD_829), true)
                runTask(npc!!, 3) {
                    npc(FaceAnim.FRIENDLY, "*burp*").also { stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HAROLD_1078)
    }
}

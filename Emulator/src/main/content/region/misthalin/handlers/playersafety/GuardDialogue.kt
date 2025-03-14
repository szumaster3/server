package content.region.misthalin.handlers.playersafety

import core.api.openInterface
import core.api.runTask
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.secondsToTicks
import org.rs.consts.Components
import org.rs.consts.NPCs

/**
 * Represents the Guard dialogue.
 */
@Initializable
class GuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        val hasRead = player.savedData.globalData.hasReadPlaques()
        if (!hasRead) {
            npcl(FaceAnim.ANNOYED, "Ahem! Can I help you?")
        } else {
            npcl(FaceAnim.NEUTRAL, "Can I help you?").also { stage = 10 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "I'd like to go up to the training centre, please.").also { stage++ }
            1 -> npc(FaceAnim.ANNOYED, "Sorry, citizen, you can't go up there.").also { stage++ }
            2 -> player(FaceAnim.HALF_ASKING, "Why not?").also { stage++ }
            3 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "You must learn about player safety before entering the",
                    "training centre.",
                ).also {
                    stage++
                }
            4 -> player(FaceAnim.HALF_ASKING, "Oh. How do I do that?").also { stage++ }
            5 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Each of these gublinches have been caught breaking the",
                    "Rules of ${settings!!.name}. You should read the plaques on",
                    "each of their cells to learn what they did wrong.",
                ).also {
                    stage++
                }
            6 ->
                player(
                    FaceAnim.SAD,
                    "Oh, right. I can enter the training centre once I have",
                    "done that?",
                ).also { stage++ }
            7 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Yes. Once you have examined each of the plaques,",
                    "come and speak to me and I will tell you about the",
                    "Report Abuse function.",
                ).also {
                    stage++
                }
            8 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "After that, I can let you into the training centre,",
                    "upstairs.",
                ).also { stage++ }
            9 -> {
                end()
                player(FaceAnim.FRIENDLY, "Okay, thanks for the help.")
                sendMessage(player, "You need to read the jail plaques before the guard will allow you upstairs.")
            }
            10 -> player(FaceAnim.FRIENDLY, "Can I go upstairs?").also { stage++ }
            11 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Yes citizen. Before you do, I am instructed to give",
                    "you one final piece of information.",
                ).also {
                    stage++
                }
            12 -> player(FaceAnim.NEUTRAL, "Oh, okay then.").also { stage++ }
            13 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "In your travels around ${settings!!.name}, should you find a ",
                    "player who acts in a way that breaks one of our rules,",
                    "you should report them.",
                ).also {
                    stage++
                }
            14 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Reporting is very simple and easy to do. Simply click",
                    "the Report Abuse button at the button of the screen",
                    "and you will be shown of following screen:",
                ).also {
                    stage++
                }
            15 -> {
                openInterface(player, Components.TUTORIAL2_REPORT_ABUSE_700)
                if (buttonId == 27) {
                    sendMessage(player, "Please close the interface you have open before using 'Report Abuse'")
                    return false
                }
                runTask(player, secondsToTicks(5)) {
                    if (player != null) player.interfaceManager.close()
                    return@runTask
                }.also { stage++ }
            }
            18 ->
                npc(
                    "Simply enter the player's name in the box and click the",
                    "rule that the offender was breaking.",
                ).also {
                    stage++
                }
            19 -> player(FaceAnim.FRIENDLY, "Okay. Then what?").also { stage++ }
            20 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "That's it! It really is that simple and it only takes a",
                    "moment to do. Now you may enter the training",
                    "centre. Good luck, citizen.",
                ).also {
                    stage++
                }
            21 -> player(FaceAnim.HALF_GUILTY, "Thanks!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARD_7142)
    }
}

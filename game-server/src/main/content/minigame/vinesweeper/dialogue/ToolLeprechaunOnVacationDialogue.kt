package content.minigame.vinesweeper.dialogue

import core.api.openNpcShop
import core.api.openInterface
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs

/**
 * Represents the Tool Leprechaun (Trollweis) dialogue.
 */
@Initializable
class ToolLeprechaunOnVacationDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_HAPPY, "Aye, top o' th' mornin' to ya!", "Are ye wantin' help with th' tool store?")
        stage = 2
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            2 -> showTopics(
                Topic(FaceAnim.NEUTRAL, "Yes please.", 10),
                Topic(FaceAnim.THINKING, "Why are you sunbathing up a mountain?", 3),
                Topic(FaceAnim.NEUTRAL, "No thanks, I'll keep hold of my stuff.", 20),
                Topic("Would you like to trade?", 12),
            )
            3 -> npcl(FaceAnim.OLD_HAPPY, "We tool leprechauns work hard, that we do. An'nary a penny do we get in return. So ye cannae begrudge me mah holiday an' a wee drink or twelve!").also { stage++ }
            4 -> playerl(FaceAnim.THINKING, "Yes, very nice, but why are you sunbathing up a mountain? Surely a beach would be more appropriate?").also { stage++ }
            5 -> npcl(FaceAnim.OLD_HAPPY, "Ahh, but I likes th' ruggedy mountain, ye see. Also, I ha' a terrible allergy to sand.").also { stage++ }
            6 -> playerl(FaceAnim.NEUTRAL, "Fair enough, I suppose.").also { stage++ }
            7 -> npcl(FaceAnim.OLD_HAPPY, "So were ye wantin' help with th' tool store?").also { stage++ }
            8 -> showTopics(
                Topic(FaceAnim.NEUTRAL, "Yes, please.", 10),
                Topic(FaceAnim.NEUTRAL, "No thanks, I'll keep hold of my stuff.", 20),
            )
            10 -> {
                end()
                openInterface(player, Components.FARMING_TOOLS_125)
            }
            12 -> npcl(FaceAnim.OLD_HAPPY, "Sure, have a look.").also { stage++ }
            13 -> {
                end()
                openNpcShop(player, NPCs.TOOL_LEPRECHAUN_4965)
            }
            20 -> npcl(FaceAnim.OLD_NORMAL, "Ye must be dafter than ye look if ye likes luggin' yer tools everywhere ye goes!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.TOOL_LEPRECHAUN_4965)
}
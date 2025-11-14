package content.region.island.tutorial.dialogue

import content.data.GameAttributes
import content.region.island.tutorial.plugin.TutorialStage
import core.api.clearHintIcon
import core.api.getAttribute
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class RSGuideDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val tutStage = getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)

        when {
            tutStage == 39 -> {
                npc(FaceAnim.HAPPY, "Greetings! I see you are a new arrival to this land. My", "job is to welcome all new visitors. So welcome!")
                stage = 0
            }
            tutStage == 2 -> {
                npc(FaceAnim.NEUTRAL, "I'm glad you're making progress!")
                stage = 6
            }
            tutStage < 2 -> {
                npc(FaceAnim.NEUTRAL, "You will notice a flashing icon of a spanner; please click", "on this to continue the tutorial.")
                stage = END_DIALOGUE
            }
            else -> {
                npc(FaceAnim.HALF_GUILTY, "Please follow the onscreen instructions!")
                stage = END_DIALOGUE
            }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.NEUTRAL, "You have already learned the first thing needed to", "succeed in this world: talking to other people!").also { stage++ }
            1 -> npc(FaceAnim.NEUTRAL, "You will find many inhabitants of this world have useful", "things to say to you. By clicking on them with your", "mouse you can talk to them.").also { stage++ }
            2 -> npc(FaceAnim.NEUTRAL, "I would also suggest reading through some of the", "supporting information on the website. There you can", "find the Game Guide, which contain all the additional", "information you're ever likely to need. It also contains").also { stage++ }
            3 -> npc(FaceAnim.NEUTRAL, "maps and helpful tips to help you on your journey.").also { stage++ }
            4 -> npc(FaceAnim.HAPPY, "You will notice a flashing icon of a spanner; please click", "on this to continue the tutorial.").also { stage++ }
            5 -> {
                end()
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 1)
                TutorialStage.load(player, 1)
            }
            6 -> npc(FaceAnim.NEUTRAL, "To continue the tutorial go through that door over", "there and speak to your first instructor!").also { stage++ }
            7 -> {
                end()
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 3)
                TutorialStage.load(player, 3)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = RSGuideDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.RUNESCAPE_GUIDE_945)
}

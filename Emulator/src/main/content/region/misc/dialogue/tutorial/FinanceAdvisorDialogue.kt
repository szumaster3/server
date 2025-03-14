package content.region.misc.dialogue.tutorial

import content.region.misc.handlers.tutorial.TutorialStage
import core.api.getAttribute
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class FinanceAdvisorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            58 -> playerl(FaceAnim.FRIENDLY, "Hello, who are you?")
            59 -> npcl(FaceAnim.FRIENDLY, "Move along, now.").also { return false }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            58 ->
                when (stage++) {
                    0 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'm the Financial Advisor. I'm here to tell people how to make money.",
                        )
                    1 -> playerl(FaceAnim.FRIENDLY, "Okay. How can I make money then?")
                    2 -> npcl(FaceAnim.HALF_THINKING, "How you can make money? Quite.")
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well there are three basic ways of making money here: combat, quests, and trading. I will talk you through each of them very quickly.",
                        )
                    4 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Let's start with combat as it is probably still fresh in your mind. Many enemies, both human and monster will drop items when they die.",
                        )
                    5 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Now, the next way to earn money quickly is by quests. Many people on " + settings!!.name +
                                " have things they need doing, which they will reward you for.",
                        )
                    6 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "By getting a high level in skills such as Cooking, Mining, Smithing or Fishing, you can create or catch your own items and sell them for pure profit.",
                        )
                    7 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Lastly, we have jobs you can get from tutors in Lumbridge. These pay very handsomely early on!",
                        ).also {
                            stage++
                        }
                    8 -> npcl(FaceAnim.FRIENDLY, "Well, that about covers it. Move along now.")
                    9 -> {
                        end()
                        setAttribute(player, TutorialStage.TUTORIAL_STAGE, 59)
                        TutorialStage.load(player, 59)
                    }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FinanceAdvisorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FINANCIAL_ADVISOR_947)
    }
}

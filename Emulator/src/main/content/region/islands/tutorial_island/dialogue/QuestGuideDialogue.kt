package content.region.islands.tutorial_island.dialogue

import content.region.islands.tutorial_island.plugin.TutorialStage
import core.api.*
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class QuestGuideDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            27 -> npc(
                FaceAnim.FRIENDLY,
                "Ah. Welcome, adventurer. I'm here to tell you all about",
                "quests. Lets start by opening the Quest List.",
            )

            28 -> npc(
                FaceAnim.FRIENDLY,
                "Now you have the journal open. I'll tell you a bit about",
                "it At the moment all the quests are shown in red, which",
                "means you have not started them yet.",
            )

            in 29..72 -> npc(FaceAnim.HALF_ASKING, "Would you like to hear about quests again?")

            else -> return false
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            27 -> {
                sendUnclosablePlainDialogue(
                    player,
                    true,
                    "${core.tools.BLUE}Open the Quest Journal.",
                    "",
                    "Click on the flashing icon next to your inventory.",
                )
                TutorialStage.removeHintIcon(player)
                setVarbit(player, 3756, 3)
                player.interfaceManager.openTab(Component(Components.QUESTJOURNAL_V2_274))
            }

            28 -> when (stage) {
                0 -> npc(
                    "When you start a quest it will change colour to yellow,",
                    "and to green when you've finished. This is so you can",
                    "easily see what's complete, what's started and what's left",
                    "to begin.",
                ).also {
                    stage++
                }

                1 -> npc(
                    "The start of quests are easy to find. Look out for the",
                    "star icons on the minimap, just like the one you should",
                    "see marking my house.",
                ).also {
                    stage++
                }

                2 -> npc(
                    "There's not a lot more I can tell you about questing.",
                    "You have to experience the thrill of it yourself to fully",
                    "understand. You may find some adventure in the caves",
                    "under my house.",
                ).also {
                    stage++
                }

                3 -> {
                    end()
                    setAttribute(player, TutorialStage.TUTORIAL_STAGE, 29)
                    TutorialStage.load(player, 29)
                }
            }

            in 29..72 -> when (stage) {
                0 -> options("Yes!", "Nope, I'm ready to move on!").also { stage++ }
                1 -> when (buttonId) {
                    1 -> player("Yes!").also { stage++ }
                    2 -> player("Nope, I'm ready to move on!").also { stage = 8 }
                }
                2 -> npcl(FaceAnim.FRIENDLY, "Within your quest list, you'll unsurprisingly find a list of quests. Clicking one of these quests will display some more information on it.").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "If you haven't started the quest, it will tell you where to begin and what requirements you need. If the quest is in progress, it will remind you what to do next.").also { stage++ }
                4 -> npcl(FaceAnim.FRIENDLY, " It's very easy to find quest start points. Just look out for the quest icon on your minimap. You should see one marking this house.").also { stage++ }
                5 -> sendItemDialogue(player, Items.NULL_5092, "The minimap in the top right corner of the screen has various icons to show different points of interest. Look for the icon to the left to find quest start points.").also { stage++ }
                6 -> npcl(FaceAnim.FRIENDLY, "The quests themselves can vary greatly from collecting beads to hunting down dragons. Completing quests will reward you with all sorts of things, such as new areas and better weapons!").also { stage++ }
                7 -> npcl(FaceAnim.FRIENDLY, "There's not a lot more I can tell you about questing. You have to experience the thrill of it yourself to fully understand.").also { stage = END_DIALOGUE }
                8 -> npcl(FaceAnim.FRIENDLY, "Okay then.").also { stage++ }
                9 -> TutorialStage.rollback(player!!)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = QuestGuideDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.QUEST_GUIDE_949)
}

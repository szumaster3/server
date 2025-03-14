package content.region.misc.dialogue.tutorial

import content.region.misc.handlers.tutorial.TutorialStage
import core.api.*
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.NPCs

@Initializable
class QuestGuideDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            27 ->
                sendTutorialNPCDialogue(
                    player,
                    npc.id,
                    FaceAnim.FRIENDLY,
                    "Ah. Welcome, adventurer. I'm here to tell you all about",
                    "quests. Lets start by opening the Quest List.",
                )

            28 ->
                sendTutorialNPCDialogue(
                    player,
                    npc.id,
                    FaceAnim.FRIENDLY,
                    "Now you have the journal open. I'll tell you a bit about",
                    "it At the moment all the quests are shown in red, which",
                    "means you have not started them yet.",
                )

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

            28 ->
                when (stage) {
                    0 ->
                        sendTutorialNPCDialogue(
                            player,
                            npc.id,
                            "When you start a quest it will change colour to yellow,",
                            "and to green when you've finished. This is so you can",
                            "easily see what's complete, what's started and what's left",
                            "to begin.",
                        ).also {
                            stage++
                        }

                    1 ->
                        sendTutorialNPCDialogue(
                            player,
                            npc.id,
                            "The start of quests are easy to find. Look out for the",
                            "star icons on the minimap, just like the one you should",
                            "see marking my house.",
                        ).also {
                            stage++
                        }

                    2 ->
                        sendTutorialNPCDialogue(
                            player,
                            npc.id,
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
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return QuestGuideDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.QUEST_GUIDE_949)
    }
}

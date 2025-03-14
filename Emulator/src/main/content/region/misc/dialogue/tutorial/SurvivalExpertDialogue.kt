package content.region.misc.dialogue.tutorial

import content.region.misc.handlers.tutorial.TutorialStage
import core.api.*
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SurvivalExpertDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val tutStage = player?.getAttribute("tutorial:stage", 0) ?: 0
        // Not so close, please. I don't want you to burn me!
        when (tutStage) {
            4 ->
                sendTutorialNPCDialogue(
                    player,
                    NPCs.SURVIVAL_EXPERT_943,
                    "Hello there, newcomer. My name is Brynna. My job is",
                    "to teach you a few survival tips and tricks. First off",
                    "we're going to start with the most basic survival skill of",
                    "all: making a fire.",
                )
            11 ->
                sendTutorialNPCDialogue(
                    player,
                    NPCs.SURVIVAL_EXPERT_943,
                    "Well done! Next we need to get some food in our",
                    "bellies. We'll need something to cook. There are shrimp",
                    "in the pond there, so let's catch and cook some.",
                )
            5, 14, 15 -> {
                if (!inInventory(player, Items.BRONZE_AXE_1351)) {
                    sendItemDialogue(player, Items.BRONZE_AXE_1351, "The Survival Expert gives you a spare bronze axe.")
                    addItem(player, Items.BRONZE_AXE_1351)
                }
                if (!inInventory(player, Items.TINDERBOX_590)) {
                    sendItemDialogue(player, Items.TINDERBOX_590, "The Survival Expert gives you a spare tinderbox.")
                    addItem(player, Items.TINDERBOX_590)
                }
                return false
            }
            8 ->
                npcl(FaceAnim.FRIENDLY, "Light the logs in your backpack to make a fire.").also {
                    TutorialStage.rollback(player)
                    stage = END_DIALOGUE
                }
            12 ->
                if (!inInventory(player, Items.SMALL_FISHING_NET_303)) {
                    Component.setUnclosable(
                        player,
                        interpreter.sendItemMessage(
                            Items.SMALL_FISHING_NET_303,
                            "The Survival Guide gives you a <col=08088A>net</col>!",
                        ),
                    )
                    addItem(player, Items.SMALL_FISHING_NET_303)
                } else {
                    sendTutorialNPCDialogue(
                        player,
                        NPCs.SURVIVAL_EXPERT_943,
                        "Well done! Next we need to get some food in our",
                        "bellies. We'll need something to cook. There are shrimp",
                        "in the pond there, so let's catch and cook some.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                }
            16 -> end().also { openDialogue(player, SurvivalExpert()) }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (player?.getAttribute(TutorialStage.TUTORIAL_STAGE, 0)) {
            4 ->
                when (stage) {
                    0 -> {
                        Component.setUnclosable(
                            player,
                            interpreter.sendDoubleItemMessage(
                                Items.TINDERBOX_590,
                                Items.BRONZE_AXE_1351,
                                "The Survival Guide gives you a <col=08088A>tinderbox</col> and a <col=08088A>bronze axe</col>!",
                            ),
                        )
                        addItem(player, Items.TINDERBOX_590)
                        addItem(player, Items.BRONZE_AXE_1351)
                        stage++
                    }

                    1 -> {
                        end()
                        setAttribute(player, TutorialStage.TUTORIAL_STAGE, 5)
                        TutorialStage.load(player, 5)
                    }
                }

            11 ->
                when (stage) {
                    0 -> {
                        Component.setUnclosable(
                            player,
                            interpreter.sendItemMessage(
                                Items.SMALL_FISHING_NET_303,
                                "The Survival Guide gives you a <col=08088A>net</col>!",
                            ),
                        )
                        addItem(player, Items.SMALL_FISHING_NET_303)
                        stage++
                    }

                    1 -> {
                        end()
                        setAttribute(player, TutorialStage.TUTORIAL_STAGE, 12)
                        TutorialStage.load(player, 12)
                    }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SurvivalExpertDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SURVIVAL_EXPERT_943)
    }
}

class SurvivalExpert : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SURVIVAL_EXPERT_943)
        when (stage) {
            0 -> {
                setTitle(player!!, 5)
                sendDialogueOptions(
                    player!!,
                    title = "What would you like to hear more about?",
                    "Crafting the logs.",
                    "Light a fire.",
                    "Netting a Fishing spot.",
                    "Cooking shrimp.",
                    "Nothing, thanks.",
                )
                stage++
            }
            1 ->
                when (buttonID) {
                    1 ->
                        npcl(
                            FaceAnim.HAPPY,
                            "After you leave Tutorial Island, you'll be able to use your <col=08088A>Fletching</col> skill to craft your own bows and arrows from trees.",
                        ).also {
                            stage =
                                2
                        }
                    2 -> npcl(FaceAnim.FRIENDLY, "Light the logs in your backpack to make a fire.").also { stage = 0 }
                    3 ->
                        npcl(
                            FaceAnim.HAPPY,
                            "Whenever you see bubbles in the water, there's probably some good fishing to be had there!",
                        ).also {
                            stage =
                                0
                        }
                    4 ->
                        npcl(
                            FaceAnim.HAPPY,
                            "You'll cook your shrimp on a fire. If you're fire's gone out, chop a tree to get some logs and make a new fire.",
                        ).also {
                            stage =
                                3
                        }
                    5 ->
                        npcl(
                            FaceAnim.HAPPY,
                            "Open the gate, follow the path to the next area and talk to the master chef. He'll teach you more about <col=08088A>Cooking</col>.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            2 -> npcl(FaceAnim.FRIENDLY, "For now, right-click the logs and left-click Light.").also { stage = 0 }
            3 -> npcl(FaceAnim.FRIENDLY, "Then use the shrimp on the fire.").also { stage = 0 }
            END_DIALOGUE -> TutorialStage.rollback(player!!)
        }
    }
}

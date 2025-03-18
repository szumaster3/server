package content.region.misc.dialogue.tutorial

import content.region.misc.handlers.tutorial.TutorialStage
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class MiningInstructorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            30 ->
                sendNPCDialogue(
                    player,
                    npc.id,
                    "Hi there. You must be new around here. So what do I call you? 'Newcomer' seems so impersonal, and if we're going to be working together, I'd rather tell you by name.",
                )
            34 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "I prospected both types of rock! One set contains tin and the other has copper ore inside.",
                )
            35 -> {
                if (!inInventory(player, Items.BRONZE_PICKAXE_1265)) {
                    addItem(player, Items.BRONZE_PICKAXE_1265)
                    sendItemDialogue(
                        player,
                        Items.BRONZE_PICKAXE_1265,
                        "Dezzick gives you a$BLUE bronze pickaxe</col>!",
                    )
                    stage = 3
                } else {
                    TutorialStage.load(player, 35)
                }
            }

            40 -> playerl(FaceAnim.ASKING, "How do I make a weapon out of this?")
            41 -> {
                if (!inInventory(player, Items.HAMMER_2347)) {
                    addItem(player, Items.HAMMER_2347)
                    sendItemDialogue(player, Items.HAMMER_2347, "Dezzick gives you a$BLUE hammer</col>!")
                    stage = 3
                } else {
                    end()
                    TutorialStage.load(player, 41)
                }
            }

            in 43..100 -> end().also { openDialogue(player, MiningInstruction()) }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            30 ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "You can call me ${player.username}.").also { stage++ }
                    1 ->
                        sendNPCDialogue(
                            player,
                            npc.id,
                            "Ok then, ${player.username}. My name is Dezzick and I'm a miner by trade. Let's prospect some of these rocks.",
                        ).also {
                            stage++
                        }
                    2 -> {
                        end()
                        setAttribute(player, TutorialStage.TUTORIAL_STAGE, 31)
                        TutorialStage.load(player, 31)
                    }
                }
            34, 35 ->
                when (stage) {
                    0 ->
                        sendNPCDialogue(
                            player,
                            npc.id,
                            "Absolutely right, ${player.username}. These two ore types can be smelted together to make bronze.",
                        ).also {
                            stage++
                        }
                    1 ->
                        sendNPCDialogue(
                            player,
                            npc.id,
                            "So now you know what ore is in the rocks over there, why don't you have a go at mining some tin and copper? Here, you'll need this to start with.",
                        ).also {
                            stage++
                        }
                    2 -> {
                        addItem(player, Items.BRONZE_PICKAXE_1265)
                        sendItemDialogue(
                            player,
                            Items.BRONZE_PICKAXE_1265,
                            "Dezzick gives you a$BLUE bronze pickaxe</col>!",
                        )
                        stage++
                    }
                    3 -> {
                        end()
                        setAttribute(player, TutorialStage.TUTORIAL_STAGE, 35)
                        TutorialStage.load(player, 35)
                    }
                }
            40, 41 ->
                when (stage) {
                    0 ->
                        sendNPCDialogue(
                            player,
                            npc.id,
                            "Okay, I'll show you how to make a dagger out of it. You'll be needing this..",
                        ).also {
                            stage++
                        }
                    1 -> {
                        addItem(player, Items.HAMMER_2347)
                        sendItemDialogue(player, Items.HAMMER_2347, "Drezzick gives you a$BLUE hammer</col>!")
                        stage++
                    }
                    2 -> {
                        end()
                        setAttribute(player, TutorialStage.TUTORIAL_STAGE, 41)
                        TutorialStage.load(player, 41)
                    }
                }
            in 43..100 -> {
                when (buttonId) {
                    0 -> player("Tell me about prospecting again.").also { stage = 150 }
                    1 -> player("Tell me about Mining again.").also { stage = 200 }
                    2 -> TutorialStage.rollback(player)
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = MiningInstructorDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.MINING_INSTRUCTOR_948)
}

class MiningInstruction : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MINING_INSTRUCTOR_948)
        when (stage) {
            0 -> {
                setTitle(player!!, 3)
                sendDialogueOptions(
                    player!!,
                    title = "What would you like to hear more about?",
                    "Tell me about prospecting again.",
                    "Tell me about Mining again.",
                    "Nope, I'm ready to move on!",
                )
                stage++
            }
            1 ->
                when (buttonID) {
                    0 -> player("Tell me about prospecting again.").also { stage++ }
                    1 -> player("Tell me about Mining again.").also { stage = 3 }
                    2 -> end().also { TutorialStage.rollback(player!!) }
                }
            2 -> {
                sendNPCDialogue(
                    player!!,
                    npc!!.id,
                    "To prospect a mineable rock, just right click it and select the 'prospect rock' option. This will tell you the type of ore you can mine from it. Try it now on one of the rocks indicated.",
                )
                TutorialStage.rollback(player!!)
                end()
            }
            3 -> {
                sendNPCDialogue(
                    player!!,
                    npc!!.id,
                    "It's quite simple really. All you need to do is right click on the rock and select 'mine' You can only mine when you have a pickaxe. So give it a try: first mine one tin ore.",
                )
                TutorialStage.rollback(player!!)
                end()
            }
        }
    }
}

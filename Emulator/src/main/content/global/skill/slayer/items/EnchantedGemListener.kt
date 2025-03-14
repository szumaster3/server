package content.global.skill.slayer.items

import content.global.skill.slayer.Tasks
import core.api.interaction.*
import core.api.openDialogue
import core.api.setVarp
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class EnchantedGemListener : InteractionListener {
    override fun defineListeners() {
        on(Items.ENCHANTED_GEM_4155, IntType.ITEM, "activate") { player, _ ->
            openDialogue(player, EnchantedGemDialogue())
            return@on true
        }
    }
}

class EnchantedGemDialogue : DialogueFile() {
    var firstRun = true

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = getSlayerMaster(player!!)
        val expression = if (npc == NPC(NPCs.CHAELDAR_1598)) FaceAnim.OLD_NORMAL else FaceAnim.HALF_ASKING
        when (stage) {
            0 -> npcl(expression, "Hello there ${player!!.username}, what can I help you with?").also { stage++ }
            1 ->
                showTopics(
                    Topic(FaceAnim.ASKING, "How am I doing so far?", 100),
                    Topic(FaceAnim.HALF_ASKING, "Who are you?", 200),
                    Topic(FaceAnim.HALF_ASKING, "Where are you?", 300),
                    Topic(FaceAnim.FRIENDLY, "Got any tips for me?", 400),
                    IfTopic(FaceAnim.FRIENDLY, "Nothing really.", END_DIALOGUE, firstRun),
                    IfTopic(FaceAnim.HAPPY, "That's all thanks.", END_DIALOGUE, !firstRun),
                )

            100 -> {
                firstRun = false
                if (!hasSlayerTask(player!!)) {
                    npcl(
                        expression,
                        "You need something new to hunt. Come and see me when you can and I'll give you a new task.",
                    ).also {
                        stage =
                            1
                    }
                } else {
                    if (getSlayerTask(player!!) == Tasks.JAD) {
                        npcl(expression, "You're currently assigned to kill TzTok-Jad!")
                    } else {
                        npcl(
                            expression,
                            "You're currently assigned to kill ${getSlayerTaskName(
                                player!!,
                            )}s; only ${getSlayerTaskKillsRemaining(player!!)} more to go.",
                        )
                    }
                    setVarp(player!!, 2502, getSlayerTaskFlags(player!!) shr 4)
                    stage = 1
                }
            }

            200 -> {
                firstRun = false
                npcl(
                    expression,
                    "My name's ${getSlayerMaster(player!!).name}, I'm the Slayer Master best able to train you.",
                ).also {
                    stage =
                        1
                }
            }

            300 -> {
                firstRun = false
                npcl(
                    expression,
                    "You'll find me in ${getSlayerMasterLocation(player!!)}, I'll be here when you need a new task.",
                ).also {
                    stage =
                        1
                }
            }

            400 -> {
                firstRun = false
                npc(expression, *getSlayerTip(player!!))
                stage++
            }

            401 -> player(FaceAnim.HAPPY, "Great, thanks!").also { stage = 1 }
        }
    }
}

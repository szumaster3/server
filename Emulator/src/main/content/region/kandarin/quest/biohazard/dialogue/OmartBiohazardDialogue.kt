package content.region.kandarin.quest.biohazard.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class OmartBiohazardDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.BIOHAZARD)
        npc = NPC(NPCs.OMART_350)
        when {
            (questStage in 2..3) -> {
                when (stage) {
                    0 -> player("Omart, Jerico said you might be able to help me.").also { stage++ }
                    1 ->
                        npc(
                            "He informed me of your problem traveller. I would",
                            "be glad to help, I have a rope ladder and my associate, ",
                            "Kilron, is waiting on the other side.",
                        ).also {
                            stage++
                        }

                    2 -> player("Good stuff.").also { stage++ }
                    3 ->
                        npc(
                            "Unfortunately we can't risk it with the watch tower so",
                            "close. So first we need to distract the guards in the tower.",
                        ).also {
                            stage++
                        }

                    4 -> player("How?").also { stage++ }
                    5 ->
                        npc(
                            "Try asking Jerico, if he's not too busy with his pigeons.",
                            "I'll be waiting here for you.",
                        ).also { stage++ }

                    6 -> end()
                }
            }

            (questStage == 4) -> {
                when (stage) {
                    0 -> {
                        npc(
                            "Well done, the guards are having real trouble with those",
                            "birds. You must go now traveller, it's your only chance.",
                        )
                        sendMessage(player!!, "Omart calls to his associate.")
                        stage++
                    }

                    1 -> {
                        sendMessage(player!!, "He throws one end of the rope ladder over the wall.")
                        npc("Killron!")
                        stage++
                    }

                    2 -> npc("You must go now traveller.").also { stage++ }
                    3 -> options("Ok, lets do it.", "I'll be back soon.").also { stage++ }
                    4 ->
                        when (buttonID) {
                            1 -> player("Ok, lets do it.").also { stage = 6 }
                            2 -> player("I'll be back soon.").also { stage++ }
                        }

                    5 ->
                        npc("Don't take too long, those mourners will soon be", "rid of those birds.").also {
                            stage = END_DIALOGUE
                        }

                    6 -> {
                        end()
                        sendMessage(player!!, "You climb up the rope ladder...")
                        runTask(player!!, 2) {
                            teleport(player!!, location(2556, 3267, 0))
                            sendMessage(player!!, "and drop down on the other side.")
                        }
                    }
                }
            }

            (questStage in 4..99) -> {
                when (stage) {
                    0 -> player("Hello Omart.").also { stage++ }
                    1 ->
                        npc(
                            "Hello traveller. The guards are still distracted",
                            "if you wish to cross the wall.",
                        ).also { stage++ }

                    2 -> options("Ok, lets do it.", "I'll be back soon.").also { stage++ }
                    3 ->
                        when (buttonID) {
                            1 -> player("Ok, lets do it.").also { stage = 5 }
                            2 -> player("I'll be back soon.").also { stage++ }
                        }

                    4 ->
                        npc("Don't take too long, those mourners will soon be", "rid of those birds.").also {
                            stage = END_DIALOGUE
                        }

                    5 -> {
                        end()
                        sendMessage(player!!, "You climb up the rope ladder...")
                        runTask(player!!, 2) {
                            teleport(player!!, location(2556, 3267, 0))
                            sendMessage(player!!, "and drop down on the other side.")
                        }
                    }
                }
            }
        }
    }
}

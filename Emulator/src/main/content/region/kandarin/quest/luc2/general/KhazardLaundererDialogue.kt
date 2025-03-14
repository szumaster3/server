package content.region.kandarin.quest.luc2.general

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.quest.getQuestStage
import core.api.sendItemDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class KhazardLaundererDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.WHILE_GUTHIX_SLEEPS)
        npc = NPC(NPCs.KHAZARD_LAUNDERER_8428)
        when (questStage) {
            0 -> npc("I just do the washing around here.").also { stage = END_DIALOGUE }
            1 -> {
                when (stage) {
                    0 -> npcl("What can I do for you?").also { stage++ }
                    1 ->
                        showTopics(
                            Topic("What do you do here?", stage++),
                            Topic("Do you know someone called Movario?", stage++),
                            IfTopic(
                                "Do you have more dirty clothes belonging to Movario I could have?",
                                stage++,
                                !inInventory(player!!, Items.DIRTY_LAUNDRY_14460),
                            ),
                        )
                    2 ->
                        npcl(
                            "I mainly take in washing and make sure it's good and clean for my customers.",
                        ).also { stage++ }
                    3 ->
                        npcl(
                            "Times are tough, though. Not everyone can afford such a service and it's affecting my takings.",
                        ).also {
                            stage++
                        }
                    4 -> player("Do you find that very fulfilling?").also { stage++ }
                    5 ->
                        npcl(
                            "Ah, well, it can be a bit monotonous and the pay isn't good, but I set myself some goals and deliver my washing clean.",
                        ).also {
                            stage++
                        }
                    6 ->
                        npcl(
                            "My customers rarely complain, so I guess I must be doing something right.",
                        ).also { stage++ }
                    7 -> player("Either that or they're afraid of you!").also { stage++ }
                    8 ->
                        npc(
                            "Aha, yes, good one. Many of my customers are pretty important, I'm sure they'd have a word with me if they thought that quality was slipping.",
                        ).also {
                            stage =
                                1
                        }
                    9 -> npcl("Oh, yes, Movario...").also { stage++ }
                    10 ->
                        npcl(
                            "Oh... er, well, sorry, I'm not really supposed to divulge client details.",
                        ).also { stage++ }
                    11 ->
                        npcl(
                            "Times are tough enough as it is; I can do without losing customers because of my gossiping!",
                        ).also {
                            stage++
                        }
                    12 -> npcl("Sorry.").also { stage = END_DIALOGUE }
                    13 -> {
                        if (!inInventory(player!!, Items.DIRTY_LAUNDRY_14460)) {
                            npcl("I don't have any more dirty clothes for Movario at the moment.").also {
                                stage =
                                    END_DIALOGUE
                            }
                        } else {
                            npcl("You're in luck: here you go, but look after them this time.").also { stage++ }
                        }
                    }
                    14 -> {
                        end()
                        sendItemDialogue(player!!, Items.DIRTY_LAUNDRY_14460, "You're handed some stinky clothes.")
                        addItemOrDrop(player!!, Items.DIRTY_LAUNDRY_14460)
                    }
                }
            }
        }
    }
}

package content.region.kandarin.quest.biohazard.dialogue

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs
import org.rs.consts.Quests

class JericoDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.BIOHAZARD)
        npc = NPC(NPCs.JERICO_366)
        when {
            (questStage in 1..2) -> {
                when (stage) {
                    0 -> player("Hello Jerico.").also { stage++ }
                    1 ->
                        npc(
                            "Hello, I've been expecting you. Elena tells me you need",
                            "to cross the wall.",
                        ).also { stage++ }
                    2 -> player("That's right.").also { stage++ }
                    3 -> npc("My messenger pigeons help me communicate with", "friends over the wall.").also { stage++ }
                    4 ->
                        npc(
                            "I have arranged for two friends to aid you with a rope",
                            "ladder. Omart is waiting for you at the south end of the",
                            "wall.",
                        ).also {
                            stage++
                        }
                    5 ->
                        npc(
                            "But be careful, if the mourners catch you the",
                            "punishment will be severe.",
                        ).also { stage++ }
                    6 -> player("Thanks Jerico.").also { stage++ }
                    7 -> {
                        end()
                        setQuestStage(player!!, Quests.BIOHAZARD, 2)
                    }
                }
            }

            (questStage == 3) -> {
                when (stage) {
                    0 ->
                        player(
                            "Hello Jerico, I need someway to distract the",
                            "watch tower, any ideas?",
                        ).also { stage++ }

                    1 -> npc("Hmmm. Nothing springs to mind.").also { stage++ }
                    2 ->
                        options(
                            "Maybe you could shout and scream, and call them away?",
                            "Maybe I could use your messenger pigeons to distract them?",
                            "Maybe if i'm really quiet they won't notice me?",
                            "I can't think of anything either.",
                        ).also { stage++ }

                    3 ->
                        when (buttonID) {
                            1 -> player("Maybe you could shout and scream, and call them away?").also { stage++ }
                            2 -> player("Maybe I could use your messenger pigeons to distract them?").also { stage = 7 }
                            3 -> player("Maybe if i'm really quiet they won't notice me?").also { stage = 11 }
                            4 -> player("I can't think of anything either.").also { stage = 15 }
                        }

                    4 -> npc("So they chase after me?").also { stage++ }
                    5 -> player("Yes. How quickly can you run?").also { stage++ }
                    6 -> npc("No. I don't like this idea.").also { stage = 2 }
                    7 -> npc("You might have some luck with that idea.").also { stage++ }
                    8 -> npc("The pigeons are around the back of my house", "if you want to try that.").also { stage++ }
                    9 -> player("Ok, maybe I'll give it a go.").also { stage++ }
                    10 -> end()
                    11 -> npc("And what stops them seeing you?").also { stage++ }
                    12 -> player("Well... perhaps I wait till nightfall?").also { stage++ }
                    13 -> npc("There's no time for that.").also { stage = 10 }
                    15 -> npc("That's too bad.").also { stage = 10 }
                }
            }

            (questStage in 4..8) -> {
                when (stage) {
                    0 -> player("Hello again Jerico.").also { stage++ }
                    1 -> npc("So you've returned traveller. Did you get what you wanted?").also { stage++ }
                    2 -> player("Not yet.").also { stage++ }
                    3 -> npc("Omart will be waiting by the wall, in", "case you need to cross again.").also { stage++ }
                    4 -> end()
                }
            }
        }
    }
}

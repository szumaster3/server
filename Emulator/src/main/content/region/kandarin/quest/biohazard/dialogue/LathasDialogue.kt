package content.region.kandarin.quest.biohazard.dialogue

import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs
import org.rs.consts.Quests

class LathasDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.BIOHAZARD)
        npc = NPC(NPCs.KING_LATHAS_364)
        when {
            (questStage >= 16) -> {
                when (stage) {
                    0 ->
                        player(
                            FaceAnim.SUSPICIOUS,
                            "I assume that you are the King of East Ardougne?",
                        ).also { stage++ }

                    1 ->
                        npc(
                            FaceAnim.ANNOYED,
                            "You assume correctly, but where do you get such",
                            "impertinence.",
                        ).also { stage++ }

                    2 -> player("I get it from finding out that the plague is a hoax.").also { stage++ }
                    3 -> npc("A hoax? I've never heard such a ridiculous thing...").also { stage++ }
                    4 -> player("I have evidence, from Guidor of Varrock.").also { stage++ }
                    5 ->
                        npc(
                            "Ah... I see. Well then you are right about the plague.",
                            "But I did it for the good of my people.",
                        ).also { stage++ }

                    6 -> player("When is it ever good to lie to people like that?").also { stage++ }
                    7 ->
                        npc(
                            "When it protects them from a far greater danger, a",
                            "fear too big to fathom.",
                        ).also { stage++ }

                    8 -> options("I don't understand...", "Well I've wasted enough of my time here.").also { stage++ }
                    9 ->
                        when (buttonID) {
                            1 -> player("I don't understand...").also { stage = 13 }
                            2 -> player("Well I've wasted enough of my time here.").also { stage = 11 }
                        }

                    10 -> player("Well I've wasted enough of my time here.").also { stage++ }
                    11 -> npc("No time is ever wasted, thanks for all you've done.").also { stage++ }
                    12 -> end()
                    13 ->
                        npc(
                            "Their King, Tyras, journeyed out to the West on a",
                            "voyage of discovery. But he was captured by the Dark",
                            "Lord.",
                        ).also { stage++ }

                    14 ->
                        npc(
                            "The Dark Lord agreed to spare his life, but only on",
                            "one condition... That he would drink from the Chalice of",
                            "Eternity.",
                        ).also { stage++ }

                    15 -> player("So what happened?").also { stage++ }
                    16 ->
                        npc(
                            "The chalice corrupted him. He joined forces with the",
                            "Dark Lord, the embodiment of pure evil, banished all",
                            "those years ago...",
                        ).also { stage++ }

                    17 ->
                        npc(
                            "And so I erected this wall, not just to protect my",
                            "people, but to protect all the people of Gielinor.",
                        ).also { stage++ }

                    18 ->
                        npc(
                            "Now, with the King of West Ardougne, the Dark Lord",
                            "has an ally on the inside.",
                        ).also { stage++ }

                    19 ->
                        npc(
                            "So I'm sorry that I lied about the plague. I just hope",
                            "that you can understand my reasons.",
                        ).also { stage++ }

                    20 -> player("Well at least I know now, but what can we do about it?").also { stage++ }
                    21 ->
                        npc(
                            "Nothing at the moment, I'm waiting for my scouts to",
                            "come back. They will tell us how we can get through",
                            "the mountains.",
                        ).also { stage++ }

                    22 -> npc("When this happens, can I count on your support?").also { stage++ }
                    23 -> player("Absolutely!").also { stage++ }
                    24 -> npc("Thank the gods! I give you permission to use", "my training area.").also { stage++ }
                    25 ->
                        npc(
                            "It's located just to the north west of Ardougne, there",
                            "you can prepare for the challenge ahead.",
                        ).also { stage++ }

                    26 ->
                        player(
                            "Ok. There's just one thing I don't understand, how do",
                            "you know so much about King Tyras?",
                        ).also { stage++ }

                    27 -> npc("How could I not do? He was my brother.").also { stage++ }
                    28 -> {
                        end()
                        finishQuest(player!!, Quests.BIOHAZARD)
                    }
                }
            }
        }
    }
}

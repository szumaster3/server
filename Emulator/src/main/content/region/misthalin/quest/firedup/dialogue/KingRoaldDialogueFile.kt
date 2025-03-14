package content.region.misthalin.quest.firedup.dialogue

import core.api.getStatLevel
import core.api.quest.finishQuest
import core.api.quest.setQuestStage
import core.api.quest.updateQuestTab
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.skill.Skills
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Quests

class KingRoaldDialogueFile(
    val questStage: Int,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (questStage) {
            0 -> {
                when (stage) {
                    0 -> player(FaceAnim.HAPPY, "Greetings, your majestry.").also { stage++ }
                    1 -> npc(FaceAnim.NEUTRAL, "Well hello there. What do you want?").also { stage++ }
                    2 -> npc("Ah, it's you again. Hello there.").also { stage++ }
                    3 ->
                        player(
                            "Hello, Your Majesty. I am happy to report that the",
                            "situation at the Temple of Paterdomus has been sorted.",
                            "Misthalin's borders should once again be fully protected",
                            "against the threats of Morytania.",
                        ).also { stage++ }

                    4 -> npc("Excellent, excellent. The Kingdom of Misthalin is in", "you debt.").also { stage++ }
                    5 ->
                        player(
                            "In my debt? Does that mean you're going to give me",
                            "fabulous rewards for my efforts?",
                        ).also { stage++ }

                    6 ->
                        npc(
                            "Of course not; however, if it's rewards you're after, it",
                            "occurs to me that you could be of even more service to",
                            "the kingdom...and this time, there's payment in it for",
                            "you.",
                        ).also { stage++ }

                    7 -> options("Oh, Tell me more!", "Sorry, not interested.").also { stage++ }
                    8 ->
                        when (buttonID) {
                            1 -> player("Oh, Tell me more!").also { stage++ }
                            2 -> player("Sorry, not interested.").also { stage = END_DIALOGUE }
                        }

                    9 ->
                        npc(
                            "Well, you see, because of the mounting threats from",
                            "Morytania and the Wilderness, the southern kingdoms",
                            "have banded together to take action.",
                        ).also { stage++ }

                    10 ->
                        npc(
                            "We have constructed a network of beacons that stretch",
                            "all the way from the source of the River Salve to the",
                            "northwestern-most edge of the Wilderness.",
                        ).also { stage++ }

                    11 ->
                        npc(
                            "Should there be any threat from these uncivilized lands,",
                            "we'll be able to spread the word as fast as the light of",
                            "the flames can travel.",
                        ).also { stage++ }

                    12 -> player("So, how could I be of help?").also { stage++ }
                    13 ->
                        npc(
                            "The task itself should be rather straightforward: I need",
                            "you to help us test the network of beacons to make",
                            "sure everything is in order, in case the worst should",
                            "occur.",
                        ).also { stage++ }

                    14 -> options("I'd be happy to help.", "Actually, I'm a bit busy right now.").also { stage++ }
                    15 ->
                        when (buttonID) {
                            1 -> player("I'd be happy to help.").also { stage++ }
                            2 -> player("I'm a bit busy right now.").also { stage = END_DIALOGUE }
                        }

                    16 ->
                        if (getStatLevel(player!!, Skills.FIREMAKING) < 43) {
                            npc(
                                "I'd love for you to help, but you need",
                                "to get better at lighting fires first.",
                            ).also { stage = END_DIALOGUE }
                        } else {
                            npc("Excellent! The kingdom of Misthalin is eternally", "grateful.").also { stage++ }
                        }

                    17 -> player("So, what do I need to do?").also { stage++ }
                    18 ->
                        npc(
                            "Talk to the head fire tender, Blaze Sharpeye - he'll",
                            "explain everything. He is stationed just south of the",
                            "Temple of Paterdomus, on the cliffs by the River Salve.",
                        ).also { stage++ }

                    19 ->
                        npc(
                            "With speed, ${player!!.username}. The security of Misthalin is in",
                            "your hands.",
                        ).also { stage++ }

                    20 -> {
                        end()
                        setQuestStage(player!!, Quests.ALL_FIRED_UP, 10)
                        updateQuestTab(player!!)
                    }
                }
            }

            90 -> {
                when (stage) {
                    START_DIALOGUE -> player(FaceAnim.HAPPY, "Greetings, your majestry.").also { stage++ }
                    1 -> npc(FaceAnim.NEUTRAL, "Well hello there. What do you want?").also { stage++ }
                    2 -> npc("Ah, it's you again. Hello there.").also { stage++ }
                    3 ->
                        player(
                            FaceAnim.HAPPY,
                            "I'm happy to report that the beacon network seems to",
                            "be working as expected.",
                        ).also { stage++ }

                    4 -> npc("Excellent! I'm delighted to hear it.").also { stage++ }
                    5 -> player("So, about that reward you promised?").also { stage++ }
                    6 ->
                        npc(
                            "What happened to the days when adventurers felt",
                            "rewarded in full by the knowledge of a job well done?",
                        ).also { stage++ }

                    7 -> player("Well before my time, I'm afraid.").also { stage++ }
                    8 ->
                        npc(
                            "Hmph. Well, I suppose a king must stick to his word.",
                            "Mind you, let me stress how grateful we are - and how",
                            "grateful we'd be if you could continue helping us test",
                            "the beacons.",
                        ).also { stage++ }

                    9 ->
                        npc(
                            "There is much more to be done and this is but a",
                            "pittance compared to what I'm willing to offer for",
                            "further assistance!",
                        ).also { stage++ }

                    10 -> {
                        end()
                        finishQuest(player!!, Quests.ALL_FIRED_UP)
                    }
                }
            }

            else -> {
                abandonFile()
            }
        }
    }
}

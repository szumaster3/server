package content.region.kandarin.quest.scorpcatcher.dialogue

import content.region.kandarin.quest.scorpcatcher.ScorpionCatcher
import content.region.kandarin.quest.scorpcatcher.handlers.ScorpionCatcherListeners.Companion.getScorpionLocation
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Quests

class SeersDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questName = Quests.SCORPION_CATCHER
        val questStage = getQuestStage(player!!, questName)
        npc = NPC(NPCs.SEER_388)
        when {
            (questStage in 1..30) -> {
                when (stage) {
                    0 ->
                        if (getAttribute(player!!, ScorpionCatcher.ATTRIBUTE_MIRROR, false)) {
                            npcl(
                                FaceAnim.FRIENDLY,
                                "I can see a scorpion that you seek. It would appear to be near some nasty spiders. I can see two coffins there as well.",
                            ).also {
                                stage =
                                    10
                            }
                        } else {
                            options(
                                "I need to locate some scorpions.",
                                "Your friend Thormac sent me to speak to you.",
                                "I seek knowledge and power!",
                            ).also {
                                stage++
                            }
                        }

                    1 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.NEUTRAL, "I need to locate some scorpions.").also { stage = 13 }
                            2 ->
                                playerl(
                                    FaceAnim.NEUTRAL,
                                    "Your friend Thormac sent me to speak to you.",
                                ).also { stage = 2 }
                            3 -> playerl(FaceAnim.NEUTRAL, "I seek knowledge and power!").also { stage = 15 }
                        }
                    2 -> npcl(FaceAnim.HALF_ASKING, "What does the old fellow want?").also { stage++ }
                    3 -> playerl(FaceAnim.FRIENDLY, "He's lost his valuable lesser Kharid scorpions.").also { stage++ }
                    4 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well you have come to the right place. I am a master of animal detection.",
                        ).also {
                            stage++
                        }
                    5 ->
                        npcl(FaceAnim.NEUTRAL, "Let me look into my looking glass.").also {
                            lock(player!!, 6)
                            lockInteractions(npc!!, 6)
                            getScorpionLocation(player!!)
                            player!!.interfaceManager.close(Component(Components.NPCCHAT1_241))
                        }
                    10 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "The scorpion seems to be going through some crack in the wall. Its gone into some sort of secret room.",
                        ).also {
                            stage++
                        }
                    11 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well see if you can find the scorpion then, and I'll try and get you some information on the others.",
                        ).also {
                            stage++
                        }
                    12 -> {
                        end()
                        removeAttribute(player!!, ScorpionCatcher.ATTRIBUTE_MIRROR)
                        setQuestStage(player!!, Quests.SCORPION_CATCHER, 10)
                    }
                    13 ->
                        npcl(
                            FaceAnim.HALF_ASKING,
                            "Do you need to locate any particular scorpion? Scorpions are a creature somewhat in abundance.",
                        ).also {
                            stage++
                        }
                    14 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I'm looking for some lesser Kharid scorpions. They belong to Thormac the sorcerer.",
                        ).also {
                            stage =
                                4
                        }
                    15 ->
                        npcl(FaceAnim.FRIENDLY, "Knowledge comes from experience, power comes from battleaxes.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
            }

            (questStage == 10) -> {
                when (stage) {
                    0 ->
                        playerl(
                            FaceAnim.NEUTRAL,
                            "Hi, I have retrieved the scorpion from near the spiders.",
                        ).also { stage++ }
                    1 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "Well, I've checked my looking glass. There seems to be",
                            "a kharid scorpion in a little village in the east,",
                            "surrounded by lots of uncivilized-looking warriors. Some",
                            "kind of merchant there seems to have picked it up.",
                        ).also {
                            stage++
                        }
                    2 -> npcl(FaceAnim.NEUTRAL, "That's all I can tell about that scorpion.").also { stage++ }
                    3 -> playerl(FaceAnim.HALF_ASKING, "Any more scorpions?").also { stage++ }
                    4 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "It's good that you should ask. I have information on the last scorpion for you.",
                        ).also {
                            stage++
                        }
                    5 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "It seems to be in some sort of upstairs room. There seems to be some sort of brown clothing lying on a table.",
                        ).also {
                            stage++
                        }
                    6 -> {
                        end()
                        setAttribute(player!!, ScorpionCatcher.ATTRIBUTE_NPC, true)
                    }
                }
            }

            (questStage > 10) -> {
                when (stage) {
                    0 -> options("Many greetings.", "I seek knowledge and power!").also { stage++ }
                    1 ->
                        when (buttonID) {
                            1 -> player("Many greetings.").also { stage = 3 }
                            2 -> player("I seek knowledge and power!").also { stage = 4 }
                        }
                    3 ->
                        npc(
                            "Remember, whenever you set out to do something,",
                            "something else must be done first.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    4 ->
                        npc("Knowledge comes from experience, power", "comes from battleaxes.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
            }
        }
    }
}

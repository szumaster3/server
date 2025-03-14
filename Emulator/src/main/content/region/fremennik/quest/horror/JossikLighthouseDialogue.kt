package content.region.fremennik.quest.horror

import content.region.fremennik.quest.horror.handlers.DagannothBossCutscene
import content.region.fremennik.quest.horror.handlers.DagannothCutscene
import core.api.quest.getQuestStage
import core.api.sendMessage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Jossik dialogue under lighthouse.
 */
class JossikLighthouseDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.JOSSIK_1335)
        when (getQuestStage(player!!, Quests.HORROR_FROM_THE_DEEP)) {
            55 ->
                when (stage) {
                    0 ->
                        npc(
                            FaceAnim.SCARED,
                            "*cough*",
                            "Please...please help me...",
                            "I think my leg is broken and those creatures will be",
                            "back any minute now!",
                        ).also { stage++ }

                    1 ->
                        player(
                            FaceAnim.STRUGGLE,
                            "I guess you're Jossik then...",
                            "What creatures are you talking about?",
                        ).also { stage++ }

                    2 ->
                        npc(
                            FaceAnim.SCARED,
                            "I... I do not know.",
                            "I have never seen their like before!",
                        ).also { stage++ }

                    3 ->
                        npc(
                            FaceAnim.SCARED,
                            "I was searching for information about my uncle Silas,",
                            "who vanished mysteriously from this lighthouse many",
                            "months ago. I found the secret of that strange wall, and",
                            "discovered that I could use it as a door, but when I",
                        ).also { stage++ }

                    4 -> npc(FaceAnim.SCARED, "came down here I was attacked by...").also { stage++ }
                    5 ->
                        npc(
                            FaceAnim.SCARED,
                            "Well, I do not know what they are, but they are very",
                            "strong! They hurt me badly enough to trap me here,",
                            "and I have been fearing for my life ever since!",
                        ).also { stage++ }

                    6 ->
                        player(
                            FaceAnim.FRIENDLY,
                            "Don't worry, I'm here now.",
                            "Larrissa was worried about you and asked for my help.",
                        ).also { stage++ }

                    7 ->
                        player(
                            FaceAnim.FRIENDLY,
                            "I'll go back upstairs and let her know that I've found",
                            "you and that you're still alive, and then we can work",
                            "out some way of getting you out of here, okay?",
                        ).also { stage++ }

                    8 ->
                        npc(
                            FaceAnim.SCARED,
                            "NO! No, you can't leave me now!",
                            "Look! They're coming again! Do something!",
                        ).also { stage++ }

                    9 -> {
                        end()
                        DagannothCutscene(player!!).start()
                    }
                }

            in 56..59 ->
                when (stage) {
                    0 -> {
                        end()
                        sendMessage(player!!, "You are too busy to talk to Jossik.")
                    }
                }

            60 ->
                when (stage) {
                    0 ->
                        player(
                            FaceAnim.FRIENDLY,
                            "Okay, now that the creature's dead we can get you out",
                            "of here.",
                        ).also { stage++ }

                    1 -> npc(FaceAnim.SCARED, "No... you do not understand...").also { stage++ }
                    2 -> npc(FaceAnim.SCARED, "That was not the creature that attacked me...").also { stage++ }
                    3 -> npc(FaceAnim.SCARED, "That was one of its babies...").also { stage++ }
                    4 -> {
                        end()
                        DagannothBossCutscene(player!!).start()
                    }
                }

            in 61..69 ->
                when (stage) {
                    0 -> {
                        end()
                        sendMessage(player!!, "You are too busy to talk to Jossik.")
                    }
                }

            100 ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Okay, it's dead! Let's get out of here!").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Yes, quickly, the mother might be dead, but its children are not!",
                        ).also { stage++ }

                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Follow me upstairs, I might be able to help you with that casket you found.",
                        ).also { stage++ }

                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Bring it to my library, it looks familiar somehow...",
                        ).also { stage++ }

                    4 -> {
                        end()
                    }
                }
        }
    }
}

package content.region.asgarnia.quest.rd.handlers.tests

import content.region.asgarnia.quest.rd.RecruitmentDrive
import content.region.asgarnia.quest.rd.cutscene.FailTest
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs

class WitsTest(
    private val dialogueNum: Int = 0,
) : DialogueBuilderFile() {
    companion object {
        const val ATTRIBUTE_RANDOM_RIDDLE = "rd:randomriddle"
        const val ATTRIBUTE_CORRECT_ANSWER = "rd:recentlycorrect"
    }

    override fun create(b: DialogueBuilder) {
        b
            .onPredicate { player -> true }
            .branch { player ->
                if (getAttribute(player, ATTRIBUTE_CORRECT_ANSWER, false)) {
                    return@branch 3
                } else if (getAttribute(player, RecruitmentDrive.stageFail, false)) {
                    return@branch 2
                } else if (getAttribute(player, RecruitmentDrive.stagePass, false)) {
                    return@branch 1
                } else {
                    return@branch 0
                }
            }.let { branch ->
                val failedStage = b.placeholder()
                failedStage
                    .builder()
                    .npc(
                        FaceAnim.SAD,
                        "No... I am very sorry.",
                        "Apparently you are not up to the challenge.",
                        "I will return you where you came from, better luck in the",
                        "future.",
                    ).endWith { _, player ->
                        removeAttribute(player, ATTRIBUTE_RANDOM_RIDDLE)
                        setAttribute(player, RecruitmentDrive.stagePass, false)
                        setAttribute(player, RecruitmentDrive.stageFail, false)
                        runTask(player, 3) {
                            FailTest(player).start()
                            return@runTask
                        }
                    }

                val passedStage = b.placeholder()
                passedStage
                    .builder()
                    .betweenStage { _, player, _, _ ->
                        removeAttribute(player, ATTRIBUTE_CORRECT_ANSWER)
                        if (!getAttribute(player, RecruitmentDrive.stageFail, false)) {
                            setAttribute(player, RecruitmentDrive.stagePass, true)
                            removeAttribute(player, ATTRIBUTE_RANDOM_RIDDLE)
                        }
                    }.npc(
                        FaceAnim.HAPPY,
                        "Excellent work, @name",
                        "Please step through the portal to meet your next",
                        "challenge.",
                    ).end()

                branch.onValue(3).goto(passedStage)
                branch.onValue(2).goto(failedStage)
                branch
                    .onValue(1)
                    .npc(
                        "You certainly have the wits to be a Temple Knight.",
                        "Pass on through the portal to find your next challenge.",
                    ).end()
                branch
                    .onValue(0)
                    .betweenStage { _, player, _, _ ->
                        if (getAttribute(player, ATTRIBUTE_RANDOM_RIDDLE, 5) == 5) {
                            setAttribute(player, ATTRIBUTE_RANDOM_RIDDLE, (0..4).random())
                        }
                    }.npc("Greetings, @name", "I am here to test your wits with a simple riddle.")
                    .branch { player -> getAttribute(player, ATTRIBUTE_RANDOM_RIDDLE, 0) }
                    .let { branch ->
                        branch
                            .onValue(0)
                            .npc(
                                FaceAnim.THINKING,
                                "Here is my riddle:",
                                "I estimate there to be one million inhabitants in the world",
                                "of @servername, creatures and people both.",
                            ).npc(
                                FaceAnim.THINKING,
                                "What number would you get if you multiply",
                                "the number of fingers on everything's left hand, to the",
                                "nearest million?",
                            ).manualStage { _, player, _, _ ->
                                sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                                    if (value == 5000000) {
                                        setAttribute(player, ATTRIBUTE_CORRECT_ANSWER, true)
                                    } else {
                                        setAttribute(player, RecruitmentDrive.stageFail, true)
                                    }
                                    openDialogue(player, WitsTest(), NPC(NPCs.MS_HYNN_TERPRETT_2289))
                                }
                            }.end()

                        branch
                            .onValue(1)
                            .npc(FaceAnim.THINKING, "Here is my riddle:", "Which of the following statements is true?")
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option("The number of false statements here is one.").goto(failedStage)
                                optionBuilder.option("The number of false statements here is two.").goto(failedStage)
                                optionBuilder.option("The number of false statements here is three.").goto(passedStage)
                                optionBuilder.option("The number of false statements here is four.").goto(failedStage)
                            }

                        branch
                            .onValue(2)
                            .npc(FaceAnim.THINKING, "Here is my riddle:", "I have both a husband and daughter.")
                            .npc(
                                FaceAnim.THINKING,
                                "My husband is four times older than my daughter. ",
                                "In twenty years time, he will be twice as old as my",
                                "daughter.",
                            ).npc(FaceAnim.THINKING, "How old is my daughter now?")
                            .manualStage { _, player, _, _ ->
                                sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                                    if (value == 10) {
                                        setAttribute(player, ATTRIBUTE_CORRECT_ANSWER, true)
                                    } else {
                                        setAttribute(player, RecruitmentDrive.stageFail, true)
                                    }
                                    openDialogue(player, WitsTest(), NPC(NPCs.MS_HYNN_TERPRETT_2289))
                                }
                            }.end()

                        branch
                            .onValue(3)
                            .npc(
                                FaceAnim.THINKING,
                                "Here is my riddle:",
                                "Imagine that you have been captured by an enemy.",
                                "You are to be killed, but in a moment of mercy, the",
                                "enemy has allowed you to pick your own demise.",
                            ).npc(FaceAnim.THINKING, "Your first choice is to be drowned in a lake of acid.")
                            .npc(FaceAnim.THINKING, "Your second choice is to be burned on a fire.")
                            .npc(
                                FaceAnim.THINKING,
                                "Your third choice is to be thrown to a pack of wolves",
                                "that have not been fed in over a month.",
                            ).npc(
                                FaceAnim.THINKING,
                                "Your final choice of fate is to be thrown from the walls",
                                "of a castle, many hundreds of feet high.",
                            ).npc(FaceAnim.THINKING, "Which fate would you be wise to choose?")
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option("The lake of acid.").goto(failedStage)
                                optionBuilder.option("The large fire.").goto(failedStage)
                                optionBuilder.option("The wolves.").goto(passedStage)
                                optionBuilder.option("The castle walls.").goto(failedStage)
                            }

                        branch
                            .onValue(4)
                            .npc(
                                FaceAnim.THINKING,
                                "Here is my riddle:",
                                "I dropped four identical stones, into four identical",
                                "buckets, each containing an identical amount of water.",
                            ).npc(
                                FaceAnim.THINKING,
                                "The first bucket's water was at 32 degrees Fahrenheit,",
                                "the second was at 33 degrees, the third at 34 and the",
                                "fourth was at 35 degrees.",
                            ).npc(
                                FaceAnim.THINKING,
                                "Which bucket's stone dropped to the bottom of the bucket",
                                "last?",
                            ).options()
                            .let { optionBuilder ->
                                optionBuilder.option("Bucket A (32 degrees)").goto(passedStage)
                                optionBuilder.option("Bucket B (33 degrees)").goto(failedStage)
                                optionBuilder.option("Bucket C (34 degrees)").goto(failedStage)
                                optionBuilder.option("Bucket D (35 degrees)").goto(failedStage)
                            }
                    }
            }
    }
}

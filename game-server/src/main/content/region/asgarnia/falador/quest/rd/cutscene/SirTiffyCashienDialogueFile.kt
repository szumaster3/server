package content.region.asgarnia.falador.quest.rd.cutscene

import content.region.asgarnia.falador.quest.rd.RecruitmentDrive
import content.region.asgarnia.falador.quest.rd.plugin.RDUtils
import content.region.asgarnia.falador.quest.rd.plugin.RecruitmentDrivePlugin
import content.region.asgarnia.falador.quest.rd.plugin.RecruitmentDrivePlugin.Companion.initRoomStage
import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import org.rs.consts.NPCs
import org.rs.consts.Quests

class SirTiffyCashienDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onQuestStages(Quests.RECRUITMENT_DRIVE, 1)
            .playerl(FaceAnim.FRIENDLY, "Sir Amik Varze sent me to meet you here for some sort of testing...")
            .npcl(
                FaceAnim.FRIENDLY,
                "Ah, @name! Amik told me all about you, dontchaknow! Spliffing job you you did with the old Black Knights there, absolutely first class.",
            ).playerl(FaceAnim.GUILTY, "...Thanks I think.")
            .npcl(FaceAnim.FRIENDLY, "Well, not in those exact words, but you get my point, what?")
            .npcl(
                FaceAnim.FRIENDLY,
                "A top-notch filly like yourself is just the right sort we've been looking for for our organisation.",
            ).npcl(FaceAnim.FRIENDLY, "So, are you ready to begin testing?")
            .let { path ->
                val originalPath = b.placeholder()
                path.goto(originalPath)
                return@let originalPath
                    .builder()
                    .options()
                    .let { optionBuilder ->
                        val continuePath = b.placeholder()
                        optionBuilder
                            .option("Testing..?")
                            .playerl(FaceAnim.THINKING, "Testing? What exactly do you mean by testing?")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Jolly bad show! Varze was supposed to have informed you about all this before sending you here!",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "Well, not your fault I suppose, what? Anywho, our organisation is looking for a certain specific type of person to join.",
                            ).playerl(
                                FaceAnim.FRIENDLY,
                                "So... You want me to go kill some monster or something for you?",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "Not at all, old bean. There's plenty of warriors around should we require dumb muscle.",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "That's really not the kind of thing our organisation is after, what?",
                            ).playerl(
                                FaceAnim.FRIENDLY,
                                "So you want me to go and fetch you some kind of common item, and then take it for delivery somewhere on the other side of the country?",
                            ).playerl(FaceAnim.SAD, "Because I really hate doing that!")
                            .npcl(FaceAnim.FRIENDLY, "Haw, haw, haw! What a dull thing to ask of someone, what?")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "I know what you mean, though. I did my fair share of running errands when I was a young adventurer, myself!",
                            ).playerl(FaceAnim.FRIENDLY, "So what exactly will this test consist of?")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Can't let just any old riff-raff in, what? The mindless thugs and bully boys are best left in the White Knights or the city guard. We look for the top-shelf brains to join us.",
                            ).playerl(FaceAnim.HALF_ASKING, "So you want to test my brains? Will it hurt?")
                            .npcl(FaceAnim.FRIENDLY, "Haw, haw, haw! That's a good one!")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Not in the slightest.. Well, maybe a bit, but we all have to make sacrifices occasionally, what?",
                            ).playerl(FaceAnim.FRIENDLY, "What do you want me to do then?")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "It's a test of wits, what? I'll take you to our secret training grounds, and you will have to pass through a series of five separate intelligence test to prove you're our sort of adventurer.",
                            ).npcl(FaceAnim.FRIENDLY, "Standard puzzle room rules will apply.")
                            .playerl(FaceAnim.THINKING, "Erm... What are standard puzzle room rules exactly?")
                            .npcl(FaceAnim.HAPPY, "Never done this sort of thing before, what?")
                            .npc(
                                "The simple rules are:",
                                "No items or equipment to be brought with you.",
                                "Each room is a self-contained puzzle.",
                                "You may quit at any time.",
                            ).npcl(
                                FaceAnim.HAPPY,
                                "Of course, if you quit a room, then all your progress up to that point will be cleared, and you'll have to start again from scratch.",
                            ).npc(
                                FaceAnim.HAPPY,
                                "Our organisation manages to filter all the top-notch",
                                "adventurers this way.",
                                "So, are you ready to go?",
                            ).goto(originalPath)
                        optionBuilder
                            .option("Organisation?")
                            .playerl(
                                FaceAnim.FRIENDLY,
                                "This organisation you keep mentioning.. Perhaps you could tell me a little about it?",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "Oh, that Amik! Jolly bad form. Did he not tell you anything that he was supposed to?",
                            ).playerl(
                                FaceAnim.FRIENDLY,
                                "No. He didn't really tell me anything except to come here and meet you.",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "Well, now, old sport, let me give you the heads up and the low down, what?",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "I represent the Temple Knights. We are the premier order of Knights in Asgarnia, if not the world. Saradomin himself personally founded our order centuries ago, and we answer only to him.",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "Only the very best of the best are permitted to join, and the powers we command are formidable indeed.",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "You might say that we are the front line of defence for the entire kingdom!",
                            ).playerl(FaceAnim.THINKING, "So what's the difference between you and the White Knights?")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Well, in simple terms, we're better! Any fool with a sword can manage to get into the White Knights, which is mostly the reason they are so very, very incompetent, what?",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "The Temple Knights, on the other hand, have to be smarter, stronger and better than all others. We are the elite. No man controls us, for our orders come directly from Saradomin himself!",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "According to Sir Vey Lance, our head of operations, that is. He claims that everything he tells us to do is done with Saradomin's implicit permission.",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "It's not every job where you have more authority than the king, though, is it?",
                            ).playerl(FaceAnim.THINKING, "Wait... You can order the King around?")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Well, not me personally. I'm only in the recruitment side of things, dontchaknow, but the higher ranking members of the organisation have almost absolute power over the kingdom.",
                            ).npcl(FaceAnim.NEUTRAL, "Plus a few others, so I hear...")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Anyway, this is why we keep our organisation shrouded in secrecy, and why we demand such rigorous testing for all potential recruits. Speaking of which, are you ready to begin your testing?",
                            ).goto(originalPath)
                        optionBuilder
                            .option("Yes, let's go!")
                            .player(FaceAnim.FRIENDLY, "Yeah. this sounds right up my street.", "Let's go!")
                            .branch { player ->
                                if (player.inventory.isEmpty &&
                                    player.equipment.isEmpty &&
                                    !(player.familiarManager.hasFamiliar() || player.familiarManager.hasPet())
                                ) {
                                    1
                                } else {
                                    0
                                }
                            }.let { branch ->
                                branch
                                    .onValue(0)
                                    .npcl(
                                        FaceAnim.FRIENDLY,
                                        "To start the test you can't have anything in the inventory and equipment.",
                                    ).end()
                                return@let branch
                            }.onValue(1)
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Jolly good show! Now, the training grounds location is a secret, so...",
                            ).goto(continuePath)
                        optionBuilder
                            .option("No, I've changed my mind.")
                            .player("No, I've changed my mind.")
                            .end()

                        return@let continuePath.builder()
                    }
            }.endWith { _, player ->
                if (getQuestStage(player, Quests.RECRUITMENT_DRIVE) == 1) {
                    setQuestStage(player, Quests.RECRUITMENT_DRIVE, 2)
                }
                RDUtils.shuffleTask(player)
                InitTest(player).start()
            }

        b
            .onQuestStages(Quests.RECRUITMENT_DRIVE, 2)
            .npc(FaceAnim.FRIENDLY, "Ah, what ho!", "Back for another go at the old testing, what?")
            .options()
            .let { optionBuilder ->
                val continuePath = b.placeholder()
                optionBuilder
                    .option("Yes, let's go!")
                    .player(FaceAnim.FRIENDLY, "Yeah. this sounds right up my street.", "Let's go!")
                    .branch { player ->
                        if (player.inventory.isEmpty &&
                            player.equipment.isEmpty &&
                            !(player.familiarManager.hasFamiliar() || player.familiarManager.hasPet())
                        ) {
                            1
                        } else {
                            0
                        }
                    }.let { branch ->
                        branch
                            .onValue(0)
                            .npcl(
                                FaceAnim.NEUTRAL,
                                "Well, bad luck, old @g[guy,gal]. You'll need to have a completely empty inventory and you can't be wearing any equipment before we can accurately test you.",
                            ).npcl(
                                FaceAnim.HAPPY,
                                "Don't want people cheating by smuggling stuff in, what? That includes things carried by familiars, too! Come and see me again after you've been to the old bank to drop your stuff off, what?",
                            ).end()
                        return@let branch
                    }.onValue(1)
                    .npc(FaceAnim.FRIENDLY, "Jolly good show!", "Now the training grounds location is a secret, so...")
                    .endWith { _, player ->
                        RDUtils.shuffleTask(player)
                        InitTest(player).start()
                    }
                optionBuilder
                    .option("No, I've changed my mind.")
                    .player("No, I've changed my mind.")
                    .end()
                return@let continuePath.builder()
            }
        b
            .onQuestStages(Quests.RECRUITMENT_DRIVE, 3)
            .npc(
                FaceAnim.HAPPY,
                "Oh, jolly well done!",
                "Your performance will need to be evaluated by Sir Vey",
                "personally, but I don't think it's going too far ahead of",
                "myself to welcome you to the team!",
            ).endWith { _, player ->
                finishQuest(player, Quests.RECRUITMENT_DRIVE)
            }
    }
}

class SirTiffyCashienFailedDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onPredicate { _ -> true }
            .npc(FaceAnim.SAD, "Oh, jolly bad luck, what?", "Not quite the brainbox you thought you were, eh?")
            .npc(
                FaceAnim.HAPPY,
                "Well, never mind!",
                "You have an open invitation to join our organization, so",
                "when you're feeling a little smarter, come back and talk",
                "to me again.",
            )
    }
}

class InitTest(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        val currentStage = getAttribute(player, RecruitmentDrive.stageArray[0], 0)
        setExit(
            RecruitmentDrivePlugin.Companion.Rooms.index[currentStage]!!
                .location,
        )
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                setMinimapState(player, 2)
                timedUpdate(6)
            }

            1 -> {
                dialogueLinesUpdate(NPCs.SIR_TIFFY_CASHIEN_2290, FaceAnim.HAPPY, "Here we go!", "Mind your head!")
                timedUpdate(3)
            }

            2 -> {
                dialogueLinesUpdate(
                    NPCs.SIR_TIFFY_CASHIEN_2290,
                    FaceAnim.HAPPY,
                    "Oops. Ignore the smell!",
                    "Nearly there!",
                )
                timedUpdate(3)
            }

            3 -> {
                dialogueLinesUpdate(
                    NPCs.SIR_TIFFY_CASHIEN_2290,
                    FaceAnim.HAPPY,
                    "And...",
                    "Here we are!",
                    "Best of luck!",
                )
                timedUpdate(3)
            }

            4 -> {
                clearInventory(player)
                endWithoutFade {
                    val currentStage = getAttribute(player, RecruitmentDrive.stageArray[0], 0)
                    val firstStage = RecruitmentDrivePlugin.Companion.Rooms.index[currentStage]!!
                    queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                        when (stage) {
                            0 -> {
                                fadeFromBlack()
                                return@queueScript delayScript(player, 3)
                            }

                            1 -> {
                                forceWalk(player, firstStage.destination, "dumb")
                                return@queueScript delayScript(player, 2)
                            }

                            2 -> {
                                unlock(player)
                                closeDialogue(player)
                                initRoomStage(player, firstStage.npc)
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                }
            }
        }
    }
}

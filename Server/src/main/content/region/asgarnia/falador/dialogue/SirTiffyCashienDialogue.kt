package content.region.asgarnia.falador.dialogue

import content.data.RespawnPoint
import content.region.asgarnia.falador.quest.rd.cutscene.EnterTestingRoomCutscene
import content.region.asgarnia.falador.quest.rd.RDUtils
import core.ServerConstants
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Sir Tiffy Cashien dialogue.
 */
@Initializable
class SirTiffyCashienDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestInProgress(player!!, Quests.RECRUITMENT_DRIVE, 1, 99)) {
            openDialogue(player, SirTiffyCashienDialogueFile(), npc)
            return true
        } else {
            playerl(FaceAnim.HAPPY, "Hello.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc("What ho, " + (if (player.isMale) "sirrah" else "milady") + ". Spiffing day for a walk in the park,", "what?").also { stage++ }
            1 -> player("Spiffing?").also { stage++ }
            2 -> npc("Absolutely, top-hole! Well, can't stay and chat all day,", "dontchaknow! Ta-ta for now!").also { stage++ }
            3 -> if (!isQuestComplete(player, Quests.RECRUITMENT_DRIVE)) {
                playerl(FaceAnim.HALF_GUILTY, "Erm...goodbye.").also { stage = END_DIALOGUE }
            } else {
                options("Do you have any jobs for me yet?", "Can you explain the Gaze of Saradomin to me?", "Can I buy some armours?", "Can I switch respawns please?", "Goodbye.").also {
                    stage = 10
                }
            }
            10 -> when (buttonId) {
                1 -> playerl(FaceAnim.FRIENDLY, "Do you have any jobs for me yet?").also { stage = 11 }
                2 -> playerl(FaceAnim.FRIENDLY, "I don't really understand this 'Gaze of Saradomin' thing... Do you think you could explain what it does for me?").also { stage = 13 }
                3 -> playerl(FaceAnim.FRIENDLY, "Can I buy some armours?").also { stage = 19 }
                4 -> if (player.properties.spawnLocation == ServerConstants.HOME_LOCATION) {
                    playerl(FaceAnim.FRIENDLY, "Hi Tiffy, I was wondering, can I change my respawn point to Falador?").also { stage = 22 }
                } else {
                    playerl(FaceAnim.FRIENDLY, "Can I change my respawn point back to Lumbridge?").also { stage = 21 }
                }
                5 -> playerl(FaceAnim.FRIENDLY, "Goodbye.").also { stage = END_DIALOGUE }
            }

            11 -> npcl(FaceAnim.FRIENDLY, "Sorry dear " + (if (player!!.isMale) "boy" else "gal") + " but we are still in the process of organising. I'm sure that we will have something for you soon, so please feel free to check back later.").also { stage++ }

            12 -> npcl(FaceAnim.FRIENDLY, "Anything else I can do for you in the meantime?").also { stage = 10 }
            13 -> npcl(FaceAnim.FRIENDLY, "Certainly " + (if (player!!.isMale) "sirrah" else "milady") + " As you know, we Temple Knights are personally favoured by Saradomin himself. And when I say personally favoured, I don't mean that some time in the future he's going to buy us all a drink!").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "He watches over us, and, when we die, we're offered the chance to respawn in Falador Castle, ready to get on with our adventures.").also { stage++ }
            15 -> npcl(FaceAnim.FRIENDLY, "Now, this doesn't happen if we die in especially evil places like the Wilderness, but it can be a big help if you're out slaying dragons or whatever.").also { stage++ }
            16 -> npcl(FaceAnim.FRIENDLY, "Our equipment isn't protected any more than usual, but it's a small price to pay to be hale and hearty again, what?").also { stage++ }
            17 -> playerl(FaceAnim.FRIENDLY, "Thank you.").also { stage = 12 }
            18 -> npcl(FaceAnim.FRIENDLY, "Was there something else you wanted to ask good old Tiffy, " + (if (player!!.isMale) "sirrah" else "milady") + "?").also { stage = 10 }
            19 -> npcl(FaceAnim.FRIENDLY, "Of course, dear " + (if (player!!.isMale) "sirrah" else "milady") + ".").also { stage++ }
            20 -> {
                end()
                openNpcShop(player, NPCs.SIR_TIFFY_CASHIEN_2290)
            }
            21 -> npcl(FaceAnim.HALF_GUILTY, "What? You're saying you want to respawn in Lumbridge? Are you sure?").also { stage = 26 }
            22 -> npcl(FaceAnim.FRIENDLY, "Ah, so you'd like to respawn in Falador, the good old homestead! Are you sure?").also { stage++ }
            23 -> options("Yes, I want to respawn in Falador.", "Actually, no thanks. I like my respawn point.").also { stage++ }
            24 -> when (buttonId) {
                1 -> player("Yes, I want to respawn in Falador.").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Actually, no thanks. I like my respawn point.").also { stage = 29 }
            }
            25 -> {
                npcl(FaceAnim.FRIENDLY, "Top-hole, what? Good old Fally is definitely the hot-spot nowadays!")
                player.setRespawnLocation(RespawnPoint.FALADOR)
                stage = END_DIALOGUE
            }
            26 -> options("Yes, I want to respawn in Lumbridge.", "Actually, no thanks. I like my respawn point.").also { stage++ }
            27 -> when (buttonId) {
                1 -> player("Yes, I want to respawn in Lumbridge.").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Actually, no thanks. I like my respawn point.").also { stage = 29 }
            }
            28 -> {
                npcl(FaceAnim.HALF_GUILTY, "Why anyone would want to visit that smelly little swamp village of oiks is quite beyond me, I'm afraid, but the deed is done now.")
                player.setRespawnLocation(RespawnPoint.LUMBRIDGE)
                stage = END_DIALOGUE
            }
            29 -> npcl(FaceAnim.FRIENDLY, " As you wish, what? Ta-ta for now.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_TIFFY_CASHIEN_2290)


    class SirTiffyCashienDialogueFile : DialogueBuilderFile() {
        override fun create(b: DialogueBuilder) {
            b.onQuestStages(Quests.RECRUITMENT_DRIVE, 1)
                .playerl(
                    FaceAnim.FRIENDLY,
                    "Sir Amik Varze sent me to meet you here for some sort of testing..."
                )
                .npcl(
                    FaceAnim.FRIENDLY,
                    "Ah, @name! Amik told me all about you, dontchaknow! Spliffing job you you did with the old Black Knights there, absolutely first class."
                )
                .playerl(FaceAnim.GUILTY, "...Thanks I think.")
                .npcl(FaceAnim.FRIENDLY, "Well, not in those exact words, but you get my point, what?")
                .npcl(
                    FaceAnim.FRIENDLY,
                    "A top-notch filly like yourself is just the right sort we've been looking for for our organisation."
                )
                .npcl(FaceAnim.FRIENDLY, "So, are you ready to begin testing?")
                .let { path ->
                    val originalPath = b.placeholder()
                    path.goto(originalPath)
                    return@let originalPath.builder().options().let { optionBuilder ->
                        val continuePath = b.placeholder()
                        optionBuilder
                            .option("Testing..?")
                            .playerl(FaceAnim.THINKING, "Testing? What exactly do you mean by testing?")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Jolly bad show! Varze was supposed to have informed you about all this before sending you here!"
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Well, not your fault I suppose, what? Anywho, our organisation is looking for a certain specific type of person to join."
                            )
                            .playerl(
                                FaceAnim.FRIENDLY,
                                "So... You want me to go kill some monster or something for you?"
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Not at all, old bean. There's plenty of warriors around should we require dumb muscle."
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "That's really not the kind of thing our organisation is after, what?"
                            )
                            .playerl(
                                FaceAnim.FRIENDLY,
                                "So you want me to go and fetch you some kind of common item, and then take it for delivery somewhere on the other side of the country?"
                            )
                            .playerl(FaceAnim.SAD, "Because I really hate doing that!")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Haw, haw, haw! What a dull thing to ask of someone, what?"
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "I know what you mean, though. I did my fair share of running errands when I was a young adventurer, myself!"
                            )
                            .playerl(FaceAnim.FRIENDLY, "So what exactly will this test consist of?")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Can't let just any old riff-raff in, what? The mindless thugs and bully boys are best left in the White Knights or the city guard. We look for the top-shelf brains to join us."
                            )
                            .playerl(
                                FaceAnim.HALF_ASKING,
                                "So you want to test my brains? Will it hurt?"
                            )
                            .npcl(FaceAnim.FRIENDLY, "Haw, haw, haw! That's a good one!")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Not in the slightest.. Well, maybe a bit, but we all have to make sacrifices occasionally, what?"
                            )
                            .playerl(FaceAnim.FRIENDLY, "What do you want me to do then?")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "It's a test of wits, what? I'll take you to our secret training grounds, and you will have to pass through a series of five separate intelligence test to prove you're our sort of adventurer."
                            )
                            .npcl(FaceAnim.FRIENDLY, "Standard puzzle room rules will apply.")
                            .playerl(
                                FaceAnim.THINKING,
                                "Erm... What are standard puzzle room rules exactly?"
                            )
                            .npcl(FaceAnim.HAPPY, "Never done this sort of thing before, what?")
                            .npc(
                                "The simple rules are:",
                                "No items or equipment to be brought with you.",
                                "Each room is a self-contained puzzle.",
                                "You may quit at any time."
                            )
                            .npcl(
                                FaceAnim.HAPPY,
                                "Of course, if you quit a room, then all your progress up to that point will be cleared, and you'll have to start again from scratch."
                            )
                            .npc(
                                FaceAnim.HAPPY,
                                "Our organisation manages to filter all the top-notch",
                                "adventurers this way.",
                                "So, are you ready to go?"
                            )
                            .goto(originalPath)
                        optionBuilder
                            .option("Organisation?")
                            .playerl(
                                FaceAnim.FRIENDLY,
                                "This organisation you keep mentioning.. Perhaps you could tell me a little about it?"
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Oh, that Amik! Jolly bad form. Did he not tell you anything that he was supposed to?"
                            )
                            .playerl(
                                FaceAnim.FRIENDLY,
                                "No. He didn't really tell me anything except to come here and meet you."
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Well, now, old sport, let me give you the heads up and the low down, what?"
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "I represent the Temple Knights. We are the premier order of Knights in Asgarnia, if not the world. Saradomin himself personally founded our order centuries ago, and we answer only to him."
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Only the very best of the best are permitted to join, and the powers we command are formidable indeed."
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "You might say that we are the front line of defence for the entire kingdom!"
                            )
                            .playerl(
                                FaceAnim.THINKING,
                                "So what's the difference between you and the White Knights?"
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Well, in simple terms, we're better! Any fool with a sword can manage to get into the White Knights, which is mostly the reason they are so very, very incompetent, what?"
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "The Temple Knights, on the other hand, have to be smarter, stronger and better than all others. We are the elite. No man controls us, for our orders come directly from Saradomin himself!"
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "According to Sir Vey Lance, our head of operations, that is. He claims that everything he tells us to do is done with Saradomin's implicit permission."
                            )
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "It's not every job where you have more authority than the king, though, is it?"
                            )
                            .playerl(FaceAnim.THINKING, "Wait... You can order the King around?")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Well, not me personally. I'm only in the recruitment side of things, dontchaknow, but the higher ranking members of the organisation have almost absolute power over the kingdom."
                            )
                            .npcl(FaceAnim.NEUTRAL, "Plus a few others, so I hear...")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Anyway, this is why we keep our organisation shrouded in secrecy, and why we demand such rigorous testing for all potential recruits. Speaking of which, are you ready to begin your testing?"
                            )
                            .goto(originalPath)
                        optionBuilder
                            .option("Yes, let's go!")
                            .player(
                                FaceAnim.FRIENDLY,
                                "Yeah. this sounds right up my street.",
                                "Let's go!"
                            )
                            .branch { player ->
                                if (
                                    player.inventory.isEmpty &&
                                    player.equipment.isEmpty &&
                                    !(player.familiarManager.hasFamiliar() ||
                                            player.familiarManager.hasPet())
                                ) {
                                    1
                                } else {
                                    0
                                }
                            }
                            .let { branch ->
                                branch
                                    .onValue(0)
                                    .npcl(
                                        FaceAnim.FRIENDLY,
                                        "To start the test you can't have anything in the inventory and equipment."
                                    )
                                    .end()
                                return@let branch
                            }
                            .onValue(1)
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Jolly good show! Now, the training grounds location is a secret, so..."
                            )
                            .goto(continuePath)
                        optionBuilder
                            .option("No, I've changed my mind.")
                            .player("No, I've changed my mind.")
                            .end()

                        return@let continuePath.builder()
                    }
                }
                .endWith { _, player ->
                    if (getQuestStage(player, Quests.RECRUITMENT_DRIVE) == 1) {
                        setQuestStage(player, Quests.RECRUITMENT_DRIVE, 2)
                    }
                    RDUtils.shuffleTask(player)
                    EnterTestingRoomCutscene(player).start()
                }

            b.onQuestStages(Quests.RECRUITMENT_DRIVE, 2)
                .npc(FaceAnim.FRIENDLY, "Ah, what ho!", "Back for another go at the old testing, what?")
                .options()
                .let { optionBuilder ->
                    val continuePath = b.placeholder()
                    optionBuilder
                        .option("Yes, let's go!")
                        .player(FaceAnim.FRIENDLY, "Yeah. this sounds right up my street.", "Let's go!")
                        .branch { player ->
                            if (
                                player.inventory.isEmpty &&
                                player.equipment.isEmpty &&
                                !(player.familiarManager.hasFamiliar() ||
                                        player.familiarManager.hasPet())
                            ) {
                                1
                            } else {
                                0
                            }
                        }
                        .let { branch ->
                            branch
                                .onValue(0)
                                .npcl(
                                    FaceAnim.NEUTRAL,
                                    "Well, bad luck, old @g[guy,gal]. You'll need to have a completely empty inventory and you can't be wearing any equipment before we can accurately test you."
                                )
                                .npcl(
                                    FaceAnim.HAPPY,
                                    "Don't want people cheating by smuggling stuff in, what? That includes things carried by familiars, too! Come and see me again after you've been to the old bank to drop your stuff off, what?"
                                )
                                .end()
                            return@let branch
                        }
                        .onValue(1)
                        .npc(
                            FaceAnim.FRIENDLY,
                            "Jolly good show!",
                            "Now the training grounds location is a secret, so..."
                        )
                        .endWith { _, player ->
                            RDUtils.shuffleTask(player)
                            EnterTestingRoomCutscene(player).start()
                        }
                    optionBuilder
                        .option("No, I've changed my mind.")
                        .player("No, I've changed my mind.")
                        .end()
                    return@let continuePath.builder()
                }
            b.onQuestStages(Quests.RECRUITMENT_DRIVE, 3)
                .npc(
                    FaceAnim.HAPPY,
                    "Oh, jolly well done!",
                    "Your performance will need to be evaluated by Sir Vey",
                    "personally, but I don't think it's going too far ahead of",
                    "myself to welcome you to the team!"
                )
                .endWith { _, player -> finishQuest(player, Quests.RECRUITMENT_DRIVE) }
        }
    }

    class SirTiffyCashienFailedDialogueFile : DialogueBuilderFile() {
        override fun create(b: DialogueBuilder) {
            b.onPredicate { _ -> true }
                .npc(
                    FaceAnim.SAD,
                    "Oh, jolly bad luck, what?",
                    "Not quite the brainbox you thought you were, eh?"
                )
                .npc(
                    FaceAnim.HAPPY,
                    "Well, never mind!",
                    "You have an open invitation to join our organization, so",
                    "when you're feeling a little smarter, come back and talk",
                    "to me again."
                )
        }
    }
}

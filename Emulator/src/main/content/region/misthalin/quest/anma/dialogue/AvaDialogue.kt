package content.region.misthalin.quest.anma.dialogue

import content.region.misthalin.quest.anma.AnimalMagnetism
import core.api.getStatLevel
import core.api.inInventory
import core.api.removeItem
import core.api.setVarp
import core.game.container.Container
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class AvaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun newInstance(player: Player): Dialogue {
        return AvaDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.ANIMAL_MAGNETISM)
        if (!quest!!.hasRequirements(player)) {
            player.packetDispatch.sendMessage("She doesn't seem interested in talking to you.")
            return false
        }
        when (quest!!.getStage(player)) {
            0 ->
                npc(
                    "Hello there and welcome to my humble abode. It's sadly",
                    "rather more humble than I'd like, to be honest, although",
                    "perhaps you can help with that?",
                )
            10 ->
                npc(
                    "Do you need a reminder what you are supposed to do?",
                    "I know you must have shiny beads distracting you, but",
                    "it is an important job you are doing for me.",
                )
            20 ->
                npc(
                    "My spiritometric devices show that you have been in",
                    "close contact with ghostly animals. Are we closer to",
                    "success?",
                )
            25 ->
                npc(
                    "Go and talk to the Witch next door. She'd talk the hind",
                    "legs off a donkey but she can select the iron with which",
                    "it is suitable for the chicken to interact.",
                )
            27 ->
                if (inInventory(player, Items.BAR_MAGNET_10489)) {
                    player("I've manufactured the magnet; here it is.")
                    stage = 4
                } else {
                    player("I've talked to the Witch and now I've given her some", "iron bars.")
                }

            28 -> npc("So, what terrible hitch have you encountered now?")
            29, 30 ->
                player(
                    "Well, I tried to hack the trees with my axe, but it just",
                    "bounced off the trunk! It did all seem all-too-convenient",
                    "to work on the first try.",
                )

            31 ->
                if (inInventory(player, Items.UNDEAD_CHICKEN_10487)) {
                    player("I have that undead wood at last. Well, twigs anyway.")
                    stage++
                } else {
                    npc("Come back when you have undead wood...")
                }

            32 -> player("I'd like to look at those research notes now, unless you", "have translated them without me?")
            33 -> {
                if (player.inventory.containsItem(AnimalMagnetism.TRANSLATED_NOTES)) {
                    player("I've translated the notes. See? I'm not just a thuggish", "moron like you seem to think.")
                    stage = 10
                }
                if (player.hasItem(AnimalMagnetism.RESEARCH_NOTES)) {
                    player("I have the notes but haven't translated them yet. Any", "hints?")
                } else {
                    player("I seem to have lost the research notes.")
                    stage = 4
                }
            }

            34 -> {
                if (player.inventory.containsItem(AnimalMagnetism.CONTAINER)) {
                    npc(
                        "Wow, great, now the arrow manufacturer is ready for",
                        "use...there you are! Talk to me if you need more",
                        "information later.",
                    )
                    stage = 20
                }
                if (player.hasItem(AnimalMagnetism.PATTERN)) {
                    player("So what do I do with this pattern again?")
                } else {
                    player("My pattern seems to have vanished from my pack...not", "my fault, of course.")
                    stage = 10
                }
            }

            100 ->
                npc(
                    "I'm busy with my newest research, so can't gossip too",
                    "much. Are you after an upgrade to your device, or a",
                    "new device, or some information, of would you like to",
                    "see my goods for sale?",
                )

            else -> npc("How's the quest going?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            0 ->
                when (stage) {
                    0 -> {
                        player("I would be happy to make your home a better place.")
                        stage++
                    }

                    1 -> {
                        npc(
                            "Yay, I didn't even have to talk about a reward; you're",
                            "more gullible than most adventurers, that's for sure.",
                        )
                        stage++
                    }

                    2 -> {
                        npc(
                            "Don't worry, though; I just need you to help fix this",
                            "vile old bed for me. Then I'll find a suitable reward for",
                            "you.",
                        )
                        stage++
                    }

                    3 -> {
                        player("Great, will I be able to take a nap in it?")
                        stage++
                    }

                    4 -> {
                        npc(
                            "Don't be silly; everyone knows that true warriors don't",
                            "ever sleep...or perform many other bodily functions, for",
                            "that matter. I'll come up with something, though.",
                        )
                        stage++
                    }

                    5 -> {
                        player(
                            "I'm not convinced by just a vague something; can you",
                            "be a slight bit more inspiring in your offer?",
                        )
                        stage++
                    }

                    6 -> {
                        npc(
                            "I'll use one for my bed, then see what I can make",
                            "from the other in the way of a reward. I have some",
                            "ideas involving infinite feathers.",
                        )
                        stage++
                    }

                    7 -> {
                        player("Very well then, I shall await my mystery prize with", "bated breath.")
                        stage++
                    }

                    8 -> {
                        quest!!.start(player)
                        end()
                    }
                }

            10 ->
                when (stage) {
                    0 -> {
                        player("Yes, please.")
                        stage++
                    }

                    1 -> {
                        npc("...And people say we scientists are absent minded...")
                        stage++
                    }

                    2 -> {
                        npc(
                            "You need to fetch 2 undead chickens for me to use in",
                            "my redecoration. It's not rocket science.",
                        )
                        stage++
                    }

                    3 -> {
                        player("Rocket?")
                        stage++
                    }

                    4 -> {
                        npc(
                            "If they existed, they would be like arrows but much,",
                            "much bigger. Considering the mess you rangers make",
                            "with small bits of sticks, we scientists have decided they",
                            "don't exist. Now get chicken hunting.",
                        )
                        stage++
                    }

                    5 -> end()
                }

            20 ->
                when (stage) {
                    0 ->
                        if (player.inventory.containsItem(UNDEAD_CHICKENS)) {
                            player("Here they are.")
                            stage = 2
                        } else {
                            player("I'm still looking for undead chickens...")
                            stage = 1
                        }

                    1 -> end()
                    2 -> {
                        npc("Amazing! Success! I can look forward to some good", "nights' sleep after all.")
                        stage++
                    }

                    3 -> {
                        player("Can I ask exactly how an undead chicken will help you", "sleep?")
                        stage++
                    }

                    4 -> {
                        npc(
                            "Well, I need the feathers to make my bed more",
                            "comfortable. A comfortable bed will help me sleep.",
                            "Obvious, really.",
                        )
                        stage++
                    }

                    5 -> {
                        player(
                            "Obvious, yes, but why on " + settings!!.name + " would you need",
                            "an undead chicken when there are perfectly good live",
                            "chickens just down the road?",
                        )
                        stage++
                    }

                    6 -> {
                        npc(
                            "Well, for a start, undead feathers are much cleaner",
                            "than living ones; no dust mites or anything. Secondly, I",
                            "always think of Ernest when I see a chicken, so my",
                            "nerves can't take killing them.",
                        )
                        stage++
                    }

                    7 -> {
                        player(
                            "Then why do I need a chicken for my reward; we",
                            "already established that I don't use a bed?",
                        )
                        stage++
                    }

                    8 -> {
                        npc(
                            "Seeing as how you ranger types use so many feathers",
                            "in your arrows, I was thinking I could harness an",
                            "undead chicken to make an unending supply of arrow",
                            "flights for you.",
                        )
                        stage++
                    }

                    9 -> {
                        player("Beats chicken slaying or hanging around in fishing", "shops, I suppose. So, what next?")
                        stage++
                    }

                    10 -> {
                        npc(
                            "We'll need a magnet next, one with purely natural",
                            "fields and made from a carefully selected iron bar. A",
                            "firm impact when the iron is parallel to " + settings!!.name + "'s",
                            "field will stabilise this field in the rod.",
                        )
                        stage++
                    }

                    11 -> {
                        npc("Go and talk to the Witch next door.")
                        stage++
                    }

                    12 ->
                        if (player.inventory.remove(UNDEAD_CHICKENS)) {
                            quest!!.setStage(player, 25)
                            end()
                        }
                }

            25 ->
                when (stage) {
                    0 -> {
                        npc(
                            "Despite my extensive studies, her years of experience",
                            "make her better at instinctive magico-mystical",
                            "interaction. Oh well, at least I'm cleverer, prettier and",
                            "will have a better bed.",
                        )
                        stage++
                    }

                    1 -> {
                        player(
                            "Okay, okay, you're great. Yes, I'll go and talk to her",
                            "when you've finished praising yourself.",
                        )
                        stage++
                    }

                    2 -> end()
                }

            27 ->
                when (stage) {
                    0 -> {
                        npc("I'm thrilled for you. What about my magnet?")
                        stage++
                    }

                    1 -> {
                        player("The process is subject to some unexpected delays.")
                        stage++
                    }

                    2 -> {
                        npc(
                            "I can see now why Frenekstrain prefers to work with",
                            "dead folk. It can't be more frustrating than working",
                            "with you.",
                        )
                        stage++
                    }

                    3 -> end()
                    4 -> {
                        npc(
                            "Great stuff! with the Witch's influence within the",
                            "magnet, the undead chicken can use this, I'm sure.",
                        )
                        stage++
                    }

                    5 -> {
                        npc(
                            "The plan is that the chicken will operate the magnet to",
                            "attract bits of iron and steel, maybe even your own",
                            "recently fired arrows. There are plenty of totally lost",
                            "arrowheads lying about in the Fields of " + settings!!.name + ", I",
                        )
                        stage++
                    }

                    6 -> {
                        npc("bet.")
                        stage++
                    }

                    7 -> {
                        npc(
                            "In addition, arrows which you fire should be able to be",
                            "attracted back to your quiver by cunning avian.",
                        )
                        stage++
                    }

                    8 -> {
                        player(
                            "I begin to understand your plan. We've covered",
                            "feathers and arrowheads now; what next?",
                        )
                        stage++
                    }

                    9 -> {
                        npc(
                            "We need a source of wood, but one which is spiritually",
                            "active and can regenerate itself. That will save you",
                            "some axework in the future.",
                        )
                        stage++
                    }

                    10 -> {
                        npc(
                            "Try using a woodcutting axe on the pesky trees in the",
                            "garden here, that ones that attack rather than the really",
                            "dead ones. They are probably just the sort of thing we",
                            "could use.",
                        )
                        stage++
                    }

                    11 -> {
                        player("They will try to kill me, though, and I can't fight back!")
                        stage++
                    }

                    12 -> {
                        npc(
                            "Now you know how those poor guards feel when you",
                            "hide behind mushrooms and fences and attack them",
                            "from afar! Anyway, I reckon you'll need to try a",
                            "mithril or better axe on the trees. At least the trees axe",
                        )
                        stage++
                    }

                    13 -> {
                        npc("pretty close.")
                        stage++
                    }

                    14 ->
                        if (removeItem(player, Items.BAR_MAGNET_10489)) {
                            setVarp(player, 939, 150, true)
                            quest!!.setStage(player, 28)
                            end()
                        }
                }

            28 ->
                when (stage) {
                    0 -> {
                        player(
                            "Nothing really; I just decided to talk to you in case",
                            "you have any great advice for me.",
                        )
                        stage++
                    }

                    1 -> {
                        npc("Nope, sorry.")
                        stage++
                    }

                    2 -> end()
                }

            29, 30 ->
                when (stage) {
                    0 -> {
                        npc(
                            "Fortunately for you, I've done some research and it",
                            "seems to suggest that there are two choices open to",
                            "you.",
                        )
                        stage++
                    }

                    1 -> {
                        player("Tell me the worst.")
                        stage++
                    }

                    2 -> {
                        npc(
                            "The first is more interesting. We cut off your arms,",
                            "have them reanimated as undead, reattach them and",
                            "then you should be able to cut the trees normally.",
                        )
                        stage++
                    }

                    3 -> {
                        npc(
                            "Of course, you won't be able to pick your nose any",
                            "more, so I suppose you'll want to try the second option.",
                        )
                        stage++
                    }

                    4 -> {
                        player(
                            "I'm not exactly addicted to picking my nose, but I do",
                            "think I'll pass on that method.",
                        )
                        stage++
                    }

                    5 -> {
                        npc(
                            "Well, in that case, I think it may have something to do",
                            "with Slayer abilities. After all, I did see Turael poking",
                            "around the trees while I was moving in.",
                        )
                        stage++
                    }

                    6 -> {
                        npc(
                            "As he's not known for his random touristic activities,",
                            "you should try chatting with this Turael. He's the",
                            "Slayer Master near Burthorpe.",
                        )
                        stage++
                    }

                    7 -> {
                        player(
                            "Oh dear, I hope he doesn't want me to buy one of his",
                            "ridiculous fashion accessories. Those earmuffs he sells",
                            "make heroic adventurers into a laughing stock.",
                        )
                        stage++
                    }

                    8 -> {
                        if (quest!!.getStage(player) == 29) {
                            quest!!.setStage(player, 30)
                        }
                        end()
                    }
                }

            31 ->
                when (stage) {
                    0 -> end()
                    1 -> {
                        npc("You certainly took your time.")
                        stage++
                    }

                    2 -> {
                        player(
                            "I'd say they didn't grow on trees, but I guess you'd",
                            "just be sarcastic about my sense of humour.",
                        )
                        stage++
                    }

                    3 -> {
                        npc(
                            "Quite. Now that we have all the ingredients for infinite",
                            "arrows, we just need a container in which we can keep",
                            "the components in the correct mutual alignment.",
                        )
                        stage++
                    }

                    4 -> {
                        npc(
                            "I've gathered together some research notes from various",
                            "sources but I can't quite make out what they mean. If",
                            "you want to have a go at making them out, just ask me",
                            "for a copy.",
                        )
                        stage++
                    }

                    5 -> {
                        if (removeItem(player, Items.UNDEAD_CHICKEN_10487)) {
                            quest!!.setStage(player, 32)
                        }
                        end()
                    }
                }

            32 ->
                when (stage) {
                    0 -> {
                        npc(
                            "They are still stumping me. Here are the notes; I",
                            "really hope your head doesn't explode from reading",
                            "them.",
                        )
                        stage++
                    }

                    1 -> {
                        player("I'd find it slightly inconvenient, I'm sure.")
                        stage++
                    }

                    2 -> {
                        npc(
                            "It wouldn't be all bad as your body would be useful for",
                            "research after death. What I'd be upset about was if",
                            "bits of you landed in my nice new bed.",
                        )
                        stage++
                    }

                    3 -> {
                        player("Your concern is touching.")
                        stage++
                    }

                    4 -> {
                        quest!!.setStage(player, 33)
                        player.inventory.add(AnimalMagnetism.RESEARCH_NOTES)
                        end()
                    }
                }

            33 ->
                when (stage) {
                    0 -> {
                        npc(
                            "I know you have the notes; I gave them to you, in case",
                            "you forgot! Furthermore, if I had hints, I'd have",
                            "translated those notes myself.",
                        )
                        stage++
                    }

                    1 -> {
                        npc(
                            "So, take the hint and go off and translate them. If it's",
                            "too hard, you can always go and shoot demons in",
                            "cages.",
                        )
                        stage++
                    }

                    2 -> end()
                    4 -> {
                        if (!player.inventory.hasSpaceFor(AnimalMagnetism.RESEARCH_NOTES)) {
                            player("Sorry, I don't have enough inventory space.")
                            stage++
                            return true
                        }
                        player.inventory.add(AnimalMagnetism.RESEARCH_NOTES)
                        npc(
                            "Don't tell me, your cat ate them? You won't get out of",
                            "the job that easily; here are some copies I made.",
                        )
                        stage++
                    }

                    5 -> end()
                    10 -> {
                        npc(
                            "For all I know, it was pure luck, so don't jump to any",
                            "conclusions about your mighty intellect.",
                        )
                        stage++
                    }

                    11 -> {
                        player("I can see why you don't have any assistants, you're", "not exactly easy to work with.")
                        stage++
                    }

                    12 -> {
                        npc(
                            "Let's get back to the work we're doing, then.",
                            "Remember, this is all a favour to you. I could have just",
                            "decided to fob you off with a feather duster.",
                        )
                        stage++
                    }

                    13 -> {
                        npc(
                            "I've given you a pattern for the container; you'll need",
                            "to combine them with some polished buttons and hard",
                            "leather. Then we're almost done. Good news, eh?",
                        )
                        stage++
                    }

                    14 -> {
                        npc(
                            "If you are having trouble finding buttons, I've heard",
                            "rumours that the H.A.M society carry this sort of stuff",
                            "more than most.",
                        )
                        stage++
                    }

                    15 -> {
                        player("Really? How would you know this strange detail?")
                        stage++
                    }

                    16 -> {
                        npc(
                            "I hear they lose their clothes a lot to thieves so they",
                            "have to make do with shoddy goods. Whatever the",
                            "reason, they seem to carry buttons about in their pockets.",
                        )
                        stage++
                    }

                    17 ->
                        if (player.inventory.remove(AnimalMagnetism.TRANSLATED_NOTES)) {
                            player.inventory.add(AnimalMagnetism.PATTERN)
                            quest!!.setStage(player, 34)
                            end()
                        }
                }

            34 ->
                when (stage) {
                    0 -> {
                        npc(
                            "Your short-term memory loss worries me. Combine the",
                            "pattern with hard leather and some polished buttons,",
                            "then hand the resulting container to me.",
                        )
                        stage++
                    }

                    1 -> end()
                    10 -> {
                        npc(
                            "You're pretty careless, aren't you. I assume you're the",
                            "type who leaves a trail of arrows and knives behind",
                            "behind them when they train?",
                        )
                        stage++
                    }

                    11 -> {
                        if (!player.inventory.hasSpaceFor(AnimalMagnetism.PATTERN)) {
                            player("Sorry, I don't have enough room in my backpack.")
                            stage++
                        }
                        player.inventory.add(AnimalMagnetism.PATTERN)
                        npc("Here's a replacement; perhaps if I charged for them,", "you'd be more careful.")
                        stage++
                    }

                    12 -> end()
                    20 ->
                        if (player.inventory.remove(AnimalMagnetism.CONTAINER)) {
                            quest!!.finish(player)
                            end()
                        }
                }

            100 ->
                when (stage) {
                    0 -> {
                        options(
                            "I'd like information, please.",
                            "I seem to need a new device.",
                            "I'd like to upgrade my device, please.",
                            "I'd like to see your stuff for sale, please.",
                            "I'll just head off, I think.",
                        )
                        stage++
                    }

                    1 ->
                        when (buttonId) {
                            1 -> {
                                player("I'd like information, please.")
                                stage = 20
                            }

                            2 -> {
                                player("I seem to need a new device.")
                                stage = 10
                            }

                            3 -> {
                                player("I'd like to upgrade my device, please.")
                                stage = 17
                            }

                            4 -> {
                                player("I'd like to see your stuff for sale, please.")
                                stage = 30
                            }

                            5 -> {
                                player("I'll just head off, I think.")
                                stage = 40
                            }
                        }
                    10 -> player("I need another arrow-attracting device, please").also { stage++ }
                    11 ->
                        npc(
                            "Well, luckily for you, they have a habit of returning to",
                            "me when people lose them, So I have some spares. They",
                            "are homing chickens, it seems.",
                        ).also {
                            stage++
                        }
                    12 ->
                        npc(
                            "I'l: need 999gp, however, for the expenses involved in",
                            "restoring the poor creature to health, or unhealth, or",
                            "whatever.",
                        ).also {
                            stage++
                        }
                    13 -> player(FaceAnim.HALF_ASKING, "Why 999?").also { stage++ }
                    14 ->
                        npc(
                            "Well, it just sounds less expensive than 1000. Do you",
                            "want that replacement or not?",
                        ).also { stage++ }
                    15 -> options("Sounds good to me.", "I'd prefer not to, actually.").also { stage++ }
                    16 ->
                        when (buttonId) {
                            1 -> player(FaceAnim.HAPPY, "Sounds good to me.").also { buy(upgrade = false) }
                            2 -> player("I'd prefer not to, actually.").also { stage = 42 }
                        }
                    17 -> {
                        if (!inInventory(player, Items.AVAS_ATTRACTOR_10498) &&
                            getStatLevel(player, Skills.RANGE) > 50
                        ) {
                            npc("You don't have a device in your bags that I can", "upgrade, I'm afraid.").also {
                                stage = END_DIALOGUE
                            }
                        } else if (getStatLevel(player, Skills.RANGE) < 50) {
                            npc(
                                "I'm afraid you aren't yet skilled enough for the",
                                "upgraded version. You need a Range level of 50 or",
                                "greater.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                        } else {
                            npc(
                                FaceAnim.HAPPY,
                                "You are ready to upgrade. I'll take 75 steel arrows and",
                                "the old device, if that's all fine with you?",
                            ).also {
                                stage++
                            }
                        }
                    }

                    18 -> options("Sounds good to me.", "I'd prefer not to, actually.").also { stage++ }
                    19 ->
                        when (buttonId) {
                            1 -> player(FaceAnim.HAPPY, "Sounds good to me.").also { buy(upgrade = true) }
                            2 -> player("I'd prefer not to, actually.").also { stage = 42 }
                        }

                    20 -> {
                        npc("Just a few bits of information before you run away to", "persecute rock crabs or cows.")
                        stage++
                    }

                    21 -> {
                        npc("First, your new device won't work if you are wearing", "certain metallic chest armours.")
                        stage++
                    }

                    22 -> {
                        npc(
                            "The magnetic attraction required for operation would",
                            "cause feedback, so the device does not allow such",
                            "incompatible item equippage.",
                        )
                        stage++
                    }

                    23 -> {
                        npc(
                            "Secondly, although the device is calibrated to attract",
                            "only arrow heads, there is a chance that other",
                            "magnetically active items will be attracted.",
                        )
                        stage++
                    }

                    24 -> {
                        npc(
                            "The arrow recovery function of the devices is slightly",
                            "different and relies upon a harmonic bond between",
                            "your arrows and the chicken's magnet. You'll thus only",
                            "recover arrows which you have fired rather than those",
                        )
                        stage++
                    }

                    25 -> {
                        npc("fired by nearby folks.")
                        stage++
                    }

                    26 -> {
                        npc(
                            "Thirdly, there is an upgraded version available when",
                            "your Ranged level is 50 or greater.",
                        )
                        stage++
                    }

                    27 -> {
                        npc(
                            "You'll also need to move about if you want to collect",
                            "arrows since the gathering of long-lost arrowheads can't",
                            "work otherwise.",
                        )
                        stage++
                    }

                    28 -> {
                        npc(
                            "Finally, the device will only work when worn. It",
                            "automatically deactivates in other circumstances.",
                        )
                        stage++
                    }

                    29 -> {
                        npc(
                            "I was worried about what would happen if you were to",
                            "place it in a bank in its active state, so I've made sure",
                            "it only works when it's on your back.",
                        )
                        stage = 41
                    }

                    30 -> {
                        npc("Here's my shop.")
                        stage++
                    }

                    31 -> {
                        end()
                        npc.openShop(player)
                    }

                    40 -> {
                        npc("Thanks for the help with my bed; see you again.")
                        stage++
                    }

                    41 -> end()

                    42 -> npc("I've better things to do than be irritated by you.").also { stage = END_DIALOGUE }
                }

            else ->
                when (stage) {
                    0 -> {
                        player("It's going...")
                        stage++
                    }

                    1 -> end()
                }
        }
        return true
    }

    private fun buy(upgrade: Boolean) {
        val item = if (upgrade) AnimalMagnetism.AVAS_ACCUMULATOR else AnimalMagnetism.AVAS_ATTRACTOR
        if (!player.inventory.hasSpaceFor(item)) {
            end()
            player("Sorry, I don't have enough inventory space.")
            stage++
            return
        }
        val coins = Item(995, 999)
        if (upgrade) {
            if (!player.inventory.contains(886, 75)) {
                npcl(FaceAnim.HALF_GUILTY, "I need 75 steel arrows for the upgrade process, I'm afraid.")
                stage++
                return
            }
        }
        if (!player.inventory.containsItem(coins)) {
            end()
            npcl(
                FaceAnim.HALF_GUILTY,
                "You seem not to have enough cash; you could always sell some of your gear, though.",
            )
            return
        }
        if (upgrade) {
            player.inventory.remove(Item(886, 75))
        }
        end()
        removeAll(player, item, if (upgrade) AnimalMagnetism.AVAS_ATTRACTOR else AnimalMagnetism.AVAS_ACCUMULATOR)
        player.inventory.remove(coins)
        npc(
            FaceAnim.HAPPY,
            if (upgrade) "Here's your upgraded device; take good care of it." else "Here's your device; take good care of your chicken.",
        )
        stage++
    }

    private fun removeAll(
        player: Player,
        add: Item,
        remove: Item,
    ) {
        val containers: MutableList<Container> = ArrayList(20)
        containers.add(player.inventory)
        containers.add(player.equipment)
        containers.add(player.bank)
        var replace = false
        for (c in containers) {
            if (c.containsItem(remove)) {
                c.replace(add, c.getSlot(remove))
                replace = true
                break
            }
        }
        if (!replace) {
            player.inventory.add(add)
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AVA_5198, NPCs.AVA_5199)
    }

    companion object {
        private val UNDEAD_CHICKENS = Item(Items.UNDEAD_CHICKEN_10487, 2)
    }
}

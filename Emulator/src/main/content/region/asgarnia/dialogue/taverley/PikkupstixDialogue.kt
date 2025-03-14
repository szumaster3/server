package content.region.asgarnia.dialogue.taverley

import core.api.*
import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class PikkupstixDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.WOLF_WHISTLE)
        when (quest!!.getStage(player)) {
            0 -> npc("You there! What are you doing here, as if I didn't have", "enough troubles?")
            10 -> {
                npc("Have you met the monster upstairs?")
                stage = 0
            }

            20 -> {
                player("The teeth!")
                stage = 0
            }

            30 -> {
                if (!player.inventory.containsItem(BONES)) {
                    npc("You need to bring two lots of wolf bones. I can provide", "the other items you will need.")
                    stage = 0
                } else {
                    player("I have the wolf bones right here.")
                    stage = 1
                }
            }

            40 -> {
                if (player.inventory.containsItem(WOLF_POUCH) && player.inventory.containsItem(HOWL_SCROLL)) {
                    player("Here is the pouch and scroll.")
                    stage = 1
                    return true
                }
                if (!player.inventory.contains(12528, 1) &&
                    !player.bank.contains(
                        12528,
                        1,
                    ) &&
                    !player.getAttribute("has-key", false)
                ) {
                    player.inventory.add(Item(12528, 1), player)
                }
                if (!player.inventory.containsItem(WOLF_POUCH) || !player.inventory.containsItem(HOWL_SCROLL)) {
                    npc("You need to bring me a wolf pouch and a howl scroll.")
                    stage = 0
                }
            }

            50 -> {
                if (!player.inventory.containsItem(WOLF_POUCH) || !player.inventory.containsItem(HOWL_SCROLL)) {
                    for (i in WOLF_ITEMS) {
                        if (i.id == 12183) {
                            continue
                        }
                        if (!player.inventory.containsItem(i) &&
                            !player.bank.containsItem(i) &&
                            player.getAttribute<Any?>(
                                "taken-summoning-supplies",
                            ) == null
                        ) {
                            if (i.id == 12528 && getVarp(player, 1178) == (2 shl 11)) {
                                continue
                            }
                            setAttribute(player, "taken-summoning-supplies", true)
                            player.inventory.add(i, player)
                        }
                    }
                }
                npc("Hurry up! Go remove the wolpertinger at last!")
            }

            60 ->
                if (player.getSkills().getLevel(Skills.SUMMONING) <
                    player
                        .getSkills()
                        .getStaticLevel(Skills.SUMMONING)
                ) {
                    npc("So, how did it go?")
                } else if (player.getSkills().getLevel(Skills.SUMMONING) >=
                    player
                        .getSkills()
                        .getStaticLevel(Skills.SUMMONING)
                ) {
                    npc("Feeling refreshed?")
                    stage = 20
                }

            100 -> npc("Welcome to my humble abode. How can I help", "you?")
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
                        player("I'm just a passing adventurer. Who are you?")
                        stage = 1
                    }

                    1 -> {
                        npc(
                            "Who am I? My dear boy, I just so happen to be one",
                            "of the most important druids in this village!",
                        )
                        stage = 2
                    }

                    2 -> {
                        player("Wow, I don't meet that many celebrities.")
                        stage = 3
                    }

                    3 -> {
                        player("What do you do here?")
                        stage = 4
                    }

                    4 -> {
                        npc("Well, firstly, I-")
                        stage = 5
                    }

                    5 -> {
                        interpreter.sendDialogue(
                            "There is a loud crash from upstairs, followed by the tinkling of",
                            "broken glass.",
                        )
                        stage = 6
                    }

                    6 -> {
                        interpreter.sendDialogues(ids[0], FaceAnim.FURIOUS, "Confound you, lapine menace!")
                        stage = 7
                    }

                    7 -> {
                        player("What was that?")
                        stage = 8
                    }

                    8 -> {
                        npc(
                            "That was the sound of my carefully arranged room",
                            "being destroyed by a fluffy typhoon of wickedness!",
                        )
                        stage = 9
                    }

                    9 -> {
                        player("What?")
                        stage = 10
                    }

                    10 -> {
                        player("Wait, I'm good with monsters. Do you want me to go", "kill it for you?")
                        stage = 11
                    }

                    11 -> {
                        npc("Well, yes, but it's a little more complicated than that.")
                        stage = 12
                    }

                    12 -> {
                        player("How complicated can it be?")
                        stage = 13
                    }

                    13 -> {
                        sendDialogue(player, "A resounding crash comes from upstairs.")
                        stage = 14
                    }

                    14 -> {
                        npc("Complicated enough for me to waste time explaining it", "to you.")
                        stage = 15
                    }

                    15 -> {
                        npc("Tell me, have you ever heard of Summoning?")
                        stage = 16
                    }

                    16 -> {
                        player("Not really.")
                        stage = 17
                    }

                    17 -> {
                        npc(
                            "Well, some of the concepts I am about to discuss might",
                            "go over your head, so I'll stick to the basics.",
                        )
                        stage = 18
                    }

                    18 -> {
                        interpreter.sendDialogue("There is a series of loud crunches from upstairs.")
                        stage = 19
                    }

                    19 -> {
                        interpreter.sendDialogues(ids[0], FaceAnim.FURIOUS, "Gah! That better not be my wardrobe!")
                        stage = 20
                    }

                    20 -> {
                        npc(
                            "Summoners are able to call upon animal familiars for a",
                            "number of uses. These familiars can help the summoner",
                            "practice their skills, fight on the battlefield, or offer any",
                            "number of other benefits.",
                        )
                        stage = 21
                    }

                    21 -> {
                        npc(
                            "Beneath this house is a monument - an obelisk - that",
                            "allows us druids to trap this Summoning power.",
                        )
                        stage = 22
                    }

                    22 -> {
                        interpreter.sendDialogue("A loud gnawing begins upstairs.")
                        stage = 23
                    }

                    23 -> {
                        npc("I chased it upstairs, but I didn't have the necessary", "Summoning pouch to banish it.")
                        stage = 24
                    }

                    24 -> {
                        npc(
                            "I sent my assistant, Stikklebrix, out to bring me what I",
                            "needed, but he was not returned.",
                        )
                        stage = 25
                    }

                    25 -> {
                        npc(
                            "If I leave this house, it will come down here, potentially",
                            "even getting to the obelisk. If that happens, it could call",
                            "another familiar through and multiply.",
                        )
                        stage = 26
                    }

                    26 -> {
                        interpreter.sendDialogue("The gnawing stops. There is a crash.")
                        stage = 27
                    }

                    27 -> {
                        player("How fast can it multiply?")
                        stage = 28
                    }

                    28 -> {
                        npc("Like a rabbit, because it is-")
                        stage = 29
                    }

                    29 -> {
                        player("A rabbit!")
                        stage = 30
                    }

                    30 -> {
                        npc("Well yes, rabbit-like, but-")
                        stage = 31
                    }

                    31 -> {
                        player("Oh, don't worry, I'll deal with this myself. One rabbit", "stew coming right up.")
                        stage = 32
                    }

                    32 -> {
                        interpreter.sendDialogues(
                            ids[0],
                            FaceAnim.FURIOUS,
                            "Very well, young fool. You go see how well you do",
                            "against it.",
                        )
                        stage = 33
                    }

                    33 -> {
                        end()
                        quest!!.start(player)
                    }
                }

            10 ->
                when (stage) {
                    0 -> {
                        player("No, not yet.")
                        stage = 1
                    }

                    1 -> {
                        npc("What are you waiting for? I need to get", "that monster out of my room.")
                        stage = 2
                    }

                    2 -> {
                        player("Sorry, I'll go right now.")
                        stage = 3
                    }

                    3 -> end()
                }

            20 ->
                when (stage) {
                    0 -> {
                        npc(
                            "So, you finally saw what you're against, eh? Not as",
                            "harmless as you assumed, I take it?",
                        )
                        stage = 1
                    }

                    1 -> {
                        player("Horns! Teeth!")
                        stage = 2
                    }

                    2 -> {
                        npc("Are you prepared to treat this situation with the gravity", "it deserves, now?")
                        stage = 3
                    }

                    3 -> {
                        player("What IS it?")
                        stage = 4
                    }

                    4 -> {
                        npc("A wolpertinger, and a pretty big one at that.")
                        stage = 5
                    }

                    5 -> {
                        npc(
                            "They are spirits that tend to be on the energetic and",
                            "destructive side when they manifest here, but are a little",
                            "less violent on the spiritual plane.",
                        )
                        stage = 6
                    }

                    6 -> {
                        player("So, what can be done about it? Can't you banish it?")
                        stage = 7
                    }

                    7 -> {
                        npc("Well, I could, but in order to do so, I'd need a spirit", "wolf pouch.")
                        stage = 8
                    }

                    8 -> {
                        npc(
                            "Wolpertingers are generally afraid of wolves; that's the",
                            "rabbity-side of them. A spirit wolf will scare it and banish",
                            "it.",
                        )
                        stage = 9
                    }

                    9 -> {
                        npc(
                            "The problem is that I don't have any of the necessary",
                            "spirit wolf pouches, and the only thing keeping the giant",
                            "wolpertingers upstairs is my presence. If I were to leave,",
                            "it would amble downstairs to bring more of its kind.",
                        )
                        stage = 10
                    }

                    10 -> {
                        npc("thought, as I said before.")
                        stage = 11
                    }

                    11 -> {
                        player("Well, what if I were to bring you the elements you", "needed? Would that work?")
                        stage = 12
                    }

                    12 -> {
                        npc(
                            "Not really; I would need to go down to the obelisk in",
                            "my cellar and use the necessary ingredients to make a",
                            "spirit wolf pouch and some Howl scrolls, but it may slip",
                            "out of the front door.",
                        )
                        stage = 13
                    }

                    13 -> {
                        npc(
                            "You could infuse the pouch yourself, though. Would",
                            "you like to learn the secrets of Summoning? I can",
                            "sense the spark within you, urging you to master the",
                            "art.",
                        )
                        stage = 14
                    }

                    14 -> {
                        player("Certainly!")
                        stage = 15
                    }

                    15 -> {
                        player("What do you need me to bring?")
                        stage = 16
                    }

                    16 -> {
                        npc("That's wonderful!")
                        stage = 17
                    }

                    17 -> {
                        npc("You need to bring two lots of wolf bones. I can provide", "the other items you will need.")
                        stage = 18
                    }

                    18 -> {
                        player("I'll get right on it.")
                        stage = 19
                    }

                    19 -> {
                        end()
                        quest!!.setStage(player, 30)
                    }
                }

            30 ->
                when (stage) {
                    0 -> end()
                    1 -> {
                        npc("Splendid, dear boy.")
                        stage = 2
                    }

                    2 -> {
                        quest!!.setStage(player, 40)
                        for (i in WOLF_ITEMS) {
                            player.inventory.add(i, player)
                        }
                        npc(
                            "Here are the pouches, spirit shards and charms you will",
                            "need to make the spirit wolf pouch and Howl scrolls.",
                            "This key is to the trapdoor over there, which leads to",
                            "the obelisk.",
                        )
                        stage = 20
                    }
                }

            40 ->
                when (stage) {
                    0 -> end()
                    1 -> {
                        npc(
                            "Wonderful! Now, all you have to do is go upstairs and",
                            "summon the spirit wolf, then the Howl effect.",
                        )
                        stage = 2
                    }

                    2 -> {
                        player("That's it?")
                        stage = 3
                    }

                    3 -> {
                        npc(
                            "It's that simple. The spirit wolf will, when you use the",
                            "Howl scroll, chase away the giant wolpertinger and then",
                            "disappear. Under normal circumstances, the spirit wolf",
                            "would follow you around, defend you in combat and",
                        )
                        stage = 4
                    }

                    4 -> {
                        npc("often lend you its powers.")
                        stage = 5
                    }

                    5 -> {
                        player("What sort of powers?")
                        stage = 6
                    }

                    6 -> {
                        npc(
                            "Well, for a start, it has the ability to perform a",
                            "Summoning scroll that forces the opponents to flee. That is",
                            "what it will use on the giant wolpertinger, with the",
                            "difference being that the giant wolpertinger will be so",
                        )
                        stage = 7
                    }

                    7 -> {
                        npc("scared that it will retreat to its spiritual plane, rather", "than face the spirit wolf.")
                        stage = 8
                    }

                    8 -> {
                        player("Great! So, how will I make it perform that Summoning", "scroll?")
                        stage = 9
                    }

                    9 -> {
                        npc(
                            "All you need to do is use the special move button in",
                            "your Summoning interface, and aim it at the giant",
                            "wolpertinger, once the spirit wolf has been summoned.",
                            "The spirit wolf will then perform its special ability.",
                        )
                        stage = 10
                    }

                    10 -> {
                        player("What a relief!")
                        stage = 11
                    }

                    11 -> {
                        npc("Well, I think that I have talked enough. Now you have", "to put it into practice.")
                        stage = 12
                    }

                    12 -> {
                        player("Wish me luck!")
                        stage = 13
                    }

                    13 -> {
                        quest!!.setStage(player, 50)
                        end()
                    }

                    20 -> {
                        player("What are these things?")
                        stage = 21
                    }

                    21 -> {
                        npc(
                            "Well, the wolf bones, spirit shards and gold charms are",
                            "all ingredients to go into the pouch.",
                        )
                        stage = 22
                    }

                    22 -> {
                        npc(
                            "The wolf bones give a solidity to the spirit, the charm",
                            "attracts the type of familiar that you desire - in this",
                            "case a gold charm - and that shards are the spirit's",
                            "focus.",
                        )
                        stage = 23
                    }

                    23 -> {
                        npc(
                            "You will first need to make two Summoning pouches:",
                            "one that can be used to summon the spirit wolf, and",
                            "another to tear open at the obelisk, creating some Howl",
                            "scrolls. These Howl scrolls will help make the spirit wolf",
                        )
                        stage = 24
                    }

                    24 -> {
                        npc("perform a fear-inducing Howl special move.")
                        stage = 25
                    }

                    25 -> {
                        player("Okay, I think I understood that. So, how do I get the", "obelisk to work?")
                        stage = 26
                    }

                    26 -> {
                        npc(
                            "Well, you stand before the obelisk with the charms,",
                            "pouches, shards and bones necessary to complete two",
                            "Summoning pouches. You should then 'use' your empty",
                            "pouch on the obelisk. Your ingredients will be added to",
                        )
                        stage = 27
                    }

                    27 -> {
                        npc("the pouch, mixing with the spirits of a familiar to create a", "spirit wolf pouch.")
                        stage = 28
                    }

                    28 -> {
                        npc(
                            "Creating a scroll is a similar process. By using a",
                            "Summoning pouch on an obelisk and breaking it open,",
                            "you are allowing the spirit energy to transform into a",
                            "different form - some scrolls.",
                        )
                        stage = 29
                    }

                    29 -> {
                        npc(
                            "Once you have a spirit wolf pouch and some Howl",
                            "scrolls, you can come upstairs to see me, to find out",
                            "what must be done with the giant wolpertinger.",
                        )
                        stage = 30
                    }

                    30 -> {
                        player("It all sounds simple enough.")
                        stage = 31
                    }

                    31 -> end()
                }

            50 -> end()
            60 ->
                when (stage) {
                    0 -> {
                        player("The spirit wolf scared it away!")
                        stage = 1
                    }

                    1 -> {
                        npc("My dear boy, you've done it!")
                        stage = 2
                    }

                    2 -> {
                        npc("I just hope the damage to my room isn't too bad.")
                        stage = 3
                    }

                    3 -> {
                        player("All in a day's...")
                        stage = 4
                    }

                    4 -> {
                        player("Woah, I feel a little dizzy.")
                        stage = 5
                    }

                    5 -> {
                        npc("Well, that Summoning was a drain on your rather", "untrained reserves.")
                        stage = 6
                    }

                    6 -> {
                        npc("Go downstairs to the obelisk, as it will renew your", "energy.")
                        stage = 7
                    }

                    7 -> {
                        npc(
                            "You should do that immediately - you'll be cut off from",
                            "Summoning while you have no Summoning skill points",
                            "remaining.",
                        )
                        stage = 8
                    }

                    8 -> {
                        player("Woooooah...")
                        stage = 9
                    }

                    9 -> {
                        npc(
                            "You really need to renew your Summoning skill points",
                            "at the obelisk, " + player.username + ".",
                        )
                        stage = 10
                    }

                    10 -> end()
                    20 -> {
                        player("Yeah, that does feel a lot better!")
                        stage = 21
                    }

                    21 -> {
                        player("Won't my energy renew on its own?")
                        stage = 22
                    }

                    22 -> {
                        npc(
                            "Not really. Your Summoning skill points need to be",
                            "renewed at the obelisks or mini obelisks scattered",
                            "around the world, while your special move bar will",
                            "recharge slowly over time.",
                        )
                        stage = 23
                    }

                    23 -> {
                        npc(
                            "The special move bar is like your special attack bar, not",
                            "only because it is useful when attacking, but because it",
                            "recharges slowly overtime.",
                        )
                        stage = 24
                    }

                    24 -> {
                        npc("To help you on your way, I can tell you that I know", "of two other obelisks you can use.")
                        stage = 25
                    }

                    25 -> {
                        npc(
                            "One is guarded by the ogres in Gu'Tanoth, and the",
                            "other is on the coast near Brimhaven. There will be",
                            "others, however.",
                        )
                        stage = 26
                    }

                    26 -> {
                        player("Thanks for the information.")
                        stage = 27
                    }

                    27 -> {
                        npc("My dear boy, it was nothing.")
                        stage = 28
                    }

                    28 -> {
                        npc(
                            "Well, from your actions, you have proved yourself",
                            "worthy to learn the secrets of Summoning.",
                        )
                        stage = 29
                    }

                    29 -> {
                        npc(
                            "Speak to me on the subject if you wish to learn more",
                            "about it. In the meantime, I will use my power to grant",
                            "you access to the skill.",
                        )
                        stage = 30
                    }

                    30 -> {
                        npc("Here is a little something to help you start your", "Summoning training.")
                        stage = 31
                    }

                    31 -> {
                        player("Thanks!")
                        stage = 32
                    }

                    32 -> {
                        end()
                        quest!!.finish(player)
                    }
                }

            100 ->
                when (stage) {
                    0 ->
                        if (player.getSkills().getStaticLevel(Skills.SUMMONING) == 99) {
                            options(
                                "So, what's Summoning all about, then?",
                                "Can I buy some Summoning supplies?",
                                "Can I buy a Summoning skillcape?",
                            )
                            stage = 600
                        } else {
                            options(
                                "So, what's Summoning all about, then?",
                                "Can I buy some Summoning supplies?",
                                "Please tell me about skillcapes.",
                            )
                            stage = 1
                        }

                    1 ->
                        when (buttonId) {
                            1 -> {
                                player("So, what's summoning all about, then?")
                                stage = 10
                            }

                            2 -> {
                                player("Can I buy some summoning supplies, please?")
                                stage = 34
                            }

                            3 -> {
                                player("Please tell me about Skillcapes.")
                                stage = 400
                            }
                        }

                    10 -> {
                        npc("In general? Or did you have a specific topic in mind?")
                        stage = 11
                    }

                    11 -> {
                        options(
                            "In general.",
                            "Tell me about summoning familiars.",
                            "Tell me about special moves.",
                            "Tell me about pets.",
                        )
                        stage = 12
                    }

                    12 ->
                        when (buttonId) {
                            1 -> {
                                npc(
                                    "Well, you already know about Summoning in general;",
                                    "otherwise, we would not be having this conversation!",
                                )
                                stage = 100
                            }

                            2 -> {
                                npc(
                                    "Summoned familiars are at the very core of Summoning.",
                                    "Each familiar is different, and the more powerful the",
                                    "summoner, the more powerful the familiar they can",
                                    "summon.",
                                )
                                stage = 20001
                            }

                            3 -> {
                                npc(
                                    "Well, if a Summoning pouch is split apart at an obelisk,",
                                    "then the energy it contained will reconstitute itself -",
                                    "transform - into a scroll. This scroll can then be used to",
                                    "make your familiar perform its special move.",
                                )
                                stage = 2001
                            }

                            4 -> {
                                npc(
                                    "Well, these are not really an element of the skill, as such,",
                                    "but more like a side-effect of training.",
                                )
                                stage = 2001
                            }
                        }

                    100 -> {
                        npc(
                            "Effectively, the skill can be broken into two main parts:",
                            "summoned familiars, and pets.",
                        )
                        stage = 101
                    }

                    101 -> {
                        npc(
                            "Summoned familiars are spiritual animals that can be",
                            "called to you from the spirit plane, to serve you for a",
                            "period of time.",
                        )
                        stage = 102
                    }

                    102 -> {
                        npc(
                            "These animals can also perform a special move, which is",
                            "specific to the species. For example, a spirit wolf can",
                            "perform the Howl special move if you are holding the",
                            "correct Howl scroll.",
                        )
                        stage = 103
                    }

                    103 -> {
                        npc(
                            "The last part of Summoning: the pets. The more",
                            "you practice the skill, the more you will comprehend the",
                            "natural world around you.",
                        )
                        stage = 104
                    }

                    104 -> {
                        npc(
                            "This is reflected in your increased ability to raise animals",
                            "as pets. It takes a skilled summoner to be able to raise",
                            "some of Gielinor's more exotic animals, such as the lizards",
                            "of Karamja, or even dragons!",
                        )
                        stage = 105
                    }

                    105 -> {
                        npc("Now that I've given you this overview, do you want to", "know about anything specific?")
                        stage = 106
                    }

                    106 -> {
                        options(
                            "Tell me about summoning familiars.",
                            "Tell me about special moves.",
                            "Tell me about pets.",
                        )
                        stage = 107
                    }

                    107 ->
                        when (buttonId) {
                            1 -> {
                                npc(
                                    "Summoned familiars are at the very core of Summoning.",
                                    "Each familiar is different, and the more powerful the",
                                    "summoner, the more powerful the familiar they can",
                                    "summon.",
                                )
                                stage = 20001
                            }

                            2 -> {
                                npc(
                                    "Well, if a Summoning pouch is split apart at an obelisk,",
                                    "then the energy it contained will reconstitute itself -",
                                    "transform - into a scroll. This scroll can then be used to",
                                    "make your familiar perform its special move.",
                                )
                                stage = 2001
                            }

                            3 -> {
                                npc(
                                    "Well, these are not really an element of the skill, as such,",
                                    "but more like a side-effect of training.",
                                )
                                stage = 2001
                            }
                        }

                    2001 -> end()
                    2000 -> {
                        npc(
                            "Summoned familiars are at the very core of Summoning.",
                            "Each familiar is different, and the more powerful the",
                            "summoner, the more powerful the familiar they can",
                            "summon.",
                        )
                        stage = 20001
                    }

                    20001 -> end()
                    34 -> {
                        npc("If you like! It's good to see you training.")
                        stage = 35
                    }

                    35 -> {
                        end()
                        openNpcShop(player, NPCs.PIKKUPSTIX_6971)
                    }

                    400 -> {
                        npc(
                            "Of course. Skillcapes are a symbol of achievement. Only",
                            "people who have mastered a skill and reached level 99 can",
                            "get their hands on them and gain the benefits they carry.",
                            "Is there something else I can help you with perhaps?",
                        )
                        stage = 401
                    }

                    401 -> {
                        sendDialogueOptions(
                            player,
                            "Choose an option:",
                            "So, what's Summoning all about, then?",
                            "Can I buy some Summoning supplies?",
                            "Please tell me about skillcapes.",
                        )
                        stage = 1
                    }

                    600 ->
                        when (buttonId) {
                            1 -> {
                                player("So, what's summoning all about, then?")
                                stage = 10
                            }

                            2 -> {
                                player("Can I buy some summoning supplies, please?")
                                stage = 34
                            }

                            3 -> {
                                player("Can I buy a Skillcape of Summoning?")
                                stage = 599
                            }
                        }

                    599 -> {
                        npc(
                            "Why yes you can! I must warn you that they cost",
                            "a total of 99000 coins. Do you wish to still",
                            "buy a Skillcape of Summoning?",
                        )
                        stage = 601
                    }

                    601 -> {
                        options("Yes.", "No.")
                        stage = 602
                    }

                    602 ->
                        when (buttonId) {
                            1 -> {
                                player("Yes, please.")
                                stage = 603
                            }

                            2 -> end()
                        }

                    603 -> {
                        if (player.inventory.freeSlots() < 2) {
                            player("Sorry, I don't seem to have enough inventory space.")
                            stage = 604
                            return true
                        }
                        if (!inInventory(player, Items.COINS_995, 99000)) {
                            end()
                            return true
                        }
                        if (!removeItem(player, Item(Items.COINS_995, 99000))) {
                            player("Sorry, I don't seem to have enough coins", "with me at this time.")
                            stage = 604
                            return true
                        } else {
                            end()
                            player.inventory.add(ITEMS[if (player.getSkills().masteredSkills > 1) 1 else 0], ITEMS[2])
                            player("There you go, enjoy!")
                        }
                    }

                    604 -> end()
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return PikkupstixDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PIKKUPSTIX_6970, NPCs.PIKKUPSTIX_6971)
    }

    companion object {
        private var quest: Quest? = null
        private val ITEMS =
            arrayOf(
                Item(Items.SUMMONING_CAPE_12169),
                Item(Items.SUMMONING_CAPET_12170),
                Item(Items.SUMMONING_HOOD_12171),
            )
        private val BONES = Item(Items.WOLF_BONES_2859, 2)
        private val WOLF_ITEMS =
            arrayOf(
                Item(Items.GOLD_CHARM_12158, 2),
                Item(Items.POUCH_12155, 2),
                Item(Items.SPIRIT_SHARDS_12183, 14),
                Item(Items.TRAPDOOR_KEY_12528),
            )
        private val HOWL_SCROLL = Item(Items.HOWL_SCROLL_12425)
        private val WOLF_POUCH = Item(Items.SPIRIT_WOLF_POUCH_12047)
    }
}

class PikkupstixDialogueExtension : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.PIKKUPSTIX_6970)
        when (stage) {
            0 -> npc(FaceAnim.NEUTRAL, "Good choice. Here you go, you can now store spells on", "it.").also { stage++ }
            1 -> player(FaceAnim.CALM_TALK, "Excellent. Thank you!").also { stage = END_DIALOGUE }
        }
    }
}

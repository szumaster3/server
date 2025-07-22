package content.region.misthalin.draynor.quest.anma.dialogue

import content.region.misthalin.draynor.quest.anma.AnimalMagnetism
import content.region.misthalin.draynor.quest.anma.cutscene.AnimalMagnetismCutscene
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class AliceHusbandDialogue(player: Player? = null) : Dialogue(player) {
    
    private lateinit var quest: Quest

    override fun open(vararg args: Any?): Boolean {
        quest = player.questRepository.getQuest(Quests.ANIMAL_MAGNETISM)
        npc = args[0] as NPC
        if (!player.equipment.containsAtLeastOneItem(Items.GHOSTSPEAK_AMULET_552)) {
            npc("Wooo wooo wooooo!")
            return true
        }
        when (quest.getStage(player)) {
            0 -> {
                npc("Hi, I don't feel like talking.")
            }

            10 -> {
                player("Your animals don't look too healthy.")
            }

            11 -> {
                npc("'Ave you talked to the wife for me?")
            }

            12 -> {
                player("Your wife says she needs the family cash and wants to", "know what you did with it.")
                stage++
            }

            13 -> {
                npc("Any luck wiv me wife?")
            }

            14 -> {
                npc("'Ave you talked to 'er?")
            }

            15 -> {
                npc("'Ave you talked to 'er?")
            }

            16 -> {
                player(
                    "I talked to your wife and thought that if you had a",
                    "special amulet, you could speak to her and sort out the",
                    "bank situation without me being involved.",
                )
            }

            17 -> {
                player(
                    "I talked to your wife and thought that if you had a",
                    "special amulet, you could speak to her and sort out the",
                    "bank situation without me being involved.",
                )
            }

            18 -> {
                player(
                    "I talked to your wife and thought that if you had a",
                    "special amulet, you could speak to her and sort out the",
                    "bank situation without me being involved.",
                )
            }

            19 -> {
                npc("Ahh, many thanks. Now what was it you were wanting", "again?")
            }

            else -> {
                npc("Hello, how can I help you? I'm sellin' if you have ecto-", "tokens.")
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (!player.equipment.containsAtLeastOneItem(Items.GHOSTSPEAK_AMULET_552)) {
            end()
            return true
        }
        when (quest.getStage(player)) {
            0 -> {
                when (stage) {
                    else -> {
                        end()
                    }
                }
            }

            10 -> {
                when (stage) {
                    0 -> {
                        npc(
                            "It's that fountain thingy in the temple to the east. It's",
                            "turned them all into zombies.",
                        )
                        stage++
                    }

                    1 -> {
                        player("What use are zombie animals?")
                        stage++
                    }

                    2 -> {
                        npc(
                            "None at all, mate, except that those worshippers at that",
                            "temple keep comin' and killin' em all for their bones.",
                            "Don't ask me why.",
                        )
                        stage++
                    }

                    3 -> {
                        player("But you're a ghost - surely you know", "something about it.")
                        stage++
                    }

                    4 -> {
                        npc(
                            "I don't know nuthin' about nuthin'. Oim a simple ghost",
                            "with simple needs. All I know is, years ago, that temple",
                            "started glowing green and, a few months later, I woke",
                            "up dead. That's all there is to it.",
                        )
                        stage++
                    }

                    5 -> {
                        npc("I do miss the wife though; tell 'er I still loves her.")
                        stage++
                    }

                    6 -> {
                        player("Would I be able to buy some of your chickens?")
                        stage++
                    }

                    7 -> {
                        npc("Talk to my wife and I'll think about it.")
                        stage++
                    }

                    8 -> {
                        if (quest.getStage(player) == 10) {
                            quest.setStage(player, 11)
                        }
                        end()
                    }
                }
            }

            11 -> {
                when (stage) {
                    0 -> {
                        player("Not yet; I've been distracted by the thought of undead", "cow milk.")
                        stage++
                    }

                    1 -> {
                        end()
                    }
                }
            }

            12 -> {
                when (stage) {
                    1 -> {
                        npc("Tell 'er I spent it on cheap spirits, har har.")
                        stage++
                    }

                    2 -> {
                        player("Your sense of humour died too, it seems...")
                        stage++
                    }

                    3 -> {
                        npc("Hah, just trying to lift your spirits.")
                        stage++
                    }

                    4 -> {
                        player("I rest my case.")
                        stage++
                    }

                    5 -> {
                        npc(
                            "Suit yerself, stick-in-the-mud. Anyway, Oim not one o'",
                            "them yokels. Tell 'er I putted the cash in the bank like",
                            "she always told me to.",
                        )
                        stage++
                    }

                    6 -> {
                        npc("A warning to ya, too; annoy her and I'll haunt ya till", "yer hair turns white.")
                        stage++
                    }

                    7 -> {
                        quest.setStage(player, 13)
                        end()
                    }
                }
            }

            13 -> {
                when (stage) {
                    0 -> {
                        player("Nothing new, no.")
                        stage++
                    }

                    1 -> {
                        end()
                    }
                }
            }

            14 -> {
                when (stage) {
                    0 -> {
                        player("You may not believe me, but she wants me to find", "your bank pass now.")
                        stage++
                    }

                    1 -> {
                        npc(
                            "Maybe she said that, maybe she didn't. I think you're",
                            "just after me savings. Tell 'er that no one but a fool",
                            "gives away their pass numbers.",
                        )
                        stage++
                    }

                    2 -> {
                        npc("Go tell 'er now, if you're not a double-dealing' scammer,", "that is.")
                        stage++
                    }

                    3 -> {
                        quest.setStage(player, 15)
                        end()
                    }
                }
            }

            15 -> {
                when (stage) {
                    0 -> {
                        player("Not since we last spoke.")
                        stage++
                    }

                    1 -> {
                        end()
                    }
                }
            }

            16 -> {
                when (stage) {
                    0 -> {
                        npc(
                            "Arr, that makes far more sense than I was expecting",
                            "from a muscle-head like you. My wife's a clever one.",
                        )
                        stage++
                    }

                    1 -> {
                        if (player.inventory.containsItem(AnimalMagnetism.CRONE_AMULET)) {
                            player(
                                "Well... oh, never mind. I'm desperate enough for those",
                                "chickens to let that pass.",
                            )
                            stage += 2
                        } else {
                            player("Well...oh, never mind. I'm working on getting the", "amulet anyway.")
                            stage++
                        }
                    }

                    2 -> {
                        end()
                    }

                    3 -> {
                        npc(
                            "Give me that amulet, then, and we'll be seeing about",
                            "your unnatural desire for chickens.",
                        )
                        stage++
                    }

                    4 -> {
                        player("Okay, you need it more than I do, I suppose.")
                        stage++
                    }

                    5 -> {
                        npc("Ta, mate.")
                        stage++
                    }

                    6 -> {
                        player("Lucky we had such a brilliant idea.")
                        stage++
                    }

                    7 -> {
                        if (player.inventory.remove(AnimalMagnetism.CRONE_AMULET)) {
                            quest.setStage(player, 19)
                            end()
                        }
                    }
                }
            }

            19 -> {
                when (stage) {
                    0 -> {
                        player("I need a couple of your chickens.")
                        stage++
                    }

                    1 -> {
                        npc("Chickens is tricksy, 'specially dead 'uns. I'll have to catch", "'em for ye.")
                        stage++
                    }

                    2 -> {
                        player("They look preety pathetic, how hard can it be?")
                        stage++
                    }

                    3 -> {
                        npc("Stand back while I catches 'em, ya city slicker.")
                        stage++
                    }

                    4 -> {
                        end()
                        AnimalMagnetismCutscene(player).start()
                    }
                }
            }

            else -> {
                when (stage) {
                    0 -> {
                        options(
                            "Could I buy those chickens now, then?",
                            "Your animals don't look too healthy.",
                            "I'm okay, thank you.",
                            "Where can I get these ecto-tokens?",
                        )
                        stage++
                    }

                    1 -> {
                        when (buttonId) {
                            1 -> {
                                player("Could I buy those chickens now, then?")
                                stage = 10
                            }

                            2 -> {
                                player("Your animals don't look too healthy.")
                                stage = 20
                            }

                            3 -> {
                                player("I'm okay, thank you.")
                                stage = 30
                            }

                            4 -> {
                                player("Where can I get these ecto-tokens?")
                                stage = 40
                            }
                        }
                    }

                    10 -> {
                        npc("I can hand over a chicken if you give me 10 of them", "ecto-token thingies per bird.")
                        stage++
                    }

                    11 -> {
                        options(
                            "Could I buy 1 chicken now?",
                            "Could I buy 2 chickens now?",
                            "Your animals don't look too healthy; I'll buy elsewhere.",
                        )
                        stage++
                    }

                    12 -> {
                        when (buttonId) {
                            1 -> {
                                player("Could I buy 1 chicken now?")
                                stage = 14
                            }

                            2 -> {
                                player("Could I buy 2 chickens now?")
                                stage = 15
                            }

                            3 -> {
                                player("Your animals don't look too healthy; I'll buy elsewhere.")
                                stage++
                            }
                        }
                    }

                    13 -> {
                        end()
                    }

                    14 -> {
                        buy(1)
                        stage = 16
                    }

                    15 -> {
                        buy(2)
                        stage = 16
                    }

                    16 -> {
                        end()
                    }

                    20 -> {
                        npc(
                            "It's that fountain thingy in the temple to the east. It's",
                            "turned them all into zombies.",
                        )
                        stage++
                    }

                    21 -> {
                        player("What use are zombie animals?")
                        stage++
                    }

                    22 -> {
                        npc(
                            "None at all, mate, except that those worshippers at that",
                            "temple keep comin' and killin' em all for their bones.",
                            "Don't ask me why.",
                        )
                        stage++
                    }

                    23 -> {
                        player("But you're a ghost - surely you know", "something about it.")
                        stage++
                    }

                    24 -> {
                        npc(
                            "I don't know nuthin' about nuthin'. Oim a simple ghost",
                            "with simple needs. All I know is, years ago, that temple",
                            "started glowing green and, a few months later, I woke",
                            "up dead. That's all there is to it.",
                        )
                        stage++
                    }

                    25 -> {
                        end()
                    }

                    30 -> {
                        end()
                    }

                    40 -> {
                        npc(
                            "The ghosts I talk to say that the tokens have something",
                            "to do with the tower just east of here. If you need to",
                            "collect some I'd try there.",
                        )
                        stage++
                    }

                    41 -> {
                        end()
                    }
                }
            }
        }
        return true
    }

    private fun buy(amount: Int) {
        val tokens = Item(4278, amount * 10)
        if (!player.inventory.containsItem(tokens)) {
            npc(
                "I'm not a charity here, ya know. Bad enough all you",
                "cow-killing folks are a'slaughterin' me beasts. Come back",
                "when ya have enough tokens.",
            )
            return
        }
        if (player.inventory.freeSlots() < amount) {
            player("Sorry, I don't have enough inventory space.")
            return
        }
        if (player.inventory.remove(tokens)) {
            player.inventory.add(Item(10487, amount), player)
            npc("Great! I'm laying away me tokens for some killer cows.", "That'll learn them bones rustlers.")
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ALICES_HUSBAND_5202)
}

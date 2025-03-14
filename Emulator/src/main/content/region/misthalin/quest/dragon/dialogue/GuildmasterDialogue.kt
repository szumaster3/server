package content.region.misthalin.quest.dragon.dialogue

import content.region.misthalin.quest.dragon.DragonSlayer
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.GroundItemManager
import org.rs.consts.NPCs
import org.rs.consts.Quests

class GuildmasterDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (player.getQuestRepository().points < 32) {
            return true
        }
        quest = player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER)
        npc("Greetings!")
        stage =
            if (quest!!.getStage(player) == 10) {
                0
            } else {
                1
            }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            30 -> end()
            40 -> end()
            100 ->
                when (stage) {
                    1 -> {
                        options("What is this place?", "Can I have another key to Melzar's Maze?")
                        stage = 2
                    }

                    2 ->
                        when (buttonId) {
                            1 -> handleDescription(buttonId)
                            2 -> {
                                player("Can I have another key to Melzar's Maze?")
                                stage = 20
                            }
                        }

                    99 -> end()
                    20 -> {
                        if (!player.inventory.containsItem(DragonSlayer.MAZE_KEY) &&
                            !player.bank.containsItem(DragonSlayer.MAZE_KEY)
                        ) {
                            if (!player.inventory.add(DragonSlayer.MAZE_KEY)) {
                                GroundItemManager.create(DragonSlayer.MAZE_KEY, player)
                            }
                            interpreter.sendItemMessage(DragonSlayer.MAZE_KEY.id, "The Guildmaster hands you a key.")
                            stage = 21
                        }
                        npc("I think you've already got a key.")
                        stage = 21
                    }

                    21 -> end()
                }

            20 ->
                when (stage) {
                    1 -> {
                        player("About my quest to kill the dragon...")
                        stage = 2
                    }

                    2 -> {
                        npc(
                            "If you're serious about taking on Elvarg, first you'll",
                            "need to get to Crandor. The island is surrounded by",
                            "dangerous reefs, so you'll need a ship capable of getting",
                            "through them and a map to show you the way.",
                        )
                        stage = 3
                    }

                    3 -> {
                        npc(
                            "When you reach Crandor, you'll also need some kind of",
                            "protection against the dragon's breath.",
                        )
                        stage = 18
                    }

                    18 -> {
                        options(
                            "How can I find the route to Crandor?",
                            "Where can I find the right ship?",
                            "How can I protect myself from the dragon's breath?",
                            "What's so special about this dragon?",
                            "Okay, I'll get going.",
                        )
                        stage = 19
                    }

                    19 ->
                        when (buttonId) {
                            1 -> {
                                player("How can I find the route to Crandor?")
                                stage = 100
                            }

                            2 -> {
                                player("Where can I find the right ship?")
                                stage = 110
                            }

                            3 -> {
                                player("How can I protect myself from the dragon's breath?")
                                stage = 120
                            }

                            4 -> {
                                player("What's so special about this dragon?")
                                stage = 130
                            }

                            5 -> {
                                player("Okay, I'll get going.")
                                stage = 140
                            }
                        }

                    100 -> {
                        npc(
                            "Only one map exists that shows the route through the",
                            "reefs of Crandor. That map was split into three pieces",
                            "by Melzar, Thalzar and Lozar, the wizards who escaped",
                            "from the dragon. Each of them took one piece.",
                        )
                        stage = 101
                    }

                    101 -> {
                        options(
                            "Where is Melzar's map piece?",
                            "Where is Thalzar's map piece?",
                            "Where is Lozar's map piece?",
                        )
                        stage = 102
                    }

                    102 ->
                        when (buttonId) {
                            1 -> {
                                player("Where is Melzar's map piece?")
                                stage = 1000
                            }

                            2 -> {
                                player("Where is Thalzar's map piece?")
                                stage = 1020
                            }

                            3 -> {
                                player("Where is Lozar's map piece?")
                                stage = 1030
                            }
                        }

                    1000 -> {
                        npc(
                            "Melzar built a castle on the site of the Crandorian",
                            "refugee camp, north of Rimmington. He's locked himself",
                            "in there and no one's seen him for years.",
                        )
                        stage = 1001
                    }

                    1001 -> {
                        npc(
                            "The inside of his castle is like a maze, and is populated",
                            "by undead monsters. Maybe, if you could get all the",
                            "way through the maze, you could find his piece of the",
                            "map.",
                        )
                        if (player.inventory.containsItem(DragonSlayer.MAZE_KEY) ||
                            player.bank.containsItem(DragonSlayer.MAZE_KEY)
                        ) {
                            stage = 1004
                            return true
                        }
                        stage = 1002
                    }

                    1002 -> {
                        npc(
                            "Adventurers sometimes go in there to prove themselves,",
                            "so I can give you this key to Melzar's Maze.",
                        )
                        stage = 1003
                    }

                    1003 -> {
                        if (!player.inventory.add(DragonSlayer.MAZE_KEY)) {
                            GroundItemManager.create(DragonSlayer.MAZE_KEY, player)
                        }
                        interpreter.sendItemMessage(DragonSlayer.MAZE_KEY.id, "The Guildmaster hands you a key.")
                        stage = 1004
                    }

                    1004 -> end()
                    1020 -> {
                        npc(
                            "Thalzar was the most paranoid of the three wizards. He",
                            "hid his map piece and took the secret of its location to",
                            "his grave.",
                        )
                        stage = 1021
                    }

                    1021 -> {
                        npc(
                            "I don't think you'll be able to find out where it is by",
                            "ordinary means. You'll need to talk to the Oracle on",
                            "Ice Mountain.",
                        )
                        stage = 1022
                    }

                    1022 -> end()
                    1030 -> {
                        npc(
                            "A few weeks ago, I'd have told you to speak to Lozar",
                            "herself, in her house across the river from Lumbridge.",
                        )
                        stage = 1031
                    }

                    1031 -> {
                        npc(
                            "Unfortunately, goblin raiders killed her and stole",
                            "everything. One of the goblins from the Goblin Village",
                            "probably has the map piece now.",
                        )
                        stage = 1032
                    }

                    1032 -> end()
                    110 -> {
                        npc(
                            "Even if you find the right route, only a ship made to",
                            "the old crandorian design would be able to navigate",
                            "through the reefs to the island.",
                        )
                        stage = 111
                    }

                    111 -> {
                        npc("If there's still one in existence, it's probably in Port", "Sarim.")
                        stage = 112
                    }

                    112 -> {
                        npc(
                            "Then, of course, you'll need to find a captain willing to",
                            "sail to Crandor, and I'm not sure where you'd find one",
                            "of them!",
                        )
                        stage = 113
                    }

                    113 -> end()
                    120 -> {
                        npc(
                            "That part shouldn't be too different, actually. I believe",
                            "the Duke of Lumbridge has a special shield in his",
                            "armoury that is enchanted against dragon's breath.",
                        )
                        stage = 121
                    }

                    121 -> end()
                    130 -> {
                        npc(
                            "Thirty years ago, Candor was a thriving community",
                            "with a great tradition of mages and adventurers. Many",
                            "Crandorians even earned the right to be part of the",
                            "Champions' Guild!",
                        )
                        stage = 131
                    }

                    131 -> {
                        npc(
                            "One of their adventurers went too far, however. He",
                            "descended into the volcano in the centre of Crandor",
                            "and woke the dragon Elvarg.",
                        )
                        stage = 132
                    }

                    132 -> {
                        npc(
                            "He must have fought valiantly against the dragon",
                            "because they say that, to this day, she has a scar down",
                            "her side,",
                        )
                        stage = 133
                    }

                    133 -> {
                        npc(
                            "but the dragon still won the fight. She emerged and laid",
                            "waste to the whole of Crandor with her fire breath!",
                        )
                        stage = 134
                    }

                    134 -> {
                        npc(
                            "Some refugees managed to escape in fishing boats.",
                            "They landed on the coast, north of Rimmington, and",
                            "set up camp but the dragon followed them and burned",
                            "the camp to the ground.",
                        )
                        stage = 135
                    }

                    135 -> {
                        npc(
                            "Out of all the people of Crandor there were only three",
                            "survivors: a trio of wizards who used magic to escape.",
                            "Their names were Thalzar, Lozar and Melzar.",
                        )
                        stage = 140
                    }

                    136 -> {
                        npc(
                            "If you're serious about taking on Elvarg, first you'll",
                            "need to get to Crandor. The island is surrounded by",
                            "dangerous reefs, so you'll need a ship capable of getting",
                            "through them and a map to show you the way.",
                        )
                        stage = 17
                    }

                    140 -> end()
                    99 -> end()
                }

            15 ->
                when (stage) {
                    1 -> {
                        options("What is this place?", "I talked to Oziach...")
                        stage = 2
                    }

                    2 ->
                        when (buttonId) {
                            1 -> handleDescription(buttonId)
                            2 -> {
                                player("I talked to Oziach and he have me a quest.")
                                stage = 3
                            }
                        }

                    3 -> {
                        npc("Oh? What did he tell you to do?")
                        stage = 4
                    }

                    4 -> {
                        player("Defeat the dragon of Crandor.")
                        stage = 5
                    }

                    5 -> {
                        npc("The dragon of Crandor?")
                        stage = 7
                    }

                    7 -> {
                        player("Um, yes...")
                        stage = 8
                    }

                    8 -> {
                        npc("Goodness, he hasn't given you an easy job, has he?")
                        stage = 9
                    }

                    9 -> {
                        player("What's so special about this dragon?")
                        stage = 10
                    }

                    10 -> {
                        npc(
                            "Thirty years ago, Crandor was a thriving community",
                            "with a great tradition of mages and adventurers. Many",
                            "Crandorians even earned the right to be part of the",
                            "Champion's Guild!",
                        )
                        stage = 11
                    }

                    11 -> {
                        npc(
                            "One of their adventurers went too far, however. He",
                            "descended into the volcano in the centre of Crandor",
                            "and woke the dragon Elvarg.",
                        )
                        stage = 12
                    }

                    12 -> {
                        npc(
                            "He must have fought valiantly against the dragon",
                            "because they say that, to this day, she has a scar down",
                            "her side,",
                        )
                        stage = 13
                    }

                    13 -> {
                        npc(
                            "but the dragon still won the fight. She emerged and laid",
                            "waste to the whole of Crandor with her fire breath!",
                        )
                        stage = 14
                    }

                    14 -> {
                        npc(
                            "Some refugees managed to escape in fishing boats.",
                            "They landed on the coast, north of Rimmington, and",
                            "set up camp but the dragon followed them and burned",
                            "the camp to the ground.",
                        )
                        stage = 15
                    }

                    15 -> {
                        npc(
                            "Out of all the people of Crandor there were only three",
                            "survivors: a trio of wizards who used the magic to escape.",
                            "Their names were Thalzar, Lozar and Melzar.",
                        )
                        stage = 16
                    }

                    16 -> {
                        npc(
                            "If you're serious about taking on Elvarg, first you'll",
                            "need to get to Crandor. The island is surrounded by",
                            "dangerous reefs, so you'll need a ship capable of getting",
                            "through them and a map to show you the way.",
                        )
                        stage = 17
                    }

                    17 -> {
                        npc(
                            "When you reach Crandor, you'll also need some kind of",
                            "protection against the dragon's breath.",
                        )
                        stage = 19
                    }

                    19 -> {
                        player("Okay, I'll get going.")
                        stage = 20
                    }

                    20 -> {
                        quest!!.setStage(player, 20)
                        end()
                    }

                    99 -> end()
                }

            10 ->
                when (stage) {
                    18 -> end()
                    0 -> {
                        npc("Have you gone to talk to Oziach yet?")
                        stage = 1
                    }

                    1 -> {
                        player("No, not yet.")
                        stage = 2
                    }

                    2 -> {
                        npc(
                            "You can only begin your journey once you've",
                            "spoken to Oziach who is located near Edgeville.",
                        )
                        stage = 3
                    }

                    3 -> {
                        player("Okay, thanks.")
                        stage = 4
                    }

                    4 -> end()
                }

            0 ->
                when (stage) {
                    1 -> {
                        options("What is this place?", "Can I have a quest?")
                        stage = 10
                    }

                    10 ->
                        when (buttonId) {
                            1 -> handleDescription(buttonId)
                            2 -> {
                                player("Can I have a quest?")
                                stage = 13
                            }
                        }

                    11 -> {
                        if (quest!!.getStage(player) == 10) {
                            npc(
                                "You're already on a quest for me, if I recall correctly.",
                                "Have you talked to Oziach yet?",
                            )
                            stage = 20
                            return true
                        }
                        npc(
                            "This is the Champions' Guild. Only adventurers who",
                            "have proved themselves worthy by gaining influence",
                            "from quests are allowed in here.",
                        )
                        stage = 12
                    }

                    12 -> end()
                    13 -> {
                        npc("Aha!")
                        stage = 14
                    }

                    14 -> {
                        npc(
                            "Yes! A mighty and perilous quest fit only for the most",
                            "powerful champion! And, at the end of it, you will earn",
                            "the right to wear the legendary rune platebody!",
                        )
                        stage = 15
                    }

                    15 -> {
                        player("So, what is this quest?")
                        stage = 16
                    }

                    16 -> {
                        npc(
                            "You'll have to speak to Oziach, the maker of rune",
                            "armour. He sets the quests that champions must",
                            "complete in order to wear it.",
                        )
                        stage = 17
                    }

                    17 -> {
                        npc(
                            "Oziach lives in a hut, by the cliffs to the west of",
                            "Edgeville. he can be a little...odd...sometimes, though.",
                        )
                        stage = 18
                        quest!!.start(player)
                    }

                    18 -> end()
                    20 -> {
                        player("No, not yet.")
                        stage = 21
                    }

                    21 -> {
                        npc(
                            "Well, he's the only one who can grant you the right to",
                            "wear rune platemail. He lives in a hut, by the cliffs west",
                            "of Edgeville.",
                        )
                        stage = 22
                    }

                    22 -> {
                        player("Okay, I'll go and talk to him.")
                        stage = 23
                    }

                    23 -> end()
                    99 -> end()
                }
        }
        return true
    }

    fun handleDescription(buttonId: Int) {
        when (buttonId) {
            1 -> {
                if (quest!!.getStage(player) == 10) {
                    npc("You're already on a quest for me, if I recall correctly.", "Have you talked to Oziach yet?")
                    stage = 20
                    return
                }
                npc(
                    "This is the Champions' Guild. Only adventurers who",
                    "have proved themselves worthy by gaining influence",
                    "from quests are allowed in here.",
                )
                stage = 99
            }

            2 -> end()
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUILDMASTER_198)
    }
}

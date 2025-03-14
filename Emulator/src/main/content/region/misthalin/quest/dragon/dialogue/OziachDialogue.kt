package content.region.misthalin.quest.dragon.dialogue

import content.region.misthalin.quest.dragon.DragonSlayer
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class OziachDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER)
        player.debug("" + quest!!.getStage(player))
        when (quest!!.getStage(player)) {
            100 -> {
                npc("Aye, 'tis a fair day, my mighty dragon-slaying friend.")
                stage =
                    if (player.inventory.containsItem(Item(11286))) {
                        42
                    } else {
                        0
                    }
            }

            40, 30, 20, 15 ->
                if (args.size > 1) {
                    npc("I'm not selling' ye anything till you've slayed that", "dragon! Now be off wi' ye.")
                    stage = -1
                    return true
                } else {
                    npc("Have ye slayed that dragon yet?")
                    stage = 0
                }

            10 ->
                stage =
                    if (args.size > 1) {
                        player("Can you sell me a rune platebody?")
                        0
                    } else {
                        npc("Aye, 'tis a fair day my friend.")
                        -1
                    }

            else -> {
                npc("Aye, 'tis a fair day my friend.")
                stage = 0
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            30, 20 ->
                if (stage == -1) {
                    end()
                } else {
                    end()
                }

            100 ->
                when (stage) {
                    0 -> {
                        options(
                            "Can I buy a rune platebody now, please?",
                            "I'm not your friend.",
                            "Yes, it's a very nice day.",
                            "Can I have another key to Melzar's Maze?",
                        )
                        stage = 1
                    }

                    1 ->
                        when (buttonId) {
                            1 -> {
                                player("Can I buy a rune platebody now, please?")
                                stage = 10
                            }

                            2 -> {
                                player("I'm not your friend.")
                                stage = 20
                            }

                            3 -> {
                                player("Yes, it's a very nice day.")
                                stage = 30
                            }

                            4 -> {
                                player("Can I have another key to Melzar's Maze?")
                                stage = 40
                            }

                            5 -> {
                                player("Here you go.")
                                stage = 44
                            }
                        }

                    10 -> {
                        end()
                        npc.openShop(player)
                    }

                    20 -> end()
                    30 -> end()
                    40 -> {
                        npc(
                            "It's the Guildmaster in the Champions' Guild who hands",
                            "those keys out now. Go talk to him. No need to bother",
                            "me if you don't need armour.",
                        )
                        stage = 41
                    }

                    41 -> end()
                    42 -> {
                        npc("Ye've got... Ye've found a draconic visage! Could I look at", "it?")
                        stage++
                    }

                    43 -> {
                        options(
                            "Can I buy a rune platebody now, please?",
                            "I'm not your friend.",
                            "Yes, it's a very nice day.",
                            "Can I have another key to Melzar's Maze?",
                            "Here you go",
                        )
                        stage = 1
                    }

                    44 -> {
                        npc("Amazin'! Ye can almost feel it pulsin with draconic power!")
                        stage++
                    }

                    45 -> {
                        npc(
                            "Now, if ye want me to, I could attach this to yer anti-",
                            "dragonbreath shield and make something pretty special.",
                        )
                        stage++
                    }

                    46 -> {
                        npc("The shield won't be easy to weild though; ye'll need level", "75 Defence.")
                        stage++
                    }

                    47 -> {
                        npc("I'll charge 1,250,000 coins to construct it. What d'ye say?")
                        stage++
                    }

                    48 -> {
                        options("Yes, please!", "No, thanks.", "That's a bit expensive!")
                        stage++
                    }

                    49 ->
                        when (buttonId) {
                            1 -> {
                                player("Yes please!")
                                stage = 50
                            }

                            2 -> {
                                player("No thanks.")
                                stage = 52
                            }

                            3 -> {
                                player("That's a bit expensive!")
                                stage = 53
                            }
                        }

                    50 ->
                        stage =
                            if (player.inventory.contains(11286, 1)) {
                                if (player.inventory.contains(995, 1250000)) {
                                    if (player.inventory.contains(1540, 1)) {
                                        player.inventory.remove(Item(1540, 1))
                                        player.inventory.remove(Item(11286, 1))
                                        player.inventory.remove(Item(995, 1250000))
                                        player.inventory.add(Item(11283, 1))
                                        player.dialogueInterpreter.sendItemMessage(
                                            11283,
                                            "Oziach skillfully forges the shield and visage into a new shield.",
                                        )
                                        51
                                    } else {
                                        npc(
                                            "Ye need an anti-dragonbreath shield for me to",
                                            "attach this onto, talk to me again once you do.",
                                        )
                                        41
                                    }
                                } else {
                                    player("I don't seem to have enough coins tho,", "I will return once I do.")
                                    41
                                }
                            } else {
                                npc(
                                    "Ye need the draconic visage for me to attach.",
                                    "Talk to me again once you have it.",
                                )
                                41
                            }

                    51 -> {
                        npc(
                            "There ye go. Now, the more dragonfire your shield",
                            "absorbs, the more powerful it'll become.",
                        )
                        stage = 41
                    }

                    52 -> {
                        npc("Talk to me again if ye change your mind", "mighty dragon slayer.")
                        stage = 41
                    }

                    53 -> {
                        npc("It's the price ye pay to make such a magnificent shield.")
                        stage = 41
                    }
                }

            40 ->
                when (stage) {
                    -1 -> end()
                    0 -> {
                        if (!player.inventory.containsItem(DragonSlayer.ELVARG_HEAD)) {
                            player("Nope, not yet.").also { stage = END_DIALOGUE }
                            return true
                        }
                        player("Yes, I have!")
                        stage = 2
                    }

                    2 -> {
                        player("I have its head here.")
                        stage = 3
                    }

                    3 -> {
                        npc("You actually did it?")
                        stage = 4
                    }

                    4 -> {
                        npc("I underestimated ye, adventurer. I apologize.")
                        stage = 5
                    }

                    5 -> {
                        npc("Yer a true hero, and I'd be happy to sell ye rune", "platebodies.")
                        stage = 6
                    }

                    6 -> {
                        end()
                        val heads = player.inventory.getAmount(DragonSlayer.ELVARG_HEAD)
                        if (player.inventory.remove(
                                Item(
                                    DragonSlayer.ELVARG_HEAD.id,
                                    heads,
                                ),
                            ) &&
                            !player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).isCompleted(player)
                        ) {
                            quest!!.finish(player)
                        }
                    }
                }

            15 ->
                when (stage) {
                    -1 -> end()
                    0 -> {
                        player("Um... no.")
                        stage = 2
                    }

                    1 -> {
                        player("Um... no.")
                        stage = 2
                    }

                    2 -> {
                        npc("Be off with ye then.")
                        stage = 3
                    }

                    3 -> end()
                }

            10 ->
                when (stage) {
                    -1 -> {
                        player("Can you sell me a rune platebody?")
                        stage = 0
                    }

                    0 -> {
                        npc("So, how does thee know I 'ave some?")
                        stage = 2
                    }

                    2 -> {
                        player("The Guildmaster of the Champions' Guild told me.")
                        stage = 3
                    }

                    3 -> {
                        npc(
                            "Yes, I suppose he would, wouldn't he? He's always",
                            "sending you fancy-pants 'heroes' up to bother me.",
                            "Telling me I'll give them a quest or sommat like that.",
                        )
                        stage = 4
                    }

                    4 -> {
                        npc(
                            "Well, I'm not going to let just anyone wear my rune",
                            "platemail. It's only for heroes. So, leave me alone.",
                        )
                        stage = 5
                    }

                    5 -> {
                        player("I thought you were going to give me a quest.")
                        stage = 6
                    }

                    6 -> {
                        npc("*Sigh*")
                        stage = 7
                    }

                    7 -> {
                        npc("All right, I'll give ye a quest. I'll let ye wear my rune", "platemail if ye...")
                        stage = 8
                    }

                    8 -> {
                        npc("Slay the dragon of Crandor!")
                        stage = 9
                    }

                    9 -> {
                        options(
                            "A dragon, that sounds like fun.",
                            "I may be a champion, but I don't think I'm up to dragon-killing yet.",
                        )
                        stage = 10
                    }

                    10 ->
                        when (buttonId) {
                            1 -> {
                                player("A dragon, that sounds like fun!")
                                stage = 100
                            }

                            2 -> {
                                player("I may be a champion, but I don't think I'm up to", "dragon-killing yet.")
                                stage = 200
                            }
                        }

                    100 -> {
                        npc(
                            "Hah, yes, you are a typical reckless adventurer, aren't",
                            "you? Now go kill the dragon and get out of my face.",
                        )
                        stage = 101
                    }

                    101 -> {
                        player("But how can I defeat the dragon?")
                        stage = 102
                    }

                    102 -> {
                        npc(
                            "Go talk to the Guildmaster in the Champions' Guild. He'll",
                            "help ye out if yer so keen on doing a quest. I'm not",
                            "going to be handholding any adventurers.",
                        )
                        stage = 103
                    }

                    103 -> {
                        quest!!.setStage(player, 15)
                        end()
                    }

                    200 -> {
                        npc("Yes, I can understand that. Yer a coward.")
                        stage = 201
                    }

                    201 -> end()
                }

            else ->
                when (stage) {
                    0 -> {
                        options("I'm not your friend.", "Yes, it's a very nice day.")
                        stage = 1
                    }

                    1 ->
                        when (buttonId) {
                            1 -> {
                                player("I'm not your friend.")
                                stage = 10
                            }

                            2 -> {
                                player("Yes, it's a very nice day.")
                                stage = 20
                            }
                        }

                    10 -> {
                        npc("I'm surprised if you're anyone's friend with those kind", "of manners.")
                        stage = 11
                    }

                    11 -> end()
                    20 -> {
                        npc("Aye, may the gods walk by yer side. Now leave me", "alone.")
                        stage = 21
                    }

                    21 -> end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OZIACH_747)
    }
}

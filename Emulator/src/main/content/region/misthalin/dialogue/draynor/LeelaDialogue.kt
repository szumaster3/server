package content.region.misthalin.dialogue.draynor

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LeelaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null
    private var itemCount = 0

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.PRINCE_ALI_RESCUE)
        stage =
            when (quest!!.getStage(player)) {
                60, 100 -> {
                    npc(
                        "Thank you, Al-Kharid will forever owe you for your",
                        "help. I think that if there is every anything that needs to",
                        "be done, you will be someone they can rely on.",
                    )
                    0
                }

                50, 40 ->
                    if (!player.inventory.containsItem(BRONZE_KEY) && !player.bank.containsItem(BRONZE_KEY)) {
                        player("I'm afraid I lost that key you gave me.")
                        0
                    } else {
                        npc(
                            "Ok now, you have all the basic equipment. What are",
                            "your plans to stop the guard interfering?",
                        )
                        10
                    }

                30 -> {
                    npc("My father sent this key for you.", "Be careful not to lose it.")
                    0
                }

                20 -> {
                    player("I am here to help you free the prince.")
                    0
                }

                else -> {
                    player("Hi!")
                    0
                }
            }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            60, 100 -> end()
            50, 40 ->
                when (stage) {
                    0 -> {
                        npc("Foolish man!", "A new key will cost 15 coins.")
                        stage = 1
                    }

                    1 ->
                        if (player.inventory.contains(995, 15)) {
                            player("Here, I have 15 coins.")
                            stage = 2
                        } else {
                            end()
                            player.packetDispatch.sendMessage("You don't have enough coins.")
                        }

                    2 -> {
                        val remove = Item(995, 15)
                        if (!player.inventory.containsItem(remove)) {
                            end()
                            return true
                        }
                        if (!player.inventory.containsItem(BRONZE_KEY) && player.inventory.remove(remove)) {
                            if (!player.inventory.add(BRONZE_KEY)) {
                                GroundItemManager.create(BRONZE_KEY, player)
                            }
                            player.packetDispatch.sendMessage("Leela gives you another key.")
                        }
                        end()
                        stage = 99
                    }

                    99 -> end()
                    10 -> {
                        options(
                            "I haven't spoken to him yet.",
                            "I was going to attack him.",
                            "I hoped to get him drunk.",
                            "Maybe I could bribe him to leave.",
                        )
                        stage = 11
                    }

                    11 ->
                        when (buttonId) {
                            1 -> {
                                player("I haven't spoken to him yet.")
                                stage = 110
                            }

                            2 -> {
                                player("I was going to attack him.")
                                stage = 120
                            }

                            3 -> {
                                player("I hoped to get him drunk.")
                                stage = 130
                            }

                            4 -> {
                                player("Maybe I could bribe him to leave.")
                                stage = 140
                            }
                        }

                    110 -> {
                        npc(
                            "Well, speaking to him may find a weakness he has. See",
                            "if there's something that could stop him bothering us.",
                        )
                        stage = 111
                    }

                    111 -> end()
                    120 -> {
                        npc(
                            "I don't think you should. If you do the rest of the",
                            "gang and Keli would attack you. The door guard",
                            "should be removed first, to make it easy.",
                        )
                        stage = 121
                    }

                    121 -> end()
                    130 -> {
                        npc(
                            "Well, that's possible. These guards do like a drink. I",
                            "would think it will take at least 3 beers to do it well.",
                            "You would probably have to do it all at the same time",
                            "too. The effects of the local bear off quickly.",
                        )
                        stage = 131
                    }

                    131 -> end()
                    140 -> {
                        npc(
                            "You could try. I don't think the emir will pay anything",
                            "towards it. And we did bribe on of their guards once.",
                        )
                        stage = 141
                    }

                    141 -> end()
                }

            30 ->
                when (stage) {
                    0 -> {
                        interpreter.sendDialogue("Leela gives you a copy of the key to the prince's door.")
                        stage = 1
                    }

                    1 -> {
                        if (!player.inventory.add(BRONZE_KEY)) {
                            GroundItemManager.create(BRONZE_KEY, player)
                        }
                        quest!!.setStage(player, 40)
                        npc(
                            "Good, you have all the basic equipment. Next to deal",
                            "with the guard on the door. He is talkative, try to find",
                            "a weakness in him.",
                        )
                        stage = 2
                    }

                    2 -> end()
                }

            20 ->
                when (stage) {
                    0 -> {
                        npc("Your employment is known to me. Now, do you know", "all that we need to make the break?")
                        stage = 1
                    }

                    1 -> {
                        options(
                            "I must make a disguise. What do you suggest?",
                            "I need to get the key made.",
                            "What can I do with the guards?",
                            "I will go and get the rest of the escape equipment.",
                        )
                        stage = 2
                    }

                    2 ->
                        when (buttonId) {
                            1 -> {
                                player("I must a make a disguise. What do you suggest?")
                                stage = 10
                            }

                            2 -> {
                                player("I need to get the key made.")
                                stage = 20
                            }

                            3 -> {
                                player("What can I do with the guards?")
                                stage = 30
                            }

                            4 -> {
                                player("I will go and get the rest of the escape equipment.")
                                stage = 40
                            }
                        }

                    10 -> {
                        npc(
                            "Only the lady Keli, can wander about outside the jail.",
                            "The guards will shoot to kill if they see the prince out",
                            "so we need a disguise good enough to fool them at a",
                            "distance.",
                        )
                        stage = 11
                    }

                    11 -> {
                        if (player.inventory.containsItem(YELLOW_WIG)) {
                            npc("The wig you have got, well done.")
                            itemCount++
                        } else {
                            npc(
                                "You need a wig, maybe made from wool. If you find",
                                "someone who can work with wool ask them about it.",
                                "There's a witch nearby may be able to help you dye it.",
                            )
                        }
                        stage = 12
                    }

                    12 -> {
                        if (player.inventory.containsItem(SKIRT)) {
                            npc("You have got the skirt, good.")
                            itemCount++
                        } else {
                            npc("You will need to get a pink skirt, same as Keli's.")
                        }
                        stage = 13
                    }

                    13 -> {
                        if (player.inventory.containsItem(PASTE)) {
                            npc("You have the skin paint, well done. I thought you would", "struggle to make that.")
                            itemCount++
                        } else {
                            npc(
                                "We still need something to colour the Prince's skin",
                                "lighter. There's a witch close to here. She knows about",
                                "many things. She may know some way to make the",
                                "skin lighter.",
                            )
                        }
                        stage = if (itemCount == 3) 14 else 15
                    }

                    14 -> {
                        npc("You do have everything for the disguise.")
                        stage = 15
                    }

                    15 ->
                        stage =
                            if (player.inventory.containsItem(ROPE)) {
                                npc(
                                    "You have the rope I see, to tie up Keli. That will be the",
                                    "most dangerous part of the plan.",
                                )
                                16
                            } else {
                                npc(
                                    "You will still need some rope to tie up Keli, of course. I",
                                    "heard that there's a good rope maker around here.",
                                )
                                16
                            }

                    16 -> end()
                    20 -> {
                        npc(
                            "Yes, that is most important. There is no way you can",
                            "get the real key. It is on a chain around Keli's neck.",
                            "Almost impossible to steal.",
                        )
                        stage = 21
                    }

                    21 -> {
                        npc(
                            "Get some soft clay and get her to show you the key",
                            "somehow. Then take the print, with bronze, to my",
                            "father.",
                        )
                        stage = 22
                    }

                    22 -> end()
                    30 -> {
                        npc(
                            "most of the guards will be easy. The disguise will get",
                            "past them. The only guards who will be a problem will be",
                            "the one at the door.",
                        )
                        stage = 31
                    }

                    31 -> end()
                    40 -> {
                        npc("Good, I shall await your return with everything.")
                        stage = 41
                    }

                    41 -> end()
                }

            else ->
                when (stage) {
                    0 -> {
                        npc("Please leave me alone adventurer, I am", "a very busy woman.")
                        stage = 1
                    }

                    1 -> end()
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return LeelaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LEELA_915)
    }

    companion object {
        private val ROPE = Item(954)
        private val SKIRT = Item(1013)
        private val YELLOW_WIG = Item(2419)
        private val PASTE = Item(2424)
        private val BRONZE_KEY = Item(2418)
    }
}

package content.region.kandarin.quest.fishingcompo.dialogue

import content.region.kandarin.quest.fishingcompo.FishingContest
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class GrandpaJackDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            "Hello young'on!",
            "Come to visit old Grandpa Jack? I can tell ye stories",
            " for sure. I used to be the best fisherman these parts",
            "have seen!",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                options(
                    "Tell me a story then.",
                    "Are you entering the fishing competition?",
                    "Sorry, I don't have time now.",
                    "Can I buy one of your fishing rods?",
                    "I've forgotten how to fish, can you remind me?",
                )
                stage++
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("Tell me a story then.")
                        stage = 10
                    }

                    2 -> {
                        player("Are you entering the fishing competition?")
                        stage = 20
                    }

                    3 -> {
                        player("Sorry, I don't have time now.")
                        stage = 35
                    }

                    4 -> {
                        player("Can I buy one of your fishing rods?")
                        stage = 40
                    }

                    5 -> {
                        player("I've forgotten how to fish, can you remind me?")
                        stage = 50
                    }
                }

            4 -> {
                options(
                    "I don't suppose you could give me any hints?",
                    "That's less competition for me then.",
                )
                stage++
            }

            5 ->
                when (buttonId) {
                    1 -> {
                        player("I don't suppose you could give me any hints?")
                        stage = 22
                    }

                    2 -> {
                        player("That's less competition for me then.")
                        stage = 30
                    }
                }

            6 -> {
                options(
                    "Very fair, I'll buy that rod!",
                    "That's too rich for me, I'll go to Catherby.",
                )
                stage++
            }

            7 ->
                when (buttonId) {
                    1 -> {
                        player("Very fair, I'll buy that rod!")
                        stage =
                            if (player.inventory.containsItem(Item(995, 5))) {
                                42
                            } else {
                                60
                            }
                    }

                    2 -> {
                        player("That's too rich for me, I'll go to Catherby.")
                        stage = 44
                    }
                }

            9 -> end()
            10 -> {
                npc(
                    "Well, when I were a young man we used",
                    "to take fishing trips over to Catherby.",
                    "The fishing over there, now that was something!",
                )
                stage++
            }

            11 -> {
                npc(
                    "Anyway, we decided to do a bit of fishing with our nets,",
                    "I wasn't having the best of days turning up",
                    "nothing but old boots and bits of seaweed.",
                )
                stage++
            }

            12 -> {
                npc(
                    "Then my net suddenly got really heavy!",
                    "I pulled it up... To my amazement",
                    "I'd caught this little chest thing!",
                )
                stage++
            }

            13 -> {
                npc(
                    "Even more amazing was when I opened it",
                    "it contained a diamond the size of a radish!",
                    "That's the best catch I've ever had!",
                )
                stage = 0
            }

            20 -> {
                npc("Ah... the Hemenster fishing competition...")
                stage++
            }

            21 -> {
                npc(
                    " I know all about that... I won that four years straight!",
                    "I'm too old for that lark now though...",
                )
                stage = 4
            }

            22 -> {
                npc("Well, you sometimes get these really big fish in the", "water just by the outflow pipes.")
                stage++
            }

            23 -> {
                npc("I think they're some kind of carp...")
                stage++
            }

            24 -> {
                npc("Try to get a spot round there. ", "The best sort of bait for them is red vine worms.")
                stage++
            }

            25 -> {
                npc(
                    "I used to get those from McGrubor's wood, north of",
                    "here. Just dig around in the red vines up there but be",
                    "careful of the guard dogs.",
                )
                stage++
            }

            26 -> {
                player(
                    "There's this weird creepy guy who says he's not a",
                    "vampire using that spot. He keeps winning too.",
                )
                stage++
            }

            27 -> {
                npc(
                    "Ahh well, I'm sure you'll find something to put him off.",
                    "After all, there must be a kitchen around here with",
                    "some garlic in it, perhaps in Seers Village or Ardougne.",
                    "If he's pretending to be a vampire then he can pretend",
                )
                stage++
            }

            28 -> {
                npc("to be scared of garlic!")
                stage++
            }

            29 -> {
                player("You're right! Thanks Jack!")
                stage = 9
            }

            30 -> {
                npc(
                    "Why you young whippersnapper!",
                    "If I was twenty years younger I'd show you something that's for sure!",
                )
                stage = 0
            }

            35 -> {
                npc("Sigh... Young people - always in such a rush. ")
                stage = 9
            }

            40 -> {
                npc(
                    "Of course you can young man. Let's see now...",
                    "I think 5 gold is a fair price for a rod which",
                    "has won the Fishing contest before eh?",
                )
                stage = 6
            }

            42 -> {
                npc("Excellent choice!")
                player.inventory.remove(Item(995, 5))
                player.inventory.add(FishingContest.FISHING_ROD, player)
                stage = 9
            }

            44 -> {
                npc("If you're sure... passing up an opportunity of a lifetime you are.")
                stage = 9
            }

            50 -> {
                npc(
                    "Of course! Let me see now... You'll need a rod and bait.",
                    "You can fish with a net too, but not in the competition.",
                )
                stage++
            }

            51 -> {
                player("Ok... I think I can get those in Catherby.")
                stage++
            }

            52 -> {
                npc(
                    "Then simply find yourself a fishing spot, ",
                    "either in the competition near here, or wherever you can.",
                    "I recommend net fishing in Catherby.",
                )
                stage++
            }

            53 -> {
                npc("Net or Lure the fish in the fishing spot", "by clicking on it and then be patient...")
                stage++
            }

            54 -> {
                player("It's that simple?")
                stage++
            }

            55 -> {
                npc("Yep! Go get em tiger.")
                stage = 0
            }

            60 -> {
                player("I don't have enough money for that,", "I'll go get some and come back.")
                stage++
            }

            61 -> {
                npc("Right you are. I'll be here. ")
                stage = 9
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GRANDPA_JACK_230)
    }
}

package content.region.desert.quest.desertrescue.dialogue

import content.region.desert.quest.desertrescue.TouristTrap
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import org.rs.consts.Quests

class AlShabimDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
        when (quest!!.getStage(player)) {
            else -> npc("Hello Effendi!")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            54 ->
                when (stage) {
                    0 ->
                        if (player.inventory.containsItem(TouristTrap.PROTOTYPE_DART)) {
                            npc("Wonderful, I see you have made the new weapon!")
                            stage = 20
                        } else {
                            if (!player.hasItem(TouristTrap.BEDABIN_KEY)) {
                                player("I seemed to have lost the key...")
                                stage++
                            } else {
                                npc("Have you figured out the plans yet?")
                                stage = 3
                            }
                        }

                    1 -> {
                        npc("Ahh Effendi, it's a good thing I have a spare!")
                        stage++
                    }

                    2 -> {
                        player.inventory.add(TouristTrap.BEDABIN_KEY, player)
                        interpreter.sendItemMessage(TouristTrap.BEDABIN_KEY, "Al Shabim gives you a key.")
                        stage = 11
                    }

                    3 -> {
                        player("No, not yet.")
                        stage = 11
                    }

                    11 -> end()
                    20 -> {
                        interpreter.sendItemMessage(
                            TouristTrap.PROTOTYPE_DART,
                            "You show Al Shabim the prototype dart.",
                        )
                        stage++
                    }

                    21 -> {
                        npc("This is truly fantastic Effendi!")
                        stage++
                    }

                    22 ->
                        if (player.inventory.containsItem(TouristTrap.TECHNICAL_PLANS)) {
                            npc("We will take the technical plans for the weapon as well.")
                            stage++
                        } else {
                            player.inventory.remove(TouristTrap.TECHNICAL_PLANS)
                            npc(
                                "We are forever grateful for this gift. My advisor have",
                                "discovered some secrets which we will share with you.",
                            )
                            stage = 25
                        }

                    23 -> {
                        interpreter.sendItemMessage(
                            TouristTrap.TECHNICAL_PLANS,
                            "You hand over the technical plans for the weapon.",
                        )
                        stage++
                    }

                    24 -> {
                        player.inventory.remove(TouristTrap.TECHNICAL_PLANS)
                        npc(
                            "We are forever grateful for this gift. My advisor have",
                            "discovered some secrets which we will share with you.",
                        )
                        stage++
                    }

                    25 -> {
                        interpreter.sendDialogue(
                            "Al Shabim's advisors show you some advanced techniques for making",
                            "the new weapon.",
                        )
                        stage++
                    }

                    26 -> {
                        npc(
                            "Please accept this selection of six bronze throwing darts",
                            "as a token of our appreciation.",
                        )
                        stage++
                    }

                    27 -> {
                        player.inventory.add(BRONZE_DARTS, player)
                        interpreter.sendItemMessage(806, "You receive six bronze throwing darts from Al Shabim.")
                        stage = if (player.inventory.containsItem(TouristTrap.BEDABIN_KEY)) 28 else 29
                    }

                    28 -> {
                        player.inventory.remove(TouristTrap.BEDABIN_KEY)
                        npc("I'll take that key off your hands as well effendi! Many", "thanks.")
                        stage++
                    }

                    29 -> {
                        quest!!.setStage(player, 60)
                        player.inventory.remove(TouristTrap.PROTOTYPE_DART)
                        player.inventory.add(TouristTrap.TENTI_PINEAPPLE)
                        interpreter.sendItemMessage(
                            806,
                            "<col=08088A>*** Dart Construction ***",
                            "Congratulations! You can now construct darts.",
                        )
                        stage++
                    }
                }

            50 ->
                when (stage) {
                    0 -> {
                        npc(
                            "I am Al Shabim, greetings on behalf of the Bedabin",
                            "nomads. Now... what can I do for you?",
                        )
                        stage++
                    }

                    1 -> {
                        player("I am looking for a pineapple.")
                        stage++
                    }

                    2 -> {
                        npc(
                            "Oh yes, well that is interesting. Our sweet pineapples",
                            "are renowned throughout the whole of Kharid! and I'll",
                            "give you one if you do me a favour?",
                        )
                        stage++
                    }

                    3 -> {
                        player("Oh yes?")
                        stage++
                    }

                    4 -> {
                        npc(
                            "Captain Siad at the mining camp is holding some secret",
                            "information. It is very important to us and we would",
                            "like you to get it for us. It gives details of an",
                            "interesting, yet ancient weapon.",
                        )
                        stage++
                    }

                    5 -> {
                        npc(
                            "We would gladly share this information with you. All",
                            "you have to do is gain access to his private room",
                            "upstairs. We have a key for the chest that contains this",
                            "information. Are you interested in our deal?",
                        )
                        stage++
                    }

                    6 -> {
                        player("Yes, I'm interested.")
                        stage++
                    }

                    7 -> {
                        npc("That's great Effendi!")
                        stage++
                    }

                    8 -> {
                        npc("Here is a copy of the key that should give you access", "to the chest.")
                        stage++
                    }

                    9 -> {
                        quest!!.setStage(player, 51)
                        player.inventory.add(TouristTrap.BEDABIN_KEY, player)
                        interpreter.sendItemMessage(TouristTrap.BEDABIN_KEY, "Al Shabim gives you a key.")
                        stage++
                    }
                }

            51, 52, 53 ->
                when (stage) {
                    0 ->
                        if (player.inventory.containsItem(TouristTrap.TECHNICAL_PLANS)) {
                            npc(
                                "Aha! I see you have the plans. This is great! However,",
                                "these plans do indeed look very technical. My people",
                                "have further need of your skills.",
                            )
                            stage = 20
                        } else {
                            if (!player.hasItem(TouristTrap.BEDABIN_KEY)) {
                                player("I seemed to have lost the key...")
                                stage++
                            } else {
                                npc("Have you found the plans yet?")
                                stage = 3
                            }
                        }

                    1 -> {
                        npc("Ahh Effendi, it's a good thing I have a spare!")
                        stage++
                    }

                    2 -> {
                        player.inventory.add(TouristTrap.BEDABIN_KEY, player)
                        interpreter.sendItemMessage(TouristTrap.BEDABIN_KEY, "Al Shabim gives you a key.")
                        stage = 11
                    }

                    3 -> {
                        player("No, sorry. I'm still looking for them.")
                        stage++
                    }

                    4 -> {
                        npc("Okay, Effendi!")
                        stage = 11
                    }

                    10 -> {
                        npc(
                            "Bring us back the plans inside the chest, they should be",
                            "sealed. All haste to you Effendi!",
                        )
                        stage = 11
                    }

                    11 -> end()
                    20 -> {
                        npc(
                            "If you can help us to manufacture this item, we will",
                            "share its secret with you. Does this deal interest you",
                            "effendi?",
                        )
                        stage++
                    }

                    21 -> {
                        player("Yes, I'm very interested.")
                        stage++
                    }

                    22 -> {
                        npc(
                            "Okay Effendi, you need to follow the plans. You will",
                            "need some special tools for this...  There is an anvil in",
                            "the other tent. You have my permission to use it, but",
                            "show the plans to the guard.",
                        )
                        stage++
                    }

                    23 -> {
                        quest!!.setStage(player, 54)
                        end()
                    }
                }

            100, 60, 70, 80, 90 ->
                when (stage) {
                    0 -> {
                        if (quest!!.getStage(player) == 60 && !player.hasItem(TouristTrap.TENTI_PINEAPPLE)) {
                            player.inventory.add(TouristTrap.TENTI_PINEAPPLE, player)
                            interpreter.sendItemMessage(
                                TouristTrap.TENTI_PINEAPPLE,
                                "You receive a tasty looking pineapple from Al Shabim.",
                            )
                            stage++
                            stage = 32
                        }
                        npc("Many thanks with helping previously Effendi!")
                        stage++
                    }

                    30 -> {
                        npc("Oh, and here is your pineapple!")
                        stage++
                    }

                    31 -> {
                        interpreter.sendItemMessage(
                            TouristTrap.TENTI_PINEAPPLE,
                            "You receive a tasty looking pineapple from Al Shabim.",
                        )
                        stage++
                    }

                    32 -> end()
                    1 -> end()
                }

            else ->
                when (stage) {
                    0 -> {
                        player("Hi")
                        stage++
                    }

                    1 -> end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(832)
    }

    companion object {
        private val BRONZE_DARTS = Item(806, 6)
    }
}

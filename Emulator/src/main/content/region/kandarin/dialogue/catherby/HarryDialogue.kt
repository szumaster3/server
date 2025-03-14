package content.region.kandarin.dialogue.catherby

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class HarryDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private fun needsFish(): Boolean {
        return player.inventory.containsAtLeastOneItem(FISHBOWL_SEAWEED)
    }

    private fun needsSeaWeed(): Boolean {
        return player.inventory.containsAtLeastOneItem(FISHBOWL_WATER)
    }

    private fun needsFood(): Boolean {
        return player.inventory.containsAtLeastOneItem(
            intArrayOf(FISHBOWL_SEAWEED, FISHBOWL_BLUE, FISHBOWL_GREEN, FISHBOWL_SPINE),
        )
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.HAPPY,
            "Welcome! You can buy Fishing equipment at my store.",
            "We'll also give you a good price for any fish that you",
            "catch.",
        )
        stage =
            if (needsFish() || needsSeaWeed()) {
                10
            } else if (needsFood()) {
                20
            } else {
                0
            }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            999 -> end()
            0 -> {
                options("Let's see what you've got, then.", "Sorry, I'm not interested.")
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("Let's see what you've got, then.")
                        stage++
                    }

                    2 -> {
                        player("Sorry, I'm not interested.")
                        stage = 999
                    }
                }

            2 -> {
                end()
                npc.openShop(player)
            }

            10 -> {
                options(
                    "Let's see what you've got, then.",
                    "Can I get a fish for this bowl?",
                    "Do you have any fishfood?",
                    "Sorry, I'm not interested.",
                )
                stage++
            }

            11 ->
                when (buttonId) {
                    1 -> {
                        player("Let's see what you've got, then.")
                        stage = 2
                    }

                    2 -> {
                        player("Can I get a fish for this bowl?")
                        stage = 30
                    }

                    3 -> {
                        player("Do you have any fishfood?")
                        stage = 40
                    }

                    4 -> {
                        player("Sorry, I'm not interested.")
                        stage = 999
                    }
                }

            20 -> {
                options("Let's see what you've got, then.", "Do you have any fishfood?", "Sorry, I'm not interested.")
                stage++
            }

            21 ->
                when (buttonId) {
                    1 -> {
                        player("Let's see what you've got, then.")
                        stage = 2
                    }

                    2 -> {
                        player("Do you have any fishfood?")
                        stage = 40
                    }

                    3 -> {
                        player("Sorry, I'm not interested.")
                        stage = 999
                    }
                }

            30 ->
                if (!needsFish()) {
                    npc("Sorry, you need to put some seaweed into the bowl", "first.")
                    stage++
                } else {
                    npc("Yes you can!")
                    stage = 33
                }

            31 -> {
                player("Seaweed?")
                stage++
            }

            32 -> {
                npc("Yes, the fish seem to like it. Come and see me when", "you have put some in the bowl.")
                stage = 999
            }

            33 -> {
                npc(
                    "I can see that you have a nicely filled fishbowl there to",
                    "use, and you can catch a fish from my aquarium if",
                    "you want.",
                )
                stage++
            }

            34 -> {
                npc("You will need a special net to do this though, and I sell", "them for 10 gold.")
                stage++
            }

            35 -> {
                options("I'll take it!", "No thanks, later maybe")
                stage++
            }

            36 ->
                when (buttonId) {
                    1 -> {
                        player("I'll take it!")
                        stage++
                    }

                    2 -> {
                        player("No thanks, later maybe")
                        stage = 999
                    }
                }

            37 ->
                stage =
                    if (player.inventory.getAmount(995) >= 10) {
                        if (!player.inventory.hasSpaceFor(Item(TINY_NET))) {
                            npc("Here you... oh.")
                            38
                        } else {
                            npc("Here you go!")
                            if (player.inventory.remove(Item(995, 10))) {
                                player.inventory.add(Item(TINY_NET))
                                player.packetDispatch.sendMessage("Harry sells you a tiny net!")
                            }
                            999
                        }
                    } else {
                        npc("Well, I'll be happy to give you the net once you", "have the cash, but not before!")
                        999
                    }

            38 -> {
                npc("Well, you don't seem to have any free space for", "this right now. Come back later when you do.")
                stage = 999
            }

            40 -> {
                npc("Sorry, I'm all out. I used up the last of it feeding the", "fish in the aquarium.")
                stage++
            }

            41 -> {
                npc(
                    "I have some empty boxes though, they have the",
                    "ingredients written on the back. I'm sure if you pick up",
                    "a pestle and mortar you will be able to make your own.",
                )
                stage++
            }

            42 -> {
                npc("Here. I can hardly charge you for an empty box.")
                player.inventory.add(Item(Items.AN_EMPTY_BOX_6675))
                if (needsFood() && !needsSeaWeed()) {
                    stage++
                } else {
                    stage = 999
                }
            }

            43 -> {
                npc("Take good care of that fish!")
                stage = 999
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return HarryDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HARRY_576)
    }

    companion object {
        private const val FISHBOWL_EMPTY = Items.FISHBOWL_6667
        private const val FISHBOWL_WATER = Items.FISHBOWL_6668
        private const val FISHBOWL_SEAWEED = Items.FISHBOWL_6669
        private const val FISHBOWL_BLUE = Items.FISHBOWL_6670
        private const val FISHBOWL_GREEN = Items.FISHBOWL_6671
        private const val FISHBOWL_SPINE = Items.FISHBOWL_6672
        private const val TINY_NET = Items.TINY_NET_6674
    }
}

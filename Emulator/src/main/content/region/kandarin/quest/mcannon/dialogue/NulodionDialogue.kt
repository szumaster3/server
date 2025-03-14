package content.region.kandarin.quest.mcannon.dialogue

import content.region.kandarin.quest.mcannon.DwarfCannon
import core.api.setVarp
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import org.rs.consts.NPCs
import org.rs.consts.Quests

class NulodionDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.DWARF_CANNON)
        when (quest!!.getStage(player)) {
            70 -> player("Hello there.")
            else -> player("Hello again.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            70 ->
                when (stage) {
                    0 -> {
                        npc(FaceAnim.OLD_NORMAL, "Can I help you?")
                        stage++
                    }

                    1 -> {
                        player("Captain Lawgof sent me. He's having trouble with his", "cannon.")
                        stage++
                    }

                    2 -> {
                        npc(FaceAnim.OLD_NORMAL, "Of course, we forgot to send the ammo mould!")
                        stage++
                    }

                    3 -> {
                        player("It fires a mould?")
                        stage++
                    }

                    4 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Don't be silly - the ammo's made by using a mould.",
                            "Here, take these to him. The instructions explain",
                            "everything.",
                        )
                        stage++
                    }

                    5 -> {
                        player("That's great, thanks.")
                        stage++
                    }

                    6 -> {
                        npc(FaceAnim.OLD_NORMAL, "Thank you, adventurer. The Dwarf Black Guard will", "remember this.")
                        stage++
                    }

                    7 -> {
                        setVarp(player, 0, 10)
                        quest!!.setStage(player, 80)
                        player.sendMessage("The Cannon Engineer gives you some notes and a mould.")
                        player.inventory.add(DwarfCannon.NULODION_NOTES, player)
                        player.inventory.add(DwarfCannon.MOULD, player)
                        end()
                    }
                }

            80 ->
                when (stage) {
                    0 -> {
                        if (!player.hasItem(DwarfCannon.NULODION_NOTES)) {
                            player("I've lost the notes.")
                            stage = 100
                        }
                        if (!player.hasItem(DwarfCannon.MOULD)) {
                            player("I've lost the cannonball mould.")
                            stage = 102
                        }
                        npc(FaceAnim.OLD_NORMAL, "So has the Captain figured out how to work the cannon", "yet?")
                        stage++
                    }

                    1 -> {
                        player("Not yet, but I'm sure he will.")
                        stage++
                    }

                    2 -> {
                        npc(FaceAnim.OLD_NORMAL, "If you can get those items to him it'll be a great help.")
                        stage++
                    }

                    3 -> end()
                    100 -> {
                        npc(FaceAnim.OLD_NORMAL, "Here, take these...")
                        stage++
                    }

                    101 -> {
                        player.sendMessage("The Cannon Engineer gives you some more notes.")
                        player.inventory.add(DwarfCannon.NULODION_NOTES, player)
                        end()
                    }

                    102 -> {
                        npc(FaceAnim.OLD_NORMAL, "Deary me, you are trouble. Here, take this one.")
                        stage++
                    }

                    103 -> {
                        player.sendMessage("The Cannon Engineer gives you another mould.")
                        player.inventory.add(DwarfCannon.MOULD, player)
                        end()
                    }
                }

            else ->
                when (stage) {
                    0 -> {
                        npc(FaceAnim.OLD_NORMAL, "Hello traveller, how's things?")
                        stage++
                    }

                    1 -> {
                        player("Not bad thanks, yourself?")
                        stage++
                    }

                    2 -> {
                        npc(FaceAnim.OLD_NORMAL, "I'm good, just working hard as usual...")
                        stage++
                    }

                    3 -> {
                        options(
                            "I was hoping you might sell a cannon?",
                            "Well, take care of yourself then.",
                            "I want to know more about the cannon.",
                            "I've lost my cannon.",
                        )
                        stage++
                    }

                    4 ->
                        when (buttonId) {
                            1 -> {
                                player("I was hoping you might sell me a cannon?")
                                stage = 10
                            }

                            2 -> {
                                player("Well, take care of yourself then.")
                                stage = 20
                            }

                            3 -> {
                                player("I want to know more about the cannon.")
                                stage = 30
                            }

                            4 -> {
                                player("I've lost my cannon.")
                                stage = 40
                            }
                        }

                    10 -> {
                        npc(FaceAnim.OLD_NORMAL, "Hmmmmmmm...")
                        stage++
                    }

                    11 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "I shouldn't really, but as you helped us so much, well, I",
                            "could sort something out. I'll warn you though, they",
                            "don't come cheap!",
                        )
                        stage++
                    }

                    12 -> {
                        player("How much?")
                        stage++
                    }

                    13 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "For the full setup, 750,000 coins. Or I can sell you",
                            "the separate parts... but it'll cost extra!",
                        )
                        stage++
                    }

                    14 -> {
                        player("That's not cheap!")
                        stage++
                    }

                    15 -> {
                        options(
                            "Okay, I'll take a cannon please.",
                            "Can I look at the separate parts please?",
                            "Sorry, that's too much for me.",
                            "Have you any ammo or instructions to sell?",
                        )
                        stage++
                    }

                    16 ->
                        when (buttonId) {
                            1 -> {
                                player("Okay, I'll take a cannon please.")
                                stage = 110
                            }

                            2 -> {
                                player("Can I look at the separate parts please?")
                                stage = 120
                            }

                            3 -> {
                                player("Sorry, that's too much for me.")
                                stage = 130
                            }

                            4 -> {
                                player("Have you any ammo or instructions to sell?")
                                stage = 140
                            }
                        }

                    20 -> {
                        npc(FaceAnim.OLD_NORMAL, "Indeed I will adventurer.")
                        stage++
                    }

                    21 -> end()
                    30 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "There's only so much I can tell you, adventurer.",
                            "We've been working on this little beauty for some time",
                            "now.",
                        )
                        stage++
                    }

                    31 -> {
                        player("Is it effective?")
                        stage++
                    }

                    32 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "In short bursts it's very effective, the most destructive",
                            "weapon to date. The cannon automatically targets",
                            "monsters close by. You just have to make the ammo",
                            "and let rip.",
                        )
                        stage++
                    }

                    33 -> end()
                    40 -> {
                        npc(FaceAnim.OLD_NORMAL, "That's unfortunate... but don't worry, I can sort you", "out.")
                        stage++
                    }

                    41 -> {
                        if (player.getSavedData().activityData.isLostCannon) {
                            npc(FaceAnim.OLD_NORMAL, "There you go, take better care next time.")
                            stage = 43
                        }
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Oh dear, I'm only allowed to replace cannons that were",
                            "stolen in reward. I'm sorry, but you'll have to buy a",
                            "new set.",
                        )
                        stage++
                    }

                    43 -> {
                        player.getSavedData().activityData.isLostCannon = false
                        for (i in CANNON_PIECES) {
                            player.inventory.add(i, player)
                        }
                        end()
                    }

                    42 -> end()
                    110 -> {
                        npc(FaceAnim.OLD_NORMAL, "Okay then, but keep it quiet... This thing's top secret!")
                        stage++
                    }

                    111 -> {
                        if (!player.inventory.contains(995, 750000)) {
                            player("Oops, I don't have enough money.")
                            stage++
                        }
                        if (player.inventory.remove(Item(995, 750000))) {
                            for (i in CANNON_PIECES) {
                                player.inventory.add(i, player)
                            }
                        }
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "There you go adventurer, I hope you enjoy this",
                            "fine work of craftsmanship.",
                        )
                        stage = 113
                    }

                    112 -> {
                        npc(FaceAnim.OLD_NORMAL, "Sorry, I can't go any lower than that.")
                        stage++
                    }

                    113 -> end()
                    120 -> {
                        npc(FaceAnim.OLD_NORMAL, "Of course!")
                        stage++
                    }

                    121 -> {
                        end()
                        npc.openShop(player)
                    }

                    130 -> {
                        npc(FaceAnim.OLD_NORMAL, "Fair enough, it's too much for most of us.")
                        stage++
                    }

                    131 -> end()
                    140 -> {
                        npc(FaceAnim.OLD_NORMAL, "Of course!")
                        stage = 121
                    }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NULODION_209)
    }

    companion object {
        private val CANNON_PIECES = arrayOf(Item(6), Item(8), Item(10), Item(12))
    }
}

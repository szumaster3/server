package content.region.desert.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SimonTempletonDialogue(
    player: Player? = null,
) : Dialogue(player) {
    val ARTIFACTS =
        arrayOf(
            arrayOf(Item(Items.POTTERY_SCARAB_9032), Item(Items.POTTERY_STATUETTE_9036), Item(Items.IVORY_COMB_9026)),
            arrayOf(Item(Items.STONE_SEAL_9042), Item(Items.STONE_SCARAB_9030), Item(Items.STONE_STATUETTE_9038)),
            arrayOf(Item(Items.GOLD_SEAL_9040), Item(Items.GOLDEN_SCARAB_9028), Item(Items.GOLDEN_STATUETTE_9034)),
        )

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC

        if (args.size == 4) {
            if (args[3] as Int == 9044 || args[3] as Int == 9046 || args[3] as Int == 9048 || args[3] as Int == 9050) {
                npc(
                    "You sellin' me this gold colored",
                    "stick thing. Looks fake to me.",
                    "I'll give you 100 gold for it.",
                )
                stage = 30
                return true
            }
        }
        npc("G'day, mate. Got any new", "pyramid artefacts for me?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        var hasArtefacts = false
        var hasPyramidTopper = false
        val goldReward = (1000 + getStatLevel(player, 16) / 99.0 * 9000).toInt()
        when (stage) {
            0 -> {
                var i = 0
                while (i < ARTIFACTS.size) {
                    var j = 0
                    while (j < 3) {
                        if (player.inventory.containsItem(ARTIFACTS[i][j])) {
                            hasArtefacts = true
                            break
                        }
                        j++
                    }
                    i++
                }
                hasPyramidTopper = player.inventory.containsItem(Item(6970))
                if (hasPyramidTopper) {
                    player("Yes, actually. The top of that pyramid.")
                    stage = 6
                }
                if (hasArtefacts && !hasPyramidTopper) {
                    player("Why, yes I do!")
                    stage = 1
                }
                player("No, I haven't.")
                stage = 3
            }

            1 -> {
                npc("Excellent! I would like to buy them.")
                stage = 2
            }

            2 -> {
                interpreter.sendOptions("Sell artefacts?", "Yes, I need money!!", "No, I'll hang onto them.")
                stage = 10
            }

            3 -> {
                npc("Well, keep my offer in mind", "if you do find one.")
                stage = 4
            }

            4 -> {
                player("I will. Goodbye, Simon.")
                stage = 5
            }

            5 -> {
                npc("Bye, mate.")
                stage = 999
            }

            6 -> {
                npc("Hmmm, very nice. I'll buy them for $goldReward each.")
                stage = 7
            }

            7 -> {
                options("Sounds good!", "No thanks.")
                stage = 8
            }

            8 ->
                when (buttonId) {
                    1 -> {
                        val pyramidTopsInInv = amountInInventory(player, Items.PYRAMID_TOP_6970)
                        if (removeItem<Item>(
                                player,
                                Item(Items.PYRAMID_TOP_6970, pyramidTopsInInv),
                                Container.INVENTORY,
                            )
                        ) {
                            addItem(player, Items.COINS_995, goldReward * pyramidTopsInInv, Container.INVENTORY)
                        }
                        end()
                    }

                    2 -> {
                        npc("Have it your way.")
                        stage = 9
                    }
                }

            9 -> end()
            10 ->
                when (buttonId) {
                    1 -> {
                        setTitle(player, 4)
                        interpreter.sendOptions(
                            "Which ones do you want to sell?",
                            "Clay and Ivory",
                            "Stone",
                            "Gold",
                            "Sell them all!",
                        )
                        stage = 20
                    }

                    2 -> {
                        npc("Aww, alright. Well, keep my", "offer in mind, will ya?")
                        player("Sure thing, Simon.")
                        npc("Thanks, mate.")
                        stage = 999
                    }
                }

            20 ->
                when (buttonId) {
                    1 -> {
                        end()
                        var j = 0
                        while (j < 28) {
                            when (player.inventory.getId(j)) {
                                9032 -> {
                                    player.inventory.remove(Item(9032), j, true)
                                    player.inventory.add(Item(995, 75))
                                }

                                9036 -> {
                                    player.inventory.remove(Item(9036), j, true)
                                    player.inventory.add(Item(995, 100))
                                }

                                9026 -> {
                                    player.inventory.remove(Item(9026), j, true)
                                    player.inventory.add(Item(995, 50))
                                }
                            }
                            j++
                        }
                        stage = 999
                    }

                    2 -> {
                        end()
                        var j = 0
                        while (j < 28) {
                            when (player.inventory.getId(j)) {
                                9042 -> {
                                    player.inventory.remove(Item(9042), j, true)
                                    player.inventory.add(Item(995, 150))
                                }

                                9030 -> {
                                    player.inventory.remove(Item(9030), j, true)
                                    player.inventory.add(Item(995, 175))
                                }

                                9038 -> {
                                    player.inventory.remove(Item(9038), j, true)
                                    player.inventory.add(Item(995, 200))
                                }
                            }
                            j++
                        }
                        stage = 999
                    }

                    3 -> {
                        end()
                        var j = 0
                        while (j < 28) {
                            when (player.inventory.getId(j)) {
                                9040 -> {
                                    player.inventory.remove(Item(9040), j, true)
                                    player.inventory.add(Item(995, 750))
                                }

                                9028 -> {
                                    player.inventory.remove(Item(9028), j, true)
                                    player.inventory.add(Item(995, 1000))
                                }

                                9034 -> {
                                    player.inventory.remove(Item(9034), j, true)
                                    player.inventory.add(Item(995, 1250))
                                }
                            }
                            j++
                        }
                        stage = 999
                    }

                    4 -> {
                        end()
                        var j = 0
                        while (j < 28) {
                            when (player.inventory.getId(j)) {
                                9040 -> {
                                    player.inventory.remove(Item(9040), j, true)
                                    player.inventory.add(Item(995, 750))
                                }

                                9028 -> {
                                    player.inventory.remove(Item(9028), j, true)
                                    player.inventory.add(Item(995, 1000))
                                }

                                9034 -> {
                                    player.inventory.remove(Item(9034), j, true)
                                    player.inventory.add(Item(995, 1250))
                                }

                                9042 -> {
                                    player.inventory.remove(Item(9042), j, true)
                                    player.inventory.add(Item(995, 150))
                                }

                                9030 -> {
                                    player.inventory.remove(Item(9030), j, true)
                                    player.inventory.add(Item(995, 175))
                                }

                                9038 -> {
                                    player.inventory.remove(Item(9038), j, true)
                                    player.inventory.add(Item(995, 200))
                                }

                                9032 -> {
                                    player.inventory.remove(Item(9032), j, true)
                                    player.inventory.add(Item(995, 75))
                                }

                                9036 -> {
                                    player.inventory.remove(Item(9036), j, true)
                                    player.inventory.add(Item(995, 100))
                                }

                                9026 -> {
                                    player.inventory.remove(Item(9026), j, true)
                                    player.inventory.add(Item(995, 50))
                                }
                            }
                            j++
                        }
                        stage = 999
                    }
                }

            30 -> {
                player(
                    "What! This is a genuine pharaoh's scepter - made out of",
                    "solid gold and finely jewelled with precious gems",
                    " by the finest craftsmen in the area.",
                )
                stage = 31
            }

            31 -> {
                npc(
                    "Strewth! I can tell a pile of croc when I hear it!",
                    "You've got the patter mate, but I'm no mug.",
                    "That's a fake.",
                )
                stage = 32
            }

            32 -> {
                player("It has magical powers!")
                stage = 33
            }

            33 -> {
                npc(
                    "Oh, magical powers... yeah yeah yeah. Heard it all before",
                    "mate. I'll give you 100 gold, or some 'magic beans'.",
                    "Take it or leave it.",
                )
                stage = 34
            }

            34 -> {
                player("I don't think so! I'll find someone who'll give me", "what it's worth.")
                stage = 35
            }

            35 -> {
                npc("Suit yerself...")
                stage = 999
            }

            999 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIMON_TEMPLETON_3123)
    }
}

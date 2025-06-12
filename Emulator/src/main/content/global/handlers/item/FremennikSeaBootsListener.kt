package content.global.handlers.item

import content.region.fremennik.rellekka.plugin.LyreTeleport
import core.ServerStore.Companion.getBoolean
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs.consts.Items
import org.rs.consts.NPCs

val seaBoots = intArrayOf(
    Items.FREMENNIK_SEA_BOOTS_1_14571,
    Items.FREMENNIK_SEA_BOOTS_2_14572,
    Items.FREMENNIK_SEA_BOOTS_3_14573
)

val enchantedLyre = intArrayOf(
    Items.ENCHANTED_LYRE1_3691,
    Items.ENCHANTED_LYRE2_6125,
    Items.ENCHANTED_LYRE3_6126,
    Items.ENCHANTED_LYRE4_6127,
    Items.ENCHANTED_LYRE5_14590,
    Items.ENCHANTED_LYRE6_14591,
)

class FremennikSeaBootsListener : InteractionListener {
    override fun defineListeners() {
        on(seaBoots, IntType.ITEM, "operate") { player, _ ->
            openDialogue(player, SeaBootsDialogue())
            return@on true
        }
    }
}

class SeaBootsDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.FOSSEGRIMEN_1273)

        val bootsTier = when {
            inEquipmentOrInventory(player!!, Items.FREMENNIK_SEA_BOOTS_3_14573) -> 3
            inEquipmentOrInventory(player!!, Items.FREMENNIK_SEA_BOOTS_2_14572) -> 2
            inEquipmentOrInventory(player!!, Items.FREMENNIK_SEA_BOOTS_1_14571) -> 1
            else -> 0
        }

        when (stage) {
            0 -> if (bootsTier > 0) {
                setTitle(player!!, bootsTier + 2)
                val options = mutableListOf("Contact the Fossegrimen.")
                when (bootsTier) {
                    1 -> options += listOf("Explain benefits.", "Cancel.")
                    2 -> options += listOf("Free lyre teleport.", "Explain benefits.", "Cancel.")
                    3 -> options += listOf("Free lyre teleport.", "Lyre teleport destination.", "Explain benefits.", "Cancel.")
                }
                sendDialogueOptions(player!!, "What would you like to do?", *options.toTypedArray())
                stage++
            }

            1 -> when (buttonID) {
                1 -> {
                    npc(FaceAnim.FRIENDLY, "Good day, ${player!!.username}. Do you want to make an", "offering?")
                    stage = 4
                }
                2 -> {
                    when (bootsTier) {
                        1 -> {
                            player!!.dialogueInterpreter.sendItemMessage(
                                Items.FREMENNIK_SEA_BOOTS_1_14571,
                                "Your Fremennik sea boots will bring you certain",
                                "benefits within the Fremennik area."
                            )
                            stage = 7
                        }
                        2, 3 -> {
                            player!!.dialogueInterpreter.sendItemMessage(
                                Items.ENCHANTED_LYRE1_3691,
                                "You may use lyre once per day without draining",
                                "any of its enchantment."
                            )
                            stage = 8
                        }
                    }
                }
                3 -> {
                    when (bootsTier) {
                        1 -> end()
                        2 -> {
                            player!!.dialogueInterpreter.sendItemMessage(
                                Items.FREMENNIK_SEA_BOOTS_2_14572,
                                "Your Fremennik sea boots will bring you certain",
                                "benefits within the Fremennik area."
                            )
                            stage = 10
                        }
                        3 -> {
                            val destination = if (getAttribute(player!!, LyreTeleport.LYRE_TELEPORT_ALT, false)) "Waterbirth Island." else "Relleka."
                            sendItemDialogue(player!!, Items.LYRE_3689, "Your lyre currently teleports you to $destination")
                            stage = 13
                        }
                    }
                }
                4 -> {
                    when (bootsTier) {
                        2 -> end()
                        3 -> {
                            player!!.dialogueInterpreter.sendItemMessage(
                                Items.FREMENNIK_SEA_BOOTS_3_14573,
                                "Your Fremennik sea boots will bring you certain",
                                "benefits within the Fremennik area."
                            )
                            stage = 15
                        }
                    }
                }
                5 -> if (bootsTier == 3) end()
            }

            4 -> {
                npc(FaceAnim.NEUTRAL,
                    "Remember that you will gain a greater enchantment",
                    "from offerings if you bring them to my altar.")
                stage++
            }

            5 -> {
                setTitle(player!!, 2)
                val option1 = if (inInventory(player!!, Items.RAW_BASS_363)) "A raw bass." else "A raw shark."
                sendDialogueOptions(player!!, "What do you offer?", option1, "Nothing at the moment.")
                stage++
            }

            6 -> when (buttonID) {
                1 -> {
                    if (inInventory(player!!, Items.RAW_BASS_363) &&
                        (inEquipmentOrInventory(player!!, Items.RING_OF_CHAROSA_6465) || inEquipmentOrInventory(player!!, Items.RING_OF_CHAROS_4202))) {
                        npcl(FaceAnim.HALF_GUILTY, "A raw bass? You should know that is not a worthy offering, outlander.")
                        stage = 0
                    } else if (hasAnItem(player!!, *enchantedLyre).container == player!!.inventory) {
                        player!!.dialogueInterpreter.sendDialogue(
                            "All lyre charges must be used up before it will allow a charge",
                            "to the lyre."
                        )
                        stage = 0
                    } else if (inInventory(player!!, Items.RAW_SHARK_383) &&
                        hasAnItem(player!!, Items.ENCHANTED_LYRE_3690).container == player!!.inventory &&
                        removeItem(player!!, Items.ENCHANTED_LYRE_3690, Container.INVENTORY) &&
                        removeItem(player!!, Items.RAW_SHARK_383, Container.INVENTORY)) {
                        npc(FaceAnim.FRIENDLY, "I offer you this enchantment for your worthy offering.")
                        addItemOrDrop(player!!, Items.ENCHANTED_LYRE1_3691, 1)
                        stage = 0
                    } else {
                        sendDialogue(player!!, "You don't have required items in your inventory.")
                        stage = 0
                    }
                }
                2 -> {
                    end()
                    stage = 0
                }
            }

            7 -> {
                player!!.dialogueInterpreter.sendItemMessage(
                    Items.FREMENNIK_SEA_BOOTS_1_14571,
                    "If you speak to Peer the Seer, he will deposit items into",
                    "your bank. The Fossegrimen's enchantment will give",
                    "your lyre extra charges, if you make her an offering in",
                    "person."
                )
                stage = 0
            }

            8 -> {
                setTitle(player!!, 2)
                sendDialogueOptions(player!!, "Do this now?", "Yes.", "No.")
                stage = 9
            }

            9 -> when (buttonID) {
                1 -> {
                    if (LyreTeleport.getStoreFile().getBoolean(player!!.username.lowercase())) {
                        sendDialogue(player!!, "This can only be done once per day.")
                        stage = 0
                    } else {
                        end()
                        LyreTeleport.teleport(player!!)
                    }
                }
                2 -> {
                    end()
                    stage = 0
                }
            }

            10, 15 -> {
                val boots = if (stage == 10) Items.FREMENNIK_SEA_BOOTS_2_14572 else Items.FREMENNIK_SEA_BOOTS_3_14573
                player!!.dialogueInterpreter.sendItemMessage(
                    boots,
                    "If you speak to Peer the Seer, he will deposit items into",
                    "your bank. The Fossegrimen's enchantment will give",
                    "your lyre extra charges, if you make her an offering in",
                    "person."
                )
                stage++
            }

            11, 16 -> {
                val boots = if (stage == 11) Items.FREMENNIK_SEA_BOOTS_2_14572 else Items.FREMENNIK_SEA_BOOTS_3_14573
                player!!.dialogueInterpreter.sendItemMessage(
                    boots,
                    "As Regent of Miscellania, the people will appreciate your",
                    "efforts more and your approval rating will increase",
                    "faster. There is also a broken section of pier on",
                    "Miscellania that you can use to quickly travel between"
                )
                stage++
            }

            12 -> {
                player!!.dialogueInterpreter.sendItemMessage(
                    Items.FREMENNIK_SEA_BOOTS_2_14572,
                    "there and Etceteria."
                )
                stage = 0
            }

            13 -> {
                setTitle(player!!, 3)
                sendDialogueOptions(player!!, "Choose a destination:", "Rellekka", "Waterbirth Island", "Don't change.")
                stage++
            }

            14 -> when (buttonID) {
                1 -> {
                    sendItemDialogue(player!!, Items.FREMENNIK_SEA_BOOTS_3_14573, "Remember that you must be wearing your Fremennik sea boots if you want to teleport to an alternative location.")
                    removeAttribute(player!!, LyreTeleport.LYRE_TELEPORT_ALT)
                    stage = 0
                }
                2 -> {
                    sendItemDialogue(player!!, Items.FREMENNIK_SEA_BOOTS_3_14573, "Remember that you must be wearing your Fremennik sea boots if you want to teleport to an alternative location.")
                    setAttribute(player!!, LyreTeleport.LYRE_TELEPORT_ALT, true)
                    stage = 0
                }
                3 -> {
                    end()
                    stage = 0
                }
            }

            17 -> {
                sendItemDialogue(player!!, Items.FREMENNIK_SEA_BOOTS_3_14573, "there and Etceteria.")
                stage++
            }

            18 -> {
                player!!.dialogueInterpreter.sendItemMessage(
                    Items.FREMENNIK_SEA_BOOTS_3_14573,
                    "Advisor Ghrim will accept flat-packed furniture as a",
                    "contribution to the coffers of Miscellania."
                )
                stage = 0
            }
        }
    }
}

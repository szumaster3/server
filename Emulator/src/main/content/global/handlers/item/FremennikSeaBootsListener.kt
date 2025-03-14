package content.global.handlers.item

import content.global.travel.LyreTeleport
import core.ServerStore.Companion.getBoolean
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs.consts.Items
import org.rs.consts.NPCs

val seaBoots =
    intArrayOf(Items.FREMENNIK_SEA_BOOTS_1_14571, Items.FREMENNIK_SEA_BOOTS_2_14572, Items.FREMENNIK_SEA_BOOTS_3_14573)
val enchantedLyre =
    intArrayOf(
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
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        npc = NPC(NPCs.FOSSEGRIMEN_1273)
                        when (stage) {
                            0 -> {
                                if (inEquipmentOrInventory(player, Items.FREMENNIK_SEA_BOOTS_1_14571)) {
                                    setTitle(player, 3)
                                    sendDialogueOptions(
                                        player,
                                        "What would you like to do?",
                                        "Contact the Fossegrimen.",
                                        "Explain benefits.",
                                        "Cancel.",
                                    )
                                    stage++
                                } else if (inEquipmentOrInventory(player, Items.FREMENNIK_SEA_BOOTS_2_14572)) {
                                    setTitle(player, 4)
                                    sendDialogueOptions(
                                        player,
                                        "What would you like to do?",
                                        "Contact the Fossegrimen.",
                                        "Free lyre teleport.",
                                        "Explain benefits.",
                                        "Cancel.",
                                    )
                                    stage++
                                } else if (inEquipmentOrInventory(player, Items.FREMENNIK_SEA_BOOTS_3_14573)) {
                                    setTitle(player, 5)
                                    sendDialogueOptions(
                                        player,
                                        "What would you like to do?",
                                        "Contact the Fossegrimen.",
                                        "Free lyre teleport.",
                                        "Lyre teleport destination.",
                                        "Explain benefits.",
                                        "Cancel.",
                                    )
                                    stage++
                                }
                            }

                            1 ->
                                when (buttonID) {
                                    1 -> {
                                        npc(
                                            FaceAnim.FRIENDLY,
                                            "Good day, ${player.username}. Do you want to make an",
                                            "offering?",
                                        )
                                        stage = 4
                                    }

                                    2 -> {
                                        if (inEquipmentOrInventory(player, Items.FREMENNIK_SEA_BOOTS_1_14571)) {
                                            player.dialogueInterpreter.sendItemMessage(
                                                Items.FREMENNIK_SEA_BOOTS_1_14571,
                                                "Your Fremennik sea boots will bring you certain",
                                                "benefits within the Fremennik area.",
                                            )
                                            stage = 7
                                        } else if (inEquipmentOrInventory(
                                                player,
                                                Items.FREMENNIK_SEA_BOOTS_2_14572,
                                            ) ||
                                            inEquipmentOrInventory(player, Items.FREMENNIK_SEA_BOOTS_3_14573)
                                        ) {
                                            player.dialogueInterpreter.sendItemMessage(
                                                Items.ENCHANTED_LYRE1_3691,
                                                "You may use lyre once per day without draining",
                                                "any of its enchantment.",
                                            )
                                            stage = 8
                                        }
                                    }

                                    3 -> {
                                        if (inEquipmentOrInventory(player, Items.FREMENNIK_SEA_BOOTS_1_14571)) {
                                            end()
                                        } else if (inEquipmentOrInventory(player, Items.FREMENNIK_SEA_BOOTS_2_14572)) {
                                            player.dialogueInterpreter.sendItemMessage(
                                                Items.FREMENNIK_SEA_BOOTS_2_14572,
                                                "Your Fremennik sea boots will bring you certain",
                                                "benefits within the Fremennik area.",
                                            )
                                            stage = 10
                                        } else if (inEquipmentOrInventory(player, Items.FREMENNIK_SEA_BOOTS_3_14573)) {
                                            sendItemDialogue(
                                                player,
                                                Items.LYRE_3689,
                                                "Your lyre currently teleports you to " +
                                                    if (getAttribute(
                                                            player,
                                                            LyreTeleport.LYRE_TELEPORT_ALT,
                                                            false,
                                                        )
                                                    ) {
                                                        "Waterbirth Island."
                                                    } else {
                                                        "Relleka."
                                                    },
                                            )
                                            stage = 13
                                        }
                                    }

                                    4 -> {
                                        if (inEquipmentOrInventory(player, Items.FREMENNIK_SEA_BOOTS_2_14572)) {
                                            end()
                                        } else if (inEquipmentOrInventory(player, Items.FREMENNIK_SEA_BOOTS_3_14573)) {
                                            player.dialogueInterpreter.sendItemMessage(
                                                Items.FREMENNIK_SEA_BOOTS_3_14573,
                                                "Your Fremennik sea boots will bring you certain",
                                                "benefits within the Fremennik area.",
                                            )
                                            stage = 15
                                        }
                                    }

                                    5 -> {
                                        if (inEquipmentOrInventory(player, Items.FREMENNIK_SEA_BOOTS_3_14573)) {
                                            end()
                                        }
                                    }
                                }

                            4 -> {
                                npc(
                                    FaceAnim.NEUTRAL,
                                    "Remember that you will gain a greater enchantment",
                                    "from offerings if you bring them to my altar.",
                                )
                                stage++
                            }

                            5 -> {
                                setTitle(player, 2)
                                sendDialogueOptions(
                                    player,
                                    "What do you offer?",
                                    if (inInventory(player, Items.RAW_BASS_363)) "A raw bass." else "A raw shark.",
                                    "Nothing at the moment.",
                                )
                                stage++
                            }

                            6 ->
                                when (buttonID) {
                                    1 -> {
                                        if (inInventory(player, Items.RAW_BASS_363) &&
                                            inEquipmentOrInventory(
                                                player,
                                                Items.RING_OF_CHAROSA_6465,
                                            ) ||
                                            inEquipmentOrInventory(player, Items.RING_OF_CHAROS_4202)
                                        ) {
                                            npcl(
                                                FaceAnim.HALF_GUILTY,
                                                "A raw bass? You should know that is not a worthy offering, outlander.",
                                            )
                                            stage = 0
                                        }
                                        if (hasAnItem(player, *enchantedLyre).container == player.inventory) {
                                            player.dialogueInterpreter.sendDialogue(
                                                "All lyre charges must be used up before it will allow a charge",
                                                "to the lyre.",
                                            )
                                            stage = 0
                                        } else if (inInventory(player, Items.RAW_SHARK_383) &&
                                            hasAnItem(
                                                player,
                                                Items.ENCHANTED_LYRE_3690,
                                            ).container == player.inventory &&
                                            removeItem(
                                                player,
                                                Items.ENCHANTED_LYRE_3690,
                                                Container.INVENTORY,
                                            ) &&
                                            removeItem(player, Items.RAW_SHARK_383, Container.INVENTORY)
                                        ) {
                                            npc(
                                                FaceAnim.FRIENDLY,
                                                "I offer you this enchantment for your worthy offering.",
                                            )
                                            addItemOrDrop(player, Items.ENCHANTED_LYRE1_3691, 1)
                                            stage = 0
                                        } else {
                                            sendDialogue(player, "You don't have required items in your inventory.")
                                            stage = 0
                                        }
                                    }

                                    2 -> {
                                        end()
                                        stage = 0
                                    }
                                }

                            7 ->
                                player.dialogueInterpreter
                                    .sendItemMessage(
                                        Items.FREMENNIK_SEA_BOOTS_1_14571,
                                        "If you speak to Peer the Seer, he will deposit items into",
                                        "your bank. The Fossegrimen's enchantment will give",
                                        "your lyre extra charges, if you make her an offering in",
                                        "person.",
                                    ).also { stage = 0 }

                            8 -> {
                                setTitle(player, 2)
                                sendDialogueOptions(player, "Do this now?", "Yes.", "No.")
                                stage = 9
                            }

                            9 ->
                                when (buttonID) {
                                    1 -> {
                                        if (LyreTeleport.getLyreTeleportFile().getBoolean(
                                                player.username.lowercase(),
                                            )
                                        ) {
                                            sendDialogue(player, "This can only be done once per day.")
                                            stage = 0
                                        } else {
                                            end()
                                            LyreTeleport.teleport(player)
                                        }
                                    }

                                    2 -> {
                                        end()
                                        stage = 0
                                    }
                                }

                            10 -> {
                                player.dialogueInterpreter.sendItemMessage(
                                    Items.FREMENNIK_SEA_BOOTS_2_14572,
                                    "If you speak to Peer the Seer, he will deposit items into",
                                    "your bank. The Fossegrimen's enchantment will give",
                                    "your lyre extra charges, if you make her an offering in",
                                    "person.",
                                )
                                stage++
                            }

                            11 -> {
                                player.dialogueInterpreter.sendItemMessage(
                                    Items.FREMENNIK_SEA_BOOTS_2_14572,
                                    "As Regent of Miscellania, the people will appreciate your",
                                    "efforts more and your approval rating will increase",
                                    "faster. There is also a broken section of pier on",
                                    "Miscellania that you can use to quickly travel between",
                                )
                                stage++
                            }

                            12 -> {
                                sendItemDialogue(player, Items.FREMENNIK_SEA_BOOTS_2_14572, "there and Etceteria.")
                                stage = 0
                            }

                            13 -> {
                                setTitle(player, 3)
                                sendDialogueOptions(
                                    player,
                                    "Choose a destination:",
                                    "Rellekka",
                                    "Waterbirth Island",
                                    "Don't change.",
                                )
                                stage++
                            }

                            14 ->
                                when (buttonID) {
                                    1 -> {
                                        sendItemDialogue(
                                            player,
                                            Items.FREMENNIK_SEA_BOOTS_3_14573,
                                            "Remember that you must be wearing your Fremennik sea boots if you want to teleport to an alternative location.",
                                        )
                                        removeAttribute(player, LyreTeleport.LYRE_TELEPORT_ALT)
                                        stage = 0
                                    }

                                    2 -> {
                                        sendItemDialogue(
                                            player,
                                            Items.FREMENNIK_SEA_BOOTS_3_14573,
                                            "Remember that you must be wearing your Fremennik sea boots if you want to teleport to an alternative location.",
                                        )
                                        setAttribute(player, LyreTeleport.LYRE_TELEPORT_ALT, true)
                                        stage = 0
                                    }

                                    3 -> {
                                        end()
                                        stage = 0
                                    }
                                }

                            15 -> {
                                player.dialogueInterpreter.sendItemMessage(
                                    Items.FREMENNIK_SEA_BOOTS_3_14573,
                                    "If you speak to Peer the Seer, he will deposit items into",
                                    "your bank. The Fossegrimen's enchantment will give",
                                    "your lyre extra charges, if you make her an offering in",
                                    "person.",
                                )
                                stage++
                            }

                            16 -> {
                                player.dialogueInterpreter.sendItemMessage(
                                    Items.FREMENNIK_SEA_BOOTS_3_14573,
                                    "As Regent of Miscellania, the people will appreciate your",
                                    "efforts more and your approval rating will increase",
                                    "faster. There is also a broken section of pier on",
                                    "Miscellania that you can use to quickly travel between",
                                )
                                stage++
                            }

                            17 -> {
                                sendItemDialogue(player, Items.FREMENNIK_SEA_BOOTS_3_14573, "there and Etceteria.")
                                stage++
                            }

                            18 -> {
                                player.dialogueInterpreter.sendItemMessage(
                                    Items.FREMENNIK_SEA_BOOTS_3_14573,
                                    "Advisor Ghrim will accept flat-packed furniture as a",
                                    "contribution to the coffers of Miscellania.",
                                )
                                stage = 0
                            }
                        }
                    }
                },
            )
            return@on true
        }
    }
}

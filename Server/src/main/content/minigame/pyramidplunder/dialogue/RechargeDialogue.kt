package content.minigame.pyramidplunder.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Recharge dialogue.
 */
@Initializable
class RechargeDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        usedSceptre = args[0] as Item
        sendMessage(player, "" + args[0])
        sendNPCDialogueLines(player, NPCs.GUARDIAN_MUMMY_4476, FaceAnim.OLD_NOT_INTERESTED, false, "Mrrrh, how do you have this? You shouldn't.", "Nevertheless, I can recharge this for you.")
        return true
    }

    override fun handle(dialogId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                setTitle(player, 2)
                sendOptions(player, "Recharge sceptre?", "Yes, please.", "No, thanks.").also { stage++ }
            }
            1 -> when (buttonId) {
                1 -> {
                    setTitle(player, 3)
                    sendOptions(player, "Recharge with?", "Clay/Ivory Artefacts(24)", "Stone Artefacts(12)", "Gold Artefacts(6)").also { stage = 20 }
                }

                2 -> stage = 100
            }

            20 -> when (buttonId) {
                1 -> {
                    var totalCounter = 0
                    run {
                        var i = 0
                        while (i < 28) {
                            if (totalCounter == 24) {
                                break
                            }
                            val thisSlot = player.inventory.getId(i)
                            if (thisSlot == Items.IVORY_COMB_9026) {
                                clayRecharge[totalCounter] = i
                                totalCounter += 1
                            }
                            i++
                        }
                    }
                    run {
                        var i = 0
                        while (i < 28) {
                            if (totalCounter == 24) {
                                break
                            }
                            val thisSlot = player.inventory.getId(i)
                            if (thisSlot == Items.POTTERY_SCARAB_9032) {
                                clayRecharge[totalCounter] = i
                                totalCounter += 1
                            }
                            i++
                        }
                    }
                    run {
                        var i = 0
                        while (i < 28) {
                            if (totalCounter == 24) {
                                break
                            }
                            val thisSlot = player.inventory.getId(i)
                            if (thisSlot == Items.POTTERY_STATUETTE_9036) {
                                clayRecharge[totalCounter] = i
                                totalCounter += 1
                            }
                            i++
                        }
                    }
                    if (clayRecharge[23] == 0) {
                        sendMessage(player, "You do not have enough clay/ivory artifacts.")
                        end()
                    }
                    var i = 0
                    while (i < 24) {
                        player.inventory.remove(
                            Item(player.inventory.getId(clayRecharge[i])),
                            clayRecharge[i],
                            true,
                        )
                        i++
                    }
                    removeItem(player, usedSceptre)
                    addItem(player, Items.PHARAOHS_SCEPTRE_9044)
                    sendMessage(player, "<col=7f03ff>Your Pharoah's Sceptre has been fully recharged!")
                    clayRecharge[23] = 0
                    end()
                }

                2 -> {
                    var totalCounter = 0
                    run {
                        var i = 0
                        while (i < 28) {
                            if (totalCounter == 12) {
                                break
                            }
                            val thisSlot = player.inventory.getId(i)
                            if (thisSlot == Items.STONE_SEAL_9042) {
                                stoneRecharge[totalCounter] = i
                                totalCounter += 1
                            }
                            i++
                        }
                    }
                    run {
                        var i = 0
                        while (i < 28) {
                            if (totalCounter == 12) {
                                break
                            }
                            val thisSlot = player.inventory.getId(i)
                            if (thisSlot == Items.STONE_SCARAB_9030) {
                                stoneRecharge[totalCounter] = i
                                totalCounter += 1
                            }
                            i++
                        }
                    }
                    run {
                        var i = 0
                        while (i < 28) {
                            if (totalCounter == 12) {
                                break
                            }
                            val thisSlot = player.inventory.getId(i)
                            if (thisSlot == Items.STONE_STATUETTE_9038) {
                                stoneRecharge[totalCounter] = i
                                totalCounter += 1
                            }
                            i++
                        }
                    }
                    if (stoneRecharge[11] == 0) {
                        sendMessage(player, "You do not have enough stone artifacts.")
                        end()
                    }
                    var i = 0
                    while (i < 12) {
                        player.inventory.remove(
                            Item(player.inventory.getId(stoneRecharge[i])),
                            stoneRecharge[i],
                            true,
                        )
                        i++
                    }
                    removeItem(player, usedSceptre)
                    addItem(player, Items.PHARAOHS_SCEPTRE_9044)
                    sendMessage(player, "<col=7f03ff>Your Pharoah's Sceptre has been fully recharged!")
                    stoneRecharge[11] = 0
                    end()
                }

                3 -> {
                    var totalCounter = 0
                    run {
                        var i = 0
                        while (i < 28) {
                            if (totalCounter == 6) {
                                break
                            }
                            val thisSlot = player.inventory.getId(i)
                            if (thisSlot == Items.GOLD_SEAL_9040) {
                                goldRecharge[totalCounter] = i
                                totalCounter += 1
                            }
                            i++
                        }
                    }
                    run {
                        var i = 0
                        while (i < 28) {
                            if (totalCounter == 6) {
                                break
                            }
                            val thisSlot = player.inventory.getId(i)
                            if (thisSlot == Items.GOLDEN_SCARAB_9028) {
                                goldRecharge[totalCounter] = i
                                totalCounter += 1
                            }
                            i++
                        }
                    }
                    run {
                        var i = 0
                        while (i < 28) {
                            if (totalCounter == 6) {
                                break
                            }
                            val thisSlot = player.inventory.getId(i)
                            if (thisSlot == Items.GOLDEN_STATUETTE_9034) {
                                goldRecharge[totalCounter] = i
                                totalCounter += 1
                            }
                            i++
                        }
                    }
                    if (goldRecharge[5] == 0) {
                        sendMessage(player, "You do not have enough gold artifacts.")
                        end()
                    }
                    var i = 0
                    while (i < 6) {
                        player.inventory.remove(
                            Item(player.inventory.getId(goldRecharge[i])),
                            goldRecharge[i],
                            true,
                        )
                        i++
                    }
                    player.inventory.remove(usedSceptre)
                    addItem(player, Items.PHARAOHS_SCEPTRE_9044)
                    sendMessage(player, "<col=7f03ff>Your Pharoah's Sceptre has been fully recharged!")
                    goldRecharge[5] = 0
                    end()
                }
            }

            100 -> end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(999876)

    companion object {
        var usedSceptre = Item(Items.PHARAOHS_SCEPTRE_9050)
        var clayRecharge = IntArray(24)
        var stoneRecharge = IntArray(12)
        var goldRecharge = IntArray(6)
    }
}

package content.region.tirannwn.quest.roving_elves.dialogue

import content.region.tirannwn.quest.roving_elves.RovingElves
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class ElunedDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (getQuestStage(player, Quests.ROVING_ELVES) == 10) {
            player(
                FaceAnim.HALF_GUILTY,
                "Hey there... Islwyn said you may be able to help me.",
                "He told me you know how to consecrate ground for an",
                "elven burial. I need to reconsecrate Glarial's resting",
                "place.",
            )
            stage = 1
        } else {
            player(FaceAnim.HALF_GUILTY, "Good day.")
            stage = 1000
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            500 -> end()
            1000 -> {
                if (getQuestStage(player, Quests.ROVING_ELVES) == 15 &&
                    !inInventory(player, RovingElves.CONSECRATION_SEED_CHARGED, 1)
                ) {
                    npc(FaceAnim.HALF_GUILTY, "Hello, any luck finding the consecration seed?")
                    stage = 1002
                }
                if (getQuestStage(player, Quests.ROVING_ELVES) == 15 &&
                    inInventory(player, RovingElves.CONSECRATION_SEED_CHARGED, 1)
                ) {
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "You still have the charged seed with you, I can feel it.",
                        "Hurry adventurer, go plant the seed and free",
                        "my grandmother's spirit.",
                    )
                    stage = 500
                }
                if (getQuestStage(player, Quests.ROVING_ELVES) == 15 &&
                    inInventory(player, RovingElves.CONSECRATION_SEED, 1)
                ) {
                    npc(FaceAnim.HALF_GUILTY, "Hello, any luck finding the consecration seed?")
                    stage = 1002
                }
                if (getQuestStage(player, Quests.ROVING_ELVES) == 20) {
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "Hey... how's it going? Have you managed to",
                        "reconsecrate Glarial's resting place?",
                    )
                    stage = 12
                } else if (getQuestStage(player, Quests.ROVING_ELVES) != 15) {
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "Hello there, it's a lovely day for a walk in the woods.",
                        "So what can I help you with?",
                    )
                    stage = 1001
                }
            }

            1001 ->
                stage =
                    if (getQuestStage(player, Quests.ROVING_ELVES) >= 100 &&
                        inInventory(player, Items.TINY_ELF_CRYSTAL_6103, 1)
                    ) {
                        player(FaceAnim.HALF_GUILTY, "I am looking to recharge teleportation crystals.")
                        1200
                    } else {
                        player(FaceAnim.HALF_GUILTY, "I'm just looking around.")
                        500
                    }

            1002 ->
                stage =
                    if (inInventory(player, RovingElves.CONSECRATION_SEED, 1)) {
                        player(FaceAnim.HALF_GUILTY, "Yes, I have it here.")
                        6
                    } else {
                        player(FaceAnim.HALF_GUILTY, "I forgot what you told me to do.")
                        1003
                    }

            1003 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Re-enter Glarial's tomb and defeat the tomb's guardian.",
                    "Take the consecration seed it is guarding and",
                    "bring it back to me.",
                )
                stage = 500
            }

            1200 -> {
                val timesRecharged = player.getAttribute("rovingelves:crystal-teleport-recharges", 0)
                val price = crystalTeleportPrice(timesRecharged)
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Very well. I'll recharge your teleportation",
                    String.format("crystal for %d gold. What do you say?", price),
                )
                stage = 1202
            }

            1202 -> {
                options("Recharge a crystal", "Nevermind")
                stage = 1203
            }

            1203 ->
                when (buttonId) {
                    1 -> {
                        stage = 1204
                        val timesRecharged = player.getAttribute("rovingelves:crystal-teleport-recharges", 0)
                        var price = crystalTeleportPrice(timesRecharged)
                        if (!player.inventory.contains(995, price)) {
                            player(FaceAnim.HALF_GUILTY, "Actually, I don't have enough coins.")
                        } else {
                            sendDialogue(
                                player,
                                String.format("Eluned recharges your elven teleportation crystal for %d gold.", price),
                            )
                            if (removeItem(player, Items.TINY_ELF_CRYSTAL_6103) &&
                                removeItem(player, Item(995, price))
                            ) {
                                player.inventory.add(Item(Items.TP_CRYSTAL_4_6099, 1))
                                player.incrementAttribute("/save:rovingelves:crystal-teleport-recharges", 1)
                            }
                        }
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "Nevermind, I really must be going.")
                        stage = 1204
                    }
                }

            1204 -> end()
            1 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "That I do... It is elvish tradition to plant a specially",
                    "enchanted crystal seed at the graves of our ancestors.",
                    "The seed will create guardians to protect the area.",
                )
                stage = 2
            }

            2 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Unfortunately the crystal seed must be tuned to the",
                    "person it's protecting... a new seed won't do. But you",
                    "should be able to recover the seed from her old tomb.",
                )
                stage = 3
            }

            3 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "The tomb's guardian will be protecting the seed, you'll",
                    "need to defeat him to get it. Once you have it, return",
                    "here and I will re-enchant it.",
                )
                stage = 4
            }

            4 -> {
                player(FaceAnim.HALF_GUILTY, "OK... I'll be back as soon as I have it.")
                stage = 5
            }

            5 -> {
                setQuestStage(player, Quests.ROVING_ELVES, 15)
                end()
            }

            6 -> {
                sendItemDialogue(player, RovingElves.CONSECRATION_SEED, "You hand the crystal seed to Eluned.")
                stage = 7
            }

            7 -> {
                npc(FaceAnim.HALF_GUILTY, "")
                stage = 8
            }

            8 -> {
                player(FaceAnim.HALF_GUILTY, "How odd. I can see her lips moving... But there's no", "sound.")
                stage = 9
            }

            9 -> {
                if (removeItem(player, RovingElves.CONSECRATION_SEED)) {
                    addItem(player, RovingElves.CONSECRATION_SEED_CHARGED)
                }
                sendItemDialogue(
                    player,
                    RovingElves.CONSECRATION_SEED_CHARGED,
                    "Eluned hands you an enchanted crystal seed.",
                )
                stage = 10
            }

            10 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Plant this seed in Glarial's new tomb, close to her",
                    "remains and she will rest in peace.",
                )
                stage = 11
            }

            11 -> {
                player(FaceAnim.HALF_GUILTY, "OK.")
                stage = 500
            }

            12 -> {
                player(FaceAnim.HALF_GUILTY, "Yes, it's done.")
                stage = 13
            }

            13 -> {
                npc(FaceAnim.HALF_GUILTY, "Well done... You should go see Islwyn, but I'd guess he", "already knows.")
                stage = 500
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ELUNED_1679)
    }

    fun crystalTeleportPrice(timesRecharged: Int): Int {
        return Math.max(750 - 150 * timesRecharged, 150)
    }
}

package content.region.misthalin.dialogue.lumbridge.tutor

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class WoodcuttingTutorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (getStatLevel(player, Skills.WOODCUTTING) >= 99) {
            npc(
                FaceAnim.HALF_GUILTY,
                "Wow! It's not often I meet somebody as accomplished",
                "as me in Woodcutting! Seeing as youre so skilled,",
                "maybe you are interested in buying a Skillcape of",
                "Woodcutting?",
            ).also {
                stage =
                    100
            }
        } else {
            options("Tell me about different trees and axes.", "What is that cape you're wearing?", "Goodbye.").also {
                stage =
                    500
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Who are you?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "What is that cape you're wearing?").also { stage = 20 }
                }
            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "My name is Wilfred and I'm the best woodsman in",
                    "Asgarnia! I've spent my life studying the best methods for",
                    "woodcutting. That's why I have this cape, the Skillcape of",
                    "Woodcutting.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            20 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "This is a Skillcape of Woodcutting. Only a person who has",
                    "achieved the highest possible level in a skill can wear one.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            100 -> options("Yes, please.", "No, thank you.").also { stage++ }
            101 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Yes, please.").also { stage = 1000 }
                    2 -> player(FaceAnim.HALF_GUILTY, "No, thank you.").also { stage = 2000 }
                }
            2000 ->
                options(
                    "Tell me about different trees and axes.",
                    "What is that cape you're wearing?",
                    "Goodbye.",
                ).also {
                    stage =
                        500
                }
            2002 -> player(FaceAnim.HALF_GUILTY, "Bye!").also { stage = END_DIALOGUE }
            1000 -> {
                if (freeSlots(player) == 1) {
                    player(FaceAnim.HALF_GUILTY, "Sorry, I don't have enough inventory space.").also {
                        stage =
                            END_DIALOGUE
                    }
                    return true
                }
                if (player.inventory.contains(Items.COINS_995, 99000)) {
                    player.inventory.remove(Item(Items.COINS_995, 99000))
                    player.inventory.add(
                        Item(
                            Items.WOODCUTTING_CAPE_9807 + (if (player.getSkills().masteredSkills > 1) 1 else 0),
                        ),
                    )
                    player.inventory.add(Item(Items.WOODCUTTING_HOOD_9809, 1))
                    npc(FaceAnim.HALF_GUILTY, "Excellent! Wear that cape with pride my friend.")
                    stage = 107
                } else {
                    player(FaceAnim.HALF_GUILTY, "Sorry, I don't seem to have enough coins.")
                    stage = 160
                }
            }

            160 -> npc(FaceAnim.HALF_GUILTY, "Very well, farewell adventurer.").also { stage = END_DIALOGUE }
            107 -> player(FaceAnim.HALF_GUILTY, "Will do, Wilfred.").also { stage++ }
            108 -> npc(FaceAnim.HALF_GUILTY, "Very well, farewell adventurer.").also { stage = END_DIALOGUE }
            500 ->
                when (buttonId) {
                    1 -> player("Tell me about different trees and axes.").also { stage = 510 }
                    2 -> player("What is that cape you're wearing?").also { stage = 520 }
                    3 -> player("Goodbye.").also { stage = END_DIALOGUE }
                }
            510 ->
                sendDialogueOptions(
                    player,
                    "Trees",
                    "Oak and Willow",
                    "Maple and Yew",
                    "Magic and other trees",
                    "Axes",
                    "Go back to teaching",
                ).also {
                    stage++
                }
            511 ->
                when (buttonId) {
                    1 ->
                        sendDoubleItemDialogue(
                            player,
                            Items.LOGS_1511,
                            Items.OAK_LOGS_1521,
                            "Almost every tree can be chopped down. Normal logs will be produced by chopping 'Trees' and Oak logs will come from chopping 'Oak Trees'. You can find Oak trees in amongst normal trees scatterd about the",
                        ).also {
                            stage =
                                5100
                        }
                    2 ->
                        sendItemDialogue(
                            player,
                            Items.MAPLE_LOGS_1517,
                            "Maple logs can be gleaned from Maple trees. You'll usually find Maple trees standing alone amongst other trees.",
                        ).also {
                            stage =
                                5200
                        }
                    3 ->
                        sendItemDialogue(
                            player,
                            Items.MAGIC_LOGS_1513,
                            "Magic trees are... magic. A difficult wood to work with, but worth it for the rewards. Find them in the areas south of Seers village or on the East side of the Mage arena.",
                        ).also {
                            stage =
                                5300
                        }
                    4 ->
                        sendItemDialogue(
                            player,
                            Items.BRONZE_AXE_1351,
                            "Bronze axes are easy to get, simply go visit Bob in his shop in Lumbridge, or talk to me if you have mislaid yours.",
                        ).also {
                            stage =
                                5400
                        }
                    5 ->
                        options(
                            "Tell me about different trees and axes.",
                            "What is that cape you're wearing?",
                            "Goodbye.",
                        ).also {
                            stage =
                                500
                        }
                }
            5400 ->
                npc(
                    "As you progress in your combat skill you will find you",
                    "can wield your woodcutting axe as a weapon, it's not",
                    "very effective, but it frees up a slot for another log.",
                ).also {
                    stage++
                }
            5401 ->
                sendDoubleItemDialogue(
                    player,
                    Items.IRON_AXE_1349,
                    Items.STEEL_AXE_1353,
                    "As your woodcutting skill increases you will find yourself able to use better axes to chop trees faster.... anything up to steel you can buy from Bob's axe shop.",
                ).also {
                    stage++
                }
            5402 ->
                sendItemDialogue(
                    player,
                    Items.RUNE_AXE_1359,
                    "Rune axes can be player made with very high level smithing and mining. They can also be obtained through killing one of the fearsome tree spirits, though this is very rare.",
                ).also {
                    stage =
                        510
                }
            5300 ->
                npc(
                    "Hollow trees can be found in the Haunted Woods east",
                    "of Canifis, but be careful of the leeches.",
                ).also {
                    stage =
                        510
                }
            5200 ->
                sendItemDialogue(
                    player,
                    Items.YEW_LOGS_1515,
                    "Yew trees are few and far between. We do our best to cultivate them. Look for the tree icon on your mini map to find rare trees. Try North of Port Sarim.",
                ).also {
                    stage =
                        510
                }
            5100 -> sendDoubleItemDialogue(player, Items.LOGS_1511, Items.OAK_LOGS_1521, "lands.").also { stage = 5101 }
            5101 ->
                sendItemDialogue(
                    player,
                    Items.WILLOW_LOGS_1519,
                    "Willow trees will yield willow logs. You'll find willows like to grow near water, you can find some south of Draynor.",
                ).also {
                    stage =
                        5102
                }
            5102 ->
                sendDialogueOptions(
                    player,
                    "Trees",
                    "Oak and Willow",
                    "Maple and Yew",
                    "Magic and other trees",
                    "Axes",
                    "Go back to teaching",
                ).also {
                    stage =
                        511
                }
            520 ->
                npc(
                    "This is a Skillcape of Woodcutting. Only a person who",
                    "has achieved the highest possible level in a skill can wear",
                    "one.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return WoodcuttingTutorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WOODCUTTING_TUTOR_4906)
    }
}

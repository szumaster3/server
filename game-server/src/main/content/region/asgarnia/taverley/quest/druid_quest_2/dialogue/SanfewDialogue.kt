package content.region.asgarnia.taverley.quest.druid_quest_2.dialogue

import core.api.getQuestStage
import core.api.setQuestStage
import core.api.updateQuestTab
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SanfewDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "What can I do for you young 'un?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val questStage = getQuestStage(player, Quests.DRUIDIC_RITUAL)
        when (stage) {
            0 -> {
                when (questStage) {
                    20 -> {
                        npc(FaceAnim.ASKING, "Did you bring me the required ingredients for the", "potion?")
                        stage = 100
                    }

                    10 -> {
                        options(
                            "I've been sent to help purify the Varrock stone circle.",
                            "Actually, I don't need to speak to you.",
                        )
                        stage = 2
                    }
                    else -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "Nothing... I'll just be on my way now.",
                        )
                        stage = 1
                    }
                }
            }

            1 -> end()
            2 ->
                when (buttonId) {
                    1 -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "I've been sent to assist you with the ritual to purify the",
                            "Varrockian stone circle.",
                        )
                        stage = 5
                    }

                    2 -> {
                        player(FaceAnim.NEUTRAL, "Actually, I don't need to speak to you.")
                        stage = 3
                    }
                }

            3 -> {
                npc(FaceAnim.FRIENDLY, "Well, we all make mistakes sometimes.")
                stage = 4
            }

            4 -> end()
            5 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, what I'm struggling with right now is the meats",
                    "needed for the potion to honour Guthix. I need the raw",
                    "meat of four different animals for it, but not just any",
                    "old meats will do.",
                )
                stage = 6
            }

            6 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Each meat has to be dipped individually into the",
                    "Cauldron of Thunder for it to work correctly.",
                )
                stage = 7
            }

            7 -> {
                options("Where can I find this cauldron?", "Ok, I'll do that then.")
                stage = 8
            }

            8 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "Where can I find this cauldron?").also { stage = 9 }
                    2 -> player(FaceAnim.FRIENDLY, "Ok, I'll do that then.").also { stage = 10 }
                }

            9 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "It is located somewhere in the mysterious underground",
                    "halls which are located somewhere in the woods just",
                    "South of here. They are too dangerous for me to go",
                    "myself however.",
                )
                stage = 10
            }

            10 -> {
                setQuestStage(player, Quests.DRUIDIC_RITUAL, 20)
                end()
            }

            100 -> {
                if (!player.inventory.containItems(
                        Items.ENCHANTED_BEEF_522,
                        Items.ENCHANTED_RAT_MEAT_523,
                        Items.ENCHANTED_BEAR_MEAT_524,
                        Items.ENCHANTED_CHICKEN_525,
                    )
                ) {
                    player(FaceAnim.HALF_GUILTY, "No, not yet...")
                    stage = 101
                } else {
                    player(FaceAnim.HAPPY, "Yes, I have all four now!")
                    stage = 200
                }
            }

            101 -> {
                npc(FaceAnim.HALF_GUILTY, "Well let me know when you do young 'un.")
                stage = 102
            }

            102 -> {
                player(FaceAnim.HALF_GUILTY, "I'll get on with it.")
                stage = 103
            }

            103 -> {
                npc(FaceAnim.HALF_GUILTY, "Good, good.")
                stage = 104
            }

            104 -> end()
            200 -> {
                npc(
                    "Well hand 'em over then " + (if (player.isMale) "lad" else "lass") + "!",
                )
                stage = 201
            }

            201 -> {
                npc(
                    "Thank you so much adventurer! These meats will allow",
                    "our potion to honour Guthix to be completed, and bring",
                    "one step closer to reclaiming our stone circle!",
                )
                stage = 202
            }

            202 -> {
                player.inventory.remove(
                    Item(Items.ENCHANTED_BEEF_522, 1),
                    Item(Items.ENCHANTED_RAT_MEAT_523, 1),
                    Item(Items.ENCHANTED_BEAR_MEAT_524, 1),
                    Item(Items.ENCHANTED_CHICKEN_525, 1),
                )
                setQuestStage(player, Quests.DRUIDIC_RITUAL, 99)
                updateQuestTab(player)
                npc(
                    "Now go and talk to Kaqemeex and he will introduce",
                    "you to the wonderful world of herblore and potion",
                    "making!",
                )
                stage = 203
            }

            203 -> end()
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SANFEW_454)
}

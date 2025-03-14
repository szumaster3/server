package content.region.misthalin.dialogue.draynor

import content.region.desert.quest.rescue.dialogue.NedDialogueFile
import content.region.misthalin.dialogue.lumbridge.NedDiaryDialogue
import content.region.misthalin.quest.dragon.dialogue.NedDialogue
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class NedDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            "Why, hello there, lad. Me friends call me Ned. I was a",
            "man of the sea, but it's past me now. Could I be",
            "making or selling you some rope?",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                val dSlayerStage = player.questRepository.getStage(Quests.DRAGON_SLAYER)
                val parStage = player.questRepository.getStage(Quests.PRINCE_ALI_RESCUE)
                showTopics(
                    IfTopic(
                        "I'd like to talk about ${Quests.DRAGON_SLAYER}.",
                        NedDialogue(dSlayerStage),
                        dSlayerStage == 20 || dSlayerStage == 30,
                    ),
                    IfTopic(
                        "I'd like to talk about ${Quests.PRINCE_ALI_RESCUE}.",
                        NedDialogueFile(parStage),
                        parStage == 20 || parStage == 30 || parStage == 40 || parStage == 50,
                    ),
                    Topic("Yes, I would like some rope.", 10),
                    Topic("No thanks Ned, I don't need any.", END_DIALOGUE),
                    Topic("I'd like to ask about my Achievement Diary.", NedDiaryDialogue()),
                )
            }

            10 ->
                npc(
                    "Well, I can sell you some rope for 15 coins. Or I can",
                    "be making you some if you gets me 4 balls of wool. I",
                    "strands them together I does, makes em strong.",
                ).also { stage++ }

            11 -> player("You make rope from wool?").also { stage++ }
            12 -> npc("Of course you can!").also { stage++ }
            13 -> player("I thought you needed hemp or jute.").also { stage++ }
            14 -> npc("Do you want some rope or not?").also { stage++ }
            15 ->
                if (!inInventory(player, Items.BALL_OF_WOOL_1759, 4)) {
                    options(
                        "Okay, please sell me some rope.",
                        "That's a little more than I want to pay.",
                        "I will go and get some wool.",
                    ).also { stage = 16 }
                } else {
                    options(
                        "Okay, please sell me some rope.",
                        "I have some balls of wool. Could you make me some rope?",
                        "That's a little more than I want to pay.",
                    ).also { stage = 17 }
                }

            17 ->
                when (buttonId) {
                    1 -> player("Okay, please sell me some rope.").also { stage = 100 }
                    2 -> player("I have some balls of wool.", "Could you make me some rope?").also { stage = 120 }
                    3 -> player("That's a little more than I want to pay.").also { stage = 200 }
                }

            16 ->
                when (buttonId) {
                    1 -> player("Okay, please sell me some rope.").also { stage = 100 }
                    2 -> player("That's a little more than I want to pay.").also { stage = 200 }
                    3 -> player("I will go and get some wool.").also { stage = 300 }
                }

            40 ->
                when (buttonId) {
                    1 -> player("Ned could you make other things from wool?").also { stage = 41 }
                    2 -> player("Yes, I would like some rope.").also { stage = 10 }
                    3 -> player("No thanks Ned, I don't need any.").also { stage = 20 }
                }

            100 -> npc("There you go, finest rope in " + settings!!.name + ".").also { stage++ }
            101 -> {
                if (!removeItem(player, Item(Items.COINS_995, 15))) {
                    sendDialogue(player!!, "You don't have enough coins to pay for rope.").also { stage = END_DIALOGUE }
                } else {
                    end()
                    sendItemDialogue(player, Items.ROPE_955, "You hand Ned 15 coins. Ned gives you a coil of rope.")
                    addItemOrDrop(player, Items.ROPE_955, 1)
                }
            }

            120 -> npc("Sure I can.").also { stage++ }
            121 ->
                if (removeItem(player, Item(Items.BALL_OF_WOOL_1759, 4))) {
                    end()
                    sendDialogue(player!!, "You hand over 4 balls of wool. Ned gives you a coil of rope.")
                    addItemOrDrop(player, Items.ROPE_955, 1)
                }

            200 ->
                npc(
                    "Well, if you ever need rope that's the price. Sorry.",
                    "An old sailor needs money for a little drop o' rum.",
                ).also { stage = END_DIALOGUE }

            300 ->
                npc("Aye, you do that. Remember, it takes 4 balls of wool to", "make strong rope.").also {
                    stage = END_DIALOGUE
                }

            20 ->
                npc(
                    "Well, old Neddy is always here if you do. Tell your",
                    "friends. I can always be using the business.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return NedDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NED_743)
    }
}

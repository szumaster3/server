package content.region.misthalin.dialogue.varrock

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BaraekDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        showTopics(
            IfTopic(
                "Can you tell me where I can find the Phoenix Gang?",
                0,
                getQuestStage(player, Quests.SHIELD_OF_ARRAV) in 30..40,
            ),
            Topic("Can you sell me some furs?", 11),
            Topic("Hello. I am in search of a quest.", 16),
            IfTopic("Would you like to buy my fur?", 17, inInventory(player, Items.FUR_6814)),
            IfTopic(
                "Would you like to buy my grey wolf fur?",
                24,
                inInventory(player, Items.GREY_WOLF_FUR_958) && !inInventory(player, Items.FUR_6814),
            ),
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("Sh sh sh, not so loud! You don't want to get me in", "trouble!").also { stage++ }
            1 -> player("So DO you know where they are?").also { stage++ }
            2 -> npc("I may do.").also { stage++ }
            3 -> npc("But I don't to get in trouble for revealing their", "hideout.").also { stage++ }
            4 ->
                npc(
                    "Of course, if I was, say 20 gold coins richer I may",
                    "happen to be more inclined to take that sort of risk...",
                ).also {
                    stage++
                }

            5 ->
                options(
                    "Okay. Have 20 gold coins.",
                    "No, I don't like things like bribery.",
                    "Yes. I'd like to be 20 gold coins richer too.",
                ).also {
                    stage++
                }

            6 ->
                when (buttonId) {
                    1 -> player("Okay. Have 20 gold coins.").also { stage = 7 }
                    2 -> player("No, I don't like things like bribery.").also { stage = 10 }
                    3 -> player("Yes. I'd like to be 20 gold coins richer too.").also { stage = END_DIALOGUE }
                }

            7 ->
                if (!removeItem(player, Item(Items.COINS_995, 20))) {
                    player("I don't have enough coins.").also { stage = END_DIALOGUE }
                } else {
                    setQuestStage(player, Quests.SHIELD_OF_ARRAV, 40)
                    npc(
                        "Ok, to get to the gang hideout, enter Varrock through",
                        "the south gate. Then, if you take the first turning east",
                        "somewhere along there is an alleway to the south. The",
                        "door at the end of there is an entrance to the Phoenix",
                    ).also {
                        stage++
                    }
                }

            8 ->
                npc(
                    "Gang. they're operating there under the name of",
                    "VTAM Corporation. Be careful. The Phoenixes ain't",
                    "the type to be messed about.",
                ).also {
                    stage++
                }

            9 -> player("Thanks!").also { stage = END_DIALOGUE }
            10 ->
                npc(
                    "Heh. If you wanna deal with the Phoenix Gand they're",
                    "involved in much worse than a bit of briber.",
                ).also {
                    stage =
                        END_DIALOGUE
                }

            11 -> npc("Yeah, sure. They're 20 gold coins each.").also { stage++ }
            12 -> options("Yeah, okay, here you go.", "20 gold coins? That's an outrade!").also { stage++ }
            13 ->
                when (buttonId) {
                    1 -> player("Yeah, OK, here you go.").also { stage = 16 }
                    2 -> player("20 gold coins? That's an outrage!").also { stage++ }
                }

            14 ->
                npc("Well, I can't go any cheaper than that mate. I have a", "family to feed.").also {
                    stage = END_DIALOGUE
                }

            15 -> {
                if (!removeItem(player, Item(Items.COINS_995, 20))) {
                    player("Oops, I dont' seem to have enough coins.").also { stage = END_DIALOGUE }
                } else {
                    end()
                    sendItemDialogue(player, Items.FUR_6814, "Baraeck sells you a fur.")
                    addItemOrDrop(player, Items.FUR_6814, 12)
                }
            }

            16 -> npc("Sorry kiddo, I'm a fur trader not a damsel in distress.").also { stage = END_DIALOGUE }
            17 -> npc("Let's have a look at it.").also { stage++ }
            18 -> sendItemDialogue(player, Items.FUR_6814, "You hand Baraeck your fur to look at.").also { stage++ }
            19 -> npc("It's not in the best condition. I guess I could give you", "12 coins for it.").also { stage++ }
            20 -> options("Yeah, that'll do.", "I think I'll keep hold of it actually.").also { stage++ }
            21 ->
                when (buttonId) {
                    1 -> player("Yeah, that'll do.").also { stage = 23 }
                    2 -> player("I think I'll keep hold of it actually!").also { stage++ }
                }

            22 -> npc("Oh ok. Didn't want it anyway!").also { stage = END_DIALOGUE }
            23 -> {
                if (removeItem(player, Item(Items.FUR_6814, 1))) {
                    end()
                    addItemOrDrop(player, Items.COINS_995, 12)
                    player("Thanks!")
                }
            }

            24 -> npc("Let's have a look at it.").also { stage++ }
            25 ->
                sendItemDialogue(
                    player,
                    Items.GREY_WOLF_FUR_958,
                    "You hand Baraeck your grey wolf fur to look at.",
                ).also { stage++ }

            26 ->
                if (!inEquipment(player, Items.RING_OF_CHAROSA_6465)) {
                    npc(
                        "It's not in the best condition. I guess I could give you",
                        "120 coins for it.",
                    ).also { stage++ }
                } else {
                    npc(
                        "It's not in the best condition. I guess I could give you",
                        "150 coins for it.",
                    ).also { stage++ }
                }

            27 -> options("Yeah, that'll do.", "I think I'll keep hold of it actually.").also { stage++ }
            28 ->
                when (buttonId) {
                    1 -> player("Yeah, that'll do.").also { stage = 30 }
                    2 -> player("I think I'll keep hold of it actually!").also { stage++ }
                }

            29 -> npc("Oh ok. Didn't want it anyway!").also { stage = END_DIALOGUE }
            30 -> {
                if (removeItem(player, Item(Items.GREY_WOLF_FUR_958, 1))) {
                    end()
                    player("Thanks!")
                    if (!inEquipment(player, Items.RING_OF_CHAROSA_6465)) {
                        addItemOrDrop(player, Items.COINS_995, 120)
                    } else {
                        addItemOrDrop(player, Items.COINS_995, 150)
                    }
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return BaraekDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARAEK_547)
    }
}

package content.region.desert.dialogue.pollnivneach

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class AliTheDyerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        playerl(FaceAnim.AMAZED, "Wow! look at this place, it's so colourful.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.HALF_GUILTY, "Why thank you. Can I help you with anything?").also { stage++ }
            1 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "I take it by the clothes you are wearing and this room that you are in the dye trade?",
                ).also {
                    stage++
                }

            2 -> npcl(FaceAnim.HALF_GUILTY, "How observant of you.").also { stage++ }
            3 ->
                options(
                    "Could you make some dye for me please?",
                    "How did you get into the colour business?",
                ).also { stage++ }

            4 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "Could you make some dye for me please?").also { stage++ }
                    2 -> player("How did you get into the colour business?").also { stage = 18 }
                }

            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "What colour dye do you want? I can make you blue, red or yellow dyes for you. If you bring me the correct raw materials.",
                ).also {
                    stage++
                }

            6 ->
                options(
                    "What do you need to make red dye?",
                    "What do you need to make yellow dye?",
                    "What do you need to make blue dye?",
                ).also { stage++ }

            7 ->
                when (buttonId) {
                    1 -> player("What do you need to make red dye?").also { stage++ }
                    2 -> player("What do you need to make yellow dye?").also { stage = 11 }
                    3 -> player("What do you need to make blue dye?").also { stage = 15 }
                }

            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Hmm... 3 lots of redberries should do the trick and 25 coins should cover any other costs.",
                ).also {
                    stage++
                }

            9 -> player("Okay, make me some red dye please.").also { stage++ }
            10 -> {
                if (amountInInventory(player, Items.COINS_995) < 25 || !player.inventory.containsItem(REDBERRIES)) {
                    end()
                    sendDialogue(player!!, "You need 3 redberries leaves and 25 coins.")
                } else {
                    end()
                    removeItem(player, Item(Items.COINS_995, 25))
                    removeItem(player, REDBERRIES)
                    addItemOrDrop(player, Items.RED_DYE_1763)
                    sendItemDialogue(
                        player,
                        RED_DYE,
                        "You hand the berries and payment to Ali The Dyer. Ali produces a red bottle and hands it to you.",
                    )
                }
            }
            11 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Yellow is an awkward colour to recreate, I guess if you got me 2 onions I could use their skins to make warm yellow dye for you.",
                ).also {
                    stage++
                }
            12 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh yes and lest I forget I'll need 25 coins to cover the cost of the glass ware and my wages.",
                ).also {
                    stage++
                }
            13 -> player("Okay, make me some yellow dye please.").also { stage++ }
            14 -> {
                if (amountInInventory(player, Items.COINS_995) < 25 || !player.inventory.containsItem(ONIONS)) {
                    end()
                    sendDialogue(player!!, "You need 2 onions and 25 coins.")
                } else {
                    end()
                    removeItem(player, Item(Items.COINS_995, 25))
                    removeItem(player, ONIONS)
                    addItemOrDrop(player, Items.YELLOW_DYE_1765)
                    sendItemDialogue(
                        player,
                        YELLOW_DYE,
                        "You hand the onions and payment to Ali The Dyer. Ali produces a yellow bottle and hands it to you.",
                    )
                }
            }
            15 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "To make a bottle of blue dye I'll need 2 woad leaves and 25 coins from you.",
                ).also {
                    stage++
                }
            16 -> player("Okay, make me some blue dye please.").also { stage++ }
            17 -> {
                if (amountInInventory(player, Items.COINS_995) < 25 || !player.inventory.containsItem(WOAD_LEAVES)) {
                    end()
                    sendDialogue(player, "You need 2 woad leaves and 25 coins.")
                } else {
                    end()
                    removeItem(player, Item(Items.COINS_995, 25))
                    removeItem(player, WOAD_LEAVES)
                    addItemOrDrop(player, Items.BLUE_DYE_1767)
                    sendItemDialogue(
                        player,
                        BLUE_DYE,
                        "You hand the woad leaves and payment to Ali The Dyer. Ali produces a blue bottle and hands it to you.",
                    )
                }
            }
            18 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, it's quite simple really my mother was a drysalter and I learnt the trade from her.",
                ).also {
                    stage++
                }
            19 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Umm, what's a drysalter, it isn't a medical condition is it?",
                ).also { stage++ }
            20 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "No silly, it's someone who deals with preservatives and dyes. I didn't really enjoy the preservatives side of the things so I've chosen to deal solely in dyeing.",
                ).also {
                    stage++
                }
            21 -> playerl(FaceAnim.HALF_GUILTY, "So how's business then?").also { stage++ }
            22 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "It's a dyeing trade! Oh gosh I'm sorry that was a truly dreadful joke.",
                ).also {
                    stage++
                }
            23 -> playerl(FaceAnim.HALF_GUILTY, "Hmm yes, about the worst I've heard today at least.").also { stage++ }
            24 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Thanks. But really, it's not the best around here. nearly everyone in this town wears only white, which means I don't exactly do a roaring trade. Now is there anything I can get you?",
                ).also {
                    stage++
                }
            25 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AliTheDyerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_DYER_2549)
    }

    companion object {
        val ONIONS = Item(Items.ONION_1957, 2)
        val REDBERRIES = Item(Items.REDBERRIES_1951, 3)
        val WOAD_LEAVES = Item(Items.WOAD_LEAF_1793, 5)
        val BLUE_DYE = Item(Items.BLUE_DYE_1767)
        val YELLOW_DYE = Item(Items.YELLOW_DYE_1765)
        val RED_DYE = Item(Items.RED_DYE_1763)
    }
}

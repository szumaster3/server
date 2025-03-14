package content.region.asgarnia.dialogue.falador

import content.data.tables.BirdNestDropTable
import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryLevel
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class WysonTheGardenerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        var birdNest = anyInInventory(player, Items.MOLE_CLAW_7416, Items.MOLE_SKIN_7418)
        if (birdNest) {
            npc(
                "If I'm not mistaken, you've got some mole bits there!",
                "I'll trade 'em for bird nest if ye likes.",
            ).also {
                stage =
                    100
            }
            return true
        } else {
            npc(
                "I'm the head gardener around here.",
                "If you're looking for woad leaves, or if you need help",
                "with owt, I'm yer man.",
            ).also {
                stage =
                    0
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes please, I need woad leaves.", "Sorry, but I'm not interested.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Yes please, I need woad leaves.").also { stage = 10 }
                    2 -> player("Sorry, but I'm not interested.").also { stage = 200 }
                }
            10 -> npc("How much are you willing to pay?").also { stage++ }
            11 ->
                options(
                    "How about 5 coins?",
                    "How about 10 coins?",
                    "How about 15 coins?",
                    "How about 20 coins?",
                ).also {
                    stage++
                }
            12 ->
                when (buttonId) {
                    1 -> player("How about 5 coins?").also { stage = 110 }
                    2 -> player("How about 10 coins?").also { stage = 110 }
                    3 -> player("How about 15 coins?").also { stage = 130 }
                    4 -> player("How about 20 coins?").also { stage = 140 }
                }
            110 ->
                npc(
                    "No no, that's far too little. Woad leaves are hard to get. I",
                    "used to have plenty but someone kept stealing them off",
                    "me.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            130 -> npc("Mmmm... ok, that sounds fair.").also { stage++ }
            131 -> {
                if (inInventory(player, Items.COINS_995, 15)) {
                    removeItem(player, COINS[0])
                    addItemOrDrop(player, Items.WOAD_LEAF_1793, 1)
                    player("Thanks.")
                    sendMessage(player, "You buy a woad leaf from Wyson.")
                    stage++
                } else {
                    end()
                    sendMessage(player, "You need 15 cold coins to buy a woad leaf.")
                }
            }

            132 -> npc("I'll be around if you have any more gardening needs.").also { stage = END_DIALOGUE }
            140 -> npc("Thanks for being generous", "here's an extra woad leave.").also { stage++ }
            141 -> {
                if (inInventory(player, Items.COINS_995, 20)) {
                    player.inventory.remove(COINS[1])
                    var i = 0
                    while (i < 2) {
                        player.inventory.add(WOAD_LEAF, player)
                        i++
                    }
                    player("Thanks.")
                    sendMessage(player, "You buy two woad leaves from Wyson.")
                    stage = 132
                } else {
                    end()
                    sendMessage(player, "You need 20 cold coins to buy a woad leaf.")
                }
            }

            200 -> npc("Fair enough.").also { stage = END_DIALOGUE }
            100 ->
                options(
                    "Yes, I will trade the mole claws.",
                    "Okay, I will trade the mole skin.",
                    "I'd like to trade both.",
                    "No, thanks.",
                ).also {
                    stage =
                        900
                }
            900 ->
                when (buttonId) {
                    1 -> player("Yes, I will trade the mole claws.").also { stage = 910 }
                    2 -> player("Okay, I will trade the mole skin.").also { stage = 920 }
                    3 -> player("I'd like to trade both.").also { stage = 930 }
                    4 -> player("No, thanks.").also { stage = END_DIALOGUE }
                }
            910 -> {
                end()
                if (!inInventory(player, Items.MOLE_CLAW_7416)) {
                    player("Sorry, I don't have any mole claws.").also { stage = END_DIALOGUE }
                } else {
                    addRewards()
                }
            }
            920 -> {
                end()
                if (!inInventory(player, Items.MOLE_SKIN_7418)) {
                    player("Sorry, I don't have any mole skins.").also { stage = END_DIALOGUE }
                } else {
                    addRewards()
                }
            }
            930 -> {
                end()
                if (!anyInInventory(player, Items.MOLE_CLAW_7416, Items.MOLE_SKIN_7418)) {
                    player("Sorry, I don't have any.").also { stage = END_DIALOGUE }
                } else {
                    addRewards()
                }
            }
        }
        return true
    }

    private fun addRewards() {
        val moleClaws = amountInInventory(player, Items.MOLE_CLAW_7416)
        val moleSkin = amountInInventory(player, Items.MOLE_SKIN_7418)
        val nestAmount = moleClaws + moleSkin

        removeItem(player, Item(Items.MOLE_CLAW_7416, moleClaws))
        removeItem(player, Item(Items.MOLE_SKIN_7418, moleSkin))

        if (moleSkin > 0 &&
            player.achievementDiaryManager.getDiary(DiaryType.FALADOR)!!.checkComplete(
                DiaryLevel.HARD,
            )
        ) {
            player.inventory.add(Item(14589, moleSkin), player)
        }

        for (i in 0 until nestAmount) {
            if (!player.inventory.add(Item(BirdNestDropTable.getRandomNest(true)!!.nest.id, 1), player)) {
                GroundItemManager.create(
                    Item(BirdNestDropTable.getRandomNest(true)!!.nest.id, 1),
                    player.location,
                    player,
                )
            }
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WYSON_THE_GARDENER_36)
    }

    companion object {
        private val COINS = arrayOf(Item(995, 15), Item(995, 20))
        private val WOAD_LEAF = Item(1793, 1)
        private var birdNest = false
    }
}

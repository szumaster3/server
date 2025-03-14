package content.region.misthalin.dialogue.wizardstower

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.dialogue.Dialogue
import core.game.event.ResourceProducedEvent
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import kotlin.math.min

@Initializable
class SplitbarkWizardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Hello there, can I help you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "What do you do here?",
                    "What's that you're wearing?",
                    "Can you make me some armour please?",
                    "No thanks.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> player("What do you do here?").also { stage = 10 }
                    2 -> player("What's that you're wearing?").also { stage = 20 }
                    3 -> player("Can you make me some armour please?").also { stage = 30 }
                    4 -> player("No thanks.").also { stage = END_DIALOGUE }
                }

            30 -> npc("Certainly, what would you like me to make?").also { stage++ }
            31 -> end().also { initExchange(player, npc) }
            20 ->
                npc(
                    "Split-bark armour is special armour for mages, it's much",
                    "more resistant to physical attacks than normal robes.",
                    "It's actually very easy for me to make, but I've been",
                    "having trouble getting hold of the pieces.",
                ).also { stage = 15 }

            10 -> npc("I've been studying the practice of making split-bark", "armour.").also { stage++ }
            11 -> options("Split-bark armour, what's that?", "Can you make me some?").also { stage++ }
            12 ->
                when (buttonId) {
                    1 -> player("Split-bark armour, what's that?").also { stage++ }
                    2 -> player("Can you make me some?").also { stage = 50 }
                }

            13 ->
                npc(
                    "Split-bark armour is special armour for mages, it's much",
                    "more resistant to physical attacks than normal robes.",
                    "It's actually very easy for me to make, but I've been",
                    "having trouble getting hold of the pieces.",
                ).also { stage++ }

            14 -> options("Well good luck with that.", "Can you make me some?").also { stage++ }
            15 ->
                when (buttonId) {
                    1 -> player("Well good luck with that.").also { stage = END_DIALOGUE }
                    2 -> player("Can you make me some?").also { stage++ }
                }

            50 ->
                npc(
                    "I need bark from a hollow tree, and some fine cloth.",
                    "Unfortunately both these items can be found in",
                    "Morytania, especially the cloth which is found in the",
                    "tombs of shades.",
                ).also { stage++ }

            51 ->
                npc(
                    "Of course I'd happily sell you some at a discounted",
                    "price if you bring me those items.",
                ).also { stage++ }

            52 -> options("Ok, guess I'll go looking then!", "Ok, how much do I need?").also { stage++ }
            53 ->
                when (buttonId) {
                    1 -> player("Ok, guess I'll go looking then!").also { stage = END_DIALOGUE }
                    2 -> player("Ok, how much do I need?").also { stage++ }
                }

            54 ->
                npc(
                    "1 need 1 piece of each for either gloves or boots,",
                    "2 pieces of each for a hat,",
                    "3 pieces of each for leggings,",
                    "and 4 pieces of each for a top.",
                ).also { stage++ }

            55 ->
                npc(
                    "I'll charge you 1,000 coins for either gloves or boots,",
                    "6,000 coins for a hat",
                    "32,000 coins for leggings,",
                    "and 37,000 for a top.",
                ).also { stage++ }

            56 -> player("Ok, guess I'll go looking then!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WIZARD_1263)
    }

    companion object {
        private const val BARK = Items.BARK_3239
        private const val COINS = Items.COINS_995

        fun initExchange(
            player: Player,
            npc: NPC,
        ) {
            sendSkillDialogue(player) {
                withItems(*SplitBark.values().map { it.itemId }.toIntArray())

                create { id, amount ->
                    val barkType = SplitBark.values().find { it.itemId == id } ?: return@create
                    handleExchange(player, npc, barkType, amount)
                }

                calculateMaxAmount { id ->
                    val barkType = SplitBark.values().find { it.itemId == id } ?: return@calculateMaxAmount 0
                    val maxByBark = player.inventory.getAmount(BARK) / barkType.amount
                    val maxByCoins = player.inventory.getAmount(COINS) / barkType.cost
                    min(maxByBark, maxByCoins)
                }
            }
        }

        private fun handleExchange(
            player: Player,
            npc: NPC,
            bark: SplitBark,
            amount: Int,
        ) {
            val totalBarkRequired = bark.amount * amount
            val totalCost = bark.cost * amount

            if (player.inventory.getAmount(COINS) < totalCost && player.inventory.getAmount(BARK) < totalBarkRequired) {
                sendNPCDialogue(
                    player,
                    NPCs.WIZARD_1263,
                    "You need at least $totalBarkRequired pieces of bark and $totalCost coins to make this.",
                )
                return
            }
            if (player.inventory.getAmount(BARK) < totalBarkRequired) {
                sendNPCDialogue(
                    player,
                    NPCs.WIZARD_1263,
                    "You need at least $totalBarkRequired pieces of bark for this.",
                )
                return
            }
            if (player.inventory.getAmount(COINS) < totalCost) {
                sendNPCDialogue(player, NPCs.WIZARD_1263, "You need $totalCost coins to make this.")
                return
            }
            if (player.inventory.freeSlots() < amount) {
                sendPlayerDialogue(player, "I don't have enough inventory space.")
                return
            }

            if (player.inventory.remove(Item(BARK, totalBarkRequired)) &&
                player.inventory.remove(
                    Item(
                        COINS,
                        totalCost,
                    ),
                )
            ) {
                player.inventory.add(Item(bark.itemId, amount))
                sendNPCDialogue(player, NPCs.WIZARD_1263, "There you go, enjoy your new armour!")
                rewardXP(player, Skills.CRAFTING, bark.experience * amount)
                player.dispatch(ResourceProducedEvent(bark.itemId, amount, player))
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
        }

        enum class SplitBark(
            val itemId: Int,
            val cost: Int,
            val amount: Int,
            val experience: Double,
        ) {
            HELM(Items.SPLITBARK_HELM_3385, 6000, 2, 50.0),
            BODY(Items.SPLITBARK_BODY_3387, 37000, 4, 150.0),
            LEGS(Items.SPLITBARK_LEGS_3389, 32000, 3, 120.0),
            GAUNTLETS(Items.SPLITBARK_GAUNTLETS_3391, 1000, 1, 20.0),
            BOOTS(Items.SPLITBARK_BOOTS_3393, 1000, 1, 20.0),
        }
    }
}

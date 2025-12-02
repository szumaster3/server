package content.region.misthalin.draynor.wizardstower.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.Topic
import core.game.event.ResourceProducedEvent
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import kotlin.math.min

@Initializable
class WizardDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("Hello there, can I help you?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> showTopics(
                Topic("What do you do here?", 1),
                Topic("What's that you're wearing?", 3),
                Topic("Can you make me some armour please?", 4),
                Topic("I'm fine, thanks.", END_DIALOGUE)
            )
            1 -> npc("I've been studying the practice of making split-bark", "armour.").also { stage++ }
            2 -> showTopics(
                Topic("Split-bark armour, what's that?", 3),
                Topic("Can you make me some?", 4),
            )
            3 -> npc("Split-bark armour is special armour for mages, it's much", "more resistant to physical attacks than normal robes.", "It's actually very easy for me to make, but I've been", "having trouble getting hold of the pieces.").also { stage = 2 }
            4 -> npc("Certainly, what would you like me to make?").also { stage++ }
            5 -> end().also { makeArmour(player, npc) }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.WIZARD_1263)

    companion object {
        private const val BARK = Items.BARK_3239
        private const val COINS = Items.COINS_995

        fun makeArmour(player: Player, npc: NPC)
        {
            val splitBarkById = SplitBark.values().associateBy { it.itemId }

            sendSkillDialogue(player) {
                withItems(*splitBarkById.keys.toIntArray())

                create { id, amount ->
                    val barkType = splitBarkById[id] ?: return@create
                    handleExchange(player, npc, barkType, amount)
                }

                calculateMaxAmount { id ->
                    val barkType = splitBarkById[id] ?: return@calculateMaxAmount 0

                    val maxByBark = player.inventory.getAmount(BARK) / barkType.amount
                    val maxByCoins = player.inventory.getAmount(COINS) / barkType.cost

                    min(maxByBark, maxByCoins)
                }
            }
        }

        private fun handleExchange(player: Player, npc: NPC, bark: SplitBark, amount: Int) {
            val totalBarkRequired = bark.amount * amount
            val totalCost = bark.cost * amount

            val barkAmount = player.inventory.getAmount(BARK)
            val coinAmount = player.inventory.getAmount(COINS)

            if (barkAmount < totalBarkRequired || coinAmount < totalCost)
            {
                sendNPCDialogue(
                    player, NPCs.WIZARD_1263,
                    "You need at least $totalBarkRequired pieces of bark and $totalCost coins to make this."
                )
                return
            }

            if (player.inventory.freeSlots() < amount)
            {
                sendPlayerDialogue(player, "I don't have enough inventory space.")
                return
            }

            if (player.inventory.remove(Item(BARK, totalBarkRequired))
                && player.inventory.remove(Item(COINS, totalCost))
            ) {
                player.inventory.add(Item(bark.itemId, amount))
                sendNPCDialogue(player, NPCs.WIZARD_1263, "There you go, enjoy your new armour!")
                rewardXP(player, Skills.CRAFTING, bark.experience * amount)
                player.dispatch(ResourceProducedEvent(bark.itemId, amount, player))
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
        }

        enum class SplitBark(val itemId: Int, val cost: Int, val amount: Int, val experience: Double) {
            HELM(Items.SPLITBARK_HELM_3385, 6000, 2, 50.0),
            BODY(Items.SPLITBARK_BODY_3387, 37000, 4, 150.0),
            LEGS(Items.SPLITBARK_LEGS_3389, 32000, 3, 120.0),
            GAUNTLETS(Items.SPLITBARK_GAUNTLETS_3391, 1000, 1, 20.0),
            BOOTS(Items.SPLITBARK_BOOTS_3393, 1000, 1, 20.0),
        }
    }
}

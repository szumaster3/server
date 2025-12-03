package content.global.skill.crafting.misc

import content.global.skill.construction.items.NailType
import content.global.skill.crafting.CraftingDefinition
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import shared.consts.Animations
import shared.consts.Items
import kotlin.math.min

class TribalCraftingItemPlugin : InteractionListener {

    private val snakeskin = Items.SNAKESKIN_6289
    private val nailsRequired = 8

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.HAMMER_2347, *CraftingDefinition.TRIBAL_ITEM_IDS.keys.toIntArray()) { player, _, with ->
            val maskId = with.id
            val shieldId = CraftingDefinition.TRIBAL_ITEM_IDS[maskId] ?: return@onUseWith false

            if (getStatLevel(player, Skills.CRAFTING) < 35) {
                sendMessage(player, "You don't have the crafting level needed to do that.")
                return@onUseWith false
            }
            if (!inInventory(player, snakeskin, 2)) {
                sendMessage(player, "You don't have enough snakeskins.")
                return@onUseWith false
            }

            when (checkNails(player)) {
                NailCheck.NONE ->
                    sendMessage(player, "You don't have nails.")
                NailCheck.NOT_ENOUGH ->
                    sendMessage(player, "You don't have enough nails.")
                NailCheck.HAS_CHEAP ->
                    handleMake(player, maskId, shieldId)
                NailCheck.ONLY_EXPENSIVE ->
                    openDialogue(player, ConfirmExpensiveNailsDialogue(maskId, shieldId))
            }
            return@onUseWith true
        }
    }

    private fun handleMake(player: Player, maskId: Int, shieldId: Int) {
        if (removeAllMaterials(player, maskId)) {
            animate(player, craftAnimation(maskId))
            addItemOrDrop(player, shieldId, 1)
            rewardXP(player, Skills.CRAFTING, 100.0)
        }
    }

    private fun craftAnimation(maskId: Int) = when (maskId) {
        Items.TRIBAL_MASK_6335 -> Animations.CRAFT_SHIELD_GREEN_2410
        Items.TRIBAL_MASK_6337 -> Animations.CRAFT_SHIELD_ORANGE_2411
        Items.TRIBAL_MASK_6339 -> Animations.CRAFT_SHIELD_WHITE_2409
        else -> Animations.CRAFT_SHIELD_GREEN_2410
    }

    private fun removeAllMaterials(player: Player, maskId: Int): Boolean =
        removeItem(player, maskId) && removeItem(player, Item(snakeskin, 2)) && removeNails(player)

    private enum class NailCheck { NONE, NOT_ENOUGH, ONLY_EXPENSIVE, HAS_CHEAP }

    private fun checkNails(player: Player): NailCheck {
        var total = 0
        var hasCheap = false
        var hasExpensive = false

        for (nail in NailType.values) {
            val count = player.inventory.getAmount(Item(nail.itemId))
            if (count > 0) {
                total += count
                if (nail.ordinal <= NailType.STEEL.ordinal) hasCheap = true else hasExpensive = true
            }
        }

        if (total == 0) return NailCheck.NONE
        if (total < nailsRequired) return NailCheck.NOT_ENOUGH
        if (!hasCheap && hasExpensive) return NailCheck.ONLY_EXPENSIVE
        return NailCheck.HAS_CHEAP
    }

    private fun removeNails(player: Player): Boolean {
        var remaining = nailsRequired

        for (type in NailType.values) {
            if (remaining <= 0) break
            val amount = player.inventory.getAmount(Item(type.itemId))
            if (amount > 0) {
                val remove = min(amount, remaining)
                removeItem(player, Item(type.itemId, remove))
                remaining -= remove
            }
        }
        return remaining == 0
    }

    inner class ConfirmExpensiveNailsDialogue(
        private val maskId: Int, private val shieldId: Int
    ) : DialogueFile() {

        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> sendDoubleItemDialogue(player!!, Items.BLACK_NAILS_4821, Items.RUNE_NAILS_4824, "Using these nails will consume higher value nails. Are you sure?").also { stage++ }
                1 -> showTopics(
                    Topic("Yes, use the high-value nails.",2),
                    Topic("No, I'll get cheaper nails.", END_DIALOGUE)
                )
                2 -> {
                    end()
                    if (removeAllMaterials(player!!, maskId)) {
                        animate(player!!, craftAnimation(maskId))
                        addItemOrDrop(player!!, shieldId, 1)
                        rewardXP(player!!, Skills.CRAFTING, 100.0)
                    }
                }
            }
        }
    }
}

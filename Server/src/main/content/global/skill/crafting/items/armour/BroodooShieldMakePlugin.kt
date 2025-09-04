package content.global.skill.crafting.items.armour

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items
import content.global.skill.construction.items.NailType
import core.game.dialogue.DialogueFile
import core.game.node.entity.player.Player
import kotlin.math.min

class BroodooShieldMakePlugin : InteractionListener {

    private val PRODUCTS = mapOf(
        Items.TRIBAL_MASK_6335 to Items.BROODOO_SHIELD_10_6215,
        Items.TRIBAL_MASK_6337 to Items.BROODOO_SHIELD_10_6237,
        Items.TRIBAL_MASK_6339 to Items.BROODOO_SHIELD_10_6259
    )
    private val snakeskinId = Items.SNAKESKIN_6289
    private val nailsRequired = 8

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.HAMMER_2347, *PRODUCTS.keys.toIntArray()) { player, _, with ->
            val maskId = with.id
            val shieldId = PRODUCTS[maskId] ?: return@onUseWith false

            if (!hasLevel(player, Skills.CRAFTING, 35)) return@onUseWith false

            if (!inInventory(player, snakeskinId, 2)) {
                sendMessage(player, "You don't have enough snakeskins.")
                return@onUseWith true
            }

            when (checkNails(player, nailsRequired)) {
                NailCheck.NONE -> {
                    sendMessage(player, "You don't have nails.")
                }
                NailCheck.NOT_ENOUGH -> {
                    sendMessage(player, "You don't have enough nails.")
                }
                NailCheck.ONLY_EXPENSIVE -> {
                    openDialogue(player, CreateBroodooShieldDialogue(maskId,shieldId))
                }
                NailCheck.HAS_CHEAP -> {
                    if (remove(player, maskId)) {
                        craft(player, shieldId, maskId)
                    }
                }
            }
            return@onUseWith true
        }
    }

    private fun remove(player: Player, maskId: Int): Boolean {
        if (!removeItem(player, maskId)) return false
        if (!removeItem(player, Item(snakeskinId, 2))) return false
        if (!removeNails(player)) return false
        return true
    }

    private fun craft(player: Player, shieldId: Int, maskId: Int) {
        val animation = when (maskId) {
            Items.TRIBAL_MASK_6335 -> Animations.CRAFT_SHIELD_GREEN_2410
            Items.TRIBAL_MASK_6337 -> Animations.CRAFT_SHIELD_ORANGE_2411
            Items.TRIBAL_MASK_6339 -> Animations.CRAFT_SHIELD_WHITE_2409
            else -> return
        }
        animate(player, animation)
        addItemOrDrop(player, shieldId, 1)
        rewardXP(player, Skills.CRAFTING, 100.0)
    }

    private fun hasLevel(player: Player, skill: Int, level: Int): Boolean {
        if (getStatLevel(player, skill) < level) {
            sendMessage(player, "You don't have the crafting level needed to do that.")
            return false
        }
        return true
    }

    private enum class NailCheck { NONE, NOT_ENOUGH, ONLY_EXPENSIVE, HAS_CHEAP }

    private fun checkNails(player: Player, amount: Int): NailCheck {
        var total = 0
        var hasCheap = false
        var hasExpensive = false

        for (nail in NailType.values) {
            val count = player.inventory.getAmount(Item(nail.itemId, 1))
            if (count > 0) {
                total += count
                if (nail.ordinal <= NailType.STEEL.ordinal) hasCheap = true
                else hasExpensive = true
            }
        }

        if (total == 0) return NailCheck.NONE
        if (total < amount) return NailCheck.NOT_ENOUGH
        if (!hasCheap && hasExpensive) return NailCheck.ONLY_EXPENSIVE
        return NailCheck.HAS_CHEAP
    }

    private fun removeNails(player: Player): Boolean {
        var nailsToRemove = nailsRequired
        for (nailType in NailType.values) {
            val amountInInventory = player.inventory.getAmount(Item(nailType.itemId, 1))
            if (amountInInventory > 0) {
                val removeAmount = min(amountInInventory, nailsToRemove)
                removeItem(player, Item(nailType.itemId, removeAmount))
                nailsToRemove -= removeAmount
            }
            if (nailsToRemove <= 0) break
        }
        return nailsToRemove == 0
    }

    inner class CreateBroodooShieldDialogue(
        private val maskId: Int,
        private val shieldId: Int
    ) : DialogueFile() {

        init {
            stage = 0
        }

        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> {
                    sendDoubleItemDialogue(player!!, Items.BLACK_NAILS_4821, Items.RUNE_NAILS_4824, "You have no low cost nails which means that you will most likely use black, mithril, adamantite or rune nails, are you sure you want to continue?")
                    stage = 1
                }
                1 -> {
                    options("Yes, I'll make the shield with the higher value nails.", "Er, no. I'll get some cheaper nails...")
                    stage = 2
                }
                2 -> {
                    when(buttonID){
                        1 -> if (remove(player!!, maskId)) end().also { craft(player!!, shieldId, maskId) }
                        2 -> end()
                    }
                }
            }
        }
    }
}

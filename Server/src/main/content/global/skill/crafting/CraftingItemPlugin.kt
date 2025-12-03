package content.global.skill.crafting

import content.global.skill.construction.items.NailType
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds
import kotlin.math.min

class CraftingItemPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles crafting the crab equipment.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, *CraftingDefinition.CRAB_ITEM_IDS.keys.toIntArray()) { player, _, used ->
            val (productId, xp) = CraftingDefinition.CRAB_ITEM_IDS[used.id] ?: return@onUseWith true
            val productName = getItemName(productId).lowercase()

            if (!hasLevelDyn(player, Skills.CRAFTING, 15)) {
                sendDialogue(player, "You need a crafting level of at least 15 in order to do this.")
                return@onUseWith true
            }

            val available = amountInInventory(player, used.id)
            if (available < 1) {
                sendMessage(player, "You do not have enough ${getItemName(used.id).lowercase()} to craft this.")
                return@onUseWith true
            }

            if (available == 1) {
                if (removeItem(player, used.id)) {
                    addItem(player, productId)
                    rewardXP(player, Skills.CRAFTING, xp)
                    sendMessage(player, "You craft a $productName.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(productId)
                create { _, amount ->
                    runTask(player, 1, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, used.id)) {
                            addItem(player, productId)
                            rewardXP(player, Skills.CRAFTING, xp)
                            sendMessage(player, "You craft ${if (amount > 1) "$amount ${productName}s" else "a $productName"}.")
                        } else {
                            sendMessage(player, "You do not have enough ${getItemName(used.id).lowercase()} to continue crafting.")
                        }
                    }
                }
                calculateMaxAmount { available }
            }

            return@onUseWith true
        }

        /*
         * Handles crafting the Feather headdress hats.
         */

        onUseWith(IntType.ITEM, Items.COIF_1169, *CraftingDefinition.FeatherHeaddress.baseIds) { player, used, _ ->
            val item = CraftingDefinition.FeatherHeaddress.forBase(used.id) ?: return@onUseWith false
            if (!hasLevelDyn(player, Skills.CRAFTING, 79)) {
                sendMessage(player, "You need a crafting level of at least 79 in order to do this.")
                return@onUseWith true
            }

            val available = amountInInventory(player, item.base) / 20
            if (available < 1) {
                sendMessage(player, "You don't have enough ${getItemName(item.base).lowercase()} to craft this.")
                return@onUseWith true
            }

            if (available == 1) {
                if (removeItem(player, Item(item.base, 20))) {
                    addItem(player, item.product, 1)
                    rewardXP(player, Skills.CRAFTING, 50.0)
                    sendMessage(player, "You add the feathers to the coif to make a feathered headdress.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(item.product)
                create { _, amount ->
                    runTask(player, 1, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(item.base, 20))) {
                            addItem(player, item.product, 1)
                            rewardXP(player, Skills.CRAFTING, 50.0)
                            sendMessage(player, "You add the feathers to the coif to make ${if (amount > 1) "$amount feathered headdresses" else "a feathered headdress"}.")
                        } else {
                            sendMessage(player, "You don't have enough materials to continue crafting.")
                        }
                    }
                }
                calculateMaxAmount { available }
            }

            return@onUseWith true
        }

        /*
         * Handles crafting the snelm helmets.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, *CraftingDefinition.SnelmItem.SHELLS) { player, _, used ->
            val snelmId = CraftingDefinition.SnelmItem.fromShellId(used.id) ?: return@onUseWith true

            if (!hasLevelDyn(player, Skills.CRAFTING, 15)) {
                sendMessage(player, "You need a crafting level of at least 15 to do this.")
                return@onUseWith true
            }

            val available = amountInInventory(player, used.id)
            if (available < 1) {
                sendMessage(player, "You do not have enough ${getItemName(used.id).lowercase()} to make this.")
                return@onUseWith true
            }

            if (available == 1) {
                if (removeItem(player, snelmId.shell)) {
                    addItem(player, snelmId.product)
                    rewardXP(player, Skills.CRAFTING, 32.5)
                    sendMessage(player, "You craft the shell into a helmet.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(snelmId.product)
                create { _, amount ->
                    runTask(player, 1, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, snelmId.shell)) {
                            addItem(player, snelmId.product)
                            rewardXP(player, Skills.CRAFTING, 32.5)
                            sendMessage(player, "You craft the shell into a helmet.")
                        }
                    }
                }
                calculateMaxAmount { available }
            }

            return@onUseWith true
        }

        /*
         * Handles crafting the battlestaves.
         */

        onUseWith(IntType.ITEM,
            CraftingDefinition.Battlestaff.ORB_ID,
            CraftingDefinition.Battlestaff.BATTLESTAFF_ID
        ) { player, used, with ->
            val product = CraftingDefinition.Battlestaff.forId(used.id) ?: return@onUseWith true

            if (!hasLevelDyn(player, Skills.CRAFTING, product.requiredLevel)) {
                sendMessage(player, "You need a crafting level of ${product.requiredLevel} to make this.")
                return@onUseWith true
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, product.required) &&
                    removeItem(player, CraftingDefinition.Battlestaff.BATTLESTAFF_ID))
                {
                    playAudio(player, Sounds.ATTACH_ORB_2585)
                    addItem(player, product.productId, product.amount)
                    rewardXP(player, Skills.CRAFTING, product.experience)
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(product.productId)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask

                        if (removeItem(player, product.required) &&
                            removeItem(player, CraftingDefinition.Battlestaff.BATTLESTAFF_ID))
                        {
                            playAudio(player, Sounds.ATTACH_ORB_2585)
                            addItem(player, product.productId)
                            rewardXP(player, Skills.CRAFTING, product.experience)
                        }

                        if (product.productId == Items.AIR_BATTLESTAFF_1397) {
                            finishDiaryTask(player, DiaryType.VARROCK, 2, 6)
                        } else {
                            return@runTask
                        }
                    }
                }

                calculateMaxAmount { _ ->
                    min(amountInInventory(player, with.id), amountInInventory(player, used.id))
                }
            }

            return@onUseWith true
        }

        /*
         * Handles crafting broodo shields.
         */

        onUseWith(IntType.ITEM, Items.HAMMER_2347, *CraftingDefinition.TRIBAL_ITEM_IDS.keys.toIntArray()) { player, _, with ->
            val maskId = with.id
            val shieldId = CraftingDefinition.TRIBAL_ITEM_IDS[maskId] ?: return@onUseWith false

            if (getStatLevel(player, Skills.CRAFTING) < 35) {
                sendMessage(player, "You don't have the crafting level needed to do that.")
                return@onUseWith false
            }

            if (!inInventory(player, Items.SNAKESKIN_6289, 2)) {
                sendMessage(player, "You don't have enough snakeskins.")
                return@onUseWith false
            }

            var totalNails = 0
            var hasCheap = false
            var hasExpensive = false
            for (nail in NailType.values) {
                val count = player.inventory.getAmount(Item(nail.itemId))
                if (count > 0) {
                    totalNails += count
                    if (nail.ordinal <= NailType.STEEL.ordinal) hasCheap = true else hasExpensive = true
                }
            }

            when {
                totalNails == 0 -> sendMessage(player, "You don't have nails.")
                totalNails < 8 -> sendMessage(player, "You don't have enough nails.")
                !hasCheap && hasExpensive -> {
                    object : DialogueFile() {
                        override fun handle(componentID: Int, buttonID: Int) {
                            when (stage) {
                                0 -> sendDoubleItemDialogue(player, Items.BLACK_NAILS_4821, Items.RUNE_NAILS_4824, "Using these nails will consume higher value nails. Are you sure?").also { stage++ }
                                1 -> showTopics(
                                    Topic("Yes, use the high-value nails.",2),
                                    Topic("No, I'll get cheaper nails.", END_DIALOGUE)
                                )
                                2 -> {
                                    end()
                                    var canCraft = true
                                    if (!removeItem(player, maskId) || !removeItem(player, Item(Items.SNAKESKIN_6289, 2))) canCraft = false

                                    var remaining = 8
                                    for (type in NailType.values) {
                                        if (remaining <= 0) break
                                        val amt = player.inventory.getAmount(Item(type.itemId))
                                        if (amt > 0) {
                                            val remove = min(amt, remaining)
                                            removeItem(player, Item(type.itemId, remove))
                                            remaining -= remove
                                        }
                                    }
                                    if (remaining > 0) canCraft = false

                                    if (canCraft) {
                                        val anim = when (maskId) {
                                            Items.TRIBAL_MASK_6335 -> Animations.CRAFT_SHIELD_GREEN_2410
                                            Items.TRIBAL_MASK_6337 -> Animations.CRAFT_SHIELD_ORANGE_2411
                                            Items.TRIBAL_MASK_6339 -> Animations.CRAFT_SHIELD_WHITE_2409
                                            else -> Animations.CRAFT_SHIELD_GREEN_2410
                                        }
                                        animate(player, anim)
                                        addItemOrDrop(player, shieldId, 1)
                                        rewardXP(player, Skills.CRAFTING, 100.0)
                                    }
                                }
                            }
                        }
                    }
                }
                else -> {
                    var canCraft = true
                    if (!removeItem(player, maskId) || !removeItem(player, Item(Items.SNAKESKIN_6289, 2))) canCraft = false

                    var remaining = 8
                    for (type in NailType.values) {
                        if (remaining <= 0) break
                        val amt = player.inventory.getAmount(Item(type.itemId))
                        if (amt > 0) {
                            val remove = min(amt, remaining)
                            removeItem(player, Item(type.itemId, remove))
                            remaining -= remove
                        }
                    }
                    if (remaining > 0) canCraft = false

                    if (canCraft) {
                        val anim = when (maskId) {
                            Items.TRIBAL_MASK_6335 -> Animations.CRAFT_SHIELD_GREEN_2410
                            Items.TRIBAL_MASK_6337 -> Animations.CRAFT_SHIELD_ORANGE_2411
                            Items.TRIBAL_MASK_6339 -> Animations.CRAFT_SHIELD_WHITE_2409
                            else -> Animations.CRAFT_SHIELD_GREEN_2410
                        }
                        animate(player, anim)
                        addItemOrDrop(player, shieldId, 1)
                        rewardXP(player, Skills.CRAFTING, 100.0)
                    }
                }
            }
            return@onUseWith true
        }
    }

}
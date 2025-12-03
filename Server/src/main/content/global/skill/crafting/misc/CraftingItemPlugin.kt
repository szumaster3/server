package content.global.skill.crafting.misc

import content.global.skill.crafting.CraftingDefinition
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
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
    }

}
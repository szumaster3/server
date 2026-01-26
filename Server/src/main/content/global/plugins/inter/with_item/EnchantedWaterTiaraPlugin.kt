package content.global.plugins.inter.with_item

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Sounds

class EnchantedWaterTiaraPlugin : InteractionListener {
    private val tiaraIDs = intArrayOf(Items.WATER_TIARA_5531, Items.ENCHANTED_WATER_TIARA_11969)

    override fun defineListeners() {

        /*
         * Handles charging the Enchanted water tiara.
         */

        onUseWith(IntType.ITEM, Items.WATER_RUNE_555, *tiaraIDs) { player, used, with ->
            val tiara = with.asItem() ?: return@onUseWith false
            val runeItem = used.asItem() ?: return@onUseWith false

            val runeAmount = runeItem.amount
            val runeCharges = (runeAmount / 3) * 1000

            if (tiara.id == Items.WATER_TIARA_5531 && !player.isAdmin) {
                if (getStatLevel(player, Skills.RUNECRAFTING) < 50 || getStatLevel(player, Skills.MAGIC) < 50) {
                    sendMessage(player, "You need level 50 in both Runecrafting and Magic to enchant this tiara.")
                    return@onUseWith false
                }

                if (!hasRequirement(player, Quests.DEALING_WITH_SCABARAS, false)) {
                    sendMessage(player, "You need to complete the quest 'Dealing with Scabaras' to enchant this tiara.")
                    return@onUseWith false
                }

                if (runeAmount < 3) {
                    sendMessage(player, "You need at least 3 Water Runes to create an Enchanted Water Tiara.")
                    return@onUseWith false
                }

                val charges = runeCharges.coerceAtMost(500_000)
                val enchantedTiara = Item(Items.ENCHANTED_WATER_TIARA_11969)
                setCharge(enchantedTiara, charges)

                val tiaraSlot = tiara.slot
                if (removeItem(player, tiara)) {
                    replaceSlot(player, tiaraSlot, enchantedTiara)
                    removeItem(player, Item(Items.WATER_RUNE_555, charges / 1000 * 3))
                    sendDialogue(
                        player,
                        "You transform the Water Tiara into an Enchanted Water Tiara with $charges charges."
                    )
                    return@onUseWith true
                } else {
                    return@onUseWith false
                }
            }

            if (tiara.id == Items.ENCHANTED_WATER_TIARA_11969) {
                val currentCharges = getCharge(tiara)
                if (currentCharges >= 500_000) {
                    sendMessage(player, "The Enchanted Water Tiara is already fully charged.")
                    return@onUseWith true
                }

                val maxAddableCharges = 500_000 - currentCharges
                val addedCharges = runeCharges.coerceAtMost(maxAddableCharges)

                adjustCharge(tiara, addedCharges)

                val runesToRemove = addedCharges / 1000 * 3
                if (inInventory(player, Items.WATER_RUNE_555) && removeItem(player, Item(Items.WATER_RUNE_555, runesToRemove))) {
                    sendDialogue(player, "You add $runesToRemove Water Runes to the Enchanted Water Tiara.")
                } else {
                    sendMessage(player, "You do not have enough Water Runes.")
                }

                return@onUseWith true
            }

            return@onUseWith false
        }

        /*
         * Handles destroying the Enchanted water tiara.
         */

        on(Items.ENCHANTED_WATER_TIARA_11969, IntType.ITEM, "destroy") { player, node ->
            val item = node as Item
            val charges = getCharge(item)

            if (charges > 0) {
                sendDestroyItemDialogue(player, Items.ENCHANTED_WATER_TIARA_11969, item.name)
                addDialogueAction(player) { player, button ->
                    if (button == 3) {
                        replaceSlot(player, item.slot, Item(Items.WATER_TIARA_5531))
                        addItemOrDrop(player, Items.WATER_RUNE_555, charges / 1000 * 3)
                        sendDialogue(
                            player,
                            "The Enchanted Water Tiara has been destroyed and turned back into a Water Tiara. You recover ${charges / 1000 * 3} Water Runes.",
                        )
                        playAudio(player, Sounds.DESTROY_OBJECT_2381)
                    }
                }
            } else {
                sendMessage(player, "The Enchanted Water Tiara is empty and cannot be destroyed.")
            }
            return@on true
        }

        /*
         * Handles checking the charges of Enchanted water tiara.
         */

        on(Items.ENCHANTED_WATER_TIARA_11969, IntType.ITEM, "check-charges") { player, node ->
            val charges = getCharge(node as Item)
            sendMessage(player, "The Enchanted Water Tiara has ${charges / 1000} drinkable charges remaining.")
            return@on true
        }

        onUseWith(IntType.ITEM, Items.CUP_OF_WATER_4458, Items.ENCHANTED_WATER_TIARA_11969) { player, used, with ->
            val tiara = with.asItem()

            if (getCharge(tiara) >= 500_000) {
                sendMessage(player, "The enchanted water tiara is already full.")
                return@onUseWith true
            }

            if (removeItem(player, used.asItem())) {
                adjustCharge(tiara, 1000)
                replaceSlot(player, used.asItem().slot, Item(Items.EMPTY_CUP_1980))
                sendMessage(player, "You add water to the enchanted water tiara.")
            }

            return@onUseWith true
        }
    }
}

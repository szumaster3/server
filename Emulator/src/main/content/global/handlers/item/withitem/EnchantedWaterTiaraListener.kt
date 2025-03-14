package content.global.handlers.item.withitem

import core.api.*
import core.api.quest.hasRequirement
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Sounds

class EnchantedWaterTiaraListener : InteractionListener {
    val tiaraIDs = intArrayOf(Items.WATER_TIARA_5531, Items.ENCHANTED_WATER_TIARA_11969)

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.WATER_RUNE_555, *tiaraIDs) { player, used, with ->
            val tiara = with.asItem()
            val runeAmount = used.asItem().amount
            val runeCharges = (runeAmount / 3) * 1000

            if (tiara?.id == Items.WATER_TIARA_5531 && !player.isAdmin) {
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

                val charges = runeCharges.coerceAtMost(500000)
                val enchantedTiara = Item(Items.ENCHANTED_WATER_TIARA_11969)
                setCharge(enchantedTiara, charges)
                replaceSlot(player, tiara.slot, enchantedTiara)
                removeItem(player, Item(used.asItem().id, charges / 1000 * 3))
                sendDialogue(
                    player,
                    "You transform the Water Tiara into an Enchanted Water Tiara with $charges charges.",
                )
                return@onUseWith true
            }

            if (tiara?.id == Items.ENCHANTED_WATER_TIARA_11969) {
                val currentCharges = getCharge(tiara)
                if (currentCharges >= 500000) {
                    sendMessage(player, "The Enchanted Water Tiara is already fully charged.")
                    return@onUseWith true
                }

                val maxAddableCharges = 500000 - currentCharges
                val addedCharges = runeCharges.coerceAtMost(maxAddableCharges)

                adjustCharge(tiara, addedCharges)
                removeItem(player, Item(used.asItem().id, addedCharges / 1000 * 3))
                sendDialogue(player, "You add ${addedCharges / 1000 * 3} Water Runes to the Enchanted Water Tiara.")
                return@onUseWith true
            }

            return@onUseWith false
        }

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

        on(Items.ENCHANTED_WATER_TIARA_11969, IntType.ITEM, "check-charges") { player, node ->
            val charges = getCharge(node as Item)
            sendMessage(player, "The Enchanted Water Tiara has ${charges / 1000} drinkable charges remaining.")
            return@on true
        }

        onUseWith(IntType.ITEM, Items.CUP_OF_WATER_4458, Items.ENCHANTED_WATER_TIARA_11969) { player, used, with ->
            val tiara = with.asItem()
            if (getCharge(tiara) >= 500000) {
                sendMessage(player, "The enchanted water tiara is already full.")
                return@onUseWith true
            }
            adjustCharge(tiara, 1000)
            replaceSlot(player, used.asItem().slot, Item(Items.EMPTY_CUP_1980))
            sendMessage(player, "You add water to the enchanted water tiara.")
            return@onUseWith true
        }
    }
}

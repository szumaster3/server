package content.global.skill.summoning.items

import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Quests

class EnchantedHeadgearListener : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles Enchant headgear.
         */

        on(NPCs.PIKKUPSTIX_6970, IntType.NPC, "Enchant") { player, _ ->
            if (!isQuestComplete(player, Quests.WOLF_WHISTLE)) {
                sendMessage(player, "You need to complete Wolf Whistle to enchant the headgear.")
                return@on true
            }

            if (!anyInInventory(player, *normalItemIDs)) {
                sendNPCDialogue(player, NPCs.PIKKUPSTIX_6970, "You do not have items that can be enchanted.")
                return@on true
            }

            sendNPCDialogue(player, NPCs.PIKKUPSTIX_6970, "What would you like disenchanted or enchanted?")
            addDialogueAction(player) { _, _ ->
                sendItemSelect(player, "Choose") { slot, _ ->
                    val item = player.inventory[slot] ?: return@sendItemSelect
                    enchant(player, item, 1)
                }
            }
            return@on true
        }

        /*
         * Handles uncharge the headgear.
         */

        on(chargedIDs, IntType.ITEM, "Uncharge") { player, node ->
            val item = node.asItem() ?: return@on true
            if (!EnchantedHeadgearScrolls.hasScrolls(player, item.id)) {
                sendDialogue(player, "There are no scrolls stored in this headgear.")
                return@on true
            }
            if (freeSlots(player) == 0) {
                sendDialogue(player, "You need at least one free inventory slot to remove the scrolls.")
                return@on true
            }
            EnchantedHeadgearScrolls.unchargeHeadgear(player, item.id)
            return@on true
        }

        /*
         * Handles check stored scrolls in charged headgear.
         */

        on(chargedIDs, IntType.ITEM, "Commune", "Operate") { player, node ->
            EnchantedHeadgearScrolls.checkHeadgear(player, node.id)
            return@on true
        }

        /*
         * Handles store scrolls into headgear.
         */

        onUseWith(IntType.ITEM, enchantedIDs, *allowedScrollIDs) { player, used, with ->
            val enchantedItem = used.asItem() ?: return@onUseWith true
            val scrollItem = with.asItem() ?: return@onUseWith true

            val headgear = EnchantedHeadgear.forEnchanted(enchantedItem) ?: return@onUseWith true

            if (getStatLevel(player, Skills.SUMMONING) < headgear.requiredLevel) {
                sendMessage(player, "You need Summoning level ${headgear.requiredLevel} to enchant this headgear.")
                return@onUseWith true
            }

            val chargedItem = EnchantedHeadgear.getChargedItem(enchantedItem) ?: run {
                sendMessage(player, "Failed to charge the headgear.")
                return@onUseWith true
            }

            EnchantedHeadgearScrolls.addScrollToChargedItem(
                chargedItemId = chargedItem.id,
                scrollId = scrollItem.id,
                amount = scrollItem.amount,
                player = player
            )

            val slot = enchantedItem.index
            if (slot >= 0) {
                replaceSlot(player, slot, chargedItem)
            } else {
                sendMessage(player, "Failed to update your inventory.")
            }

            return@onUseWith true
        }
    }

    companion object {
        /**
         * IDs of headgear items that can be enchanted.
         */
        private val normalItemIDs = EnchantedHeadgear.values().map { it.defaultItem.id }.toIntArray()

        /**
         * All enchanted and charged item ids.
         */
        private val enchantedIDs = EnchantedHeadgear.values().map { it.enchantedItem.id }.toIntArray()

        /**
         * IDs of fully charged enchanted headgear items.
         */
        private val chargedIDs = EnchantedHeadgear.values().map { it.chargedItem.id }.toIntArray()

        /**
         * IDs of scrolls allowed to store in headgear.
         */
        val allowedScrollIDs = EnchantedHeadgearScrolls.allowedScrollIDs

        /**
         * Enchants the provided item if possible.
         *
         * @param player The player.
         * @param slot The item to enchant.
         * @param option Enchant option (usually 1 = confirm).
         * @return `true` if enchantment succeeds, `false` otherwise.
         */
        fun enchant(player: Player, slot: Item, option: Int): Boolean {
            val item = EnchantedHeadgear.forItem(slot) ?: return false

            if (getStatLevel(player, Skills.SUMMONING) < item.requiredLevel) return false
            if (option == 1) lock(player, 1)
            animate(findLocalNPC(player, NPCs.PIKKUPSTIX_6970)!!, Animations.CAST_SPELL_711)
            sendGraphics(Graphics(434, 100), player.location)

            val npc = NPC(NPCs.PIKKUPSTIX_6971)
            return when {
                slot.id == item.defaultItem.id -> {
                    removeItem(player, item.defaultItem.id)
                    addItem(player, item.enchantedItem.id)
                    dialogue(player) {
                        npc(npc, FaceAnim.NEUTRAL, "Good choice. Here you go, you can now store spells on", "it.")
                        player(FaceAnim.CALM_TALK, "Excellent. Thank you!")
                    }
                    true
                }

                else -> false
            }
        }
    }
}

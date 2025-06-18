package content.global.skill.summoning.item

import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class EnchantedHeadgearPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles enchanting headgear via Pikkupstix NPC.
         */

        on(NPCs.PIKKUPSTIX_6970, IntType.NPC, "Enchant") { player, _ ->

            if (!isQuestComplete(player, Quests.WOLF_WHISTLE)) {
                sendMessage(player, "You need to complete Wolf Whistle to enchant the headgear.")
                return@on true
            }

            if (!anyInInventory(player, *DEFAULT_ID)) {
                sendNPCDialogue(player, NPCs.PIKKUPSTIX_6970, "You do not have items that can be enchanted.")
                return@on true
            }

            sendNPCDialogue(player, NPCs.PIKKUPSTIX_6970, "What would you like disenchanted or enchanted?")
            addDialogueAction(player) { _, _ ->
                sendItemSelect(player, "Choose an item") { slot, _ ->
                    val item = player.inventory[slot] ?: return@sendItemSelect
                    enchant(player, item, 1)
                }
            }
            return@on true
        }

        /*
         * Handles uncharging the headgear.
         */

        on(CHARGED_ID, IntType.ITEM, "Uncharge") { player, node ->
            val item = node.asItem() ?: return@on true
            val enchManager = player.enchgearManager

            if (!enchManager.hasScrolls(item.id)) {
                sendMessage(player, "Your headgear has no scrolls to remove.")
                return@on true
            }

            if (freeSlots(player) == 0) {
                sendDialogue(player, "You need at least one free inventory slot to remove the scrolls.")
                return@on true
            }

            val enchantedGear = enchManager.enchantedGear[item.id] ?: run {
                sendMessage(player, "Could not find the charged headgear in your inventory.")
                return@on true
            }

            val firstScroll = enchantedGear.container.toArray().firstOrNull { it != null } ?: run {
                sendMessage(player, "No scrolls found in the headgear.")
                return@on true
            }

            val success = enchManager.withdrawScrolls(item.id, firstScroll.id, firstScroll.amount)
            if (success) {
                sendMessages(player,
                    "You remove the scrolls. You will need to use a Summoning scroll on it to charge the",
                    "headgear up once more."
                )
            } else {
                player.debug("Failed to remove the scrolls.")
            }
            return@on true
        }

        /*
         * Handles checking stored scrolls in charged headgear.
         */

        on(CHARGED_ID, IntType.ITEM, "Commune", "Operate") { player, node ->
            val item = node.asItem() ?: return@on true
            val enchManager = player.enchgearManager
            enchManager.checkHeadgear(item.id)
            return@on true
        }

        /*
         * Handles using a scroll on enchanted gear to charge it.
         */

        onUseWith(IntType.ITEM, ENCHANTED_ID, *ALLOWED_SCROLL_ID) { player, used, with ->
            val enchantedItem = used.asItem() ?: return@onUseWith true
            val scrollItem = with.asItem() ?: return@onUseWith true

            val (headgear, type) = EnchantedHeadgear.itemMap[enchantedItem.id] ?: return@onUseWith true
            if (type != EnchantedHeadgear.HeadgearType.ENCHANTED) return@onUseWith true

            if (getStatLevel(player, Skills.SUMMONING) < headgear.requiredLevel) {
                sendMessage(player, "You need Summoning level ${headgear.requiredLevel} to enchant this headgear.")
                return@onUseWith true
            }

            val chargedItem = headgear.chargedItem
            val enchManager = player.enchgearManager

            enchManager.addScrolls(chargedItem.id, scrollItem.id, scrollItem.amount)

            val slot = enchantedItem.index
            if (slot >= 0) {
                replaceSlot(player, slot, chargedItem)
            }
            return@onUseWith true
        }
    }

    companion object {
        private val DEFAULT_ID = EnchantedHeadgear.values().map { it.defaultItem.id }.toIntArray()
        private val ENCHANTED_ID = EnchantedHeadgear.values().map { it.enchantedItem.id }.toIntArray()
        private val CHARGED_ID = EnchantedHeadgear.values().map { it.chargedItem.id }.toIntArray()

        val ALLOWED_SCROLL_ID = intArrayOf(
            Items.HOWL_SCROLL_12425,
            Items.DREADFOWL_STRIKE_SCROLL_12445,
            Items.SLIME_SPRAY_SCROLL_12459,
            Items.PESTER_SCROLL_12838,
            Items.ELECTRIC_LASH_SCROLL_12460,
            Items.FIREBALL_ASSAULT_SCROLL_12839,
            Items.SANDSTORM_SCROLL_12446,
            Items.VAMPIRE_TOUCH_SCROLL_12447,
            Items.BRONZE_BULL_RUSH_SCROLL_12461,
            Items.EVIL_FLAMES_SCROLL_12448,
            Items.PETRIFYING_GAZE_SCROLL_12458,
            Items.IRON_BULL_RUSH_SCROLL_12462,
            Items.ABYSSAL_DRAIN_SCROLL_12454,
            Items.DISSOLVE_SCROLL_12453,
            Items.AMBUSH_SCROLL_12836,
            Items.RENDING_SCROLL_12840,
            Items.DOOMSPHERE_SCROLL_12455,
            Items.DUST_CLOUD_SCROLL_12468,
            Items.STEEL_BULL_RUSH_SCROLL_12463,
            Items.POISONOUS_BLAST_SCROLL_12467,
            Items.MITH_BULL_RUSH_SCROLL_12464,
            Items.TOAD_BARK_SCROLL_12452,
            Items.FAMINE_SCROLL_12830,
            Items.ARCTIC_BLAST_SCROLL_12451,
            Items.RISE_FROM_THE_ASHES_SCROLL_14622,
            Items.CRUSHING_CLAW_SCROLL_12449,
            Items.MANTIS_STRIKE_SCROLL_12450,
            Items.INFERNO_SCROLL_12841,
            Items.ADDY_BULL_RUSH_SCROLL_12465,
            Items.DEADLY_CLAW_SCROLL_12831,
            Items.ACORN_MISSILE_SCROLL_12457,
            Items.SPIKE_SHOT_SCROLL_12456,
            Items.EBON_THUNDER_SCROLL_12837,
            Items.SWAMP_PLAGUE_SCROLL_12832,
            Items.RUNE_BULL_RUSH_SCROLL_12466,
            Items.BOIL_SCROLL_12833,
            Items.IRON_WITHIN_SCROLL_12828,
            Items.STEEL_OF_LEGENDS_SCROLL_12825
        )

        fun enchant(player: Player, item: Item, option: Int): Boolean {
            val (headgear, type) = EnchantedHeadgear.itemMap[item.id] ?: return false

            if (getStatLevel(player, Skills.SUMMONING) < headgear.requiredLevel) {
                sendMessage(player, "You need a Summoning level of ${headgear.requiredLevel} to do this.")
                return true
            }

            if (option == 1) {
                val npc = findLocalNPC(player, NPCs.PIKKUPSTIX_6970) ?: return false
                lock(player, 1)
                animate(npc, Animations.CAST_SPELL_711)
                sendGraphics(Graphics(434, 100), player.location)

                when (type) {
                    EnchantedHeadgear.HeadgearType.DEFAULT -> {
                        removeItem(player, item.id)
                        addItem(player, headgear.enchantedItem.id)
                        dialogue(player) {
                            npc(NPCs.PIKKUPSTIX_6971, FaceAnim.NEUTRAL, "Good choice. Here you go, you can now store spells on", "it.")
                            player(FaceAnim.CALM_TALK, "Excellent. Thank you!")
                        }
                        return true
                    }

                    EnchantedHeadgear.HeadgearType.ENCHANTED -> {
                        if (player.enchgearManager.hasScrolls(headgear.chargedItem.id)) {
                            sendMessage(player, "Remove the stored scrolls from the headgear first.")
                            return true
                        }

                        removeItem(player, item.id)
                        addItem(player, headgear.defaultItem.id)
                        return true
                    }

                    EnchantedHeadgear.HeadgearType.CHARGED -> {
                        sendMessage(player, "Remove the stored scrolls first before reverting this item.")
                        return true
                    }
                }
            }
            return false
        }
    }
}

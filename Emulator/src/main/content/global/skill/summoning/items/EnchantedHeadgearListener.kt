package content.global.skill.summoning.items

import content.region.asgarnia.dialogue.taverley.PikkupstixDialogueExtension
import core.api.*
import core.api.quest.isQuestComplete
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

class EnchantedHeadgearListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.PIKKUPSTIX_6970, IntType.NPC, "Enchant") { player, _ ->
            face(findLocalNPC(player, NPCs.PIKKUPSTIX_6970)!!, player, 1)
            if (!isQuestComplete(player, Quests.WOLF_WHISTLE)) {
                sendMessage(player, "You need to complete Wolf Whistle to Enchant the headgear.")
                return@on true
            }
            sendNPCDialogue(player, NPCs.PIKKUPSTIX_6970, "What would you like disenchanted or enchanted?")
            addDialogueAction(player) { _, _ ->
                sendItemSelect(player, "Choose") { slot, index ->
                    val item = player.inventory[slot] ?: return@sendItemSelect
                    enchant(player, item, index)
                }
            }
            return@on true
        }

        on(chargedIDs, IntType.ITEM, "Uncharge") { player, node ->
            val item = node.asItem() ?: return@on true
            val enchantId = EnchantedHeadgear.forItem(item) ?: return@on true
            sendMessages(
                player,
                "You remove the scrolls. You will need to use a Summoning scroll on it to charge the",
                "headgear up once more.",
            )
            replaceSlot(player, item.slot, enchantId.enchantedItem)
            return@on true
        }
    }

    companion object {
        private val enchantIDs =
            intArrayOf(
                Items.ANTLERS_12204,
                Items.ADAMANT_FULL_HELM_E_12658,
                Items.SLAYER_HELMET_E_14636,
                Items.SNAKESKIN_BANDANA_E_12660,
                Items.LIZARD_SKULL_12207,
                Items.SPLITBARK_HELM_E_12662,
                Items.RUNE_FULL_HELM_E_12664,
                Items.WARRIOR_HELM_E_12676,
                Items.BERSERKER_HELM_E_12674,
                Items.ARCHER_HELM_E_12672,
                Items.FARSEER_HELM_E_12678,
                Items.HELM_OF_NEITIZNOT_E_12680,
                Items.FEATHER_HEADDRESS_12210,
                Items.FEATHER_HEADDRESS_12222,
                Items.FEATHER_HEADDRESS_12216,
                Items.FEATHER_HEADDRESS_12219,
                Items.FEATHER_HEADDRESS_12213,
                Items.DRAGON_MED_HELM_E_12666,
                Items.LUNAR_HELM_E_12668,
                Items.ARMADYL_HELMET_E_12670,
            )
        private val chargedIDs = EnchantedHeadgear.values().map { it.chargedItem.id }.toIntArray()
        private val scrollsIDs =
            intArrayOf(
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
                Items.STEEL_OF_LEGENDS_SCROLL_12825,
            )

        @JvmStatic
        fun enchant(
            player: Player,
            slot: Item,
            option: Int,
        ): Boolean {
            val item = EnchantedHeadgear.forItem(slot) ?: return false

            if (getStatLevel(player, Skills.SUMMONING) < item.requiredLevel) return false
            if (option == 1) lock(player, 1)
            animate(findLocalNPC(player, NPCs.PIKKUPSTIX_6970)!!, Animations.CAST_SPELL_711)
            sendGraphics(Graphics(1574, 100), player.location)

            return when {
                slot.id == item.defaultItem.id -> {
                    removeItem(player, item.defaultItem.id)
                    addItem(player, item.enchantedItem.id)
                    openDialogue(player, PikkupstixDialogueExtension())
                    true
                }

                else -> false
            }
        }
    }
}

package content.global.plugins.item.withitem

import content.data.GameAttributes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import shared.consts.Animations
import shared.consts.Graphics
import shared.consts.Items
import shared.consts.Sounds

class EnchantTabletPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles breaking the tabs.
         */

        on(IntType.ITEM, "break") { player, node ->
            if (hasTimerActive(player, GameAttributes.TELEBLOCK_TIMER)) {
                sendMessage(player, "A magical force has stopped you from teleporting.")
                return@on true
            }

            if(!removeItem(player, node.id)) return@on false
            queueScript(player, 1, strength = QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        val itemsMap = when (node.id) {
                            Items.ENCHANT_SAPPHIRE_8016     -> LVL_1_ENCHANT
                            Items.ENCHANT_EMERALD_8017      -> LVL_2_ENCHANT
                            Items.ENCHANT_RUBY_8018         -> LVL_3_ENCHANT
                            Items.ENCHANT_DIAMOND_8019      -> LVL_4_ENCHANT
                            Items.ENCHANT_DRAGONSTN_8020    -> LVL_5_ENCHANT
                            Items.ENCHANT_ONYX_8021         -> LVL_6_ENCHANT
                            else -> return@queueScript stopExecuting(player)
                        }

                        for (item in player.inventory.toArray()) {
                            if (item == null) continue
                            val product = itemsMap[item.id] ?: continue

                            removeItem(player, item.id)
                            addItem(player, product)
                        }

                        return@queueScript keepRunning(player)
                    }

                    1 -> {
                        playAudio(player, Sounds.POH_TABLET_BREAK_979)
                        visualize(player, Animations.BREAK_SPELL_TABLET_B_4070,Graphics.SICKLE_ENCHANTMENT_1604)
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }

            return@on true
        }
    }

    companion object {
        private val LVL_1_ENCHANT = mapOf(
            Items.SAPPHIRE_RING_1637      to Items.RING_OF_RECOIL_2550,
            Items.SAPPHIRE_NECKLACE_1656  to Items.GAMES_NECKLACE8_3853,
            Items.SAPPHIRE_AMULET_1694    to Items.AMULET_OF_MAGIC_1727,
            Items.SAPPHIRE_BRACELET_11072 to Items.BRACELET_OF_CLAY_11074
        )
        private val LVL_2_ENCHANT = mapOf(
            Items.EMERALD_RING_1639            to Items.RING_OF_DUELLING8_2552,
            Items.EMERALD_NECKLACE_1658        to Items.BINDING_NECKLACE_5521,
            Items.EMERALD_AMULET_1696          to Items.AMULET_OF_DEFENCE_1729,
            Items.EMERALD_BRACELET_11076       to Items.CASTLEWAR_BRACE3_11079,
            Items.SILVER_SICKLE_EMERALDB_13155 to Items.ENCHANTED_SICKLE_EMERALDB_13156
        )
        private val LVL_3_ENCHANT = mapOf(
            Items.RUBY_RING_1641      to Items.RING_OF_FORGING_2568,
            Items.RUBY_NECKLACE_1660  to Items.DIGSITE_PENDANT_5_11194,
            Items.RUBY_AMULET_1698    to Items.AMULET_OF_STRENGTH_1725,
            Items.RUBY_BRACELET_11085 to Items.INOCULATION_BRACE_11088
        )
        private val LVL_4_ENCHANT = mapOf(
            Items.DIAMOND_RING_1643      to Items.RING_OF_LIFE_2570,
            Items.DIAMOND_NECKLACE_1662  to Items.PHOENIX_NECKLACE_11090,
            Items.DIAMOND_AMULET_1700    to Items.AMULET_OF_POWER_1731,
            Items.DIAMOND_BRACELET_11092 to Items.FORINTHRY_BRACE5_11095
        )
        private val LVL_5_ENCHANT = mapOf(
            Items.DRAGONSTONE_RING_1645 to Items.RING_OF_WEALTH_2572,
            Items.DRAGON_NECKLACE_1664  to Items.SKILLS_NECKLACE4_11105,
            Items.DRAGONSTONE_AMMY_1702 to Items.AMULET_OF_GLORY4_1712,
            Items.DRAGON_BRACELET_11115 to Items.COMBAT_BRACELET4_11118
        )
        private val LVL_6_ENCHANT = mapOf(
            Items.ONYX_RING_6575      to Items.RING_OF_STONE_6583,
            Items.ONYX_NECKLACE_6577  to Items.BERSERKER_NECKLACE_11128,
            Items.ONYX_AMULET_6581    to Items.AMULET_OF_FURY_6585,
            Items.ONYX_BRACELET_11130 to Items.REGEN_BRACELET_11133
        )
    }
}

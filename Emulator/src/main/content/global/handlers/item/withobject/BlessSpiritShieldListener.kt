package content.global.handlers.item.withobject

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class BlessSpiritShieldListener : InteractionListener {
    companion object {
        private val COMPONENTS = intArrayOf(Items.HOLY_ELIXIR_13754, Items.SPIRIT_SHIELD_13734)
        private val ALTARS =
            intArrayOf(
                Scenery.ALTAR_409,
                Scenery.ALTAR_2640,
                Scenery.ALTAR_4008,
                Scenery.ALTAR_18254,
                Scenery.ALTAR_19145,
                Scenery.ALTAR_24343,
                Scenery.SARADOMIN_ALTAR_26287,
                Scenery.ALTAR_27661,
                Scenery.ALTAR_34616,
                Scenery.ALTAR_36972,
                Scenery.ALTAR_39842,
            )
        private val ANVILS =
            intArrayOf(
                Scenery.ANVIL_2782,
                Scenery.ANVIL_2783,
                Scenery.ANVIL_4306,
                Scenery.ANVIL_6150,
                Scenery.ANVIL_22725,
                Scenery.LATHE_26817,
                Scenery.ANVIL_26822,
                Scenery.ANVIL_37622,
            )
        private val SIGIL_MAP =
            mapOf(
                Items.ARCANE_SIGIL_13746 to Items.ARCANE_SPIRIT_SHIELD_13738,
                Items.DIVINE_SIGIL_13748 to Items.DIVINE_SPIRIT_SHIELD_13740,
                Items.ELYSIAN_SIGIL_13750 to Items.ELYSIAN_SPIRIT_SHIELD_13742,
                Items.SPECTRAL_SIGIL_13752 to Items.SPECTRAL_SPIRIT_SHIELD_13744,
            )
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, COMPONENTS, *ALTARS) { player, _, _ ->
            if (!inInventory(player, Items.HOLY_ELIXIR_13754) || !inInventory(player, Items.SPIRIT_SHIELD_13734)) {
                sendMessage(player, "You need a holy elixir and an unblessed spirit shield in order to do this.")
                return@onUseWith true
            }

            if (!hasLevelDyn(player, Skills.PRAYER, 85)) {
                sendMessage(player, "You need a Prayer level of 85 in order to bless the shield.")
            }

            if (!removeItem(player, Items.HOLY_ELIXIR_13754)) {
                return@onUseWith false
            }

            if (!removeItem(player, Items.SPIRIT_SHIELD_13734)) {
                return@onUseWith false
            }

            addItem(player, Items.BLESSED_SPIRIT_SHIELD_13736)
            sendMessage(player, "You successfully bless the shield using the holy elixir.")

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, SIGIL_MAP.keys.toIntArray(), *ANVILS) { player, used, _ ->
            if (!inInventory(player, Items.BLESSED_SPIRIT_SHIELD_13736)) {
                sendMessage(player, "You need a blessed spirit shield in order to continue.")
                return@onUseWith true
            }

            if (!hasLevelDyn(player, Skills.PRAYER, 90) || !hasLevelDyn(player, Skills.SMITHING, 85)) {
                sendMessage(
                    player,
                    "You need a Prayer level of 90 and a Smithing level of 85 in order to attach the sigil.",
                )
                return@onUseWith true
            }

            if (!inInventory(player, Items.HAMMER_2347)) {
                sendMessage(player, "You need a hammer in order to do this.")
                return@onUseWith true
            }

            sendDialogueOptions(player, "Combine the two?", "Yes", "No")
            addDialogueAction(player) { _, buttonId ->
                when (buttonId) {
                    2 -> {
                        if (removeItem(player, used) && removeItem(player, Items.BLESSED_SPIRIT_SHIELD_13736)) {
                            val product = Item(SIGIL_MAP[used.id]!!)
                            addItem(player, product.id)
                            animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
                            sendItemDialogue(
                                player,
                                product.id,
                                "You successfully attach the " + used.name.lowercase() +
                                    " to the blessed spirit shield.",
                            )
                        }
                    }
                }
            }

            return@onUseWith true
        }
    }
}

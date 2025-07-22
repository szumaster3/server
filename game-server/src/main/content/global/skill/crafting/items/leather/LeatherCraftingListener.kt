package content.global.skill.crafting.items.leather

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Components
import org.rs.consts.Items

class LeatherCraftingListener : InteractionListener , InterfaceListener {
    val STUDDED_LEATHER = StuddedArmour.values().map { it.leather }.toIntArray()
    val DRAGON_LEATHER = intArrayOf(Items.GREEN_D_LEATHER_1745, Items.BLUE_D_LEATHER_2505, Items.RED_DRAGON_LEATHER_2507, Items.BLACK_D_LEATHER_2509)

    override fun defineListeners() {

        /*
         * Handles crafting soft leather.
         */

        onUseWith(IntType.ITEM, Items.LEATHER_1741, Items.NEEDLE_1733) { player, _, _ ->
            openInterface(player, Components.LEATHER_CRAFTING_154)
            return@onUseWith true
        }

        /*
         * Handles crafting studded leather.
         */

        onUseWith(IntType.ITEM, STUDDED_LEATHER, Items.STEEL_STUDS_2370) { player, used, _ ->
            val item = StuddedArmour.forId(used.id) ?: return@onUseWith true

            sendSkillDialogue(player) {
                withItems(item.product)
                create { _, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse = StuddedArmourPulse(player, Item(item.leather), item, amount),
                    )
                }

                calculateMaxAmount {
                    amountInInventory(player, used.id)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles crafting the snakeskin.
         */

        onUseWith(IntType.ITEM, Items.NEEDLE_1733, Items.SNAKESKIN_6289) { player, used, _ ->
            sendString(player, "Which snakeskin item would you like to make?", Components.SKILL_MAKE_306, 27)
            sendSkillDialogue(player) {
                withItems(Items.SNAKESKIN_BODY_6322, Items.SNAKESKIN_CHAPS_6324, Items.SNAKESKIN_VBRACE_6330, Items.SNAKESKIN_BANDANA_6326, Items.SNAKESKIN_BOOTS_6328)
                create { id, amount ->
                    val item = SnakeskinLeather.forId(id)
                    item?.let {
                        submitIndividualPulse(
                            entity = player,
                            pulse = SnakeskinCraftingPulse(player, null, amount, it),
                        )
                    } ?: sendMessage(player, "Invalid snakeskin item selected.")
                }
                calculateMaxAmount {
                    amountInInventory(player, used.id)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles crafting hard leather.
         */

        onUseWith(IntType.ITEM, Items.HARD_LEATHER_1743, Items.NEEDLE_1733) { player, used, _ ->
            if (getStatLevel(player, Skills.CRAFTING) < 28) {
                sendDialogue(player, "You need a crafting level of " + 28 + " to make a hard leather body.")
                return@onUseWith false
            }
            sendSkillDialogue(player) {
                withItems(Items.HARDLEATHER_BODY_1131)
                create { _, amount ->
                    submitIndividualPulse(entity = player, pulse = HardLeatherCraftingPulse(player, used.asItem(), amount))
                }
                calculateMaxAmount {
                    amountInInventory(player, used.id)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles crafting dragon leather.
         */

        onUseWith(IntType.ITEM, DRAGON_LEATHER, Items.NEEDLE_1733) { player, used, with ->
            val index = IntArray(3)

            if (used.id == Items.GREEN_D_LEATHER_1745) {
                index[0] = DragonLeather.GREEN_D_HIDE_BODY.product
                index[1] = DragonLeather.GREEN_D_HIDE_VAMBS.product
                index[2] = DragonLeather.GREEN_D_HIDE_CHAPS.product
            }
            if (used.id == Items.BLUE_D_LEATHER_2505) {
                index[0] = DragonLeather.BLUE_D_HIDE_BODY.product
                index[1] = DragonLeather.BLUE_D_HIDE_VAMBS.product
                index[2] = DragonLeather.BLUE_D_HIDE_CHAPS.product
            }
            if (used.id == Items.RED_DRAGON_LEATHER_2507) {
                index[0] = DragonLeather.RED_D_HIDE_BODY.product
                index[1] = DragonLeather.RED_D_HIDE_VAMBS.product
                index[2] = DragonLeather.RED_D_HIDE_CHAPS.product
            }
            if (used.id == Items.BLACK_D_LEATHER_2509) {
                index[0] = DragonLeather.BLACK_D_HIDE_BODY.product
                index[1] = DragonLeather.BLACK_D_HIDE_VAMBS.product
                index[2] = DragonLeather.BLACK_D_HIDE_CHAPS.product
            }

            sendSkillDialogue(player) {
                withItems(*index)
                create { id, amount ->
                    val item = DragonLeather.forId(id)
                    submitIndividualPulse(entity = player, pulse = DragonLeatherCraftingPulse(player, null, item!!, amount))
                }
                calculateMaxAmount {
                    amountInInventory(player, used.id)
                }
            }
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.LEATHER_CRAFTING_154) { player, _, opcode, buttonID, _, _ ->
            var amount = 0
            val soft = SoftLeather.forButton(buttonID) ?: return@on true
            when (opcode) {
                155 -> amount = 1
                196 -> amount = 5
                124 -> amount = amountInInventory(player, Items.LEATHER_1741)
                199 -> {
                    sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                        submitIndividualPulse(
                            player,
                            SoftLeatherCraftingPulse(player, Item(Items.LEATHER_1741), soft, value as Int),
                        )
                    }
                    return@on true
                }
            }
            submitIndividualPulse(player, SoftLeatherCraftingPulse(player, null, soft, amount))
            return@on true
        }
    }
}


package content.global.handlers.iface

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import core.tools.StringUtils
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

class EnchantedStaffInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.STAFF_ENCHANT_332) { player, _ ->
            for (staff in EnchantedStaff.values()) {
                sendItemZoomOnInterface(
                    player,
                    Components.STAFF_ENCHANT_332,
                    staff.child,
                    staff.basic,
                )
            }
            return@onOpen true
        }

        on(Components.STAFF_ENCHANT_332) { player, _, opcode, buttonID, slot, itemID ->
            val price = 40000
            val discountPrice = 27000
            val headbandInEquipment = inEquipment(player, Items.SEERS_HEADBAND_1_14631)
            val completeDiary = if (!headbandInEquipment) price else discountPrice

            if (EnchantedStaff.childToBasic.containsKey(buttonID)) {
                val basicStaff = Item(EnchantedStaff.childToBasic[buttonID]!!)
                val enchantedStaff = Item(EnchantedStaff.basicToEnchanted[basicStaff.id]!!)
                if (!inInventory(player, basicStaff.id)) {
                    sendMessage(
                        player,
                        "You don't have a" + (
                            if (StringUtils.isPlusN(
                                    basicStaff.name,
                                )
                            ) {
                                "n "
                            } else {
                                " "
                            }
                        ) + basicStaff.name +
                            " to enchant.",
                    )
                    return@on true
                }

                if (!inInventory(player, Items.COINS_995, completeDiary)) {
                    closeInterface(player)
                    sendNPCDialogue(
                        player,
                        NPCs.THORMAC_389,
                        "I need $completeDiary coins for materials. Come back when you have the money!",
                    )
                    return@on true
                }

                if (player.inventory.remove(basicStaff, Item(Items.COINS_995, completeDiary))) {
                    closeInterface(player)
                    sendNPCDialogue(
                        player,
                        NPCs.THORMAC_389,
                        "Just a moment... hang on... hocus pocus abra- cadabra... there you go! Enjoy your enchanted staff!",
                    )
                    replaceSlot(player, basicStaff.slot, enchantedStaff)
                }
            }
            return@on true
        }
    }

    enum class EnchantedStaff(
        val enchanted: Int,
        val basic: Int,
        val child: Int,
    ) {
        AIR(enchanted = Items.MYSTIC_AIR_STAFF_1405, basic = Items.AIR_BATTLESTAFF_1397, child = 21),
        WATER(enchanted = Items.MYSTIC_WATER_STAFF_1403, basic = Items.WATER_BATTLESTAFF_1395, child = 22),
        EARTH(enchanted = Items.MYSTIC_EARTH_STAFF_1407, basic = Items.EARTH_BATTLESTAFF_1399, child = 23),
        FIRE(enchanted = Items.MYSTIC_FIRE_STAFF_1401, basic = Items.FIRE_BATTLESTAFF_1393, child = 24),
        LAVA(enchanted = Items.MYSTIC_LAVA_STAFF_3054, basic = Items.LAVA_BATTLESTAFF_3053, child = 25),
        MUD(enchanted = Items.MYSTIC_MUD_STAFF_6563, basic = Items.MUD_BATTLESTAFF_6562, child = 26),
        STEAM(enchanted = Items.MYSTIC_STEAM_STAFF_11738, basic = Items.STEAM_BATTLESTAFF_11736, child = 27),
        ;

        companion object {
            val basicToEnchanted = HashMap<Int, Int>()
            val childToBasic = HashMap<Int, Int>()

            init {
                for (staff in values()) {
                    basicToEnchanted[staff.basic] = staff.enchanted
                    childToBasic[staff.child] = staff.basic
                }
            }
        }
    }
}

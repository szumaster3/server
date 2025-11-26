package content.global.plugins.interfaces

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.diary.DiaryManager
import core.game.node.item.Item
import core.tools.StringUtils
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the enchanting of battle staffs into mystic staffs.
 * @author afaroutdude
 */
class StaffEnchantInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.STAFF_ENCHANT_332) { player, _ ->
            for (staff in EnchantedStaff.values()) {
                sendItemZoomOnInterface(player, Components.STAFF_ENCHANT_332, staff.child, staff.basic)
            }
            return@onOpen true
        }

        on(Components.STAFF_ENCHANT_332) { player, _, _, buttonID, _, _ ->
            val price = if (DiaryManager(player).hasHeadband()) 27000 else 40000

            val basicStaffId = EnchantedStaff.childToBasic[buttonID] ?: return@on true
            val basicStaff = Item(basicStaffId)
            val enchantedStaffId = EnchantedStaff.basicToEnchanted[basicStaffId] ?: return@on true
            val enchantedStaff = Item(enchantedStaffId)

            if (!inInventory(player, basicStaff.id)) {
                val article = if (StringUtils.isPlusN(basicStaff.name)) "n" else ""
                sendMessage(player, "You don't have a $article ${basicStaff.name} to enchant.")
                return@on true
            }

            if (!inInventory(player, Items.COINS_995, price)) {
                closeInterface(player)
                sendNPCDialogue(player, NPCs.THORMAC_389, "I need $price coins for materials. Come back when you have the money!")
                return@on true
            }

            if (player.inventory.remove(basicStaff, Item(Items.COINS_995, price))) {
                closeInterface(player)
                sendNPCDialogue(player, NPCs.THORMAC_389, "Just a moment... hang on... hocus pocus abra- cadabra... there you go! Enjoy your enchanted staff!")
                addItem(player, enchantedStaff.id, 1)
            }
            return@on true
        }

    }

    /**
     * Represents the various enchanted staffs.
     *
     * @property enchanted The item id of the enchanted staff.
     * @property basic The item id of the basic staff.
     * @property child The button id in the interface for the staff.
     */
    enum class EnchantedStaff(val enchanted: Int, val basic: Int, val child: Int) {
        AIR(Items.MYSTIC_AIR_STAFF_1405, Items.AIR_BATTLESTAFF_1397, 21),
        WATER(Items.MYSTIC_WATER_STAFF_1403, Items.WATER_BATTLESTAFF_1395, 22),
        EARTH(Items.MYSTIC_EARTH_STAFF_1407, Items.EARTH_BATTLESTAFF_1399, 23),
        FIRE(Items.MYSTIC_FIRE_STAFF_1401, Items.FIRE_BATTLESTAFF_1393, 24),
        LAVA(Items.MYSTIC_LAVA_STAFF_3054, Items.LAVA_BATTLESTAFF_3053, 25),
        MUD(Items.MYSTIC_MUD_STAFF_6563, Items.MUD_BATTLESTAFF_6562, 26),
        STEAM(Items.MYSTIC_STEAM_STAFF_11738, Items.STEAM_BATTLESTAFF_11736, 27),
        ;

        companion object {
            /**
             * Mapping base staff item ids to enchanted staff item ids.
             */
            val basicToEnchanted = HashMap<Int, Int>()

            /**
             * Mapping interface button ids to basic staff item ids.
             */
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

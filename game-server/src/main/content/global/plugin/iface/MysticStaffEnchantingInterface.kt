package content.global.plugin.iface

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.diary.DiaryManager
import core.game.node.item.Item
import core.tools.StringUtils
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the enchanting of battle staffs into mystic staffs.
 * @author afaroutdude
 */
class MysticStaffEnchantingInterface : InterfaceListener {
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
        AIR(enchanted = Items.MYSTIC_AIR_STAFF_1405, basic = Items.AIR_BATTLESTAFF_1397, child = 21),
        WATER(enchanted = Items.MYSTIC_WATER_STAFF_1403, basic = Items.WATER_BATTLESTAFF_1395, child = 22),
        EARTH(enchanted = Items.MYSTIC_EARTH_STAFF_1407, basic = Items.EARTH_BATTLESTAFF_1399, child = 23),
        FIRE(enchanted = Items.MYSTIC_FIRE_STAFF_1401, basic = Items.FIRE_BATTLESTAFF_1393, child = 24),
        LAVA(enchanted = Items.MYSTIC_LAVA_STAFF_3054, basic = Items.LAVA_BATTLESTAFF_3053, child = 25),
        MUD(enchanted = Items.MYSTIC_MUD_STAFF_6563, basic = Items.MUD_BATTLESTAFF_6562, child = 26),
        STEAM(enchanted = Items.MYSTIC_STEAM_STAFF_11738, basic = Items.STEAM_BATTLESTAFF_11736, child = 27),
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

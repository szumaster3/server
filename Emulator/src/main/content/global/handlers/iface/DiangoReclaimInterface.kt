package content.global.handlers.iface

import core.api.openInterface
import core.api.sendMessage
import core.api.setAttribute
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.container.access.InterfaceContainer
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.Items

@Initializable
class DiangoReclaimInterface : ComponentPlugin() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(COMPONENT_ID, this)
        ITEMS.addAll(HOLIDAY_ITEMS)
        return this
    }

    override fun handle(
        player: Player,
        component: Component,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        var reclaimable = player.getAttribute<Array<Item?>>("diango-reclaimables", null)
        if (reclaimable == null) reclaimable = getEligibleItems(player)

        val reclaimItem = reclaimable[slot]
        if (reclaimItem == null) {
            sendMessage(player, "Something went wrong there. Please try again.")
            return true
        }

        when (opcode) {
            155 -> {
                player.inventory.add(reclaimItem)
                refresh(player)
            }

            9 -> sendMessage(player, reclaimItem.definition.examine)
        }
        return false
    }

    companion object {
        private const val COMPONENT_ID = Components.DIANGO_RECLAIMABLE_468

        val ITEMS: MutableList<Item> = mutableListOf()

        val HOLIDAY_ITEMS: Array<Item> =
            arrayOf(
                Item(Items.YO_YO_4079),
                Item(Items.REINDEER_HAT_10507),
                Item(Items.WINTUMBER_TREE_10508),
                Item(Items.RUBBER_CHICKEN_4566),
                Item(Items.ZOMBIE_HEAD_6722),
                Item(Items.BOBBLE_SCARF_6857),
                Item(Items.BOBBLE_HAT_6856),
                Item(Items.JESTER_HAT_6858),
                Item(Items.JESTER_SCARF_6859),
                Item(Items.TRI_JESTER_HAT_6860),
                Item(Items.TRI_JESTER_SCARF_6861),
                Item(Items.WOOLLY_HAT_6862),
                Item(Items.WOOLLY_SCARF_6863),
                Item(Items.JACK_LANTERN_MASK_9920),
                Item(Items.SKELETON_BOOTS_9921),
                Item(Items.SKELETON_GLOVES_9922),
                Item(Items.SKELETON_LEGGINGS_9923),
                Item(Items.SKELETON_SHIRT_9924),
                Item(Items.SKELETON_MASK_9925),
                Item(Items.CHICKEN_FEET_11019),
                Item(Items.CHICKEN_WINGS_11020),
                Item(Items.CHICKEN_HEAD_11021),
                Item(Items.CHICKEN_LEGS_11022),
                Item(Items.GRIM_REAPER_HOOD_11789),
                Item(Items.SNOW_GLOBE_11949),
                Item(Items.CHOCATRICE_CAPE_12645),
                Item(Items.WARLOCK_TOP_14076),
                Item(Items.WARLOCK_LEGS_14077),
                Item(Items.WARLOCK_CLOAK_14081),
                Item(Items.SANTA_COSTUME_TOP_14595),
                Item(Items.SANTA_COSTUME_GLOVES_14602),
                Item(Items.SANTA_COSTUME_LEGS_14603),
                Item(Items.SANTA_COSTUME_BOOTS_14605),
                Item(Items.ICE_AMULET_14596),
                Item(Items.RED_MARIONETTE_6867),
                Item(Items.GREEN_MARIONETTE_6866),
                Item(Items.BLUE_MARIONETTE_6865),
            )

        @JvmStatic
        fun open(player: Player) {
            val curOpen = player.interfaceManager.opened
            curOpen?.close(player)
            val reclaimable = getEligibleItems(player)
            setAttribute(player, "diango-reclaimables", reclaimable)
            if (reclaimable!!.isNotEmpty()) {
                InterfaceContainer.generateItems(
                    player,
                    reclaimable,
                    arrayOf("Take"),
                    Components.DIANGO_RECLAIMABLE_468,
                    2,
                    8,
                    8,
                )
            }
            openInterface(player, Components.DIANGO_RECLAIMABLE_468)
        }

        fun refresh(player: Player) {
            player.interfaceManager.close()
            open(player)
        }

        fun getEligibleItems(player: Player): Array<Item?>? =
            ITEMS
                .filter { item ->
                    !player.equipment.containsItem(item) &&
                        !player.inventory.containsItem(item) &&
                        !player.bank.containsItem(item)
                }.toTypedArray()
    }
}

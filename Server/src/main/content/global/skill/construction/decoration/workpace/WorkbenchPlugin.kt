package content.global.skill.construction.decoration.workpace

import content.data.GameAttributes
import content.global.skill.construction.BuildHotspot
import content.global.skill.construction.BuildingUtils.buildDecoration
import content.global.skill.construction.Decoration
import content.global.skill.construction.Hotspot
import core.api.*
import core.game.component.Component
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import shared.consts.Components
import shared.consts.Items
import kotlin.math.min
import shared.consts.Scenery as Objects

/**
 * Handles the workbench interface and interactions in a poh.
 */
class WorkbenchPlugin : InterfaceListener, InteractionListener {
    companion object {
        private val FLATPACK_ITEM_IDS = Decoration.values().map { it.interfaceItem }.toIntArray()
        private val BUILD_HOTSPOT_OBJECT = BuildHotspot.values().map { it.objectId }.toIntArray()
        private val WORKBENCH_OBJECT_IDS = intArrayOf(
            Objects.WORKBENCH_13704,
            Objects.WORKBENCH_13705,
            Objects.WORKBENCH_13706,
            Objects.WORKBENCH_13707,
            Objects.WORKBENCH_13708
        )
    }

    override fun defineListeners() {
        on(WORKBENCH_OBJECT_IDS, IntType.SCENERY, "work-at") { player, obj ->
            player.interfaceManager.close()
            openInterface(player, Components.POH_WORKBENCH_397)
            when (obj.id) {
                Objects.WORKBENCH_13704 -> setAttribute(player, GameAttributes.CON_FLATPACK_TIER, 20)
                Objects.WORKBENCH_13705 -> setAttribute(player, GameAttributes.CON_FLATPACK_TIER, 40)
                Objects.WORKBENCH_13706 -> setAttribute(player, GameAttributes.CON_FLATPACK_TIER, 60)
                Objects.WORKBENCH_13707 -> setAttribute(player, GameAttributes.CON_FLATPACK_TIER, 80)
                Objects.WORKBENCH_13708 -> setAttribute(player, GameAttributes.CON_FLATPACK_TIER, 99)
            }

            return@on true
        }

        for (hotspot in BUILD_HOTSPOT_OBJECT) {
            onUseWith(IntType.SCENERY, FLATPACK_ITEM_IDS, hotspot) { player, used, with ->
                return@onUseWith buildFlatpack(player, used.asItem(), with.asScenery())
            }
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.POH_WORKBENCH_397) { player, _, _, buttonID, _, _ ->
            val furnitureType =
                when (buttonID) {
                    111 -> BuildHotspot.CHAIRS_1
                    112 -> BuildHotspot.BOOKCASE
                    113 -> BuildHotspot.BARRELS
                    114 -> BuildHotspot.KITCHEN_TABLE
                    115 -> BuildHotspot.DINING_TABLE
                    116 -> BuildHotspot.DINING_BENCH_1
                    117 -> BuildHotspot.BED
                    118 -> BuildHotspot.DRESSER
                    119 -> BuildHotspot.DRAWERS
                    120 -> BuildHotspot.CLOCK
                    121 -> BuildHotspot.CAPE_RACK
                    122 -> BuildHotspot.MAGIC_WARDROBE
                    123 -> BuildHotspot.ARMOUR_CASE
                    124 -> BuildHotspot.TREASURE_CHEST
                    125 -> BuildHotspot.COSTUME_BOX
                    126 -> BuildHotspot.TOY_BOX
                    else -> return@on false
                }
            val hotspot = Hotspot(BuildHotspot.FLATPACK, 0, 0)

            player.setAttribute(GameAttributes.CON_HOTSPOT, hotspot)
            openBuildInterface(player, furnitureType)

            return@on true
        }
    }

    /**
     * Builds a flatpack item.
     *
     * @param player The player building the item.
     * @param used The flatpack item used.
     * @param with The scenery where the item is built.
     * @return True if building was successful, false otherwise.
     */
    private fun buildFlatpack(player: Player, used: Item, with: Scenery): Boolean {
        val hotspotUsed = player.houseManager.getHotspot(with)
        val decorationUsed = Decoration.forObjectId(used.id)

        if (!hotspotUsed.hotspot.decorations.contains(decorationUsed)) {
            sendMessage(player, "You can't build that here.")
            return false
        }
        if (!player.inventory.containsItems(Item(Items.HAMMER_2347), Item(Items.SAW_8794))) {
            sendMessage(player, "You need a hammer and a saw to build this.")
            return false
        }
        buildDecoration(player, hotspotUsed, decorationUsed, with.asScenery(), true)
        return true
    }

    /**
     * Opens the build interface for a given furniture type.
     *
     * @param player The player opening the interface.
     * @param hotspot The hotspot representing the furniture type.
     */
    private fun openBuildInterface(player: Player, hotspot: BuildHotspot) {
        val BUILD_INDEXES = intArrayOf(0, 2, 4, 6, 1, 3, 5)
        player.interfaceManager.open(Component(396))
        val tempItems: Array<Item?> = arrayOfNulls(7)

        var c261Value = 0

        for (menuIndex in 0..6) {
            val itemsStringOffset = 97 + (menuIndex * 5)

            if (menuIndex >= hotspot.decorations.size ||
                (hotspot.decorations[menuIndex] != null && hotspot.decorations[menuIndex].isInvisibleNode)
            ) {
                for (j in 0..4) {
                    player.packetDispatch.sendString("", Components.POH_BUILD_FURNITURE_396, itemsStringOffset + j)
                }
                player.packetDispatch.sendString("", Components.POH_BUILD_FURNITURE_396, 140 + menuIndex)
                c261Value += (1 shl (menuIndex + 1))
                continue
            }

            val decoration = hotspot.decorations[menuIndex]!!

            tempItems[BUILD_INDEXES[menuIndex]] = Item(decoration.interfaceItem)

            player.packetDispatch.sendString(
                getItemName(decoration.interfaceItem),
                Components.POH_BUILD_FURNITURE_396,
                itemsStringOffset,
            )

            var hasRequirements = min(
                player.skills.getLevel(22),
                player.getAttribute(GameAttributes.CON_FLATPACK_TIER, 0)
            ) >= decoration.level

            for (j in 0..3) {
                if (j >= decoration.items.size) {
                    if (j == decoration.items.size && decoration.nailAmount > 0) {
                        player.packetDispatch.sendString(
                            "Nails: " + decoration.nailAmount,
                            Components.POH_BUILD_FURNITURE_396,
                            (itemsStringOffset + 1) + j,
                        )
                    } else {
                        player.packetDispatch.sendString(
                            "",
                            Components.POH_BUILD_FURNITURE_396,
                            (itemsStringOffset + 1) + j,
                        )
                    }
                } else {
                    val item = decoration.items[j]
                    if (!player.inventory.containsItem(item)) {
                        hasRequirements = false
                    }
                    val s = item.name + ": " + item.amount
                    player.packetDispatch.sendString(s, Components.POH_BUILD_FURNITURE_396, (itemsStringOffset + 1) + j)
                }
            }

            if (hasRequirements) {
                c261Value += (1 shl (menuIndex + 1))
            }
            setVarp(player, 1485 + menuIndex, if (hasRequirements || player.isStaff) 1 else 0)
            player.packetDispatch.sendString(
                "Level " + decoration.level,
                Components.POH_BUILD_FURNITURE_396,
                140 + menuIndex,
            )
        }

        setVarp(player, 261, c261Value)
        val items: Array<Item> = tempItems.map { it ?: Item(-1) }.toTypedArray()

        PacketRepository.send(
            ContainerPacket::class.java,
            ContainerContext(player, Components.POH_BUILD_FURNITURE_396, 132, 8, items, false),
        )
    }
}

package content.region.karamja.quest.mm.plugin

import core.api.*
import core.game.global.action.EquipHandler
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Items
import org.rs.consts.NPCs

class MonkeyGreeGreesPlugin :
    InteractionListener,
    MapArea {
    companion object {
        private val APE_ATOLL_ZONE = ZoneBorders(2690, 2695, 2817, 2814)
        private val LOGOUT = "greegree-equip"
        private const val SMALL_NINJA_MONKEY = Items.MONKEY_GREEGREE_4024
        private const val MEDIUM_NINJA_MONKEY = Items.MONKEY_GREEGREE_4025
        private const val GORILLA_GREEGREE = Items.MONKEY_GREEGREE_4026
        private const val BEARDED_GORILLA_GREEGREE = Items.MONKEY_GREEGREE_4027
        private const val ANCIENT_MONKEY = Items.MONKEY_GREEGREE_4028
        private const val SMALL_ZOMBIE_MONKEY = Items.MONKEY_GREEGREE_4029
        private const val LARGE_ZOMBIE_MONKEY = Items.MONKEY_GREEGREE_4030
        private const val MONKEY_GREEGREE = Items.MONKEY_GREEGREE_4031
        private val ALL_STAFFS = intArrayOf(SMALL_NINJA_MONKEY, MEDIUM_NINJA_MONKEY, GORILLA_GREEGREE, BEARDED_GORILLA_GREEGREE, ANCIENT_MONKEY, SMALL_ZOMBIE_MONKEY, LARGE_ZOMBIE_MONKEY, MONKEY_GREEGREE)
        private val AIR_BLAST_ANIMATION = Graphics(359, 96)
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(APE_ATOLL_ZONE)

    override fun areaLeave(entity: Entity, logout: Boolean) {
        super.areaLeave(entity, logout)

        if (entity is Player && !entity.isArtificial) {
            val player = entity.asPlayer()
            if (!getRegionBorders(11050).insideRegion(player) ||
                !getRegionBorders(11051).insideRegion(player) ||
                !getRegionBorders(10794).insideRegion(player) ||
                !getRegionBorders(10795).insideRegion(player)
            ) {
                for (i in ALL_STAFFS) {
                    EquipHandler.unequip(player, EquipmentSlot.WEAPON.ordinal, i)
                    player.appearance.transformNPC(-1)
                }
                clearLogoutListener(player, LOGOUT)
            }
        }
    }

    override fun defineListeners() {
        onEquip(ALL_STAFFS) { player, used ->

            registerLogoutListener(player, LOGOUT) {
                for (i in ALL_STAFFS) {
                    EquipHandler.unequip(player, EquipmentSlot.WEAPON.ordinal, i)
                    player.appearance.transformNPC(-1)
                }
            }

            if (!inBorders(player, 2689, 2694, 2819, 2815)) {
                sendMessage(player, "You have to be in Ape Atoll in order to transform into a monkey.")
                return@onEquip false
            }

            lock(player, 1)
            visualize(player, -1, AIR_BLAST_ANIMATION)
            when (used.asItem().id) {
                4024 -> player.appearance.transformNPC(NPCs.SMALL_NINJA_MONKEY_1480)
                4025 -> player.appearance.transformNPC(NPCs.MEDIUM_NINJA_MONKEY_1481)
                4026 -> player.appearance.transformNPC(NPCs.GORILLA_1482)
                4027 -> player.appearance.transformNPC(NPCs.BEARDED_GORILLA_1483)
                4028 -> player.appearance.transformNPC(NPCs.ANCIENT_MONKEY_1484)
                4029 -> player.appearance.transformNPC(NPCs.SMALL_ZOMBIE_MONKEY_1485)
                4030 -> player.appearance.transformNPC(NPCs.LARGE_ZOMBIE_MONKEY_1486)
                4031 -> player.appearance.transformNPC(NPCs.MONKEY_1487)
            }
            return@onEquip true
        }

        onUnequip(ALL_STAFFS) { player, _ ->
            player.appearance.transformNPC(-1)
            return@onUnequip true
        }
    }
}

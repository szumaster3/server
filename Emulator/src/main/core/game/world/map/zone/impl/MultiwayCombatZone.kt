package core.game.world.map.zone.impl

import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.MapDistance
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.map.path.Pathfinder
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.net.packet.PacketRepository
import core.net.packet.context.InterfaceContext
import core.net.packet.out.CloseInterface
import core.net.packet.out.Interface

class MultiwayCombatZone
    private constructor() : MapZone("Multicombat", true) {
        override fun configure() {
            register(FIGHT_CAVE)
            register(ZoneBorders(3210, 9333, 3339, 9424))
            register(ZoneBorders(2607, 3296, 2644, 3332))
            register(ZoneBorders(2949, 3370, 3001, 3392))
            register(ZoneBorders(3250, 9800, 3342, 9870))
            register(ZoneBorders(3190, 3648, 3327, 3839))
            register(ZoneBorders(3200, 3840, 3390, 3967))
            register(ZoneBorders(2992, 3912, 3007, 3967))
            register(ZoneBorders(2946, 3816, 2959, 3831))
            register(ZoneBorders(3008, 3856, 3199, 3903))
            register(ZoneBorders(3008, 3600, 3071, 3711))
            register(ZoneBorders(3072, 3608, 3327, 3647))
            register(ZoneBorders(2624, 2550, 2690, 2619))
            register(ZoneBorders(2371, 5062, 2422, 5117))
            register(ZoneBorders(2896, 3595, 2927, 3630))
            register(ZoneBorders(2820, 5250, 2955, 5370))
            register(ZoneBorders(2892, 4435, 2932, 4464))
            register(ZoneBorders(2724, 5071, 2747, 5109))
            register(ZoneBorders(2256, 4680, 2287, 4711))
            register(ZoneBorders(3107, 3234, 3134, 3259))
            register(ZoneBorders(2931, 3514, 2940, 3518))
            register(ZoneBorders(2869, 3687, 2940, 3839))
            register(ZoneBorders(1728, 5120, 1791, 5247))
            register(ZoneBorders(3136, 3523, 3328, 3710))
            registerRegion(13105, ZoneBorders(3282, 3159, 3303, 3177))
            registerRegion(12341)
            registerRegion(KALPHITE_LAIR_REGION)
            registerRegion(ABYSS_AREA_REGION)
            registerRegion(7505)
            registerRegion(FREMENNIK_ROCK_CRABS_REGION)
            registerRegion(WIZARDS_TOWER_REGION)
            registerRegion(ZMI_REGION)
            register(ZoneBorders(2424, 5105, 2536, 5183))
            register(ZoneBorders(2487, 10113, 2563, 10174))
            registerRegion(PHOENIX_LAIR_1)
            registerRegion(PHOENIX_LAIR_2)
            registerRegion(THZAAR_CAVE_REGION_0)
            registerRegion(THZAAR_CAVE_REGION_1)
            registerRegion(THZAAR_CAVE_REGION_2)
            registerRegion(THZAAR_CAVE_REGION_3)
            register(ZoneBorders(3097, 4224, 3225, 4317))
            register(ZoneBorders(3116, 5412, 3362, 5584))
            register(ZoneBorders(3078, 5520, 3123, 5552, 0))
            registerRegion(CORPOREAL_BEAST_REGION)
            registerRegion(TDS_REGION)
            registerRegion(NORTH_JATIZSO_REGION)
            // Eastern rock crabs.
            register(ZoneBorders(2734, 3736, 2688, 3712))
            registerRegion(DESERT_BANDITS_REGION_0)
            register(ZoneBorders(2855, 9928, 2880, 9968))
            registerRegion(DESERT_BANDITS_REGION_1)
            register(ZoneBorders(2685, 2685, 2825, 2825))
            // Necromancer tower +1.
            register(ZoneBorders(2673, 3238, 2666, 3250, 1, false))
            // Neitiznot
            register(ZoneBorders(2381, 3823, 2306, 3843))
        }

        override fun enter(e: Entity): Boolean {
            if (e is Player) {
                val p = e
                val resizable = p.interfaceManager.isResizable
                PacketRepository.send(
                    Interface::class.java,
                    InterfaceContext(p, p.interfaceManager.windowPaneId, if (resizable) 17 else 7, 745, true),
                )
                p.packetDispatch.sendInterfaceConfig(745, 1, false)
            }
            e.properties.isMultiZone = true
            return super.enter(e)
        }

        override fun leave(
            e: Entity,
            logout: Boolean,
        ): Boolean {
            if (!logout && e is Player) {
                val resizable = e.interfaceManager.isResizable
                PacketRepository.send(
                    CloseInterface::class.java,
                    InterfaceContext(e, e.interfaceManager.windowPaneId, if (resizable) 17 else 7, 745, true),
                )
            }
            e.properties.isMultiZone = false
            return super.leave(e, logout)
        }

        override fun move(
            e: Entity,
            loc: Location,
            destination: Location,
        ): Boolean {
            if (e.properties.isNPCWalkable) {
                return true
            }
            for (n in getLocalNpcs(e, MapDistance.RENDERING.distance / 2)) {
                if (n.isInvisible || !n.definition.hasAttackOption() || n === e) {
                    continue
                }
                if (n.shouldPreventStacking(e)) {
                    val s1 = e.size()
                    val s2 = n.size()
                    val x = destination.x
                    val y = destination.y
                    val l = n.location
                    if (Pathfinder.isStandingIn(x, y, s1, s1, l.x, l.y, s2, s2)) {
                        return false
                    }
                }
            }
            return true
        }

        companion object {
            @JvmStatic val instance: MultiwayCombatZone = MultiwayCombatZone()

            @JvmField val FIGHT_CAVE = ZoneBorders(3210, 9333, 3339, 9424)
            val KALPHITE_LAIR_REGION = 13972
            val ABYSS_AREA_REGION = 12107
            val FREMENNIK_ROCK_CRABS_REGION = 10554
            val WIZARDS_TOWER_REGION = 12337
            val ZMI_REGION = 13131
            val THZAAR_CAVE_REGION_0 = 7236
            val THZAAR_CAVE_REGION_1 = 7492
            val THZAAR_CAVE_REGION_2 = 7748
            val THZAAR_CAVE_REGION_3 = 12610
            val CORPOREAL_BEAST_REGION = 11844
            val TDS_REGION = 10329
            val NORTH_JATIZSO_REGION = 9532
            val DESERT_BANDITS_REGION_0 = 12590
            val DESERT_BANDITS_REGION_1 = 11318
            val PHOENIX_LAIR_1 = 13905
            val PHOENIX_LAIR_2 = 14161
        }
    }

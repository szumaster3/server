package content.region.fremennik.rellekka.plugin

import content.global.skill.agility.AgilityHandler
import content.region.islands.waterbirth_island.plugin.TravelDestination
import content.region.islands.waterbirth_island.plugin.WaterbirthUtils
import core.api.*
import core.api.quest.requireQuest
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery
import java.util.*

class RellekkaPlugin : InteractionListener, MapArea {

    override fun defineListeners() {
        on(LADDER, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(2509, 10245, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(TUNNEL, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location.create(2773, 10162, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(ROCKSLIDE, IntType.SCENERY, "climb-over") { player, _ ->
            lock(player, 1)
            AgilityHandler.forceWalk(player, -1, player.location, player.location.transform(0, if (player.location.y <= 3657) 3 else -3, 0), Animation.create(839), 20, 1.0, null, 0)
            return@on true
        }

        on(STAIRS, IntType.SCENERY, "ascend", "descend") { player, _ ->
            if (player.location.y < 3802) {
                player.properties.teleportLocation =
                    when (player.location.x) {
                        2715 -> DOWN1A
                        2716 -> DOWN1B
                        2726 -> DOWN2A
                        2727 -> DOWN2B
                        else -> player.location
                    }
            } else {
                player.properties.teleportLocation =
                    when (player.location.x) {
                        2715 -> UP1A
                        2716 -> UP1B
                        2726 -> UP2A
                        2727 -> UP2B
                        else -> player.location
                    }
            }
            return@on true
        }

        on(NPCs.MARIA_GUNNARS_5508, IntType.NPC, "ferry-neitiznot") { player, _ ->
            if (!requireQuest(player, Quests.THE_FREMENNIK_TRIALS, "")) return@on true
            WaterbirthUtils.sail(player, TravelDestination.RELLEKKA_TO_NEITIZNOT)
            return@on true
        }

        on(NPCs.MARIA_GUNNARS_5507, IntType.NPC, "ferry-rellekka") { player, _ ->
            WaterbirthUtils.sail(player, TravelDestination.NEITIZNOT_TO_RELLEKKA)
            return@on true
        }

        on(NPCs.MORD_GUNNARS_5481, IntType.NPC, "ferry-jatizso") { player, _ ->
            if (!requireQuest(player, Quests.THE_FREMENNIK_TRIALS, "")) return@on true
            WaterbirthUtils.sail(player, TravelDestination.RELLEKKA_TO_JATIZSO)
            return@on true
        }

        on(NPCs.MORD_GUNNARS_5482, IntType.NPC, "ferry-rellekka") { player, _ ->
            WaterbirthUtils.sail(player, TravelDestination.JATIZSO_TO_RELLEKKA)
            return@on true
        }

        on(NPCs.SAILOR_1385, IntType.NPC, "travel") { player, _ ->
            if (!requireQuest(player, Quests.THE_FREMENNIK_TRIALS, "")) return@on true
            WaterbirthUtils.sail(player, TravelDestination.RELLEKA_TO_MISCELLANIA)
            return@on true
        }

        on(NPCs.SAILOR_1304, IntType.NPC, "travel") { player, _ ->
            if (!requireQuest(player, Quests.THE_FREMENNIK_TRIALS, "")) return@on true
            WaterbirthUtils.sail(player, TravelDestination.MISCELLANIA_TO_RELLEKKA)
            return@on true
        }
    }

    companion object {
        private val UP1A: Location? = Location.create(2715, 3798, 0)
        private val UP1B: Location? = Location.create(2716, 3798, 0)
        private val UP2A: Location? = Location.create(2726, 3801, 0)
        private val UP2B: Location? = Location.create(2727, 3801, 0)

        private val DOWN1A: Location? = Location.create(2715, 3802, 1)
        private val DOWN1B: Location? = Location.create(2716, 3802, 1)
        private val DOWN2A: Location? = Location.create(2726, 3805, 1)
        private val DOWN2B: Location? = Location.create(2727, 3805, 1)

        private val STAIRS = intArrayOf(Scenery.STEPS_19690, Scenery.STEPS_19691)

        private const val TUNNEL = Scenery.TUNNEL_5008
        private const val ROCKSLIDE = Scenery.ROCKSLIDE_5847
        private const val LADDER = Scenery.LADDER_15116
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(2602, 3639, 2739, 3741))

    override fun interactBehavior(entity: Entity, target: Node, option: Option, ): Boolean {
        if (entity is Player) {
            val player = entity.asPlayer()
            when (target.id) {
                Scenery.ANVIL_4306,
                Scenery.POTTER_S_WHEEL_4310,
                Scenery.SPINNING_WHEEL_4309,
                Scenery.FURNACE_4304,
                Scenery.POTTERY_OVEN_4308,
                    -> {
                    sendMessage(player, "Only Fremenniks may use this ${target.name.lowercase(Locale.getDefault())}.")
                    return true
                }

                Scenery.TRAPDOOR_100 -> {
                    sendDialogueLines(player, "You try to open the trapdoor but it won't budge! It looks like the", "trapdoor can only be opened from the other side.")
                    return true
                }

                2435,
                NPCs.JARVALD_2436,
                NPCs.JARVALD_2437,
                NPCs.JARVALD_2438,
                    -> {
                    if (option.name == "Travel") {
                        player.dialogueInterpreter.open(target.id, target, true)
                        return true
                    }
                }
            }
        }

        return false
    }
}

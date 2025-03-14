package content.region.kandarin.miniquest.zaros

import core.game.interaction.InteractionListener
import core.game.world.map.Location
import core.tools.Log
import core.tools.SystemLogger
import org.rs.consts.NPCs

class CurseOfZarosListener : InteractionListener {
    private val npcLocations =
        mapOf(
            NPCs.MYSTERIOUS_GHOST_2397 to
                listOf(
                    Location.create(3020, 3946, 0),
                    Location.create(3035, 3701, 0),
                    Location.create(3163, 2982, 0),
                ),
            NPCs.MYSTERIOUS_GHOST_2401 to
                listOf(
                    Location.create(2851, 3348, 0),
                    Location.create(2396, 3480, 0),
                    Location.create(3041, 3203, 0),
                ),
            NPCs.MYSTERIOUS_GHOST_2400 to
                listOf(
                    Location.create(2951, 3820, 0),
                    Location.create(3069, 3859, 0),
                    Location.create(3217, 3676, 0),
                ),
            NPCs.MYSTERIOUS_GHOST_2398 to
                listOf(
                    Location.create(3053, 3378, 1),
                    Location.create(3112, 3157, 0),
                    Location.create(3052, 3497, 1),
                ),
            NPCs.MYSTERIOUS_GHOST_2402 to
                listOf(
                    Location.create(3124, 9993, 0),
                    Location.create(3295, 3934, 1),
                    Location.create(3447, 3549, 1),
                ),
        )

    init {
        val randomIndex =
            npcLocations.values
                .firstOrNull()
                ?.indices
                ?.random() ?: 0

        npcLocations.forEach { (npcId, locations) ->
            locations.getOrNull(randomIndex)?.let { location ->
                core.game.node.entity.npc.NPC
                    .create(npcId, location)
                    .init()
                SystemLogger.processLogEntry(
                    this::class.java,
                    Log.INFO,
                    "NPC ID: $npcId | Path: $randomIndex | Location: $location",
                )
            }
        }
    }
}

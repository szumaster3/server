package content.global.handlers.npc

import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.Location
import core.tools.Log
import core.tools.SystemLogger
import org.rs.consts.NPCs

class PostiePeteNPC : NPCBehavior(NPCs.POSTIE_PETE_3805) {
    init {
        val POSTIE_PETE_LOCATIONS =
            arrayOf(
                Location(3676, 3537, 0),
                Location.create(3209, 3495, 0),
                Location.create(3011, 3503, 0),
                Location.create(3026, 3701, 0),
                Location.create(2609, 3285, 0),
                Location.create(3052, 3375, 0),
            )

        val randomLocation = POSTIE_PETE_LOCATIONS.random()
        core.game.node.entity.npc.NPC
            .create(
                NPCs.POSTIE_PETE_3805,
                randomLocation,
                null,
            ).init()
        SystemLogger.processLogEntry(this::class.java, Log.INFO, "Postie Pete spawn: $randomLocation")
    }
}

package content.global.travel

import content.global.handlers.iface.FairyRingInterface
import core.api.anyInEquipment
import core.api.openInterface
import core.api.quest.hasRequirement
import core.api.sendMessage
import core.api.teleport
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Scenery

class FairyRingListener : InteractionListener {
    private val RINGS =
        intArrayOf(
            Scenery.FAIRY_RING_12095,
            Scenery.FAIRY_RING_14058,
            Scenery.FAIRY_RING_14061,
            Scenery.FAIRY_RING_14064,
            Scenery.FAIRY_RING_14067,
            Scenery.FAIRY_RING_14070,
            Scenery.FAIRY_RING_14073,
            Scenery.FAIRY_RING_14076,
            Scenery.FAIRY_RING_14079,
            Scenery.FAIRY_RING_14082,
            Scenery.FAIRY_RING_14085,
            Scenery.FAIRY_RING_14088,
            Scenery.FAIRY_RING_14091,
            Scenery.FAIRY_RING_14094,
            Scenery.FAIRY_RING_14097,
            Scenery.FAIRY_RING_14100,
            Scenery.FAIRY_RING_14103,
            Scenery.FAIRY_RING_14106,
            Scenery.FAIRY_RING_14109,
            Scenery.FAIRY_RING_14112,
            Scenery.FAIRY_RING_14115,
            Scenery.FAIRY_RING_14118,
            Scenery.FAIRY_RING_14121,
            Scenery.FAIRY_RING_14124,
            Scenery.FAIRY_RING_14127,
            Scenery.FAIRY_RING_14130,
            Scenery.FAIRY_RING_14133,
            Scenery.FAIRY_RING_14136,
            Scenery.FAIRY_RING_14139,
            Scenery.FAIRY_RING_14142,
            Scenery.FAIRY_RING_14145,
            Scenery.FAIRY_RING_14148,
            Scenery.FAIRY_RING_14151,
            Scenery.FAIRY_RING_14154,
            Scenery.FAIRY_RING_14157,
            Scenery.FAIRY_RING_14160,
            Scenery.FAIRY_RING_16181,
            Scenery.FAIRY_RING_16184,
            Scenery.FAIRY_RING_23047,
            Scenery.FAIRY_RING_27325,
            Scenery.FAIRY_RING_37727,
        )
    private val MAIN_RING = Scenery.FAIRY_RING_12128
    private val ENTRY_RING = Scenery.FAIRY_RING_12094
    private val MARKETPLACE_RING = Scenery.FAIRY_RING_12003

    override fun defineListeners() {
        on(RINGS, IntType.SCENERY, "use") { player, _ ->
            if (!fairyMagic(player)) return@on true
            teleport(player, Location.create(2412, 4434, 0), TeleportType.FAIRY_RING)
            return@on true
        }

        on(MAIN_RING, IntType.SCENERY, "use") { player, _ ->
            if (!fairyMagic(player)) return@on true
            openFairyRing(player)
            return@on true
        }

        on(ENTRY_RING, IntType.SCENERY, "use") { player, _ ->
            teleport(player, Location.create(3203, 3168, 0), TeleportType.FAIRY_RING)
            return@on true
        }

        on(MARKETPLACE_RING, IntType.SCENERY, "use") { player, _ ->
            teleport(player, Location.create(3262, 3167, 0), TeleportType.FAIRY_RING)
            return@on true
        }
    }

    private fun fairyMagic(player: Player): Boolean {
        if (!hasRequirement(player, Quests.FAIRYTALE_I_GROWING_PAINS) ||
            !anyInEquipment(
                player,
                Items.DRAMEN_STAFF_772,
                Items.LUNAR_STAFF_9084,
            )
        ) {
            sendMessage(player, "The fairy ring only works for those who weld fairy magic.")
            return false
        }
        return true
    }

    private fun openFairyRing(player: Player) {
        openInterface(player, FairyRingInterface.RINGS_IFACE)
    }
}

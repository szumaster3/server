package content.region.fremennik.rellekka.plugin

import content.global.skill.agility.AgilityHandler
import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.*
import core.api.openNpcShop
import core.api.isQuestComplete
import core.api.requireQuest
import core.game.dialogue.FaceAnim
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
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery
import java.util.*

class RellekkaPlugin : InteractionListener, MapArea {

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
        private val JARVALD_NPC = intArrayOf(NPCs.NULL_2435, NPCs.JARVALD_2436, NPCs.JARVALD_2437,NPCs.JARVALD_2438)
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(2602, 3639, 2739, 3741))


    override fun defineListeners() {

        on(Scenery.LADDER_15116, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(2509, 10245, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles interaction with cave entrance to Keldagrim.
         */

        on(Scenery.TUNNEL_5008, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location.create(2773, 10162, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(Scenery.ROCKSLIDE_5847, IntType.SCENERY, "climb-over") { player, _ ->
            lock(player, 1)
            AgilityHandler.forceWalk(player, -1, player.location, player.location.transform(0, if (player.location.y <= 3657) 3 else -3, 0), Animation.create(839), 20, 1.0, null, 0)
            return@on true
        }

        /*
         * Handles interaction with snow stairs in hunter area.
         */

        on(STAIRS, IntType.SCENERY, "ascend", "descend") { player, _ ->
            val up = player.location.y >= 3802
            player.properties.teleportLocation = when (player.location.x) {
                2715 -> if (up) UP1A else DOWN1A
                2716 -> if (up) UP1B else DOWN1B
                2726 -> if (up) UP2A else DOWN2A
                2727 -> if (up) UP2B else DOWN2B
                else -> player.location
            }
            return@on true
        }

        /*
         * Handles the dialogue interaction with the Fish Monger NPC in Rellekka.
         */

        on(NPCs.FISH_MONGER_1315, IntType.NPC, "talk-to") { player, node ->
            if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                sendNPCDialogue(player, node.id, "I don't sell to outlanders.", FaceAnim.ANNOYED)
            } else {
                sendNPCDialogue(player, node.id, "Hello there, ${FremennikTrials.getFremennikName(player)}. Looking for fresh fish?")
                openNpcShop(player, NPCs.FISH_MONGER_1315)
            }
            return@on true
        }

        /*
         * Handles use an item on Jarvald.
         */

        onUseAnyWith(IntType.NPC, *JARVALD_NPC) { player, _, with ->
            sendNPCDialogue(player, with.id, "Thank you, but I have no need for that!")
            return@onUseAnyWith true
        }

    }
}

package content.region.asgarnia.falador.dwarven_mine.plugin

import content.region.asgarnia.falador.dwarven_mine.dialogue.DwarvenMineGuardDialogue
import content.region.asgarnia.falador.dwarven_mine.dialogue.DwarvenMineShopkeeperDialogue
import core.api.getDynLevel
import core.api.openDialogue
import core.api.sendNPCDialogue
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler.climb
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.NPCs
import shared.consts.Scenery

class MiningGuildPlugin : InteractionListener {

    private val REQUIRED_MINING_LEVEL = 60
    private val animation = Animation(Animations.HUMAN_CLIMB_STAIRS_828)

    private val ladderMap = mapOf(
        Location.create(3019, 3340, 0) to Location.create(3019, 9741, 0),
        Location.create(3019, 3338, 0) to Location.create(3019, 9737, 0),
        Location.create(3018, 3339, 0) to Location.create(3017, 9739, 0),
        Location.create(3020, 3339, 0) to Location.create(3021, 9739, 0),
        Location.create(3019, 3340, 0) to Location.create(3019, 9741, 0),
    )

    override fun defineListeners() {

        /*
         * Handles ladder interaction to mining guild.
         */

        on(Scenery.LADDER_2113, IntType.SCENERY,"climb-down") { player, node ->
            val location = node.location
            val destination = ladderMap[location] ?: return@on false

            if (getDynLevel(player, Skills.MINING) < REQUIRED_MINING_LEVEL) {
                sendNPCDialogue(player, NPCs.DWARF_382, "Sorry, but you need level 60 Mining to go in there.", FaceAnim.OLD_NORMAL)
                return@on false
            }

            climb(player, animation, destination)
            return@on true
        }

        /*
         * Handles door interaction to mining guild.
         */

        on(Scenery.DOOR_2112, IntType.SCENERY, "open") { player, node ->
            if (getDynLevel(player, Skills.MINING) < REQUIRED_MINING_LEVEL) {
                sendNPCDialogue(player, NPCs.DWARF_382, "Sorry, but you need level 60 Mining to go in there.", FaceAnim.OLD_NORMAL)
                return@on false
            }
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }


        on(NPCs.GUARD_206, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, DwarvenMineGuardDialogue(), node.id)
            return@on true
        }

        on(NPCs.DWARF_582, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, DwarvenMineShopkeeperDialogue(), node.id)
            return@on true
        }
    }
}

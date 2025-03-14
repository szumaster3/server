package content.region.asgarnia.handlers.guilds

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler.climb
import core.game.global.action.ClimbActionHandler.climbLadder
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class MiningGuildListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.LADDER_2113, IntType.SCENERY, "climb-down") { player, node ->
            val option = getUsedOption(player)
            if (withinDistance(player, Location.create(3019, 3339, 0), 4)) {
                if (getDynLevel(player, Skills.MINING) < 60) {
                    openDialogue(
                        player,
                        dialogue = NPCs.DWARF_382,
                        core.game.node.entity.npc.NPC
                            .create(NPCs.DWARF_382, Location.create(0, 0, 0)),
                        1,
                    )
                    return@on true
                }
                climb(player, Animation(Animations.USE_LADDER_828), Location.create(3021, 9739, 0))
                return@on true
            }
            climbLadder(player, node.asScenery(), option)
            return@on true
        }

        on(Scenery.DOOR_2112, IntType.SCENERY, "open") { player, node ->
            if (getDynLevel(player, Skills.MINING) < 60) {
                sendNPCDialogue(
                    player,
                    NPCs.DWARF_382,
                    "Sorry, but you need level 60 Mining to go in there.",
                    FaceAnim.OLD_NORMAL,
                )
                return@on true
            }
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        on(Scenery.LADDER_30941, IntType.SCENERY, "climb-up") { player, node ->
            val option = getUsedOption(player)
            if (withinDistance(player, Location(3019, 9739, 0))) {
                climb(player, Animation(Animations.USE_LADDER_828), Location(3017, 3339, 0))
            } else {
                climbLadder(player, node.asScenery(), option)
            }
            return@on true
        }
    }
}

package content.region.karamja.handlers

import content.region.karamja.handlers.brimhaven.BrimhavenUtils
import core.api.getAttribute
import core.api.location
import core.api.removeAttribute
import core.api.sendNPCDialogue
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class BrimhavenDungeonListener : InteractionListener {
    override fun defineListeners() {
        on(ENTRANCE, IntType.SCENERY, "enter") { player, _ ->
            if (!getAttribute(player, "saniboch:paid", false) ||
                !player.achievementDiaryManager.getDiary(DiaryType.KARAMJA)!!.isComplete
            ) {
                sendNPCDialogue(player, NPCs.SANIBOCH_1595, "You can't go in there without paying!", FaceAnim.NEUTRAL)
            }

            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, location(2713, 9564, 0))
            removeAttribute(player, "saniboch:paid")
            return@on true
        }

        on(EXIT, IntType.SCENERY, "leave") { player, _ ->
            player.properties.teleportLocation = Location.create(2745, 3152, 0)
            return@on true
        }

        on(STAIRS, IntType.SCENERY, "walk-up", "walk-down") { player, node ->
            BrimhavenUtils.handleStairs(node.asScenery(), player)
            return@on true
        }

        on(STEPPING_STONES, IntType.SCENERY, "jump-from") { player, node ->
            BrimhavenUtils.handleSteppingStones(player, node.asScenery())
            return@on true
        }

        on(VINES, IntType.SCENERY, "chop-down") { player, node ->
            BrimhavenUtils.handleVines(player, node.asScenery())
            return@on true
        }

        on(SANIBOCH_NPC, IntType.NPC, "pay") { player, node ->
            player.dialogueInterpreter.open(NPCs.SANIBOCH_1595, node.asNpc(), 10)
            return@on true
        }

        on(LOGS, IntType.SCENERY, "walk-across") { player, node ->
            if (player.skills.getLevel(Skills.AGILITY) < 30) {
                player.packetDispatch.sendMessage("You need an agility level of 30 to cross this.")
                return@on true
            }

            if (node.id == 5088) {
                content.global.skill.agility.AgilityHandler.walk(
                    player,
                    -1,
                    player.location,
                    Location.create(2687, 9506, 0),
                    Animation.create(155),
                    0.0,
                    null,
                )
            } else {
                content.global.skill.agility.AgilityHandler.walk(
                    player,
                    -1,
                    player.location,
                    Location.create(2682, 9506, 0),
                    Animation.create(155),
                    0.0,
                    null,
                )
            }
            return@on true
        }
    }

    companion object {
        private const val SANIBOCH_NPC = NPCs.SANIBOCH_1595
        private const val ENTRANCE = Scenery.DUNGEON_ENTRANCE_5083
        private const val EXIT = Scenery.EXIT_5084
        private val VINES =
            intArrayOf(
                Scenery.VINES_5103,
                Scenery.VINES_5104,
                Scenery.VINES_5105,
                Scenery.VINES_5106,
                Scenery.VINES_5107,
            )
        private val STEPPING_STONES =
            intArrayOf(
                Scenery.STEPPING_STONE_5110,
                Scenery.STEPPING_STONE_5111,
            )
        private val STAIRS =
            intArrayOf(
                Scenery.STAIRS_5094,
                Scenery.STAIRS_5096,
                Scenery.STAIRS_5097,
                Scenery.STAIRS_5098,
            )
        private val LOGS =
            intArrayOf(
                Scenery.LOG_BALANCE_5088,
                Scenery.LOG_BALANCE_5090,
            )
    }
}

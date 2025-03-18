package content.region.asgarnia.handlers.trollheim.godwars

import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class GodwarsEntranceListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.HOLE_26340, IntType.SCENERY, "tie-rope") { player, _ ->
            if (!removeItem(player, Items.ROPE_954)) {
                sendMessage(player, "You don't have a rope to tie around the pillar.")
                return@on true
            }
            setVarbit(player, 3932, 1, true)
            return@on true
        }

        on(Scenery.HOLE_26341, IntType.SCENERY, "climb-down") { player, _ ->
            if (getStatLevel(player, Skills.AGILITY) < 15) {
                sendMessage(player, "You need an agility level of 15 to enter this.")
                return@on true
            }
            if (getVarbit(player, 3936) == 0) {
                sendNPCDialogue(player, NPCs.KNIGHT_6201, "Cough... Hey, over here.", FaceAnim.HALF_GUILTY)
                return@on true
            }
            lock(player, 2)
            sendMessage(player, "You climb down the rope.")
            animate(player, Animations.USE_LADDER_828)
            GameWorld.Pulser.submit(
                object : Pulse(1, player) {
                    override fun pulse(): Boolean {
                        teleport(player, Location.create(2882, 5311, 2))
                        return true
                    }
                },
            )
            return@on true
        }

        on(Scenery.BOULDER_26338, IntType.SCENERY, "move") { player, node ->
            if (!isQuestComplete(player, Quests.TROLL_STRONGHOLD)) {
                sendMessage(player, "You need complete the Troll Stronghold quest to move this boulder.")
                return@on true
            }
            if (getStatLevel(player, Skills.STRENGTH) < 60) {
                sendMessage(player, "You need a Strength level of 60 to move this boulder.")
                return@on true
            }
            animateScenery(node.asScenery(), 6980)
            if (player.location.y < 3716) {
                ForceMovement.run(
                    player,
                    Location.create(2898, 3715, 0),
                    Location.create(2898, 3719, 0),
                    Animation(6978),
                    3,
                )
            } else {
                ForceMovement.run(
                    player,
                    Location.create(2898, 3719, 0),
                    Location.create(2898, 3715, 0),
                    Animation(6979),
                    3,
                )
            }
            submitWorldPulse(
                object : Pulse(12, player) {
                    override fun pulse(): Boolean {
                        animateScenery(RegionManager.getObject(0, 2898, 3716)!!.asScenery(), 6981)
                        return true
                    }
                },
            )
            return@on true
        }

        on(Scenery.LITTLE_CRACK_26305, IntType.SCENERY, "crawl-through") { player, node ->
            if (getStatLevel(player, Skills.AGILITY) < 60) {
                sendMessage(player, "You need an agility level of 60 to squeeze through the crack.")
                return@on true
            }
            teleport(
                player,
                if (node.location == Location.create(2900, 3713, 0)) {
                    Location.create(2904, 3720, 0)
                } else {
                    Location.create(2899, 3713, 0)
                },
            )
            return@on true
        }

        on(Scenery.CLIMBING_WALL_41068, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(2941, 3822, 0))
            return@on true
        }
    }
}

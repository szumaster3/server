package content.global.skill.agility.shortcuts

import content.data.GameAttributes
import core.api.*
import core.api.quest.isQuestComplete
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.build.DynamicRegion
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Quests

class GodWarsBoulderShortcut : InteractionListener {

    var boulder: Scenery? = null

    override fun defineListeners() {
        on(26338, IntType.SCENERY, "move") { player, node ->
            if (!isQuestComplete(player, Quests.TROLL_STRONGHOLD)) {
                sendMessage(player, "You need to complete the Troll Stronghold quest to move this boulder.")
                return@on true
            }

            if (getStatLevel(player, Skills.STRENGTH) < 60) {
                sendMessage(player, "You need a Strength level of 60 to move this boulder.")
                return@on true
            }

            val region = DynamicRegion.create(11578)
            region.add(player)
            region.flagActive()
            region.setRegionTimeOut(16)

            val isMovingNorth = player.location.y < 3716
            val startLocation = if (isMovingNorth) 3 else 7
            val targetLocation = region.baseLocation.transform(18, startLocation, 0)

            teleport(player, targetLocation)
            boulder = Scenery(node.id, region.baseLocation.transform(18,4,0), 10, 0)

            registerLogoutListener(player, GameAttributes.LOGOUT) { p ->
                p.location = getAttribute(p, GameAttributes.ORIGINAL_LOCATION, player.location)
            }

            val boulder = boulder!!.getChild(player)

            lock(player, 14)
            val moveOffsetY = if (isMovingNorth) 4 else -4
            submitWorldPulse(object : Pulse(1) {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            openOverlay(player, Components.SNOW_OVERLAY_370)
                            animateScenery(boulder!!, Animations.MOVE_BOULDER_6980)
                            ForceMovement.run(player, player.location, player.location.transform(0, moveOffsetY, 0), Animation(if (isMovingNorth) Animations.LIFT_REALLY_HEAVY_ROCK_6978 else Animations.LIFT_REALLY_HEAVY_ROCK_6979), 3)
                        }

                        12 -> {
                            animateScenery(boulder!!, Animations.MOVE_BOULDER_6981)
                        }

                        14 -> {
                            val finalY = 3715 + if (isMovingNorth) 4 else 0
                            teleport(player, Location.create(2898, finalY, 0), TeleportManager.TeleportType.INSTANT)
                            removeAttributes(player, GameAttributes.LOGOUT, GameAttributes.ORIGINAL_LOCATION)
                            return true
                        }
                    }
                    return false
                }
            })
            return@on true
        }
    }
}
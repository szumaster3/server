package content.minigame.mage_arena.plugin

import content.data.GodType
import core.api.*
import core.game.global.action.PickupHandler.take
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class MageArenaPlugin: InteractionListener {

    override fun defineListeners() {
        on(GOD_CAPE_ITEM_IDS, IntType.ITEM, "take", "drop") { player, node ->
            val type = GodType.forCape(node.id)
            if (getUsedOption(player) == "take") {
                val capeOnGround = node as GroundItem
                if (GodType.hasCape(player)) {
                    GroundItemManager.destroy(capeOnGround)
                    sendMessage(player, "You may only possess one sacred cape at a time. The conflicting powers of the capes drive them apart.")
                } else {
                    take(player, capeOnGround)
                }
                return@on true
            } else {
                if (type != null) {
                    sendMessage(player, type.dropMessage)
                    removeItem(player, type.capeId)
                }
            }
            return@on true
        }

        on(GOD_STATUE_SCENERY_IDS, IntType.SCENERY, "pray at") { player, node ->
            GodType.forStatue(node.id)?.pray(player, node.asScenery())
            return@on true
        }

        on(SPARKLING_POOL_SCENERY_IDS, IntType.SCENERY, "step-into") { player, node ->
            val hasCompleteMageArena = player.getSavedData().activityData.hasKilledKolodion()
            if (node.id != Scenery.SPARKLING_POOL_2879) {
                if (!hasCompleteMageArena) {
                    sendMessage(player, "You step into the pool.")
                    sendMessage(player, "Your boots get wet.", 1)
                    return@on true
                }
            }

            sendDialogueLines(player, "You step into the pool of sparkling water. You feel energy rush", "through your veins.")

            addDialogueAction(player) { _, _ ->
                val startDestination = if(node.id == Scenery.SPARKLING_POOL_2879) Location(2509, 4689, 0) else Location(2542, 4718, 0)
                val jumpDestination = if(node.id == Scenery.SPARKLING_POOL_2879) Location(2509, 4687, 0) else Location(2542, 4720, 0)
                val teleportDestination = if(node.id == Scenery.SPARKLING_POOL_2879) Location(2542, 4718, 0) else Location(2509, 4689, 0)

                registerLogoutListener(player, "sparking-pool-interaction") { pl: Player ->
                    pl.location = startDestination
                }

                queueScript(player, 1, QueueStrength.STRONG) { stage ->
                    when (stage) {
                        0 -> {
                            forceWalk(player, startDestination, "")
                            return@queueScript delayScript(player, 1)
                        }
                        1 -> {
                            faceLocation(player, node.asScenery().location)
                            playAudio(player, 2467, 1)
                            forceMove(player, startDestination, jumpDestination, 0, 30, null, Animations.HUMAN_JUMP_SHORT_GAP_741)
                            return@queueScript delayScript(player, 1)
                        }
                        2 -> {
                            playAudio(player, 2496)
                            animate(player, Animations.HUMAN_SHRINK_804)
                            sendGraphics(player, Graphics(shared.consts.Graphics.WATER_SPLASH_68, 100))
                            teleport(player, teleportDestination, TeleportManager.TeleportType.INSTANT, 1)
                            return@queueScript delayScript(player, 1)
                        }
                        3 -> {
                            resetAnimator(player)
                            clearLogoutListener(player, "sparking-pool-interaction")
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }
            return@on true
        }
    }

    companion object {
        private val GOD_CAPE_ITEM_IDS = intArrayOf(Items.SARADOMIN_CAPE_2412, Items.GUTHIX_CAPE_2413, Items.ZAMORAK_CAPE_2414)
        private val GOD_STATUE_SCENERY_IDS = intArrayOf(Scenery.STATUE_OF_SARADOMIN_2873, Scenery.STATUE_OF_ZAMORAK_2874, Scenery.STATUE_OF_GUTHIX_2875)
        private val SPARKLING_POOL_SCENERY_IDS = intArrayOf(Scenery.SPARKLING_POOL_2878, Scenery.SPARKLING_POOL_2879)
    }
}

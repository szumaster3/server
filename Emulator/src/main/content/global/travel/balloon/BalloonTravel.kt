package content.global.travel.balloon

import content.data.GameAttributes
import core.api.*
import core.api.quest.isQuestComplete
import core.api.ui.setMinimapState
import core.cache.def.impl.ItemDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

/**
 * Handles the balloon travel system.
 */
class BalloonTravel : InterfaceListener, InteractionListener {

    init {
        assistants.forEach { (npcId, location) ->
            NPC(npcId, location).apply {
                init()
                isWalks = true
            }
        }
    }

    companion object {
        /**
         * Represents the assistant npc.
         */
        private val assistantIds = intArrayOf(5050, 5053, 5054, 5055, 5056, 5057, 5063, 5065)

        /**
         * Represents the basket ids.
         */
        private val basketIds = intArrayOf(Scenery.BASKET_19128, Scenery.BASKET_19129)

        /**
         * Represents assistant npcs and their spawn locations.
         */
        private val assistants = mapOf(
            NPCs.ASSISTANT_SERF_5053 to Location.create(3298, 3484, 0),
            NPCs.ASSISTANT_LE_SMITH_5056 to Location.create(2480, 3458, 0),
            NPCs.ASSISTANT_STAN_5057 to Location.create(2938, 3424, 0)
        )

        /**
         * Opens the interface.
         */
        private fun openBalloonInterface(player: Player, location: Balloon) {
            player.setAttribute(GameAttributes.BALLOON_ORIGIN, location)
            openInterface(player, Components.ZEP_BALLOON_MAP_469)
            setComponentVisibility(player, Components.ZEP_BALLOON_MAP_469, location.componentId, false)
        }

        /**
         * Executes the flight.
         */
        fun flightPulse(player: Player, destination: Balloon) {
            val origin = player.getAttribute<Balloon>(GameAttributes.BALLOON_ORIGIN)
            if (origin == null) {
                player.debug("null location.")
                return
            }

            lock(player, 7)
            lockInteractions(player, 7)

            val animationId = Balloon.getAnimationId(origin, destination)
            val animationTime = Animation(animationId).duration

            playJingle(player, 118)

            closeInterface(player)
            setMinimapState(player, 2)
            openInterface(player, Components.FADE_TO_BLACK_120)
            sendMessage(player, "You board the balloon and fly to ${destination.areaName}.")
            queueScript(player, 4, QueueStrength.SOFT) { ticks: Int ->
                when (ticks) {
                    0 -> {
                        openInterface(player, Components.ZEP_BALLOON_MAP_469)
                        setComponentVisibility(player, Components.ZEP_BALLOON_MAP_469, 12, false)
                        animateInterface(player, Components.ZEP_BALLOON_MAP_469, 12, animationId)
                        player.teleport(destination.destination)
                        return@queueScript delayScript(player, animationTime)
                    }

                    1 -> {
                        unlock(player)
                        closeInterface(player)
                        setMinimapState(player, 0)
                        openOverlay(player, Components.FADE_FROM_BLACK_170)
                        removeAttribute(player, GameAttributes.BALLOON_ORIGIN)
                        sendDialogue(player, "You arrive safely in ${destination.areaName}.")
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.ZEP_BALLOON_MAP_469) { player, _, _, buttonID, _, _ ->
            val destination = Balloon.fromButtonId(buttonID) ?: return@on true
            val isAdmin = player.rights == Rights.ADMINISTRATOR

            //TODO: You can open new locations from Entrana.

            if (!hasLevelStat(player, Skills.FIREMAKING, destination.requiredLevel)) {
                sendDialogue(player, "You require a Firemaking level of ${destination.requiredLevel} to travel to ${destination.areaName}.")
                return@on true
            }

            val origin = player.getAttribute<Balloon>(GameAttributes.BALLOON_ORIGIN)
            if (origin == destination) {
                sendDialogue(player, "You can't fly to the same location.")
                return@on true
            }

            if (player.familiarManager.hasFamiliar()) {
                sendMessage(player, "You can't take a follower on a ride.")
                return@on true
            }

            if(player.settings.weight > 40.0) {
                sendDialogue(player, "You're carrying too much weight to fly. Try reducing your weight below 40 kg.")
                return@on true
            }

            if (destination == Balloon.ENTRANA) {
                if (!isAdmin && !ItemDefinition.canEnterEntrana(player)) {
                    sendDialogue(player, "You can't take flight with weapons and armour to Entrana.")
                    return@on true
                } else {
                    sendMessage(player, "You are quickly searched.")
                }
            }

            if (isAdmin) {
                flightPulse(player, destination)
                return@on true
            }

            if (removeItem(player, Item(destination.logId, 1))) {
                flightPulse(player, destination)
                if (destination == Balloon.VARROCK) {
                    finishDiaryTask(player, DiaryType.VARROCK, 2, 17)
                }
            } else {
                val requiredItem = getItemName(destination.logId).lowercase().removeSuffix("s").trim()
                sendDialogue(player, "You need at least one $requiredItem.")
            }
            return@on true
        }
    }

    override fun defineListeners() {

        /*
         * Handles interaction with basket scenery.
         */

        on(basketIds, IntType.SCENERY, "use") { player, node ->
            val sceneryId = node.asScenery().wrapper.id
            val location = Balloon.fromSceneryId(sceneryId)
            if (location != null) {
                openBalloonInterface(player, location)
            }
            return@on true
        }

        /*
         * Handles interaction with npc.
         */

        on(assistantIds, IntType.NPC, "Fly") { player, node ->
            if (!isQuestComplete(player, Quests.ENLIGHTENED_JOURNEY)) {
                sendMessage(player, "You must complete ${Quests.ENLIGHTENED_JOURNEY} before you can use it.")
                return@on true
            }

            val location = Balloon.fromNpcId(node.id)
            if (location != null) {
                openBalloonInterface(player, location)
            }
            return@on true
        }
    }
}
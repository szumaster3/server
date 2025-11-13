package content.region.kandarin.yanille.plugin

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class YanilleAgilityDungeonPlugin : MapZone("Yanille agility", true), InteractionListener {

    private val sinisterChestContent = arrayOf(
        Item(Items.GRIMY_HARRALANDER_205, 2),
        Item(Items.GRIMY_RANARR_207, 3),
        Item(Items.GRIMY_IRIT_209),
        Item(Items.GRIMY_AVANTOE_211),
        Item(Items.GRIMY_KWUARM_213),
        Item(Items.GRIMY_TORSTOL_219)
    )

    override fun configure() {
        register(ZoneBorders(2544, 9481, 2631, 9587))
    }

    override fun defineListeners() {
        ZoneBuilder.configure(this)

        on(Scenery.STAIRCASE_1728, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location(2620, 9565, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(Scenery.STAIRCASE_1729, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location(2620, 9496, 0),  TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(Scenery.STAIRCASE_2316, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location(2569, 9525, 0),  TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles climbing up or down a pile of rubble in the agility course.
         */

        on(intArrayOf(Scenery.PILE_OF_RUBBLE_2318, Scenery.PILE_OF_RUBBLE_2317), IntType.SCENERY, "climb-up", "climb-down") { player, target ->

            if (getStatLevel(player, Skills.AGILITY) < 67) {
                sendMessage(player, "You need an agility level of at least 67 in order to do this.")
                return@on true
            }

            lock(player, 3)

            queueScript(player, delay = 1, strength = QueueStrength.SOFT) {
                val isClimbingUp = target.id == Scenery.PILE_OF_RUBBLE_2317
                val destination = if (isClimbingUp) Location(2614, 9504, 0) else Location(2617, 9571, 0)
                ClimbActionHandler.climb(player, if (isClimbingUp) ClimbActionHandler.CLIMB_UP else ClimbActionHandler.CLIMB_DOWN, destination)
                sendMessage(player, "You climb ${if (isClimbingUp) "up" else "down"} the pile of rubble...")
                rewardXP(player, Skills.AGILITY, 5.5)
                return@queueScript stopExecuting(player)
            }

            return@on true
        }

        /*
         * Handles walking across the balancing ledges in the agility course.
         */

        on(intArrayOf(Scenery.BALANCING_LEDGE_35969, Scenery.BALANCING_LEDGE_2303), IntType.SCENERY, "walk-across") { player, node ->
            val target = node as core.game.node.scenery.Scenery

            if (getStatLevel(player, Skills.AGILITY) < 40) {
                sendMessage(player, "You need an agility level of at least 40 in order to do this.")
                return@on true
            }

            val dir = Direction.getLogicalDirection(player.location, target.location)
            val diff = if (player.location.y == 9512) 0 else 1

            var end = target.location
            val failChance = AgilityHandler.hasFailed(player, 40, 0.01)

            if (failChance) {
                lock(player, 3)
                GameWorld.Pulser.submit(object : Pulse(2, player) {
                    override fun pulse(): Boolean {
                        AgilityHandler.fail(player, 1, Location(2572, 9570, 0), Animation.create(761 - diff), RandomFunction.random(1, 3), "You lost your balance!")
                        runTask(player, 2) {
                            animate(player, Animations.ON_BACK_GET_UP_BRUSH_SELF_5056, true)
                        }
                        return true
                    }
                })
            } else {
                end = target.location.transform(dir.stepX * 7, dir.stepY * 7, 0)
                AgilityHandler.walk(
                    player,
                    -1,
                    player.location,
                    end,
                    Animation.create(157 - diff),
                    22.5,
                    null
                )
            }

            return@on true
        }

        /*
         * Handles the interaction with the sinister chest.
         */

        on(Scenery.CLOSED_CHEST_377, IntType.SCENERY, "open") { player, target ->
            if (!inInventory(player, Items.SINISTER_KEY_993)) {
                sendMessage(player, "The chest is locked.")
                return@on true
            }

            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space.")
                return@on true
            }

            lock(player, 1)

            if (removeItem(player, Items.SINISTER_KEY_993)) {
                sendMessage(player, "You unlock the chest with your key... A foul gas seeps from the chest.")
                applyPoison(player, player, 2)

                for (item in sinisterChestContent) {
                    addItemOrDrop(player, item.id, item.amount)
                }

                SceneryBuilder.replace(
                    target.asScenery(),
                    target.asScenery().transform(target.id + 1), 5
                )
            }

            return@on true
        }

        on(Scenery.OPEN_CHEST_378, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "The chest is empty.")
            return@on true
        }

        on(Scenery.OPEN_CHEST_378, IntType.SCENERY, "shut") { _, _ ->
            return@on true
        }
    }
}

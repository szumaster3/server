package content.global.skill.agility.courses.barbarian

import content.region.kandarin.miniquest.barcrawl.BarcrawlManager
import core.api.animateScenery
import core.api.impact
import core.api.playAudio
import core.api.sendMessage
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Sounds

@Initializable
class BarbarianOutpostCourse
    @JvmOverloads
    constructor(
        player: Player? = null,
    ) : content.global.skill.agility.AgilityCourse(player, 6, 46.2) {
        override fun createInstance(player: Player): content.global.skill.agility.AgilityCourse {
            return BarbarianOutpostCourse(player)
        }

        override fun handle(
            player: Player,
            node: Node,
            option: String,
        ): Boolean {
            val id = node.id
            getCourse(player)
            when (id) {
                2115, 2116 ->
                    if (!BarcrawlManager.getInstance(player).isFinished ||
                        BarcrawlManager
                            .getInstance(player)
                            .isStarted()
                    ) {
                        player.dialogueInterpreter.open(384)
                    } else {
                        DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                    }

                2282 -> handleRopeSwing(player, node as Scenery)
                2294 -> handleLogBalance(player, node as Scenery)
                2302 -> handleLedgeBalance(player, node as Scenery)
                20211 -> {
                    if (player.location.x < node.location.x) {
                        return true
                    }
                    content.global.skill.agility.AgilityHandler.climb(
                        player,
                        2,
                        ClimbActionHandler.CLIMB_UP,
                        player.location.transform(-1, 0, 1),
                        8.0,
                        "You climb the netting...",
                    )
                }

                1948 -> {
                    if (player.location.x > node.location.x) {
                        player.packetDispatch.sendMessage("You cannot climb from this side.")
                        return true
                    }
                    val flag =
                        if (node.location == Location(2536, 3553, 0)) {
                            4
                        } else if (node.location ==
                            Location(
                                2539,
                                3553,
                                0,
                            )
                        ) {
                            5
                        } else {
                            6
                        }
                    sendMessage(player, "You climb the low wall...")
                    content.global.skill.agility.AgilityHandler.forceWalk(
                        player,
                        flag,
                        node.location.transform(-1, 0, 0),
                        node.location.transform(1, 0, 0),
                        Animation.create(839),
                        10,
                        13.5,
                        null,
                    )
                }

                455 -> BarcrawlManager.getInstance(player).read()
                385 -> {
                    sendMessage(player, "The scorpion stings you!")
                    impact(player, 3, HitsplatType.NORMAL)
                }

                386 -> {
                    sendMessage(player, "The scorpion stings you!")
                    impact(player, 3, HitsplatType.NORMAL)
                }

                387 -> {
                    sendMessage(player, "The scorpion stings you!")
                    impact(player, 3, HitsplatType.NORMAL)
                }
            }
            return true
        }

        private fun handleRopeSwing(
            player: Player,
            scenery: Scenery,
        ) {
            if (player.location.y < 3554) {
                sendMessage(player, "You cannot do that from here.")
                return
            }
            if (ropeDelay > GameWorld.ticks) {
                sendMessage(player, "The rope is being used.")
                return
            }
            if (content.global.skill.agility.AgilityHandler
                    .hasFailed(player, 1, 0.1)
            ) {
                content.global.skill.agility.AgilityHandler.fail(
                    player,
                    0,
                    Location.create(2549, 9951, 0),
                    null,
                    getHitAmount(player),
                    "You slip and fall to the pit below.",
                )
                return
            }
            ropeDelay = GameWorld.ticks + 2
            content.global.skill.agility.AgilityHandler.forceWalk(
                player,
                0,
                player.location,
                Location.create(2551, 3549, 0),
                Animation.create(751),
                50,
                22.0,
                "You skillfully swing across.",
                1,
            )
            playAudio(player, Sounds.SWING_ACROSS_2494, 1)
            animateScenery(player, scenery, 497, true)
        }

        private fun handleLogBalance(
            player: Player,
            scenery: Scenery,
        ) {
            val failed =
                content.global.skill.agility.AgilityHandler
                    .hasFailed(player, 1, 0.5)
            val end = if (failed) Location.create(2545, 3546, 0) else Location.create(2541, 3546, 0)
            sendMessage(player, "You walk carefully across the slippery log...")
            content.global.skill.agility.AgilityHandler.walk(
                player,
                if (failed) -1 else 1,
                Location.create(2551, 3546, 0),
                end,
                Animation.create(155),
                if (failed) 0.0 else 13.5,
                if (failed) null else "...You make it safely to the other side.",
            )
            if (failed) {
                content.global.skill.agility.AgilityHandler.walk(
                    player,
                    -1,
                    player.location,
                    Location.create(2545, 3546, 0),
                    Animation.create(155),
                    0.0,
                    null,
                )
                GameWorld.Pulser.submit(getSwimPulse(player))
                return
            }
        }

        private fun getSwimPulse(player: Player): Pulse {
            return object : Pulse(1, player) {
                var counter = 0

                override fun pulse(): Boolean {
                    when (++counter) {
                        6 -> {
                            player.animate(Animation.create(771))
                            sendMessage(
                                player,
                                "...You loose your footing and fall into the water. Something in the water bites you.",
                            )
                        }

                        7 -> {
                            player.graphics(Graphics.create(68))
                            player.properties.teleportLocation = Location.create(2545, 3545, 0)
                            player.impactHandler.manualHit(player, getHitAmount(player), HitsplatType.NORMAL)
                            content.global.skill.agility.AgilityHandler.walk(
                                player,
                                -1,
                                Location.create(2545, 3545, 0),
                                Location.create(2545, 3543, 0),
                                Animation.create(152),
                                0.0,
                                null,
                            )
                        }

                        11 -> {
                            player.properties.teleportLocation = Location.create(2545, 3542, 0)
                            return true
                        }
                    }
                    return false
                }
            }
        }

        private fun handleLedgeBalance(
            player: Player,
            scenery: Scenery,
        ) {
            val failed =
                content.global.skill.agility.AgilityHandler
                    .hasFailed(player, 1, 0.3)
            val end = if (failed) Location.create(2534, 3547, 1) else Location.create(2532, 3547, 1)
            content.global.skill.agility.AgilityHandler.walk(
                player,
                if (failed) -1 else 3,
                Location.create(2536, 3547, 1),
                end,
                Animation.create(157),
                if (failed) 0.0 else 22.0,
                if (failed) null else "You skillfully edge across the gap.",
            )
            sendMessage(player, "You put your foot on the ledge and try to edge across..")
            if (failed) {
                content.global.skill.agility.AgilityHandler.fail(
                    player,
                    3,
                    Location.create(2534, 3545, 0),
                    Animation(760),
                    getHitAmount(player),
                    "You slip and fall to the pit below.",
                )
                return
            }
        }

        override fun configure() {
            SceneryDefinition.forId(2115).handlers["option:open"] = this
            SceneryDefinition.forId(2116).handlers["option:open"] = this
            SceneryDefinition.forId(2282).handlers["option:swing-on"] = this
            SceneryDefinition.forId(2294).handlers["option:walk-across"] = this
            SceneryDefinition.forId(20211).handlers["option:climb-over"] = this
            SceneryDefinition.forId(2302).handlers["option:walk-across"] = this
            SceneryDefinition.forId(1948).handlers["option:climb-over"] = this
            ItemDefinition.forId(455).handlers["option:read"] = this
            NPCDefinition.forId(385).handlers["option:pick-up"] = this
            NPCDefinition.forId(386).handlers["option:pick-up"] = this
            NPCDefinition.forId(387).handlers["option:pick-up"] = this
        }

        override fun getDestination(
            node: Node,
            n: Node,
        ): Location? {
            if (n is Scenery) {
                when (n.getId()) {
                    2282 -> return Location.create(2551, 3554, 0)
                }
            }
            return null
        }

        companion object {
            private var ropeDelay = 0
        }
    }

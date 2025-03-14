package content.global.skill.agility.grapple

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items

@Initializable
class KaramjaGrapple : OptionHandler() {
    companion object {
        private val REQUIREMENTS = HashMap<Int?, Int>()
        private var requirementsString: String? = null
        private val ROPES: MutableList<Scenery> = ArrayList(20)
        private val RUN_ANIMATION = Animation(1995)

        init {
            REQUIREMENTS.putIfAbsent(Skills.AGILITY, 53)
            REQUIREMENTS.putIfAbsent(Skills.RANGE, 42)
            REQUIREMENTS.putIfAbsent(Skills.STRENGTH, 21)
            requirementsString =
                "You need at least " + REQUIREMENTS[Skills.AGILITY] + " " + Skills.SKILL_NAME[Skills.AGILITY] + ", " +
                REQUIREMENTS[Skills.RANGE] +
                " " +
                Skills.SKILL_NAME[Skills.RANGE] +
                ", and " +
                REQUIREMENTS[Skills.STRENGTH] +
                " " +
                Skills.SKILL_NAME[Skills.STRENGTH] +
                " to use this shortcut."
        }

        private val crossbowIds =
            intArrayOf(
                Items.DORGESHUUN_CBOW_8880,
                Items.MITH_CROSSBOW_9181,
                Items.ADAMANT_CROSSBOW_9183,
                Items.RUNE_CROSSBOW_9185,
                Items.KARILS_CROSSBOW_4734,
                Items.HUNTERS_CROSSBOW_10156,
            )
        private val grappleId = Items.MITH_GRAPPLE_9419
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(17074).handlers["option:grapple"] = this
        return this
    }

    fun handleObjects(
        add: Boolean,
        player: Player,
    ) {
        val current = player.location
        if (add) {
            if (current.y > 3134) {
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(2874, 3141, 0),
                        10,
                        0,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(2874, 3140, 0),
                        10,
                        0,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(2874, 3139, 0),
                        10,
                        0,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(2874, 3138, 0),
                        10,
                        0,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(2874, 3137, 0),
                        10,
                        0,
                    ),
                )
            } else {
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(2874, 3128, 0),
                        10,
                        0,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(2874, 3129, 0),
                        10,
                        0,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(2874, 3130, 0),
                        10,
                        0,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(2874, 3131, 0),
                        10,
                        0,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(2874, 3132, 0),
                        10,
                        0,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(2874, 3133, 0),
                        10,
                        0,
                    ),
                )
            }
            for (rope in ROPES) {
                SceneryBuilder.add(rope)
            }
        } else {
            for (rope in ROPES) {
                SceneryBuilder.remove(rope)
            }
            ROPES.clear()
        }
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val current = player.location
        val startTree: Scenery?
        val endTree: Scenery?
        val direction: Direction
        if (current.y > 3134) {
            startTree = getObject(Location.create(2874, 3144, 0))
            endTree = getObject(Location.create(2873, 3125, 0))
            direction = Direction.SOUTH
        } else {
            startTree = getObject(Location.create(2873, 3125, 0))
            endTree = getObject(Location.create(2874, 3144, 0))
            direction = Direction.NORTH
        }
        val islandTree = getObject(Location.create(2873, 3134, 0))
        when (option) {
            "grapple" -> {
                for ((key, value) in REQUIREMENTS) {
                    if (player.getSkills().getLevel(key!!) < value) {
                        sendDialogue(player, requirementsString.toString())
                        return true
                    }
                }
                if (!anyInEquipment(player, *crossbowIds) || !inEquipment(player, grappleId)) {
                    sendMessage(player, "You need a mithril grapple tipped bolt with a rope to do that.")
                    return true
                }
                player.lock()
                Pulser.submit(
                    object : Pulse(0, player) {
                        var counter = 1

                        override fun pulse(): Boolean {
                            when (counter++) {
                                1 -> {
                                    player.faceLocation(player.location.transform(direction))
                                    player.animate(Animation(Animations.FIRE_CROSSBOW_4230))
                                }

                                3 ->
                                    player.packetDispatch.sendPositionedGraphic(
                                        67,
                                        10,
                                        0,
                                        player.location.transform(direction, 5),
                                    )

                                4 -> {
                                    SceneryBuilder.replace(startTree, startTree!!.transform(startTree.id + 1), 10)
                                    SceneryBuilder.replace(islandTree, islandTree!!.transform(islandTree.id + 2), 10)
                                }

                                5 -> visualize(player, -1, Graphics.create(org.rs.consts.Graphics.WATER_SPLASH_68))
                                6 -> {
                                    ForceMovement.run(
                                        player,
                                        player.location,
                                        if (current.y > 3134) {
                                            Location.create(2874, 3136)
                                        } else {
                                            Location.create(
                                                2874,
                                                3133,
                                            )
                                        },
                                        Animation.create(4466),
                                        ForceMovement.RUNNING_SPEED,
                                    )
                                    handleObjects(true, player)
                                }

                                12 -> {
                                    ForceMovement.run(
                                        player,
                                        player.location,
                                        if (current.y > 3134) {
                                            Location.create(2875, 3136)
                                        } else {
                                            Location.create(
                                                2875,
                                                3133,
                                            )
                                        },
                                        ForceMovement.WALK_ANIMATION,
                                        ForceMovement.WALKING_SPEED,
                                    )
                                    handleObjects(false, player)
                                }

                                15 ->
                                    ForceMovement.run(
                                        player,
                                        player.location,
                                        if (current.y >
                                            3134
                                        ) {
                                            Location.create(2875, 3133)
                                        } else {
                                            Location.create(2875, 3136)
                                        },
                                        RUN_ANIMATION,
                                        ForceMovement.RUNNING_SPEED,
                                    )

                                18 ->
                                    ForceMovement.run(
                                        player,
                                        player.location,
                                        if (current.y >
                                            3134
                                        ) {
                                            Location.create(2874, 3133)
                                        } else {
                                            Location.create(2874, 3136)
                                        },
                                        ForceMovement.WALK_ANIMATION,
                                        ForceMovement.WALKING_SPEED,
                                    )

                                21 -> {
                                    player.faceLocation(player.location.transform(direction))
                                    animate(player, Animation(Animations.FIRE_CROSSBOW_4230))
                                }

                                23 ->
                                    player.packetDispatch.sendPositionedGraphic(
                                        67,
                                        10,
                                        0,
                                        player.location.transform(direction, 5),
                                    )

                                24 -> {
                                    SceneryBuilder.replace(islandTree, islandTree!!.transform(islandTree.id + 1), 10)
                                    SceneryBuilder.replace(endTree, endTree!!.transform(endTree.id + 1), 10)
                                    SceneryBuilder.replace(startTree, startTree!!.transform(startTree.id + 2), 10)
                                }

                                25 -> visualize(player, -1, Graphics.create(68))
                                26 -> {
                                    ForceMovement.run(
                                        player,
                                        player.location,
                                        if (current.y > 3134) {
                                            Location.create(2874, 3127)
                                        } else {
                                            Location.create(
                                                2874,
                                                3142,
                                            )
                                        },
                                        Animation.create(4466),
                                        ForceMovement.RUNNING_SPEED,
                                    )
                                    handleObjects(true, player)
                                }

                                34 -> {
                                    player.unlock()
                                    finishDiaryTask(player, DiaryType.KARAMJA, 2, 6)
                                    handleObjects(false, player)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
        }
        return true
    }

    override fun getDestination(
        moving: Node,
        destination: Node,
    ): Location {
        return if (moving.location.y > 3134) {
            Location.create(2874, 3142, 0)
        } else {
            Location.create(2874, 3127, 0)
        }
    }
}

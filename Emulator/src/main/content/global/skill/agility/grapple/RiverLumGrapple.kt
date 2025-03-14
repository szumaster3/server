package content.global.skill.agility.grapple

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
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
class RiverLumGrapple : OptionHandler() {
    companion object {
        private val REQUIREMENTS = HashMap<Int?, Int>()
        private var requirementsString: String? = null
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
        private val ROPES: MutableList<Scenery> = ArrayList(20)

        init {
            REQUIREMENTS.putIfAbsent(Skills.AGILITY, 8)
            REQUIREMENTS.putIfAbsent(Skills.RANGE, 37)
            REQUIREMENTS.putIfAbsent(Skills.STRENGTH, 17)
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
    }

    fun handleRopeScenery(
        add: Boolean,
        player: Player,
    ) {
        val current = player.location
        if (add) {
            if (current.x > 3258 || current.x == 3253) {
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(3257, 3179, 0),
                        10,
                        1,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(3256, 3179, 0),
                        10,
                        1,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(3255, 3179, 0),
                        10,
                        1,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(3254, 3179, 0),
                        10,
                        1,
                    ),
                )
            } else {
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(3251, 3179, 0),
                        10,
                        1,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(3250, 3179, 0),
                        10,
                        1,
                    ),
                )
                ROPES.add(
                    Scenery(
                        1998,
                        Location.create(3249, 3179, 0),
                        10,
                        1,
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

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(17068).handlers["option:grapple"] = this
        return this
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
        if (current == Location.create(3259, 3179, 0)) {
            startTree = getObject(Location.create(3260, 3178, 0))
            endTree = getObject(Location.create(3244, 3179, 0))
            direction = Direction.WEST
        } else {
            startTree = getObject(Location.create(3244, 3179, 0))
            endTree = getObject(Location.create(3260, 3178, 0))
            direction = Direction.EAST
        }
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
                                    handleRopeScenery(true, player)
                                }

                                2 -> {
                                    SceneryBuilder.replace(startTree, startTree!!.transform(startTree.id + 1), 10)
                                    sendMessage(player, "You successfully grapple the raft and tie the rope to a tree.")
                                }

                                4 -> {
                                    visualize(player, -1, Graphics.create(org.rs.consts.Graphics.WATER_SPLASH_68))
                                    ForceMovement.run(
                                        player,
                                        player.location,
                                        if (current.x > 3258) {
                                            Location.create(3253, 3179, 0)
                                        } else {
                                            Location.create(
                                                3251,
                                                3179,
                                                0,
                                            )
                                        },
                                        Animation.create(Animations.PULL_SELF_THROUGH_WATER_4464),
                                        ForceMovement.WALKING_SPEED,
                                    )
                                }

                                12 -> {
                                    handleRopeScenery(false, player)
                                }

                                13 ->
                                    ForceMovement.run(
                                        player,
                                        player.location,
                                        if (current.x > 3258) {
                                            Location.create(3252, 3180, 0)
                                        } else {
                                            Location.create(
                                                3253,
                                                3179,
                                                0,
                                            )
                                        },
                                        ForceMovement.WALK_ANIMATION,
                                        ForceMovement.WALKING_SPEED,
                                    )

                                16 -> {
                                    player.faceLocation(player.location.transform(direction))
                                    animate(player, Animation(Animations.FIRE_CROSSBOW_4230))
                                    sendMessage(
                                        player,
                                        if (current.x <=
                                            3253
                                        ) {
                                            "You successfully grapple the tree on the opposite bank."
                                        } else {
                                            "You successfully grapple the tree."
                                        },
                                    )
                                }

                                18 -> {
                                    SceneryBuilder.replace(endTree, endTree!!.transform(endTree.id + 2), 10)
                                    SceneryBuilder.replace(startTree, startTree!!.transform(startTree.id + 1), 10)
                                    handleRopeScenery(true, player)
                                }

                                19 -> visualize(player, -1, Graphics.create(org.rs.consts.Graphics.WATER_SPLASH_68))
                                20 ->
                                    ForceMovement.run(
                                        player,
                                        player.location,
                                        if (current.x > 3258) {
                                            Location.create(3246, 3179, 0)
                                        } else {
                                            Location.create(
                                                3259,
                                                3179,
                                                0,
                                            )
                                        },
                                        Animation.create(Animations.PULL_SELF_THROUGH_WATER_4466),
                                        ForceMovement.WALKING_SPEED,
                                    )

                                26 -> {
                                    player.unlock()
                                    handleRopeScenery(false, player)
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
        return if (moving.location.x > 3258) {
            Location.create(3259, 3179, 0)
        } else {
            Location.create(3246, 3179, 0)
        }
    }
}

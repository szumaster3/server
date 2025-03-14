package content.global.skill.agility.courses.pyramid

import content.global.skill.agility.AgilityCourse
import content.global.skill.agility.AgilityHandler
import core.api.getVarp
import core.api.playAudio
import core.api.playJingle
import core.api.setVarp
import core.cache.def.impl.SceneryDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.global.action.ClimbActionHandler
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import org.rs.consts.Sounds

@Initializable
class AgilityPyramidCourse
    @JvmOverloads
    constructor(
        player: Player? = null,
    ) : AgilityCourse(player, 5, 0.0) {
        override fun configure() {
            SceneryDefinition.forId(org.rs.consts.Scenery.CLIMBING_ROCKS_16535).handlers["option:climb"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.CLIMBING_ROCKS_16536).handlers["option:climb"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.CLIMBING_ROCKS_10851).handlers["option:climb"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.DOORWAY_10855).handlers["option:enter"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.DOORWAY_10856).handlers["option:enter"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.LEDGE_10860).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.GAP_10861).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.GAP_10862).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.GAP_10863).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.GAP_10864).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.PLANK_10868).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.PLANK_10867).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.GAP_10882).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.GAP_10883).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.GAP_10884).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.GAP_10885).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.LEDGE_10886).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.LEDGE_10887).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.LEDGE_10888).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.LEDGE_10889).handlers["option:cross"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.GAP_10859).handlers["option:jump"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.STAIRS_10857).handlers["option:climb-up"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.STAIRS_10858).handlers["option:climb-down"] = this
            SceneryDefinition.forId(org.rs.consts.Scenery.LOW_WALL_10865).handlers["option:climb-over"] = this
            RollingBlock.BlockSets.values()
            definePlugin(MovingBlockNPC())
            definePlugin(AgilityPyramidZone())
        }

        override fun handle(
            player: Player,
            node: Node,
            option: String,
        ): Boolean {
            getCourse(player)
            val scenery = node as Scenery
            if (scenery.location.getDistance(player.location) >= 3) {
                player.packetDispatch.sendMessage("I can't reach that!")
                return true
            }
            when (scenery.id) {
                16535, 16536 -> handleRockClimb(player, scenery)
                10865 -> handleLowWall(player, scenery)
                10888, 10889, 10860, 10886, 10887 -> handleLedge(player, scenery)
                10868, 10867 -> handlePlank(player, scenery)
                10882, 10883, 10884, 10885, 10861, 10862, 10863, 10864 -> handleGapCross(player, scenery)
                10857, 10858 -> handleStairs(player, scenery)
                10851 -> handlePyramidTop(player, scenery)
                10859 -> handleJumpGap(player, scenery)
                10855, 10856 -> {
                    addConfig(player, 10869, 0, true)
                    if (getVarp(player, 640) == 505 || getVarp(player, 640) == 515) {
                        player.getSkills().addExperience(Skills.AGILITY, 300.0, true)
                    }
                    player.dialogueInterpreter.sendDialogue(
                        "You climb down the steep passage. It leads to the base of the",
                        "pyramid.",
                    )
                    player.properties.teleportLocation = Location.create(3364, 2830, 0)
                    player.getSavedData().activityData.isTopGrabbed = false
                }
            }
            return true
        }

        private fun handleStairs(
            player: Player,
            scenery: Scenery,
        ) {
            val dir = Direction.getLogicalDirection(player.location, scenery.location)
            var dest: Location? = null
            if (dir == Direction.NORTH) {
                dest = scenery.location.transform(0, 2, 1)
            } else if (dir == Direction.SOUTH) {
                dest = scenery.location.transform(0, -1, -1)
            }
            if (scenery.location == Location.create(3360, 2837, 3)) {
                dest = Location.create(3040, 4695, 2)
            } else if (scenery.location == Location.create(3040, 4693, 2)) {
                dest = Location.create(3360, 2836, 3)
            } else if (scenery.location == Location(3354, 2831, 0)) {
                addConfig(player, 10869, 0, true)
            }
            ClimbActionHandler.climb(player, null, dest!!)
        }

        private fun handleRockClimb(
            player: Player,
            scenery: Scenery,
        ) {
            val scale = player.location.x < scenery.location.x
            val end = scenery.location.transform(if (scale) 2 else -2, 0, 0)
            if (scenery.id == 16536 && player.getSkills().getStaticLevel(Skills.AGILITY) < 30) {
                player.packetDispatch.sendMessage("You must be level 30 agility or higher to climb down the rocks.")
                return
            }
            playAudio(player, Sounds.CLIMBING_LOOP_2454, 0, 6)
            if (!scale) {
                ForceMovement
                    .run(
                        player,
                        player.location,
                        end,
                        Animation.create(740),
                        Animation.create(740),
                        Direction.WEST,
                        13,
                    ).endAnimation = Animation.RESET
            } else {
                ForceMovement
                    .run(
                        player,
                        player.location,
                        end,
                        Animation.create(1148),
                        Animation.create(1148),
                        Direction.WEST,
                        13,
                    ).endAnimation = Animation.RESET
            }
        }

        private fun handleLowWall(
            player: Player,
            scenery: Scenery,
        ) {
            var d = Direction.getDirection(player.location, scenery.location)
            val fail = player.getSkills().getLevel(Skills.AGILITY) < 75 && hasFailed(player)
            if (player.location == Location.create(3355, 2848, 1) ||
                player.location == Location.create(3359, 2838, 3)
            ) {
                d = Direction.NORTH
            } else if (player.location == Location.create(3355, 2850, 1) ||
                player.location ==
                Location.create(
                    3359,
                    2840,
                    3,
                )
            ) {
                d = Direction.SOUTH
            } else if (player.location == Location.create(3046, 4694, 2) ||
                player.location ==
                Location.create(
                    3355,
                    2850,
                    1,
                ) ||
                player.location == Location.create(3041, 4702, 2) ||
                player.location == Location(3369, 2834, 2)
            ) {
                d = Direction.EAST
            } else if (player.location == Location.create(3048, 4694, 2) ||
                player.location ==
                Location.create(
                    3371,
                    2834,
                    2,
                ) ||
                player.location == Location.create(3043, 4702, 2)
            ) {
                d = Direction.WEST
            }
            player.lock(4)
            player.packetDispatch.sendMessage("You climb the low wall...")
            if (fail) {
                val end = player.location.transform(d, 1)
                player.lock(3)
                playAudio(player, Sounds.CLIMB_WALL_2453, 40)
                AgilityHandler.failWalk(
                    player,
                    2,
                    player.location,
                    end,
                    end,
                    Animation.create(1106),
                    15,
                    getHitAmount(player),
                    "You lost your balance!",
                )
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    end,
                    player.location,
                    ForceMovement.WALK_ANIMATION,
                    10,
                    0.0,
                    null,
                    4,
                )
                return
            }
            playAudio(player, Sounds.CLIMB_WALL_2453, 10, 40)
            AgilityHandler.forceWalk(
                player,
                0,
                player.location,
                player.location.transform(d, 2),
                Animation.create(1252),
                6,
                8.0,
                "... and make it over.",
            )
            player.animate(Animation.RESET, 4)
        }

        private fun handleLedge(
            player: Player,
            scenery: Scenery,
        ) {
            val d = Direction.getLogicalDirection(player.location, getLedgeLocation(player, scenery))
            val dir = d
            val diff =
                if (scenery.rotation == 3 &&
                    dir == Direction.EAST
                ) {
                    1
                } else if (scenery.rotation == 3 &&
                    dir == Direction.WEST
                ) {
                    0
                } else if (d == Direction.EAST ||
                    (d == Direction.SOUTH && scenery.rotation != 0) ||
                    d == Direction.NORTH
                ) {
                    0
                } else {
                    1
                }
            val fail = player.getSkills().getLevel(Skills.AGILITY) < 75 && hasFailed(player)
            val end = player.location.transform(dir.stepX * (if (fail) 3 else 5), dir.stepY * (if (fail) 3 else 5), 0)
            player.packetDispatch.sendMessage("You put your foot on the ledge and try to edge across...")
            playAudio(player, Sounds.BALANCING_LEDGE_2451, 0, 5)
            if (fail) {
                player.lock(4)
                playAudio(player, Sounds.FALL_LAND_2455, 125)
                AgilityHandler.walk(
                    player,
                    -1,
                    player.location,
                    end,
                    Animation.create(157 - diff),
                    0.0,
                    "You slip and fall to the level below.",
                )
                Pulser.submit(
                    object : Pulse(3, player) {
                        override fun pulse(): Boolean {
                            val dest = end
                            var x = 0
                            var y = 0
                            when (dir) {
                                Direction.NORTH, Direction.SOUTH ->
                                    x =
                                        if (scenery.rotation == 2) {
                                            2
                                        } else {
                                            -2
                                        }

                                Direction.WEST, Direction.EAST ->
                                    if (scenery.rotation == 3) {
                                        y = -2
                                    } else if (scenery.rotation == 1) {
                                        y = 2
                                    }

                                else -> {}
                            }
                            AgilityHandler.fail(
                                player,
                                1,
                                transformLevel(dest.transform(x, y, 0)),
                                Animation.create(761 - diff),
                                10,
                                null,
                            )
                            return true
                        }
                    },
                )
                return
            }
            AgilityHandler.walk(
                player,
                3,
                player.location,
                end,
                Animation.create(157 - diff),
                52.0,
                "You skillfully edge across the gap.",
            )
        }

        private fun handlePlank(
            player: Player,
            scenery: Scenery,
        ) {
            val custom = scenery.location == Location(3365, 2835, 3) || scenery.location == Location(3370, 2835, 3)
            val dir = if (custom) Direction.EAST else Direction.getLogicalDirection(player.location, scenery.location)
            val fail = player.getSkills().getLevel(Skills.AGILITY) < 75 && hasFailed(player)
            val end = scenery.location.transform(if (scenery.id != 10868) dir else dir.opposite, if (fail) 2 else 5)
            AgilityHandler.walk(
                player,
                if (fail) -1 else 1,
                player.location,
                end,
                Animation.create(155),
                if (fail) 0.0 else 56.4,
                if (fail) null else "You walk carefully across the slippery plank...",
            )
            playAudio(player, Sounds.PLANKWALK_2480, 0, 3)
            if (fail) {
                Pulser.submit(
                    object : Pulse(2, player) {
                        override fun pulse(): Boolean {
                            val dest = transformLevel(end.transform(if (!custom) 2 else 0, if (custom) -2 else 0, 0))
                            playAudio(player, Sounds.FALL_LAND_2455, 10, 50)
                            AgilityHandler.failWalk(player, 2, end, dest, dest, Animation.create(764), 10, 10, null)
                            return true
                        }
                    },
                )
            }
        }

        private fun handleJumpGap(
            player: Player,
            scenery: Scenery,
        ) {
            val dir = Direction.getDirection(player.location, getGapLocation(player, scenery))
            val fail = player.getSkills().getLevel(Skills.AGILITY) < 75 && hasFailed(player)
            player.packetDispatch.sendMessage("You jump the gap...")
            if (fail) {
                val regular = scenery.rotation == 0 || scenery.rotation == 3
                val end = player.location.transform(dir, 1)
                var dest = scenery.location.transform(dir, 1)
                dest =
                    if (!regular) {
                        dest.transform(
                            if (dir == Direction.NORTH || dir == Direction.SOUTH) 2 else 0,
                            if (dir == Direction.SOUTH) 1 else 0,
                            0,
                        )
                    } else {
                        dest.transform(
                            if (dir == Direction.NORTH || dir == Direction.SOUTH) {
                                -1
                            } else if (dir == Direction.WEST) {
                                1
                            } else {
                                0
                            },
                            if (dir == Direction.SOUTH) {
                                1
                            } else if (dir == Direction.WEST || dir == Direction.EAST) {
                                -1
                            } else {
                                0
                            },
                            0,
                        )
                    }
                dest = transformLevel(dest)
                player.lock(8)
                playAudio(player, Sounds.JUMP_NO_LAND_2467, 30)
                playAudio(player, Sounds.FALL_LAND_2455, 200)
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    player.location,
                    end,
                    Animation.create(3068),
                    10,
                    0.0,
                    "... and miss your footing.",
                )
                AgilityHandler.fail(player, 8, dest, Animation.create(3068), 8, null)
                return
            }
            playAudio(player, Sounds.JUMP2_2462, 30)
            player.lock(4)
            AgilityHandler.forceWalk(
                player,
                2,
                player.location,
                player.location.transform(dir, 3),
                Animation.create(3067),
                20,
                22.0,
                null,
            )
        }

        private fun handleGapCross(
            player: Player,
            scenery: Scenery,
        ) {
            val dir = Direction.getLogicalDirection(player.location, getGapCrossLocation(player, scenery))
            val fail = player.getSkills().getLevel(Skills.AGILITY) < 75 && hasFailed(player)
            val end = player.location.transform(dir, if (fail) 4 else 5)
            val rot = scenery.rotation
            val mod =
                if (player.location == Location(3359, 2849, 2)) {
                    0
                } else if (player.location ==
                    Location(
                        3357,
                        2841,
                        2,
                    )
                ) {
                    1
                } else if (player.location == Location(3367, 2832, 1)) {
                    1
                } else if (player.location ==
                    Location(
                        3372,
                        2832,
                        1,
                    )
                ) {
                    0
                } else if (scenery.location ==
                    Location(
                        3370,
                        2831,
                        1,
                    )
                ) {
                    0
                } else if (rot == 1 &&
                    dir == Direction.EAST
                ) {
                    0
                } else if (rot == 3 &&
                    (dir == Direction.WEST || dir == Direction.EAST)
                ) {
                    1
                } else if (rot == 0 &&
                    dir == Direction.SOUTH
                ) {
                    1
                } else if (dir == Direction.WEST &&
                    rot != 3 ||
                    dir == Direction.EAST
                ) {
                    1
                } else {
                    0
                }
            val animation = Animation.create(387 - mod)
            playAudio(player, Sounds.HANDHOLDS_GRAB_TO_SECOND_2450)
            playAudio(player, Sounds.FALL_LAND_2455, 170)
            if (fail) {
                var dest = scenery.location.transform(dir, 1)
                dest =
                    if (rot == 1 && dir == Direction.EAST) {
                        dest.transform(
                            1,
                            2,
                            0,
                        )
                    } else if (rot == 1 && dir == Direction.WEST && player.location.y < 2841) {
                        dest.transform(
                            0,
                            -2,
                            0,
                        )
                    } else if (rot == 1 && dir == Direction.WEST) {
                        dest.transform(
                            0,
                            2,
                            0,
                        )
                    } else {
                        dest.transform(
                            if (dir == Direction.NORTH || dir == Direction.SOUTH) {
                                -1
                            } else if (dir == Direction.WEST) {
                                1
                            } else {
                                0
                            },
                            if (dir == Direction.SOUTH) {
                                1
                            } else if (dir == Direction.WEST || dir == Direction.EAST) {
                                -1
                            } else {
                                0
                            },
                            0,
                        )
                    }
                AgilityHandler.walk(player, -1, player.location, end, animation, 10.0, null)
                val finalDest = dest
                Pulser.submit(
                    object : Pulse(3, player) {
                        override fun pulse(): Boolean {
                            AgilityHandler.fail(
                                player,
                                3,
                                transformLevel(finalDest),
                                Animation.create(3056 - mod),
                                8,
                                null,
                            )
                            player.appearance.setDefaultAnimations()
                            player.appearance.sync()
                            return true
                        }
                    },
                )
                return
            }
            AgilityHandler.walk(player, 4, player.location, end, animation, 22.0, null)
        }

        private fun handlePyramidTop(
            player: Player,
            scenery: Scenery,
        ) {
            if (player.getSavedData().activityData.isTopGrabbed) {
                player.packetDispatch.sendMessage("You've already claimed this!")
            } else {
                player.animate(Animation.create(3063))
                Pulser.submit(
                    object : Pulse(3, player) {
                        override fun pulse(): Boolean {
                            player.getSkills().addExperience(Skills.AGILITY, 1000.0)
                            if (getVarp(player, 640) == 505 || getVarp(player, 640) == 515) {
                                player.packetDispatch.sendMessage("You find nothing on top of the pyramid.")
                                return true
                            }
                            addConfig(player, 10869, 1, true)
                            player.getSavedData().activityData.isTopGrabbed = true
                            player.inventory.add(PYRAMID_TOP, player)
                            player.dialogueInterpreter.sendItemMessage(PYRAMID_TOP, "You find a golden pyramid!")
                            playJingle(player, 151)
                            return true
                        }
                    },
                )
            }
        }

        private fun getGapLocation(
            player: Player,
            scenery: Scenery,
        ): Location? {
            val data = getLocationData(GAP_LOCATIONS, scenery.location)
            return getClosest(arrayOf(data!![0], data[1]), player.location)
        }

        private fun getGapCrossLocation(
            player: Player,
            scenery: Scenery,
        ): Location? {
            val data = getLocationData(GAP_CROSS_LOCATIONS, scenery.location)
            return getClosest(arrayOf(data!![4], data[5]), player.location)
        }

        private fun getLedgeLocation(
            player: Player,
            scenery: Scenery,
        ): Location? {
            val data = getLocationData(LEDGE_LOCATIONS, scenery.location)
            return getClosest(arrayOf(data!![4], data[5]), player.location)
        }

        private fun getClosest(
            data: Array<Location>,
            location: Location,
        ): Location? {
            var closest: Location? = null
            for (l in data) {
                if (closest == null || l.getDistance(location) < closest.getDistance(location)) {
                    closest = l
                }
            }
            return closest
        }

        private fun getLocationData(
            data: Array<Array<Location>>,
            location: Location,
        ): Array<Location>? {
            for (datum in data) {
                for (l in datum) {
                    if (l == location) {
                        return datum
                    }
                }
            }
            return null
        }

        private fun hasFailed(
            player: Player,
            mod: Double = 0.009000,
        ): Boolean {
            return AgilityHandler.hasFailed(player, 10, mod)
        }

        override fun getDestination(
            node: Node,
            n: Node,
        ): Location? {
            when (n.id) {
                16535, 16536 -> return n.location.transform(if (node.location.x < n.location.x) -1 else 1, 0, 0)
                10865 -> return null
                10857 -> return n.location.transform(0, -1, 0)
                10858 -> return n.location.transform(0, 2, 0)
                10859 -> return getClosest(getLocationData(GAP_LOCATIONS, n.location)!!, node.location)
                10860, 10886, 10887, 10888, 10889 -> return getClosest(
                    getLocationData(LEDGE_LOCATIONS, n.location)!!,
                    node.location,
                )

                10868, 10867 -> return n.location
                10882, 10883, 10884, 10885, 10861, 10862, 10863, 10864 -> {
                    if (n.id == 10863 && n.location == Location(3372, 2832, 1)) {
                        return Location.create(3372, 2832, 1)
                    }
                    return getClosest(getLocationData(GAP_CROSS_LOCATIONS, n.location)!!, node.location)
                }
            }
            return null
        }

        override fun createInstance(player: Player): AgilityCourse {
            return AgilityPyramidCourse(player)
        }

        companion object {
            private val PYRAMID_TOP = Item(6970)

            private const val CONFIG_ID = 640

            private val GAP_LOCATIONS =
                arrayOf(
                    arrayOf(
                        Location(3356, 2847, 2),
                        Location.create(3357, 2847, 2),
                        Location.create(3356, 2846, 2),
                        Location.create(3357, 2846, 2),
                        Location.create(3356, 2849, 2),
                        Location.create(3357, 2849, 2),
                    ),
                    arrayOf(
                        Location(3364, 2833, 2),
                        Location.create(3364, 2834, 2),
                        Location.create(3366, 2834, 2),
                        Location.create(3366, 2833, 2),
                        Location.create(3363, 2834, 2),
                        Location.create(3363, 2833, 2),
                    ),
                    arrayOf(
                        Location(3370, 2841, 3),
                        Location(3371, 2841, 3),
                        Location.create(3370, 2840, 3),
                        Location.create(3371, 2840, 3),
                        Location.create(3370, 2843, 3),
                        Location.create(3371, 2843, 3),
                    ),
                    arrayOf(
                        Location(3040, 4697, 2),
                        Location.create(3041, 4697, 2),
                        Location.create(3041, 4696, 2),
                        Location.create(3040, 4696, 2),
                        Location.create(3041, 4699, 2),
                        Location.create(3040, 4699, 2),
                    ),
                    arrayOf(
                        Location(3048, 4695, 2),
                        Location.create(3049, 4695, 2),
                        Location.create(3048, 4694, 2),
                        Location.create(3049, 4694, 2),
                        Location.create(3048, 4697, 2),
                        Location.create(3049, 4697, 2),
                    ),
                    arrayOf(
                        Location.create(3046, 4697, 3),
                        Location.create(3047, 4697, 3),
                        Location.create(3046, 4696, 3),
                        Location.create(3047, 4696, 3),
                        Location.create(3046, 4699, 3),
                        Location.create(3047, 4699, 3),
                    ),
                )

            private val LEDGE_LOCATIONS =
                arrayOf(
                    arrayOf(
                        Location(3366, 2851, 1),
                        Location(3364, 2851, 1),
                        Location.create(3368, 2851, 1),
                        Location.create(3363, 2851, 1),
                        Location.create(3367, 2851, 1),
                        Location.create(3364, 2851, 1),
                    ),
                    arrayOf(
                        Location(3362, 2831, 1),
                        Location(3360, 2831, 1),
                        Location.create(3364, 2832, 1),
                        Location.create(3359, 2832, 1),
                        Location.create(3360, 2832, 1),
                        Location.create(3363, 2832, 1),
                    ),
                    arrayOf(
                        Location(3372, 2837, 2),
                        Location(3372, 2839, 2),
                        Location.create(3372, 2841, 2),
                        Location.create(3372, 2836, 2),
                        Location.create(3372, 2837, 2),
                        Location.create(3372, 2840, 2),
                    ),
                    arrayOf(
                        Location(3358, 2843, 3),
                        Location(3358, 2845, 3),
                        Location.create(3359, 2842, 3),
                        Location.create(3359, 2847, 3),
                        Location.create(3359, 2846, 3),
                        Location.create(3359, 2843, 3),
                    ),
                )

            private val GAP_CROSS_LOCATIONS =
                arrayOf(
                    arrayOf(
                        Location(3368, 2831, 1),
                        Location(3370, 2831, 1),
                        Location.create(3372, 2832, 1),
                        Location.create(3367, 2832, 1),
                        Location.create(3371, 2832, 1),
                        Location.create(3368, 2832, 1),
                    ),
                    arrayOf(
                        Location(3356, 2837, 2),
                        Location(3356, 2839, 2),
                        Location.create(3357, 2841, 2),
                        Location.create(3357, 2836, 2),
                        Location.create(3357, 2840, 2),
                        Location.create(3357, 2837, 2),
                    ),
                    arrayOf(
                        Location(3360, 2849, 2),
                        Location(3362, 2849, 2),
                        Location.create(3359, 2849, 2),
                        Location.create(3364, 2849, 2),
                        Location.create(3363, 2849, 2),
                        Location.create(3360, 2849, 2),
                    ),
                )

            @JvmStatic
            fun transformLevel(location: Location): Location {
                val xDiff = 320
                val yDiff = 1856
                if (location.regionId == 12105 && location.z == 2) {
                    return Location(location.x + xDiff, location.y - yDiff, 3)
                }
                return location.transform(0, 0, -1)
            }

            @JvmStatic
            fun addConfig(
                player: Player,
                objectId: Int,
                value: Int,
                save: Boolean,
            ) {
                val definition = VarbitDefinition.forSceneryId(SceneryDefinition.forId(objectId).varbitID)
                val oldVal = (definition.getValue(player) shl definition.startBit)
                val newVal = (value shl definition.startBit)
                setVarp(player, CONFIG_ID, (getVarp(player, CONFIG_ID) - oldVal) + newVal, save)
            }

            @JvmStatic
            fun addConfig(
                player: Player,
                scenery: Scenery,
                value: Int,
                save: Boolean,
            ) {
                addConfig(player, scenery.id, value, save)
            }
        }
    }

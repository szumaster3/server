package content.region.tirannwn.quest.roving_elves.handlers

import content.global.skill.agility.AgilityHandler
import core.api.quest.hasRequirement
import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Scenery

class RovingElvesObstacles : OptionHandler() {
    private val LEAF_SUCCESS_MSG = "You safely jump across."
    private val LEAF_LADDER_MSG = "You climb out of the pit."
    private val STICK_SUCCESS_MSG = "You manage to skillfully pass the trap."
    private val WIRE_SUCCESS_MSG = "You successfully step over the tripwire."

    private val OVER = Animation(839)
    private val THROUGH = Animation(1237)
    private val STICK_TRAP = Animation(Animations.HUMAN_WALK_SHORT_819)
    private val LEAF_TRAP = Animation(Animations.BA_PRESSURE_1115)
    private val WIRE_TRAP = Animation(Animations.CROSS_TRIPWIRE_1236)

    private val STICK_TRAP_NORTH = Location(0, 2, 0)
    private val STICK_TRAP_SOUTH = Location(0, -1, 0)
    private val STICK_TRAP_EAST = Location(2, 0, 0)
    private val STICK_TRAP_WEST = Location(-1, 0, 0)

    private val WIRE_TRAP_NORTH = Location(0, 2, 0)
    private val WIRE_TRAP_SOUTH = Location(0, -1, 0)
    private val WIRE_TRAP_EAST = Location(2, 0, 0)
    private val WIRE_TRAP_WEST = Location(-1, 0, 0)

    private val FOREST_NORTH = Location(0, 2, 0)
    private val FOREST_SOUTH = Location(0, -1, 0)
    private val FOREST_EAST = Location(2, 0, 0)
    private val FOREST_WEST = Location(-1, 0, 0)
    private val LEAF_TRAP_CLIMB = Location(2274, 3172, 0)
    private val illegalJump = listOf(3174)

    private fun nodeCenter(node: Node): Location {
        if (node.asScenery().rotation % 2 == 0) return node.location.transform(1, 0, 0)
        return node.location.transform(0, 1, 0)
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(Scenery.STICKS_3922).handlers["option:pass"] = this
        SceneryDefinition.forId(Scenery.DENSE_FOREST_3999).handlers["option:enter"] = this
        SceneryDefinition.forId(Scenery.DENSE_FOREST_3939).handlers["option:enter"] = this
        SceneryDefinition.forId(Scenery.TRIPWIRE_3921).handlers["option:step-over"] = this
        SceneryDefinition.forId(Scenery.DENSE_FOREST_3998).handlers["option:enter"] = this
        SceneryDefinition.forId(Scenery.DENSE_FOREST_3938).handlers["option:enter"] = this
        SceneryDefinition.forId(Scenery.DENSE_FOREST_3937).handlers["option:enter"] = this
        SceneryDefinition.forId(Scenery.LEAVES_3924).handlers["option:jump"] = this
        SceneryDefinition.forId(Scenery.LEAVES_3925).handlers["option:jump"] = this
        SceneryDefinition.forId(Scenery.TREE_8742).handlers["option:pass"] = this
        SceneryDefinition.forId(Scenery.PROTRUDING_ROCKS_3927).handlers["option:climb"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        player.lock(5)
        player.faceLocation(node.asScenery().location)

        var isNorthOrSouth = node.asScenery().rotation % 2 == 0

        if (node.id == 3922) {
            isNorthOrSouth = !isNorthOrSouth
        }

        val NORTH_SOUTH = if (player.location.y <= node.location.y) Direction.NORTH else Direction.SOUTH
        val EAST_WEST = if (player.location.x <= node.location.x) Direction.EAST else Direction.WEST

        when (node.id) {
            8742 -> {
                if (!hasRequirement(player, "Mourning's End Part I")) return true
                player.teleport(player.location.transform(EAST_WEST, 2))
            }

            3999 ->
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    player.location,
                    player.location.transform(NORTH_SOUTH, 3),
                    OVER,
                    25,
                    0.0,
                    null,
                )

            3921 ->
                if (isNorthOrSouth) {
                    if (NORTH_SOUTH ==
                        Direction.NORTH
                    ) {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            node.location.transform(WIRE_TRAP_SOUTH),
                            node.location.transform(WIRE_TRAP_NORTH),
                            WIRE_TRAP,
                            100,
                            0.0,
                            WIRE_SUCCESS_MSG,
                        )
                    } else {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            node.location.transform(WIRE_TRAP_NORTH),
                            node.location.transform(WIRE_TRAP_SOUTH),
                            WIRE_TRAP,
                            100,
                            0.0,
                            WIRE_SUCCESS_MSG,
                        )
                    }
                } else {
                    if (EAST_WEST ==
                        Direction.EAST
                    ) {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            node.location.transform(WIRE_TRAP_WEST),
                            node.location.transform(WIRE_TRAP_EAST),
                            WIRE_TRAP,
                            100,
                            0.0,
                            WIRE_SUCCESS_MSG,
                        )
                    } else {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            node.location.transform(WIRE_TRAP_EAST),
                            node.location.transform(WIRE_TRAP_WEST),
                            WIRE_TRAP,
                            100,
                            0.0,
                            WIRE_SUCCESS_MSG,
                        )
                    }
                }

            3922 ->
                if (isNorthOrSouth) {
                    if (NORTH_SOUTH ==
                        Direction.NORTH
                    ) {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            node.location,
                            node.location.transform(STICK_TRAP_NORTH),
                            STICK_TRAP,
                            25,
                            0.0,
                            STICK_SUCCESS_MSG,
                        )
                    } else {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            node.location,
                            node.location.transform(STICK_TRAP_SOUTH),
                            STICK_TRAP,
                            25,
                            0.0,
                            STICK_SUCCESS_MSG,
                        )
                    }
                } else {
                    if (EAST_WEST ==
                        Direction.EAST
                    ) {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            node.location.transform(STICK_TRAP_WEST),
                            node.location.transform(STICK_TRAP_EAST),
                            STICK_TRAP,
                            25,
                            0.0,
                            STICK_SUCCESS_MSG,
                        )
                    } else {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            node.location.transform(STICK_TRAP_EAST),
                            node.location.transform(STICK_TRAP_WEST),
                            STICK_TRAP,
                            25,
                            0.0,
                            STICK_SUCCESS_MSG,
                        )
                    }
                }

            3998, 3939, 3938 ->
                if (isNorthOrSouth) {
                    if (NORTH_SOUTH ==
                        Direction.NORTH
                    ) {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            nodeCenter(node).transform(FOREST_SOUTH),
                            nodeCenter(node).transform(FOREST_NORTH),
                            THROUGH,
                            25,
                            0.0,
                            null,
                        )
                    } else {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            nodeCenter(node).transform(FOREST_NORTH),
                            nodeCenter(node).transform(FOREST_SOUTH),
                            THROUGH,
                            25,
                            0.0,
                            null,
                        )
                    }
                } else {
                    if (EAST_WEST ==
                        Direction.EAST
                    ) {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            nodeCenter(node).transform(FOREST_WEST),
                            nodeCenter(node).transform(FOREST_EAST),
                            THROUGH,
                            25,
                            0.0,
                            null,
                        )
                    } else {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            nodeCenter(node).transform(FOREST_EAST),
                            nodeCenter(node).transform(FOREST_WEST),
                            THROUGH,
                            25,
                            0.0,
                            null,
                        )
                    }
                }

            3937 ->
                if (isNorthOrSouth) {
                    if (NORTH_SOUTH ==
                        Direction.NORTH
                    ) {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            nodeCenter(node).transform(FOREST_SOUTH),
                            nodeCenter(node).transform(FOREST_NORTH),
                            OVER,
                            25,
                            0.0,
                            null,
                        )
                    } else {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            nodeCenter(node).transform(FOREST_NORTH),
                            nodeCenter(node).transform(FOREST_SOUTH),
                            OVER,
                            25,
                            0.0,
                            null,
                        )
                    }
                } else {
                    if (EAST_WEST ==
                        Direction.EAST
                    ) {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            nodeCenter(node).transform(FOREST_WEST),
                            nodeCenter(node).transform(FOREST_EAST),
                            OVER,
                            25,
                            0.0,
                            null,
                        )
                    } else {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            nodeCenter(node).transform(FOREST_EAST),
                            nodeCenter(node).transform(FOREST_WEST),
                            OVER,
                            25,
                            0.0,
                            null,
                        )
                    }
                }

            3924 ->
                if (!illegalJump.contains(player.location.y)) {
                    if (isNorthOrSouth) {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            node.location.transform(NORTH_SOUTH, -1),
                            node.location.transform(NORTH_SOUTH, 2),
                            LEAF_TRAP,
                            25,
                            0.0,
                            LEAF_SUCCESS_MSG,
                        )
                    } else {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            node.location.transform(EAST_WEST, -2),
                            node.location.transform(EAST_WEST, 2),
                            LEAF_TRAP,
                            25,
                            0.0,
                            LEAF_SUCCESS_MSG,
                        )
                    }
                }

            3925 ->
                if (!illegalJump.contains(node.location.y)) {
                    if (!isNorthOrSouth) {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            node.location.transform(NORTH_SOUTH, -1),
                            node.location.transform(NORTH_SOUTH, 3),
                            LEAF_TRAP,
                            25,
                            0.0,
                            LEAF_SUCCESS_MSG,
                        )
                    } else {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            nodeCenter(node).transform(EAST_WEST, -1),
                            node.location.transform(EAST_WEST, 3),
                            LEAF_TRAP,
                            25,
                            0.0,
                            LEAF_SUCCESS_MSG,
                        )
                    }
                }

            3927 -> {
                player.teleport(LEAF_TRAP_CLIMB)
                sendMessage(player, LEAF_LADDER_MSG)
            }
        }
        return true
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        when (node.id) {
            3999 -> return Location(2188, 3162)
            3998 -> return Location(2188, 3171)
        }
        return null
    }

    override fun isWalk(
        player: Player,
        node: Node,
    ): Boolean {
        return node !is Item
    }

    override fun isWalk(): Boolean {
        return false
    }
}

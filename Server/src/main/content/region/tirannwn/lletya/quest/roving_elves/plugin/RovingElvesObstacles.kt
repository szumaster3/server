package content.region.tirannwn.lletya.quest.roving_elves.plugin

import core.api.forceMove
import core.api.hasRequirement
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
import shared.consts.Animations
import shared.consts.Quests
import shared.consts.Scenery

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

    private val LEAF_TRAP_CLIMB = Location(2274, 3172, 0)
    private val illegalJump = listOf(3174)

    private fun nodeCenter(node: Node): Location =
        if (node.asScenery().rotation % 2 == 0) node.location.transform(1, 0, 0)
        else node.location.transform(0, 1, 0)

    override fun newInstance(arg: Any?): Plugin<Any> {
        val mappings = mapOf(
            Scenery.STICKS_3922 to "option:pass",
            Scenery.DENSE_FOREST_3999 to "option:enter",
            Scenery.DENSE_FOREST_3939 to "option:enter",
            Scenery.TRIPWIRE_3921 to "option:step-over",
            Scenery.DENSE_FOREST_3998 to "option:enter",
            Scenery.DENSE_FOREST_3938 to "option:enter",
            Scenery.DENSE_FOREST_3937 to "option:enter",
            Scenery.LEAVES_3924 to "option:jump",
            Scenery.LEAVES_3925 to "option:jump",
            Scenery.TREE_8742 to "option:pass",
            Scenery.PROTRUDING_ROCKS_3927 to "option:climb"
        )
        mappings.forEach { (id, option) ->
            SceneryDefinition.forId(id).handlers[option] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        player.lock(5)
        player.faceLocation(node.asScenery().location)

        val nodeLoc = node.location
        val playerLoc = player.location
        val dir = Direction.getDirection(playerLoc, nodeLoc)
        var dest: Location? = null
        var anim: Animation? = null
        var msg: String? = null

        when (node.id) {
            Scenery.TREE_8742 -> {
                if (!hasRequirement(player, Quests.MOURNINGS_END_PART_I)) return true
                dest = playerLoc.transform(dir, 2)
            }

            Scenery.DENSE_FOREST_3999, Scenery.DENSE_FOREST_3998, Scenery.DENSE_FOREST_3939, Scenery.DENSE_FOREST_3938, Scenery.DENSE_FOREST_3937 -> {
                val distance = when (node.id) {
                    3999, 3937 -> 3
                    else -> 2
                }
                dest = nodeCenter(node).transform(dir, distance)
                anim = if (node.id == Scenery.DENSE_FOREST_3937) OVER else THROUGH
            }

            Scenery.TRIPWIRE_3921 -> {
                dest = nodeLoc.transform(dir, 2)
                anim = WIRE_TRAP
                msg = WIRE_SUCCESS_MSG
            }

            Scenery.STICKS_3922 -> {
                dest = nodeLoc.transform(dir, 2)
                anim = STICK_TRAP
                msg = STICK_SUCCESS_MSG
            }

            Scenery.LEAVES_3924, Scenery.LEAVES_3925 -> { // Leaves
                if (!illegalJump.contains(playerLoc.y)) {
                    dest = nodeLoc.transform(dir, 3)
                    anim = LEAF_TRAP
                    msg = LEAF_SUCCESS_MSG
                }
            }

            Scenery.PROTRUDING_ROCKS_3927 -> {
                dest = LEAF_TRAP_CLIMB
                msg = LEAF_LADDER_MSG
            }
        }


        if (dest != null && anim != null) {
            forceMove(player, playerLoc, dest, 0, 60, null, anim.id) {
                msg?.let { sendMessage(player, it) }
            }
        }

        return true
    }

    override fun getDestination(node: Node, n: Node): Location? = when (node.id) {
        3999 -> Location(2188, 3162)
        3998 -> Location(2188, 3171)
        else -> null
    }

    override fun isWalk(player: Player, node: Node): Boolean = node !is Item
    override fun isWalk(): Boolean = false
}
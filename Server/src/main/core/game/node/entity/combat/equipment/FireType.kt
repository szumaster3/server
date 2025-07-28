package core.game.node.entity.combat.equipment

import core.api.applyPoison
import core.api.registerTimer
import core.api.spawnTimer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.player.Player
import core.game.system.task.NodeTask
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Graphics

/**
 * The fire types.
 *
 * @author Emperor
 */
enum class FireType(
    val animation: Animation,
    val projectileId: Int,
    val task: NodeTask,
) {
    FIERY_BREATH(
        Animation(Animations.DRAGON_BREATH_81, Priority.HIGH),
        Graphics.KBD_FIERY_PROJECTILE_393,
        object : NodeTask(0) {
            override fun exec(
                node: Node,
                vararg n: Node,
            ): Boolean = true
        },
    ),

    SHOCKING_BREATH(
        Animation(84, Priority.HIGH),
        Graphics.KBD_SHOCKING_PROJECTILE_396,
        object : NodeTask(0) {
            override fun exec(
                node: Node,
                vararg n: Node,
            ): Boolean {
                if (RandomFunction.random(10) < 3) {
                    (node as Entity).getSkills().updateLevel(RandomFunction.random(3), -5, 0)
                    if (node is Player) {
                        node.packetDispatch.sendMessage("You have been shocked.")
                    }
                }
                return true
            }
        },
    ),

    TOXIC_BREATH(
        Animation(82, Priority.HIGH),
        Graphics.KBD_TOXIC_PROJECTILE_394,
        object : NodeTask(0) {
            override fun exec(
                node: Node,
                vararg n: Node,
            ): Boolean {
                applyPoison((node as Entity), (n[0] as Entity), 40)
                return true
            }
        },
    ),

    ICY_BREATH(
        Animation(83, Priority.HIGH),
        Graphics.KBD_ICY_PROJECTILE_395,
        object : NodeTask(0) {
            override fun exec(
                node: Node,
                vararg n: Node,
            ): Boolean {
                registerTimer((node as Entity), spawnTimer("frozen", 7, true))
                return true
            }
        },
    ),
}

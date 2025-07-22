package content.global.skill.construction.decoration

import content.global.skill.construction.Decoration
import core.api.forceMove
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class ChairPlugin : OptionHandler() {
    companion object {
        private val CHAIRS =
            arrayOf(
                arrayOf(Decoration.CRUDE_CHAIR, 4073, 4103),
                arrayOf(Decoration.WOODEN_CHAIR, 4075, 4103),
                arrayOf(Decoration.ROCKING_CHAIR, 4079, 4103),
                arrayOf(Decoration.OAK_CHAIR, 4081, 4103),
                arrayOf(Decoration.OAK_ARMCHAIR, 4083, 4103),
                arrayOf(Decoration.TEAK_ARMCHAIR, 4085, 4103),
                arrayOf(Decoration.MAHOGANY_ARMCHAIR, 4087, 4103),
                arrayOf(Decoration.BENCH_WOODEN, 4089, 4104),
                arrayOf(Decoration.BENCH_OAK, 4091, 4104),
                arrayOf(Decoration.BENCH_CARVED_OAK, 4093, 4104),
                arrayOf(Decoration.BENCH_TEAK, 4095, 4104),
                arrayOf(Decoration.BENCH_CARVED_TEAK, 4097, 4104),
                arrayOf(Decoration.BENCH_MAHOGANY, 4099, 4104),
                arrayOf(Decoration.BENCH_GILDED, 4101, 4104),
                arrayOf(Decoration.CARVED_TEAK_BENCH, 4097, 4104),
                arrayOf(Decoration.MAHOGANY_BENCH, 4099, 4104),
                arrayOf(Decoration.GILDED_BENCH, 4101, 4104),
                arrayOf(Decoration.OAK_THRONE, 4111, 4103),
                arrayOf(Decoration.TEAK_THRONE, 4112, 4103),
                arrayOf(Decoration.MAHOGANY_THRONE, 4113, 4103),
                arrayOf(Decoration.GILDED_THRONE, 4114, 4103),
                arrayOf(Decoration.SKELETON_THRONE, 4115, 4103),
                arrayOf(Decoration.CRYSTAL_THRONE, 4116, 4103),
                arrayOf(Decoration.DEMONIC_THRONE, 4117, 4103),
            )
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (data in CHAIRS) {
            SceneryDefinition.forId((data[0] as Decoration).objectId).handlers["option:sit-on"] = this
        }
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val scenery = node as Scenery
        var animId = -1
        var sitAnimId = -1
        for (data in CHAIRS) {
            if ((data[0] as Decoration).objectId == scenery.id) {
                animId = data[1] as Int
                sitAnimId = data[2] as Int
                break
            }
        }
        if (scenery.type == 11) {
            animId++
        }
        val animation = animId
        val sitAnimation = sitAnimId
        forceMove(player, player.location, node.location, 0, 30, scenery.direction, sitAnimation, null)
        player.locks.lockInteractions(600000)
        player.pulseManager.run(
            object : Pulse(2) {
                override fun pulse(): Boolean {
                    player.animate(Animation.create(animation))
                    return false
                }

                override fun stop() {
                    super.stop()
                    player.locks.unlockInteraction()
                    player.animate(Animation.create(sitAnimation + 2))
                }
            },
        )
        return true
    }
}

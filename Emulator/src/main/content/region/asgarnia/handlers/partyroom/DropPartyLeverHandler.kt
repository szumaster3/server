package content.region.asgarnia.handlers.partyroom

import core.api.animate
import core.api.face
import core.api.lock
import core.api.setAttribute
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.plugin.Plugin
import org.rs.consts.Animations

class DropPartyLeverHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(26194).handlers["option:pull"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val scenery = node as Scenery
        if (player.getAttribute("delay:lever", -1) > ticks) return true
        setAttribute(player, "delay:picking", ticks + 3)
        lock(player, 2)
        face(player, scenery)
        player.dialogueInterpreter.open(1 shl 16 or 2)
        Pulser.submit(
            object : Pulse(1, player) {
                override fun pulse(): Boolean {
                    animate(player, Animations.HUMAN_PARTY_ROOM_LEVER_6933)
                    return true
                }
            },
        )
        return true
    }
}

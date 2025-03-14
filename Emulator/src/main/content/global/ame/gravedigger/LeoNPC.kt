package content.global.ame.gravedigger

import content.data.RandomEvent
import content.global.ame.RandomEventNPC
import core.api.registerLogoutListener
import core.api.ui.setMinimapState
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.NPCs

class LeoNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.LEO_3508) {
    override fun init() {
        super.init()
        sendChat("Can I borrow you for a minute, ${player.username}?")
    }

    override fun tick() {
        super.tick()
        if (RandomFunction.random(1, 10) == 5) {
            sendChat("Can I borrow you for a minute, ${player.username}?")
            core.api.setAttribute(player, RandomEvent.save(), player.location)
            registerLogoutListener(player, RandomEvent.logout()) { p ->
                p.location = core.api.getAttribute(p, RandomEvent.save(), player.location)
            }
            setMinimapState(player, 2)
            player.properties.teleportLocation = Location.create(1928, 5002, 0)
            AntiMacro.terminateEventNpc(player)
        }
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(LeoDialogue(), npc)
    }
}

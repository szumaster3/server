package content.region.misthalin.handlers.lumbridge

import core.game.component.Component
import core.game.node.entity.player.Player
import org.rs.consts.Components

enum class GnomeCopterSign(
    private val button: String,
    vararg info: String,
) {
    ENTRANCE(
        "~ Gnomecopter Tours ~",
        "Welcome to Gnomecopter",
        "Tours: the unique flying",
        "experience!",
        "",
        "If you're a new flier, talk to",
        "<col=FF0000>Hieronymus</col> and prepare to be",
        "amazed by this triumph of",
        "gnomic engineering.",
        "",
        "",
        "Warning: all riders must be at",
        "least 2ft, 6ins tall.",
    ),
    ;

    private val info: Array<String> = info as Array<String>

    fun read(player: Player) {
        player.interfaceManager.openSingleTab(Component(Components.CARPET_INFO_723))
        player.packetDispatch.sendString(button, Components.CARPET_INFO_723, 9)
        var information = info[0]
        for (i in 1 until info.size) {
            information += "<br>" + info[i]
        }
        player.packetDispatch.sendString(information, Components.CARPET_INFO_723, 10)
    }
}

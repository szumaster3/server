package content.minigame.clanwars

import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.zone.*
import core.game.world.map.zone.impl.WildernessZone
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import java.util.*

@Initializable
class ClanWarsChallengeRoom :
    MapZone("clan wars cr", true, ZoneRestriction.RANDOM_EVENTS),
    Plugin<Any?> {
    override fun configure() {
        register(ZoneBorders(3264, 3672, 3279, 3695))
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            val p = e
            p.skullManager.isWildernessDisabled = true
            p.skullManager.isWilderness = false
            p.interfaceManager.closeOverlay()
            p.interaction.remove(Option._P_ASSIST)
            p.interaction.set(CWChallengeOptionHandler.OPTION)
        }
        return super.enter(e)
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (e is Player) {
            val p = e
            p.skullManager.isWildernessDisabled = false
            p.interaction.remove(CWChallengeOptionHandler.OPTION)
            p.interaction.set(Option._P_ASSIST)
            if (WildernessZone.isInZone(e)) {
                WildernessZone.show(p)
                p.skullManager.isWilderness = true
            }
        }
        return super.leave(e, logout)
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (target is Scenery) {
            val player = e as Player
            if (target.id == 28213) {
                if (player.communication.clan == null) {
                    player.packetDispatch.sendMessage("You have to be in a clan to enter this portal.")
                } else if (player.communication.clan.isDefault) {
                    player.sendMessage("You can't use the main clan chat for this.")
                    return true
                } else if (player.communication.clan.clanWar == null) {
                    player.packetDispatch.sendMessage("Your clan has to be in a war.")
                } else {
                    player.communication.clan.clanWar
                        .fireEvent("join", player)
                }
                return true
            }
        }
        return false
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        definePlugin(CWChallengeOptionHandler())
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }
}

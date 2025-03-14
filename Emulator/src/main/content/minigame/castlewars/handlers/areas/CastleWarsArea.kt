package content.minigame.castlewars.handlers.areas

import content.global.skill.summoning.familiar.BurdenBeast
import content.minigame.castlewars.handlers.CastleWars
import core.api.LogoutListener
import core.api.MapArea
import core.api.removeTimer
import core.api.sendMessage
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player

abstract class CastleWarsArea :
    MapArea,
    LogoutListener,
    InteractionListener {
    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        super.areaLeave(entity, logout)
        exitArea(entity as? Player ?: return)
    }

    override fun logout(player: Player) {
        if (!defineAreaBorders().any { it.insideBorder(player.location) }) {
            return
        }

        player.location = CastleWars.lobbyBankArea.randomWalkableLoc
        exitArea(player)
    }

    open fun exitArea(player: Player) {
        player.appearance.transformNPC(-1)

        player.interfaceManager.restoreTabs()

        player.walkingQueue.isRunDisabled = false

        if ((CastleWarsGameArea.areaBorders + CastleWarsWaitingArea.areaBorders).none {
                it.insideBorder(
                    player.location,
                )
            }
        ) {
            exitCastleWars(player)
        }
    }

    private fun exitCastleWars(player: Player) {
        player.interfaceManager.closeOverlay()

        removeTimer(player, "teleblock")

        val cwarsItems =
            intArrayOf(
                CastleWars.saradominTeamHoodedCloak,
                CastleWars.zamorakTeamHoodedCloak,
                CastleWars.saradominFlag,
                CastleWars.zamorakFlag,
            )

        player.equipment.removeAll(cwarsItems)
        player.inventory.removeAll(cwarsItems)
        (player.familiarManager.familiar as? BurdenBeast)?.container?.removeAll(cwarsItems)
    }

    override fun defineListeners() {
        onUnequip(intArrayOf(CastleWars.saradominTeamHoodedCloak, CastleWars.zamorakTeamHoodedCloak)) { player, _ ->
            defineAreaBorders().forEach { border ->
                if (border.insideBorder(player)) {
                    sendMessage(player, "You can't remove your team's colours")
                    return@onUnequip false
                }
            }
            return@onUnequip true
        }
    }
}

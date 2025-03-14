package content.minigame.castlewars.handlers.areas

import content.minigame.castlewars.handlers.CastleWars
import core.api.TickListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders

class CastleWarsRespawnArea :
    CastleWarsArea(),
    TickListener {
    companion object {
        val zamorakRespawnRoom: ZoneBorders =
            ZoneBorders(Location.create(2376, 3127, 1), Location.create(2368, 3135, 1))
        val saradominRespawnRoom: ZoneBorders =
            ZoneBorders(Location.create(2423, 3080, 1), Location.create(2431, 3072, 1))

        val zamorakPlayersInRespawnRoom = mutableSetOf<Player>()
        val saradominPlayersInRespawnRoom = mutableSetOf<Player>()
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(zamorakRespawnRoom, saradominRespawnRoom)
    }

    override fun areaEnter(entity: Entity) {
        val player = entity as? Player ?: return
        if (saradominRespawnRoom.insideBorder(player.location)) {
            player.equipment.replace(Item(CastleWars.saradominTeamHoodedCloak), 1)
        } else if (zamorakRespawnRoom.insideBorder(player.location)) {
            player.equipment.replace(Item(CastleWars.zamorakTeamHoodedCloak), 1)
        }
    }

    override fun tick() {
    }
}

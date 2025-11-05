package content.region.fremennik.plugin.dungeon

import core.api.MapArea
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import shared.consts.Music

class IceTrollDungeon : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders.forRegion(9632))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.CANNON)
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            if (!entity.musicPlayer.hasUnlocked(Music.OGRE_THE_TOP_224)) {
                entity.musicPlayer.unlock(Music.OGRE_THE_TOP_224)
            }
        }
    }
}
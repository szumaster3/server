package content.region.misthalin.varrock.quest.dslay.cutscene

import core.api.*
import core.game.activity.ActivityManager
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Items
import org.rs.consts.Quests

class DemonSlayerCutscene : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(3222, 3364, 3234, 3375))

    override fun areaEnter(entity: Entity) {
        if (entity !is Player) return

        val quest = entity.questRepository.getQuest(Quests.DEMON_SLAYER)
        val alreadyInCutscene = getAttribute(entity, "demon-slayer:cutscene", false)
        val hasSilverlight = inInventory(entity, Items.SILVERLIGHT_2402) || inEquipment(entity, Items.SILVERLIGHT_2402)

        if (quest.getStage(entity) == 30 && !alreadyInCutscene && hasSilverlight) {
            ActivityManager.start(entity, "Demon Slayer Cutscene", false)
            setAttribute(entity, "demon-slayer:cutscene", true)
        }
    }
}

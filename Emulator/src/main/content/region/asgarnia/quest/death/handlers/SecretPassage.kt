package content.region.asgarnia.quest.death.handlers

import core.api.MapArea
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendMessage
import core.api.sendPlayerDialogue
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Quests

class SecretPassage : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2866, 3609, 2866, 3609))
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player && getQuestStage(entity, Quests.DEATH_PLATEAU) == 25) {
            sendPlayerDialogue(
                entity,
                "I think this is far enough, I can see Death Plateau and it looks like the trolls haven't found the path. I'd better go and tell Denulth.",
            )
            sendMessage(entity, "You can see that there are no trolls on the secret path")
            sendMessage(entity, "You should go and speak to Denulth")
            setQuestStage(entity, Quests.DEATH_PLATEAU, 26)
        }
    }
}

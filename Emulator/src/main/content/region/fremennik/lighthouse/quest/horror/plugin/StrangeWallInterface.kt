package content.region.fremennik.lighthouse.quest.horror.plugin

import content.data.GameAttributes
import core.api.*
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import org.rs.consts.Components
import org.rs.consts.Quests
import org.rs.consts.Sounds

class StrangeWallInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.HORROR_METALDOOR_142) { player, _ ->
            val questComplete = isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)
            val runeAttributes =
                listOf(
                    GameAttributes.QUEST_HFTD_USE_FIRE_RUNE to 2,
                    GameAttributes.QUEST_HFTD_USE_AIR_RUNE to 3,
                    GameAttributes.QUEST_HFTD_USE_EARTH_RUNE to 4,
                    GameAttributes.QUEST_HFTD_USE_WATER_RUNE to 5,
                    GameAttributes.QUEST_HFTD_USE_ARROW to 6,
                    GameAttributes.QUEST_HFTD_USE_SWORD to 7,
                )

            runeAttributes.forEach { (attribute, componentIndex) ->
                setComponentVisibility(
                    player,
                    Components.HORROR_METALDOOR_142,
                    componentIndex,
                    getAttribute(player, attribute, 0) != 1,
                )
            }
            if (questComplete) return@onOpen true

            if (getAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_DOOR, 0) > 5) {
                closeInterface(player)
                queueScript(player, 1, QueueStrength.SOFT) {
                    sendMessage(player, "You hear the sound of something moving within the wall.")
                    playAudio(player, Sounds.STRANGEDOOR_SOUND_1627)
                    setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, 50)
                    return@queueScript stopExecuting(player)
                }
            }

            return@onOpen true
        }
    }
}

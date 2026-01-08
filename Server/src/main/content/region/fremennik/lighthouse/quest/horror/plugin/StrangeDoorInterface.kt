package content.region.fremennik.lighthouse.quest.horror.plugin

import content.data.GameAttributes
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import shared.consts.Components
import shared.consts.Quests
import shared.consts.Sounds

class StrangeDoorInterface : InterfaceListener {

    private val runeComponents = mapOf(
        GameAttributes.QUEST_HFTD_USE_FIRE_RUNE  to 2,
        GameAttributes.QUEST_HFTD_USE_AIR_RUNE   to 3,
        GameAttributes.QUEST_HFTD_USE_EARTH_RUNE to 4,
        GameAttributes.QUEST_HFTD_USE_WATER_RUNE to 5,
        GameAttributes.QUEST_HFTD_USE_ARROW      to 6,
        GameAttributes.QUEST_HFTD_USE_SWORD      to 7,
    )

    companion object {
        private const val UNLOCK_DOOR_THRESHOLD = 5
        private const val QUEST_STAGE_OPEN_DOOR = 50
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.HORROR_METALDOOR_142) { player, _ ->
            if (isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)) return@onOpen true

            runeComponents.forEach { (attribute, componentIndex) ->
                val used = getAttribute(player, attribute, 0) == 1
                setComponentVisibility(player, Components.HORROR_METALDOOR_142, componentIndex, !used)
            }

            if (getAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_DOOR, 0) > UNLOCK_DOOR_THRESHOLD) {
                closeInterface(player)
                sendMessage(player, "You hear the sound of something moving within the wall.")
                playAudio(player, Sounds.STRANGEDOOR_SOUND_1627, 1)
                setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, QUEST_STAGE_OPEN_DOOR)
            }
            return@onOpen true
        }
    }
}

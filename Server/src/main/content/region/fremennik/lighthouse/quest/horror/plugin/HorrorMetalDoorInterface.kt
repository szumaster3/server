package content.region.fremennik.lighthouse.quest.horror.plugin

import core.api.*
import core.api.isQuestComplete
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import shared.consts.Components
import shared.consts.Quests
import shared.consts.Sounds
import shared.consts.Vars

class HorrorMetalDoorInterface : InterfaceListener {

    override fun defineInterfaceListeners() {
        onOpen(Components.HORROR_METALDOOR_142) { player, _ ->
            val questComplete = isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)

            /*
             * Map runes/items to component indices
             */

            val vbit = listOf(
                Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_FIRE_RUNE_40 to 2,
                Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_AIR_RUNE_43 to 3,
                Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_EARTH_RUNE_42 to 4,
                Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_WATER_RUNE_41 to 5,
                Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_ARROW_45 to 6,
                Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_SWORD_44 to 7
            )

            /*
             * Set visibility of components based on varbit state
             */

            vbit.forEach { (varbit, componentIndex) ->
                setComponentVisibility(
                    player,
                    Components.HORROR_METALDOOR_142,
                    componentIndex,
                    getVarbit(player, varbit) == 0
                )
            }

            if (questComplete) return@onOpen true

            /*
             * Unlock door if varbit indicates progress
             */

            if (getVarbit(player, Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_UNLOCKED_35) > 0) {
                closeInterface(player)
                queueScript(player, 1, QueueStrength.SOFT) {
                    sendMessage(player, "You hear the sound of something moving within the wall.")
                    playAudio(player, Sounds.STRANGEDOOR_SOUND_1627)
                    setVarbit(player, Vars.VARBIT_QUEST_HORROR_FROM_THE_DEEP_PROGRESS_34, 50, true)
                    return@queueScript stopExecuting(player)
                }
            }

            return@onOpen true
        }
    }
}
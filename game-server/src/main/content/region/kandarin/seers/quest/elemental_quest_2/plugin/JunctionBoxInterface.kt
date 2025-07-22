package content.region.kandarin.seers.quest.elemental_quest_2.plugin

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.InterfaceListener
import org.rs.consts.Components

class JunctionBoxInterface : InterfaceListener {
    private val junctionBox = Components.JUNCTION_BOX_262

    override fun defineInterfaceListeners() {
        onOpen(junctionBox) { player, _ ->
            setVarbit(player, 4835, 3)
            return@onOpen true
        }

        on(junctionBox) { player, _, _, buttonID, _, _ ->
            val button1 = 24
            val button2 = 25
            val button3 = 20
            val button4 = 21
            val button5 = 22
            val button6 = 23

            if (buttonID == button2) {
                setAttribute(player, "ew2:press-2", true)
            }
            if (buttonID == button4) {
                setAttribute(player, "ew2:press-4", true)
            }
            if (buttonID == button1) {
                setAttribute(player, "ew2:press-1", true)
            }

            when (buttonID) {
                button5 ->
                    if (getAttribute(player, "ew2:press-4", false)) {
                        setVarbit(player, 4835, 0)
                        setComponentVisibility(player, junctionBox, 8, false)
                    }

                button3 ->
                    if (getAttribute(player, "ew2:press-2", false)) {
                        setVarbit(player, 4835, 1)
                        setComponentVisibility(player, junctionBox, 17, false)
                    }

                button6 ->
                    if (getAttribute(player, "ew2:press-1", false)) {
                        setVarbit(player, 4835, 2)
                        setComponentVisibility(player, junctionBox, 12, false)
                    }
            }

            if (getVarbit(player, 4835) == 0) {
                lock(player, 3)
                removeAttributes(player, "ew2:press-1", "ew2:press-2", "ew2:press-4")
                runTask(player, 2) {
                    closeInterface(player)
                    sendPlayerDialogue(player, "I hope I got that right.", FaceAnim.HAPPY)
                }
            }
            return@on true
        }
    }
}

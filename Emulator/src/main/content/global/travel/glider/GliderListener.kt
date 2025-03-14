package content.global.travel.glider

import core.api.*
import core.api.quest.isQuestComplete
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Quests

class GliderListener :
    InteractionListener,
    InterfaceListener {
    private val GNOME_PILOTS =
        intArrayOf(
            NPCs.CAPTAIN_DALBUR_3809,
            NPCs.CAPTAIN_BLEEMADGE_3810,
            NPCs.CAPTAIN_ERRDO_3811,
            NPCs.CAPTAIN_KLEMFOODLE_3812,
        )

    override fun defineListeners() {
        on(GNOME_PILOTS, IntType.NPC, "glider") { player, _ ->
            if (!isQuestComplete(player, Quests.THE_GRAND_TREE)) {
                sendMessage(player, "You must complete The Grand Tree Quest to access the gnome glider.")
            } else {
                openInterface(player, Components.GLIDERMAP_138)
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.GLIDERMAP_138) { player, _ ->
            setVarp(player, 153, 0)
            return@onOpen true
        }

        on(Components.GLIDERMAP_138) { player, _, _, buttonID, _, _ ->
            val glider = Glider.forId(buttonID) ?: return@on true
            when (buttonID) {
                glider.button -> submitWorldPulse(GliderPulse(1, player, glider))
            }
            return@on true
        }

        onClose(Components.GLIDERMAP_138) { player, _ ->
            unlock(player)
            return@onClose true
        }
    }
}

package content.global.travel.glider

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Quests

class GliderPlugin : InteractionListener, InterfaceListener {

    private val GNOME_PILOTS = intArrayOf(NPCs.CAPTAIN_DALBUR_3809, NPCs.CAPTAIN_BLEEMADGE_3810, NPCs.CAPTAIN_ERRDO_3811, NPCs.CAPTAIN_KLEMFOODLE_3812)
    private val GNOME_ID = intArrayOf(NPCs.GNORMADIUM_AVLAFRIM_1800, NPCs.CAPTAIN_DALBUR_3809, NPCs.CAPTAIN_BLEEMADGE_3810, NPCs.CAPTAIN_KLEMFOODLE_3812)

    override fun defineListeners() {

        /*
         * Handles option for glider transportation.
         */

        on(GNOME_PILOTS, IntType.NPC, "glider") { player, _ ->
            if (!isQuestComplete(player, Quests.THE_GRAND_TREE)) {
                sendMessage(player, "You must complete The Grand Tree Quest to access the gnome glider.")
            } else {
                openInterface(player, Components.GLIDERMAP_138)
            }
            return@on true
        }

        /*
         * Handles talking to gnomes.
         */

        on(GNOME_ID, IntType.NPC, "talk-to") { player, node ->
            dialogue(player) {
                npc(node.id, FaceAnim.OLD_DEFAULT, "What do you want human?")
                player(FaceAnim.HALF_GUILTY, "May you fly me somewhere on your glider?")
                if (!isQuestComplete(player, Quests.THE_GRAND_TREE)) {
                    npc(FaceAnim.OLD_ANGRY3, "I only fly friends of the gnomes!")
                } else {
                    npc(FaceAnim.OLD_DEFAULT, "If you wish.")
                    end {
                        openInterface(player, Components.GLIDERMAP_138)
                    }
                }
            }
            return@on true
        }

    }

    override fun defineInterfaceListeners() {

        /*
         * Handles glider interface.
         */

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

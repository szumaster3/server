package content.region.island.braindeath.plugin

import core.api.faceLocation
import core.api.findLocalNPC
import core.api.sendChat
import core.api.sendMessage
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class BraindeathIslandPlugin : InteractionListener {

    val ZOMBIE_SWAB_ID = intArrayOf(
        NPCs.ZOMBIE_SWAB_2843,
        NPCs.ZOMBIE_SWAB_2844,
        NPCs.ZOMBIE_SWAB_2845,
        NPCs.ZOMBIE_SWAB_2846,
        NPCs.ZOMBIE_SWAB_2847,
        NPCs.ZOMBIE_SWAB_2848
    )

    override fun defineListeners() {

        /*
         * Handles trying to open the bridge gate.
         */

        on(Scenery.GATE_10172, IntType.SCENERY, "open") { player, node ->
            val lukeNPC = findLocalNPC(player, NPCs.LUKE_50PERCENT_2828)
            faceLocation(lukeNPC!!, player.location)
            if(player.location.y > 5098) {
                dialogue(player) {
                    npc(NPCs.LUKE_50PERCENT_2828, FaceAnim.OLD_DEFAULT, "Hey! What are you doing out there?")
                    player("Nothing.")
                    npc(NPCs.LUKE_50PERCENT_2828, FaceAnim.OLD_DEFAULT, "Well Cap'n Donnie said no livin' landlubbers were", "allowed out of the compound.")
                    npc(NPCs.LUKE_50PERCENT_2828, FaceAnim.OLD_DEFAULT,  "So get yerself back in here, or yer for it!")
                    end {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    }
                }
                return@on true
            }

            val distractionDialogue = arrayOf(
                "Hey you! Look over there!",
                "Who's that making faces behind you?",
                "Oh my! Is that a genuine 3rd Age Diversion?",
                "Is that your distraction?",
                "Who is that behind you?",
                "That is the most amazing thing I have ever seen!"
            )

            val index = distractionDialogue.random()
            dialogue(player) {
                npc(NPCs.LUKE_50PERCENT_2828, FaceAnim.OLD_DEFAULT, "Arr! Tryin' ter get away eh? Well ye'll never sneak", "past me, I'm the best lookout this crew has ever seen!")
                player(index)
                end {
                    faceLocation(lukeNPC, Location(2120, 5095, 0))
                    sendChat(lukeNPC, "Where?", 1)
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                }
            }
            return@on true
        }

        /*
         * Handles talking to Zombie swab NPC.
         */

        on(ZOMBIE_SWAB_ID, IntType.NPC, "talk-to") {player, _ ->
            sendMessage(player, "I don't think he wants to talk to you.")
            return@on true
        }
    }
}
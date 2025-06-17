package content.region.island.braindeath.plugin

import core.api.faceLocation
import core.api.findLocalNPC
import core.api.sendChat
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Scenery
import kotlin.random.Random

class BraindeathIslandPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles trying to open the bridge gate.
         */

        on(Scenery.GATE_10172, IntType.SCENERY, "open") { player, node ->
            val lukeNPC = findLocalNPC(player, NPCs.LUKE_50PERCENT_2828)
            val distractionDialogue = arrayOf(
                "Hey you! Look over there!",
                "Who's that making faces behind you?",
                "Oh my! Is that a genuine 3rd Age Diversion?",
                "Is that your distraction?",
                "Who is that behind you?",
                "That is the most amazing thing I have ever seen!"
            )
            val index = Random.nextInt(distractionDialogue.size)
            faceLocation(lukeNPC!!, player.location)
            dialogue(player) {
                npc(NPCs.LUKE_50PERCENT_2828, "Arr! Tryin' ter get away eh? Well ye'll never sneak", "past me, I'm the best lookout this crew has ever seen!")
                player(distractionDialogue[index])
                end {
                    faceLocation(lukeNPC, Location(2120, 5095, 0))
                    sendChat(lukeNPC, "Where?", 1)
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                }
            }
            return@on true
        }
    }
}
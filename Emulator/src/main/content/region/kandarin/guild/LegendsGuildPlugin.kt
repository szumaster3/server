package content.region.kandarin.guild

import core.api.*
import core.api.interaction.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class LegendsGuildPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles opening the main Legends Guild doors.
         */

        on(2896, IntType.SCENERY, "open") { player, node ->
            handleMainDoorOpen(player, node.asScenery())
            return@on true
        }

        on(2897, IntType.SCENERY, "open") { player, node ->
            handleMainDoorOpen(player, node.asScenery())
            return@on true
        }

        /*
         * Handles opening the Legends Guild gates.
         */

        on(2391, IntType.SCENERY, "open") { player, node ->
            handleGateOpen(player, node.asScenery())
            return@on true
        }

        on(2392, IntType.SCENERY, "open") { player, node ->
            handleGateOpen(player, node.asScenery())
            return@on true
        }

        /*
         * Handles climbing the staircase in Legends Guild.
         */

        on(32048, IntType.SCENERY, "climb-up") { player, _ ->
            player.teleport(Location(2723, 3375, 0))
            return@on true
        }

        /*
         * Handles talking to npcs inside guild.
         */

        on(NPCs.RADIMUS_ERKLE_400, IntType.SCENERY, "climb-up") { player, node ->
            sendNPCDialogue(player, node.id, "Excuse me a moment won't you. Do feel free to explore the rest of the building.", FaceAnim.HALF_GUILTY)
            sendMessage(player, "Radimus looks busy...")
            setVarbit(player, 5511, 2, true)
            return@on true
        }

        on(NPCs.SIEGFRIED_ERKLE_933, IntType.SCENERY, "climb-up") { player, node ->
            openDialogue(player, SiegfriedDialogue(), node)
            return@on true
        }


    }

    private fun handleGateOpen(player: Player, node: Scenery) {
        DoorActionHandler.handleAutowalkDoor(player, node)
        sendMessage(player, "The guards salute you as you walk past.")
        findLocalNPC(player, NPCs.LEGENDS_GUARD_398)?.let { guard ->
            sendChat(guard, "Legends' Guild member approaching!", 1)
        }
    }

    private fun handleMainDoorOpen(player: Player, node: Scenery) {
        DoorActionHandler.handleAutowalkDoor(player, node)
        sendMessage(player, "You push the huge Legends Guild doors open.")
        if (player.location.y < 3374) {
            sendMessage(player, "You approach the Legends Guild main doors.")
        }
    }
}

private class SiegfriedDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npcl(FaceAnim.HALF_GUILTY, "Hello there and welcome to the shop of useful items. Can I help you at all?").also { stage++ }
            1 -> options("Yes please. What are you selling?", "No thanks.", "Didn't you once sell Silverlight?").also { stage++ }
            2 -> when (buttonID) {
                1 -> player("Yes please. What are you selling?").also { stage = 5 }
                2 -> player("No thanks.").also { stage = 6 }
                3 -> player("Didn't you once sell Silverlight?").also { stage++ }
            }
            3 -> npcl(FaceAnim.SUSPICIOUS, "Silverlight? Oh, Sir Prysin of Varrock explained that was a unique sword and told us to stop selling it.").also { stage++ }
            4 -> npcl(FaceAnim.NEUTRAL, "If you want Silverlight, but don't have it, you should speak to him.").also { stage = END_DIALOGUE }
            5 -> end().also { openNpcShop(player!!, npc!!.id) }
            6 -> npcl(FaceAnim.NOD_YES, "Ok, well, if you change your mind, do pop back.").also { stage = END_DIALOGUE }
        }
    }
}
package content.region.kandarin.guild

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.plugin.ClassScanner
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class LegendsGuildPlugin : InteractionListener {

    override fun defineListeners() {
        ClassScanner.definePlugins(
            RadimusErkleDialogue(),
            SiegfriedErkleDialogue()
        )
        /*
         * Handles interaction with legends guild gates.
         */

        on(intArrayOf(Scenery.GATE_2391, Scenery.GATE_2392), IntType.SCENERY, "open") { player, node ->
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            sendMessage(player, "The guards salute you as you walk past.")
            val nearbyNPCs = RegionManager.getLocalNpcs(player, 8)
            val guards = nearbyNPCs.filter { it.id == NPCs.LEGENDS_GUARD_398 || it.id == NPCs.LEGENDS_GUARD_399 }
            guards.forEach { guard ->
                guard.faceTemporary(player, 1)
                guard.sendChat("Legends' Guild member approaching!", 1)
            }
            return@on true
        }

        /*
         * Handles interaction with legend guild doors.
         */

        on(
            intArrayOf(Scenery.LEGENDS_GUILD_DOOR_2896, Scenery.LEGENDS_GUILD_DOOR_2897),
            IntType.SCENERY, "open"
        ) { player, node ->
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            sendMessage(player, "You push the huge Legends Guild doors open.", null)
            if (player.location.y < 3374) {
                sendMessage(player, "You approach the Legends Guild main doors.", null)
            }
            return@on true
        }

        /*
         * Handles interaction with staircase.
         */

        on(Scenery.STAIRCASE_32048, IntType.SCENERY, "climb-up", "open") { player, _ ->
            teleport(player, Location.create(2723, 3375, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles interaction with cupboard.
         */

        on(Scenery.OPEN_CUPBOARD_2886, IntType.SCENERY, "search") { player, _ ->
            animate(player, 537)

            if (inInventory(player, Items.MACHETE_975)) {
                sendMessage(player, "You search the cupboard but find nothing interesting.")
                return@on true
            }

            sendItemDialogue(player, Items.MACHETE_975, "You find a machete in the cupborad.")

            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space to hold that item.")
                return@on true
            }

            sendMessage(player, "You find a machete.")
            addItem(player, Items.MACHETE_975)
            return@on true
        }
    }


    /**
     *  Represents the Siegfried Erkle dialogue.
     */
    inner class SiegfriedErkleDialogue(player: Player? = null) : Dialogue(player) {
        override fun open(vararg args: Any?): Boolean {
            npc = args[0] as NPC
            npcl(FaceAnim.HALF_GUILTY, "Hello there and welcome to the shop of useful items. Can I help you at all?")
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (stage) {
                0 -> options(
                    "Yes please. What are you selling?",
                    "No thanks.",
                    "Didn't you once sell Silverlight?"
                ).also { stage++ }

                1 -> when (buttonId) {
                    1 -> player("Yes please. What are you selling?").also { stage = 4 }
                    2 -> player("No thanks.").also { stage = 5 }
                    3 -> player("Didn't you once sell Silverlight?").also { stage++ }
                }

                2 -> npcl(
                    FaceAnim.SUSPICIOUS,
                    "Silverlight? Oh, Sir Prysin of Varrock explained that was a unique sword and told us to stop selling it."
                ).also { stage++ }

                3 -> npcl(
                    FaceAnim.NEUTRAL,
                    "If you want Silverlight, but don't have it, you should speak to him."
                ).also { stage = END_DIALOGUE }

                4 -> end().also { openNpcShop(player, npc.id) }
                5 -> npcl(FaceAnim.NOD_YES, "Ok, well, if you change your mind, do pop back.").also {
                    stage = END_DIALOGUE
                }
            }
            return true
        }

        override fun newInstance(player: Player?): Dialogue = SiegfriedErkleDialogue(player)

        override fun getIds(): IntArray = intArrayOf(NPCs.SIEGFRIED_ERKLE_933)
    }


    /**
     *  Represents the Radimus Erkle dialogue.
     */
    inner class RadimusErkleDialogue(player: Player? = null) : Dialogue(player) {

        override fun open(vararg args: Any?): Boolean {
            npc = args[0] as NPC
            npcl(
                FaceAnim.HALF_GUILTY,
                "Excuse me a moment won't you. Do feel free to explore the rest of the building."
            )
            stage = 0
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (stage) {
                0 -> {
                    end()
                    sendMessage(player, "Radimus looks busy...")
                    // setVarbit(player, 5511, 2, true)
                }
            }
            return true
        }

        override fun newInstance(player: Player?): Dialogue = RadimusErkleDialogue(player)

        override fun getIds(): IntArray = intArrayOf(NPCs.RADIMUS_ERKLE_400)
    }
}
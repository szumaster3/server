package content.region.morytania.phasmatys.quest.ahoy.dialogue

import content.region.morytania.phasmatys.quest.ahoy.plugin.GhostsAhoyUtils
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class GhostCaptainDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when {
            !inEquipment(player, Items.GHOSTSPEAK_AMULET_552) -> npc(FaceAnim.FRIENDLY, "Woooo wooo wooooo woooo")
            inBorders(player, 3788, 3556, 3797, 3562) ->
                options(
                    "Can you take me back to Phasmatys, now?",
                    "There isn't much on this island, is there?",
                ).also { stage = 7 }

            else -> player("Where do you sail to, in such a small boat?").also { stage = 1 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 ->
                if (inBorders(player, 3788, 3556, 3797, 3562)) {
                    sendDialogue(
                        player,
                        "The ghost sailor wails at you incomprehensibly, and points at the boat, nodding his ghostly head.",
                    ).also { stage = END_DIALOGUE }
                } else {
                    sendDialogue(player, "You cannot understand the ghost.").also { stage = END_DIALOGUE }
                }

            1 ->
                npc(
                    "I can take you to Dragontooth Island, which is a most",
                    "pleasant island in the sea between Morytania and the",
                    "lands of the east.",
                ).also { stage++ }

            2 -> player("How much does it cost?").also { stage++ }
            3 -> npc("It'll cost you 25 ectotokens for the return trip.").also { stage++ }
            4 ->
                if (amountInInventory(player, Items.ECTO_TOKEN_4278) >= 25) {
                    options(
                        "I have the ectotokens here - please take me to Dragontooth Island.",
                        "No thanks, not at the moment.",
                    ).also { stage++ }
                } else {
                    player("No thanks, not at the moment.").also { stage = END_DIALOGUE }
                }

            5 ->
                when (buttonId) {
                    1 ->
                        player(
                            "I have the ectotokens here - please take me",
                            "to Dragontooth Island.",
                        ).also { stage++ }
                    2 -> player("No thanks, not at the moment.").also { stage = END_DIALOGUE }
                }

            6 -> {
                end()
                if (!removeItem(player, Item(Items.ECTO_TOKEN_4278, 25), Container.INVENTORY)) {
                    player.debug("An attempt to remove an item failed. This should not happen.")
                } else {
                    GhostsAhoyUtils.travelToDragontoothIsland(player)
                }
            }

            7 ->
                when (buttonId) {
                    1 -> player("Can you take me back to Phasmatys, now?").also { stage++ }
                    2 -> player("There isn't much on this island, is there?").also { stage = 10 }
                }

            8 -> npc("Yes, climb in.").also { stage++ }

            9 -> {
                end()
                GhostsAhoyUtils.travelToPortPhasmatys(player)
            }

            10 -> npc("Beautiful, isn't it?").also { stage++ }
            11 -> player("If you say so.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GHOST_CAPTAIN_1704)
}

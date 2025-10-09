package content.global.travel.ship

import core.api.inInventory
import core.api.openNpcShop
import core.api.sendOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.StringUtils
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Charter dialogue.
 */
@Initializable
class TraderCrewmemberDialogue(player: Player? = null) : Dialogue(player) {

    private var destination: CharterShipUtils.Destination? = null
    private var cost = 0

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (args.size > 1) {
            destination = (args[1] as CharterShipUtils.Destination)
            cost = args[2] as Int
            core.api.sendDialogue(player, "To sail to " + StringUtils.formatDisplayName(destination!!.name) + " from here will cost you " + cost + " gold. Are you sure you want to pay that?")
            stage = 14
            return true
        }
        npc("Can I help you?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> sendOptions(player, "Choose an option:", "Yes, who are you?", "Yes, I would like to charter a ship.").also {
                stage++
            }
            1 -> when (buttonId) {
                1 -> player(FaceAnim.ASKING, "Yes, who are you?").also { stage++ }
                2 -> player(FaceAnim.FRIENDLY, "Yes, I would like to charter a ship.").also { stage = 12 }
            }
            2 -> npc(FaceAnim.FRIENDLY, "I'm one of the Trader Stan's crew; we are all part of the", "largest fleet of trading and sailing vessels to ever sail the", "seven seas.").also { stage++ }
            3 -> npc(FaceAnim.FRIENDLY, "If you want to get to a port in a hurry then you can", "charter one of our ships to take you there - if the price", "is right...").also { stage++ }
            4 -> player(FaceAnim.HALF_ASKING, "So, where exactly can I go with your ships?").also { stage++ }
            5 -> npc(FaceAnim.NEUTRAL, "We run ships from Port Phasmatys over to Port Tyras,", "stopping at Port Sarim, Catherby, Karamja,", "the Shipyard and Port Khazard.").also { stage++ }
            6 -> player(FaceAnim.FRIENDLY, "Wow, that's a lot of ports. I take it you have some exotic", "stuff to trade?").also { stage++ }
            7 -> npc(FaceAnim.HAPPY, "We certainly do! We have access to items", "bought and sold from around the world.").also { stage++ }
            8 -> npc(FaceAnim.HALF_ASKING, "Would you like to take a look?").also { stage++ }
            9 -> options("Yes.", "No.").also { stage++ }
            10 -> when (buttonId) {
                1 -> player(FaceAnim.FRIENDLY, "Yes.").also { stage++ }
                2 -> end()
            }

            11 -> {
                end()
                openNpcShop(player, npc.id)
            }

            12 -> npc(FaceAnim.HAPPY, "Certainly sir, where would you like to go?").also { stage++ }
            13 -> {
                end()
                CharterShipUtils.open(player)
            }

            14 -> options("Ok", "Choose again", "No").also { stage++ }
            15 -> when (buttonId) {
                1 -> {
                    end()
                    if (cost == 0) {
                        destination!!.sail(player)
                    }
                    if (!inInventory(player, Items.COINS_995, cost)) {
                        player(FaceAnim.HALF_GUILTY, "I don't have the money for that.")
                        return true
                    }
                    if (!player.inventory.remove(Item(Items.COINS_995, cost))) {
                        player(FaceAnim.HALF_GUILTY, "I don't have the money for that.")
                        return true
                    }
                    destination!!.sail(player)
                }

                2 -> {
                    end()
                    CharterShipUtils.open(player)
                }

                3 -> end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = TraderCrewmemberDialogue(player)

    override fun getIds(): IntArray = intArrayOf(
        NPCs.TRADER_STAN_4650,
        NPCs.TRADER_CREWMEMBER_4651,
        NPCs.TRADER_CREWMEMBER_4652,
        NPCs.TRADER_CREWMEMBER_4653,
        NPCs.TRADER_CREWMEMBER_4654,
        NPCs.TRADER_CREWMEMBER_4655,
        NPCs.TRADER_CREWMEMBER_4656,
    )
}

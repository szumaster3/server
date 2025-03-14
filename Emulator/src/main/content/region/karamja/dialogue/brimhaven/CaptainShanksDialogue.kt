package content.region.karamja.dialogue.brimhaven

import content.global.travel.charter.Charter
import core.api.*
import core.api.quest.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class CaptainShanksDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var coins: Item? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!hasRequirement(player, "Shilo Village", false)) {
            npcl(
                FaceAnim.HALF_GUILTY,
                "Oh dear, this ship is in a terrible state. And I just can't get the items I need to repair it because Shilo village is overrun with zombies.",
            )
            return true
        }
        npcl(
            FaceAnim.HALF_ASKING,
            "Hello there shipmate! I sail to Khazard Port and to Port Sarim. Where are you bound?",
        )
        stage =
            if (!inInventory(player, Items.SHIP_TICKET_621)) {
                -1
            } else {
                0
            }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            -1 ->
                npc(
                    FaceAnim.HALF_ASKING,
                    "I see you don't have a ticket for the ship, my colleague",
                    "normally only sells them in Shilo village. But I could sell",
                    "you one for a small additional charge.",
                ).also {
                    stage =
                        3
                }

            0 -> {
                setTitle(player, 3)
                sendDialogueOptions(
                    player,
                    "Captain Shanks asks, 'Where are you bound?",
                    "Khazard Port please.",
                    "Port Sarim please.",
                    "Nowhere just at the moment thanks.",
                ).also {
                    stage++
                }
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("Khazard Port please.")
                        stage =
                            if (!inInventory(player, Items.SHIP_TICKET_621)) {
                                -1
                            } else {
                                10
                            }
                    }

                    2 -> {
                        player("Port Sarim please.")
                        stage =
                            if (!inInventory(player, Items.SHIP_TICKET_621)) {
                                -1
                            } else {
                                20
                            }
                    }

                    3 -> player("Nowhere just at the moment thanks.").also { stage++ }
                }

            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Very well then me old shipmate, Just let me know if you change your mind.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            3 -> {
                coins = Item(995, RandomFunction.random(20, 50))
                npcl(FaceAnim.ASKING, "Shall we say " + coins!!.amount + " gold pieces?").also { stage++ }
            }
            4 -> {
                setTitle(player, 2)
                sendDialogueOptions(
                    player,
                    "Buy a ticket for " + coins!!.amount + " gold pieces.",
                    "Yes, I'll buy a ticket for the ship.",
                    "No thanks, not just at the moment.",
                ).also {
                    stage++
                }
            }

            5 ->
                when (buttonId) {
                    1 -> player("Yes, I'll buy a ticket for the ship.").also { stage = 7 }
                    2 -> player("No thanks, not just at the moment.").also { stage++ }
                }

            6 ->
                npcl(FaceAnim.HALF_GUILTY, "Very well me old shipmate, come back if you change your mind now.").also {
                    stage =
                        END_DIALOGUE
                }
            7 ->
                if (!inInventory(player, coins!!.amount)) {
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "Sorry me old ship mate, but you seem to be financially challenged at the moment. Come back when your coffers are full!",
                    )
                    stage = END_DIALOGUE
                } else if (freeSlots(player) == 0) {
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "Sorry me old ship mate, it looks like you haven't got enough space for a ticket. Come back when you've got rid of some of that junk.",
                    )
                    stage = END_DIALOGUE
                } else {
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "It's a good deal and no mistake. Here you go me old shipmate, here's your ticket.",
                    )
                    removeItem(player, coins)
                    addItem(player, Items.SHIP_TICKET_621)
                    stage++
                }

            8 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "Ok, now you have your ticket, do you want to sail anywhere?",
                ).also { stage++ }
            9 -> {
                setTitle(player, 3)
                sendDialogueOptions(
                    player,
                    "Captain Shanks asks, 'Do you want to sail anywhere?'",
                    "Khazard Port please.",
                    "Port Sarim please.",
                    "Nowhere just at the moment thanks.",
                ).also {
                    stage =
                        1
                }
            }
            10 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Very well then me old shipmate, I'll just take your ticket and then we'll set sail.",
                ).also {
                    stage++
                }
            11 -> {
                end()
                if (removeItem(player, Items.SHIP_TICKET_621)) {
                    Charter.sail(player, Charter.CAIRN_ISLAND_TO_PORT_KHAZARD)
                }
            }

            20 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Very well then me old shipmate, I'll just take your ticket and then we'll set sail.",
                ).also {
                    stage++
                }
            21 -> {
                end()
                if (removeItem(player, Items.SHIP_TICKET_621)) {
                    Charter.sail(player, Charter.PORT_SARIM)
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CaptainShanksDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAPTAIN_SHANKS_518)
    }
}

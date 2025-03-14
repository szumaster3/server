package content.region.kandarin.dialogue.seers

import core.api.freeSlots
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class SeerBartenderDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Good morning, what would you like?")
        return true
    }

    fun buy(
        item: Int,
        ammount: Int,
    ) {
        if (freeSlots(player) == 0) {
            player(FaceAnim.HALF_GUILTY, "I don't seem to have room, sorry.")
            stage = 99
        }
        if (!player.inventory.contains(995, ammount)) {
            player(FaceAnim.HALF_GUILTY, "Sorry, I don't seem to have enough coins.")
            stage = 99
        } else {
            end()
            player.inventory.remove(Item(995, ammount))
            player.inventory.add(Item(item, 1))
            when (item) {
                1917 -> sendMessage(player, "You buy a beer.")
                2327 -> sendMessage(player, "You buy a nice hot meat pie.")
                else -> sendMessage(player, "You buy a bowl of home made stew.")
            }
        }
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                options("What do you have?", "Beer please.", "I don't really want anything thanks.")
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "What do you have?")
                        stage = 30
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "Beer please.")
                        stage = 20
                    }

                    3 -> {
                        player(FaceAnim.HALF_GUILTY, "I don't really want anything thanks.")
                        stage = 67
                    }
                }

            67 -> end()
            30 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well we have beer, or if you want some food, we have",
                    "our home made stew and meat pies.",
                )
                stage = 31
            }

            20 -> {
                npc(FaceAnim.HALF_GUILTY, "One beer comng up. Ok, that'll be two coins.")
                stage = 101
            }

            10 -> end()
            31 -> {
                options(
                    "Beer please.",
                    "I'll try the meat pie.",
                    "Could I have some stew please?",
                    "I don't really want anything thanks.",
                )
                stage = 32
            }

            32 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "Beer please.")
                        stage = 100
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "I'll try the meat pie.")
                        stage = 200
                    }

                    3 -> {
                        player(FaceAnim.HALF_GUILTY, "Could I have some stew please?")
                        stage = 300
                    }

                    4 -> {
                        player(FaceAnim.HALF_GUILTY, "I don't really want anything thanks.")
                        stage = 99
                    }
                }

            99 -> end()
            100 -> {
                npc(FaceAnim.HALF_GUILTY, "One beer comng up. Ok, that'll be two coins.")
                stage = 101
            }

            101 -> buy(1917, 2)
            200 -> {
                npc(FaceAnim.HALF_GUILTY, "Okay, that will be 16 coins.")
                stage = 201
            }

            201 -> buy(2327, 16)
            300 -> {
                npc(FaceAnim.HALF_GUILTY, "A bowl of stew, that'll be 20 coins please.")
                stage = 301
            }

            301 -> buy(2003, 20)
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SeerBartenderDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARTENDER_737, NPCs.BARTENDER_738)
    }
}

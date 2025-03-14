package content.region.misc.dialogue.keldagrim

import core.api.addItemOrDrop
import core.api.amountInInventory
import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class BarmanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (amountInInventory(player, Items.COINS_995) > 2000) {
            npc(
                FaceAnim.OLD_HAPPY,
                "And what will it be for you, fine " + (if (player!!.isMale) "Sir" else "Madam") + "?",
            )
        } else {
            npc(FaceAnim.OLD_NORMAL, "Yes? Can I help you with something?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "A beer please.",
                    "A dwarven stout please.",
                    "A fine wine please.",
                    "Nothing, thanks.",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "A beer please.").also { stage = 10 }
                    2 -> player(FaceAnim.FRIENDLY, "A dwarven stout please.").also { stage = 15 }
                    3 -> player(FaceAnim.FRIENDLY, "A fine wine please.").also { stage = 20 }
                    4 -> end()
                }
            10 -> npc(FaceAnim.OLD_NORMAL, " That'll be 1 gold coin please.").also { stage++ }
            11 -> options("Pay.", "Don't pay.").also { stage++ }
            12 ->
                when (buttonId) {
                    1 ->
                        if (!removeItem(player, Item(Items.COINS_995, 1))) {
                            player(FaceAnim.NEUTRAL, "I'm sorry, I seem to have run out of money.")
                            stage = 100
                        } else {
                            npc(FaceAnim.OLD_DEFAULT, "Here you go. One delicious pint of beer.")
                            addItemOrDrop(player, Items.BEER_1917)
                            stage = 100
                        }

                    2 -> player(FaceAnim.NEUTRAL, "Sorry, I changed my mind.").also { stage = 100 }
                }

            15 -> npc(FaceAnim.OLD_HAPPY, "It'll be yours for just 2 gold coins!").also { stage++ }
            16 -> options("Pay.", "Don't pay.").also { stage++ }
            17 ->
                when (buttonId) {
                    1 ->
                        if (removeItem(player, Item(Items.COINS_995, 2))) {
                            player(FaceAnim.NEUTRAL, "I'm sorry, I seem to have run out of money.")
                            stage = 100
                        } else {
                            npc(FaceAnim.OLD_NORMAL, "One dwarven stout, best in all of Keldagrim!")
                            addItemOrDrop(player, Items.DWARVEN_STOUT_1913)
                            stage = 100
                        }

                    2 -> player(FaceAnim.NEUTRAL, "Sorry, I changed my mind.").also { stage = 100 }
                }

            20 ->
                npc(
                    FaceAnim.OLD_HAPPY,
                    "Ah, " + (if (player!!.isMale) "Sir" else "Madam") + " has taste! A jug of wine will cost",
                    "you 10 gold coins.",
                ).also { stage++ }
            21 -> options("Pay.", "Don't pay.").also { stage++ }
            22 ->
                when (buttonId) {
                    1 ->
                        if (!removeItem(player, Item(Items.COINS_995, 10))) {
                            player("I'm sorry, I seem to have run out of money.")
                            stage = 100
                        } else {
                            npc(
                                FaceAnim.OLD_HAPPY,
                                "I hope " + (if (player!!.isMale) "Sir" else "Madam") + " will enjoy it!",
                            )
                            addItemOrDrop(player, Items.JUG_OF_WINE_1993)
                            stage = 100
                        }
                    2 -> player(FaceAnim.NEUTRAL, "Sorry, I changed my mind.").also { stage = 100 }
                }
            100 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BarmanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARMAN_2179)
    }
}

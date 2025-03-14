package content.region.asgarnia.handlers

import content.region.misc.handlers.MinecartTravel
import core.api.openDialogue
import core.api.removeItem
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class DwarvenMinesListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.TRAIN_CART_7029, IntType.SCENERY, "ride") { player, _ ->
            var visitedKeldagrim = player.getAttribute("keldagrim-visited", false)
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        npc = NPC(NPCs.CART_CONDUCTOR_2180)
                        when (stage) {
                            0 -> player("I'd like to use your cart, please.").also { stage++ }
                            1 ->
                                if (!visitedKeldagrim) {
                                    npc(
                                        FaceAnim.OLD_DEFAULT,
                                        "Sorry, but I can only take people",
                                        "who have been there before.",
                                    ).also {
                                        stage =
                                            END_DIALOGUE
                                    }
                                } else {
                                    npc(FaceAnim.OLD_DEFAULT, "Alright, that'll cost ye 150gp.").also { stage++ }
                                }

                            2 -> options("Okay, sure.", "No, thanks.").also { stage++ }
                            3 ->
                                when (buttonID) {
                                    1 -> {
                                        end()
                                        if (!removeItem(player, Item(Items.COINS_995, 150))) {
                                            sendDialogue(player, "You can not afford that.")
                                        } else {
                                            MinecartTravel.goToKeldagrim(player)
                                        }
                                    }

                                    2 -> end()
                                }
                        }
                    }
                },
            )
            return@on true
        }
    }
}

package content.region.karamja.dialogue.tzhaar

import core.api.removeItem
import core.api.sendDialogueOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

class TzhaarMejJalDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "I have a fire cape here.")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                sendDialogueOptions(
                    player,
                    "Sell your fire cape?",
                    "Yes, sell it for 8,000 TokKul.",
                    "No, keep it.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        if (!removeItem(player, Item(Items.FIRE_CAPE_6570))) {
                            npc(FaceAnim.OLD_NORMAL, "You not have firecape, JalYt.")
                            return true
                        } else {
                            npc(FaceAnim.OLD_NORMAL, "Hand your cape here, young JalYte.")
                            Item(Items.TOKKUL_6529).amount = 8000
                            player.inventory.add(Item(Items.TOKKUL_6529))
                            Item(Items.TOKKUL_6529).amount = 1
                        }
                    }

                    2 -> end()
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return TzhaarMejJalDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(DialogueInterpreter.getDialogueKey("firecape-exchange"))
    }
}

package content.region.desert.dialogue.pollnivneach

import core.api.addItem
import core.api.anyInInventory
import core.api.freeSlots
import core.api.inBank
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class AliTheSnakeCharmerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (args.size > 0) {
            player("Wow a snake charmer. Can I have a go? Please?").also { stage = 5 }
            return true
        }
        player("Wow a snake charmer. Can I have a go? Please?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                sendDialogue(
                    "The snake charmer fails to acknowledge you,",
                    "he seems too deep into the music to notice you.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            5 ->
                sendDialogue(
                    "The snake charmer snaps out of his trance",
                    "and directs his full attention to you.",
                ).also {
                    stage++
                }
            6 -> player(FaceAnim.JOLLY, "Wow a snake charmer. Can I have a go? Please?").also { stage++ }
            7 -> {
                end()
                if (!anyInInventory(player, Items.SNAKE_CHARM_4605, Items.SNAKE_BASKET_4606) &&
                    !inBank(player, Items.SNAKE_CHARM_4605) &&
                    !inBank(player, Items.SNAKE_BASKET_4606)
                ) {
                    if (freeSlots(player) >= 2) {
                        addItem(player, Items.SNAKE_CHARM_4605)
                        addItem(player, Items.SNAKE_BASKET_4606)
                    } else {
                        GroundItemManager.create(Item(Items.SNAKE_CHARM_4605), player.location)
                        GroundItemManager.create(Item(Items.SNAKE_BASKET_4606), player.location)
                    }
                    npc(
                        FaceAnim.ANNOYED,
                        "If it means that you'll leave me alone, I would give you",
                        "my snake charming super starter kit complete",
                        "with flute and basket.",
                    )
                } else {
                    npc(FaceAnim.ANGRY, "I already gave you one!")
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AliTheSnakeCharmerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_SNAKE_CHARMER_1872)
    }
}

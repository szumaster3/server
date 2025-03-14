package content.global.skill.cooking.handlers

import content.global.skill.cooking.data.DairyProduct
import core.api.openChatbox
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items

@Initializable
class DairyChurnDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val ingredientId =
        arrayOf(Item(Items.BUCKET_OF_MILK_1927, 1), Item(Items.POT_OF_CREAM_2130, 1), Item(Items.PAT_OF_BUTTER_6697, 1))

    private var product: DairyProduct? = null
    private var amount: Int = 0

    override fun open(vararg args: Any): Boolean {
        openChatbox(player, Components.COOKING_CHURN_OP3_74)
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        var product: DairyProduct? = null
        var amount = 0
        when (buttonId) {
            6 -> {
                product = DairyProduct.POT_OF_CREAM
                amount = 1
            }

            5 -> {
                product = DairyProduct.POT_OF_CREAM
                amount = 5
            }

            4 -> {
                product = DairyProduct.POT_OF_CREAM
                amount = 10
            }

            9 -> {
                product = DairyProduct.PAT_OF_BUTTER
                amount = 1
            }

            8 -> {
                product = DairyProduct.PAT_OF_BUTTER
                amount = 5
            }

            7 -> {
                product = DairyProduct.PAT_OF_BUTTER
                amount = 10
            }

            12 -> {
                product = DairyProduct.CHEESE
                amount = 1
            }

            11 -> {
                product = DairyProduct.CHEESE
                amount = 5
            }

            10 -> {
                product = DairyProduct.CHEESE
                amount = 10
            }
        }
        player.pulseManager.run(DairyChurnPulse(player, ingredientId[0], product!!, amount))
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return DairyChurnDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(984374)
    }
}

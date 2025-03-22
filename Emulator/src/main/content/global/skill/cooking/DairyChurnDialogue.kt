package content.global.skill.cooking

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
    private val ingredientIds =
        listOf(
            Item(Items.BUCKET_OF_MILK_1927, 1),
            Item(Items.POT_OF_CREAM_2130, 1),
            Item(Items.PAT_OF_BUTTER_6697, 1),
        )

    override fun open(vararg args: Any): Boolean {
        val hasMilk = player.inventory.contains(Items.BUCKET_OF_MILK_1927, 1)
        val hasCream = player.inventory.contains(Items.POT_OF_CREAM_2130, 1)
        val hasButter = player.inventory.contains(Items.PAT_OF_BUTTER_6697, 1)

        val interfaceId =
            when {
                hasMilk && !hasButter -> Components.COOKING_CHURN_OP1_72
                hasButter || hasCream -> Components.COOKING_CHURN_OP2_73
                else -> Components.COOKING_CHURN_OP3_74
            }

        openChatbox(player, interfaceId)
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val (product, amount) =
            when (buttonId) {
                6 -> DairyProduct.POT_OF_CREAM to 1
                5 -> DairyProduct.POT_OF_CREAM to 5
                4 -> DairyProduct.POT_OF_CREAM to 10
                9 -> DairyProduct.PAT_OF_BUTTER to 1
                8 -> DairyProduct.PAT_OF_BUTTER to 5
                7 -> DairyProduct.PAT_OF_BUTTER to 10
                12 -> DairyProduct.CHEESE to 1
                11 -> DairyProduct.CHEESE to 5
                10 -> DairyProduct.CHEESE to 10
                else -> return false
            }

        player.pulseManager.run(DairyChurnPulse(player, ingredientIds.first(), product, amount))
        return true
    }

    override fun newInstance(player: Player): Dialogue = DairyChurnDialogue(player)

    override fun getIds(): IntArray = intArrayOf(984374)
}

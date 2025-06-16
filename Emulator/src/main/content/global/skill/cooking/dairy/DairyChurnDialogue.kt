package content.global.skill.cooking.dairy

import core.api.openChatbox
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items

@Initializable
class DairyChurnDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        val hasMilk = player.inventory.contains(Items.BUCKET_OF_MILK_1927, 1)
        val hasCream = player.inventory.contains(Items.POT_OF_CREAM_2130, 1)
        val hasButter = player.inventory.contains(Items.PAT_OF_BUTTER_6697, 1)

        val interfaceId = when {
            hasMilk -> Components.COOKING_CHURN_OP3_74
            hasCream -> Components.COOKING_CHURN_OP2_73
            hasButter -> Components.COOKING_CHURN_OP3_74
            else -> return false
        }

        openChatbox(player, interfaceId)
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (interfaceId) {

            /*
             * Handles creating cheese, butter, or cream from milk.
             */

            74 -> {
                val (product, amount) = when (buttonId) {
                    13 -> DairyProduct.CHEESE to 1
                    12 -> DairyProduct.CHEESE to 5
                    11 -> DairyProduct.CHEESE to 10

                    10 -> DairyProduct.PAT_OF_BUTTER to 1
                    9 -> DairyProduct.PAT_OF_BUTTER to 5
                    8 -> DairyProduct.PAT_OF_BUTTER to 10

                    7 -> DairyProduct.POT_OF_CREAM to 1
                    6 -> DairyProduct.POT_OF_CREAM to 5
                    5 -> DairyProduct.POT_OF_CREAM to 10
                    else -> return false
                }

                player.pulseManager.run(DairyChurnPulse(player, Item(Items.BUCKET_OF_MILK_1927), product, amount))
            }

            /*
             * Handles creating butter or cheese from cream.
             */

            73 -> {
                val (product, amount) = when (buttonId) {
                    9 -> DairyProduct.CHEESE to 1
                    8 -> DairyProduct.CHEESE to 5
                    7 -> DairyProduct.CHEESE to 10

                    6 -> DairyProduct.PAT_OF_BUTTER to 1
                    5 -> DairyProduct.PAT_OF_BUTTER to 5
                    4 -> DairyProduct.PAT_OF_BUTTER to 10
                    else -> return false
                }

                player.pulseManager.run(DairyChurnPulse(player, Item(Items.POT_OF_CREAM_2130), product, amount))
            }

            /*
             * Handles turn pat of butter into a cheese.
             */

            else -> {
                val (product, amount) = when (buttonId) {
                    5 -> DairyProduct.CHEESE to 1
                    4 -> DairyProduct.CHEESE to 5
                    3 -> DairyProduct.CHEESE to 10
                    else -> return false
                }

                player.pulseManager.run(DairyChurnPulse(player, Item(Items.PAT_OF_BUTTER_6697), product, amount))
            }

        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = DairyChurnDialogue(player)

    override fun getIds(): IntArray = intArrayOf(984374)
}

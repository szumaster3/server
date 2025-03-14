package content.global.skill.cooking

import content.global.skill.cooking.data.CookableItem
import core.api.*
import core.api.ui.repositionChild
import core.game.dialogue.DialogueFile
import core.game.node.scenery.Scenery
import org.rs.consts.Components
import org.rs.consts.Items

class CookingDialogue(
    vararg val args: Any,
) : DialogueFile() {
    private var initial = 0
    private var product = 0
    private var scenery: Scenery? = null
    private var sinew = false
    private var itemId = 0

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> handleCook()
            1 -> handleAmount(buttonID)
            100, 101 -> handleProduct(buttonID)
        }
    }

    private fun handleCook() {
        when (args.size) {
            2 -> {
                initial = args[0] as Int
                product =
                    if (CookableItem.intentionalBurn(
                            initial,
                        )
                    ) {
                        CookableItem.getIntentionalBurn(initial).id
                    } else {
                        CookableItem.forId(initial)!!.cooked
                    }
                scenery = args[1] as Scenery
            }

            5 -> {
                initial = args[0] as Int
                product = args[1] as Int
                sinew = args[2] as Boolean
                scenery = args[3] as Scenery
                itemId = args[4] as Int
                options(if (sinew) "Dry the meat into sinew" else "Cook the meat", "Cook the meat")
                stage = if (amountInInventory(player!!, initial) > 1) 100 else 101
                return
            }
        }
        display()
    }

    private fun handleAmount(buttonID: Int) {
        end()
        val amount = getAmount(buttonID)
        if (amount == -1) {
            sendInputDialogue(player!!, true, "Enter the amount:") { value ->
                CookingRewrite.cook(player!!, scenery, initial, product, value.toString().toInt())
            }
        } else {
            CookingRewrite.cook(player!!, scenery, initial, product, amount)
        }
    }

    private fun handleProduct(buttonID: Int) {
        product =
            when (buttonID) {
                1 -> Items.SINEW_9436
                2 -> CookableItem.forId(initial)!!.cooked
                else -> return
            }
        display()
    }

    private fun getAmount(buttonId: Int): Int =
        when (buttonId) {
            5 -> 1
            4 -> 5
            3 -> -1
            2 -> amountInInventory(player!!, initial)
            else -> -1
        }

    private fun display() {
        sendItemZoomOnInterface(player!!, Components.SKILL_COOKMANY_307, 2, initial, 160)
        sendString(player!!, "<br><br><br><br>${getItemName(initial)}", Components.SKILL_COOKMANY_307, 6)
        repositionChild(player!!, Components.SKILL_COOKMANY_307, 0, 12, 15)
        repositionChild(player!!, Components.SKILL_COOKMANY_307, 1, 431, 15)
        repositionChild(player!!, Components.SKILL_COOKMANY_307, 7, 0, 12)
        repeat(4) {
            repositionChild(player!!, Components.SKILL_COOKMANY_307, 3 + it, 58, 27)
        }
        openChatbox(player!!, Components.SKILL_COOKMANY_307)
        stage = 1
    }
}

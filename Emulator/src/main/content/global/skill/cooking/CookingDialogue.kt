package content.global.skill.cooking

import core.api.*
import core.api.ui.repositionChild
import core.game.dialogue.DialogueFile
import core.game.node.scenery.Scenery
import core.tools.START_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * Represents the cooking dialogue.
 *
 * @param args Arguments required for initialization:
 * - [Int]: Initial item id (raw item).
 * - [Scenery]: Object used for cooking (e.g., fire, range).
 *
 * **Optionally includes**:
 *   - [Int]: Product item id (used for sinew flow).
 *   - [Boolean]: Whether it's sinew.
 *   - [Scenery]: Cooking object again.
 *   - [Int]: Item id.
 */
class CookingDialogue(vararg val args: Any) : DialogueFile() {

    /**
     * Represents the raw item used for cooking.
     */
    var initial = 0

    /**
     * Represents the product item (e.g., cooked food or sinew).
     */
    var product = 0

    /**
     * Represents the scenery (object) used for cooking, like a fire or a stove.
     */
    var scenery: Scenery? = null

    /**
     * Whether the sinew drying option is enabled.
     */
    private var isSinew = false

    /**
     * Optional item ID, used when sinew cooking is triggered.
     */
    var itemId = 0

    /**
     * Handles interaction logic for the dialogue interface.
     *
     * @param componentID The id of the component.
     * @param buttonID The id of the button clicked by the player.
     */
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> {
                when (args.size) {
                    2 -> {
                        initial = args[0] as Int
                        if (CookItem.intentionalBurn(initial)) {
                            // Intentional burning case
                            product = CookItem.getIntentionalBurn(initial).id
                        } else {
                            product = CookItem.forId(initial)!!.cooked
                        }
                        scenery = args[1] as Scenery
                    }

                    5 -> {
                        initial = args[0] as Int
                        product = args[1] as Int
                        isSinew = args[2] as Boolean
                        scenery = args[3] as Scenery
                        itemId = args[4] as Int
                        if (isSinew) {
                            options("Dry the meat into sinew", "Cook the meat")
                            stage = if (amountInInventory(player!!, initial) > 1) 100 else 101
                            return
                        }
                    }
                }
                display()
            }

            1 -> {
                end()
                when (val amount = getAmount(buttonID)) {
                    -1 -> sendInputDialogue(player!!, true, "Enter the amount:") { value ->
                        val count = if (value is String) value.toInt() else value as Int
                        CookingRewrite.cook(player!!, scenery, initial, product, count)
                    }

                    else -> {
                        end()
                        CookingRewrite.cook(player!!, scenery, initial, product, amount)
                    }
                }
            }

            100 -> {
                when (buttonID) {
                    1 -> {
                        product = Items.SINEW_9436
                        display()
                    }

                    2 -> {
                        product = CookItem.forId(initial)!!.cooked
                        display()
                    }
                }
            }

            101 -> {
                when (buttonID) {
                    1 -> {
                        end()
                        CookingRewrite.cook(player!!, scenery, initial, Items.SINEW_9436, 1)
                    }

                    2 -> {
                        end()
                        CookingRewrite.cook(player!!, scenery, initial, CookItem.forId(initial)!!.cooked, 1)
                    }
                }
            }
        }
    }

    /**
     * Maps the button id to the corresponding cooking amount.
     *
     * @param buttonId id of the button clicked.
     * @return Amount of items to cook or -1 if custom amount.
     */
    private fun getAmount(buttonId: Int): Int {
        return when (buttonId) {
            5 -> 1
            4 -> 5
            3 -> -1
            2 -> amountInInventory(player!!, initial)
            else -> -1
        }
    }

    /**
     * Displays the cooking interface, showing the item and repositioning components.
     */
    fun display() {
        repositionChild(player!!, Components.SKILL_COOKMANY_307, 2, 215, 27)
        sendItemZoomOnInterface(player!!, Components.SKILL_COOKMANY_307, 2, initial, 160)

        val item = getItemName(initial)
        sendString(player!!, "<br><br><br><br>    $item", Components.SKILL_COOKMANY_307, 6)

        /*
         * Swords sprite.
         */
        repositionChild(player!!, Components.SKILL_COOKMANY_307, 0, 12, 15)
        repositionChild(player!!, Components.SKILL_COOKMANY_307, 1, 431, 15)
        /*
         * "How many would you like to cook?".
         */
        repositionChild(player!!, Components.SKILL_COOKMANY_307, 7, 0, 12)
        /*
         * Right click context menu boxes.
         */
        repositionChild(player!!, Components.SKILL_COOKMANY_307, 3, 58, 27)
        repositionChild(player!!, Components.SKILL_COOKMANY_307, 4, 58, 27)
        repositionChild(player!!, Components.SKILL_COOKMANY_307, 5, 58, 27)
        repositionChild(player!!, Components.SKILL_COOKMANY_307, 6, 58, 27)
        openChatbox(player!!, Components.SKILL_COOKMANY_307)
        stage = 1
    }
}

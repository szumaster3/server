package content.global.skill.cooking

import core.api.*
import core.game.dialogue.Dialogue
import core.game.event.ResourceProducedEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Scenery
import java.util.*

/**
 * Handles interactions with dairy churns.
 */
class DairyChurnPlugin : InteractionListener {

    private val ingredients = intArrayOf(Items.BUCKET_OF_MILK_1927, Items.POT_OF_CREAM_2130, Items.PAT_OF_BUTTER_6697)
    private val churns = intArrayOf(Scenery.DAIRY_CHURN_10093, Scenery.DAIRY_CHURN_10094, Scenery.DAIRY_CHURN_25720, Scenery.DAIRY_CHURN_34800, Scenery.DAIRY_CHURN_35931)

    companion object {
        private const val DIALOGUE_ID = 984374
    }

    private fun checkHasIngredients(player: Player): Boolean {
        if (!anyInInventory(player, *ingredients)) {
            sendMessage(player, "You need some milk, cream or butter to use in the churn.")
            return false
        }
        return true
    }

    override fun defineListeners() {
        ClassScanner.definePlugin(DairyChurnDialogue())

        /*
         * Handles interaction with churn scenery objects.
         */

        on(churns, IntType.SCENERY, "churn") { player, _ ->
            if (!checkHasIngredients(player)) return@on true
            player.dialogueInterpreter.open(DIALOGUE_ID)
            return@on true
        }

        /*
         * Handles using milk, cream or butter on churn scenery objects.
         */

        onUseWith(IntType.SCENERY, ingredients, *churns) { player, _, _ ->
            if (!checkHasIngredients(player)) return@onUseWith true
            player.dialogueInterpreter.open(DIALOGUE_ID)
            return@onUseWith true
        }
    }
}

/**
 * Handles Dialogue shown to player when interacting with a dairy churn.
 */
class DairyChurnDialogue(player: Player? = null) : Dialogue(player) {

    companion object {
        private const val FALLBACK_INTERFACE_ID = -1
    }

    override fun open(vararg args: Any?): Boolean {
        val interfaceId = when {
            inInventory(player, Items.BUCKET_OF_MILK_1927) || inInventory(player, Items.PAT_OF_BUTTER_6697) -> Components.COOKING_CHURN_OP3_74
            inInventory(player, Items.POT_OF_CREAM_2130) -> Components.COOKING_CHURN_OP2_73
            else -> return false
        }

        openChatbox(player, interfaceId)
        return true
    }

    private data class ChurnOption(val input: Int, val product: DairyProduct, val amount: Int)

    private val churnOptions: Map<Int, Map<Int, ChurnOption>> = mapOf(
        Components.COOKING_CHURN_OP3_74 to mapOf(
            13 to ChurnOption(Items.BUCKET_OF_MILK_1927, DairyProduct.CHEESE, 1),
            12 to ChurnOption(Items.BUCKET_OF_MILK_1927, DairyProduct.CHEESE, 5),
            11 to ChurnOption(Items.BUCKET_OF_MILK_1927, DairyProduct.CHEESE, 10),
            10 to ChurnOption(Items.BUCKET_OF_MILK_1927, DairyProduct.PAT_OF_BUTTER, 1),
            9  to ChurnOption(Items.BUCKET_OF_MILK_1927, DairyProduct.PAT_OF_BUTTER, 5),
            8  to ChurnOption(Items.BUCKET_OF_MILK_1927, DairyProduct.PAT_OF_BUTTER, 10),
            7  to ChurnOption(Items.BUCKET_OF_MILK_1927, DairyProduct.POT_OF_CREAM, 1),
            6  to ChurnOption(Items.BUCKET_OF_MILK_1927, DairyProduct.POT_OF_CREAM, 5),
            5  to ChurnOption(Items.BUCKET_OF_MILK_1927, DairyProduct.POT_OF_CREAM, 10),
        ),
        Components.COOKING_CHURN_OP2_73 to mapOf(
            9 to ChurnOption(Items.POT_OF_CREAM_2130, DairyProduct.CHEESE, 1),
            8 to ChurnOption(Items.POT_OF_CREAM_2130, DairyProduct.CHEESE, 5),
            7 to ChurnOption(Items.POT_OF_CREAM_2130, DairyProduct.CHEESE, 10),
            6 to ChurnOption(Items.POT_OF_CREAM_2130, DairyProduct.PAT_OF_BUTTER, 1),
            5 to ChurnOption(Items.POT_OF_CREAM_2130, DairyProduct.PAT_OF_BUTTER, 5),
            4 to ChurnOption(Items.POT_OF_CREAM_2130, DairyProduct.PAT_OF_BUTTER, 10),
        ),
        FALLBACK_INTERFACE_ID to mapOf(
            5 to ChurnOption(Items.PAT_OF_BUTTER_6697, DairyProduct.CHEESE, 1),
            4 to ChurnOption(Items.PAT_OF_BUTTER_6697, DairyProduct.CHEESE, 5),
            3 to ChurnOption(Items.PAT_OF_BUTTER_6697, DairyProduct.CHEESE, 10),
        )
    )

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val id = if (churnOptions.containsKey(interfaceId)) interfaceId else FALLBACK_INTERFACE_ID
        val option = churnOptions[id]?.get(buttonId) ?: return false

        player.pulseManager.run(DairyChurnPulse(player, Item(option.input), option.product, option.amount))
        return true
    }

    override fun newInstance(player: Player?): Dialogue = DairyChurnDialogue(player)

    override fun getIds(): IntArray = intArrayOf(984374)
}

/**
 * handles the churning process for dairy products.
 */
private class DairyChurnPulse(
    player: Player?,
    item: Item?,
    private val dairy: DairyProduct,
    private var amount: Int
) : SkillPulse<Item?>(player, item) {

    companion object {
        private val ANIMATION = Animation(Animations.CHURN_BUTTER_2793)
        private val BUCKET_OF_MILK = Item(Items.BUCKET_OF_MILK_1927)
        private val BUCKET = Item(Items.BUCKET_1925)
    }

    init {
        delay = 8
    }

    override fun checkRequirements(): Boolean {
        closeChatBox(player)
        val input = dairy.inputsItems.firstOrNull { inInventory(player, it.id) } ?: run {
            sendMessage(player, "You need a bucket of milk.")
            return false
        }

        if (getStatLevel(player, Skills.COOKING) < dairy.level) {
            sendMessage(player, "You need a cooking level of ${dairy.level} to cook this.")
            return false
        }

        val availableAmount = amountInInventory(player, input.id)
        if (amount > availableAmount) amount = availableAmount
        if (amount < 1) return false

        animate()
        node = input
        return true
    }

    override fun animate() {
        player.animate(ANIMATION)
    }

    override fun reward(): Boolean {
        amount--
        for (input in dairy.inputsItems) {
            if (removeItem(player, input)) {
                player.inventory.add(dairy.product)
                if (input.id == BUCKET_OF_MILK.id) addItemOrDrop(player, BUCKET.id)

                val productName = getItemName(dairy.product.id).lowercase(Locale.getDefault())
                val article = if (productName.firstOrNull()?.isVowel() == true) "an" else "a"
                sendMessage(player, "You make $article $productName.")

                player.dispatch(ResourceProducedEvent(dairy.product.id, amount, input, BUCKET_OF_MILK.id))
                player.getSkills().addExperience(Skills.COOKING, dairy.experience, true)
                break
            }
        }
        return amount < 1
    }

    private fun Char.isVowel(): Boolean = this.lowercaseChar() in "aeiou"
}

/**
 * Represents dairy products.
 *
 * @property level      The required cooking level to make the product.
 * @property experience The experience gained per product.
 * @property product    The resulting item.
 * @property inputs     The array of possible input item ids.
 */
enum class DairyProduct(val level: Int, val experience: Double, val product: Item, inputs: Array<Int>) {
    POT_OF_CREAM(21, 18.0, Item(Items.POT_OF_CREAM_2130), arrayOf(Items.BUCKET_OF_MILK_1927)),
    PAT_OF_BUTTER(38, 40.5, Item(Items.PAT_OF_BUTTER_6697), arrayOf(Items.BUCKET_OF_MILK_1927, Items.POT_OF_CREAM_2130)),
    CHEESE(48, 64.0, Item(Items.CHEESE_1985), arrayOf(Items.BUCKET_OF_MILK_1927, Items.POT_OF_CREAM_2130, Items.PAT_OF_BUTTER_6697));

    val inputsItems: Array<Item> by lazy { inputs.map { Item(it) }.toTypedArray() }
}

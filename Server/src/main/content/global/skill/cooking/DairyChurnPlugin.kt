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
import shared.consts.*

/**
 * Represents dairy products.
 */
private enum class Churnable(val product: Item, val xp: Double, val animation: Animation?) {
    BUCKET_OF_MILK(Item(Items.BUCKET_OF_MILK_1927), 0.0, null),
    CREAM(Item(Items.POT_OF_CREAM_2130), 18.0, Animation(Animations.CHURN_CREAM_2793)),
    BUTTER(Item(Items.PAT_OF_BUTTER_6697), 41.0, Animation(Animations.CHURN_BUTTER_2794)),
    CHEESE(Item(Items.CHEESE_1985), 64.0, Animation(Animations.CHURN_CHEESE_2795));
    override fun toString(): String = name.lowercase().replace("_", " ").replace("bucket of ", "")


    companion object {

        private val CHURN_DELAY = mapOf<Pair<Churnable, Churnable>, Int>(
            Churnable.BUCKET_OF_MILK to Churnable.CREAM to 10,
            Churnable.BUCKET_OF_MILK to Churnable.BUTTER to 20,
            Churnable.BUCKET_OF_MILK to Churnable.CHEESE to 27,
            Churnable.CREAM to Churnable.BUTTER to 10,
            Churnable.CREAM to Churnable.CHEESE to 17,
            Churnable.BUTTER to Churnable.CHEESE to 7
        )

        fun getChurnDelay(ingredient: Churnable, product: Churnable): Int =
            CHURN_DELAY[ingredient to product] ?: 0
    }
}

private data class ChurnOption(val churnable: Churnable, val amount: Int)

/**
 * Handles interactions with dairy churns.
 */
class DairyChurnPlugin : InteractionListener {

    private val ingredients = intArrayOf(
        Items.BUCKET_OF_MILK_1927,
        Items.POT_OF_CREAM_2130,
        Items.PAT_OF_BUTTER_6697
    )

    private val churns = intArrayOf(
        Scenery.DAIRY_CHURN_10093,
        Scenery.DAIRY_CHURN_10094,
        Scenery.DAIRY_CHURN_25720,
        Scenery.DAIRY_CHURN_34800,
        Scenery.DAIRY_CHURN_35931
    )

    companion object {
        const val DIALOGUE_ID = 984374
    }

    override fun defineListeners() {
        ClassScanner.definePlugin(DairyChurnDialogue())

        on(churns, IntType.SCENERY, "churn") { player, _ ->
            player.dialogueInterpreter.open(DIALOGUE_ID)
            return@on true
        }

        onUseWith(IntType.SCENERY, ingredients, *churns) { player, _, _ ->
            player.dialogueInterpreter.open(DIALOGUE_ID)
            return@onUseWith true
        }
    }

    inner /**
     * Dialogue shown when interacting with a dairy churn.
     */
    class DairyChurnDialogue(player: Player? = null) : Dialogue(player) {

        private val churnOptionsOp1 = mapOf(
            4 to ChurnOption(Churnable.CHEESE, 1),
            3 to ChurnOption(Churnable.CHEESE, 5),
            2 to ChurnOption(Churnable.CHEESE, 10)
        )

        private val churnOptionsOp2 = mapOf(
            8 to ChurnOption(Churnable.CHEESE, 1),
            7 to ChurnOption(Churnable.CHEESE, 5),
            6 to ChurnOption(Churnable.CHEESE, 10),
            5 to ChurnOption(Churnable.BUTTER, 1),
            4 to ChurnOption(Churnable.BUTTER, 5),
            3 to ChurnOption(Churnable.BUTTER, 10)
        )

        private val churnOptionsOp3 = mapOf(
            12 to ChurnOption(Churnable.CHEESE, 1),
            11 to ChurnOption(Churnable.CHEESE, 5),
            10 to ChurnOption(Churnable.CHEESE, 10),
            9  to ChurnOption(Churnable.BUTTER, 1),
            8  to ChurnOption(Churnable.BUTTER, 5),
            7  to ChurnOption(Churnable.BUTTER, 10),
            6  to ChurnOption(Churnable.CREAM, 1),
            5  to ChurnOption(Churnable.CREAM, 5),
            4  to ChurnOption(Churnable.CREAM, 10)
        )

        override fun open(vararg args: Any?): Boolean {
            val interfaceId = when {
                getStatLevel(player, Skills.COOKING) >= 48 -> Components.COOKING_CHURN_OP3_74
                getStatLevel(player, Skills.COOKING) >= 38 -> Components.COOKING_CHURN_OP2_73
                getStatLevel(player, Skills.COOKING) >= 21 -> Components.COOKING_CHURN_OP1_72
                else -> {
                    sendMessage(player, "You must have a Cooking level of at least 21 to use this.")
                    return false
                }
            }

            openChatbox(player, interfaceId)
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            val map = when (interfaceId) {
                Components.COOKING_CHURN_OP3_74 -> churnOptionsOp3
                Components.COOKING_CHURN_OP2_73 -> churnOptionsOp2
                Components.COOKING_CHURN_OP1_72 -> churnOptionsOp1
                else -> return false
            }

            val option = map[buttonId] ?: run {
                println("No option found for button=$buttonId.")
                return false
            }
            player.pulseManager.run(DairyChurnPulse(player, option.churnable, option.amount))
            return true
        }

        override fun newInstance(player: Player?) = DairyChurnDialogue(player)
        override fun getIds() = intArrayOf(DairyChurnPlugin.DIALOGUE_ID)
    }
}

/**
 * Handles the churning process for dairy products.
 */
private class DairyChurnPulse(
    player: Player,
    private val churnable: Churnable,
    private var amount: Int
) : SkillPulse<Item?>(player, churnable.product) {

    companion object {
        private val BUCKET = Item(Items.BUCKET_1925)
    }

    private var ingredient: Churnable? = null
    private var cycle: Int = 0
    private var ticks = 0

    override fun checkRequirements(): Boolean {
        closeChatBox(player)
        ingredient = player.findIngredient(churnable) ?: run {
            sendMessage(player, "You need some milk to churn $churnable.")
            return false
        }
        node = ingredient!!.product
        cycle = Churnable.getChurnDelay(ingredient!!, churnable)
        animate()
        return true
    }

    override fun animate() {
        player.animate(churnable.animation)
        playAudio(player, Sounds.CHURN_2574, 1)
    }

    override fun reward(): Boolean {
        ticks++
        if (ticks < cycle) return false
        ticks = 0

        val ingredientId = player.findIngredient(churnable)?.product ?: return true
        if (!removeItem(player, ingredientId)) return true

        addItem(player, churnable.product.id)
        if (ingredientId.id == Items.BUCKET_OF_MILK_1927) addItemOrDrop(player, BUCKET.id)

        sendMessage(player, "You make $churnable.")
        player.dispatch(ResourceProducedEvent(churnable.product.id, 1, ingredientId, Items.BUCKET_OF_MILK_1927))
        rewardXP(player, Skills.COOKING, churnable.xp)

        amount--
        return amount <= 0
    }
}

private fun Player.findIngredient(forProduct: Churnable): Churnable? = when (forProduct) {
    Churnable.CHEESE -> listOf(Churnable.BUTTER, Churnable.CREAM, Churnable.BUCKET_OF_MILK)
        .firstOrNull { inventory.containsItem(it.product) }
    Churnable.BUTTER -> listOf(Churnable.CREAM, Churnable.BUCKET_OF_MILK)
        .firstOrNull { inventory.containsItem(it.product) }
    Churnable.CREAM -> if (inventory.containsItem(Churnable.BUCKET_OF_MILK.product)) Churnable.BUCKET_OF_MILK else null
    else -> null
}

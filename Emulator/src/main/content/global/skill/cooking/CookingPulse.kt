package content.global.skill.cooking

import content.global.skill.cooking.data.CookableItem
import core.api.getStatLevel
import core.api.playAudio
import core.api.quest.isQuestComplete
import core.api.sendDialogue
import core.api.sendMessage
import core.game.event.ResourceProducedEvent
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Sounds

open class CookingPulse(
    open val player: Player,
    open val scenery: Scenery,
    open val initial: Int,
    open val product: Int,
    open var amount: Int,
) : Pulse() {
    private var experience = 0.0
    private var burned = false

    var properties: CookableItem? = null

    override fun start() {
        properties = CookableItem.forId(initial)
        if (checkRequirements()) {
            super.start()
            cook(player, scenery, properties != null && burned, initial, product)
            amount--
        }
    }

    override fun pulse(): Boolean {
        if (amount < 1 || !checkRequirements()) {
            return true
        }
        return reward()
    }

    fun animate() {
        player.animate(getAnimation(scenery))
    }

    open fun checkRequirements(): Boolean {
        this.experience = 0.0
        if (properties != null) {
            if (scenery.id == LUMBRIDGE_RANGE && !isQuestComplete(player, Quests.COOKS_ASSISTANT)) {
                sendDialogue(player, "That requires completion of the Cook's Assistant quest in order to use it.")
                return false
            }

            if (getStatLevel(player, Skills.COOKING) < properties!!.level) {
                sendDialogue(player, "You need a cooking level of " + properties!!.level + " to cook this.")
                return false
            }

            this.experience = properties!!.experience
            this.burned = isBurned(player, scenery, initial)
        }
        if (amount < 1) {
            return false
        }

        return scenery.isActive
    }

    open fun reward(): Boolean {
        if (delay == 1) {
            var delay = if (scenery.name.lowercase().contains("range")) 5 else 4
            setDelay(delay)
            return false
        }

        if (cook(player, scenery, burned, initial, product)) {
            amount--
        } else {
            return true
        }
        return amount < 1
    }

    open fun isBurned(
        player: Player,
        scenery: Scenery,
        food: Int,
    ): Boolean {
        val hasGauntlets = player.equipment.containsItem(Item(Items.COOKING_GAUNTLETS_775))
        var effectiveCookingLevel = player.getSkills().getLevel(Skills.COOKING)

        val item = CookableItem.forId(food)
        val low: Int
        val high: Int

        if (hasGauntlets &&
            CookableItem.gauntletValues.containsKey(
                food,
            )
        ) {
            val successValues = CookableItem.gauntletValues[food]
            low = successValues!![0]
            high = successValues[1]
        } else if (scenery.id == LUMBRIDGE_RANGE) {
            val successValues =
                CookableItem.lumbridgeRangeValues.getOrDefault(
                    food,
                    intArrayOf(item!!.lowRange, item.highRange),
                )
            low = successValues[0]
            high = successValues[1]
        } else {
            val isFire = scenery.name.lowercase().contains("fire")
            low = if (isFire) item!!.low else item!!.lowRange
            high = if (isFire) item.high else item.highRange
        }
        val host_ratio = RandomFunction.randomDouble(100.0)
        val client_ratio = RandomFunction.getSkillSuccessChance(low.toDouble(), high.toDouble(), effectiveCookingLevel)
        return host_ratio > client_ratio
    }

    open fun cook(
        player: Player,
        sceneryId: Scenery?,
        burned: Boolean,
        initial: Int,
        product: Int,
    ): Boolean {
        val initialItem = Item(initial)
        val productItem = Item(product)
        animate()

        when (initial) {
            Items.SKEWERED_CHOMPY_7230, Items.SKEWERED_RABBIT_7224, Items.SKEWERED_BIRD_MEAT_9984, Items.SKEWERED_BEAST_9992, Items.IRON_SPIT_7225 ->
                if (RandomFunction.random(
                        15,
                    ) == 5
                ) {
                    sendMessage(player, "Your iron spit seems to have broken in the process.")
                } else {
                    if (!player.inventory.add(Item(Items.IRON_SPIT_7225))) {
                        GroundItemManager.create(Item(Items.IRON_SPIT_7225), player.location, player)
                    }
                }

            Items.UNCOOKED_CAKE_1889 ->
                if (!player.inventory.add(Item(Items.CAKE_TIN_1887))) {
                    GroundItemManager.create(Item(Items.CAKE_TIN_1887), player)
                }
        }
        if (player.inventory.remove(initialItem)) {
            if (!burned) {
                player.inventory.add(productItem)
                player.dispatch(ResourceProducedEvent(productItem.id, 1, sceneryId!!, initialItem.id))
                player.getSkills().addExperience(Skills.COOKING, experience, true)
            } else {
                player.dispatch(
                    ResourceProducedEvent(
                        CookableItem.getBurnt(initial).id,
                        1,
                        sceneryId!!,
                        initialItem.id,
                    ),
                )
                player.inventory.add(CookableItem.getBurnt(initial))
            }
            getMessage(initialItem, productItem, burned)?.let { sendMessage(player, it) }
            playAudio(player, Sounds.FRY_2577)
            return true
        }
        return false
    }

    open fun getMessage(
        food: Item,
        product: Item,
        burned: Boolean,
    ): String? {
        return when {
            food.id == Items.RAW_OOMLIE_2337 -> "The meat is far too delicate to cook like this. Perhaps you should wrap something around it to protect it from the heat."
            product.id == Items.SODA_ASH_1781 && (food.id == Items.SEAWEED_401 || food.id == Items.SWAMP_WEED_10978) -> "You burn the ${food.name.lowercase()} into soda ash."
            food.id == Items.RAW_SWAMP_PASTE_1940 -> "You warm the paste over the fire. It thickens into a sticky goo."
            product.id == Items.BURNT_PIE_2329 && burned -> "You accidentally burn the pie."
            product.id == Items.NETTLE_TEA_4239 && !burned -> "You boil the water and make nettle tea."
            product.id == Items.BAKED_POTATO_6701 && !burned -> "You successfully bake a potato."
            product.id == Items.REDBERRY_PIE_2325 && !burned -> "You successfully bake a delicious redberry pie."
            product.id == Items.MEAT_PIE_2327 && !burned -> "You successfully bake a tasty meat pie."
            product.id == Items.APPLE_PIE_2323 && !burned -> "You successfully bake a traditional apple pie."
            product.id == Items.MUD_PIE_7170 && !burned -> "You successfully bake a mucky mud pie."
            product.id in
                listOf(
                    Items.BOWL_OF_HOT_WATER_4456,
                    Items.CUP_OF_HOT_WATER_4460,
                )
            -> if (burned) "You accidentally let the water boil over." else "You boil the water."

            CookableItem.intentionalBurn(food.id) -> "You deliberately burn the perfectly good piece of meat."

            else ->
                if (!burned && food.name.startsWith("Raw")) {
                    "You manage to cook some ${food.name.replace("Raw ", "")}."
                } else {
                    "You accidentally burn some ${food.name.replace("Raw ", "")}."
                }
        }
    }

    private fun getAnimation(scenery: Scenery): Animation {
        return if (!scenery.name.equals("fire", ignoreCase = true)) RANGE_ANIMATION else FIRE_ANIMATION
    }

    companion object {
        private val RANGE_ANIMATION = Animation(Animations.HUMAN_MAKE_PIZZA_883, Animator.Priority.HIGH)
        private val FIRE_ANIMATION = Animation(Animations.OLD_COOK_FIRE_897, Animator.Priority.HIGH)
        private const val LUMBRIDGE_RANGE = 114
    }
}

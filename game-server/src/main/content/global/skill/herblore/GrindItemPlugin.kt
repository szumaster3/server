package content.global.skill.herblore

import core.api.addItem
import core.api.amountInInventory
import core.api.removeItem
import core.api.repositionChild
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.SkillPulse
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Items
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * Represents an item that can be ground down.
 */
private enum class GrindItem(val items: Set<Int>, val product: Int, val message: String) {
    UNICORN_HORN(setOf(Items.UNICORN_HORN_237), Items.UNICORN_HORN_DUST_235, "You grind the Unicorn horn to dust."),
    KEBBIT_TEETH(setOf(Items.KEBBIT_TEETH_10109), Items.KEBBIT_TEETH_DUST_10111, "You grind the Kebbit teeth to dust."),
    BIRDS_NEST(setOf(Items.BIRDS_NEST_5070, Items.BIRDS_NEST_5071, Items.BIRDS_NEST_5072, Items.BIRDS_NEST_5073, Items.BIRDS_NEST_5074, Items.BIRDS_NEST_5075), Items.CRUSHED_NEST_6693, "You grind the Bird's nest down."),
    GOAT_HORN(setOf(Items.DESERT_GOAT_HORN_9735), Items.GOAT_HORN_DUST_9736, "You grind the goat's horn to dust."),
    MUD_RUNE(setOf(Items.MUD_RUNE_4698), Items.GROUND_MUD_RUNES_9594, "You grind the Mud rune down."),
    ASHES(setOf(Items.ASHES_592), Items.GROUND_ASHES_8865, "You grind down the ashes."),
    RAW_KARAMBWAN(setOf(Items.RAW_KARAMBWAN_3142), Items.KARAMBWAN_PASTE_3152, "You grind the raw Karambwan to form a sticky paste."),
    POISON_KARAMBWAN(setOf(Items.POISON_KARAMBWAN_3146), Items.KARAMBWAN_PASTE_3153, "You grind the cooked Karambwan to form a sticky paste."),
    FISHING_BAIT(setOf(Items.FISHING_BAIT_313), Items.GROUND_FISHING_BAIT_12129, "You grind down the Fishing bait."),
    SEAWEED(setOf(Items.SEAWEED_401), Items.GROUND_SEAWEED_6683, "You grind down the seaweed."),
    BAT_BONES(setOf(Items.BAT_BONES_530), Items.GROUND_BAT_BONES_2391, "You grind the bones to a powder."),
    CHARCOAL(setOf(Items.CHARCOAL_973), Items.GROUND_CHARCOAL_704, "You grind the charcoal to a powder."),
    ASTRAL_RUNE_SHARDS(setOf(Items.ASTRAL_RUNE_SHARDS_11156), Items.GROUND_ASTRAL_RUNE_11155, "You grind down the Astral Rune shard's."),
    GARLIC(setOf(Items.GARLIC_1550), Items.GARLIC_POWDER_4668, "You grind the Garlic into a powder."),
    DRAGON_SCALE(setOf(Items.BLUE_DRAGON_SCALE_243), Items.DRAGON_SCALE_DUST_241, "You grind the Dragon scale to dust."),
    ANCHOVIES(setOf(Items.ANCHOVIES_319), Items.ANCHOVY_PASTE_11266, "You grind the anchovies into a fishy, sticky paste."),
    CHOCOLATE_BAR(setOf(Items.CHOCOLATE_BAR_1973), Items.CHOCOLATE_DUST_1975, "You grind the chocolate into dust."),
    SULPHUR(setOf(Items.SULPHUR_3209), Items.GROUND_SULPHUR_3215, "You grind down the sulphur."),
    GUAM_LEAF(setOf(Items.CLEAN_GUAM_249), Items.GROUND_GUAM_6681, "You grind down the guam.");

    companion object {
        private val map: Map<Int, GrindItem> = values().flatMap {
                grindItem -> grindItem.items.map { it to grindItem }
        }.toMap()

        /**
         * Finds the [GrindItem] for given item id.
         */
        @JvmStatic
        fun forID(id: Int): GrindItem? = map[id]
    }
}

class GrindItemPlugin : InteractionListener {

    override fun defineListeners() {
        val grindable = GrindItem.values().flatMap { it.items }.toSet().toIntArray()

        onUseWith(IntType.ITEM, Items.PESTLE_AND_MORTAR_233, *grindable) { player, _, with ->
            val grindItem = GrindItem.forID(with.id) ?: return@onUseWith false

            val handler = object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, grindItem.product) {

                override fun create(amount: Int, index: Int) {
                    player.pulseManager.run(object : SkillPulse<Item>(player, Item(grindItem.product)) {
                        var remaining = amount

                        init {
                            val inventoryAmount = amountInInventory(player, node.id)
                            remaining = remaining.coerceAtMost(inventoryAmount)

                            if (node.id == FISHING_BAIT) {
                                val maxAmount = ceil(inventoryAmount.toDouble() / 10).roundToInt()
                                remaining = remaining.coerceAtMost(maxAmount)
                            }
                            delay = 2
                        }

                        override fun checkRequirements(): Boolean = true

                        override fun animate() {
                            player.animator.animate(ANIMATION)
                        }

                        override fun reward(): Boolean {
                            return if (node.id == FISHING_BAIT) {
                                val quantityToRemove = 10.coerceAtMost(amountInInventory(player, FISHING_BAIT))
                                if (removeItem(player, Item(node.id, quantityToRemove))) {
                                    addItem(player, grindItem.product, quantityToRemove)
                                    remaining -= 1
                                    remaining <= 0
                                } else {
                                    true
                                }
                            } else {
                                if (removeItem(player, Item(node.id, 1))) {
                                    addItem(player, grindItem.product)
                                    remaining -= 1
                                    remaining <= 0
                                } else {
                                    true
                                }
                            }
                        }
                    })
                }

                override fun getAll(index: Int): Int {
                    return amountInInventory(player, with.id)
                }
            }
            handler.open()
            repositionChild(player, Components.SKILL_MULTI1_309, 2, 210, 15)
            return@onUseWith true
        }
    }

    companion object {
        private val ANIMATION = Animation(Animations.PESTLE_MORTAR_364)
        private const val FISHING_BAIT = Items.FISHING_BAIT_313
    }
}
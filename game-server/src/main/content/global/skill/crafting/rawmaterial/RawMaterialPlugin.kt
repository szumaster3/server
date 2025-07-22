package content.global.skill.crafting.rawmaterial

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomUtils
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds
import kotlin.math.min

class RawMaterialPlugin : InteractionListener {

    companion object {
        val MAXIMUM_SUCCESS_LEVEL = 40
        val BASE_SUCCESS_PROBABILITY = 0.75
        val MAXIMUM_SUCCESS_PROBABILITY = 1.0
        val spreadSuccess = MAXIMUM_SUCCESS_PROBABILITY - BASE_SUCCESS_PROBABILITY
        val successPerLevel = spreadSuccess / MAXIMUM_SUCCESS_LEVEL
        val graniteIDs = intArrayOf(Items.GRANITE_2KG_6981, Items.GRANITE_5KG_6983)
    }

    override fun defineListeners() {

        /*
         * Handles crafting the stone pillars (TokTzKetDill quest).
         */

        onUseWith(IntType.ITEM, Items.STONE_SLAB_13245, Items.CHISEL_1755) { player, used, with ->
            if (getStatLevel(player, Skills.CRAFTING) < 20) {
                sendDialogue(player, "You need a crafting level of at least 20 to turn the stone slab into a pillar.")
                return@onUseWith true
            }

            if (!inInventory(player, Items.HAMMER_2347)) {
                sendDialogue(player, "You need a hammer to do that.")
                return@onUseWith true
            }

            runTask(player, 2) {
                playAudio(player, Sounds.HAMMER_STONE_2100)
                animate(player, Animations.USE_HAMMER_CHISEL_11041)
                if (removeItem(player, used.id)) {
                    rewardXP(player, Skills.CRAFTING, 20.0)
                    addItem(player, Items.PILLAR_13246)
                    sendMessage(
                        player,
                        "You craft the stone into a pillar."
                    )
                }
            }
            return@onUseWith true
        }

        /*
         * Handles cutting granite.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, *graniteIDs) { player, _, with ->
            setTitle(player, 2)
            sendDialogueOptions(
                player,
                "What would you like to do?",
                "Split the block into smaller pieces.",
                "Nothing.",
            )
            addDialogueAction(player) { player, button ->
                if (button == 2) {
                    var amount = min(amountInInventory(player, with.id), amountInInventory(player, with.id))
                    submitIndividualPulse(player, GraniteCuttingPulse(player, Item(with.id), amount))
                } else {
                    return@addDialogueAction closeDialogue(player)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles crafting the limestone bricks.
         */

        onUseWith(IntType.ITEM, Items.LIMESTONE_3211, Items.CHISEL_1755) { player, used, _ ->
            if (!inInventory(player, Items.CHISEL_1755)) {
                return@onUseWith true
            }
            if (!inInventory(player, used.id)) {
                sendMessage(player, "You have ran out of limestone.")
                return@onUseWith true
            }
            if (getStatLevel(player, Skills.CRAFTING) < 12) {
                sendMessage(player, "You need a crafting level of at least 12 to turn the limestone into a brick.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Items.LIMESTONE_BRICK_3420)

                create { _, amount ->

                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask

                        val craftingLevel = getStatLevel(player, Skills.CRAFTING)
                        val successProbability = BASE_SUCCESS_PROBABILITY + (craftingLevel * successPerLevel)
                        val succeeded = RandomUtils.randomDouble() <= successProbability

                        playAudio(player, Sounds.CHISEL_2586)
                        animate(player, Animations.HUMAN_CHISEL_LIMESTONE_4470)

                        if (removeItem(player, used.id)) {
                            if (succeeded) {
                                rewardXP(player, Skills.CRAFTING, 6.0)
                                addItem(player, Items.LIMESTONE_BRICK_3420)
                                sendMessage(
                                    player,
                                    "You use the chisel on the limestone and carve it into a building block.",
                                )
                            } else {
                                rewardXP(player, Skills.CRAFTING, 1.5)
                                addItem(player, Items.ROCK_968)
                                sendMessage(
                                    player,
                                    "You use the chisel on the limestone but fail to carve it into a building block.",
                                )
                            }
                        }
                    }
                }

                calculateMaxAmount { _ ->
                    min(amountInInventory(player, used.id), amountInInventory(player, used.id))
                }
            }
            return@onUseWith true
        }
    }

}

/**
 * Handles material cutting pulse.
 */
private class GraniteCuttingPulse(player: Player, node: Item, var amount: Int, ) : SkillPulse<Item>(player, node) {
    companion object {
        val GRANITE = intArrayOf(Items.GRANITE_2KG_6981, Items.GRANITE_5KG_6983)
        val SLOTS = intArrayOf(1, 2, 3, 4)
    }

    private var ticks: Int = 0

    init {
        this.resetAnimation = false
    }

    override fun checkRequirements(): Boolean {
        if (!inInventory(player, Items.CHISEL_1755)) {
            return false
        }

        for (i in SLOTS.indices) {
            if (freeSlots(player) < i) {
                sendDialogue(
                    player,
                    "You'll need ${4 - i} empty inventory ${
                        if (freeSlots(
                                player,
                            ) <= 1
                        ) {
                            "spaces"
                        } else {
                            "space"
                        }
                    } to hold the granite once you've split this block.",
                )
                return false
            }
        }

        if (!anyInInventory(player, *GRANITE)) {
            sendMessage(player, "You have ran out of granite.")
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0 || ticks < 1) {
            playAudio(player, Sounds.CHISEL_2586)
            animate(player, Animations.HUMAN_CHISEL_GRANITE_11146)
        }
    }

    override fun reward(): Boolean {
        if (++ticks % 2 != 0) {
            return false
        }
        if (amount < 1) {
            return true
        }
        if (removeItem(player, Item(node.id, 1), Container.INVENTORY)) {
            if (node.id != Items.GRANITE_5KG_6983) {
                addItem(player, Items.GRANITE_500G_6979, 4, Container.INVENTORY)
            } else {
                addItem(player, Items.GRANITE_2KG_6981, 2, Container.INVENTORY)
                addItem(player, Items.GRANITE_500G_6979, 2, Container.INVENTORY)
            }
        }
        amount--
        return amount < 1
    }
}
package content.global.skill.firemaking

import content.global.skill.firemaking.items.Origami
import core.api.*
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Quests
import kotlin.math.min

class OrigamiBalloonPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles creating the origami balloon structure.
         */

        onUseWith(IntType.ITEM, Items.PAPYRUS_970, Items.BALL_OF_WOOL_1759) { player, used, wool ->
            if (getQuestStage(player, Quests.ENLIGHTENED_JOURNEY) < 1) {
                sendMessage(player, "You need start the ${Quests.ENLIGHTENED_JOURNEY} quest in order to make this.")
                return@onUseWith false
            }
            if (removeItem(player, used.asItem()) && removeItem(player, wool.asItem())) {
                sendMessage(player, "You create the origami balloon structure.")
                animate(player, Animations.CRAFT_BONGOS_5140)
                addItemOrDrop(player, Items.BALLOON_STRUCTURE_9933)
            }
            return@onUseWith true
        }

        /*
         * Handles combining a candle with the balloon structure to create an origami balloon.
         */

        onUseWith(IntType.ITEM, intArrayOf(Items.CANDLE_36, Items.BLACK_CANDLE_38), Items.BALLOON_STRUCTURE_9933) { player, used, with ->
            if (removeItem(player, Item(used.id, 1)) && removeItem(player, Item(with.id, 1))) {
                sendMessage(player, "You create the origami balloon.")
                rewardXP(player, Skills.CRAFTING, 35.0)
                animate(player, Animations.CRAFT_BONGOS_5140)
                addItemOrDrop(player, Items.ORIGAMI_BALLOON_9934)
            }
            return@onUseWith true
        }

        /*
         * Handles dyeing an origami balloon using dyes.
         */

        onUseWith(IntType.ITEM, DYE_IDS, Items.ORIGAMI_BALLOON_9934) { player, used, balloon ->
            val product = Origami.forId(used.id) ?: return@onUseWith true

            val maxAmount = min(amountInInventory(player, used.id), amountInInventory(player, balloon.id))
            if (maxAmount < 1) return@onUseWith true

            sendSkillDialogue(player) {
                withItems(product.product)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (!inInventory(player, product.base) || !inInventory(player, Items.ORIGAMI_BALLOON_9934)) return@runTask
                        removeItem(player, product.base)
                        removeItem(player, Items.ORIGAMI_BALLOON_9934)
                        addItem(player, product.product, 1)
                    }
                }
                calculateMaxAmount { _ -> maxAmount }
            }

            return@onUseWith true
        }

        /*
         * Handles lighting and releasing a origami balloon.
         */

        onUseWith(IntType.ITEM, Items.TINDERBOX_590, *BALLOON_IDS) { player, _, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true
            if (!removeItem(player, with.asItem())) return@onUseWith true
            val gfx = Origami.forBalloon(with.id) ?: return@onUseWith true

            visualize(player, Animations.BALLOON_FLY_5142, -1)
            sendMessage(player, "You light the origami ${getItemName(with.id).lowercase()}.")
            delayClock(player, Clocks.SKILLING, 3)
            rewardXP(player, Skills.FIREMAKING, 20.0)

            spawnProjectile(
                source = player.location,
                dest = player.location.transform(player.direction, 6),
                projectile = gfx.graphic + 2,
                startHeight = 92,
                endHeight = 0,
                delay = 8,
                speed = 1000,
                angle = 0
            )

            return@onUseWith true
        }
    }

    companion object {
        private val DYE_IDS     = Origami.values().map { it.base }.toIntArray()
        private val BALLOON_IDS = Origami.values().map { it.product }.toIntArray()
    }
}
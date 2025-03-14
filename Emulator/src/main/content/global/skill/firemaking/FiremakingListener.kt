package content.global.skill.firemaking

import core.api.*
import core.api.quest.getQuestStage
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.Projectile
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests
import kotlin.math.min

class FiremakingListener : InteractionListener {
    companion object {
        private val dyesIDs = Origami.values().map { it.base }.toIntArray()
        private val balloonIDs = Origami.values().map { it.product }.toIntArray()
        private const val ORIGAMI_BALLOON = Items.ORIGAMI_BALLOON_9934
        private const val BALLOON_STRUCTURE = Items.BALLOON_STRUCTURE_9933
        private const val CRAFTING_ANIMATION = Animations.HUMAN_CRAFT_ORIGAMI_BALLOON_5140
        private const val RELEASE_A_BALLOON_ANIMATION = Animations.HUMAN_RELEASE_BALLOON_5142
        val logs =
            intArrayOf(
                Items.LOGS_1511,
                Items.OAK_LOGS_1521,
                Items.WILLOW_LOGS_1519,
                Items.MAPLE_LOGS_1517,
                Items.YEW_LOGS_1515,
                Items.MAGIC_LOGS_1513,
                Items.ACHEY_TREE_LOGS_2862,
                Items.PYRE_LOGS_3438,
                Items.OAK_PYRE_LOGS_3440,
                Items.WILLOW_PYRE_LOGS_3442,
                Items.MAPLE_PYRE_LOGS_3444,
                Items.YEW_PYRE_LOGS_3446,
                Items.MAGIC_PYRE_LOGS_3448,
                Items.TEAK_PYRE_LOGS_6211,
                Items.MAHOGANY_PYRE_LOG_6213,
                Items.MAHOGANY_LOGS_6332,
                Items.TEAK_LOGS_6333,
                Items.RED_LOGS_7404,
                Items.GREEN_LOGS_7405,
                Items.BLUE_LOGS_7406,
                Items.PURPLE_LOGS_10329,
                Items.WHITE_LOGS_10328,
                Items.SCRAPEY_TREE_LOGS_8934,
                Items.DREAM_LOG_9067,
                Items.ARCTIC_PYRE_LOGS_10808,
                Items.ARCTIC_PINE_LOGS_10810,
                Items.SPLIT_LOG_10812,
                Items.WINDSWEPT_LOGS_11035,
                Items.EUCALYPTUS_LOGS_12581,
                Items.EUCALYPTUS_PYRE_LOGS_12583,
                Items.JOGRE_BONES_3125,
            )
        val firelighter =
            intArrayOf(
                Items.RED_FIRELIGHTER_7329,
                Items.GREEN_FIRELIGHTER_7330,
                Items.BLUE_FIRELIGHTER_7331,
                Items.PURPLE_FIRELIGHTER_10326,
                Items.WHITE_FIRELIGHTER_10327,
            )
    }

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.TINDERBOX_590, *logs) { player, _, with ->
            player.pulseManager.run(
                FiremakingPulse(
                    player,
                    with.asItem(),
                    null,
                ),
            )
            return@onUseWith true
        }

        onUseWith(IntType.GROUNDITEM, Items.TINDERBOX_590, *logs) { player, _, with ->
            player.pulseManager.run(
                FiremakingPulse(
                    player,
                    with.asItem(),
                    with as GroundItem,
                ),
            )
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.LOGS_1511, *firelighter) { player, used, with ->
            var firelighter = GnomishFirelighter.forProduct(with.id)
            if (with.asItem().id == firelighter!!.product || used.id == firelighter.base) {
                sendMessage(player, "You can't do that.")
                return@onUseWith false
            }

            if (!removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                sendMessage(player, "You don't have required items in your inventory.")
            } else {
                replaceSlot(player, used.asItem().slot, Item(firelighter.product, 1))
                sendMessage(
                    player,
                    "You coat the log with the " +
                        getItemName(firelighter.base)
                            .replaceFirst(
                                "firelighter",
                                "chemicals",
                            ).lowercase() + ".",
                )
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.PAPYRUS_970, Items.BALL_OF_WOOL_1759) { player, used, wool ->
            if (getQuestStage(player, Quests.ENLIGHTENED_JOURNEY) < 1) {
                sendMessage(player, "You need start the ${Quests.ENLIGHTENED_JOURNEY} quest in order to make this.")
                return@onUseWith false
            }
            if (removeItem(player, used.asItem()) && removeItem(player, wool.asItem())) {
                sendMessage(player, "You create the origami balloonIDs structure.")
                animate(player, CRAFTING_ANIMATION)
                addItemOrDrop(player, BALLOON_STRUCTURE)
            }
            return@onUseWith true
        }

        onUseWith(
            IntType.ITEM,
            intArrayOf(Items.CANDLE_36, Items.BLACK_CANDLE_38),
            BALLOON_STRUCTURE,
        ) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                sendMessage(player, "You create the origami balloonIDs.")
                rewardXP(player, Skills.CRAFTING, 35.0)
                animate(player, CRAFTING_ANIMATION)
                addItemOrDrop(player, ORIGAMI_BALLOON)
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, dyesIDs, ORIGAMI_BALLOON) { player, used, balloon ->
            val product = Origami.forId(used.id) ?: return@onUseWith true
            if (amountInInventory(player, used.id) == amountInInventory(player, balloon.id)) {
                if (removeItem(player, product.base) && removeItem(player, ORIGAMI_BALLOON)) {
                    addItem(player, product.product, 1)
                }
                return@onUseWith true
            }
            sendSkillDialogue(player) {
                withItems(product.product)
                create { _, a ->
                    runTask(player, 2, a) {
                        if (a < 1) return@runTask
                        if (removeItem(player, product.base) && removeItem(player, ORIGAMI_BALLOON)) {
                            addItem(player, product.product, 1)
                        }
                    }
                }
                calculateMaxAmount { _ ->
                    min(amountInInventory(player, balloon.id), amountInInventory(player, balloon.id))
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.TINDERBOX_590, *balloonIDs) { player, _, with ->
            val gfx = Origami.forBalloon(with.id)

            if (removeItem(player, with.asItem())) {
                visualize(player, RELEASE_A_BALLOON_ANIMATION, gfx!!.graphic)
                sendMessage(player, "You light the origami ${getItemName(with.id).lowercase()}.")
                rewardXP(player, Skills.FIREMAKING, 20.00)
                spawnProjectile(
                    source = Projectile.getLocation(player),
                    dest = player.location.transform(player.direction, 10),
                    projectile = gfx.graphic + 2,
                    startHeight = 0,
                    endHeight = 0,
                    delay = 6,
                    speed = 1000,
                    angle = 0,
                )
            }
            return@onUseWith true
        }
    }
}

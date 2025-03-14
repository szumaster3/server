package content.region.asgarnia.handlers.taverley

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class CrystalChestListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CLOSED_CHEST_172, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "This chest is securely locked shut.")
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.CRYSTAL_KEY_989, Scenery.CLOSED_CHEST_172) { player, used, with ->
            val usedKey = used.asItem().slot
            if (!inInventory(player, Items.CRYSTAL_KEY_989, 1)) {
                sendMessage(player, "This chest is securely locked shut.")
                return@onUseWith false
            }
            if (freeSlots(player) == 0) {
                sendMessage(player, "Not enough inventory space.")
                return@onUseWith false
            }

            animate(player, Animations.OPEN_CHEST_536)
            replaceScenery(with.asScenery(), with.id + 1, 3, with.location)

            val reward = Reward.getReward(player)
            if (reward == Reward.II) removeItem(player, KEY) else replaceSlot(player, usedKey, DRAGONSTONE)

            reward?.items?.forEach { item ->
                player.inventory.add(item, player)
            }

            sendMessage(player, "You unlock the chest with your key.")
            sendMessage(player, "You find some treasure in the chest!")

            return@onUseWith true
        }
    }

    enum class Reward(
        val chance: Double,
        vararg items: Item,
    ) {
        I(39.69, Item(Items.SPINACH_ROLL_1969, 1), Item(Items.COINS_995, 2000)),
        II(16.72, Item(Items.UNCUT_DRAGONSTONE_1631, 1)),
        III(10.57, Item(Items.RAW_SWORDFISH_371, 5), Item(Items.COINS_995, 1000)),
        IV(
            7.73,
            Item(Items.AIR_RUNE_556, 50),
            Item(Items.WATER_RUNE_555, 50),
            Item(Items.EARTH_RUNE_557, 50),
            Item(Items.FIRE_RUNE_554, 50),
            Item(Items.MIND_RUNE_558, 50),
            Item(Items.BODY_RUNE_559, 50),
            Item(Items.CHAOS_RUNE_562, 10),
            Item(Items.COSMIC_RUNE_564, 10),
            Item(Items.DEATH_RUNE_560, 10),
            Item(Items.NATURE_RUNE_561, 10),
        ),
        V(6.55, Item(Items.COAL_454, 100)),
        VI(4.23, Item(Items.RUBY_1603, 2), Item(Items.DIAMOND_1601, 2)),
        VII(3.67, Item(Items.TOOTH_HALF_OF_A_KEY_985, 1), Item(Items.COINS_995, 750)),
        VIII(3.51, Item(Items.RUNITE_BAR_2363, 3)),
        IX(3.26, Item(Items.LOOP_HALF_OF_A_KEY_987, 1), Item(Items.COINS_995, 750)),
        X(2.75, Item(Items.IRON_ORE_441, 150)),
        XI(1.06, Item(Items.ADAMANT_SQ_SHIELD_1183, 1)),
        XII(0.26, Item(Items.RUNE_PLATELEGS_1079, 1)),
        XIII(0.26, Item(Items.RUNE_PLATESKIRT_1093, 1)),
        ;

        val items: Array<Item> = items as Array<Item>

        companion object {
            fun getReward(player: Player): Reward? {
                var totalChance = 0.0
                for (r in values()) {
                    if (r == XIII && player.appearance.isMale) {
                        continue
                    }
                    totalChance += r.chance
                }
                val random = RandomFunction.random(totalChance.toInt())

                var accumulatedChance = 0.0
                for (r in values()) {
                    accumulatedChance += r.chance
                    if (random < accumulatedChance) {
                        return r
                    }
                }
                return null
            }
        }
    }

    companion object {
        private val KEY = Item(Items.CRYSTAL_KEY_989)
        private val DRAGONSTONE = Item(Items.UNCUT_DRAGONSTONE_1631, 1)
    }
}

package content.minigame.barrows.plugin

import core.api.announceIfRare
import content.data.BossKillCounter
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.component.Component
import core.game.container.access.InterfaceContainer.generateItems
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.RandomFunction
import shared.consts.Components
import shared.consts.Items

object RewardChest {
    private val REGULAR_DROPS = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 1, 777, 380.0),
        WeightedItem(Items.MIND_RUNE_558, 250, 350, 125.0),
        WeightedItem(Items.CHAOS_RUNE_562, 115, 135, 125.0),
        WeightedItem(Items.DEATH_RUNE_560, 70, 85, 125.0),
        WeightedItem(Items.BLOOD_RUNE_565, 35, 45, 125.0),
        WeightedItem(Items.BOLT_RACK_4740, 35, 40, 125.0),
        WeightedItem(Items.TOOTH_HALF_OF_A_KEY_985, 1, 1, 3.0),
        WeightedItem(Items.LOOP_HALF_OF_A_KEY_987, 1, 1, 3.0),
        WeightedItem(Items.DRAGON_MED_HELM_1149, 1, 1, 1.0)
    )
    private val AHRIM = arrayOf(Items.AHRIMS_HOOD_4708, Items.AHRIMS_STAFF_4710, Items.AHRIMS_ROBETOP_4712, Items.AHRIMS_ROBESKIRT_4714)
    private val DHAROK = arrayOf(Items.DHAROKS_HELM_4716, Items.DHAROKS_GREATAXE_4718, Items.DHAROKS_PLATEBODY_4720, Items.DHAROKS_PLATELEGS_4722)
    private val GUTHAN = arrayOf(Items.GUTHANS_HELM_4724, Items.GUTHANS_WARSPEAR_4726, Items.GUTHANS_PLATEBODY_4728, Items.GUTHANS_CHAINSKIRT_4730)
    private val KARIL = arrayOf(Items.KARILS_COIF_4732, Items.KARILS_CROSSBOW_4734, Items.KARILS_LEATHERTOP_4736, Items.KARILS_LEATHERSKIRT_4738)
    private val TORAG = arrayOf(Items.TORAGS_HELM_4745, Items.TORAGS_HAMMERS_4747, Items.TORAGS_PLATEBODY_4749, Items.TORAGS_PLATELEGS_4751)
    private val VERAC = arrayOf(Items.VERACS_HELM_4753, Items.VERACS_FLAIL_4755, Items.VERACS_BRASSARD_4757, Items.VERACS_PLATESKIRT_4759)
    private val BARROWS_DROP_IDS = arrayOf(AHRIM, DHAROK, GUTHAN, KARIL, TORAG, VERAC)

    @JvmStatic
    fun reward(player: Player) {
        var barrowsRewardsIDs: MutableList<Int> = ArrayList()
        for (i in 0..5) {
            if (player.savedData.activityData.barrowBrothers[i]) {
                barrowsRewardsIDs.addAll(BARROWS_DROP_IDS[i])
            }
        }
        barrowsRewardsIDs.shuffle()

        var rewards: MutableList<Item> = ArrayList()
        val nKilled = barrowsRewardsIDs.size / 4
        roll@ for (i in 0 until nKilled + 1) {
            if (barrowsRewardsIDs.size > 0 && RandomFunction.roll(450 - 58 * nKilled)) {
                val reward = barrowsRewardsIDs[0]
                rewards.add(Item(reward, 1))
                barrowsRewardsIDs.removeAt(0)
            } else {
                val drop = REGULAR_DROPS.roll(null, 1)[0]
                for (i in 0 until rewards.size) {
                    if (rewards[i].id == drop.id) {
                        rewards[i].amount += drop.amount
                        continue@roll
                    }
                }
                rewards.add(drop)
            }
        }
        player.generateItems(
            rewards,
            Components.TRAIL_REWARD_364,
            4,
            emptyList(),
            3,
            4
        )
        player.interfaceManager.open(Component(Components.TRAIL_REWARD_364))
        BossKillCounter.addToBarrowsCount(player)
        for (item in rewards) {
            announceIfRare(player, item)
            if (!player.inventory.add(item)) {
                GroundItemManager.create(item, player)
            }
        }
    }
}

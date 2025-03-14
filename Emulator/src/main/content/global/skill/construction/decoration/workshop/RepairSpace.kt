package content.global.skill.construction.decoration.workshop

import content.data.items.BrokenItem
import content.data.items.BrokenItem.getRepair
import content.data.items.RepairItem
import content.region.misthalin.dialogue.lumbridge.BobDialogue
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery
import kotlin.math.ceil

/**
 * Handles interactions with armor repair stands.
 * Support repairing broken items, and barrows equipment.
 */
@Initializable
class RepairSpace :
    UseWithHandler(
    /*
     * itemIDs that can be used with the repair stand.
     */
        Items.BROKEN_AXE_494,
        Items.BROKEN_PICKAXE_468,
        Items.BROKEN_PICKAXE_474,
        Items.BROKEN_PICKAXE_476,
        Items.BROKEN_PICKAXE_478,
        Items.BROKEN_PICKAXE_470,
        Items.BROKEN_PICKAXE_472,
        Items.BROKEN_AXE_496,
        Items.BROKEN_AXE_498,
        Items.BROKEN_AXE_500,
        Items.BROKEN_AXE_502,
        Items.BROKEN_AXE_504,
        Items.BROKEN_AXE_506,
        Items.RUSTY_SWORD_686,
        Items.BROKEN_ARROW_687,
        Items.DAMAGED_ARMOUR_697,
        Items.BROKEN_ARMOUR_698,
        Items.BROKEN_STAFF_689,
        Items.AHRIMS_HOOD_100_4856,
        Items.AHRIMS_HOOD_75_4857,
        Items.AHRIMS_HOOD_50_4858,
        Items.AHRIMS_HOOD_25_4859,
        Items.AHRIMS_HOOD_0_4860,
        Items.AHRIMS_STAFF_100_4862,
        Items.AHRIMS_STAFF_75_4863,
        Items.AHRIMS_STAFF_50_4864,
        Items.AHRIMS_STAFF_25_4865,
        Items.AHRIMS_STAFF_0_4866,
        Items.AHRIMS_TOP_100_4868,
        Items.AHRIMS_TOP_75_4869,
        Items.AHRIMS_TOP_50_4870,
        Items.AHRIMS_TOP_25_4871,
        Items.AHRIMS_TOP_0_4872,
        Items.AHRIMS_SKIRT_100_4874,
        Items.AHRIMS_SKIRT_75_4875,
        Items.AHRIMS_SKIRT_50_4876,
        Items.AHRIMS_SKIRT_25_4877,
        Items.AHRIMS_SKIRT_0_4878,
        Items.DHAROKS_HELM_100_4880,
        Items.DHAROKS_HELM_75_4881,
        Items.DHAROKS_HELM_50_4882,
        Items.DHAROKS_HELM_25_4883,
        Items.DHAROKS_HELM_0_4884,
        Items.DHAROKS_AXE_100_4886,
        Items.DHAROKS_AXE_75_4887,
        Items.DHAROKS_AXE_50_4888,
        Items.DHAROKS_AXE_25_4889,
        Items.DHAROKS_AXE_0_4890,
        Items.DHAROKS_BODY_100_4892,
        Items.DHAROKS_BODY_75_4893,
        Items.DHAROKS_BODY_50_4894,
        Items.DHAROKS_BODY_25_4895,
        Items.DHAROKS_BODY_0_4896,
        Items.DHAROKS_LEGS_100_4898,
        Items.DHAROKS_LEGS_75_4899,
        Items.DHAROKS_LEGS_50_4900,
        Items.DHAROKS_LEGS_25_4901,
        Items.DHAROKS_LEGS_0_4902,
        Items.GUTHANS_HELM_100_4904,
        Items.GUTHANS_HELM_75_4905,
        Items.GUTHANS_HELM_50_4906,
        Items.GUTHANS_HELM_25_4907,
        Items.GUTHANS_HELM_0_4908,
        Items.GUTHANS_SPEAR_100_4910,
        Items.GUTHANS_SPEAR_75_4911,
        Items.GUTHANS_SPEAR_50_4912,
        Items.GUTHANS_SPEAR_25_4913,
        Items.GUTHANS_SPEAR_0_4914,
        Items.GUTHANS_BODY_100_4916,
        Items.GUTHANS_BODY_75_4917,
        Items.GUTHANS_BODY_50_4918,
        Items.GUTHANS_BODY_25_4919,
        Items.GUTHANS_BODY_0_4920,
        Items.GUTHANS_SKIRT_100_4922,
        Items.GUTHANS_SKIRT_75_4923,
        Items.GUTHANS_SKIRT_50_4924,
        Items.GUTHANS_SKIRT_25_4925,
        Items.GUTHANS_SKIRT_0_4926,
        Items.KARILS_COIF_100_4928,
        Items.KARILS_COIF_75_4929,
        Items.KARILS_COIF_50_4930,
        Items.KARILS_COIF_25_4931,
        Items.KARILS_COIF_0_4932,
        Items.KARILS_X_BOW_100_4934,
        Items.KARILS_X_BOW_75_4935,
        Items.KARILS_X_BOW_50_4936,
        Items.KARILS_X_BOW_25_4937,
        Items.KARILS_X_BOW_0_4938,
        Items.KARILS_TOP_100_4940,
        Items.KARILS_TOP_75_4941,
        Items.KARILS_TOP_50_4942,
        Items.KARILS_TOP_25_4943,
        Items.KARILS_TOP_0_4944,
        Items.KARILS_SKIRT_100_4946,
        Items.KARILS_SKIRT_75_4947,
        Items.KARILS_SKIRT_50_4948,
        Items.KARILS_SKIRT_25_4949,
        Items.KARILS_SKIRT_0_4950,
        Items.TORAGS_HELM_100_4952,
        Items.TORAGS_HELM_75_4953,
        Items.TORAGS_HELM_50_4954,
        Items.TORAGS_HELM_25_4955,
        Items.TORAGS_HELM_0_4956,
        Items.TORAGS_HAMMER_100_4958,
        Items.TORAGS_HAMMER_75_4959,
        Items.TORAGS_HAMMER_50_4960,
        Items.TORAGS_HAMMER_25_4961,
        Items.TORAGS_HAMMER_0_4962,
        Items.TORAGS_BODY_100_4964,
        Items.TORAGS_BODY_75_4965,
        Items.TORAGS_BODY_50_4966,
        Items.TORAGS_BODY_25_4967,
        Items.TORAGS_BODY_0_4968,
        Items.TORAGS_LEGS_100_4970,
        Items.TORAGS_LEGS_75_4971,
        Items.TORAGS_LEGS_50_4972,
        Items.TORAGS_LEGS_25_4973,
        Items.TORAGS_LEGS_0_4974,
        Items.VERACS_HELM_100_4976,
        Items.VERACS_HELM_75_4977,
        Items.VERACS_HELM_50_4978,
        Items.VERACS_HELM_25_4979,
        Items.VERACS_HELM_0_4980,
        Items.VERACS_FLAIL_100_4982,
        Items.VERACS_FLAIL_75_4983,
        Items.VERACS_FLAIL_50_4984,
        Items.VERACS_FLAIL_25_4985,
        Items.VERACS_FLAIL_0_4986,
        Items.VERACS_TOP_100_4988,
        Items.VERACS_TOP_75_4989,
        Items.VERACS_TOP_50_4990,
        Items.VERACS_TOP_25_4991,
        Items.VERACS_TOP_0_4992,
        Items.VERACS_SKIRT_100_4994,
        Items.VERACS_SKIRT_75_4995,
        Items.VERACS_SKIRT_50_4996,
        Items.VERACS_SKIRT_25_4997,
        Items.VERACS_SKIRT_0_4998,
        Items.BROKEN_AXE_6741,
    ) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(Scenery.REPAIR_BENCH_13713, OBJECT_TYPE, this)
        addHandler(Scenery.WHETSTONE_13714, OBJECT_TYPE, this)
        addHandler(Scenery.ARMOUR_REPAIR_STAND_13715, OBJECT_TYPE, this)
        return this
    }

    /**
     * Handles item repair interactions at a repair station.
     */
    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val player = event.player
        val item = event.used.asItem()
        val repairItem = RepairItem.forId(item.id)

        val brokenItem =
            intArrayOf(
                Items.BROKEN_ARROW_687,
                Items.BROKEN_STAFF_689,
                Items.RUSTY_SWORD_686,
                Items.DAMAGED_ARMOUR_697,
                Items.BROKEN_ARMOUR_698,
            )

        when (event.usedWith.id) {
            Scenery.REPAIR_BENCH_13713 ->
                if (item.id == Items.BROKEN_ARROW_687 || item.id == Items.BROKEN_STAFF_689) {
                    repair(player, item)
                    return true
                }

            Scenery.WHETSTONE_13714 ->
                if (item.id == Items.BROKEN_ARROW_687 ||
                    item.id == Items.BROKEN_STAFF_689 ||
                    item.id == Items.RUSTY_SWORD_686
                ) {
                    repair(player, item)
                    return true
                }

            Scenery.ARMOUR_REPAIR_STAND_13715 ->
                if (item.id in brokenItem) {
                    repair(player, item)
                } else {
                    var baseCost = 0.0
                    var product: Item? = null

                    if (repairItem != null) {
                        baseCost = repairItem.cost.toDouble()
                        product = repairItem.product
                    } else if (BobDialogue.BarrowsEquipment.isBarrowsItem(item.id)) {
                        val type = BobDialogue.BarrowsEquipment.formattedName(item.id)
                        val single = BobDialogue.BarrowsEquipment.getSingleName(type)
                        val equipment = BobDialogue.BarrowsEquipment.getEquipmentType(type)
                        val formattedName =
                            type
                                .lowercase()
                                .replace(single, "")
                                .trim()
                                .replace("'s", "")
                        val fullEquip =
                            BobDialogue.BarrowsEquipment.BarrowsFullEquipment.forName("$formattedName $equipment")
                        baseCost = BobDialogue.BarrowsEquipment.getFormattedCost(equipment, item).toDouble()
                        product = fullEquip.full
                    }

                    if (product == null || baseCost == 0.0) {
                        player.sendMessage("That item can't be repaired.")
                        return true
                    }

                    val cost =
                        ceil(
                            ((100.0 - (player.skills.getLevel(Skills.SMITHING) / 2.0)) / 100.0) * baseCost,
                        ).toInt()
                    openDialogue(player, RepairDialogue(item, cost, product))
                }
        }
        return true
    }

    /**
     * Repairs a damaged item.
     *
     * @param player The player repairing the item.
     * @param item   The damaged item.
     */
    private fun repair(
        player: Player,
        item: Item,
    ): Boolean {
        animate(player, Animations.CHURN_PLUMING_STAND_3654)
        getRepairType(player, item)
        return true
    }

    private fun getRepairType(
        player: Player,
        item: Item,
    ) {
        val repairType =
            when (item.id) {
                Items.BROKEN_ARROW_687 -> BrokenItem.EquipmentType.ARROWS
                Items.BROKEN_STAFF_689 -> BrokenItem.EquipmentType.STAVES
                Items.RUSTY_SWORD_686 -> BrokenItem.EquipmentType.SWORDS
                Items.DAMAGED_ARMOUR_697 -> BrokenItem.EquipmentType.ARMOUR
                Items.BROKEN_ARMOUR_698 -> BrokenItem.EquipmentType.LEGS
                else -> return
            }

        getRepair(repairType)?.let { repairedItem ->
            player.inventory.remove(item)
            player.inventory.add(repairedItem)
            sendItemDialogue(
                player,
                repairedItem.id,
                "You repair the ${getItemName(item.id)} and find it is a ${repairedItem.name.lowercase()}.",
            )
        }
    }

    inner class RepairDialogue(
        val item: Item,
        var cost: Int,
        var product: Item,
    ) : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            when (stage) {
                0 ->
                    sendDialogue(
                        player!!,
                        "Would you like to repair your ${getItemName(item.id)} for $cost gp?",
                    ).also { stage++ }
                1 -> options("Yes, please", "No, thanks.").also { stage++ }
                2 ->
                    when (buttonID) {
                        1 -> {
                            end()
                            val coins = Item(Items.COINS_995, cost)
                            if (player!!.inventory.containsItem(coins) && player!!.inventory.containsItem(item)) {
                                player!!.inventory.remove(item, coins)
                                player!!.inventory.add(product)
                                sendDialogue(player!!, "You repair your ${getItemName(product.id)} for $cost coins.")
                            } else {
                                sendDialogue(player!!, "You can't afford that.")
                            }
                        }

                        2 -> end()
                    }
            }
        }
    }
}

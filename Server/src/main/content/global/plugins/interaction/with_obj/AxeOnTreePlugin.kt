package content.global.plugins.interaction.with_obj

import content.global.skill.gather.woodcutting.WoodcuttingNode
import content.global.skill.gather.woodcutting.WoodcuttingPulse
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items

class AxeOnTreePlugin : InteractionListener {

    private val AXE_IDS = intArrayOf(Items.BRONZE_AXE_1351, Items.MITHRIL_AXE_1355, Items.IRON_AXE_1349, Items.BLACK_AXE_1361, Items.STEEL_AXE_1353, Items.ADAMANT_AXE_1357, Items.RUNE_AXE_1359, Items.DRAGON_AXE_6739, Items.INFERNO_ADZE_13661, Items.AXE_CLASS_1_14132, Items.AXE_CLASS_2_14134, Items.AXE_CLASS_3_14136, Items.AXE_CLASS_4_14138, Items.AXE_CLASS_5_14140)
    private val TREE_IDS = WoodcuttingNode.values().map { it.id }.toIntArray()

    override fun defineListeners() {

        /*
         * Handles use axe on tree.
         */

        onUseWith(IntType.SCENERY, AXE_IDS, *TREE_IDS) { player, _, with ->
            submitIndividualPulse(player, WoodcuttingPulse(player, with.asScenery()))
            return@onUseWith true
        }
    }
}

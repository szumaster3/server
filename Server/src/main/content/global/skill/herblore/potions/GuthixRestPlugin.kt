package content.global.skill.herblore.potions

import core.api.getStatLevel
import core.api.hasRequirement
import core.api.rewardXP
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Quests

class GuthixRestPlugin : InteractionListener {

    private val herbIds = setOf(Items.CLEAN_GUAM_249, Items.CLEAN_MARRENTILL_251, Items.CLEAN_HARRALANDER_255)

    enum class PartialTea(val ingredients: List<Int>, val teaId: Int) {
        HERB_TEA_MIX_1(listOf(Items.CLEAN_HARRALANDER_255), Items.HERB_TEA_MIX_4464),
        HERB_TEA_MIX_2(listOf(Items.CLEAN_GUAM_249), Items.HERB_TEA_MIX_4466),
        HERB_TEA_MIX_3(listOf(Items.CLEAN_MARRENTILL_251), Items.HERB_TEA_MIX_4468),
        HERB_TEA_MIX_4(listOf(Items.CLEAN_HARRALANDER_255, Items.CLEAN_MARRENTILL_251), Items.HERB_TEA_MIX_4470),
        HERB_TEA_MIX_5(listOf(Items.CLEAN_HARRALANDER_255, Items.CLEAN_GUAM_249), Items.HERB_TEA_MIX_4472),
        HERB_TEA_MIX_6(listOf(Items.CLEAN_GUAM_249, Items.CLEAN_GUAM_249), Items.HERB_TEA_MIX_4474), HERB_TEA_MIX_7(listOf(
            Items.CLEAN_GUAM_249,
            Items.CLEAN_MARRENTILL_251
        ), Items.HERB_TEA_MIX_4476
        ),
        HERB_TEA_MIX_8(listOf(Items.CLEAN_HARRALANDER_255, Items.CLEAN_MARRENTILL_251, Items.CLEAN_GUAM_249),
            Items.HERB_TEA_MIX_4478
        ),
        HERB_TEA_MIX_9(listOf(Items.CLEAN_GUAM_249, Items.CLEAN_GUAM_249, Items.CLEAN_MARRENTILL_251),
            Items.HERB_TEA_MIX_4480
        ),
        HERB_TEA_MIX_10(listOf(Items.CLEAN_GUAM_249, Items.CLEAN_GUAM_249, Items.CLEAN_HARRALANDER_255),
            Items.HERB_TEA_MIX_4482
        ),
        COMPLETE_MIX(listOf(
            Items.CLEAN_GUAM_249,
            Items.CLEAN_GUAM_249,
            Items.CLEAN_MARRENTILL_251,
            Items.CLEAN_HARRALANDER_255
        ), Items.GUTHIX_REST3_4419
        ),
    }

    override fun defineListeners() {
        val herbsArray = herbIds.toIntArray()
        val teaMixes = intArrayOf(
            Items.HERB_TEA_MIX_4464,
            Items.HERB_TEA_MIX_4466,
            Items.HERB_TEA_MIX_4468,
            Items.HERB_TEA_MIX_4470,
            Items.HERB_TEA_MIX_4472,
            Items.HERB_TEA_MIX_4474,
            Items.HERB_TEA_MIX_4476,
            Items.HERB_TEA_MIX_4478,
            Items.HERB_TEA_MIX_4480,
            Items.HERB_TEA_MIX_4482,
            Items.CUP_OF_HOT_WATER_4460
        )

        onUseWith(IntType.ITEM, herbsArray, *teaMixes) { player, used, base ->
            return@onUseWith handleMix(player, used.asItem(), base.asItem())
        }
    }

    private fun handleMix(player: Player, from: Item, to: Item): Boolean {
        if (!hasRequirement(player, Quests.DRUIDIC_RITUAL) || !hasRequirement(player, Quests.ONE_SMALL_FAVOUR)) {
            return false
        }

        if (getStatLevel(player, Skills.HERBLORE) < 18) {
            sendMessage(player, "You need a Herblore level of at least 18 to mix a Guthix Rest Tea.")
            return false
        }

        val (herb, mix) = if (isHerb(from)) from to to else to to from
        val existingIngredients = PartialTea.values().find { it.teaId == mix.id }?.ingredients?.toMutableList() ?: mutableListOf()
        existingIngredients.add(herb.id)

        val upgradedTea = PartialTea.values().find { it.ingredients.sorted() == existingIngredients.sorted() }
            ?: return player.sendMessage("Nothing interesting happens.").let { false }

        val slotToReplace = if (mix == from) from.slot else to.slot
        player.inventory.replace(Item(slotToReplace), upgradedTea.teaId)

        val herbSlot = if (herb == from) from.slot else to.slot
        player.inventory.remove(herb, herbSlot, true)

        sendMessage(
            player, "You place the ${
                herb.name.lowercase().replace(" leaf", "")
            } into the steamy mixture" + if (upgradedTea == PartialTea.COMPLETE_MIX) " and make Guthix Rest Tea." else "."
        )
        rewardXP(player, Skills.HERBLORE, 13.5 + existingIngredients.size * 0.5)
        return true
    }

    private fun isHerb(item: Item): Boolean = item.id in herbIds
}
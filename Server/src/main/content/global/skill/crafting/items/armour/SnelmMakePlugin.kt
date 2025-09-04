package content.global.skill.crafting.items.armour

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import shared.consts.Items

class SnelmMakePlugin : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.CHISEL_1755, *SnelmItem.SHELLS) { player, _, used ->
            val snelmId = SnelmItem.fromShellId(used.id) ?: return@onUseWith true

            if (!hasLevelDyn(player, Skills.CRAFTING, 15)) {
                sendMessage(player, "You need a crafting level of at least 15 to do this.")
                return@onUseWith true
            }

            val available = amountInInventory(player, used.id)
            if (available < 1) {
                sendMessage(player, "You do not have enough ${getItemName(used.id).lowercase()} to craft this.")
                return@onUseWith true
            }

            if (available == 1) {
                if (removeItem(player, snelmId.shell)) {
                    addItem(player, snelmId.product)
                    rewardXP(player, Skills.CRAFTING, 32.5)
                    sendMessage(player, "You craft the shell into a helmet.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(snelmId.product)
                create { _, amount ->
                    runTask(player, 1, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, snelmId.shell)) {
                            addItem(player, snelmId.product)
                            rewardXP(player, Skills.CRAFTING, 32.5)
                            sendMessage(player, "You craft the shell into a helmet.")
                        }
                    }
                }
                calculateMaxAmount { available }
            }

            return@onUseWith true
        }
    }
}

private enum class SnelmItem(val shell: Int, val product: Int) {
    MYRE_ROUNDED(Items.BLAMISH_MYRE_SHELL_3345, Items.MYRE_SNELM_3327),
    MYRE_POINTED(Items.BLAMISH_MYRE_SHELL_3355, Items.MYRE_SNELM_3337),
    BLOOD_ROUNDED(Items.BLAMISH_RED_SHELL_3347, Items.BLOODNTAR_SNELM_3329),
    BLOOD_POINTED(Items.BLAMISH_RED_SHELL_3357, Items.BLOODNTAR_SNELM_3339),
    OCHRE_ROUNDED(Items.BLAMISH_OCHRE_SHELL_3349, Items.OCHRE_SNELM_3331),
    OCHRE_POINTED(Items.BLAMISH_OCHRE_SHELL_3359, Items.OCHRE_SNELM_3341),
    BRUISE_ROUNDED(Items.BLAMISH_BLUE_SHELL_3351, Items.BRUISE_BLUE_SNELM_3333),
    BRUISE_POINTED(Items.BLAMISH_BLUE_SHELL_3361, Items.BRUISE_BLUE_SNELM_3343),
    BROKEN_ROUNDED(Items.BLAMISH_BARK_SHELL_3353, Items.BROKEN_BARK_SNELM_3335);

    companion object {
        private val shellMap = SnelmItem.values().associateBy { it.shell }
        val SHELLS = SnelmItem.values().map { it.shell }.toIntArray()
        fun fromShellId(id: Int): SnelmItem? = shellMap[id]
    }
}

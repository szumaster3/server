package content.global.skill.crafting.items.armour

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items

class SnelmItemCraftingPlugin : InteractionListener {

    private val shellIDs = Snelm.values().map(Snelm::shell).toIntArray()

    override fun defineListeners() {

        /*
         * Handles snelm helmet crafting.
         */

        onUseWith(IntType.ITEM, shellIDs, Items.CHISEL_1755) { player, used, _ ->
            val item = Snelm.fromShellId(used.id) ?: return@onUseWith true

            if (getStatLevel(player, Skills.CRAFTING) < 15) {
                sendDialogue(player, "You need a crafting level of at least 15 in order to do this.")
                return@onUseWith false
            }

            queueScript(player, 1, QueueStrength.SOFT) {
                sendMessage(player, "You craft the shell into a helmet.")
                if(removeItem(player, Item(item.shell, 1), Container.INVENTORY)){
                    addItem(player, item.product, 1, Container.INVENTORY)
                    rewardXP(player, Skills.CRAFTING, 32.5)
                }
                return@queueScript stopExecuting(player)
            }

            return@onUseWith true
        }
    }
}

private enum class Snelm(val shell: Int, val product: Int, ) {
    MYRE_ROUNDED(Items.BLAMISH_MYRE_SHELL_3345, Items.MYRE_SNELM_3327),
    MYRE_POINTED(Items.BLAMISH_MYRE_SHELL_3355, Items.MYRE_SNELM_3337),
    BLOOD_ROUNDED(Items.BLAMISH_RED_SHELL_3347, Items.BLOODNTAR_SNELM_3329),
    BLOOD_POINTED(Items.BLAMISH_RED_SHELL_3357, Items.BLOODNTAR_SNELM_3339),
    OCHRE_ROUNDED(Items.BLAMISH_OCHRE_SHELL_3349, Items.OCHRE_SNELM_3331),
    OCHRE_POINTED(Items.BLAMISH_OCHRE_SHELL_3359, Items.OCHRE_SNELM_3341),
    BRUISE_ROUNDED(Items.BLAMISH_BLUE_SHELL_3351, Items.BRUISE_BLUE_SNELM_3333),
    BRUISE_POINTED(Items.BLAMISH_BLUE_SHELL_3361, Items.BRUISE_BLUE_SNELM_3343),
    BROKEN_ROUNDED(Items.BLAMISH_BARK_SHELL_3353, Items.BROKEN_BARK_SNELM_3335),
    ;

    companion object {
        private val shellToSnelmMap: Map<Int, Snelm> = values().associateBy { it.shell }
        fun fromShellId(shellId: Int): Snelm? = shellToSnelmMap[shellId]
    }
}
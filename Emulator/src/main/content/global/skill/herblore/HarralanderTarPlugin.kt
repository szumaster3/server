package content.global.skill.herblore

import core.api.*
import core.api.quest.isQuestComplete
import core.api.ui.repositionChild
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests

class HarralanderTarPlugin : InteractionListener {
    val tar = TarItem.values().map(TarItem::ingredient).toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, tar, Items.SWAMP_TAR_1939) { player, used, _ ->
            var tar = TarItem.forId(used.id)
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(tar!!.product)) {
                    override fun create(
                        amount: Int,
                        index: Int,
                    ) {
                        player.pulseManager.run(
                            tar?.let { HarralanderTarPulse(player, null, it, amount) },
                        )
                    }

                    override fun getAll(index: Int): Int = amountInInventory(player, used.id)
                }
            handler.open()
            repositionChild(player, Components.SKILL_MULTI1_309, 2, 210, 15)
            return@onUseWith true
        }
    }
}

private class HarralanderTarPulse(player: Player?, node: Item?, val tar: TarItem, private var amount: Int, ) : SkillPulse<Item?>(player, node) {

    override fun checkRequirements(): Boolean {
        if (!isQuestComplete(player, Quests.DRUIDIC_RITUAL)) {
            sendMessage(player, "You must complete the ${Quests.DRUIDIC_RITUAL} quest before you can use Herblore.")
            return false
        }
        if (getDynLevel(player, Skills.HERBLORE) < tar.level) {
            sendMessage(player, "You need a Herblore level of at least " + tar.level + " in order to do this.")
            return false
        }
        if (!inInventory(player, PESTLE_AND_MORTAR)) {
            sendMessage(player, "You need pestle and mortar in order to crush the herb.")
            return false
        }
        if (!inInventory(player, Items.SWAMP_TAR_1939, 15)) {
            sendMessage(player, "You need at least 15 swamp tar in order to do this.")
            return false
        }
        return true
    }

    override fun animate() {
        animate(player, ANIMATION)
    }

    override fun reward(): Boolean {
        if (delay == 1) {
            delay = 4
            return false
        }

        val hasRequiredItems = inInventory(player, Items.SWAMP_TAR_1939, 15) &&
                inInventory(player, tar.ingredient)

        if (!hasRequiredItems ||
            !removeItem(player, Item(Items.SWAMP_TAR_1939, 15)) ||
            !removeItem(player, tar.ingredient)
        ) {
            return true
        }

        addItem(player, tar.product, 15)
        rewardXP(player, Skills.HERBLORE, tar.experience)

        val ingredientName = getItemName(tar.ingredient)
            .lowercase()
            .replace("clean", "")
            .trim()

        sendMessage(player, "You add the $ingredientName to the swamp tar.")

        amount--
        return amount == 0
    }

    override fun message(type: Int) {}

    companion object {
        private const val ANIMATION = Animations.PESTLE_MORTAR_364
        private const val PESTLE_AND_MORTAR = Items.PESTLE_AND_MORTAR_233
    }
}

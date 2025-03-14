package content.global.skill.herblore.potions

import content.global.skill.herblore.HerblorePulse
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class FinishedPotionHandler : UseWithHandler(*unfinishedItems) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (potion in FinishedPotion.values()) {
            addHandler(potion.ingredient.id, ITEM_TYPE, this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val finished: FinishedPotion =
            FinishedPotion.getPotion(
                if (event.usedItem.name.contains("(unf)")) event.usedItem else event.baseItem,
                if (event.usedItem.name.contains("(unf)")) event.baseItem else event.usedItem,
            ) ?: return false
        val potion: GenericPotion = GenericPotion.transform(finished)
        val player = event.player
        val handler: SkillDialogueHandler =
            object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, potion.product!!) {
                override fun create(
                    amount: Int,
                    index: Int,
                ) {
                    player.pulseManager.run(HerblorePulse(player, potion.base, amount, potion))
                }

                override fun getAll(index: Int): Int {
                    return player.inventory.getAmount(potion.base)
                }
            }
        if (player.inventory.getAmount(potion.base) == 1) {
            handler.create(0, 1)
        } else {
            handler.open()
        }
        return true
    }

    companion object {
        val unfinishedItems: IntArray
            get() {
                val ids = IntArray(UnfinishedPotion.values().size)
                var counter = 0
                for (potion in UnfinishedPotion.values()) {
                    ids[counter] = potion.potion.id
                    counter++
                }
                return ids
            }
    }
}

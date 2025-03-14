package content.global.skill.herblore.potions

import content.global.skill.herblore.HerblorePulse
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class UnfinishedPotionHandler : UseWithHandler(Items.VIAL_OF_WATER_227, Items.COCONUT_MILK_5935) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (potion in UnfinishedPotion.values()) {
            addHandler(potion.ingredient.id, ITEM_TYPE, this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val unf: UnfinishedPotion = UnfinishedPotion.forItem(event.usedItem, event.baseItem) ?: return false
        val potion: GenericPotion = GenericPotion.transform(unf)
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
}

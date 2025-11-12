package content.minigame.gnomecooking.plugin.cocktails

import core.api.inInventory
import core.api.sendDialogue
import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items

private val mixers = arrayOf(
    Items.MIXED_BLIZZARD_9566,
    Items.MIXED_SGG_9567,
    Items.MIXED_BLAST_9568,
    Items.MIXED_PUNCH_9569,
    Items.MIXED_BLURBERRY_SPECIAL_9570,
    Items.MIXED_SATURDAY_9571,
    Items.MIXED_DRAGON_9574
)

/**
 * Handles the pouring of mixed drinks into cocktail glasses.
 *
 * @author Ceikry
 */
@Initializable
class PourMixerPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (mixer in mixers) {
            ItemDefinition.forId(mixer).handlers["option:pour"] = this
        }
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        when (node.id) {
            Items.MIXED_BLIZZARD_9566 -> attemptMake(PouredDrink.WIZ_BLIZZ, player, node)
            Items.MIXED_SGG_9567 -> attemptMake(PouredDrink.SHORT_G_G, player, node)
            Items.MIXED_BLAST_9568 -> attemptMake(PouredDrink.FRUIT_BLAST, player, node)
            Items.MIXED_PUNCH_9569 -> attemptMake(PouredDrink.PINE_PUNCH, player, node)
            Items.MIXED_BLURBERRY_SPECIAL_9570 -> attemptMake(PouredDrink.BLUR_SPEC, player, node)
            Items.MIXED_SATURDAY_9571 -> attemptMake(PouredDrink.CHOC_SAT, player, node)
            Items.MIXED_DRAGON_9574 -> attemptMake(PouredDrink.DRUNK_DRAG, player, node)
        }
        return true
    }

    private fun attemptMake(drink: PouredDrink, player: Player, node: Node) {
        if (!inInventory(player,Items.COCKTAIL_GLASS_2026)) {
            sendDialogue(player, "You need a glass to pour this into.")
            return
        }

        var hasAll = true
        for (ingredient in drink.requiredItems) {
            if (!player.inventory.containsItem(ingredient)) {
                hasAll = false
            }
        }

        if (!hasAll) {
            sendDialogue(player, "You don't have the garnishes for this.")
            return
        }

        player.inventory.remove(*drink.requiredItems)
        player.inventory.remove(node.asItem())
        player.inventory.remove(Item(Items.COCKTAIL_GLASS_2026))
        player.inventory.add(Item(drink.product))
        player.inventory.add(Item(Items.COCKTAIL_SHAKER_2025))
        player.skills.addExperience(Skills.COOKING, 50.0)
    }

    internal enum class PouredDrink(val product: Int, val requiredItems: Array<Item>) {
        FRUIT_BLAST(Items.FRUIT_BLAST_9514, arrayOf(Item(Items.LEMON_SLICES_2106))),
        PINE_PUNCH(Items.PINEAPPLE_PUNCH_9512, arrayOf(Item(Items.LIME_CHUNKS_2122), Item(Items.PINEAPPLE_CHUNKS_2116), Item(Items.ORANGE_SLICES_2112))),
        WIZ_BLIZZ(Items.WIZARD_BLIZZARD_9508, arrayOf(Item(Items.PINEAPPLE_CHUNKS_2116), Item(Items.LIME_SLICES_2124))),
        SHORT_G_G(Items.SHORT_GREEN_GUY_9510, arrayOf(Item(Items.LIME_SLICES_2124), Item(Items.EQUA_LEAVES_2128))),
        DRUNK_DRAG(Items.MIXED_DRAGON_9575, arrayOf()),
        CHOC_SAT(Items.MIXED_SATURDAY_9572, arrayOf()),
        BLUR_SPEC(Items.BLURBERRY_SPECIAL_9520, arrayOf(Item(Items.LEMON_CHUNKS_2104), Item(Items.ORANGE_CHUNKS_2110), Item(Items.EQUA_LEAVES_2128), Item(Items.LIME_SLICES_2124))
        ),
    }

}
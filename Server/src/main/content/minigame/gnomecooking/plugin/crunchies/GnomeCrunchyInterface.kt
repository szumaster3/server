package content.minigame.gnomecooking.plugin.crunchies

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items

/**
 * Handles the gnome crunchy interface.
 *
 * @author Ceikry
 */
@Initializable
class GnomeCrunchyInterface : ComponentPlugin() {

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        player ?: return false
        when (button) {
            3 -> attemptMake(HalfMadeCrunchy.TOAD, player)
            10 -> attemptMake(HalfMadeCrunchy.SPICY, player)
            17 -> attemptMake(HalfMadeCrunchy.WORM, player)
            26 -> attemptMake(HalfMadeCrunchy.CHOCCHIP, player)
        }
        return true
    }

    private fun attemptMake(crunchy: HalfMadeCrunchy, player: Player) {
        var hasAll = true

        if (player.skills.getLevel(Skills.COOKING) < crunchy.reqLevel) {
            player.dialogueInterpreter.sendDialogue("You don't have the required level to make these.")
            return
        }

        if (!player.inventory.containsItem(Item(Items.GNOME_SPICE_2169))) {
            player.dialogueInterpreter.sendDialogue("You need some gnome spice to make these.")
            return
        }

        for (item in crunchy.requiredItems) {
            if (!player.inventory.containsItem(item)) {
                hasAll = false
                break
            }
        }

        if (!hasAll) {
            player.dialogueInterpreter.sendDialogue("You don't have the required ingredients to make these.")
            return
        }

        player.inventory.remove(*crunchy.requiredItems)
        player.inventory.remove(Item(Items.HALF_BAKED_CRUNCHY_2201))
        player.inventory.add(Item(crunchy.product))
        player.skills.addExperience(Skills.COOKING, 30.0)
        player.interfaceManager.close()
    }

    override fun open(player: Player?, component: Component?) {
        player ?: return
        component ?: return
        super.open(player, component)

        player.packetDispatch.sendItemOnInterface(Items.TOAD_CRUNCHIES_9538, 1, component.id, 3)
        player.packetDispatch.sendItemOnInterface(Items.SPICY_CRUNCHIES_9540, 1, component.id, 10)
        player.packetDispatch.sendItemOnInterface(Items.WORM_CRUNCHIES_9542, 1, component.id, 17)
        player.packetDispatch.sendItemOnInterface(Items.CHOCCHIP_CRUNCHIES_9544, 1, component.id, 26)
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(437, this)
        return this
    }

    internal enum class HalfMadeCrunchy(val product: Int, val reqLevel: Int, val requiredItems: Array<Item>) {
        CHOCCHIP(Items.HALF_MADE_CRUNCHY_9577, 16, arrayOf(Item(Items.CHOCOLATE_BAR_1973, 2))),
        SPICY(Items.HALF_MADE_CRUNCHY_9579, 12, arrayOf(Item(Items.EQUA_LEAVES_2128, 2))),
        TOAD(Items.HALF_MADE_CRUNCHY_9581, 10, arrayOf(Item(Items.TOADS_LEGS_2152, 2))),
        WORM(Items.HALF_MADE_CRUNCHY_9583, 14, arrayOf(Item(Items.EQUA_LEAVES_2128), Item(Items.KING_WORM_2162, 2)))
    }

}
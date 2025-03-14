package content.global.handlers.item

import core.cache.def.impl.ItemDefinition
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class GnomeCopterPlugin : Plugin<Any> {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(Items.GNOMECOPTER_12842).handlers["equipment"] = this
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any {
        val player = args[0] as Player
        val item = args[1] as Item
        when (identifier) {
            "equip" -> {}
            "unequip" -> {
                if (item.id == Items.GNOMECOPTER_12842) {
                    player.equipment.remove(item, EquipmentContainer.SLOT_WEAPON, true)
                    return false
                }
            }
        }
        return false
    }
}

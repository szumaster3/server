package content.global.handlers.iface

import core.api.getAttribute
import core.api.removeAttribute
import core.game.interaction.InterfaceListener
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import org.rs.consts.Components

class AutoCastInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.STAFF_SPELLS_319) { player, _, _, buttonID, _, _ ->
            autoCast(player, buttonID)
            return@on true
        }
        on(Components.SLAYER_STAFF_SPELLS_310) { player, _, _, buttonID, _, _ ->
            autoCast(player, buttonID)
            return@on true
        }
        on(Components.PEST_MACE_406) { player, _, _, buttonID, _, _ ->
            autoCast(player, buttonID)
            return@on true
        }
        on(Components.ANCIENT_STAFF_SPELLS_797) { player, _, _, buttonID, _, _ ->
            autoCast(player, buttonID)
            return@on true
        }
    }

    private fun autoCast(
        player: Player,
        button: Int,
    ) {
        if (!getAttribute(player, "autocast_select", false)) return
        removeAttribute(player, "autocast_select")
        val w = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
        if (w != null) {
            w.selectAutoSpell(button, true)
            player.interfaceManager.openTab(w)
        }
    }
}

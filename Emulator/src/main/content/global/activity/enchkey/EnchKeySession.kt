package content.global.activity.enchkey

import core.api.*
import core.game.node.entity.player.Player
import org.rs.consts.Items

class EnchKeySession : LogoutListener {
    override fun logout(player: Player) {
        val session = player.getAttribute<EnchKeySession>("enchanted-key", null) ?: return
        removeAttribute(player, session.toString())
        clearLogoutListener(player, "enchanted-key")
    }

    fun startActivity(player: Player) {
        if (getVarbit(player, 1390) == 1 && inInventory(player, Items.ENCHANTED_KEY_6754)) {
            registerLogoutListener(player, "enchanted-key") { pl ->
                removeItem(pl, Items.ENCHANTED_KEY_6754, Container.INVENTORY)
            }
        }
    }
}

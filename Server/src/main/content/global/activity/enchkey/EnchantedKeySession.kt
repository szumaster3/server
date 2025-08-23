package content.global.activity.enchkey

import core.api.*
import core.game.node.entity.player.Player
import shared.consts.Items
import shared.consts.Vars

/**
 * Represents a session for the Enchanted Key activity.
 * @author szu
 */
class EnchantedKeySession : LogoutListener {

    /**
     * Called when a player logs out.
     */
    override fun logout(player: Player) {
        val session = player.getAttribute<EnchantedKeySession>("enchanted-key", null) ?: return
        removeAttribute(player, session.toString())
        clearLogoutListener(player, "enchanted-key")
    }

    /**
     * Starts the activity for the given player.
     * @param player the player
     */
    fun startActivity(player: Player) {
        if (getVarbit(player, Vars.VARBIT_MAKING_HISTORY_MUSEUM_BUILT_1390) == 1 && inInventory(player, Items.ENCHANTED_KEY_6754)) {
            registerLogoutListener(player, "enchanted-key") { pl ->
                removeItem(pl, Items.ENCHANTED_KEY_6754, Container.INVENTORY)
            }
        }
    }
}

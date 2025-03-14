package content.data

import core.ServerStore
import core.api.hasAnItem
import core.api.sendMessage
import core.game.node.entity.player.Player
import org.rs.consts.Items

object GlobalStore {
    @JvmStatic
    fun check(player: Player) {
        val ringStore = ServerStore.getArchive("daily-explorer-ring")
        val hasExplorerRing =
            hasAnItem(
                player,
                Items.EXPLORERS_RING_1_13560,
                Items.EXPLORERS_RING_2_13561,
                Items.EXPLORERS_RING_3_13562,
            ).container != null
        if (hasExplorerRing &&
            ringStore[player.username.lowercase() + "run"] != 0 ||
            ringStore[player.username.lowercase() + "alchs"] == 30
        ) {
            sendMessage(player, "Your explorer's ring has been recharged.")
        }
    }
}

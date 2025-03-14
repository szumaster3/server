package content.minigame.shadesofmorton

import core.api.inInventory
import core.game.node.entity.player.Player
import org.rs.consts.Items

class ShadowOfMortonMinigame {
    fun getLogTypeFromInventory(player: Player): LogType? {
        return when {
            inInventory(player, Items.LOGS_1511) -> LogType.REGULAR
            inInventory(player, Items.OAK_LOGS_1521) -> LogType.OAK
            inInventory(player, Items.WILLOW_LOGS_1519) -> LogType.WILLOW
            inInventory(player, Items.TEAK_LOGS_6333) -> LogType.TEAK
            inInventory(player, Items.ARCTIC_PINE_LOGS_10810) -> LogType.ARCTIC_PINE
            inInventory(player, Items.MAPLE_LOGS_1517) -> LogType.MAPLE
            inInventory(player, Items.MAHOGANY_LOGS_6332) -> LogType.MAHOGANY
            inInventory(player, Items.EUCALYPTUS_LOGS_12581) -> LogType.EUCALYPTUS
            inInventory(player, Items.YEW_LOGS_1515) -> LogType.YEW
            inInventory(player, Items.MAGIC_LOGS_1513) -> LogType.MAGIC
            else -> null
        }
    }
}

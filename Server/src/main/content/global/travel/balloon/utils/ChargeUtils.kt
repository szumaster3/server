package content.global.travel.balloon.utils
import content.global.travel.balloon.BalloonDefinition
import core.api.amountInInventory
import core.api.getVarbit
import core.api.removeItem
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.item.Item

object ChargeUtils {
    private const val SANDBAG_VARBIT = 2880
    private const val LOGS_VARBIT = 2881
    private const val STORAGE_CAPACITY = 4000

    fun consumeLogs(
        player: Player,
        destination: BalloonDefinition
    ): Boolean {
        return removeItem(
            player,
            Item(destination.logId, destination.logCost)
        )
    }

    fun consumeCharges(
        player: Player,
        destination: BalloonDefinition
    ): Boolean {
        val cost = destination.chargeCost
        val current = getCharges(player)

        if (current < cost) return false

        setVarbit(player, LOGS_VARBIT, current - cost, true)
        return true
    }

    fun getCharges(player: Player): Int =
        getVarbit(player, LOGS_VARBIT)

    fun addCharges(player: Player, amount: Int) {
        if (amount <= 0) return
        setVarbit(player, LOGS_VARBIT, getCharges(player) + amount, true)
    }

    private fun removeAllLogs(player: Player): Int {
        var total = 0

        BalloonDefinition.values().forEach { def ->
            val amount = amountInInventory(player, def.logId)
            if (amount > 0) {
                removeItem(player, Item(def.logId, amount))
                total += amount
            }
        }

        return total
    }

    fun handOverLogs(player: Player): Int {
        val removed = removeAllLogs(player)

        if (removed > 0) {
            addCharges(player, removed)
        }

        return removed
    }
}
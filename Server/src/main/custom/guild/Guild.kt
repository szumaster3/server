package custom.guild

import custom.guild.warehouse.GuildWarehouse

class Guild(val id: Int, var level: Int, var gold: Long, val members: MutableMap<Int, GuildRank>) {
    /**
     * Shared warehouse for all guild members.
     */
    val warehouse = GuildWarehouse(this)

    /**
     * The guild upgrade.
     */
    fun levelUp() {
        level++
        warehouse.expand()
    }
}
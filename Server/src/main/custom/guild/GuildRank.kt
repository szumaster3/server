package custom.guild

enum class GuildRank {
    MEMBER, VETERAN, CAPTAIN, GUILDMASTER;

    fun canWithdraw(): Boolean = this == VETERAN || this == CAPTAIN || this == GUILDMASTER
}
package content.guilds

import core.api.hasRequirement
import core.game.node.entity.player.Player
import shared.consts.Quests

enum class Guilds {
    Misthalin,
    Asgarnia,
    Kandarin,
    Morytania;


    companion object {
        /**
         * Checks if a player is in any guild.
         */
        fun inGuild(player: Player): Boolean {
            return Guilds.values().any { guild ->
                player.getAttribute("guild_member-${guild.name}", false)
            }
        }
    }

    /**
     * Checks if the player meets the requirements to join guild.
     */
    fun checkRequirements(player: Player): Boolean {
        return when (this) {
            Misthalin ->
                hasRequirement(player, Quests.DRAGON_SLAYER)

            Asgarnia ->
                hasRequirement(player, Quests.THE_SLUG_MENACE)

            Kandarin ->
                hasRequirement(player, Quests.BIOHAZARD)

            Morytania ->
                hasRequirement(player, Quests.GHOSTS_AHOY)

        }
    }
}


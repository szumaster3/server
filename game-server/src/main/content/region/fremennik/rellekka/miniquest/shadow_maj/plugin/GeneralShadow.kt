package content.region.fremennik.rellekka.miniquest.shadow_maj.plugin

import core.api.allInEquipment
import core.api.getVarbit
import core.api.setVarbit
import core.game.node.entity.player.Player
import org.rs.consts.Items
import org.rs.consts.Vars

/**
 * General shadow mini-quest
 * ```
 * Varbit id: 3330
 * Varp Id: 976
 * startBit: 0, endBit: 5, mask: 63
 * ```
 */
object GeneralShadow {
    const val GS_TRUSTWORTHY = "/save:general-shadow:trustworthy"
    const val GS_RECEIVED_NOTE = "/save:general-shadow:sin-seer-note"
    const val GS_RECEIVED_SEVERED_LEG = "/save:general-shadow:bouncer"


    /**
     * Checks if the player has all the required items to start the mini-quest.
     *
     * @param player The player whose equipment is being checked.
     * @return `true` if the player has all the required items, `false` otherwise.
     */
    fun hasGhostlySet(player: Player): Boolean {
        return allInEquipment(
            player,
            Items.GHOSTLY_CLOAK_6111,
            Items.GHOSTLY_GLOVES_6110,
            Items.GHOSTLY_HOOD_6109,
            Items.GHOSTLY_ROBE_6107,
            Items.GHOSTLY_ROBE_6108,
            Items.GHOSTSPEAK_AMULET_552
        )
    }

    /**
     * Retrieves the current progress of the general shadow mini-quest for a player.
     *
     * @param player The player whose progress is being checked.
     * @return The progress value of the general shadow mini-quest (an integer).
     */
    fun getShadowProgress(player: Player): Int {
        return getVarbit(player, Vars.VARBIT_THE_GENERAL_S_SHADOW_PROGRESS_3330)
    }

    /**
     * Sets the progress of the general shadow mini-quest for a player.
     *
     * @param player The player whose progress is being updated.
     * @param progress The new progress value to be set (an integer).
     */
    fun setShadowProgress(player: Player, progress: Int) {
        setVarbit(player, Vars.VARBIT_THE_GENERAL_S_SHADOW_PROGRESS_3330, progress, true)
        player.debug("[GeneralShadow] progress updated to [${core.tools.DARK_GREEN}$progress</col>]")
    }

    /**
     * Checks if the general shadow mini-quest is complete for the player.
     *
     * @param player The player whose quest completion status is being checked.
     * @return `true` if the mini-quest is complete, `false` otherwise.
     */
    fun isComplete(player: Player): Boolean {
        return getShadowProgress(player) == 5
    }
}

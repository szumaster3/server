package content.region.desert.bedabin.quest.deserttreasure

import core.api.getAttribute
import core.api.getVarbit
import core.api.setAttribute
import core.api.spawnProjectile
import core.game.node.entity.player.Player
import core.game.world.map.Location

/**
 * Utility functions for the Desert Treasure quest.
 */
object DTUtils {
    /**
     * Checks if the player has the required items for [EblisDialogue].
     *
     * @param player The player.
     * @return `true` if the player has all required items, `false` otherwise.
     */
    fun checkGivenItem(player: Player): Boolean {
        val requiredItems =
            mapOf(
                DesertTreasure.magicLogsAmount to 12,
                DesertTreasure.steelBarsAmount to 6,
                DesertTreasure.moltenGlassAmount to 6,
                DesertTreasure.bonesAmount to 1,
                DesertTreasure.ashesAmount to 1,
                DesertTreasure.charcoalAmount to 1,
                DesertTreasure.bloodRunesAmount to 1,
            )

        return requiredItems.all { (attribute, requiredAmount) ->
            getAttribute(player, attribute, 0) >= requiredAmount
        }
    }

    /**
     * Checks if all diamonds have been inserted by the player.
     *
     * @param player The player.
     * @return `true` if the player has inserted all the diamonds, `false` otherwise.
     */
    fun allDiamondsInserted(player: Player): Boolean {
        val attributes =
            listOf(
                DesertTreasure.bloodDiamond,
                DesertTreasure.smokeDiamond,
                DesertTreasure.iceDiamond,
                DesertTreasure.shadowDiamond,
            )

        return attributes.all { getAttribute(player, it, 0) == 1 }
    }

    /**
     * Retrieves the subStage value for a given attribute name.
     *
     * @param player The player.
     * @param attributeName The attribute name.
     * @return The value of the subStage for the given attribute.
     */
    fun getSubStage(
        player: Player,
        attributeName: String,
    ): Int = getAttribute(player, attributeName, 0)

    /**
     * Sets the subStage value for a given attribute name.
     *
     * @param player The player.
     * @param attributeName The attribute name.
     * @param value The value to set.
     */
    fun setSubStage(
        player: Player,
        attributeName: String,
        value: Int,
    ) = setAttribute(player, attributeName, value)

    /**
     * Checks if all substages of the Desert Treasure quest have been completed.
     *
     * @param player The player.
     * @return `true` if all substages are completed (100), `false` otherwise.
     */
    fun completedAllSubStages(player: Player): Boolean {
        val stages =
            listOf(
                DesertTreasure.bloodStage,
                DesertTreasure.smokeStage,
                DesertTreasure.iceStage,
                DesertTreasure.shadowStage,
            )
        return stages.all { getSubStage(player, it) == 100 }
    }

    /**
     * Spawns projectiles around a base location.
     */
    fun spawnProjectiles() {
        val baseLocation = Location(3570, 3402)
        val offsets =
            listOf(
                Location(3570, 3404),
                Location(3570, 3400),
                Location(3568, 3402),
                Location(3572, 3402),
                Location(3568, 3404),
                Location(3572, 3404),
                Location(3568, 3400),
                Location(3572, 3400),
            )

        offsets.forEach { targetLocation ->
            spawnProjectile(baseLocation, targetLocation, 350, 0, 0, 0, 60, 0)
        }
    }

    /**
     * Checks if all torches in have been lit.
     *
     * @param player The player.
     * @return `true` if all the torches are lit, `false` otherwise.
     */
    fun checkAllTorchesLit(player: Player): Boolean {
        val torches =
            listOf(
                DesertTreasure.varbitStandingTorchNorthEast,
                DesertTreasure.varbitStandingTorchSouthEast,
                DesertTreasure.varbitStandingTorchSouthWest,
                DesertTreasure.varbitStandingTorchNorthWest,
            )

        return torches.all { getVarbit(player, it) == 1 }
    }
}

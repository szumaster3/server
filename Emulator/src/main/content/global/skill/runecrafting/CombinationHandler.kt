package content.global.skill.runecrafting

import content.global.skill.runecrafting.Altar.forScenery
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.scenery.Scenery
import core.plugin.Plugin

/**
 * Handles the combination of talismans and runes.
 *
 * @constructor Creates a new CombinationHandler instance with predefined item ids for talismans and runes.
 */
class CombinationHandler :
    UseWithHandler(
        Talisman.AIR.item.id,
        Talisman.WATER.item.id,
        Talisman.EARTH.item.id,
        Talisman.FIRE.item.id,
        Rune.WATER.rune.id,
        Rune.EARTH.rune.id,
        Rune.AIR.rune.id,
        Rune.FIRE.rune.id,
    ) {

    /**
     * Initializes the CombinationHandler by adding it as a handler for specific altar scenery objects.
     *
     * @param arg Optional argument passed to the new instance. Defaults to `null`.
     * @return A new instance of the CombinationHandler plugin.
     */
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (altar in Altar.values()) {
            addHandler(altar.scenery, OBJECT_TYPE, this)
        }
        return this
    }

    /**
     * Handles the combination of talismans, runes, and altars when a player interacts with the scenery.
     * This triggers the creation of a combination rune if the appropriate conditions are met.
     *
     * @param event The event representing the player's interaction with the scenery and items.
     * @return `true` if the combination is successful and the pulse is initiated; `false` otherwise.
     */
    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val altar = forScenery((event.usedWith as Scenery))
        val combo = CombinationRune.forAltar(altar!!, event.usedItem) ?: return false
        player.pulseManager.run(RunecraftPulse(player, event.usedItem, altar, true, combo))
        return true
    }
}

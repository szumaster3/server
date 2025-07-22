package content.global.skill.runecrafting

import content.global.skill.runecrafting.Altar.forScenery
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.scenery.Scenery
import core.plugin.Plugin

/**
 * Handles the combination of talismans and runes.
 */
class CombinationHandler :
    UseWithHandler(Talisman.AIR.item.id, Talisman.WATER.item.id, Talisman.EARTH.item.id, Talisman.FIRE.item.id, Rune.WATER.rune.id, Rune.EARTH.rune.id, Rune.AIR.rune.id, Rune.FIRE.rune.id) {

    override fun newInstance(arg: Any?): Plugin<Any> {
        for (altar in Altar.values()) {
            addHandler(altar.scenery, OBJECT_TYPE, this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val altar = forScenery((event.usedWith as Scenery))
        val combo = CombinationRune.forAltar(altar!!, event.usedItem) ?: return false
        player.pulseManager.run(RunecraftPulse(player, event.usedItem, altar, true, combo))
        return true
    }
}

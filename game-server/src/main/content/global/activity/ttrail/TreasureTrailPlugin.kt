package content.global.activity.ttrail

import content.global.activity.ttrail.npc.SaradominWizardNPC
import content.global.activity.ttrail.npc.ZamorakWizardNPC
import content.global.activity.ttrail.plugin.*
import core.api.sendMessage
import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.node.item.ItemPlugin
import core.game.world.map.Location
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Plugin handling interactions with clue scrolls and caskets in Treasure Trails.
 */
@Initializable
class TreasureTrailPlugin : OptionHandler() {

    /**
     * Registers handlers for clue scrolls, caskets, and related NPCs.
     */
    override fun newInstance(arg: Any?): Plugin<Any> {
        IDS.forEach { id ->
            ItemDefinition.forId(id).handlers["option:read"] = this
        }

        ClueLevel.values().forEach { level ->
            ItemDefinition.forId(level.casketId).handlers["option:open"] = this
        }

        ItemDefinition.forId(CoordinateScroll.SEXTANT.id).handlers["option:look through"] = this

        ClassScanner.definePlugin(MapClue())
        ClassScanner.definePlugin(ClueItemPlugin())
        ClassScanner.definePlugin(ChallengeScroll())
        ClassScanner.definePlugin(CrypticClue())
        ClassScanner.definePlugin(Anagram())
        ClassScanner.definePlugin(EmoteClue())
        ClassScanner.definePlugin(CoordinateClue())
        ClassScanner.definePlugin(SaradominWizardNPC())
        ClassScanner.definePlugin(ZamorakWizardNPC())

        return this
    }

    /**
     * Handles read clue scrolls and open caskets.
     */
    override fun handle(player: Player, node: Node, option: String): Boolean {
        player.lock(1)
        when (option) {
            "read" -> {
                val clue = ClueScroll.getClueScrolls()[node.id]
                if (clue == null) {
                    player.debug("Unused clue scroll item! Removed.")
                    player.inventory.remove(node as Item)
                    return false
                }
                clue.read(player)
            }
            "open" -> {
                if (!player.inventory.containsItem(node.asItem())) return true
                ClueLevel.open(player, node as Item)
            }
        }
        return true
    }

    /**
     * Prevents walking while using the option.
     */
    override fun isWalk(): Boolean = false

    /**
     * Managing item behavior for clue scrolls when dropped or picked up.
     */
    inner class ClueItemPlugin : ItemPlugin() {

        /**
         * Registers clue scroll ids.
         */
        override fun newInstance(arg: Any?): Plugin<Any> {
            register(*IDS)
            return this
        }

        /**
         * Prevents pickup if player already has a clue scroll.
         */
        override fun canPickUp(player: Player, item: GroundItem, type: Int): Boolean {
            return if (hasClue(player)) {
                sendMessage(player, "You can only have one clue scroll at a time.")
                false
            } else true
        }

        /**
         * Prevents dropping if player has a clue scroll.
         */
        override fun createDrop(item: Item, player: Player, npc: NPC?, location: Location): Boolean {
            return !hasClue(player)
        }

        /**
         * Returns the item unchanged.
         */
        override fun getItem(item: Item, npc: NPC?): Item = item

        /**
         * Checks if player currently holds a clue scroll.
         */
        private fun hasClue(player: Player): Boolean {
            return TreasureTrailManager.getInstance(player).hasClue()
        }
    }

    companion object {
        /**
         * The array of clue scroll item ids handled by this plugin.
         */
        private val IDS = TreasureTrailManager.idMap.keys.toIntArray()
    }
}

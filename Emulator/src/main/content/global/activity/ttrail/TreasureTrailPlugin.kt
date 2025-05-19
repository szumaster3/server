package content.global.activity.ttrail

import content.global.activity.ttrail.clues.AnagramClue
import content.global.activity.ttrail.clues.ChallengeClue
import content.global.activity.ttrail.clues.CoordinateClue
import content.global.activity.ttrail.scrolls.CoordinateClueScroll
import content.global.activity.ttrail.clues.CrypticClue
import content.global.activity.ttrail.clues.EmoteClue
import content.global.activity.ttrail.clues.MapClue
import content.global.activity.ttrail.npcs.SaradominWizardNPC
import content.global.activity.ttrail.npcs.ZamorakWizardNPC
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
 * Handles registration and interaction with clue scrolls and caskets for Treasure Trails.
 */
@Initializable
class TreasureTrailPlugin : OptionHandler() {

    /**
     * Initializes the plugin by registering item handlers for clue scrolls and caskets,
     * and defining plugins for clue scroll types and related NPCs.
     *
     * @param arg Optional argument, unused.
     * @return This plugin instance.
     */
    override fun newInstance(arg: Any?): Plugin<Any> {
        IDS.forEach { id ->
            ItemDefinition.forId(id).handlers["option:read"] = this
        }

        ClueLevel.values().forEach { level ->
            ItemDefinition.forId(level.casketId).handlers["option:open"] = this
        }

        ItemDefinition.forId(CoordinateClueScroll.SEXTANT.id).handlers["option:look through"] = this

        ClassScanner.definePlugin(MapClue())
        ClassScanner.definePlugin(ClueItemPlugin())
        ClassScanner.definePlugin(ChallengeClue())
        ClassScanner.definePlugin(CrypticClue())
        ClassScanner.definePlugin(AnagramClue())
        ClassScanner.definePlugin(EmoteClue())
        ClassScanner.definePlugin(CoordinateClue())
        ClassScanner.definePlugin(SaradominWizardNPC())
        ClassScanner.definePlugin(ZamorakWizardNPC())

        return this
    }

    /**
     * Handles player interaction with clue scroll items and caskets.
     *
     * Supports "read" to read clue scrolls and "open" to open clue caskets.
     *
     * @param player The player interacting.
     * @param node The node (item) being interacted with.
     * @param option The interaction option chosen.
     * @return True if the interaction was handled, false otherwise.
     */
    override fun handle(player: Player, node: Node, option: String): Boolean {
        player.lock(1)
        when (option) {
            "read" -> {
                val clue = ClueScroll.getClueScrolls()[node.id]
                if (clue == null) {
                    player.sendMessage("Unused clue scroll item! Removed.")
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
     * Indicates that this handler does not allow walking while using the option.
     *
     * @return False always.
     */
    override fun isWalk(): Boolean = false

    /**
     * Plugin for managing item behavior for clue scrolls when dropped or picked up.
     */
    inner class ClueItemPlugin : ItemPlugin() {

        /**
         * Registers the clue scroll item IDs for this plugin.
         *
         * @param arg (Optional) argument, unused.
         * @return This plugin instance.
         */
        override fun newInstance(arg: Any?): Plugin<Any> {
            register(*IDS)
            return this
        }

        /**
         * Determines if the player can pick up the specified clue scroll item.
         *
         * Prevents picking up if the player already has a clue scroll.
         *
         * @param player The player trying to pick up the item.
         * @param item The ground item being picked up.
         * @param type The type of pickup.
         * @return True if allowed to pick up, false otherwise.
         */
        override fun canPickUp(player: Player, item: GroundItem, type: Int): Boolean {
            return if (hasClue(player)) {
                player.sendMessage("A magical force prevents you from picking the clue scroll up.")
                false
            } else true
        }

        /**
         * Determines if the player can drop the clue scroll item.
         *
         * Prevents dropping if the player currently has a clue scroll.
         *
         * @param item The item to drop.
         * @param player The player dropping the item.
         * @param npc The NPC involved, if any.
         * @param location The location of the drop.
         * @return True if dropping is allowed, false otherwise.
         */
        override fun createDrop(item: Item, player: Player, npc: NPC?, location: Location): Boolean {
            return !hasClue(player)
        }

        /**
         * Returns the item without modification.
         *
         * @param item The item.
         * @param npc The NPC involved, if any.
         * @return The same item.
         */
        override fun getItem(item: Item, npc: NPC?): Item = item

        /**
         * Checks if the player currently holds a clue scroll.
         *
         * @param player The player to check.
         * @return True if player has a clue scroll, false otherwise.
         */
        private fun hasClue(player: Player): Boolean {
            return TreasureTrailManager.getInstance(player).hasClue()
        }
    }

    companion object {
        /**
         * The array of clue scroll item ids handled by this plugin.
         */
        private val IDS = TreasureTrailManager.clueScrollIds
    }
}

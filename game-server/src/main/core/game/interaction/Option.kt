package core.game.interaction

import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import java.util.*

/**
 * Represents a option on a node.
 */
class Option(@JvmField val name: String, val index: Int) {
    /**
     * Gets the assigned handler.
     */
    var handler: OptionHandler? = null

    /**
     * Sets and returns the handler.
     */
    fun setHandler(handler: OptionHandler?): Option {
        this.handler = handler
        return this
    }

    companion object {
        /**
         * Player attack option.
         */
        val P_ATTACK = Option("Attack", 0)

        /**
         * Player follow option.
         */
        val P_FOLLOW = Option("Follow", 2)

        /**
         * Player trade option.
         */
        val P_TRADE = Option("Trade with", 3)

        /**
         * Player give-to option.
         */
        val P_GIVETO = Option("Give-to", 3)

        /**
         * Player pickpocket option.
         */
        val P_PICKPOCKET = Option("Pickpocket", 4)

        /**
         * Player examine option.
         */
        val P_EXAMINE = Option("Examine", 7)

        /**
         * Player assist request option.
         */
        val P_ASSIST = Option("Req Assist", 6)

        /**
         * Fallback/null option.
         */
        val NULL = Option("null", 0)

        /**
         * Returns the default option handler for a node and option name.
         */
        fun defaultHandler(node: Node?, nodeId: Int, name: String): OptionHandler? {
            val normalized = name.lowercase(Locale.getDefault())
            return when (node) {
                is NPC     -> NPCDefinition.getOptionHandler(nodeId, normalized)
                is Scenery -> SceneryDefinition.getOptionHandler(nodeId, normalized)
                is Item    -> ItemDefinition.getOptionHandler(nodeId, normalized)
                else       -> null
            }
        }
    }
}

package core.game.node.entity.combat.spell

import core.game.node.Node
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer

/**
 * Object responsible for managing spell blocks, which prevent specific nodes from being affected by certain spells.
 */
object SpellBlocks {

    /**
     * A mapping of spell IDs to a list of blocked nodes.
     */
    private val blocks = HashMap<Int, MutableList<Node>?>()

    /**
     * Registers a node to be blocked by a specific spell.
     *
     * @param spellId The id of the spell that should block the node.
     * @param toBlock The node to be blocked.
     */
    @JvmStatic
    fun register(spellId: Int, toBlock: Node) {
        if (blocks[spellId] != null) {
            blocks[spellId]!!.add(toBlock)
        } else {
            val blockslist: MutableList<Node> = ArrayList(20)
            blockslist.add(toBlock)
            blocks[spellId] = blockslist
        }
    }

    /**
     * Checks if a node is blocked by a specific spell.
     *
     * @param spellId The id of the spell to check.
     * @param node The node to check against the spell's block list.
     * @return `true` if the node is blocked by the spell, `false` otherwise.
     */
    @JvmStatic
    fun isBlocked(spellId: Int, node: Node): Boolean {
        val blocked = AtomicBoolean(false)

        if (blocks[spellId] == null) {
            return false
        }

        blocks[spellId]!!.forEach(
            Consumer { n: Node ->
                if (node.name == n.name) {
                    blocked.set(true)
                }
            },
        )

        return blocked.get()
    }
}
package core.game.node.entity.combat.spell

import core.game.node.Node
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer

object SpellBlocks {
    private val blocks = HashMap<Int, MutableList<Node>?>()

    @JvmStatic
    fun register(
        spellId: Int,
        toBlock: Node,
    ) {
        if (blocks[spellId] != null) {
            blocks[spellId]!!.add(toBlock)
        } else {
            val blockslist: MutableList<Node> = ArrayList(20)
            blockslist.add(toBlock)
            blocks[spellId] = blockslist
        }
    }

    @JvmStatic
    fun isBlocked(
        spellId: Int,
        node: Node,
    ): Boolean {
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

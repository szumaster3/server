package content.global.skill.summoning.familiar

import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.item.Item

/**
 * The type Familiar special.
 */
class FamiliarSpecial {

    var node: Node
    var interfaceID: Int
    var component: Int
    var item: Item?

    /**
     * Instantiates a new Familiar special.
     *
     * @param node the node
     */
    constructor(node: Node) : this(node, -1, -1, null)

    /**
     * Instantiates a new Familiar special.
     *
     * @param node        the node
     * @param interfaceID the interface id
     * @param component   the component
     * @param item        the item
     */
    constructor(node: Node, interfaceID: Int, component: Int, item: Item?) {
        this.node = node
        this.interfaceID = interfaceID
        this.component = component
        this.item = item
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    val target: Entity
        get() = node as Entity
}

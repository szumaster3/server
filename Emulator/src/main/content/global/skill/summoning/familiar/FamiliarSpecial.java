package content.global.skill.summoning.familiar;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.item.Item;

/**
 * The type Familiar special.
 */
public class FamiliarSpecial {

    private Node node;

    private int interfaceID;

    private int component;

    private Item item;

    /**
     * Instantiates a new Familiar special.
     *
     * @param node the node
     */
    public FamiliarSpecial(Node node) {
        this(node, -1, -1, null);
    }

    /**
     * Instantiates a new Familiar special.
     *
     * @param node        the node
     * @param interfaceID the interface id
     * @param component   the component
     * @param item        the item
     */
    public FamiliarSpecial(Node node, int interfaceID, int component, Item item) {
        this.node = node;
        this.interfaceID = interfaceID;
        this.component = component;
        this.item = item;
    }

    /**
     * Gets node.
     *
     * @return the node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Sets node.
     *
     * @param node the node
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * Gets interface id.
     *
     * @return the interface id
     */
    public int getInterfaceID() {
        return interfaceID;
    }

    /**
     * Sets interface id.
     *
     * @param interfaceID the interface id
     */
    public void setInterfaceID(int interfaceID) {
        this.interfaceID = interfaceID;
    }

    /**
     * Gets component.
     *
     * @return the component
     */
    public int getComponent() {
        return component;
    }

    /**
     * Sets component.
     *
     * @param component the component
     */
    public void setComponent(int component) {
        this.component = component;
    }

    /**
     * Gets item.
     *
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * Sets item.
     *
     * @param item the item
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public Entity getTarget() {
        return (Entity) node;
    }

}
package core.game.interaction;

import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;

/**
 * The type Option.
 */
public final class Option {

    /**
     * The constant _P_ATTACK.
     */
    public static final Option _P_ATTACK = new Option("Attack", 0);

    /**
     * The constant _P_FOLLOW.
     */
    public static final Option _P_FOLLOW = new Option("Follow", 2);

    /**
     * The constant _P_TRADE.
     */
    public static final Option _P_TRADE = new Option("Trade with", 3);

    /**
     * The constant _P_GIVETO.
     */
    public static final Option _P_GIVETO = new Option("Give-to", 3);

    /**
     * The constant _P_PICKPOCKET.
     */
    public static final Option _P_PICKPOCKET = new Option("Pickpocket", 4);

    /**
     * The constant _P_EXAMINE.
     */
    public static final Option _P_EXAMINE = new Option("Examine", 7);

    /**
     * The constant _P_ASSIST.
     */
    public static final Option _P_ASSIST = new Option("Req Assist", 6);

    /**
     * The constant NULL.
     */
    public static final Option NULL = new Option("null", 0);

    private final String name;

    private final int index;

    private OptionHandler handler;

    /**
     * Instantiates a new Option.
     *
     * @param name  the name
     * @param index the index
     */
    public Option(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Default handler option handler.
     *
     * @param node   the node
     * @param nodeId the node id
     * @param name   the name
     * @return the option handler
     */
    public static OptionHandler defaultHandler(Node node, int nodeId, String name) {
        name = name.toLowerCase();
        if (node instanceof NPC) {
            return NPCDefinition.getOptionHandler(nodeId, name);
        }
        if (node instanceof Scenery) {
            return SceneryDefinition.getOptionHandler(nodeId, name);
        }
        if (node instanceof Item) {
            return ItemDefinition.getOptionHandler(nodeId, name);
        }

        return null;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets handler.
     *
     * @return the handler
     */
    public OptionHandler getHandler() {
        return handler;
    }

    /**
     * Sets handler.
     *
     * @param handler the handler
     * @return the handler
     */
    public Option setHandler(OptionHandler handler) {
        this.handler = handler;
        return this;
    }
}

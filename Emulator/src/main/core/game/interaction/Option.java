package core.game.interaction;

import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;

/**
 * Represents a option on a node.
 */
public final class Option {

    /**
     * Player attack option.
     */
    public static final Option _P_ATTACK = new Option("Attack", 0);

    /**
     * Player follow option.
     */
    public static final Option _P_FOLLOW = new Option("Follow", 2);

    /**
     * Player trade option.
     */
    public static final Option _P_TRADE = new Option("Trade with", 3);

    /**
     * Player give-to option.
     */
    public static final Option _P_GIVETO = new Option("Give-to", 3);

    /**
     * Player pickpocket option.
     */
    public static final Option _P_PICKPOCKET = new Option("Pickpocket", 4);

    /**
     * Player examine option.
     */
    public static final Option _P_EXAMINE = new Option("Examine", 7);

    /**
     * Player assist request option.
     */
    public static final Option _P_ASSIST = new Option("Req Assist", 6);

    /**
     * Null fallback option.
     */
    public static final Option NULL = new Option("null", 0);

    private final String name;
    private final int index;
    private OptionHandler handler;

    /**
     * Creates a new Option with the given name and index.
     */
    public Option(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Gets the default handler for a node and option name.
     */
    public static OptionHandler defaultHandler(Node node, int nodeId, String name) {
        name = name.toLowerCase();
        if (node instanceof NPC) return NPCDefinition.getOptionHandler(nodeId, name);
        if (node instanceof Scenery) return SceneryDefinition.getOptionHandler(nodeId, name);
        if (node instanceof Item) return ItemDefinition.getOptionHandler(nodeId, name);
        return null;
    }

    /**
     * Gets the option name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the option index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the assigned handler.
     */
    public OptionHandler getHandler() {
        return handler;
    }

    /**
     * Sets and returns the handler.
     */
    public Option setHandler(OptionHandler handler) {
        this.handler = handler;
        return this;
    }
}

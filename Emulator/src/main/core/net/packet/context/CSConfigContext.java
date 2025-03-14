package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Cs config context.
 */
public class CSConfigContext implements Context {

    private final Object[] parameters;
    private final String types;
    private Player player;
    private int value;
    private int id;

    /**
     * Instantiates a new Cs config context.
     *
     * @param player     the player
     * @param id         the id
     * @param value      the value
     * @param types      the types
     * @param parameters the parameters
     */
    public CSConfigContext(Player player, int id, int value, String types, Object[] parameters) {
        this.player = player;
        this.value = value;
        this.id = id;
        this.parameters = parameters;
        this.types = types;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets player.
     *
     * @param player the player
     * @return the player
     */
    public Context setPlayer(Player player) {
        this.player = player;
        return this;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets types.
     *
     * @return the types
     */
    public String getTypes() {
        return types;
    }

    /**
     * Get parameters object [ ].
     *
     * @return the object [ ]
     */
    public Object[] getParameters() {
        return parameters;
    }
}
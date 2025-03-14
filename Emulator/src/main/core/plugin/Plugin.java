package core.plugin;

import core.game.node.entity.player.Player;

/**
 * The interface Plugin.
 *
 * @param <T> the type parameter
 */
public interface Plugin<T> {

    /**
     * New instance plugin.
     *
     * @param arg the arg
     * @return the plugin
     * @throws Throwable the throwable
     */
    public Plugin<T> newInstance(T arg) throws Throwable;

    /**
     * Fire event object.
     *
     * @param identifier the identifier
     * @param args       the args
     * @return the object
     */
    Object fireEvent(String identifier, Object... args);

    /**
     * Handle selection callback.
     *
     * @param skill  the skill
     * @param player the player
     */
    public default void handleSelectionCallback(int skill, Player player){}

}
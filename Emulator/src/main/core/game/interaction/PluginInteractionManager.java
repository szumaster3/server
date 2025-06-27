package core.game.interaction;

import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;

import java.util.HashMap;

/**
 * Handles registration and dispatching of entity interaction plugins.
 */
public class PluginInteractionManager {
    private static final HashMap<Integer, PluginInteraction> npcInteractions = new HashMap<>();
    private static final HashMap<Integer, PluginInteraction> objectInteractions = new HashMap<>();
    private static final HashMap<Integer, PluginInteraction> useWithInteractions = new HashMap<>();
    private static final HashMap<Integer, PluginInteraction> groundItemInteractions = new HashMap<>();

    /**
     * Registers a new interaction with the specified type.
     *
     * @param interaction The interaction to register.
     * @param type        The type of interaction.
     */
    public static void register(PluginInteraction interaction, InteractionType type) {
        switch (type) {
            case OBJECT:
                for (int id : interaction.ids) {
                    objectInteractions.putIfAbsent(id, interaction);
                }
                break;
            case USE_WITH:
                for (int id : interaction.ids) {
                    useWithInteractions.putIfAbsent(id, interaction);
                }
                break;
            case NPC:
                for (int id : interaction.ids) {
                    npcInteractions.putIfAbsent(id, interaction);
                }
                break;
            case ITEM:
                for (int id : interaction.ids) {
                    groundItemInteractions.putIfAbsent(id, interaction);
                }
                break;
        }
    }

    /**
     * Handles interaction with a scenery object.
     *
     * @param player The player initiating the interaction.
     * @param object The scenery object being interacted with.
     * @return true if the interaction was handled, false otherwise.
     */
    public static boolean handle(Player player, Scenery object) {
        PluginInteraction i = objectInteractions.get(object.getId());
        return i != null && i.handle(player, object);
    }

    /**
     * Handles interaction with an item being used on another entity.
     *
     * @param player The player initiating the interaction.
     * @param event  The event containing usage details.
     * @return true if the interaction was handled, false otherwise.
     */
    public static boolean handle(Player player, NodeUsageEvent event) {
        PluginInteraction i = useWithInteractions.get(event.getUsed().asItem().getId());
        return i != null && i.handle(player, event);
    }

    /**
     * Handles interaction with an NPC.
     *
     * @param player The player initiating the interaction.
     * @param npc    The NPC being interacted with.
     * @param option The interaction option chosen.
     * @return true if the interaction was handled, false otherwise.
     */
    public static boolean handle(Player player, NPC npc, Option option) {
        PluginInteraction i = npcInteractions.get(npc.getId());
        return i != null && i.handle(player, npc, option);
    }

    /**
     * Handles interaction with a ground item.
     *
     * @param player The player initiating the interaction.
     * @param item   The item being interacted with.
     * @param option The interaction option chosen.
     * @return true if the interaction was handled, false otherwise.
     */
    public static boolean handle(Player player, Item item, Option option) {
        PluginInteraction i = groundItemInteractions.get(item.getId());
        return i != null && i.handle(player, item, option);
    }

    /**
     * Defines the types of interactions that can be registered.
     */
    public enum InteractionType {
        NPC, OBJECT, USE_WITH, ITEM
    }
}
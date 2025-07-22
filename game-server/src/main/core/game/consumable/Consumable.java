package core.game.consumable;

import content.data.consumables.Consumables;
import core.api.Container;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;

import static core.api.ContentAPIKt.replaceSlot;

/**
 * Abstract base class for consumable items.
 * Handles consumption logic.
 */
public abstract class Consumable {

    /**
     * Array of item ids representing this consumable's variants or stages.
     */
    protected final int[] ids;

    /**
     * Effect applied when this consumable is used.
     */
    protected final ConsumableEffect effect;

    /**
     * Optional messages to send upon consumption.
     */
    protected final String[] messages;

    /**
     * Animation played on consumption (nullable).
     */
    protected Animation animation = null;

    /**
     * Constructs a consumable with item ids, effect, and optional messages.
     *
     * @param ids      Item IDs representing this consumable.
     * @param effect   Effect to apply on consumption.
     * @param messages Optional messages to display when consumed.
     */
    public Consumable(final int[] ids, final ConsumableEffect effect, final String... messages) {
        this.ids = ids;
        this.effect = effect;
        this.messages = messages;
    }

    /**
     * Constructs a consumable with item ids, effect, animation, and optional messages.
     *
     * @param ids       Item IDs representing this consumable.
     * @param effect    Effect to apply on consumption.
     * @param animation Animation to play when consumed.
     * @param messages  Optional messages to display when consumed.
     */
    public Consumable(final int[] ids, final ConsumableEffect effect, final Animation animation, final String... messages) {
        this.ids = ids;
        this.effect = effect;
        this.animation = animation;
        this.messages = messages;
    }

    /**
     * Handles consumption logic.
     *
     * @param item   The item being consumed.
     * @param player The player consuming the item.
     */
    public void consume(final Item item, final Player player) {
        executeConsumptionActions(player);
        final int nextItemId = getNextItemId(item.getId());

        if (ids.length == 1) {
            // For stackable items, decrease amount by 1
            replaceSlot(player, item.getSlot(), new Item(item.getId(), item.getAmount() - 1), item, Container.INVENTORY);
        } else {
            // For staged items, replace with next stage item
            replaceSlot(player, item.getSlot(), new Item(nextItemId, 1), item, Container.INVENTORY);
        }

        final int initialLifePoints = player.getSkills().getLifepoints();
        Consumables.getConsumableById(item.getId()).getConsumable().effect.activate(player);
        sendMessages(player, initialLifePoints, item, messages);
    }

    /**
     * Sends appropriate messages to the player after consumption.
     *
     * @param player            The player receiving messages.
     * @param initialLifePoints Player's life points before consumption.
     * @param item              The consumed item.
     * @param messages          Custom messages to send.
     */
    protected void sendMessages(final Player player, final int initialLifePoints, final Item item, String[] messages) {
        if (messages.length == 0) {
            sendDefaultMessages(player, item);
            sendHealingMessage(player, initialLifePoints);
        } else {
            sendCustomMessages(player, messages);
        }
    }

    /**
     * Sends a healing message if the player life points increased after consumption.
     *
     * @param player            The player who consumed the item.
     * @param initialLifePoints Player's life points before consumption.
     */
    protected void sendHealingMessage(final Player player, final int initialLifePoints) {
        if (player.getSkills().getLifepoints() > initialLifePoints) {
            player.getPacketDispatch().sendMessage("It heals some health.");
        }
    }

    /**
     * Sends custom messages to the player.
     *
     * @param player   The player receiving the messages.
     * @param messages Array of messages to send.
     */
    protected void sendCustomMessages(final Player player, final String[] messages) {
        for (String message : messages) {
            player.getPacketDispatch().sendMessage(message);
        }
    }

    /**
     * Sends the default message to the player upon consumption.
     *
     * @param player The player receiving the message.
     * @param item   The consumed item.
     */
    protected abstract void sendDefaultMessages(final Player player, final Item item);

    /**
     * Executes any additional actions (animation, sound, etc.) upon consumption.
     *
     * @param player The player consuming the item.
     */
    protected abstract void executeConsumptionActions(Player player);

    /**
     * Gets the next item id in the consumable sequence after the current one.
     *
     * @param currentConsumableId The current consumables item id.
     * @return The next item ID or -1 if none exists.
     */
    protected int getNextItemId(final int currentConsumableId) {
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == currentConsumableId && i != ids.length - 1) {
                return ids[i + 1];
            }
        }
        return -1;
    }

    /**
     * Returns a formatted item name, removing dose indicators and trimming whitespace.
     *
     * @param item The item to format.
     * @return The formatted name.
     */
    public String getFormattedName(Item item) {
        return item.getName().replaceAll("\\(\\d\\)", "").trim().toLowerCase();
    }

    /**
     * Gets the health effect value applied by this consumable.
     *
     * @param player The player affected.
     * @return The health effect value.
     */
    public int getHealthEffectValue(Player player) {
        return effect.getHealthEffectValue(player);
    }

    /**
     * Gets the consumable effect.
     *
     * @return The effect.
     */
    public ConsumableEffect getEffect() {
        return effect;
    }

    /**
     * Returns the item ids for this consumable.
     *
     * @return The array of item ids.
     */
    public int[] getIds() {
        return ids;
    }
}

package core.game.consumable;

import content.data.consumables.Consumables;
import core.api.Container;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;

import static core.api.ContentAPIKt.replaceSlot;

/**
 * The type Consumable.
 */
public abstract class Consumable {

    /**
     * The Ids.
     */
    protected final int[] ids;

    /**
     * The Effect.
     */
    protected final ConsumableEffect effect;

    /**
     * The Messages.
     */
    protected final String[] messages;

    /**
     * The Animation.
     */
    protected Animation animation = null;

    /**
     * Instantiates a new Consumable.
     *
     * @param ids      the ids
     * @param effect   the effect
     * @param messages the messages
     */
    public Consumable(final int[] ids, final ConsumableEffect effect, final String... messages) {
        this.ids = ids;
        this.effect = effect;
        this.messages = messages;
    }

    /**
     * Instantiates a new Consumable.
     *
     * @param ids       the ids
     * @param effect    the effect
     * @param animation the animation
     * @param messages  the messages
     */
    public Consumable(final int[] ids, final ConsumableEffect effect, final Animation animation, final String... messages) {
        this.ids = ids;
        this.effect = effect;
        this.animation = animation;
        this.messages = messages;
    }

    /**
     * Consume.
     *
     * @param item   the item
     * @param player the player
     */
    public void consume(final Item item, final Player player) {
        executeConsumptionActions(player);
        final int nextItemId = getNextItemId(item.getId());

        if (ids.length == 1) {
            replaceSlot(player, item.getSlot(), new Item(item.getId(), (item.getAmount() - 1)), item, Container.INVENTORY);
        } else {
            replaceSlot(player, item.getSlot(), new Item(nextItemId, 1), item, Container.INVENTORY);
        }

        final int initialLifePoints = player.getSkills().getLifepoints();
        Consumables.getConsumableById(item.getId()).getConsumable().effect.activate(player);
        sendMessages(player, initialLifePoints, item, messages);
    }

    /**
     * Send messages.
     *
     * @param player            the player
     * @param initialLifePoints the initial life points
     * @param item              the item
     * @param messages          the messages
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
     * Send healing message.
     *
     * @param player            the player
     * @param initialLifePoints the initial life points
     */
    protected void sendHealingMessage(final Player player, final int initialLifePoints) {
        if (player.getSkills().getLifepoints() > initialLifePoints) {
            player.getPacketDispatch().sendMessage("It heals some health.");
        }
    }

    /**
     * Send custom messages.
     *
     * @param player   the player
     * @param messages the messages
     */
    protected void sendCustomMessages(final Player player, final String[] messages) {
        for (String message : messages) {
            player.getPacketDispatch().sendMessage(message);
        }
    }

    /**
     * Send default messages.
     *
     * @param player the player
     * @param item   the item
     */
    protected abstract void sendDefaultMessages(final Player player, final Item item);

    /**
     * Execute consumption actions.
     *
     * @param player the player
     */
    protected abstract void executeConsumptionActions(Player player);

    /**
     * Gets next item id.
     *
     * @param currentConsumableId the current consumable id
     * @return the next item id
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
     * Gets formatted name.
     *
     * @param item the item
     * @return the formatted name
     */
    public String getFormattedName(Item item) {
        return item.getName().replaceAll("\\(\\d\\)", "").trim().toLowerCase();
    }

    /**
     * Gets health effect value.
     *
     * @param player the player
     * @return the health effect value
     */
    public int getHealthEffectValue(Player player) {
        return effect.getHealthEffectValue(player);
    }

    /**
     * Gets effect.
     *
     * @return the effect
     */
    public ConsumableEffect getEffect() {
        return effect;
    }

    /**
     * Get ids int [ ].
     *
     * @return the int [ ]
     */
    public int[] getIds() {
        return ids;
    }
}

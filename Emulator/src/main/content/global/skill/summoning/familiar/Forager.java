package content.global.skill.summoning.familiar;

import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.GameWorld;
import core.tools.RandomFunction;

/**
 * The type Forager.
 */
public abstract class Forager extends BurdenBeast {

    private final Item[] items;

    private int passiveDelay;

    /**
     * Instantiates a new Forager.
     *
     * @param owner       the owner
     * @param id          the id
     * @param ticks       the ticks
     * @param pouchId     the pouch id
     * @param specialCost the special cost
     * @param attackStyle the attack style
     * @param items       the items
     */
    public Forager(Player owner, int id, int ticks, int pouchId, int specialCost, int attackStyle, final Item... items) {
        super(owner, id, ticks, pouchId, specialCost, 30, attackStyle);
        this.items = items;
        setRandomPassive();
    }

    /**
     * Instantiates a new Forager.
     *
     * @param owner       the owner
     * @param id          the id
     * @param ticks       the ticks
     * @param pouchId     the pouch id
     * @param specialCost the special cost
     * @param items       the items
     */
    public Forager(Player owner, int id, int ticks, int pouchId, int specialCost, final Item... items) {
        this(owner, id, ticks, pouchId, specialCost, WeaponInterface.STYLE_DEFENSIVE, items);
    }

    @Override
    public void handleFamiliarTick() {
        super.handleFamiliarTick();
        if (items != null && items.length > 0 && passiveDelay < GameWorld.getTicks()) {
            if (RandomFunction.random(getRandom()) < 4) {
                produceItem(items[RandomFunction.random(items.length)]);
            }
            setRandomPassive();
        }
    }

    /**
     * Produce item boolean.
     *
     * @param item the item
     * @return the boolean
     */
    public boolean produceItem(final Item item) {
        if (!container.hasSpaceFor(item)) {
            owner.getPacketDispatch().sendMessage("Your familar is too full to collect items.");
            return false;
        }
        owner.getPacketDispatch().sendMessage(item.getAmount() == 1 ? "Your familar has produced an item." : "Your familiar has produced items.");
        return container.add(item);
    }

    /**
     * Produce item boolean.
     *
     * @return the boolean
     */
    public boolean produceItem() {
        if (items == null || items.length == 0) {
            return false;
        }
        return produceItem(items[RandomFunction.random(items.length)]);
    }

    /**
     * Handle passive action.
     */
    public void handlePassiveAction() {

    }

    /**
     * Gets random.
     *
     * @return the random
     */
    public int getRandom() {
        return 11;
    }

    /**
     * Sets random passive.
     */
    public void setRandomPassive() {
        passiveDelay = GameWorld.getTicks() + RandomFunction.random(100, 440);
    }
}

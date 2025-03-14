package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;

/**
 * The type Spirit cobra npc.
 */
@Initializable
public class SpiritCobraNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Spirit cobra npc.
     */
    public SpiritCobraNPC() {
        this(null, 6802);
    }

    /**
     * Instantiates a new Spirit cobra npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public SpiritCobraNPC(Player owner, int id) {
        super(owner, id, 5600, 12015, 3, WeaponInterface.STYLE_ACCURATE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new SpiritCobraNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        final Item item = (Item) special.getNode();
        final Egg egg = Egg.forEgg(item);
        if (egg == null) {
            owner.getPacketDispatch().sendMessage("You can't use the special move on this item.");
            return false;
        }
        owner.getInventory().replace(egg.getProduct(), item.getSlot());
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{6802, 6803};
    }

    /**
     * The enum Egg.
     */
    public enum Egg {
        /**
         * The Cockatrice.
         */
        COCKATRICE(new Item(1944), new Item(12109)),
        /**
         * The Saratrice.
         */
        SARATRICE(new Item(5077), new Item(12113)),
        /**
         * The Zamatrice.
         */
        ZAMATRICE(new Item(5076), new Item(12115)),
        /**
         * The Guthatrice.
         */
        GUTHATRICE(new Item(5078), new Item(12111)),
        /**
         * The Coracatrice.
         */
        CORACATRICE(new Item(11964), new Item(12119)),
        /**
         * The Pengatrice.
         */
        PENGATRICE(new Item(12483), new Item(12117)),
        /**
         * The Vulatrice.
         */
        VULATRICE(new Item(11965), new Item(12121));

        private final Item egg;

        private final Item product;

        private Egg(Item egg, Item product) {
            this.egg = egg;
            this.product = product;
        }

        /**
         * For egg egg.
         *
         * @param item the item
         * @return the egg
         */
        public static Egg forEgg(final Item item) {
            for (Egg egg : values()) {
                if (egg.getEgg().getId() == item.getId()) {
                    return egg;
                }
            }
            return null;
        }

        /**
         * Gets egg.
         *
         * @return the egg
         */
        public Item getEgg() {
            return egg;
        }

        /**
         * Gets product.
         *
         * @return the product
         */
        public Item getProduct() {
            return product;
        }

    }
}

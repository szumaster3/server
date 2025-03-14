package content.global.skill.cooking.data;

import core.game.node.item.Item;
import org.rs.consts.Items;

import java.util.Arrays;

/**
 * The enum Dairy product.
 */
public enum DairyProduct {
    /**
     * The Pot of cream.
     */
    POT_OF_CREAM(21, 18, new Item(Items.POT_OF_CREAM_2130, 1), new Integer[]{Items.BUCKET_OF_MILK_1927}),
    /**
     * The Pat of butter.
     */
    PAT_OF_BUTTER(38, 40.5, new Item(Items.PAT_OF_BUTTER_6697, 1), new Integer[]{Items.BUCKET_OF_MILK_1927, Items.POT_OF_CREAM_2130}),
    /**
     * The Cheese.
     */
    CHEESE(48, 64, new Item(Items.CHEESE_1985, 1), new Integer[]{Items.BUCKET_OF_MILK_1927, Items.POT_OF_CREAM_2130, Items.PAT_OF_BUTTER_6697});

    private Item product;

    private int level;


    private double experience;

    private Item[] inputs;

    DairyProduct(int level, double experience, Item product, Integer[] inputs) {
        this.level = level;
        this.experience = experience;
        this.product = product;
        this.inputs = Arrays.stream(inputs).map(id -> new Item(id, 1)).toArray(len -> new Item[len]);
    }

    /**
     * Gets product.
     *
     * @return the product
     */
    public Item getProduct() {
        return product;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets experience.
     *
     * @return the experience
     */
    public double getExperience() {
        return experience;
    }

    /**
     * Get inputs item [ ].
     *
     * @return the item [ ]
     */
    public Item[] getInputs() {
        return inputs;
    }
}

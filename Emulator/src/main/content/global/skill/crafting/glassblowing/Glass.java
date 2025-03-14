package content.global.skill.crafting.glassblowing;

import org.rs.consts.Items;

import java.util.HashMap;
import java.util.Map;

public enum Glass {
    EMPTY_VIAL(38, Items.VIAL_229, 1, 33, 35.0),
    UNPOWERED_ORB(39, Items.UNPOWERED_ORB_567, 1, 46, 52.5),
    BEER_GLASS(40, Items.BEER_GLASS_1919, 1, 1, 17.5),
    EMPTY_CANDLE_LANTERN(41, Items.CANDLE_LANTERN_4527, 1, 4, 19.0),
    EMPTY_OIL_LAMP(42, Items.OIL_LAMP_4525, 1, 12, 25.0),
    LANTERN_LENS(43, Items.LANTERN_LENS_4542, 1, 49, 55.0),
    FISHBOWL(44, Items.FISHBOWL_6667, 1, 42, 42.5),
    EMPTY_LIGHT_ORB(45, Items.LIGHT_ORB_10973, 1, 87, 70.0);

    private final int buttonId, productId, amount, requiredLevel;
    private final double experience;

    private static final Map<Integer, Glass> lookupMap = new HashMap<>();

    static {
        for (Glass glass : values()) {
            lookupMap.put(glass.buttonId, glass);
            lookupMap.put(glass.productId, glass);
        }
    }

    Glass(int buttonId, int productId, int amount, int requiredLevel, double experience) {
        this.buttonId = buttonId;
        this.productId = productId;
        this.amount = amount;
        this.requiredLevel = requiredLevel;
        this.experience = experience;
    }

    public int getButtonId() {
        return buttonId;
    }

    public int getProductId() {
        return productId;
    }

    public int getAmount() {
        return amount;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public double getExperience() {
        return experience;
    }

    public static Glass getById(int id) {
        return lookupMap.get(id);
    }
}

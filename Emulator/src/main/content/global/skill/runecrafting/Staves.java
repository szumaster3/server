package content.global.skill.runecrafting;

import org.rs.consts.Items;

import java.util.HashMap;
import java.util.Map;

public enum Staves {
    AIR_RC_STAFF(Items.AIR_TALISMAN_STAFF_13630, 25.0),
    MIND_RC_STAFF(Items.MIND_TALISMAN_STAFF_13631, 27.5),
    WATER_RC_STAFF(Items.WATER_TALISMAN_STAFF_13632, 30.0),
    EARTH_RC_STAFF(Items.EARTH_TALISMAN_STAFF_13633, 32.5),
    FIRE_RC_STAFF(Items.FIRE_TALISMAN_STAFF_13634, 35.0),
    BODY_RC_STAFF(Items.BODY_TALISMAN_STAFF_13635, 37.5),
    COSMIC_RC_STAFF(Items.COSMIC_TALISMAN_STAFF_13636, 40.0),
    CHAOS_RC_STAFF(Items.CHAOS_TALISMAN_STAFF_13637, 43.5),
    NATURE_RC_STAFF(Items.NATURE_TALISMAN_STAFF_13638, 45.0),
    LAW_RC_STAFF(Items.LAW_TALISMAN_STAFF_13639, 47.5),
    DEATH_RC_STAFF(Items.DEATH_TALISMAN_STAFF_13640, 50.0),
    BLOOD_RC_STAFF(Items.BLOOD_TALISMAN_STAFF_13641, 52.5);

    private final int item;
    private final double experience;

    private static final Map<Integer, Staves> ITEM_TO_STAVES = new HashMap<>();

    static {
        for (Staves staff : values()) {
            ITEM_TO_STAVES.put(staff.item, staff);
        }
    }

    Staves(int item, double experience) {
        this.item = item;
        this.experience = experience;
    }

    public int getItem() {
        return item;
    }

    public double getExperience() {
        return experience;
    }

    public static Staves forStaff(int item) {
        return ITEM_TO_STAVES.get(item);
    }
}

package core.game.node.entity.combat.equipment;

import core.ServerConstants;
import core.game.node.entity.combat.equipment.Weapon.WeaponType;
import core.game.node.entity.impl.Animator.Priority;
import core.game.world.update.flag.context.Animation;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Range weapon.
 */
public class RangeWeapon {

    private final int itemId;
    private final Animation animation;
    private final int attackSpeed;
    private final int ammunitionSlot;
    private final int type;
    private final boolean isDropAmmo;
    private final List<Integer> ammunition;

    /**
     * Instantiates a new Range weapon.
     *
     * @param itemId         the item id
     * @param animation      the animation
     * @param attackSpeed    the attack speed
     * @param ammunitionSlot the ammunition slot
     * @param type           the type
     * @param isDropAmmo     the is drop ammo
     * @param ammunition     the ammunition
     */
    public RangeWeapon(int itemId, Animation animation, int attackSpeed, int ammunitionSlot,
                       int type, boolean isDropAmmo, List<Integer> ammunition) {
        this.itemId = itemId;
        this.animation = animation;
        this.attackSpeed = attackSpeed;
        this.ammunitionSlot = ammunitionSlot;
        this.type = type;
        this.isDropAmmo = isDropAmmo;
        this.ammunition = ammunition;
    }

    /**
     * Gets weapon type.
     *
     * @return the weapon type
     */
    public WeaponType getWeaponType() {
        return WeaponType.values()[type];
    }

    /**
     * Gets item id.
     *
     * @return the item id
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Gets animation.
     *
     * @return the animation
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * Gets attack speed.
     *
     * @return the attack speed
     */
    public int getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * Gets ammunition slot.
     *
     * @return the ammunition slot
     */
    public int getAmmunitionSlot() {
        return ammunitionSlot;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Is drop ammo boolean.
     *
     * @return the boolean
     */
    public boolean isDropAmmo() {
        return isDropAmmo;
    }

    /**
     * Gets ammunition.
     *
     * @return the ammunition
     */
    public List<Integer> getAmmunition() {
        return ammunition;
    }

    private static final Map<Integer, RangeWeapon> RANGE_WEAPONS = new HashMap<>();

    /**
     * Initialize boolean.
     *
     * @return the boolean
     */
    public static boolean initialize() {
        Document doc;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            doc = factory.newDocumentBuilder().parse(new File(ServerConstants.CACHE_PATH + "ranged_weapon_configs.json"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        var nodeList = doc.getDocumentElement().getChildNodes();
        int i = 1;

        while (i < nodeList.getLength()) {
            var n = nodeList.item(i);
            if (n != null && n.getNodeName().equalsIgnoreCase("Weapon")) {
                var list = n.getChildNodes();
                int itemId = 0;
                int animationId = 426;
                int attackSpeed = 4;
                int slot = 13;
                int type = 0;
                boolean dropAmmo = true;
                List<Integer> ammo = new ArrayList<>();

                int a = 1;
                while (a < list.getLength()) {
                    var node = list.item(a);
                    switch (node.getNodeName().toLowerCase()) {
                        case "itemid":
                            itemId = Integer.parseInt(node.getTextContent());
                            break;
                        case "animationid":
                            animationId = Integer.parseInt(node.getTextContent());
                            break;
                        case "attackspeed":
                            attackSpeed = Integer.parseInt(node.getTextContent());
                            break;
                        case "ammunitionslot":
                            slot = Integer.parseInt(node.getTextContent());
                            break;
                        case "type":
                            type = Integer.parseInt(node.getTextContent());
                            break;
                        case "drop_ammo":
                            dropAmmo = Boolean.parseBoolean(node.getTextContent());
                            break;
                        case "ammunition":
                            ammo.add(Integer.parseInt(node.getTextContent()));
                            break;
                    }
                    a++;
                }

                RANGE_WEAPONS.putIfAbsent(itemId, new RangeWeapon(
                        itemId,
                        new Animation(animationId, 0, Priority.HIGH),
                        attackSpeed,
                        slot,
                        type,
                        dropAmmo,
                        ammo
                ));
            }
            i++;
        }

        return true;
    }

    /**
     * Gets range weapons.
     *
     * @return the range weapons
     */
    public static Map<Integer, RangeWeapon> getRangeWeapons() {
        return RANGE_WEAPONS;
    }

    /**
     * Get range weapon.
     *
     * @param id the id
     * @return the range weapon
     */
    public static RangeWeapon get(int id) {
        return RANGE_WEAPONS.get(id);
    }
}

package core.game.world.map.zone;

import content.global.skill.summoning.familiar.Familiar;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.request.RequestType;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.map.Region;
import core.game.world.map.RegionManager;

import java.util.Iterator;
import java.util.Objects;

/**
 * The type Map zone.
 */
public abstract class MapZone implements Zone {

    private int uid;

    private String name;

    private boolean overlappable;

    /**
     * The Fire random events.
     */
    protected boolean fireRandomEvents;

    private int restriction;

    private int zoneType;

    /**
     * Instantiates a new Map zone.
     *
     * @param name         the name
     * @param overlappable the overlappable
     * @param restrictions the restrictions
     */
    public MapZone(String name, boolean overlappable, ZoneRestriction... restrictions) {
        this.name = name;
        this.overlappable = overlappable;
        for (ZoneRestriction restriction : restrictions) {
            addRestriction(restriction.getFlag());
        }
    }

    @Override
    public boolean enter(Entity e) {
        if (e instanceof Player) {
            Player p = (Player) e;
        } else if (e instanceof NPC) {
            NPC npc = (NPC) e;
            if (e instanceof Familiar && isRestricted(ZoneRestriction.FOLLOWERS.getFlag())) {
                npc.setInvisible(true);
            }
        }
        return true;
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        return true;
    }

    /**
     * Can logout boolean.
     *
     * @param p the p
     * @return the boolean
     */
    public boolean canLogout(Player p) {
        return true;
    }

    /**
     * Death boolean.
     *
     * @param e      the e
     * @param killer the killer
     * @return the boolean
     */
    public boolean death(Entity e, Entity killer) {
        return false;
    }

    /**
     * Interact boolean.
     *
     * @param e      the e
     * @param target the target
     * @param option the option
     * @return the boolean
     */
    public boolean interact(Entity e, Node target, Option option) {
        return false;
    }

    /**
     * Handle use with boolean.
     *
     * @param player the player
     * @param used   the used
     * @param with   the with
     * @return the boolean
     */
    public boolean handleUseWith(Player player, Item used, Node with) {
        return false;
    }

    /**
     * Action button boolean.
     *
     * @param player      the player
     * @param interfaceId the interface id
     * @param buttonId    the button id
     * @param slot        the slot
     * @param itemId      the item id
     * @param opcode      the opcode
     * @return the boolean
     */
    public boolean actionButton(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        return false;
    }

    /**
     * Continue attack boolean.
     *
     * @param e       the e
     * @param target  the target
     * @param style   the style
     * @param message the message
     * @return the boolean
     */
    public boolean continueAttack(Entity e, Node target, CombatStyle style, boolean message) {
        return true;
    }

    /**
     * Ignore multi boundaries boolean.
     *
     * @param attacker the attacker
     * @param victim   the victim
     * @return the boolean
     */
    public boolean ignoreMultiBoundaries(Entity attacker, Entity victim) {
        return false;
    }

    /**
     * Check multi boolean.
     *
     * @param e       the e
     * @param t       the t
     * @param message the message
     * @return the boolean
     */
    public static boolean checkMulti(Entity e, Entity t, boolean message) {
        long time = System.currentTimeMillis();
        boolean multi = t.getProperties().isMultiZone() && e.getProperties().isMultiZone();
        if (multi || e.isIgnoreMultiBoundaries(t) || e.getZoneMonitor().isIgnoreMultiBoundaries(t)) {
            return true;
        }
        Entity target = t.getAttribute("combat-attacker", e);
        if (t.getAttribute("combat-time", -1L) > time && target != e && target.isActive()) {
            if (message && e instanceof Player) {
                ((Player) e).getPacketDispatch().sendMessage("Someone else is already fighting this" + (t instanceof Player ? " player." : "."));
            }
            return false;
        }
        if (e.getAttribute("combat-time", -1L) > time && (target = e.getAttribute("combat-attacker", t)) != t && target.isActive()) {
            if (t.getId() == 1614 || t.getId() == 1613) {
                return true;
            }
            if (message && e instanceof Player) {
                ((Player) e).getPacketDispatch().sendMessage("You're already under attack!");
            }
            return false;
        }
        return true;
    }

    /**
     * Teleport boolean.
     *
     * @param e    the e
     * @param type the type
     * @param node the node
     * @return the boolean
     */
    public boolean teleport(Entity e, int type, Node node) {
        return true;
    }

    /**
     * Start death boolean.
     *
     * @param e      the e
     * @param killer the killer
     * @return the boolean
     */
    public boolean startDeath(Entity e, Entity killer) {
        return true;
    }

    /**
     * Can request boolean.
     *
     * @param type   the type
     * @param player the player
     * @param target the target
     * @return the boolean
     */
    public boolean canRequest(RequestType type, Player player, Player target) {
        return true;
    }

    /**
     * Move boolean.
     *
     * @param e    the e
     * @param from the from
     * @param to   the to
     * @return the boolean
     */
    public boolean move(Entity e, Location from, Location to) {
        return true;
    }

    /**
     * Parse command boolean.
     *
     * @param player    the player
     * @param name      the name
     * @param arguments the arguments
     * @return the boolean
     */
    public boolean parseCommand(Player player, String name, String[] arguments) {
        return false;
    }

    /**
     * Location update.
     *
     * @param e    the e
     * @param last the last
     */
    public void locationUpdate(Entity e, Location last) {

    }

    /**
     * Configure.
     */
    public void configure() {
    }

    ;

    /**
     * Clean items.
     *
     * @param player the player
     * @param items  the items
     */
    public void cleanItems(Player player, Item[] items) {
        if (player == null) {
            return;
        }
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            if (player.getInventory().containsItem(item)) {
                player.getInventory().remove(new Item(item.getId(), player.getInventory().getAmount(item)));
            }
            if (player.getEquipment().containsItem(item)) {
                player.getEquipment().remove(new Item(item.getId(), player.getEquipment().getAmount(item)));
            }
            if (player.getBank().containsItem(item)) {
                player.getBank().remove(new Item(item.getId(), player.getBank().getAmount(item)));
            }
        }
    }

    /**
     * Message.
     *
     * @param e       the e
     * @param message the message
     */
    protected static void message(Entity e, String message) {
        if (!(e instanceof Player)) {
            return;
        }
        ((Player) e).getPacketDispatch().sendMessage(message);
    }

    /**
     * Register.
     *
     * @param borders the borders
     */
    public void register(ZoneBorders borders) {
        for (Integer id : borders.getRegionIds()) {
            Region r = RegionManager.forId(id);
            if (r != null) {
                r.add(new RegionZone(this, borders));
            }
        }
    }

    /**
     * Unregister.
     *
     * @param borders the borders
     */
    public void unregister(ZoneBorders borders) {
        for (Integer id : borders.getRegionIds()) {
            Region r = RegionManager.forId(id);
            if (r != null) {
                r.remove(new RegionZone(this, borders));
            }
        }
    }

    /**
     * Register region.
     *
     * @param regionId the region id
     */
    public void registerRegion(int regionId) {
        register(ZoneBorders.forRegion(regionId));
    }

    /**
     * Register region.
     *
     * @param regionId the region id
     * @param borders  the borders
     */
    public void registerRegion(int regionId, ZoneBorders borders) {
        Region r = RegionManager.forId(regionId);
        if (r != null) {
            r.add(new RegionZone(this, borders));
        }
    }

    /**
     * Unregister region.
     *
     * @param regionId the region id
     */
    public void unregisterRegion(int regionId) {
        Region r = RegionManager.forId(regionId);
        if (r != null) {
            for (Iterator<RegionZone> it = r.getRegionZones().iterator(); it.hasNext(); ) {
                if (it.next().getZone() == this) {
                    it.remove();
                }
            }
        }
    }

    /**
     * Disable random events.
     */
    public void disableRandomEvents() {
        this.fireRandomEvents = false;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Is overlappable boolean.
     *
     * @return the boolean
     */
    public boolean isOverlappable() {
        return overlappable;
    }

    /**
     * Sets overlappable.
     *
     * @param overlappable the overlappable
     */
    public void setOverlappable(boolean overlappable) {
        this.overlappable = overlappable;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public int getUid() {
        return getName().hashCode();
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * Is fire randoms boolean.
     *
     * @return the boolean
     */
    public boolean isFireRandoms() {
        return fireRandomEvents;
    }

    /**
     * Is dynamic zone boolean.
     *
     * @return the boolean
     */
    public boolean isDynamicZone() {
        return false;
    }

    /**
     * Add restriction.
     *
     * @param restriction the restriction
     */
    public void addRestriction(ZoneRestriction restriction) {
        addRestriction(restriction.getFlag());
    }

    /**
     * Add restriction.
     *
     * @param flag the flag
     */
    public void addRestriction(int flag) {
        restriction |= flag;
    }

    /**
     * Is restricted boolean.
     *
     * @param flag the flag
     * @return the boolean
     */
    public boolean isRestricted(int flag) {
        return (restriction & flag) != 0;
    }

    /**
     * Gets restriction.
     *
     * @return the restriction
     */
    public int getRestriction() {
        return restriction;
    }

    /**
     * Gets zone type.
     *
     * @return the zone type
     */
    public int getZoneType() {
        return zoneType;
    }

    /**
     * Sets zone type.
     *
     * @param type the type
     */
    public void setZoneType(int type) {
        this.zoneType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapZone mapZone = (MapZone) o;
        return getUid() == mapZone.getUid();
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, overlappable, fireRandomEvents, restriction, zoneType);
    }
}
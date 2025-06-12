package content.region.asgarnia.gwd;

import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.container.impl.EquipmentContainer;
import core.game.global.action.DoorActionHandler;
import core.game.interaction.MovementPulse;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.RangeWeapon;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.Rights;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.map.zone.ZoneRestriction;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.Log;
import core.tools.StringUtils;

import static core.api.ContentAPIKt.*;
import static core.api.ui.InterfaceAPIKt.setMinimapState;

/**
 * The type Godwars zone.
 */
@Initializable
public final class GodwarsZone extends MapZone implements Plugin<java.lang.Object> {

    private static final ZoneBorders ZAMORAK_FORTRESS = new ZoneBorders(2880, 5317, 2944, 5362);

    static {
        ZAMORAK_FORTRESS.addException(new ZoneBorders(2880, 5317, 2904, 5338));
    }

    /**
     * Instantiates a new Godwars zone.
     */
    public GodwarsZone() {
        super("Godwars", true, ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON);
    }

    @Override
    public void configure() {
        register(new ZoneBorders(2816, 5248, 2943, 5375));
    }

    @Override
    public boolean enter(Entity e) {
        if (e instanceof Player) {
            Player player = (Player) e;
            int componentId = player.getInterfaceManager().isResizable() ? 597 : 601;
            if (ZAMORAK_FORTRESS.insideBorder(player.getLocation().getX(), player.getLocation().getY())) {
                componentId = player.getInterfaceManager().isResizable() ? 598 : 599;
            }
            openOverlay(player, componentId);
            if (player.getDetails().getRights() == Rights.ADMINISTRATOR) {
                for (GodWarsFaction faction : GodWarsFaction.values()) {
                    increaseKillcount(player, faction, 40);
                }
            }
        }
        return true;
    }

    /**
     * Sets rope setting.
     *
     * @param player  the player
     * @param setting the setting
     */
    public void setRopeSetting(Player player, int setting) {
        setVarbit(player, setting == 1 ? 3933 : 3934, 1, true);
    }

    private void openOverlay(Player player, int componentId) {
        setAttribute(player, "gwd:overlay", componentId);
        player.getInterfaceManager().openOverlay(new Component(componentId));
        int child = (componentId == 601 || componentId == 599) ? 6 : 7;
        for (GodWarsFaction faction : GodWarsFaction.values()) {
            int amount = player.getAttribute("gwd:" + faction.name().toLowerCase() + "kc", 0);
            player.getPacketDispatch().sendString(Integer.toString(amount), componentId, child + faction.ordinal());
        }
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        if (!logout && e instanceof Player) {
            for (GodWarsFaction faction : GodWarsFaction.values()) {
                e.removeAttribute("gwd:" + faction.name().toLowerCase() + "kc");
            }
            e.removeAttribute("gwd:overlay");
            e.removeAttribute("gwd:altar-recharge");
            ((Player) e).getInterfaceManager().closeOverlay();
        } else if (logout) {
            e.setLocation(e.getAttribute("cross_bridge_loc", e.getLocation()));
        }
        return true;
    }

    @Override
    public boolean death(Entity e, Entity killer) {
        if (killer instanceof Player && e instanceof NPC) {
            int npcId = e.getId();
            increaseKillcount((Player) killer, GodWarsFaction.forId(npcId), 1);
        }
        return false;
    }

    @Override
    public void locationUpdate(Entity e, Location last) {
        if (e instanceof Player) {
            Player player = (Player) e;
            Component c = player.getInterfaceManager().overlay;
            boolean inZamorakFortress = ZAMORAK_FORTRESS.insideBorder(player.getLocation().getX(), player.getLocation().getY());
            if ((c == null || c.id != 598) && inZamorakFortress) {
                openOverlay(player, 598);
            } else if ((c == null || c.id != 597 && c.id != 601) && !inZamorakFortress) {
                openOverlay(player, player.getInterfaceManager().isResizable() ? 597 : 601);
            }
        }
    }

    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if (target instanceof Scenery) {
            Scenery scenery = (Scenery) target;
            if (scenery.getId() == 26439) {
                handleIceBridge((Player) e, scenery);
                return true;
            }
            if (scenery.getId() == 26384) {
                handleBigDoor((Player) e, scenery, true);
                return true;
            }
            if (scenery.getId() == 26303) {
                handlePillarGrapple((Player) e, scenery);
                return true;
            }
            if (scenery.getId() == 26293) {
                handleRopeClimb((Player) e, Location.create(2915, 3746, 0));
                return true;
            }
            if (scenery.getId() == 26295) {
                handleRopeClimb((Player) e, Location.create(2915, 5300, 1));
                return true;
            }
            if (scenery.getId() == 26296) {
                handleRopeTie((Player) e, 1);
                return true;
            }
            if (scenery.getId() == 26297) {
                if (scenery.getLocation().getY() == 5300) {
                    handleRopeClimb((Player) e, Location.create(2912, 5300, 2));
                } else {
                    handleRopeClimb((Player) e, Location.create(2920, 5276, 1));
                }
                return true;
            }
            if (scenery.getId() == 26299) {
                handleRopeClimb((Player) e, Location.create(2919, 5274, 0));
                return true;
            }
            if (scenery.getId() == 26300) {
                handleRopeTie((Player) e, 2);
                return true;
            }
            if (scenery.getId() == 26286) {
                handleAltar((Player) e, option.getName(), GodWarsFaction.ZAMORAK, Location.create(2925, 5332, 2));
                return true;
            }
            if (scenery.getId() == 26287) {
                handleAltar((Player) e, option.getName(), GodWarsFaction.SARADOMIN, Location.create(2908, 5265, 0));
                return true;
            }
            if (scenery.getId() == 26288) {
                handleAltar((Player) e, option.getName(), GodWarsFaction.ARMADYL, Location.create(2839, 5295, 2));
                return true;
            }
            if (scenery.getId() == 26289) {
                handleAltar((Player) e, option.getName(), GodWarsFaction.BANDOS, Location.create(2863, 5354, 2));
                return true;
            }
            if (scenery.getId() >= 26425 && scenery.getId() <= 26428) {
                return handleChamberEntrance((Player) e, scenery);
            }
        }
        return false;
    }

    private void handleAltar(Player player, String option, GodWarsFaction faction, Location destination) {
        if (!option.equals("Pray-at")) {
            player.getProperties().setTeleportLocation(destination);
            return;
        }
        if (player.getAttribute("gwd:altar-recharge", 0L) > System.currentTimeMillis()) {
            player.getPacketDispatch().sendMessage("The gods blessed you recently - this time they ignore your prayers.");
            return;
        }
        if (player.inCombat()) {
            player.getPacketDispatch().sendMessage("You can't use the altar while in combat.");
            return;
        }
        if (player.getSkills().getPrayerPoints() >= player.getSkills().getStaticLevel(5)) {
            player.getPacketDispatch().sendMessage("You already have full prayer points.");
            return;
        }
        player.lock(2);
        int total = player.getSkills().getStaticLevel(5) + faction.getProtectionItemAmount(player);
        player.animate(new Animation(645));
        player.getSkills().decrementPrayerPoints(player.getSkills().getPrayerPoints() - total);
        player.getPacketDispatch().sendMessage("You recharge your Prayer points.");
        int time = 600_000;
        setAttribute(player, "/save:gwd:altar-recharge", System.currentTimeMillis() + time);
    }

    private void handleRopeTie(Player player, int type) {
        if (player.getSkills().getStaticLevel(Skills.AGILITY) < 70) {
            player.getPacketDispatch().sendMessage("You need an agility level of 70 to enter here.");
            return;
        }
        if (!player.getInventory().remove(new Item(954))) {
            player.getPacketDispatch().sendMessage("You don't have a rope to tie on this rock.");
            return;
        }
        setRopeSetting(player, type);
    }

    private void handleRopeClimb(final Player player, final Location destination) {
        player.lock(2);
        player.animate(Animation.create(828));
        GameWorld.getPulser().submit(new Pulse(1, player) {
            @Override
            public boolean pulse() {
                player.getProperties().setTeleportLocation(destination);
                return true;
            }
        });
    }

    private void handlePillarGrapple(final Player player, final Scenery scenery) {
        if (player.getSkills().getStaticLevel(Skills.RANGE) < 70) {
            player.getPacketDispatch().sendMessage("You need a Range level of 70 to enter here.");
            return;
        }
        if (player.getEquipment().getNew(EquipmentContainer.SLOT_ARROWS).getId() != 9419) {
            player.getPacketDispatch().sendMessage("You need a mithril grapple to cross this.");
            return;
        }
        RangeWeapon weapon = RangeWeapon.get(player.getEquipment().getNew(3).getId());
        if (weapon == null || weapon.getType() != 1) {
            player.getPacketDispatch().sendMessage("You need to wield a crossbow to fire a mithril grapple.");
            return;
        }
        player.lock(4);
        if (player.getLocation().getY() < scenery.getLocation().getY()) {
            ForceMovement.run(player, Location.create(2872, 5269, 2), Location.create(2872, 5279, 2), Animation.create(7081), 60).setCommenceSpeed(3);
        } else {
            ForceMovement.run(player, Location.create(2872, 5279, 2), Location.create(2872, 5269, 2), Animation.create(7081), 60).setCommenceSpeed(3);
        }
        player.graphics(new Graphics(1036, 96, 30));
    }

    private void handleBigDoor(final Player player, final Scenery scenery, boolean checkLocation) {
        player.lock(4);
        if (checkLocation && player.getLocation().getX() > scenery.getLocation().getX()) {
            GameWorld.getPulser().submit(new MovementPulse(player, scenery.getLocation()) {
                @Override
                public boolean pulse() {
                    handleBigDoor(player, scenery, false);
                    return true;
                }
            });
            return;
        }
        if (player.getSkills().getStaticLevel(Skills.STRENGTH) < 70) {
            player.getPacketDispatch().sendMessage("You need a Strength level of 70 to enter here.");
            return;
        }
        if (!player.getInventory().contains(2347, 1)) {
            player.getPacketDispatch().sendMessage("You need a hammer to bang on the door.");
            return;
        }
        player.getPacketDispatch().sendMessage("You bang on the big door.");
        player.animate(Animation.create(7002));
        GameWorld.getPulser().submit(new Pulse(1, player) {
            @Override
            public boolean pulse() {
                scenery.getDefinition().getOptions()[1] = "open";
                SceneryDefinition.getOptionHandler(scenery.getId(), "open").handle(player, scenery, "open");
                return true;
            }
        });
    }

    private boolean handleChamberEntrance(Player player, Scenery scenery) {
        Direction dir = Direction.get((scenery.getRotation() + 3) % 4);
        if (dir.getStepX() != 0) {
            if (player.getLocation().getX() == scenery.getLocation().transform(dir.getStepX(), 0, 0).getX()) {
                player.getPacketDispatch().sendMessage("You can't leave through this door. The altar can teleport you out.");
                return true;
            }
        } else if (player.getLocation().getY() == scenery.getLocation().transform(0, dir.getStepY(), 0).getY()) {
            player.getPacketDispatch().sendMessage("You can't leave through this door. The altar can teleport you out.");
            return true;
        }
        int index = scenery.getId() - 26425;
        if (index < 2) {
            index = 1 - index;
        }
        GodWarsFaction faction = GodWarsFaction.values()[index];
        String name = faction.name().toLowerCase();
        int required = 40;
        if (player.getAttribute("gwd:" + name + "kc", 0) < required) {
            player.getPacketDispatch().sendMessage("You need " + required + " " + StringUtils.formatDisplayName(name) + " kills to enter this.");
            return true;
        }

        if (DoorActionHandler.handleAutowalkDoor(player, scenery)) {
            log(this.getClass(), Log.FINE, player.getUsername() + " entered " + faction.name() + " gwd boss room");
            increaseKillcount(player, faction, -required);
        }
        return true;
    }

    private void handleIceBridge(final Player player, final Scenery scenery) {
        if (player.getSkills().getStaticLevel(Skills.HITPOINTS) < 70) {
            player.getPacketDispatch().sendMessage("You need 70 Hitpoints to cross this bridge.");
            return;
        }
        player.lock(7);
        GameWorld.getPulser().submit(new Pulse(1, player) {
            @Override
            public boolean pulse() {
                player.visualize(Animation.create(6988), Graphics.create(68));
                int diffY = 2;
                if (scenery.getLocation().getY() == 5344) {
                    diffY = -2;
                }
                player.getProperties().setTeleportLocation(player.getLocation().transform(0, diffY, 0));
                player.getInterfaceManager().openOverlay(new Component(115));
                setAttribute(player, "cross_bridge_loc", player.getLocation());
                GameWorld.getPulser().submit(new Pulse(1, player) {
                    int counter = 0;

                    @Override
                    public boolean pulse() {
                        switch (counter++) {
                            case 4:
                                if (scenery.getLocation().getY() == 5333) {
                                    player.getProperties().setTeleportLocation(Location.create(2885, 5345, 2));
                                } else {
                                    player.getProperties().setTeleportLocation(Location.create(2885, 5332, 2));
                                }
                                player.setDirection(Direction.get((player.getDirection().toInteger() + 2) % 4));
                                break;
                            case 5:
                                setMinimapState(player, 0);
                                player.getInterfaceManager().close();
                                removeAttribute(player, "cross_bridge_loc");
                                player.getPacketDispatch().sendMessage("Dripping, you climb out of the water.");
                                if (player.getLocation().getY() > 5340) {
                                    player.getSkills().decrementPrayerPoints(100.0);
                                    player.getPacketDispatch().sendMessage("The extreme evil of this area leaves your Prayer drained.");
                                }
                                return true;
                        }
                        return false;
                    }
                });
                return true;
            }
        });
    }

    /**
     * Increase killcount.
     *
     * @param p        the p
     * @param faction  the faction
     * @param increase the increase
     */
    public void increaseKillcount(Player p, GodWarsFaction faction, int increase) {
        if (faction == null) {
            return;
        }
        String key = "gwd:" + faction.name().toLowerCase() + "kc";
        int amount = p.getAttribute(key, 0) + increase;
        int componentId = p.getAttribute("gwd:overlay", 601);
        int child = (componentId == 601 || componentId == 599) ? 6 : 7;
        if (amount >= 4000) {
            p.setAttribute("/save:" + key, 4000);
            p.getPacketDispatch().sendString("Max", componentId, child + faction.ordinal());
            return;
        }
        p.setAttribute("/save:" + key, amount);
        p.getPacketDispatch().sendString(Integer.toString(amount), componentId, child + faction.ordinal());
    }

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        ZoneBuilder.configure(this);
        return this;
    }

    @Override
    public java.lang.Object fireEvent(String identifier, java.lang.Object... args) {
        return null;
    }

    /**
     * The enum God wars faction.
     */
    enum GodWarsFaction {

        /**
         * Armadyl god wars faction.
         */
        ARMADYL(6222, 6246, 87, 11694, 11718, 11720, 11722, 12670, 12671, 14671),

        /**
         * Bandos god wars faction.
         */
        BANDOS(6260, 6283, 11061, 11696, 11724, 11726, 11728),

        /**
         * Saradomin god wars faction.
         */
        SARADOMIN(6247, 6259, 1718, 2412, 2415, 2661, 2663, 2665, 2667, 3479, 3675, 3489, 3840, 4682, 6762, 8055, 10384, 10386, 10388, 10390, 10440, 10446, 10452, 10458, 10464, 10470, 11181, 11698, 11730),

        /**
         * Zamorak god wars faction.
         */
        ZAMORAK(6203, 6221, 11716, 11700, 2414, 2417, 2653, 2655, 2657, 2659, 3478, 3674, 3841, 3842, 3852, 4683, 6764, 8056, 10368, 10370, 10372, 10374, 10444, 10450, 10456, 10460, 10468, 10474, 10776, 10786, 10790);

        private final int startId;
        private final int endId;
        private final int[] protectionItems;

        GodWarsFaction(int startId, int endId, int... protectionItems) {
            this.startId = startId;
            this.endId = endId;
            this.protectionItems = protectionItems;
        }

        /**
         * For id god wars faction.
         *
         * @param npcId the npc id
         * @return the god wars faction
         */
        public static GodWarsFaction forId(int npcId) {
            for (GodWarsFaction faction : values()) {
                if (npcId >= faction.getStartId() && npcId <= faction.getEndId()) {
                    return faction;
                }
            }
            return null;
        }

        /**
         * Gets protection item amount.
         *
         * @param player the player
         * @return the protection item amount
         */
        public int getProtectionItemAmount(Player player) {
            int count = 0;
            for (Item item : player.getEquipment().toArray()) {
                if (item != null) {
                    for (int id : protectionItems) {
                        if (item.getId() == id) {
                            count++;
                        }
                    }
                }
            }
            return count;
        }

        /**
         * Gets start id.
         *
         * @return the start id
         */
        public int getStartId() {
            return startId;
        }

        /**
         * Gets end id.
         *
         * @return the end id
         */
        public int getEndId() {
            return endId;
        }

        /**
         * Get protection items int [ ].
         *
         * @return the int [ ]
         */
        public int[] getProtectionItems() {
            return protectionItems;
        }
    }

}

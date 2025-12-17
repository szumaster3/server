package content.global.activity.godwars;

import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.interaction.MovementPulse;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
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

import static core.api.ContentAPIKt.*;

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
                    GodWarsFaction.increaseKillCount(player, faction, 40);
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
            GodWarsFaction.increaseKillCount((Player) killer, GodWarsFaction.forId(npcId), 1);
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
        }
        return false;
    }

    private void handleAltar(Player player, String option, GodWarsFaction faction, Location destination) {
        if (!option.equals("Pray-at")) {
            player.getProperties().setTeleportLocation(destination);
            return;
        }

        if (player.getAttribute("gwd:altar-recharge", 0L) > System.currentTimeMillis()) {
            player.getPacketDispatch().sendMessage(
                    "The gods blessed you recently - this time they ignore your prayers."
            );
            return;
        }

        if (player.inCombat()) {
            player.getPacketDispatch().sendMessage("You can't use the altar while in combat.");
            return;
        }

        if (player.getSkills().getPrayerPoints() >= player.getSkills().getStaticLevel(5)) {
            player.getPacketDispatch().sendMessage("You already have full Prayer points.");
            return;
        }

        player.lock(2);

        int total = player.getSkills().getStaticLevel(5) +
                GodWarsFaction.getProtectionItemAmount(player, faction.getGod());

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

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        ZoneBuilder.configure(this);
        return this;
    }

    @Override
    public java.lang.Object fireEvent(String identifier, java.lang.Object... args) {
        return null;
    }

}

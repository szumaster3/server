package content.region.kandarin.quest.mcannon.handlers;

import org.rs.consts.Sounds;
import core.api.LogoutListener;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.zone.ZoneRestriction;
import core.game.world.update.flag.context.Animation;
import core.tools.RandomFunction;
import org.jetbrains.annotations.NotNull;

import static core.api.ContentAPIKt.*;

/**
 * The type Dmc handler.
 */
public final class DMCHandler implements LogoutListener {

    private final Player player;

    private Scenery cannon;

    private int cannonballs;

    private DMCRevolution direction = DMCRevolution.NORTH;
    private CannonTimer timer;

    /**
     * Instantiates a new Dmc handler.
     */
    public DMCHandler() {
        this.player = null;
    }

    /**
     * Instantiates a new Dmc handler.
     *
     * @param player the player
     */
    public DMCHandler(final Player player) {
        this.player = player;
    }

    /**
     * Rotate boolean.
     *
     * @return the boolean
     */
    public boolean rotate() {
        if (cannonballs < 1) {
            player.getPacketDispatch().sendMessage("Your cannon has run out of ammo!");
            return false;
        }
        player.getPacketDispatch().sendSceneryAnimation(cannon, Animation.create(direction.getAnimationId()));
        Location l = cannon.getLocation().transform(1, 1, 0);
        playGlobalAudio(l, Sounds.MCANNON_TURN_2877);
        direction = DMCRevolution.values()[(direction.ordinal() + 1) % DMCRevolution.values().length];
        for (NPC npc : RegionManager.getLocalNpcs(l, 10)) {
            if (direction.isInSight(npc.getLocation().getX() - l.getX(), npc.getLocation().getY() - l.getY()) && npc.isAttackable(player, CombatStyle.RANGE, false) && CombatSwingHandler.isProjectileClipped(npc, l, false)) {
                int speed = (int) (25 + (l.getDistance(npc.getLocation()) * 10));
                Projectile.create(l, npc.getLocation(), 53, 40, 36, 20, speed, 0, 128).send();
                playGlobalAudio(l, Sounds.MCANNON_FIRE_1667);
                cannonballs--;
                int hit = 0;
                if (player.getSwingHandler(false).isAccurateImpact(player, npc, CombatStyle.RANGE, 1.2, 1.0)) {
                    hit = RandomFunction.getRandom(30);
                }
                player.getSkills().addExperience(Skills.RANGE, hit * 2);
                npc.getImpactHandler().manualHit(player, hit, HitsplatType.NORMAL, (int) Math.ceil(l.getDistance(npc.getLocation()) * 0.3));
                npc.attack(player);
                break;
            }
        }
        return true;
    }

    /**
     * Start firing.
     */
    public void startFiring() {
        if (cannon == null || !cannon.isActive()) {
            player.getPacketDispatch().sendMessage("You don't have a cannon active.");
            return;
        }
        if (timer.isFiring()) {
            timer.setFiring(false);
            return;
        }
        if (cannonballs < 1) {
            int amount = player.getInventory().getAmount(new Item(2));
            if (amount < 1) {
                player.getPacketDispatch().sendMessage("Your cannon is out of cannonballs.");
                return;
            }
            int toUse = 30 - cannonballs;
            if (toUse > amount) {
                toUse = amount;
            }
            if (toUse > 0) {
                cannonballs = toUse;
                player.getPacketDispatch().sendMessage("You load the cannon with " + toUse + " cannonballs.");
                player.getInventory().remove(new Item(2, toUse));
            } else {
                player.sendMessage("Your cannon is already fully loaded.");
            }
        }
        timer.setFiring(true);
    }

    /**
     * Explode.
     *
     * @param decay the decay
     */
    public void explode(boolean decay) {
        if (!cannon.isActive()) {
            return;
        }
        player.sendMessage("Your cannon has " + (decay ? "decayed" : "been destroyed") + "!");
        for (Player p : RegionManager.getLocalPlayers(player)) {
            p.getPacketDispatch().sendPositionedGraphic(189, 0, 1, cannon.getLocation());
        }
        clear(false);
    }

    /**
     * Construct.
     *
     * @param player the player
     */
    public static void construct(final Player player) {
        final Location spawn = RegionManager.getSpawnLocation(player, new Scenery(6, player.getLocation()));
        if (spawn == null) {
            player.getPacketDispatch().sendMessage("There's not enough room for your cannon.");
            return;
        }
        if (player.getZoneMonitor().isRestricted(ZoneRestriction.CANNON)) {
            player.getPacketDispatch().sendMessage("You can't set up a cannon here.");
            return;
        }
        final DMCHandler handler = new DMCHandler(player);
        setAttribute(player, "dmc", handler);
        player.getPulseManager().clear();
        player.getWalkingQueue().reset();
        player.lock(9);
        player.faceLocation(spawn.transform(Direction.NORTH_EAST));
        GameWorld.getPulser().submit(new Pulse(2, player) {
            int count = 0;
            Scenery scenery = null;

            @Override
            public boolean pulse() {
                player.animate(Animation.create(827));
                if (!player.getInventory().remove(new Item(6 + (count * 2)))) {
                    for (int i = count - 1; i >= 0; i--) {
                        player.getInventory().add(new Item(6 + (i * 2)));
                    }
                    if (scenery != null) {
                        SceneryBuilder.remove(scenery);
                    }
                    return true;
                }
                switch (count) {
                    case 0:
                        scenery = SceneryBuilder.add(new Scenery(7, spawn));
                        player.getPacketDispatch().sendMessage("You place the cannon base on the ground.");
                        break;
                    case 1:
                        player.getPacketDispatch().sendMessage("You add the stand.");
                        break;
                    case 2:
                        player.getPacketDispatch().sendMessage("You add the barrels.");
                        break;
                    case 3:
                        player.getPacketDispatch().sendMessage("You add the furnace.");
                        SceneryBuilder.remove(scenery);
                        handler.configure(SceneryBuilder.add(scenery = scenery.transform(6)));
                        handler.timer = (CannonTimer) spawnTimer("dmc:timer", handler);
                        registerTimer(player, handler.timer);
                        return true;
                }
                playGlobalAudio(player.getLocation(), Sounds.MCANNON_SETUP_2876);
                if (count != 0) {
                    SceneryBuilder.remove(scenery);
                    SceneryBuilder.add(scenery = scenery.transform(scenery.getId() + 1));
                }
                return ++count == 4;
            }
        });
    }

    /**
     * Configure.
     *
     * @param cannon the cannon
     */
    public void configure(Scenery cannon) {
        this.cannon = cannon;
    }

    @Override
    public void logout(@NotNull Player player) {
        if (player.getAttribute("dmc") != null) {
            DMCHandler handler = player.getAttribute("dmc");
            handler.clear(false);
        }
    }

    /**
     * Clear.
     *
     * @param pickup the pickup
     */
    public void clear(boolean pickup) {
        SceneryBuilder.remove(cannon);
        removeAttribute(player, "dmc");
        if (!pickup) {
            player.getSavedData().activityData.setLostCannon(true);
            return;
        }
        for (int i = 0; i < 4; i++) {
            player.getInventory().add(new Item(12 - (i * 2)));
        }
        if (cannonballs > 0) {
            player.getInventory().add(new Item(2, cannonballs));
            cannonballs = 0;
        }
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets cannon.
     *
     * @return the cannon
     */
    public Scenery getCannon() {
        return cannon;
    }

    /**
     * Sets cannon.
     *
     * @param cannon the cannon
     */
    public void setCannon(Scenery cannon) {
        this.cannon = cannon;
    }

    /**
     * Gets cannonballs.
     *
     * @return the cannonballs
     */
    public int getCannonballs() {
        return cannonballs;
    }

    /**
     * Sets cannonballs.
     *
     * @param cannonballs the cannonballs
     */
    public void setCannonballs(int cannonballs) {
        this.cannonballs = cannonballs;
    }

}

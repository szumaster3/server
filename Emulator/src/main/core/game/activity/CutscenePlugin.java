package core.game.activity;

import core.game.component.Component;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.build.DynamicRegion;
import core.net.packet.PacketRepository;
import core.net.packet.context.MinimapStateContext;
import core.net.packet.out.MinimapState;
import core.plugin.PluginManifest;
import core.plugin.PluginType;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Cutscene plugin.
 */
@PluginManifest(type = PluginType.ACTIVITY)
public abstract class CutscenePlugin extends ActivityPlugin {

    private static final int[] TABS = new int[]{0, 1, 2, 3, 4, 5, 6, 11, 12};

    /**
     * The Npcs.
     */
    protected final List<NPC> npcs = new ArrayList<>(100);

    private final StartPulse startPulse = new StartPulse();

    private final EndPulse endPulse = new EndPulse();

    private final boolean fade;

    /**
     * Instantiates a new Cutscene plugin.
     *
     * @param name the name
     * @param fade the fade
     */
    public CutscenePlugin(String name, final boolean fade) {
        super(name, true, false, true);
        this.fade = fade;
    }

    /**
     * Instantiates a new Cutscene plugin.
     *
     * @param name the name
     */
    public CutscenePlugin(final String name) {
        this(name, true);
    }

    @Override
    public boolean start(final Player player, boolean login, Object... args) {
        player.setAttribute("cutscene:original-loc", player.getLocation());
        player.removeAttribute("real-end");
        player.setAttribute("real-end", player.getLocation());
        if (isFade()) {
            GameWorld.getPulser().submit(getStartPulse());
        } else {
            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, getMapState()));
            player.getInterfaceManager().removeTabs(getRemovedTabs());
            player.getProperties().setTeleportLocation(getStartLocation());
            player.unlock();
            player.getWalkingQueue().reset();
            player.getLocks().lockMovement(1000000);
            player.getInterfaceManager().close();
            open();
        }
        player.lock();
        return super.start(player, login, args);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean leave(final Entity e, boolean logout) {
        if (player != null) {
            if (logout) {
                player.setLocation(player.getAttribute("cutscene:original-loc", player.getLocation()));
                end();
            } else {
                unpause();
            }
            player.unlock();
            player.getInterfaceManager().openDefaultTabs();
            player.getWalkingQueue().reset();
            player.getLocks().unlockMovement();
        }
        return super.leave(e, logout);
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Stop.
     *
     * @param fade the fade
     */
    public void stop(boolean fade) {
        if (fade) {
            GameWorld.getPulser().submit(endPulse);
        } else {
            end();
        }
    }

    /**
     * End.
     */
    public void end() {
        if (region != null) {
            for (int i = 0; i < region.getPlanes().length; i++) {
                for (NPC n : region.getPlanes()[i].getNpcs()) {
                    if (n == null) {
                        continue;
                    }
                    n.clear();
                }
            }
        }
        PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
        player.getInterfaceManager().restoreTabs();
        player.unlock();
        player.getWalkingQueue().reset();
    }

    /**
     * The type Start pulse.
     */
    public class StartPulse extends Pulse {

        private int counter = 0;

        /**
         * Instantiates a new Start pulse.
         */
        public StartPulse() {
            super(1, player);
        }

        @Override
        public boolean pulse() {
            switch (counter++) {
                case 1:
                    player.lock();
                    player.getInterfaceManager().openOverlay(new Component(115));
                    break;
                case 3:
                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, getMapState()));
                    player.getInterfaceManager().removeTabs(getRemovedTabs());
                    break;
                case 4:
                    player.getProperties().setTeleportLocation(getStartLocation());
                    break;
                case 5:
                    player.getInterfaceManager().closeOverlay();
                    player.getInterfaceManager().close();
                    player.unlock();
                    player.getWalkingQueue().reset();
                    player.getLocks().lockMovement(1000000);
                    open();
                    return true;
            }
            return false;
        }

    }

    /**
     * The type End pulse.
     */
    public class EndPulse extends Pulse {

        private int counter = 0;

        /**
         * Instantiates a new End pulse.
         */
        public EndPulse() {
            super(1, player);
        }

        @Override
        public boolean pulse() {
            switch (counter++) {
                case 1:
                    player.lock();
                    player.getInterfaceManager().openOverlay(new Component(115));
                    break;
                case 3:
                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, getMapState()));
                    player.getInterfaceManager().removeTabs(getRemovedTabs());
                    break;
                case 4:
                    Location loc = (Location) (player.getAttribute("real-end", player.getAttribute("cutscene:original-loc", player.getLocation())));
                    player.getProperties().setTeleportLocation(loc);
                    break;
                case 5:
                    end();
                    stop();
                    fade();
                    if (player.getSession().isActive()) {
                        PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                    }
                    player.getInterfaceManager().closeOverlay();
                    if (player.getSession().isActive()) {
                        player.getInterfaceManager().close();
                    }
                    player.getProperties().setSafeZone(false);
                    return true;
            }
            return false;
        }

    }

    /**
     * Open.
     */
    public void open() {

    }

    /**
     * Fade.
     */
    public void fade() {

    }

    /**
     * Gets map state.
     *
     * @return the map state
     */
    public int getMapState() {
        return 2;
    }

    /**
     * Get removed tabs int [ ].
     *
     * @return the int [ ]
     */
    public int[] getRemovedTabs() {
        return TABS;
    }

    /**
     * Gets start location.
     *
     * @return the start location
     */
    public Location getStartLocation() {
        return getBase();
    }

    /**
     * Unpause.
     */
    public final void unpause() {
        stop(true);
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Gets region.
     *
     * @return the region
     */
    public DynamicRegion getRegion() {
        return region;
    }

    /**
     * Gets npcs.
     *
     * @return the npcs
     */
    public List<NPC> getNPCS() {
        return npcs;
    }

    /**
     * Is fade boolean.
     *
     * @return the boolean
     */
    public boolean isFade() {
        return fade;
    }

    /**
     * Gets start pulse.
     *
     * @return the start pulse
     */
    public Pulse getStartPulse() {
        return startPulse;
    }

    /**
     * Gets end pulse.
     *
     * @return the end pulse
     */
    public Pulse getEndPulse() {
        return new EndPulse();
    }
}

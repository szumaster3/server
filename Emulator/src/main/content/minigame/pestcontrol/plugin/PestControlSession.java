package content.minigame.pestcontrol.plugin;

import core.game.component.Component;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.game.world.map.Point;
import core.game.world.map.RegionPlane;
import core.game.world.map.build.DynamicRegion;
import core.tools.RandomFunction;

import java.util.*;

import static core.api.ContentAPIKt.setAttribute;
import static core.api.ContentAPIKt.setVarp;

/**
 * The type Pest control session.
 */
public final class PestControlSession {

    /**
     * The constant INVALID_OBJECT_IDS.
     */
    public static final int[] INVALID_OBJECT_IDS = {
        14230, 14231, 14232,
        14245, 14246, 14247, 14248,
    };
    private static final Point[] OBJECT_OFFSETS = {new Point(46, 33), new Point(46, 32), new Point(32, 25), new Point(33, 25), new Point(19, 32), new Point(19, 33), new Point(49, 33), new Point(49, 32), new Point(49, 31), new Point(49, 30), new Point(52, 27), new Point(52, 26), new Point(52, 25), new Point(52, 24), new Point(52, 14), new Point(52, 13), new Point(52, 12), new Point(52, 11), new Point(42, 18), new Point(43, 18), new Point(44, 18), new Point(45, 18), new Point(32, 15), new Point(33, 15), new Point(34, 15), new Point(35, 15), new Point(23, 18), new Point(24, 18), new Point(25, 18), new Point(26, 18), new Point(13, 11), new Point(13, 12), new Point(13, 13), new Point(13, 14), new Point(12, 28), new Point(12, 29), new Point(12, 30), new Point(12, 31),};
    private final DynamicRegion region;

    private final PestControlActivityPlugin activity;

    private final List<Scenery> barricades = new ArrayList<>(20);
    private final List<NPC> aportals = new ArrayList<>(20);
    private final NPC[] portals = new NPC[4];
    private int ticks;
    private NPC squire;
    private Integer[] ids = new Integer[4];

    private boolean active = true;

    /**
     * Instantiates a new Pest control session.
     *
     * @param region   the region
     * @param activity the activity
     */
    public PestControlSession(DynamicRegion region, PestControlActivityPlugin activity) {
        this.region = region;
        this.activity = activity;
    }

    /**
     * Update boolean.
     *
     * @return the boolean
     */
    public boolean update() {
        if (!region.isActive()) {
            return true;
        }
        if (squire != null && !squire.isActive()) {
            activity.end(this, false);
            return true;
        }
        if (ticks % 100 == 0) {
            sendString(20 - (ticks / 100) + " min", 0);
        }
        switch (++ticks) {
            case 50:
            case 100:
            case 150:
            case 200:
                int index = (ticks / 50) - 1;
                removePortalShield(ids[index]);
                return false;
            case 20_00:
                activity.end(this, true);
                return true;
        }
        return false;
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        for (Player p : region.getPlanes()[0].getPlayers()) {
            if (p.isActive()) {
                p.getPacketDispatch().sendMessage(message);
            }
        }
    }

    /**
     * Send string.
     *
     * @param message the message
     * @param child   the child
     */
    public void sendString(String message, int child) {
        for (Player p : region.getPlanes()[0].getPlayers()) {
            if (p.isActive()) {
                p.getPacketDispatch().sendString(message, 408, child);
            }
        }
    }

    /**
     * Add zeal gained.
     *
     * @param player the player
     * @param zeal   the zeal
     */
    public void addZealGained(Player player, int zeal) {
        int total = zeal + player.getAttribute("pc_zeal", 0);
        setAttribute(player, "pc_zeal", total);
        String col = total > 50 ? "<col=00FF00>" : "";
        if (total > 350) {
            player.getPacketDispatch().sendString(col + "LOTS!", 408, 11);
        } else {
            player.getPacketDispatch().sendString(col + total, 408, 11);
        }
    }

    /**
     * Send config.
     *
     * @param value the value
     */
    public void sendConfig(int value) {
        for (Player p : region.getPlanes()[0].getPlayers()) {
            if (p.isActive()) {
                setVarp(p, 719, value);
            }
        }
    }

    /**
     * Remove portal shield.
     *
     * @param index the index
     */
    public void removePortalShield(int index) {
        String message = null;
        switch (index) {
            case 0:
                message = "The purple, western portal shield has dropped!";
                break;
            case 1:
                message = "The blue, eastern portal shield has dropped!";
                break;
            case 2:
                message = "The yellow, south-eastern portal shield has dropped!";
                break;
            case 3:
                message = "The red, south-western portal shield has dropped!";
                break;
        }
        for (Player p : region.getPlanes()[0].getPlayers()) {
            if (p.isActive()) {
                p.getPacketDispatch().sendInterfaceConfig(408, 18 + (index << 1), true);
                p.getPacketDispatch().sendMessage(message);
            }
        }
        squire.sendChat(message);
        NPC portal = portals[index];

        portal.reTransform();
        aportals.add(portal);
    }

    /**
     * Start game.
     *
     * @param waitingPlayers the waiting players
     */
    public void startGame(PriorityQueue<Player> waitingPlayers) {
        region.flagActive();
        initBarricadesList();
        List<Integer> list = new ArrayList<>(20);
        for (int i = 0; i < 4; i++) {
            list.add(i);
        }
        Collections.shuffle(list, RandomFunction.RANDOM);
        region.toggleMulticombat();
        region.setMusicId(588);
        ids = list.toArray(new Integer[4]);
        int count = 0;
        String portalHealth = "<col=00FF00>" + (activity.getType().ordinal() == 0 ? 200 : 250);

        List<Player> remainingPlayers = new ArrayList<>();
        for (Player p = waitingPlayers.poll(); p != null; p = waitingPlayers.poll()) {
            if (p.getSession().isActive()) {
                if (++count > PestControlActivityPlugin.MAX_TEAM_SIZE) {
                    remainingPlayers.add(p);
                    continue;
                }
                addPlayer(p, portalHealth);
            }
        }

        for (Player p : remainingPlayers) {
            int priority = p.getAttribute("pc_prior", 0) + 1;
            p.getPacketDispatch().sendMessage("You have been given priority level " + priority + " over other players in joining the next");
            p.getPacketDispatch().sendMessage("game.");
            p.setAttribute("pc_prior", priority);
            waitingPlayers.add(p);
        }

        spawnNPCs();

    }

    private void addPlayer(Player p, String portalHealth) {
        p.lock(1);
        p.setAttribute("pc_zeal", 0);
        p.removeAttribute("pc_prior");
        p.addExtension(PestControlSession.class, this);
        p.getInterfaceManager().openOverlay(new Component(408));
        p.getPacketDispatch().sendString("200", 408, 1);
        for (int i = 0; i < 4; i++) {
            p.getPacketDispatch().sendString(portalHealth, 408, 13 + i);
        }
        Random r = RandomFunction.RANDOM;
        Location l = region.getBaseLocation();
        p.getProperties().setTeleportLocation(l.transform(32 + r.nextInt(4), 49 + r.nextInt(6), 0));
        setVarp(p, 1147, 0);
        p.getDialogueInterpreter().sendDialogues(3781, FaceAnim.ANGRY, "You must defend the Void Knight while the portals are", "unsummoned. The ritual takes twenty minutes though,", "so you can help out by destroying them yourselves!", "Now GO GO GO!");
    }

    private void initBarricadesList() {
        RegionPlane p = region.getPlanes()[0];
        for (Point point : OBJECT_OFFSETS) {
            barricades.add(p.getObjects()[point.getX()][point.getY()]);
        }
    }

    private void spawnNPCs() {
        Location l = region.getBaseLocation();
        int baseId = 6142;
        if (activity.getType().ordinal() == 1) {
            baseId = 6150;
        } else if (activity.getType().ordinal() == 2) {
            baseId = 7551;
        }
        portals[0] = NPC.create(baseId, l.transform(4, 31, 0));
        portals[1] = NPC.create(baseId + 1, l.transform(56, 28, 0));
        portals[2] = NPC.create(baseId + 2, l.transform(45, 10, 0));
        portals[3] = NPC.create(baseId + 3, l.transform(21, 9, 0));
        addNPC(squire = NPC.create(3782 + (RandomFunction.RANDOM.nextBoolean() ? 3 : 0), l.transform(32, 32, 0)));
        for (NPC npc : portals) {
            addNPC(npc);
            npc.transform(npc.getId() + 4);
        }
        addNPC(NPC.create(3781, l.transform(30, 47, 0)));
    }

    /**
     * Add npc npc.
     *
     * @param npc the npc
     * @return the npc
     */
    public NPC addNPC(NPC npc) {
        npc.addExtension(PestControlSession.class, this);
        npc.setAttribute("no-spawn-return", true);
        npc.init();
        return npc;
    }

    /**
     * Gets ticks.
     *
     * @return the ticks
     */
    public int getTicks() {
        return ticks;
    }

    /**
     * Gets activity.
     *
     * @return the activity
     */
    public PestControlActivityPlugin getActivity() {
        return activity;
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
     * Gets squire.
     *
     * @return the squire
     */
    public NPC getSquire() {
        return squire;
    }

    /**
     * Get portals npc [ ].
     *
     * @return the npc [ ]
     */
    public NPC[] getPortals() {
        return portals;
    }

    /**
     * Gets aportals.
     *
     * @return the aportals
     */
    public List<NPC> getAportals() {
        return aportals;
    }

    /**
     * Is active boolean.
     *
     * @return the boolean
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets active.
     *
     * @param active the active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets barricades.
     *
     * @return the barricades
     */
    public List<Scenery> getBarricades() {
        return barricades;
    }

}

package core.game.bots;

import content.region.island.tutorial.plugin.CharacterDesign;
import core.ServerConstants;
import core.game.interaction.DestinationFlag;
import core.game.interaction.MovementPulse;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.impl.PulseType;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.PlayerDetails;
import core.game.node.entity.player.link.appearance.Gender;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.path.Pathfinder;
import core.game.world.map.zone.impl.WildernessZone;
import core.game.world.repository.Repository;
import core.net.packet.context.MessageContext;
import core.tools.RandomFunction;
import core.tools.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Represents an Artificial Intelligent Player.
 *
 * @author Emperor
 */
public class AIPlayer extends Player {

    private static int currentUID = 0x1;
    private static final List<String> botNames = new ArrayList<String>();

    /**
     * The active Artificial intelligent players mapping.
     */
    private static final Map<Integer, AIPlayer> botMapping = new HashMap<>();

    /**
     * A line of data from namesandarmor.txt that will be used to generate the appearance
     * Data in format: <pre> name:cblevel:helmet:cape:neck:weapon:chest:shield:unknown:legs:unknown:gloves:boots: </pre>
     */
    private static String string;

    /**
     * The AIP's UID.
     */
    private final int uid;

    /**
     * The start location of the AIP.
     */
    private final Location startLocation;

    /**
     * The username.
     */
    private String username;

    /**
     * The player controlling this AIP.
     */
    private Player controller;

    static {
        loadNames("botnames.txt");
    }

    /**
     * Instantiates a new Ai player.
     *
     * @param l the l
     */
    public AIPlayer(Location l) {
        this(getRandomName(), l, null);
    }

    /**
     * Instantiates a new Ai player.
     *
     * @param fileName the file name
     * @param l        the l
     */
    public AIPlayer(String fileName, Location l) {
        this(retrieveRandomName(fileName), l, null);
    }

    @SuppressWarnings("deprecation")
    private AIPlayer(String name, Location l, String ignored) {
        super(new PlayerDetails("/aip" + (currentUID + 1) + ":" + name));
        super.setLocation(startLocation = l);
        super.artificial = true;
        super.getDetails().setSession(ArtificialSession.getSingleton());
        Repository.getPlayers().add(this);
        this.username = StringUtils.formatDisplayName(name + (currentUID + 1));
        this.uid = currentUID++;
        this.updateRandomValues();
        this.init();
    }

    /**
     * Generates bot configuration.
     */
    public void updateRandomValues() {
        this.getAppearance().setGender(RandomFunction.random(5) == 1 ? Gender.FEMALE : Gender.MALE);
        int setTo = RandomFunction.random(0, 10);
        CharacterDesign.randomize(this, true);
        this.setDirection(Direction.values()[new Random().nextInt(Direction.values().length)]);
        this.getSkills().updateCombatLevel();
        this.getAppearance().sync();
    }

    @Override
    public void update() {
        return;
    }

    private void setLevels() {
        int maxLevel = RandomFunction.random(1, Math.min(1, 99));
        for (int i = 0; i < Skills.NUM_SKILLS; i++) {
            this.getSkills().setStaticLevel(i, RandomFunction.linearDecreaseRand(maxLevel));
        }
        int combatLevelsLeft = RandomFunction.random(3, 126);
        int hitpoints = Math.max(RandomFunction.random(10, Math.min(maxLevel, combatLevelsLeft * 4)), 10);
        combatLevelsLeft -= 0.25 * hitpoints;
        int prayer = combatLevelsLeft > 0 ? RandomFunction.random(Math.min(maxLevel, combatLevelsLeft * 8)) : 1;
        combatLevelsLeft -= 0.125 * prayer;
        int defence = combatLevelsLeft > 0 ? RandomFunction.random(Math.min(maxLevel, combatLevelsLeft * 4)) : 1;
        combatLevelsLeft -= 0.25 * defence;

        combatLevelsLeft = Math.min(combatLevelsLeft, 199);

        int attack = combatLevelsLeft > 0 ? RandomFunction.normalRandDist(Math.min(maxLevel, combatLevelsLeft * 3)) : 1;
        int strength = combatLevelsLeft > 0 ? combatLevelsLeft * 3 - attack : 1;

        this.getSkills().setStaticLevel(Skills.HITPOINTS, hitpoints);
        this.getSkills().setStaticLevel(Skills.PRAYER, prayer);
        this.getSkills().setStaticLevel(Skills.DEFENCE, defence);
        this.getSkills().setStaticLevel(Skills.ATTACK, attack);
        this.getSkills().setStaticLevel(Skills.STRENGTH, strength);
        this.getSkills().setStaticLevel(Skills.RANGE, combatLevelsLeft / 2);
        this.getSkills().setStaticLevel(Skills.MAGIC, combatLevelsLeft / 2);
    }

    /**
     * Load names.
     *
     * @param fileName the file name
     */
    public static void loadNames(String fileName) {
        try {
            Scanner sc = new Scanner(new File(ServerConstants.BOT_DATA_PATH + fileName));
            while (sc.hasNextLine()) {
                botNames.add(sc.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets random name.
     *
     * @return the random name
     */
    public static String getRandomName() {
        int index = (RandomFunction.random(botNames.size()));
        String name = botNames.get(index);
        botNames.remove(index);
        return name;
    }

    /**
     * Update..
     *
     * @param fileName the file name
     */
    public static void update(String fileName) {
        Random rand = new Random();
        int n = 0;
        try {
            for (Scanner sc = new Scanner(new File(ServerConstants.BOT_DATA_PATH + fileName)); sc.hasNext(); ) {
                ++n;
                String line = sc.nextLine();
                if (rand.nextInt(n) == 0) {
                    if (line.length() < 3 || line.startsWith("#")) {
                        continue;
                    }
                    string = line;
                }
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    }

    private static String retrieveRandomName(String fileName) {
        do {
            update(fileName);
        } while (string.startsWith("#") || string.contains("_") || string.contains(" "));
        return string.split(":")[0];
    }

    private static String retrieveRandomName() {
        return retrieveRandomName("namesandarmor.txt");
    }

    @Override
    public void init() {
        getProperties().setSpawnLocation(startLocation);
        getInterfaceManager().openDefaultTabs();
        getSession().setObject(this);
        botMapping.put(uid, this);
        super.init();
        getSettings().setRunToggled(true);
        CharacterDesign.randomize(this, false);
        getPlayerFlags().setLastSceneGraph(location);
    }

    /**
     * Follow.
     *
     * @param e the e
     */
    public void follow(final Entity e) {
        getPulseManager().run(new MovementPulse(this, e, DestinationFlag.FOLLOW_ENTITY) {
            @Override
            public boolean pulse() {
                face(e);
                return false;
            }
        }, PulseType.STANDARD);
    }

    /**
     * Random walk around point.
     *
     * @param point  the point
     * @param radius the radius
     */
    public void randomWalkAroundPoint(Location point, int radius) {
        Pathfinder.find(this, point.transform(RandomFunction.random(radius, (radius * -1)), RandomFunction.random(radius, (radius * -1)), 0), true, Pathfinder.SMART).walk(this);
    }

    public void randomWalk(int radiusX, int radiusY) {
        Pathfinder.find(this, this.getLocation().transform(RandomFunction.random(radiusX, (radiusX * -1)), RandomFunction.random(radiusY, (radiusY * -1)), 0), false, Pathfinder.SMART).walk(this);
    }

    /**
     * Walk to pos smart.
     *
     * @param x the x
     * @param y the y
     */
    public void walkToPosSmart(int x, int y) {
        walkToPosSmart(new Location(x, y));
    }

    /**
     * Walk to pos smart.
     *
     * @param loc the loc
     */
    public void walkToPosSmart(Location loc) {
        Pathfinder.find(this, loc, true, Pathfinder.SMART).walk(this);
    }

    /**
     * Walk pos.
     *
     * @param x the x
     * @param y the y
     */
    public void walkPos(int x, int y) {
        Pathfinder.find(this, new Location(x, y));
    }

    /**
     * Check victim is player boolean.
     *
     * @return the boolean
     */
    public boolean checkVictimIsPlayer() {
        if (this.getProperties().getCombatPulse().getVictim() != null)
            if (this.getProperties().getCombatPulse().getVictim().isPlayer()) return true;
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (getSkullManager().isWilderness()) {
            getSkullManager().setLevel(WildernessZone.getWilderness(this));
        }
        if (getSkills().getLifepoints() <= 0) {

        }
    }

    /**
     * Gets item by id.
     *
     * @param id the id
     * @return the item by id
     */
    public Item getItemById(int id) {
        for (int i = 0; i < 28; i++) {
            Item item = this.getInventory().get(i);
            if (item != null) {
                if (item.getId() == id) return item;
            }
        }
        return null;
    }

    /**
     * Handle incoming chat.
     *
     * @param ctx the ctx
     */
    public void handleIncomingChat(MessageContext ctx) {
    }

    private ArrayList<Node> getNodeInRange(int range, int entry) {
        int meX = this.getLocation().getX();
        int meY = this.getLocation().getY();
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (NPC npc : RegionManager.getLocalNpcs(this, range)) {
            if (npc.getId() == entry) nodes.add(npc);
        }
        for (int x = 0; x < range; x++) {
            for (int y = 0; y < range - x; y++) {
                Node node = RegionManager.getObject(0, meX + x, meY + y);
                if (node != null) if (node.getId() == entry) nodes.add(node);
                Node node2 = RegionManager.getObject(0, meX + x, meY - y);
                if (node2 != null) if (node2.getId() == entry) nodes.add(node2);
                Node node3 = RegionManager.getObject(0, meX - x, meY + y);
                if (node3 != null) if (node3.getId() == entry) nodes.add(node3);
                Node node4 = RegionManager.getObject(0, meX - x, meY - y);
                if (node4 != null) if (node4.getId() == entry) nodes.add(node4);
            }
        }
        return nodes;
    }

    private ArrayList<Node> getNodeInRange(int range, List<Integer> entrys) {
        int meX = this.getLocation().getX();
        int meY = this.getLocation().getY();

        ArrayList<Node> nodes = new ArrayList<Node>();
        for (NPC npc : RegionManager.getLocalNpcs(this, range)) {
            if (entrys.contains(npc.getId())) nodes.add(npc);
        }
        for (int x = 0; x < range; x++) {
            for (int y = 0; y < range - x; y++) {
                Node node = RegionManager.getObject(0, meX + x, meY + y);
                if (node != null) if (entrys.contains(node.getId())) nodes.add(node);
                Node node2 = RegionManager.getObject(0, meX + x, meY - y);
                if (node2 != null) if (entrys.contains(node2.getId())) nodes.add(node2);
                Node node3 = RegionManager.getObject(0, meX - x, meY + y);
                if (node3 != null) if (entrys.contains(node3.getId())) nodes.add(node3);
                Node node4 = RegionManager.getObject(0, meX - x, meY - y);
                if (node4 != null) if (entrys.contains(node4.getId())) nodes.add(node4);
            }
        }
        return nodes;
    }

    /**
     * Gets closest node with entry and direction.
     *
     * @param range     the range
     * @param entry     the entry
     * @param direction the direction
     * @return the closest node with entry and direction
     */
    public Node getClosestNodeWithEntryAndDirection(int range, int entry, Direction direction) {
        ArrayList<Node> nodeList = getNodeInRange(range, entry);
        if (nodeList.isEmpty()) {

            return null;
        }
        Node node = getClosestNodeinNodeListWithDirection(nodeList, direction);
        return node;
    }

    /**
     * Gets closest node with entry.
     *
     * @param range the range
     * @param entry the entry
     * @return the closest node with entry
     */
    public Node getClosestNodeWithEntry(int range, int entry) {
        ArrayList<Node> nodeList = getNodeInRange(range, entry);
        if (nodeList.isEmpty()) {

            return null;
        }
        Node node = getClosestNodeinNodeList(nodeList);
        return node;
    }

    /**
     * Gets closest node with entry.
     *
     * @param range  the range
     * @param entrys the entrys
     * @return the closest node with entry
     */
    public Node getClosestNodeWithEntry(int range, List<Integer> entrys) {
        ArrayList<Node> nodeList = getNodeInRange(range, entrys);
        if (nodeList.isEmpty()) {

            return null;
        }
        Node node = getClosestNodeinNodeList(nodeList);
        return node;
    }

    /**
     * Gets closes creature.
     *
     * @param radius the radius
     * @return the closes creature
     */
    public Node getClosesCreature(int radius) {
        int distance = radius + 1;
        Node npcReturn = null;
        for (NPC npc : RegionManager.getLocalNpcs(this, radius)) {
            double distanceToNpc = npc.getLocation().getDistance(this.getLocation());
            if ((distanceToNpc) < distance) {
                distance = (int) distanceToNpc;
                npcReturn = npc;
            }
        }
        return npcReturn;
    }

    /**
     * Gets closes creature.
     *
     * @param radius the radius
     * @param entry  the entry
     * @return the closes creature
     */
    public Node getClosesCreature(int radius, int entry) {
        int distance = radius + 1;
        Node npcReturn = null;
        for (NPC npc : RegionManager.getLocalNpcs(this, radius)) {
            double distanceToNpc = npc.getLocation().getDistance(this.getLocation());
            if (npc.getId() == entry) {
                if ((distanceToNpc) < distance) {
                    distance = (int) distanceToNpc;
                    npcReturn = npc;
                }
            }
        }
        return npcReturn;
    }

    /**
     * Gets closes creature.
     *
     * @param radius the radius
     * @param entrys the entrys
     * @return the closes creature
     */
    public Node getClosesCreature(int radius, ArrayList<Integer> entrys) {
        int distance = radius + 1;
        Node npcReturn = null;
        for (NPC npc : RegionManager.getLocalNpcs(this, radius)) {
            double distanceToNpc = npc.getLocation().getDistance(this.getLocation());
            if (entrys.contains(npc.getId())) {
                if ((distanceToNpc) < distance) {
                    distance = (int) distanceToNpc;
                    npcReturn = npc;
                }
            }
        }
        return npcReturn;
    }

    private Node getClosestNodeinNodeListWithDirection(ArrayList<Node> nodes, Direction direction) {
        if (nodes.isEmpty()) {

            return null;
        }

        double distance = 0;
        Node nodeReturn = null;
        for (Node node : nodes) {
            double nodeDistance = this.getLocation().getDistance(node.getLocation());
            if ((nodeReturn == null || nodeDistance < distance) && node.getDirection() == direction) {
                distance = nodeDistance;
                nodeReturn = node;
            }
        }
        return nodeReturn;
    }

    private Node getClosestNodeinNodeList(ArrayList<Node> nodes) {
        if (nodes.isEmpty()) {

            return null;
        }

        double distance = 0;
        Node nodeReturn = null;
        for (Node node : nodes) {
            double nodeDistance = this.getLocation().getDistance(node.getLocation());
            if (nodeReturn == null || nodeDistance < distance) {
                distance = nodeDistance;
                nodeReturn = node;
            }
        }
        return nodeReturn;
    }

    @Override
    public void clear() {
        botMapping.remove(uid);
        super.clear();
    }

    @Override
    public void reset() {
        if (getPlayerFlags().isUpdateSceneGraph()) {
            getPlayerFlags().setLastSceneGraph(getLocation());
        }
        super.reset();
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        fullRestore();
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public int getUid() {
        return uid;
    }

    /**
     * Deregister.
     *
     * @param uid the uid
     */
    public static void deregister(int uid) {
        AIPlayer player = botMapping.get(uid);
        if (player != null) {
            player.clear();
            Repository.getPlayers().remove(player);
            return;
        }
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Get ai player.
     *
     * @param uid the uid
     * @return the ai player
     */
    public static AIPlayer get(int uid) {
        return botMapping.get(uid);
    }

    /**
     * Gets start location.
     *
     * @return the start location
     */
    public Location getStartLocation() {
        return startLocation;
    }

    /**
     * Gets controller.
     *
     * @return the controller
     */
    public Player getController() {
        return controller;
    }

    /**
     * Sets controller.
     *
     * @param controller the controller
     */
    public void setController(Player controller) {
        this.controller = controller;
    }


    /**
     * Interact.
     *
     * @param n the n
     */
    public void interact(Node n) {
    }
}